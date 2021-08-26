package server.domain;

import java.util.Arrays;

public class PlayerBoard
{
	private static final
	byte NONE = 0,
	 	 MISS = 1,
		 HIT = 2;

	private static final
	short TOTAL_SHIP_CELLS = 17,
		  GRID_SIZE = 10;
	
	private byte[][] shipCells = new byte[TOTAL_SHIP_CELLS][2];
	
	private byte[][] cellStates = new byte[GRID_SIZE][GRID_SIZE];
	
	private short numOfHits = 0;
	
	public PlayerBoard(byte[][] shipLocations)
	{
		this.shipCells = shipLocations;
		
		
		for (byte[] stateArray : cellStates)
		{
			Arrays.fill(stateArray, NONE);
		}
	}
	
	public void shotTaken(int posX, int posY)
	{
		if(cellStates[posX][posY] == NONE)
		{
			byte shot = MISS;
			for(int i=0; i<TOTAL_SHIP_CELLS; i++)
			{
				if(shipCells[i][0] == posX && shipCells[i][1] == posY)
				{
					shot = HIT;
					numOfHits++;
				}
			}
			
			cellStates[posX][posY] = shot;
		}
	}
	
	public byte[] getCellStateData()
	{
		byte[] data = new byte[100];
		
		int counter = 0;
		for(int i=0; i<GRID_SIZE; i++)
		{
			for(int j=0; j<GRID_SIZE; j++)
			{
				data[counter++] = cellStates[i][j];
			}
		}
		
		return data;
	}
	
	public boolean noShipsLeft()
	{
		return (numOfHits == TOTAL_SHIP_CELLS);
	}
	
	public void reset()
	{
		for (byte[] stateArray : cellStates)
		{
			Arrays.fill(stateArray, NONE);
		}
		
		for (byte[] ship : shipCells)
		{
			Arrays.fill(ship, NONE);
		}
	}
}
