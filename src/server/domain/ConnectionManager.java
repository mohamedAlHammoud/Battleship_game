package server.domain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager extends Subject<Socket>
{
	private static final int SERVER_PORT = 9992;

	Thread thread;

	private ServerSocket server;

	// CONSTRUCTOR
	// ----------------------------------------

	/**
	 * Constructor for Connection Manager Starts thread immediately
	 */
	public ConnectionManager()
	{
		initThread();
	}

	// RUN
	// ----------------------------------------

	/**
	 * Overrides the run method Initializes server socket and adds new clients
	 */
	@Override
	public void run()
	{
		try
		{
			server = new ServerSocket(SERVER_PORT);

			while (!Thread.currentThread().isInterrupted())
			{
				addNewClients();
			}
		} catch (IOException e)
		{
			System.out.println("server socket error!!!");
			e.printStackTrace();
		}
	}

	// ADD NEW CLIENTS
	// ----------------------------------------

	/**
	 * Waits for client connections, creates a new Client, notifies ClientManager
	 */
	private void addNewClients()
	{
		try
		{
			state = server.accept();

			notifyObservers();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}