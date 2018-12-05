import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Game{
	
	//
	//	Attributes
	//

	JFrame gameFrame; 
	JPanel mainPanel;
	Character ch;
	ArrayList<Character> chars;
	boolean isFinished;

	//
	//	Constructors
	//

	Game(){
		this.gameFrame = new JFrame("Game"); 						//instantiate
		this.mainPanel = new JPanel();
		this.ch = new Character("Player", new Point(50,50), this.mainPanel, this.gameFrame);
		this.isFinished = true;
		createGame();

	}

	public void createGame(){
															//background
		ImageIcon icon = new ImageIcon("./src/LobbyBG.png"); 
		Image newimg = icon.getImage().getScaledInstance(730, 700,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		ImagePanel bg = new ImagePanel(newIcon.getImage());
		bg.setPreferredSize(new Dimension(730,700));
		bg.setLayout(new BorderLayout());

		
		mainPanel.setLayout(null); 								//set layout to null so character can be anywhere
		mainPanel.setOpaque(false); 
		mainPanel.setPreferredSize(new Dimension(730,700));

		bg.add(mainPanel); 										//add mainpanel to panel with bg

		this.gameFrame.setSize(730,700);
		this.gameFrame.setResizable(false);
		this.gameFrame.setVisible(true);
		this.gameFrame.add(bg);									//add to frame
        	this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//
	//	Methods
	//


	//
	//	Internal Classes
	//

	class ImagePanel extends JPanel {

	    	private Image img;
		public ImagePanel(Image img) {
			this.img = img;
			Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}
		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, null);
		}
	}
}