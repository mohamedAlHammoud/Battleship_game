package client.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import client.domain.PlayAreaOverlayColor;

public class PlayAreaCell extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// CLASS FIELDS
	// ----------------------------------------

	private static final int CELL_SIZE = 45, MARKED_TRANSPARENCY = 100;

	private static final Color OVERLAY_RED = new Color(255, 0, 0, MARKED_TRANSPARENCY),
			OVERLAY_GREEN = new Color(0, 255, 0, MARKED_TRANSPARENCY), SHIP_GREY = new Color(127, 127, 127),
			TRANSPARENT = new Color(0, 0, 0, 0);

	private static final String MISS_IMAGE_DIR = "res/Client/Images/Splash.png",
			HIT_IMAGE_DIR = "res/Client/Images/Explosion.png";

	private static BufferedImage missImage, hitImage;

	// CLASS CONSTRUCTOR
	// ----------------------------------------

	static {
		// Loads hit and miss images
		try {
			missImage = ImageIO.read(new BufferedInputStream(new FileInputStream(MISS_IMAGE_DIR)));
			hitImage = ImageIO.read(new BufferedInputStream(new FileInputStream(HIT_IMAGE_DIR)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// INSTANCE FIELDS
	// ----------------------------------------

	private PlayArea playArea;

	private Color currentColor = OVERLAY_GREEN;

	private int posX, posY;

	private boolean hasShip = false, hasOverlay = false, isMiss = false, isHit = false;

	// CONSTRUCTOR
	// ----------------------------------------

	/**
	 * Constructor for PlayAreaCell
	 * 
	 * @param playArea the PlayArea this cell belongs to
	 * @param posX     the X coordinate of this cell
	 * @param posY     the Y coordinate of this cell
	 */
	public PlayAreaCell(PlayArea playArea, int posX, int posY) {
		setPosX(posX);
		setPosY(posY);
		this.playArea = playArea;

		this.setBackground(TRANSPARENT);
	}

	// OVERLAY
	// ----------------------------------------

	/**
	 * Assigns the color of the overlay
	 * 
	 * @param color the color of the overlay
	 */
	public void setOverlayColor(PlayAreaOverlayColor color) {
		switch (color) {
		case RED:
			currentColor = OVERLAY_RED;
			break;

		case GREEN:
			currentColor = OVERLAY_GREEN;
			break;
		}
	}

	// ----------------------------------------

	/**
	 * Marks this cell as having an overlay, assigns the overlay color and repaints
	 * this cell
	 * 
	 * @param color the color of the overlay to add
	 */
	public void addOverlay(PlayAreaOverlayColor color) {
		setOverlayColor(color);
		hasOverlay = true;
		repaint();
	}

	// --------------------

	/**
	 * Marks this cell as having no overlay and repaints this cell
	 */
	public void removeOverlay() {
		hasOverlay = false;
		repaint();
	}

	// ----------------------------------------

	/**
	 * Gets whether this cell currently has an overlay
	 * 
	 * @return returns true if this cell currently has an overlay, false if not
	 */
	public boolean isOverlayed() {
		return hasOverlay;
	}

	// ORIGIN CELL
	// ----------------------------------------

	/**
	 * Notifies the PlayArea to set this cell as the origin cell
	 */
	public void setOriginCell() {
		playArea.overyalGridCells(this);
	}

	// --------------------

	/**
	 * Notifies the PlayArea to remove this cell as the origin cell
	 */
	public void removeOriginCell() {
		playArea.removeOverlayGridCells();
	}

	// CELL SELECTED
	// ----------------------------------------

	/**
	 * Notifies the PlayArea to attempt to place a ship using this cell as the
	 * origin cell
	 */
	public void cellSelected() {
		playArea.cellSelected();
	}

	// POS X
	// ----------------------------------------

	/**
	 * Assigns the X coordinate for this cell
	 * 
	 * @param posX the X coordinate to assign
	 */
	private void setPosX(int posX) {
		this.posX = posX;
	}

	// --------------------

	/**
	 * Gets the X coordinate for this cell
	 * 
	 * @return the X coordinate for this cell
	 */
	public int getPosX() {
		return posX;
	}

	// POS Y
	// ----------------------------------------

	/**
	 * Assigns the Y coordinate for this cell
	 * 
	 * @param posY the Y coordinate to assign
	 */
	private void setPosY(int posY) {
		this.posY = posY;
	}

	// --------------------

	/**
	 * Gets the Y coordinate of this cell
	 * 
	 * @return the Y coordinate of this cell
	 */
	public int getPosY() {
		return posY;
	}

	// HAS SHIP
	// ----------------------------------------

	/**
	 * Sets whether a Ship occupies this cell
	 * 
	 * @param hasShip whether a Ship occupies this cell
	 */
	public void setShip(boolean hasShip) {
		this.hasShip = hasShip;
	}

	// --------------------

	/**
	 * Gets whether a Ship is occupying this cell
	 * 
	 * @return Returns true if a Ship is present on this cell, false if not
	 */
	public boolean hasShip() {
		return hasShip;
	}
	
	public void setHit(boolean isHit)
	{
		this.isHit = isHit;
	}
	
	public boolean isHit()
	{
		return isHit;
	}
	
	public void setMiss(boolean isMiss)
	{
		this.isMiss = isMiss;
	}
	
	public boolean isMiss()
	{
		return isMiss;
	}

	// GRAPHICS
	// ----------------------------------------

	/**
	 * Overrides the paintComponent method
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Repaint PlayArea background image
		playArea.repaint();

		Graphics2D g2 = (Graphics2D) g;

		// Draws a ship in this cell (grey)
		if (hasShip) {
			g2.setColor(SHIP_GREY);
			g2.fillRect(0, 0, CELL_SIZE, CELL_SIZE);
		}

		// Draws the miss image
		if (isMiss) {
			g2.drawImage(missImage, 5, 5, 35, 35, playArea);
		}
		// Draws the hit image
		else if (isHit) {
			g2.drawImage(hitImage, 0, 0, 45, 45, playArea);
		}

		// Draws the overlay
		if (hasOverlay) {
			g2.setColor(currentColor);
			g2.fillRect(0, 0, CELL_SIZE, CELL_SIZE);
		}

		// Draws the grid border
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 0, CELL_SIZE, CELL_SIZE);

		g2.dispose();
	}
}