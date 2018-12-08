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

	// 0 stands for pressed, 1 stands for released
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;  
	static final int JUMP0 = 01;
	static final int JUMP1 = 02;
	static final int LEFT0 = 30;  
	static final int LEFT1 = 31;  
	static final int RIGHT0 = 40;
	static final int RIGHT1 = 41;

	private static int movement = 3;

	private int health;
	private int type;
	private String id;
	private boolean cooldown;
	private boolean jumping;
	private boolean movingRight;
	private boolean movingLeft;
	private boolean movingDown;	// gravity
	private boolean falling;	// prevents double jumping
	private boolean enabled;
	private boolean deployedRocket;
	private boolean xCollide;
	private boolean yCollide;



	//
	//  Constructors
	//

	public Character(String name, Point init, Game g, int type){
		super(name, init, new Dimension(50, 50), g);
		this.initchar(type);
	}

	public Character(Player p, Point init, Game g, int type){
		super(p.getName(), init, new Dimension(50, 50), g);
		this.id = p.getID();
		System.out.println(this.name+"'s id = "+this.id);
		this.initchar(type);
	}

	private void initchar( int type ){
		this.addKeyBindings();

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
		this.jumping = false;
		this.movingLeft = false;
		this.movingRight = false;
		this.movingDown = false;
		this.cooldown = false;
		this.falling = false;
		this.health = 10;

		this.setOpaque(false);

		this.disable();
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

	public synchronized void endTurn(){
		this.movingLeft = false;
		this.movingRight = false;
		this.disable();
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

	private void setCharacterUI(int type){

		// sets the character icon the situation needs
		// types will be determined in constants above

		switch(type){
		case ATTACK:

			if(this.type==1){
				this.charr.setIcon(PIG_ATTACK);
			}else if(this.type==2){
				this.charr.setIcon(DYNA_ATTACK);
			}else{
				this.charr.setIcon(LUB_ATTACK);
			}
		break;

		case STANDBY:

			if(this.type==1){
				this.charr.setIcon(PIG_STANDBY);
			}else if(this.type==2){
				this.charr.setIcon(DYNA_STANDBY);
			}else{
				this.charr.setIcon(LUB_STANDBY);
			}
		break;

		case WALKLEFT:

			if(this.type==1){
				this.charr.setIcon(PIG_WALKLEFT);
			}else if(this.type==2){
				this.charr.setIcon(DYNA_WALKLEFT);
			}else{
				this.charr.setIcon(LUB_WALKLEFT);
			}
		break;

		case WALKRIGHT:

			if(this.type==1){
				this.charr.setIcon(PIG_WALKRIGHT);
			}else if(this.type==2){
				this.charr.setIcon(DYNA_WALKRIGHT);
			}else{
				this.charr.setIcon(LUB_WALKRIGHT);
			}
		break;

		default:

		break;
		}
	}


	public synchronized void moveRight(){
		Point test = new Point((int)this.position.getX()+movement, ((int)this.position.getY()));
		Rectangle testRect = new Rectangle(test, this.size);
		if((this.hasCollision(testRect,this.g.getGameObjects())) != null || !this.g.rectContains(testRect)){
			this.xCollide = true;
			return;
		}

		this.movePosition(movement, 0);
	}

	public synchronized void moveLeft(){
		Point test = new Point((int)this.position.getX()-movement, ((int)this.position.getY()));
		Rectangle testRect = new Rectangle(test, this.size);
		if((this.hasCollision(testRect,this.g.getGameObjects())) != null || !this.g.rectContains(testRect)){
			this.xCollide = true;
			return;
		}

		this.movePosition(-movement, 0);
	}
	public synchronized void moveUp(){

	// checks first if there will be a collision before moving

		Point test = new Point((int)this.position.getX(), ((int)this.position.getY())-movement);
		if((this.hasCollision(new Rectangle(test, this.size),this.g.getGameObjects())) != null){
			this.yCollide = true;
			return;
		}

		this.movePosition(0, -movement);
	}

	public synchronized int moveUp(int movement){

	// checks first if there will be a collision before moving

		Point test = new Point((int)this.position.getX(), ((int)this.position.getY())-movement);
		if((this.hasCollision(new Rectangle(test, this.size),this.g.getGameObjects())) != null){
			this.yCollide = true;
			return moveUp(movement - 2);
		}

		this.movePosition(0, -movement);
		return 1;
	}
	public synchronized void moveDown(){

		Point test = new Point((int)this.position.getX(), ((int)this.position.getY())+movement);
		if((this.hasCollision(new Rectangle(test, this.size),this.g.getGameObjects())) != null){
			this.yCollide = true;
			return;
		}

		this.movePosition(0, movement);
	}
	public synchronized void jump(){
	// thread that continuosly adds then subtracts y values at this position
	// fix bounds arguments
		(new Thread(){
			@Override
			public void run(){
				jumping = true;
				int originalY = (int)position.getY();
				int target = originalY-100; 

				while(position.getY() > target){              // moves upward

					if(yCollide)break;
					try{Thread.sleep(15);}catch(Exception e){e.printStackTrace();};
					moveUp(6);

				}
				yCollide = false;         // resets whatever made the jump stop going up
				jumping = false;
			} 
		}).start();
	}

	public void gravity(){
		(new Thread(){
		@Override
		public synchronized void run(){
			if(!movingDown && !jumping){
				movingDown = true;
				while(position.getY() < 550-((int)size.getHeight())){       // moves downwards
					if(yCollide)break;
					try{Thread.sleep(15);}catch(Exception e){e.printStackTrace();};
					moveDown();
				}
				yCollide = false;                     // resets the jump stopper for next jump attempt
				movingDown = false;
				falling = false;
			}
		}
		}).start();

	}

	public synchronized void contMoveLeft(){
		(new Thread(){

		@Override
		public void run(){
			while(isMovingLeft()){
				if(xCollide)break;
				try{Thread.sleep(25);}catch(Exception exc){exc.printStackTrace();};
				moveLeft();
			}
			xCollide = false;
		}
		}).start();
	}

	public synchronized void contMoveRight(){
		(new Thread(){
		@Override
		public void run(){
			while(isMovingRight()){
				if(xCollide)break;
				try{Thread.sleep(25);}catch(Exception exc){exc.printStackTrace();};
				moveRight();
			}
			xCollide = false;
		}
		}).start();

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
	this.enabled = true;
	this.deployedRocket = false;
	this.getActionMap().get(JUMP0).setEnabled(true);
	this.getActionMap().get(JUMP1).setEnabled(true);
	this.getActionMap().get(LEFT0).setEnabled(true);
	this.getActionMap().get(LEFT1).setEnabled(true);
	this.getActionMap().get(RIGHT0).setEnabled(true);         
	this.getActionMap().get(RIGHT1).setEnabled(true);

	}


	public synchronized void run(){

	}

	private synchronized void toggleMovingLeft(){
	this.movingLeft = !this.movingLeft;
	}

	private boolean isMovingLeft(){
	return this.movingLeft;
	}

	private synchronized void toggleMovingRight(){
	this.movingRight = !this.movingRight;
	}

	private boolean isMovingRight(){
	return this.movingRight;
	}

	private void deployRocket(Point p){

		if(this.enabled && !this.cooldown/*&& this.time != 0 && !this.deployedRocket*/){
			new Rocket("rocket", this, new Point(this.position), p, this.g, 0).alwaysOnCollisionChecker(MovingObject.gameObjects);
			this.rocketCooldown();
			// this.time = 0;
			// this.deployedRocket = true;
		}
	}

	private synchronized void rocketCooldown(){
		this.cooldown = true;
		(new Thread(){
			int time = 4;
			@Override
			public void run(){
				while(time > 0){
					try{
						// System.out.println("rocket cooldown: "+time);
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

	// public void setTimeZero(){
	// 	this.time = 0;
	// }

	// public int getTimeLeft(){
	// 	return this.time;
	// }

	public static int rng(int max, int min){ // produces a random number between [max, min]
		return (new Random()).nextInt((max - min) + 1) + min;
	}

	public synchronized void damaged(){
		int dmg = rng(10,1);
		System.out.println(this.name+" damaged by "+Integer.toString(dmg));
		this.health -= dmg;
		if(this.health <= 0){
			System.out.println(this.name+" is now dead.");
			this.alive = false;
			this.g.getGameObjects().remove(this);
			this.g.getGamePanel().remove(this);
			this.g.dead(this);
			this.setVisible(false);
			this.g.getGamePanel().invalidate();
			this.g.getGamePanel().validate();
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

			deployRocket(e.getPoint());
			falling = true;
			jumping = false;		
			gravity();
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
			gravity();

			JLabel nIcon = new JLabel();

			switch(this.moveType){
				case JUMP0:           // space pressed
					if(jumping || this.ch.isMovingLeft() || this.ch.isMovingRight() || falling)return;
					this.ch.jump();
				break;

				case JUMP1:           // space released
					falling = true;
					jumping = false;
					this.ch.gravity();
				break;

				case LEFT0:           // when key is pressed, enables the character to move 

					if(this.ch.isMovingLeft())return; // when key is ALREADY PRESSED, returns
					this.ch.setCharacterUI(WALKLEFT);
					this.ch.toggleMovingLeft(); 
					this.ch.contMoveLeft();
				break;

				case LEFT1:           // removes key's ALREADY PRESSED state
					this.ch.setCharacterUI(STANDBY);
					this.ch.toggleMovingLeft();
					this.ch.gravity();
				break;

				case RIGHT0:

					if(this.ch.isMovingRight())return;
					// this.ch.setUI(WALKRIGHT);
					this.ch.setCharacterUI(WALKRIGHT);
					this.ch.toggleMovingRight();  
					this.ch.contMoveRight();
				break;

				case RIGHT1:
					this.ch.setCharacterUI(STANDBY);
					this.ch.toggleMovingRight();
					this.ch.gravity();

				break;

				default:

				break;
			}
		}
	}

}