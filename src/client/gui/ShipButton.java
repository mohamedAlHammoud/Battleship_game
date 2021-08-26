package client.gui;

import java.awt.Dimension;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import client.domain.Ship;

public class ShipButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// INSTANCE FIELDS
	// ----------------------------------------

	private Ship ship;

	private Board board;

	// CONSTRUCTOR
	// ----------------------------------------

	/**
	 * Constructor for ShipButton
	 * 
	 * @param ship  the Ship this ShipButton represents
	 * @param board the Board this ShipButton is assigned to
	 */
	public ShipButton(Ship ship, Board board) {
		setShip(ship);
		setBoard(board);
		try {
			// Icon icon = new ImageIcon(ship.getShipLogo());
//			this.setIcon(icon);
			// could not load the icon inside the button
			this.setIcon(new ImageIcon(ship.getShipLogo()));
			this.setPreferredSize(new Dimension(100, 30));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// SHIP
	// ----------------------------------------

	/**
	 * Assigns the Ship this ShipButton represents
	 * 
	 * @param ship the Ship this ShipButton represents
	 */
	private void setShip(Ship ship) {
		this.ship = ship;

		setText(ship.getName());

	}

	// --------------------

	/**
	 * Gets the Ship the ShipButton represents
	 * 
	 * @return the Ship the ShipButton represents
	 */
	public Ship getShip() {
		return ship;
	}

	// BOARD
	// ----------------------------------------

	/**
	 * Assigns the Board this ShipButton is assigned to
	 * 
	 * @param board the Board this ShipButton is assigned to
	 */
	private void setBoard(Board board) {
		this.board = board;
	}

	// CLICKED
	// ----------------------------------------

	/**
	 * Sets the Boards current Ship
	 */
	public void buttonClicked() {
		ship.setPlaced(false);
		board.setShip(ship);
	}
}
