package client.domain;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

import client.gui.GameBoardView;
import server.domain.NC;
import server.domain.Subject;

public class ConnectionManager extends Subject<ByteBuffer>
{
	private Socket socket;
	
	private DataInputStream inputStream;
	
	private DataOutputStream outputStream;
	
	private GameBoardView view;
	
	private boolean serverConnected;
	
	public ConnectionManager(Socket socket)
	{
		setSocket(socket);
		serverConnected = true;
		initThread();
	}
	
	public void setGameBoardView(GameBoardView view)
	{
		this.view = view;
	}
	
	@Override
	public void run()
	{
		while(serverConnected)
		{
			int length = tryReadLength();
			ByteBuffer buffer = ByteBuffer.wrap(tryReadDataStream(length));
			
			if(length > NC.ERROR)
			{
				buffer.rewind();
				switch (buffer.get())
				{
					case NC.SHIP_PLACEMENT:
						view.setShipPlacement(true);
						break;
						
					case NC.CLIENT_TURN:
						view.setShotTaking(true);
						break;
						
					case NC.CLIENT_WAIT:
						view.setShotTaking(false);
						break;
						
					case NC.PLAYER_BOARD:
						byte[] pboard = new byte[buffer.remaining()];
						buffer.get(pboard, 0, buffer.remaining());
						view.setPlayerBoard(pboard);
						break;
						
					case NC.OPPONENT_BOARD:
						byte[] oboard = new byte[buffer.remaining()];
						buffer.get(oboard, 0, buffer.remaining());
						view.setOpponentBoard(oboard);
						break;
						
					case NC.CHAT_MESSAGE:
						byte[] message = new byte[buffer.remaining()];
						
						buffer.get(message, 0, buffer.remaining());
						view.recieveChatMessage(message);
						break;
						
					case NC.RESET_SHIPS:
						view.resetShips();
						break;
						
					case NC.CLIENT_LOST:
						view.rematch(false);
						break;
						
					case NC.CLIENT_WON:
						view.rematch(true);
						break;
				}
			}
		}
	}
	
	private void setSocket(Socket socket)
	{
		this.socket = socket;
		
		try
		{
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	// DATA STREAMS
	// ----------------------------------------

	public boolean tryWriteToServer(byte[] message)
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
	
	public boolean tryWriteToServer(byte message)
	{
		return tryWriteToServer(new byte[] { message });
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
			return inputStream.readInt();
		} catch (IOException e)
		{
			// Adds 1s delay so console isn't spammed
			//e.printStackTrace();
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return NC.ERROR;
	}

	// --------------------

	/**
	 * Attempts to write the length of the outgoing message to the client
	 * @param length the length of the message about to be sent
	 * @return returns true if length successfuly sent, false if not
	 */
	private boolean tryWriteLength(int length)
	{
		try
		{
			outputStream.writeInt(length);
			return true;
		} catch (IOException e)
		{
			//e.printStackTrace();
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
			inputStream.readFully(bytes, 0, length);
			return bytes;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return new byte[]
		{ NC.ERROR };
	}

	// ----------------------------------------

	/**
	 * Tries to write data stream to the client
	 * @param data the data to write
	 * @return returns true if data successfully written
	 */
	private boolean tryWriteDataStream(byte[] data)
	{
		try
		{
			outputStream.write(data);
			outputStream.flush();

			return true;
		} catch (IOException e)
		{
			//e.printStackTrace();
		} catch (NullPointerException e)
		{
			//e.printStackTrace();
		}

		return false;
	}

	// ----------------------------------------

	/**
	 * Attempts to close the connection to the client
	 */
	private void closeConnections()
	{
		try
		{
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e)
		{
			//e.printStackTrace();
		}
	}
	
	public void tryCloseConnection()
	{
		tryWriteToServer(NC.END_SESSION);
		
		closeConnections();
	}
}
