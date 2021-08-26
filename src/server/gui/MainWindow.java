package server.gui;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

public class MainWindow
{
	// CLASS FIELDS
	// ----------------------------------------

	private static ImageIcon stopIcon, startIcon;

	private static final String BUTTON_START_TEXT = "Start Server", BUTTON_STOP_TEXT = "Stop Server",
			TIMER_ZERO_TEXT = "00:00:00", START_IMAGE_DIR = "res/Server/Images/Green.png",
			STOP_IMAGE_DIR = "res/Server/Images/Red.png";

	private static final int WINDOW_SIZE_X = 600, WINDOW_SIZE_Y = 650, START_BUTTON_HEIGHT = 30;

	private static final Color OUTPUT_BG_COLOR = new Color(1, 36, 86), START_BG_COLOR = new Color(218, 255, 209);

	private static final Insets TEXT_AREA_MARGIN = new Insets(10, 5, 5, 5);

	private static final Font OUTPUT_FONT = new Font("DejaVu Sans Mono", Font.PLAIN, 15);

	private static final Border TIMER_BORDER = BorderFactory.createEmptyBorder(0, 15, 0, 15),
			START_BORDER = BorderFactory.createStrokeBorder(new BasicStroke(2), new Color(32, 210, 0));

	private static final Dimension START_BUTTON_DIMENSION = new Dimension(0, START_BUTTON_HEIGHT),
			OUTPUT_DIMENSION = new Dimension(0, 581);

	// CLASS CONSTRUCTOR
	// ----------------------------------------

	static
	{
		try
		{
			startIcon = new ImageIcon(ImageIO.read(new FileInputStream(START_IMAGE_DIR)));
			stopIcon = new ImageIcon(ImageIO.read(new FileInputStream(STOP_IMAGE_DIR)));
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	// INSTANCE FIELDS
	// ----------------------------------------

	private JFrame window = new JFrame();

	private JTextArea output = new JTextArea();
	
	private JScrollPane scrollArea = new JScrollPane(output);

	private JButton startStopButton = new JButton();

	private JLabel timer = new JLabel();

	private boolean isRunning = false;

	// CONSTRUCTOR
	// ----------------------------------------

	/**
	 * Constructor for MainWindow
	 */
	public MainWindow()
	{

		initWindow();
		initComponents();
		initLayout();

		window.setVisible(true);
	}

	// INIT WINDOW
	// ----------------------------------------

	/**
	 * Configures the frame
	 */
	private void initWindow()
	{
		window.setLayout(new GridBagLayout());
		window.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

	}

	// INIT COMPONENTS
	// ----------------------------------------

	/**
	 * Initializes and configures all components
	 */
	private void initComponents()
	{
		setStartButtonText();
		startStopButton.setFocusPainted(false);
		startStopButton.setIcon(startIcon);
		startStopButton.setPreferredSize(START_BUTTON_DIMENSION);
		startStopButton.setBorder(START_BORDER);
		startStopButton.setBackground(START_BG_COLOR);

		// ----------

		timer.setText(TIMER_ZERO_TEXT);
		timer.setFont(new Font("Sans-Serif", Font.BOLD, 18));
		timer.setHorizontalAlignment(JLabel.CENTER);
		timer.setBorder(TIMER_BORDER);

		// ----------

		output.setBackground(OUTPUT_BG_COLOR);
		output.setFont(OUTPUT_FONT);
		output.setForeground(Color.WHITE);
		output.setMargin(TEXT_AREA_MARGIN);
		output.setPreferredSize(OUTPUT_DIMENSION);
		output.setEditable(false);
		output.setLineWrap(true);
		output.setText("Server started...\n");
		
		scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollArea.setPreferredSize(OUTPUT_DIMENSION);
	}

	// INIT LAYOUT
	// ----------------------------------------

	/**
	 * Configures the layout and adds all components
	 */
	private void initLayout()
	{
		GridBagConstraints windowConstraints = new GridBagConstraints();

		windowConstraints.fill = GridBagConstraints.BOTH;
		windowConstraints.gridx = 0;
		windowConstraints.gridy = 0;
		windowConstraints.weightx = 0.8;

		window.add(startStopButton, windowConstraints);

		// ----------

		windowConstraints.gridx++;
		windowConstraints.weightx = 0.2;

		window.add(timer, windowConstraints);

		// ----------

		windowConstraints.fill = GridBagConstraints.BOTH;
		windowConstraints.gridx = 0;
		windowConstraints.gridy++;
		windowConstraints.weightx = 1;
		windowConstraints.gridwidth = 2;

		window.add(scrollArea, windowConstraints);
	}

	// SET BUTTON TEXT
	// ----------------------------------------

	/**
	 * Sets the text for the Start/Stop button depending on whether the server is
	 * running or not
	 */
	private void setStartButtonText()
	{
		if (isRunning)
		{
			startStopButton.setIcon(stopIcon);
			startStopButton.setText(BUTTON_STOP_TEXT);
		} else
		{
			startStopButton.setIcon(startIcon);
			startStopButton.setText(BUTTON_START_TEXT);
		}
	}
	
	public void outputMessage(String message)
	{
		output.append(message + "\n");
	}
}
