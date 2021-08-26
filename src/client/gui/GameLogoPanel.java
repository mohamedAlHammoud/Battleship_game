package client.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameLogoPanel extends JPanel {

	private BufferedImage image;

	public GameLogoPanel() {

		try {
			image = ImageIO.read(new File("./res/Client/Images/battleshipLogo.jpg"));
			JLabel picLabel = new JLabel(new ImageIcon(image));
			//picLabel.setPreferredSize(new Dimension(1500, 100));
			add(picLabel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
