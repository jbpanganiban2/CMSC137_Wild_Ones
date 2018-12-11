import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Character extends MovingObject{

	//
	//  Attributes
	//
	private final static Icon PIG_ATTACK = new ImageIcon("src/pig/pigAttack.gif");
	private final static Icon PIG_STANDBY = new ImageIcon("src/pig/pigStandby.gif");
	private final static Icon PIG_WALKLEFT = new ImageIcon("src/pig/pigWalkLeft.gif");
	private final static Icon PIG_WALKRIGHT = new ImageIcon("src/pig/pigWalkRight.gif");

	private final static Icon LUB_ATTACK = new ImageIcon("src/lubglub/lubAttack.gif");
	private final static Icon LUB_STANDBY = new ImageIcon("src/lubglub/lubStandby.gif");
	private final static Icon LUB_WALKLEFT = new ImageIcon("src/lubglub/lubWalkLeft.gif");
	private final static Icon LUB_WALKRIGHT = new ImageIcon("src/lubglub/lubWalkRight.gif");

	private final static Icon DYNA_ATTACK = new ImageIcon("src/dyna/dynaAttack.gif");
	private final static Icon DYNA_STANDBY = new ImageIcon("src/dyna/dynaStandby.gif");
	private final static Icon DYNA_WALKLEFT = new ImageIcon("src/dyna/dynaWalkLeft.gif");
	private final static Icon DYNA_WALKRIGHT = new ImageIcon("src/dyna/dynaWalkRight.gif");

	private final static int ATTACK = 0;
	private final static int STANDBY = 1;
	private final static int WALKLEFT = 2;
	private final static int WALKRIGHT = 3;
	private JLabel charr;

	private UDPClient udpclient;

	// 0 stands for pressed, 1 stands for released
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;  
	static final int JUMP0 = 01;
	static final int JUMP1 = 02;
	static final int LEFT0 = 30;  
	static final int LEFT1 = 31;  
	static final int RIGHT0 = 40;
	static final int RIGHT1 = 41;

	private static int movement = 4; // character velocity
	private final static float GRAVITY = 0.75f;
	private static boolean onGround;
	private static float xvelocity;
	private static float yvelocity;

	private int health;
	private int type;
	private int points;
	private String id;
	private boolean cooldown;
	private boolean enabled;
	private boolean deployedRocket;
	private boolean xCollide;
	private boolean yCollide;

	//
	//  Constructors
	//

	public Character(String name, Point init, Game g, int type){
		super(name, init, new Dimension(40, 50), g);
		this.initchar(type);
	}

	public Character(Player p, Point init, Game g, int type){
		super(p.getName(), init, new Dimension(40, 50), g);
		this.id = p.getID();
		this.udpclient = g.getUDPclient();
		this.initchar(type);
	}

	private void initchar( int type ){

		this.type = type;

		this.charr= new JLabel();
		this.charr.setOpaque(false);
		this.setCharacterUI(STANDBY);
		this.add(this.charr);

		this.setLoc();
		this.gamePanel.add(this);
		this.gamePanel.addMouseListener(new RocketListener());

		this.setOpaque(false);
		this.alive = true;
		this.cooldown = false;
		this.onGround = true;
		this.health = 10;

		this.setOpaque(false);

		this.addKeyBindings();
		this.disable();
	}

	private synchronized void printState(){
		System.out.println("-------------------------");
		System.out.println("alive: "+this.alive);
		System.out.println("cooldown: "+this.cooldown);
		System.out.println("health: "+this.health);
		System.out.println("-------------------------");
	}

	//
	//  Methods
	//

	public void attack(){
		setCharacterUI(ATTACK);
	}

	public void afterAttack(){
		setCharacterUI(STANDBY);
	}

	public void addPoints(int p){
		this.points += p;
		System.out.println(this.name+"'s current points are: "+this.points);
	}


	public int getType(){
		return this.type;
	}

	public String getID(){
		return this.id;
	}

	public boolean is(Character c){
		return this.id == c.id;
	}

	public boolean isEqual(Character c){
		return this.name == c.name;
	}

	public void setCharacterUI(int type){

		// sets the character icon the situation needs
		// types will be determined in constants above

		switch(type){
		case ATTACK:

			if(this.type==1){
				this.charr.setIcon(LUB_ATTACK);
			}else if(this.type==2){
				this.charr.setIcon(DYNA_ATTACK);
			}else{
				this.charr.setIcon(PIG_ATTACK);
			}
		break;

		case STANDBY:

			if(this.type==1){
				this.charr.setIcon(LUB_STANDBY);
			}else if(this.type==2){
				this.charr.setIcon(DYNA_STANDBY);
			}else{
				this.charr.setIcon(PIG_STANDBY);
			}
		break;

		case WALKLEFT:

			if(this.type==1){
				this.charr.setIcon(LUB_WALKLEFT);
			}else if(this.type==2){
				this.charr.setIcon(DYNA_WALKLEFT);
			}else{
				this.charr.setIcon(PIG_WALKLEFT);
			}
		break;

		case WALKRIGHT:

			if(this.type==1){
				this.charr.setIcon(LUB_WALKRIGHT);
			}else if(this.type==2){
				this.charr.setIcon(DYNA_WALKRIGHT);
			}else{
				this.charr.setIcon(PIG_WALKRIGHT);
			}
		break;

		default:

		break;
		}
	}

	public synchronized void moveX(int movement){
		Point test = new Point((int)this.position.getX()+movement, ((int)this.position.getY()));
		Rectangle testRect = new Rectangle(test, this.size);
		if((this.hasCollision(testRect,this.g.getGameObjects())) != null || !this.g.rectContains(testRect)){
			this.xCollide = true;
			return;
		}

		this.movePosition(movement, 0);
		// if(this.xvelocity != 0 && this.yvelocity != 0)
		// 	this.udpclient.send(new Point(this.position));
	}

	public synchronized void moveY(int movement){
				
		if(movement < 0){ // going upwards if movement is negative
			moveUp(movement);
		}else if(movement > 0){ // going downwards if movement is positive
			moveDown(movement);
		}

		// if(this.xvelocity != 0 && this.yvelocity != 0)
		// 	this.udpclient.send(new Point(this.position));
	}

	public synchronized void moveUp(int movement){

	// checks first if there will be a collision before moving

		Point test = new Point((int)this.position.getX(), ((int)this.position.getY())+movement);
		if((this.hasCollision(new Rectangle(test, this.size),this.g.getGameObjects())) != null){
			this.yCollide = true;
			this.yvelocity = 0;
			return;
		}

		this.movePosition(0, +movement);
		// this.udpclient.send(new Point(this.position));
	}

	public synchronized void moveDown(int movement){

		Point test = new Point((int)this.position.getX(), ((int)this.position.getY())+movement);
		if((this.hasCollision(new Rectangle(test, this.size),this.g.getGameObjects())) != null){
			this.yCollide = true;
			this.onGround = true;
			this.yvelocity = 0;
			return;
		}

		this.movePosition(0, movement);
		// this.udpclient.send(new Point(this.position));
	}

	public void addKeyBindings(){


		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), JUMP0);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), JUMP1);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), LEFT0);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), LEFT1);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,false), RIGHT0);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,true), RIGHT1);

		this.getActionMap().put(JUMP0, new Move(this, JUMP0));
		this.getActionMap().put(JUMP1, new Move(this, JUMP1));    
		this.getActionMap().put(LEFT0, new Move(this, LEFT0));
		this.getActionMap().put(LEFT1, new Move(this, LEFT1));
		this.getActionMap().put(RIGHT0, new Move(this, RIGHT0));
		this.getActionMap().put(RIGHT1, new Move(this, RIGHT1));

	}

	public synchronized void disable(){  // disables keybindings for Character

		this.enabled = false;
		this.getActionMap().get(JUMP0).setEnabled(false);
		this.getActionMap().get(JUMP1).setEnabled(false);
		this.getActionMap().get(LEFT0).setEnabled(false);
		this.getActionMap().get(LEFT1).setEnabled(false);
		this.getActionMap().get(RIGHT0).setEnabled(false);
		this.getActionMap().get(RIGHT1).setEnabled(false);

	}

	public synchronized void enable(){ // enables keyBindings for Character

	// this.time = 25;
	// this.udpclient.start();
		this.enabled = true;
		this.deployedRocket = false;
		this.getActionMap().get(JUMP0).setEnabled(true);
		this.getActionMap().get(JUMP1).setEnabled(true);
		this.getActionMap().get(LEFT0).setEnabled(true);
		this.getActionMap().get(LEFT1).setEnabled(true);
		this.getActionMap().get(RIGHT0).setEnabled(true);         
		this.getActionMap().get(RIGHT1).setEnabled(true);

	}

	public synchronized void mleft(){
		this.xvelocity = -4;
	}

	public synchronized void mright(){
		this.xvelocity = 4;
	}

	public synchronized void standByX(){
		this.xvelocity = 0;
	}

	public synchronized void standByY(){
		this.yvelocity = 0;
	}

	// int origin;
	public synchronized void startJump(){
		// System.out.println("jumpstarted");
		if(this.onGround){
			// System.out.println("enters?");
			this.yvelocity = -12;
			this.onGround = false;
			// System.out.println(this.yvelocity);
		}
	}

	public synchronized void endJump(){
	    if(this.yvelocity < -6)
	        this.yvelocity = -6;
	}

	public synchronized void run(){
		(new Thread(){
			@Override
			public synchronized void run(){
				while(alive){
					yvelocity += GRAVITY;
					moveX((int)xvelocity);
					moveY((int)yvelocity);
					if(xvelocity != 0 && yvelocity != 0)
						udpclient.send(new Point(position));
					try{Thread.sleep(33);}catch(Exception e){e.printStackTrace();};
				}
			}
		}).start();
	}


	public synchronized void deployRocket(Point p, int damage){

		if(this.enabled && !this.cooldown/*&& this.time != 0 && !this.deployedRocket*/){
			new Rocket("rocket", damage, this, new Point(this.position), p, this.g, 0).alwaysOnCollisionChecker(MovingObject.gameObjects);
			this.udpclient.send(new Point(this.position),p, damage);
			this.rocketCooldown();
		}
	}

	public void deployRocket(Point o, Point p, int damage){
		new Rocket("rocket", damage, this, new Point(o), new Point(p), this.g, 0).alwaysOnCollisionChecker(MovingObject.gameObjects);
		System.out.println("a rocket has been created");
	}

	private synchronized void rocketCooldown(){
		this.cooldown = true;
		(new Thread(){
			int time = 3;
			@Override
			public void run(){
				while(time > 0){
					try{
						System.out.println("rocket cooldown: "+time);
						time--;
						Thread.sleep(1000);
						setCharacterUI(STANDBY);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				cooldown = false;
			}
		}).start();
	}

	public static int rng(int max, int min){ // produces a random number between [max, min]
		return (new Random()).nextInt((max - min) + 1) + min;
	}

	public synchronized void damaged(int dmg){
		System.out.println(this.name+" damaged by "+Integer.toString(dmg));
		this.health -= dmg;
		if(this.health <= 0){
			System.out.println(this.name+" is now dead.");
			this.alive = false;
			this.g.getGameObjects().remove(this);
			this.g.getGamePanel().remove(this);		
			// this.g.getChars().remove(this);
			this.g.charDied();
			this.setVisible(false);
			this.g.refreshPanel();
		}
	}

	//
	//  Internal Classes
	//


	class RocketListener extends MouseAdapter{                  // listens to Rockets

		@Override
		public void mousePressed(MouseEvent e) {
			attack();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			deployRocket(e.getPoint(),rng(10,1));
		}
	}


	class Move extends AbstractAction {

		Character ch;
		int moveType;

		Move(Character ch, int moveType){
			this.ch = ch;
			this.moveType = moveType;
		}


		@Override
		public void actionPerformed(ActionEvent e) {
			// gravity();

			JLabel nIcon = new JLabel();

			switch(this.moveType){
				case JUMP0:           // space pressed
					this.ch.startJump();
				break;

				case JUMP1:           // space released
					this.ch.endJump();
				break;

				case LEFT0:           // when key is pressed, enables the character to move 
					this.ch.mleft();
					this.ch.setCharacterUI(WALKLEFT);
				break;

				case LEFT1:           // removes key's ALREADY PRESSED state
					this.ch.standByX();
					this.ch.setCharacterUI(STANDBY);
				break;

				case RIGHT0:
					this.ch.mright();
					// this.ch.setUI(WALKRIGHT);
					this.ch.setCharacterUI(WALKRIGHT);
				break;

				case RIGHT1:
					this.ch.standByX();
					this.ch.setCharacterUI(STANDBY);

				break;

				default:

				break;
			}
		}
	}

}