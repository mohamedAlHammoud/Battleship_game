package client.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.swing.*;
import javax.swing.border.Border;

import client.domain.ConnectionManager;
import server.domain.NC;

//**NOT FINISHED**
public class MainWindow
{
	private JFrame window = new JFrame();

	private LoginPanel login = new LoginPanel(this);

	private static final int WINDOW_SIZE_X = 1500, WINDOW_SIZE_Y = 783;

	private JButton disconnect;
	
	private GameBoardView gameView;

	// ----------------------------------------


	public MainWindow() {
		initWindow();
	}

	// ----------------------------------------

	// make method to call from loginpanel, pass socket, pass boolean loggedIn,
	// passUsername

	// make a method for all logic with boolean, no if statement


	public void initWindow() {
//		window.setLayout(new GridBagLayout());
		window.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
		//window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setLocationRelativeTo(null);
		window.add(login);
		window.setVisible(true);
		window.setBackground(Color.black);

		window.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent)
		    {
		    	try
		    	{
		    		gameView.endSession();
		    	}
		    	catch(Exception e)
		    	{
		    		
		    	}
	            System.exit(0);
		    }
		});

	}
	public void loginSuccesful(Socket socket, String userName)
	{
		ConnectionManager manager = new ConnectionManager(socket);
		
		ByteBuffer buffer = ByteBuffer.allocate(1 + userName.getBytes().length);
		buffer.put(NC.SET_NAME);
		buffer.put(userName.getBytes());
		
		manager.tryWriteToServer(buffer.array());
		

		window.remove(login);

		gameView = new GameBoardView(manager, this);
		window.add(gameView);

		window.revalidate();
	}
	
	public void closeProgram()
	{
		System.exit(0);
	}

}