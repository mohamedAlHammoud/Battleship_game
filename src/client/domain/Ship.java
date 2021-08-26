package client.domain;

import javax.swing.ImageIcon;

import client.gui.PlayAreaCell;

public class Ship {
	private PlayAreaCell[] cells;

	private String name;

	private int length;
	private String shipIconURL;

	private boolean isPlaced = false;

	// CONSTRUCTOR
	// ----------------------------------------

	/**
	 * Constructor for Ship
	 * 
	 * @param name   the Name of the ship
	 * @param length the number of cells this ship occupies
	 */
	public Ship(String name, int length, String shipIconURL) {
		setName(name);
		setLength(length);
		setShipLogo(shipIconURL);
	}

	public void setShipLogo(String shipIconURL) {
		this.shipIconURL = shipIconURL;
	}

	public String getShipLogo() {
		return this.shipIconURL;
	}

	// NAME
	// ----------------------------------------

	/**
	 * Assigns the name of the Ship
	 * 
	 * @param name the name to assign
	 */
	private void setName(String name) {
		this.name = name;
	}

	// --------------------

	/**
	 * Gets the name of the Ship
	 * 
	 * @return the name of the Ship
	 */
	public String getName() {
		return name;
	}

	// LENGTH
	// ----------------------------------------

	/**
	 * Assigns the length of the ship
	 * 
	 * @param length how many cells this Ship occupies
	 */
	private void setLength(int length) {
		this.length = length;
	}

	// --------------------

	/**
	 * Gets the length of the Ship
	 * 
	 * @return the number of cells this Ship occupies
	 */
	public int getLength() {
		return length;
	}

	// CELLS
	// ----------------------------------------

	/**
	 * Assigns the cells this Ship currently occupies
	 * 
	 * @param cells the cells this Ship currently occupies
	 */
	public void setCells(PlayAreaCell[] cells) {
		this.cells = cells;
	}

	// --------------------

	/**
	 * Gets the cells this Ship currently occupies
	 * 
	 * @return the cells this Ship currently occupies
	 */
	public PlayAreaCell[] getCells() {
		return cells;
	}

	// IS PLACED
	// ----------------------------------------

	/**
	 * Sets whether the ship is placed on the board
	 * 
	 * @param isPlaced whether the ship is placed on the board
	 */
	public void setPlaced(boolean isPlaced) {
		this.isPlaced = isPlaced;

		if (!isPlaced && cells != null) {
			clearCells();
		}
	}

	// --------------------

	/**
	 * Gets whether the ship is placed on the board
	 * 
	 * @return returns true if the ship has been placed on the board
	 */
	public boolean isPlaced() {
		return isPlaced;
	}

	private void clearCells() {
		for (PlayAreaCell cell : cells) {
			cell.setShip(false);
		}
	}
}
