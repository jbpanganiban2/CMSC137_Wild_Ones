import proto.*;

import java.awt.*;
import java.net.*;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Window{
	
	//
	//	Attributes
	//

	JFrame frame; 
	JPanel mainPanel;
	JPanel cardPanel;
	JPanel chatPanel;
	boolean isFinished;
	Chat chat;
	Socket server;
	String pu;
	Lobby lobby;
	Game game;
	CardLayout cl ;
	//
	//	Constructors
	//
	Window(String name, String lID){
		this.pu=name;
		this.frame = new JFrame("Wild Ones"); 						//instantiate
		this.cl = new CardLayout();
		this.cardPanel = new JPanel();
		this.cardPanel.setLayout(this.cl); 
		this.game = new Game(this.frame);
		this.lobby =  new Lobby(this.pu,lID,this.cl,this.cardPanel);
		this.server=this.lobby.getSocket();
		this.chatPanel = new JPanel();
		this.chat = new Chat(this.server,this.pu);
		this.chat.setPreferredSize(new Dimension(730,150));
		this.isFinished = true;
		createWindow();
	}

	Window(String name){
		// this.server=s;
		this.pu=name;
		this.frame = new JFrame("Wild Ones"); 						//instantiate
		this.cl = new CardLayout();
		this.cardPanel = new JPanel();
		this.cardPanel.setLayout(this.cl); 
		this.game = new Game(this.frame);
		this.lobby =  new Lobby(this.pu,this.cl,this.cardPanel);
		this.server=this.lobby.getSocket();
		this.chatPanel = new JPanel();
		this.chat = new Chat(this.server,this.pu);
		this.chat.setPreferredSize(new Dimension(730,150));
		this.isFinished = true;
		createWindow();

	}

	public void createWindow(){

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setPreferredSize(new Dimension(730,700));
		mainPanel.setOpaque(false);
		
										
		cardPanel.setOpaque(false); 
		cardPanel.setPreferredSize(new Dimension(730,550));
		cardPanel.add(lobby,"LOBBY");
		cardPanel.add(game,"GAME");
		cardPanel.setBounds(0,-10,730,550);
	
		chatPanel.setPreferredSize(new Dimension(730,150));
		chatPanel.setOpaque(false);
		chatPanel.add(this.chat);
		chatPanel.setBounds(0,540,730,150);

		
		mainPanel.add(chatPanel);
		mainPanel.add(cardPanel);

		

		this.frame.setSize(730,700);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
		this.frame.add(mainPanel);									//add to frame
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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