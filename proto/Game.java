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
	private final static int FIRE = 0;
	private final static int PIG = 1;
	private final static int DYNAMIGHT = 2;
	private final static int LUBGLUB = 3;

	JPanel parentPanel;
	JPanel gamePanel;
	JPanel chatPanel;
	JPanel playerPanel;

	ArrayList<Character> chars;
	ArrayList<Point> respawns;
	ArrayList<GameObject> gameObjects;
	Character userCharacter;
	Player userPlayer;
	boolean isFinished;

	UDPClient udpclient;

	//
	//	Constructors
	//

	Game(){

		this.gamePanel = new JPanel();
		this.respawns = respawnZoneGenerate();
		this.chars = new ArrayList<Character>();
		this.gameObjects = new ArrayList<GameObject>();
		this.playerPanel = new JPanel();

		this.isFinished = false;
		createGame();

	}

	Game(Lobby l){

		this.gamePanel = new JPanel();
		this.parentPanel = l.getGamePanel();
		this.udpclient = l.getUDPClient();
		this.respawns = respawnZoneGenerate();
		this.chars = new ArrayList<Character>();
		this.gameObjects = new ArrayList<GameObject>();
		this.playerPanel = new JPanel();

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

	}

	public void init_Players(Player[] players){						// initializes players
		int i = 0;
		Character toAdd = null;
		for(Player p : players){
			if(p == null)continue;
			if(!p.getID().equals(this.userPlayer.getID())){
				this.chars.add(new Character(p, this.respawns.get(p.getIntID()), this, i%3));
			}
			i+=1;
		}
	}

	public void init_Players(int max){						// initializes players
		int i = 0;
		Character toAdd = null;
		for(i = 0; i < max; i++){
			toAdd = new Character(Integer.toString(i), this.respawns.get(i), this, i%3);
			this.chars.add(toAdd);
		}
	}

	public void addUserPlayer(Player user, int type){
		this.userPlayer = user;
		this.userCharacter = new Character(user, this.respawns.get(user.getIntID()), this, type);
		// System.out.println("user Char ID == "+this.userCharacter.getID());
		this.chars.add(this.userCharacter);
	}

	//
	//	Methods
	//

	private ArrayList<Point> respawnZoneGenerate(){					//	returns an arraylist containing all possibleSpawnZones
		return (new ArrayList<Point>(){
					ArrayList<Point> addAll(){

						this.add(new Point(160, 10));
						this.add(new Point(626, 104));
						this.add(new Point(97, 146));

						this.add(new Point(549, 361));
						this.add(new Point(0, 416));
						return this;
					}
				}).addAll();
	}

	public ArrayList<GameObject> getGameObjects(){
		return this.gameObjects;
	}

	public JPanel getGamePanel(){
		return this.gamePanel;
	}

	public UDPClient getUDPclient(){
		return this.udpclient;
	}


	public synchronized void dead(Character c){
		this.chars.remove(c);
	}

	public boolean rectContains(Rectangle o){
		return (new Rectangle(new Point(0,0), new Dimension(730, 550))).contains(o);
	}

	private Character getCharacterByName(String name){

		// returns the character if found, else returns null
		// System.out.println("enters gcbn");
		// System.out.println(this.chars.size());
		// int i = 0;
		for(Character c : this.chars){
			// System.out.println(i++);
			if(c == null)continue;
			// System.out.println(c.getName());
			if(c.getObjName().equals(name))return c;
		}

		return null;
	}

	public void moveChar(String name, Point p){
		Character test = this.getCharacterByName(name);
		test.setLoc(p);
	}

	public void deployCharRocket(String name, Point o, Point d, int damage){
		Character test = this.getCharacterByName(name);
		test.deployRocket(o,d,damage);
	}

	public synchronized void run(){
		int time;
		this.userCharacter.enable();
		this.userCharacter.deploy();
		this.isFinished = false;

		while(this.chars.size() > 1){}

		this.userCharacter.disable();
		this.isFinished = true;

		System.out.println(this.chars.get(0).getName()+" won.");

		((CardLayout)this.parentPanel.getLayout()).next(this.parentPanel);
		this.parentPanel.remove(this);
	}

	public synchronized boolean isGameFinished(){
		return this.isFinished;
	}

	public void deploy(){
		Thread t = new Thread(this);
		t.start();

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