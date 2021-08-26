package server.domain;

import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import server.gui.MainWindow;

public class ClientManager extends Observer
{
	ConnectionManager connections = new ConnectionManager();
	LinkedList<Client> clients = new LinkedList<Client>();
	
	private MainWindow window;

	// CONSTRUCTOR
	// ----------------------------------------

	/**
	 * Constructor for ClientManager Creates a ConnectionManager and sets itself as
	 * an Observer
	 * @param window 
	 */
	public ClientManager(MainWindow window)
	{
		this.window = window;
		setSubject(connections);
		connections.addObserver(this);
	}

	// UPDATE
	// ----------------------------------------

	/**
	 * Overrides the update method Gets a new Client from the ConnectionManager and
	 * adds it to clients
	 */

	@Override
	public void update()
	{
		add(connections.getState(), getNextId());
	}

	// UPDATE
	// ----------------------------------------

	/**
	 * Creates and adds a new client to clients
	 * @param socket the Socket of the new client
	 * @param id     the ID of the new client
	 */
	private void add(Socket socket, short id)
	{
		try
		{
			clients.add(new Client(socket, id, this));
//			matchPlayers();
		} catch (SocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void remove(Client client)
	{
		clients.remove(client);
	}

	// UPDATE
	// ----------------------------------------

	/**
	 * Finds the next available ID
	 * @return returns the next available ID. returns 0 if no id found
	 */
	private short getNextId()
	{
		boolean idFound = false;
		short counter = 1;

		while (!idFound && counter < Short.MAX_VALUE)
		{
			boolean isValid = true;
			for (Client client : clients)
			{
				if (client.getId() == counter)
				{
					isValid = false;
				}
			}

			if(isValid)
			{
				idFound = true;
			} else
			{
				counter++;
			}
		}

		if (idFound)
		{
			return counter;
		} else
		{
			return NC.ERROR;
		}
	}

	// CLIENT COUNT
	// ----------------------------------------

	/**
	 * Gets the number of Clients
	 * @return the number of Clients
	 */
	public int getClientCount()
	{
		return clients.size();
	}

	// MATCH PLAYERS
	// ----------------------------------------

	/**
	 * Looks for 2 players ready for a match NOT COMPLETE
	 */
	public void matchPlayers()
	{
		Client client;
		Client[] matchedClients = new Client[2];
		int counter = 0, clientCounter = 0;

		while ((clientCounter < 2) && (counter < clients.size()))
		{
			client = clients.get(counter++);

			if (client.isLookingForMatch())
			{
				matchedClients[clientCounter++] = client;
			}
		}
		
		if(clientCounter == 2)
		{
			Match match = new Match(matchedClients[0], matchedClients[1], window);
		}
	}

	@Override
	public void update(Subject<?> subject)
	{
		;
	}
}