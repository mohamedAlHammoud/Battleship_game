package client.gui;

import java.awt.*;
import java.io.*;
import javax.swing.*;

import client.domain.Ship;

public class Board extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// CLASS FIELDS
	// ----------------------------------------

	private static final int PANEL_SIZE = 495, GRID_SIZE = 10, CELL_SIZE = 45, A_CHAR_VALUE = 65;

	private static final String MILITARY_FONT_DIR = "res/Client/Fonts/Military Stencil.ttf";

	private static final Color LABEL_COLOR = new Color(48, 54, 66);

	private static final Dimension PANEL_DIMENSIONS = new Dimension(PANEL_SIZE, PANEL_SIZE),
			CELL_DIMENSIONS = new Dimension(CELL_SIZE, CELL_SIZE);

	private static Font partialFont, labelFont;

	boolean opponent;

	// CLASS CONSTRUCTOR
	// ----------------------------------------

	static {
		// Load and create custom font
		try {
			InputStream stream = new BufferedInputStream(new FileInputStream(MILITARY_FONT_DIR));
			partialFont = Font.createFont(Font.TRUETYPE_FONT, stream);
			labelFont = partialFont.deriveFont(Font.PLAIN, 28);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	// INSTANCE FIELDS
	// ----------------------------------------

	private JLabel[] xLabels = new JLabel[GRID_SIZE], yLabels = new JLabel[GRID_SIZE];

	private PlayArea playArea;

	// CONSTRUCTOR
	// ----------------------------------------

	/**
	 * Constructor for Board
	 */
	public Board(boolean opponent, GameBoardView view) {

		initPanel();

		this.opponent = opponent;

		initComponents(view);
		initGrid();

	}

	// INIT PANEL
	// ----------------------------------------

	/**
	 * Configures all settings for this panel
	 */
	private void initPanel() {
		this.setLayout(new GridBagLayout());

		this.setPreferredSize(PANEL_DIMENSIONS);
		this.setMaximumSize(PANEL_DIMENSIONS);
		this.setMinimumSize(PANEL_DIMENSIONS);

	}

	// INIT COMPONENTS
	// ----------------------------------------

	/**
	 * Initializes all components to be added to this panel
	 */
	private void initComponents(GameBoardView view) {
		playArea = new PlayArea(!opponent, view);

		// Setup X-grid label [1-10]
		for (int i = 0; i < GRID_SIZE; i++) {
			JLabel label = new JLabel(String.valueOf(i + 1));
			label.setBackground(LABEL_COLOR);
			label.setForeground(Color.WHITE);
			label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			label.setFont(labelFont);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);
			label.setPreferredSize(CELL_DIMENSIONS);
			label.setMaximumSize(CELL_DIMENSIONS);
			label.setMinimumSize(CELL_DIMENSIONS);
			label.setOpaque(true);

			xLabels[i] = label;
		}

		// Setup Y-grid labels [A-J]
		for (int i = 0; i < GRID_SIZE; i++) {
			JLabel label = new JLabel(String.valueOf((char) (i + A_CHAR_VALUE)));
			label.setBackground(LABEL_COLOR);
			label.setForeground(Color.WHITE);
			label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			label.setFont(labelFont);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);
			label.setPreferredSize(CELL_DIMENSIONS);
			label.setMaximumSize(CELL_DIMENSIONS);
			label.setMinimumSize(CELL_DIMENSIONS);
			label.setOpaque(true);

			yLabels[i] = label;
		}
	}

	// INIT GRID
	// ----------------------------------------

	/**
	 * Setup grid layout and add all components
	 */
	private void initGrid() {
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;

		// Start at 2 so top left panel is empty
		constraints.gridx = 2;
		constraints.gridy = 1;

		// Add all X-grid labels
		for (int i = 0; i < 10; i++) {
			constraints.gridx++;
			this.add(xLabels[i], constraints);
		}

		// Start at 2 so top left panel is empty
		constraints.gridx = 1;
		constraints.gridy = 2;

		// Add all Y-Grid labels
		for (int i = 0; i < 10; i++) {
			constraints.gridy++;
			this.add(yLabels[i], constraints);
		}

		// Setup constraint for PlayArea
		constraints.gridx = 3;
		constraints.gridy = 3;
		constraints.gridheight = GRID_SIZE;
		constraints.gridwidth = GRID_SIZE;
		this.add(playArea, constraints);

	}

	// SET SHIP
	// ----------------------------------------

	/**
	 * Sets the current ship being placed
	 * 
	 * @param ship the Ship to be placed
	 */
	public void setShip(Ship ship) {
		playArea.setShip(ship);
	}
	
	public void setShotTaking(boolean takeShot)
	{
			playArea.enableShots(takeShot);
	}

	public void setHitMiss(byte[] board)
	{
		playArea.setHitMiss(board);
	}
}
