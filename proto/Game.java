import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Game extends JPanel implements Runnable{

	//
	//	Possible starting points
	//		p1 = 155, 170
	//		p2 = 345, 120
	//		p3 = 540, 170
	//		p4 = 45, 450
	//		p5 = 645, 450
	//
	
	//
	//	Attributes
	//

	private JPanel gamePanel;
	private JPanel chatPanel;

	private ArrayList<Player> players;
	private ArrayList<Character> chars;
	private ArrayList<Point> respawns;
	private ArrayList<GameObject> gameObjects;
	private boolean isFinished;

	private ChatGameWindow cgw;

	//
	//	Constructors
	//

	Game(ChatGameWindow cgw){

		this.gamePanel = new JPanel();
		this.respawns = respawnZoneGenerate();
		this.chars = new ArrayList<Character>();
		this.gameObjects = new ArrayList<GameObject>();
		this.cgw = cgw;

		int i = 0;
		for(Point p : this.respawns){
			this.chars.add(new Character("nameu"+Integer.toString(i), p, this));
		}

		this.isFinished = false;
		createGame();

	}

	public void createGame(){
															//background
		ImageIcon icon = new ImageIcon("./src/GAMEBG.png"); 
		Image newimg = icon.getImage().getScaledInstance(730, 550,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);

		gamePanel.setLayout(null); 								//set layout to null so character can be anywhere
		gamePanel.setOpaque(false); 
		gamePanel.setPreferredSize(new Dimension(730,550));

		for (int i=1;i<22 ;i++ ) {
			if(i == 8 || i == 12)continue;
			Obstacles obs = new Obstacles(i);
			obs.setBounds(this.getX(),this.getY(),this.getWidth(),this.getHeight());
			gamePanel.add(obs);
			gameObjects.add(obs);
		}

		ImagePanel bg = new ImagePanel(newIcon.getImage());
		bg.setPreferredSize(new Dimension(730,550));
		bg.setLayout(new BorderLayout());
		bg.add(gamePanel); 										//add gamePanel to panel with bg

		this.setSize(730,700);
		this.add(bg);

		// this.deploy();
	}

	//
	//	Methods
	//

	private ArrayList<Point> respawnZoneGenerate(){					//	returns an arraylist containing all possibleSpawnZones
		return (new ArrayList<Point>(){
					ArrayList<Point> addAll(){
						this.add(new Point(160, 10));
						this.add(new Point(626, 104));
						this.add(new Point(97, 166));
						this.add(new Point(549, 361));
						this.add(new Point(33, 416));
						return this;

					}
				}).addAll();
	}

	public synchronized void run(){
		this.cgw.getChat().setCharacter(this.chars.get(0));
		while(!isFinished){
			int time;
			for(Character c : this.chars){
				// play a turn -- this will only activate for player
				new Prompt(c.getUserName()+"'s turn", 1000);
				c.enable();
				while((time = c.getTimeLeft()) > 0){
					// System.out.println(time+" left");
					try{Thread.sleep(1000);}catch(Exception e){e.printStackTrace();};
				}c.endTurn();
			}
		}
	}

	public void deploy(){
		Thread t = new Thread(this);
		t.start();
	}

	public ArrayList<GameObject> getGameObjects(){
		return this.gameObjects;
	}

	public JPanel getGamePanel(){
		return this.gamePanel;
	}

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
