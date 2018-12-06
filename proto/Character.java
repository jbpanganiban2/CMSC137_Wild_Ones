import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Character extends MovingObject{

	//
	//	Attributes
	//
	private final static Icon ATTACK = new ImageIcon("src/pig/pigAttackLeft.gif");
	private final static Icon STANDBY = new ImageIcon("src/pig/pigStandby.gif");
	private final static Icon WALKLEFT = new ImageIcon("src/pig/pigWalkLeft.gif");
	private final static Icon WALKRIGHT = new ImageIcon("src/pig/pigWalkRight.gif");

	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	private JLabel charr;
	static final int JUMP0 = 01;
	static final int JUMP1 = 02;
	static final int LEFT0 = 30;	// pressed
	static final int LEFT1 = 31;	// released
	static final int RIGHT0 = 40;
	static final int RIGHT1 = 41;
	private static int movement = 3;

	private int health;
	private boolean jumping;
	private boolean movingRight;
	private boolean movingLeft;
	private boolean enabled;
	private boolean deployedRocket;
	private int time;
	private boolean xCollide;
	private boolean yCollide;
	
	//
	//	Constructors
	//

	public Character(String name, Point init, Game g){
		super(name, init, new Dimension(30, 50), g);
		this.initchar();
	}

	public Character(Player p, Point init, Game g){
		super(p.getName(), init, new Dimension(30, 50), g);
		this.initchar();
		
	}

	private void initchar(){
		this.addKeyBindings();

		this.setLoc();
		this.gamePanel.add(this);
		this.gamePanel.addMouseListener(new RocketListener());

		this.charr = new JLabel();
		this.charr.setOpaque(false);
		this.charr.setIcon(STANDBY);
		this.add(charr);
		
		this.jumping = false;
		this.movingLeft = false;
		this.movingRight = false;
		this.health = 10;
		
		this.setOpaque(false);
		this.disable();
	}


	//
	//	Methods
	//
	
	public synchronized void endTurn(){
		this.movingLeft = false;
		this.movingRight = false;
		this.disable();
	}

	public void setUI(Icon i){
		this.charr.setIcon(i);
	}

	public synchronized void moveRight(){
		Point test = new Point((int)this.position.getX()+movement, ((int)this.position.getY()));
		if((this.hasCollision(new Rectangle(test, this.size),this.g.getGameObjects())) != null){
			this.xCollide = true;
			return;
		}

		this.movePosition(movement, 0);
	}

	public synchronized void moveLeft(){
		Point test = new Point((int)this.position.getX()-movement, ((int)this.position.getY()));
		if((this.hasCollision(new Rectangle(test, this.size),this.g.getGameObjects())) != null){
			this.xCollide = true;
			return;
		}

		this.movePosition(-movement, 0);
	}
	public synchronized void moveUp(){
		if(jumping)return;

		// checks first if there will be a collision before moving
		
		Point test = new Point((int)this.position.getX(), ((int)this.position.getY())-movement);
		if((this.hasCollision(new Rectangle(test, this.size),this.g.getGameObjects())) != null){
			this.yCollide = true;
			return;
		}

		this.movePosition(0, -movement);
	}
	public synchronized void moveDown(){
		if(jumping)return;

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
				// System.out.println("ran Thread");
				int originalY = (int)position.getY();
				int target = originalY-100; 
				while(position.getY() > target){							// moves upward
					if(yCollide)break;
					
					try{Thread.sleep(15);}catch(Exception e){e.printStackTrace();};
					moveUp();
				}
				yCollide = false;
				while(position.getY() < 550-((int)size.getHeight())){				// moves downwards
					if(yCollide)break;
					try{Thread.sleep(15);}catch(Exception e){e.printStackTrace();};
					moveDown();
				}
				yCollide = false;
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

	public synchronized int getTimeLeft(){
		return this.time--;
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

	public synchronized void disable(){	// disables keybindings for Character

		this.enabled = false;
	  	this.getActionMap().get(JUMP0).setEnabled(false);
	  	this.getActionMap().get(JUMP1).setEnabled(false);
	  	this.getActionMap().get(LEFT0).setEnabled(false);
	  	this.getActionMap().get(LEFT1).setEnabled(false);
	  	this.getActionMap().get(RIGHT0).setEnabled(false);
	  	this.getActionMap().get(RIGHT1).setEnabled(false);

	}

	public synchronized void enable(){	// enables keyBindings for Character

		this.time = 25;
		this.enabled = true;
		this.deployedRocket = false;
	  	this.getActionMap().get(JUMP0).setEnabled(true);
	  	this.getActionMap().get(JUMP1).setEnabled(true);
	  	this.getActionMap().get(LEFT0).setEnabled(true);
	  	this.getActionMap().get(LEFT1).setEnabled(true);
	  	this.getActionMap().get(RIGHT0).setEnabled(true);        	
	  	this.getActionMap().get(RIGHT1).setEnabled(true);

	}

	public synchronized boolean isEnable(){
		return this.enabled;
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

	private void deployRocket(MouseEvent e){

		if(this.enabled && this.time != 0 && !this.deployedRocket){
          	new Rocket("rocket", this, new Point(this.position), e.getPoint(), this.g, 0).alwaysOnCollisionChecker(MovingObject.gameObjects);
          	// this.time = 0;
     		this.deployedRocket = true;
     	}
	}

	public void setTimeZero(){
		this.time = 0;
	}

	private static int rng(int max, int min){	// produces a random number between [max, min]
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
			this.setVisible(false);
			this.g.getGamePanel().invalidate();
			this.g.getGamePanel().validate();
		}
	}

	//
	//	Internal Classes
	//


	class RocketListener extends MouseAdapter{                  // listens to Rockets

          @Override
          public void mousePressed(MouseEvent e) {
          }

          @Override
          public synchronized void mouseReleased(MouseEvent e) {
          	deployRocket(e);
          }
     }

     class Move extends AbstractAction {				//	how the character will move

     	Character ch;
     	int moveType;

     	Move(Character ch, int moveType){
     		this.ch = ch;
     		this.moveType = moveType;
     	}

     	@Override
		public void actionPerformed(ActionEvent e) {
			switch(this.moveType){
				case JUMP0:

					if(jumping)return;
					jumping = true;
					this.ch.jump();
				break;

				case JUMP1:

					jumping = false;
				break;

				case LEFT0:						// when key is pressed, enables the character to move 

					if(this.ch.isMovingLeft())return;	// when key is ALREADY PRESSED, returns
					this.ch.setUI(WALKLEFT);
					this.ch.toggleMovingLeft();	
					this.ch.contMoveLeft();
				break;

				case LEFT1:						// removes key's ALREADY PRESSED state

					this.ch.setUI(STANDBY);
					this.ch.toggleMovingLeft();
				break;

				case RIGHT0:

					if(this.ch.isMovingRight())return;
					this.ch.setUI(WALKRIGHT);
					this.ch.toggleMovingRight();	
					this.ch.contMoveRight();
				break;

				case RIGHT1:

					this.ch.setUI(STANDBY);
					this.ch.toggleMovingRight();
				break;

				default:

				break;
			}
		}


     }

}