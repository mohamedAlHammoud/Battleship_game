package server.domain;

import java.nio.ByteBuffer;

import server.gui.MainWindow;

public class Match extends Observer implements Runnable
{
	public enum Phase
	{
		SHIP_PLACEMENT,
		PLAYER_TURNS,
		GAME_ENDED
	}

	private static final short PLAYER1 = 0, PLAYER2 = 1, REMATCH = 1, QUIT = 2;

	private Client[] players = new Client[2];

	private PlayerBoard[] boards = new PlayerBoard[2];

	private Phase currentPhase;

	private short currentPlayer, waitingPlayer, winningPlayer, losingPlayer, player1Rematch, player2Rematch;

	private boolean gameIsRunning, matchIsRunning;

	private MainWindow window;

	public Match(Client player1, Client player2, MainWindow window)
	{
		player1.addObserver(this);
		player2.addObserver(this);
		players[PLAYER1] = player1;
		players[PLAYER2] = player2;

		this.window = window;

		matchIsRunning = true;
		
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		while(matchIsRunning)
		{
			window.outputMessage("Match " + players[PLAYER1].getId() + "-" + players[PLAYER2].getId() + " started");
			getShipPlacements();

			window.outputMessage("Match " + players[PLAYER1].getId() + "-" + players[PLAYER2].getId() + " has begun player turn phase");
			getPlayerShots();
			getRematch();
		}
		window.outputMessage("Match " + players[PLAYER1].getId() + "-" + players[PLAYER2].getId() + " is terminating");
		endMatch();
	}

	private void endMatch()
	{
		if(player1Rematch == REMATCH)
		{
			players[PLAYER1].setLookingForMatch(true);	
			players[PLAYER1].removeObserver(this);
		}
		else
		{
			players[PLAYER2].setLookingForMatch(true);
			players[PLAYER2].removeObserver(this);
		}
		
		players[PLAYER1].removeObserver(this);
	}

	private void getShipPlacements()
	{
		currentPhase = Phase.SHIP_PLACEMENT;

		try
		{
			Thread.sleep(300);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Client player : players)
		{
			player.tryWriteToClient(NC.SHIP_PLACEMENT);
		}
		while (boards[PLAYER1] == null || boards[PLAYER2] == null)
		{
			tryWait();
		}

	}

	private void getPlayerShots()
	{
		currentPhase = Phase.PLAYER_TURNS;

		gameIsRunning = true;
		currentPlayer = PLAYER1;
		waitingPlayer = PLAYER2;

		while (gameIsRunning)
		{
			players[currentPlayer].tryWriteToClient(NC.CLIENT_TURN);
			tryWait();

			// ----------

			ByteBuffer buffer = ByteBuffer.allocate(1 + 100);
			buffer.put(NC.PLAYER_BOARD);
			buffer.put(boards[currentPlayer].getCellStateData());

			players[currentPlayer].tryWriteToClient(buffer.array());

			buffer = ByteBuffer.allocate(1 + 100);
			buffer.put(NC.OPPONENT_BOARD);
			buffer.put(boards[waitingPlayer].getCellStateData());

			players[currentPlayer].tryWriteToClient(buffer.array());

			// ----------

			buffer = ByteBuffer.allocate(1 + 100);
			buffer.put(NC.PLAYER_BOARD);
			buffer.put(boards[waitingPlayer].getCellStateData());

			players[waitingPlayer].tryWriteToClient(buffer.array());

			buffer = ByteBuffer.allocate(1 + 100);
			buffer.put(NC.OPPONENT_BOARD);
			buffer.put(boards[currentPlayer].getCellStateData());

			players[waitingPlayer].tryWriteToClient(buffer.array());

			// ----------

			checkGameEnded();

			short temp = currentPlayer;
			currentPlayer = waitingPlayer;
			waitingPlayer = temp;
		}
	}

	private synchronized void getRematch()
	{
		players[winningPlayer].tryWriteToClient(NC.CLIENT_WON);
		players[losingPlayer].tryWriteToClient(NC.CLIENT_LOST);

		while(player1Rematch == 0 || player2Rematch == 0)
		{
			tryWait();
		}
		
		if(player1Rematch == REMATCH && player2Rematch == REMATCH)
		{
			initRematch();
			window.outputMessage("Match " + players[PLAYER1].getId() + "-" + players[PLAYER2].getId() + " has restarted");
		}
		else
		{
			initRematch();
			matchIsRunning = false;
			
			if(player1Rematch == QUIT)
			{
				players[PLAYER1].disconnectClient();
			}
			else
			{
				players[PLAYER2].disconnectClient();
			}
		}
	}

	private void initRematch()
	{
		for (PlayerBoard playerBoard : boards)
		{
			playerBoard.reset();
		}
		
		ByteBuffer pbuffer = ByteBuffer.allocate(1 + 100),
				obuffer = ByteBuffer.allocate(1 + 100);
		
		pbuffer.put(NC.PLAYER_BOARD);
		pbuffer.put(boards[0].getCellStateData());
		
		obuffer.put(NC.OPPONENT_BOARD);
		obuffer.put(boards[1].getCellStateData());
		
		for (Client player : players)
		{
			player.tryWriteToClient(NC.RESET_SHIPS);
			player.tryWriteToClient(pbuffer.array());
			player.tryWriteToClient(obuffer.array());
		}
		
		boards[0] = null;
		boards[1] = null;
	}

	@Override
	public void update()
	{
		;
	}

	@Override
	public void update(Subject<?> subject)
	{
		parseMessage((Client) subject);
	}

	private void parseMessage(Client client)
	{
		ByteBuffer buffer = client.getState();
		buffer.rewind();
		if (buffer.get() == NC.CHAT_MESSAGE)
		{
			buffer.rewind();
			if (client.equals(players[PLAYER1]))
			{
				players[PLAYER2].tryWriteToClient(buffer.array());
			} else
			{
				players[PLAYER1].tryWriteToClient(buffer.array());
			}
		} else
		{
			switch (currentPhase)
			{
				case SHIP_PLACEMENT:
					setShipPlacement(client);
					break;
				case PLAYER_TURNS:
					setPlayerShot(client);
					break;
				case GAME_ENDED:
					getPlayerRematch(client);
					break;					
				default:
					break;
			}
		}
	}

	private synchronized void getPlayerRematch(Client client)
	{
		int player;
		if(client.equals(players[PLAYER1]))
		{
			player = PLAYER1;
		}
		else
		{
			player = PLAYER2;
		}
		
		ByteBuffer message = client.getState();
		message.rewind();
		
		if(message.get() == NC.REMATCH)
		{
			short rematch = (short) message.get();
			
			
			if(player == PLAYER1)
			{
				player1Rematch = rematch;
			}
			else
			{
				player2Rematch = rematch;
			}
			notify();
		}
	}

	private synchronized void setShipPlacement(Client client)
	{
		int player;

		if (client.equals(players[PLAYER1]))
		{
			player = PLAYER1;
		} else
		{
			player = PLAYER2;
		}

		ByteBuffer message = client.getState();
		message.rewind();

		if (message.get() == NC.SHIP_PLACEMENT)
		{
			byte[][] shipLocations = new byte[17][2];

			for (int i = 0; i < 17; i++)
			{
				for (int j = 0; j < 2; j++)
				{
					shipLocations[i][j] = message.get();
				}
			}

			boards[player] = new PlayerBoard(shipLocations);
			window.outputMessage("Player " + (player + 1) + " has placed ships");

			notify();
		} else
		{
			clientError(client, NC.SHIP_PLACEMENT);
		}
	}

	private synchronized void setPlayerShot(Client client)
	{
		if (client.equals(players[currentPlayer]))
		{
			ByteBuffer message = client.getState();
			message.rewind();

			if (message.get() == NC.CLIENT_SHOT)
			{
				boards[waitingPlayer].shotTaken(message.get(), message.get());
				notify();
			} else
			{
//				clientError(client, NC.CLIENT_TURN);
			}
		} else
		{
//			clientError(client, NC.WRONG_TURN);
		}
	}

	private void checkGameEnded()
	{
		if (boards[waitingPlayer].noShipsLeft())
		{
			currentPhase = Phase.GAME_ENDED;
			
			winningPlayer = currentPlayer;

			if (winningPlayer == 0)
			{
				losingPlayer = 1;
			} else
			{
				losingPlayer = 0;
			}

			gameIsRunning = false;
			window.outputMessage("Match " + players[PLAYER1].getId() + "-" + players[PLAYER2].getId() + " has ended with player "
					+ (winningPlayer + 1) + " winning");
			
			for (Client player : players)
			{
				player.tryWriteToClient(NC.CLIENT_WAIT);
			}
		}
	}

	public synchronized void tryWait()
	{
		try
		{
			wait();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void clientError(Client player, byte step)
	{
		player.tryWriteToClient(new byte[]
		{ NC.ERROR, step });
	}
}
