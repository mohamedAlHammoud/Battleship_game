package server.domain;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;

public class Client extends Subject<ByteBuffer>
{
	private static final String SOCKET_CLOSED_ERROR = "ERROR: socket is closed!",
			SOCKET_NULL_ERROR = "ERROR: socket is null!";

	private ClientManager manager;

	private Socket socket;

	private DataInputStream clientInput;

	private DataOutputStream clientOutput;

	private String name;

	private short id;

	private boolean lookingForMatch = false, clientConnected = true;

	// CONSTRUCTOR
	// ----------------------------------------

	/**
	 * Constructor for Client
	 * @param clientSocket the socket the client is connected through
	 * @throws SocketException thrown if socket is null or closed
	 */
	public Client(Socket clientSocket, short id, ClientManager manager) throws SocketException
	{
		setSocket(clientSocket);
		setId(id);
		setManager(manager);
		initThread();
	}

	// RUN
	// ----------------------------------------

	/**
	 * Overrides the run method While the client is connected, waits for input from
	 * the client socket and responds
	 */
	@Override
	public void run()
	{
		if (id > NC.ERROR)
		{
			setLookingForMatch(true);
			while (clientConnected)
			{
				// Get length of client message
				int length = tryReadLength();

				if (length > NC.ERROR)
				{
					ByteBuffer buffer = ByteBuffer.wrap(tryReadDataStream(length));

					// **NOT COMPLETE**
					// Determines action based on first byte
					switch (buffer.get())
					{
//						case NC.SHIP_PLACEMENT:
//							//buffer.putShort(0, id);
//							state = buffer;
//							notifyObservers();
//							break;

						case NC.SET_NAME:
							byte[] name = new byte[buffer.remaining()];
							buffer.get(name, 0, buffer.remaining());
							setName(new String(name));
							break;

						case NC.END_SESSION:
							clientConnected = false;
							break;

						default:
							state = buffer;
							notifyObservers();

					}
				}
			}
			disconnectClient();
		}
		// TODO: disconnect client
		else
		{
			disconnectClient();
		}
	}

	// SET SOCKET
	// ----------------------------------------

	/**
	 * Sets the client's socket and creates input and output streams
	 * @param clientSocket the Socket the client is connected through
	 * @throws SocketException thrown if socket is null or closed
	 */
	private void setSocket(Socket clientSocket) throws SocketException
	{
		try
		{
			if (clientSocket == null)
			{
				throw new SocketException(SOCKET_NULL_ERROR);
			} else if (clientSocket.isClosed())
			{
				throw new SocketException(SOCKET_CLOSED_ERROR);
			} else
			{
				socket = clientSocket;

				setInputStream(new DataInputStream(socket.getInputStream()));
				setOutpuStream(new DataOutputStream(socket.getOutputStream()));
			}
		} catch (IOException e)
		{
			disconnectClient();
		}
	}

	// ID
	// ----------------------------------------

	/**
	 * Assigns the clients ID
	 * @param id the ID to assign
	 */
	private void setId(short id)
	{
		this.id = id;
	}

	// --------------------

	/**
	 * Gets the clients ID
	 * @return the ID of the client
	 */
	public short getId()
	{
		return id;
	}

	// MANAGER
	// ----------------------------------------

	/**
	 * Assigns the ClientManager for this client
	 * @param manager the ClientManager for this client
	 */
	private void setManager(ClientManager manager)
	{
		this.manager = manager;
	}

	// ----------------------------------------

//	public boolean hasUnreadMessage()
//	{
//		return unreadMessage;
//	}

	// INPUT-STREAM
	// ----------------------------------------

	/**
	 * Assigns the clients DataInputStream
	 * @param inputStream the DataInputStream to assign
	 */
	private void setInputStream(DataInputStream inputStream)
	{
		this.clientInput = inputStream;
	}

	// --------------------

	/**
	 * Gets the clients DataInputStream
	 * @return the clients DataInputStream
	 */
	public DataInputStream getInputStream()
	{
		return clientInput;
	}

	// OUTPUT-STREAM
	// ----------------------------------------

	/**
	 * Assigns the clients DataOutputStream
	 * @param outputStream the DataOutputStream to assign
	 */
	private void setOutpuStream(DataOutputStream outputStream)
	{
		this.clientOutput = outputStream;
	}

	// --------------------

	/**
	 * Gets the clients DataOutputStream
	 * @return the clients DataOutputStream
	 */
	public DataOutputStream getOutputStream()
	{
		return clientOutput;
	}

	// NAME
	// ----------------------------------------

	/**
	 * Sets the clients name
	 * @param name the name of the client
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	// --------------------

	/**
	 * Gets the clients name
	 * @return the clients name
	 */
	public String getName()
	{
		return name;
	}

	// LOOKING FOR MATCH
	// ----------------------------------------

	/**
	 * Sets whether or not the player is looking for a match
	 * @param lookingForMatch whether or not the player is looking for a match
	 */
	void setLookingForMatch(boolean lookingForMatch)
	{
		this.lookingForMatch = lookingForMatch;
		if(lookingForMatch)
		{
			manager.matchPlayers();
		}
	}

	// --------------------

	/**
	 * Gets whether or not the player is looking for a match
	 * @return whether or not the player is looking for a match
	 */
	public boolean isLookingForMatch()
	{
		return lookingForMatch;
	}

	// DATA STREAMS
	// ----------------------------------------

	public boolean tryWriteToClient(byte[] message)
	{
		if (tryWriteLength(message.length))
		{
			if (tryWriteDataStream(message))
			{
				return true;
			}

		}

		return false;
	}

	// --------------------

	public boolean tryWriteToClient(byte message)
	{
		return tryWriteToClient(new byte[]
		{ message });
	}

	// ----------------------------------------

	/**
	 * Tries to read the length of the incoming message Returns 0 if an error is
	 * encountered
	 * @return the length of the incoming message
	 */
	private int tryReadLength()
	{
		try
		{
			return clientInput.readInt();

		} catch (Exception e)
		{
			closeConnections();
		}

		return 0;
	}

	// --------------------

	/**
	 * Attempts to write the length of the outgoing message to the client
	 * @param length the length of the message about to be sent
	 * @return returns true if length successfully sent, false if not
	 */
	private boolean tryWriteLength(int length)
	{
		try
		{
			clientOutput.writeInt(length);
			return true;
		} catch (IOException e)
		{
			closeConnections();
			e.printStackTrace();
			return false;
		}
	}

	// ----------------------------------------

	/**
	 * Tries to read the incoming message
	 * @param length the number of bytes to read
	 * @return the incoming message. returns NC.ERROR code if an error is
	 *         encountered
	 */
	private byte[] tryReadDataStream(int length)
	{
		try
		{
			byte[] bytes = new byte[length];
			clientInput.readFully(bytes, 0, length);
			return bytes;
		} catch (IOException e)
		{
			closeConnections();
			e.printStackTrace();
		}

		return new byte[]
		{ NC.ERROR };
	}

	// ----------------------------------------

	/**
	 * Tries to write data stream to the client
	 * 
	 * @param data the data to write
	 * @return returns true if data successfully written
	 */
	private boolean tryWriteDataStream(byte[] data) {
		try {
			clientOutput.write(data);
			clientOutput.flush();

			return true;
		} catch (IOException e)
		{
			e.printStackTrace();
			closeConnections();
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			closeConnections();
		}

		return false;
	}

	// ----------------------------------------

	/**
	 * Attempts to close the connection to the client
	 */
	public void closeConnections()
	{
		try
		{
			clientInput.close();
			clientOutput.close();
			socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void notifyObservers()
	{
		for (Observer observer : observers)
		{
			observer.update(this);
		}
	}

	@Override
	public boolean equals(Object objectToCompare)
	{
		if (objectToCompare instanceof Client)
		{
			return ((Client) objectToCompare).getId() == this.getId();
		}

		return false;
	}

	public void disconnectClient()
	{
		clientConnected = false;
//		tryWriteDataStream(new byte[] { NC.END_SESSION });
		closeConnections();
		manager.remove(this);
	}
}