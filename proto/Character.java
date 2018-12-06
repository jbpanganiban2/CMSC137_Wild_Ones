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
	private static JLabel charr;
	static final int JUMP = 0;
	static final int UP = 1;
	static final int DOWN = 2;
	static final int LEFT0 = 30;	// pressed
	static final int LEFT1 = 31;	// released
	static final int RIGHT0 = 40;
	static final int RIGHT1 = 41;
	private static int movement = 5;
	private final HashSet<Integer> moves;

	private boolean jumping;
	private boolean movingRight;
	private boolean movingLeft;
	private boolean enabled;
	private int time;
	
	//
	//	Constructors
	//

	public Character(String name, Point init, JPanel gamePanel){
		super(name, init, new Dimension(30, 50), gamePanel);

		this.moves = new HashSet<Integer>();
		this.addKeyBindings();

		this.setLoc();
		this.gamePanel.add(this);
		this.gamePanel.addMouseListener(new RocketListener());

		this.charr = new JLabel();
		this.charr.setOpaque(true);
		this.charr.setIcon(STANDBY);
		this.add(charr);
		
		this.jumping = false;
		this.movingLeft = false;
		this.movingRight = false;
		
		this.disable();
	}

	public Character(Player p, Point init, JPanel gamePanel){
		super(p.getName(), init, new Dimension(30, 50), gamePanel);

		this.moves = new HashSet<Integer>();
		this.addKeyBindings();

		this.setLoc();
		this.gamePanel.add(this);
		this.gamePanel.addMouseListener(new RocketListener());

		this.charr = new JLabel();
		this.charr.setOpaque(true);
		this.charr.setIcon(STANDBY);
		this.add(charr);
		
		this.jumping = false;
		this.movingLeft = false;
		this.movingRight = false;
		
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

	public synchronized void moveRight(){
		this.movePosition(movement, 0);
	}
	public synchronized void moveRight(int i){
		this.movePosition(i, 0);
	}
	public synchronized void moveLeft(){
		this.movePosition(-movement, 0);
	}
	public synchronized void moveLeft(int i){
		this.movePosition(-i, 0);
	}
	public synchronized void moveUp(){
		if(jumping)return;
		this.movePosition(0, -movement);
	}
	public synchronized void moveUp(int i){
		this.movePosition(0, -i);
	}
	public synchronized void moveDown(){
		if(jumping)return;
		this.movePosition(0, movement);
	}
	public synchronized void moveDown(int i){
		this.movePosition(0, i);
	}
	public synchronized void jump(){
		// thread that continuosly adds then subtracts y values at this position
		// fix bounds arguments
		(new Thread(){
			@Override
			public void run(){
				jumping = true;
				movement = 12;
				int originalY = (int)position.getY(); 
				while(position.getY() > originalY-200){		// moves upward
					moveUp(6);
					try{Thread.sleep(12);}catch(Exception e){e.printStackTrace();};
				}
				while(position.getY() != originalY){		// moves downwards
					moveDown(6);
					try{Thread.sleep(12);}catch(Exception e){e.printStackTrace();};
				}
				jumping = false;
				movement = 12;
			}	
		}).start();
	}

	// 	rise(position, originalY-200);
	// 			fall(position, originalY);
	// 			jumping = false;
	// 			movement = 12;
	// 		}	
	// 	}).start();
	// }

	// public synchronized rise(Point charPosition, int target){
	// 	while(charPosition.getY() > target){		// moves upward
	// 		moveUp(6);
	// 		try{Thread.sleep(12);}catch(Exception e){e.printStackTrace();};
	// 	}
	// }

	// public synchronized fall(Point charPosition, int target){
	// 	while(charPosition.getY() < target){		// moves downward
	// 		moveDown(6);
	// 		try{Thread.sleep(12);}catch(Exception e){e.printStackTrace();};
	// 	}
	// }


	public HashSet<Integer> getMoves(){
		return this.moves;
	}

	public synchronized int getTimeLeft(){
		return this.time--;
	}

	public void addKeyBindings(){

		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"), JUMP);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), LEFT0);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), LEFT1);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,false), RIGHT0);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0,true), RIGHT1);

		this.getActionMap().put(JUMP, new Move(this, JUMP));
        	this.getActionMap().put(LEFT0, new Move(this, LEFT0));
        	this.getActionMap().put(LEFT1, new Move(this, LEFT1));
        	this.getActionMap().put(RIGHT0, new Move(this, RIGHT0));
        	this.getActionMap().put(RIGHT1, new Move(this, RIGHT1));

	}

	public synchronized void disable(){	// disables keybindings for Character

		this.enabled = false;
		this.getActionMap().get(JUMP).setEnabled(false);
	  	this.getActionMap().get(LEFT0).setEnabled(false);
	  	this.getActionMap().get(LEFT1).setEnabled(false);
	  	this.getActionMap().get(RIGHT0).setEnabled(false);
	  	this.getActionMap().get(RIGHT1).setEnabled(false);

	}

	public synchronized void enable(){	// enables keyBindings for Character

		this.time = 25;
		this.enabled = true;
		this.getActionMap().get(JUMP).setEnabled(true);
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
		if(this.enabled && this.time != 0){
          	new Rocket("rocket", this, new Point(this.position), e.getPoint(), this.gamePanel, 0).alwaysOnCollisionChecker(MovingObject.rnc);
          	this.time = 0;
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
				case JUMP:
					if(jumping)return;
					this.ch.jump();
				break;
				case LEFT0:						// when key is pressed, enables the character to move 
					if(this.ch.isMovingLeft())return;	// when key is ALREADY PRESSED, returns
					this.ch.toggleMovingLeft();	
					(new Thread(){

						@Override
						public void run(){
							while(ch.isMovingLeft()){
								try{Thread.sleep(25);}catch(Exception exc){exc.printStackTrace();};
								ch.moveLeft(5);
							}
						}
					}).start();
				break;
				case LEFT1:						// removes key's ALREADY PRESSED state
					this.ch.toggleMovingLeft();
				break;
				case RIGHT0:
					if(this.ch.isMovingRight())return;
					this.ch.toggleMovingRight();	// encounters a false, then
					(new Thread(){
						@Override
						public void run(){
							while(ch.isMovingRight()){
								try{Thread.sleep(25);}catch(Exception exc){exc.printStackTrace();};
								ch.moveRight(5);
							}
						}
					}).start();
				break;
				case RIGHT1:
					this.ch.toggleMovingRight();
				break;
				default:

				break;
			}
		}


     }

}