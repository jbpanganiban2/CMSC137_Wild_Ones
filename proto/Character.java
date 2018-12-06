import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Character extends MovingObject{

	//
	//	Attributes
	//
	private final static Icon PIG_ATTACK = new ImageIcon("src/pig/pigAttackLeft.gif");
	private final static Icon PIG_STANDBY = new ImageIcon("src/pig/pigStandby.gif");
	private final static Icon PIG_WALKLEFT = new ImageIcon("src/pig/pigWalkLeft.gif");
	private final static Icon PIG_WALKRIGHT = new ImageIcon("src/pig/pigWalkRight.gif");

	private final static Icon LUB_ATTACK = new ImageIcon("src/lubglub/lubAttackLeft.gif");
	private final static Icon LUB_STANDBY = new ImageIcon("src/lubglub/standby.gif");
	private final static Icon LUB_WALKLEFT = new ImageIcon("src/lubglub/lubWalkLeft.gif");
	private final static Icon LUB_WALKRIGHT = new ImageIcon("src/lubglub/lubWalkRight.gif");

	private final static Icon DYNA_ATTACK = new ImageIcon("src/dyna/dynaAttackLeft.gif");
	private final static Icon DYNA_STANDBY = new ImageIcon("src/dyna/dynaStandby.gif");
	private final static Icon DYNA_WALKLEFT = new ImageIcon("src/dyna/dynaWalkLeft.gif");
	private final static Icon DYNA_WALKRIGHT = new ImageIcon("src/dyna/dynaWalkRight.gif");

	private static JLabel charr;
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	static final int JUMP = 0;
	static final int UP = 1;
	static final int DOWN = 2;
	static final int LEFT = 3;
	static final int RIGHT = 4;
	private static final int MOVEMENT = 5;
	private final HashSet<Integer> moves;
	private boolean alive;
	private boolean jumping;
	private boolean enabled;
	private int type;
	
	//
	//	Constructors
	//

	public Character(String name, Point init, JPanel gamePanel, int type){
		super(name, init, new Dimension(48, 50), gamePanel);

		this.moves = new HashSet<Integer>();
		this.addKeyBindings();

		this.type = type;

		this.charr= new JLabel();
		this.charr.setOpaque(false);
		this.setCharacter();
		this.add(this.charr);

		this.setLoc();
		this.gamePanel.add(this);
		this.gamePanel.addMouseListener(new RocketListener());


		this.setOpaque(false);
		this.alive = true;
		this.jumping = false;
		this.disable();

	}

	//
	//	Methods
	//
	public int getType(){
		return this.type;
	}

	public void setCharacter(){
		if(this.type==1){
			this.charr.setIcon(PIG_STANDBY);
		}else if(this.type==2){
			this.charr.setIcon(DYNA_STANDBY);
		}else{
			this.charr.setIcon(LUB_STANDBY);
		}

	}

	public synchronized void moveRight(){
		this.movePosition(MOVEMENT, 0);
	}
	public synchronized void moveLeft(){
		this.movePosition(-MOVEMENT, 0);
	}
	public synchronized void moveUp(){
		if(jumping)return;
		this.movePosition(0, -MOVEMENT);
	}
	public synchronized void moveUp(int i){
		this.movePosition(0, -i);
	}
	public synchronized void moveDown(){
		if(jumping)return;
		this.movePosition(0, MOVEMENT);
	}
	public synchronized void moveDown(int i){
		this.movePosition(0, i);
	}
	public synchronized void jump(){
		// thread that continuosly adds then subtracts y values at this position
		(new Thread(){
			@Override
			public void run(){
				jumping = true;
				int originalY = (int)position.getY(); 
				while(position.getY() > originalY-200){		// moves upward
					try{Thread.sleep(25);}catch(Exception e){e.printStackTrace();};
					System.out.print("moving up");
					System.out.println(position.getY());
					moveUp(5);
				}
				while(position.getY() != originalY){		// moves downwards
					try{Thread.sleep(25);}catch(Exception e){e.printStackTrace();};
					System.out.print("moving down");
					System.out.println(position.getY());
					moveDown(5);
				}
				jumping = false;
			}	
		}).start();
	}

	public HashSet<Integer> getMoves(){
		return this.moves;
	}

	public void addKeyBindings(){

		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"), JUMP);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("W"), UP);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("S"), DOWN);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("A"), LEFT);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("D"), RIGHT);

		this.getActionMap().put(JUMP, new Move(this, JUMP));
		this.getActionMap().put(UP, new Move(this, UP));
        	this.getActionMap().put(DOWN, new Move(this, DOWN));
        	this.getActionMap().put(LEFT, new Move(this, LEFT));
        	this.getActionMap().put(RIGHT, new Move(this, RIGHT));

	}

	public void disable(){	// disables keybindings for Character

		this.enabled = false;
		this.getActionMap().get(JUMP).setEnabled(false);
		this.getActionMap().get(UP).setEnabled(false);
        	this.getActionMap().get(DOWN).setEnabled(false);
        	this.getActionMap().get(LEFT).setEnabled(false);
        	this.getActionMap().get(RIGHT).setEnabled(false);

	}

	public void enable(){	// enables keyBindings for Character

		this.enabled = true;
		this.getActionMap().get(JUMP).setEnabled(true);
		this.getActionMap().get(UP).setEnabled(true);
        	this.getActionMap().get(DOWN).setEnabled(true);
        	this.getActionMap().get(LEFT).setEnabled(true);
        	this.getActionMap().get(RIGHT).setEnabled(true);

	}

	public synchronized void run(){
		
	}

	//
	//	Internal Classes
	//



	class RocketListener extends MouseAdapter{                  // listens to Rockets

          @Override
          public void mousePressed(MouseEvent e) {
          }

          @Override
          public void mouseReleased(MouseEvent e) {
          	if(enabled){
               	Rocket r = new Rocket("rocket", new Point(position), e.getPoint(), gamePanel);
          	}
          }
     }

     public void changeIcon(Character ch, JLabel icn){
     	ch.removeAll();
		ch.add(icn);
		ch.revalidate();
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
			JLabel nIcon = new JLabel();
			switch(this.moveType){
				case JUMP:
					if(jumping)return;
					this.ch.jump();
				break;
				case UP:
					this.ch.moveUp();
				break;
				case DOWN:
					this.ch.moveDown();
				break;
				case LEFT:
					if(this.ch.getType()==1){
						nIcon.setIcon(PIG_WALKLEFT);
					}else if(this.ch.getType()==2){
						nIcon.setIcon(DYNA_WALKLEFT);
					}else{
						nIcon.setIcon(LUB_WALKLEFT);
					}
					this.ch.changeIcon(this.ch,nIcon);
					this.ch.moveLeft();
				break;
				case RIGHT:
					if(this.ch.getType()==1){
						nIcon.setIcon(PIG_WALKRIGHT);
					}else if(this.ch.getType()==2){
						nIcon.setIcon(DYNA_WALKRIGHT);
					}else{
						nIcon.setIcon(LUB_WALKRIGHT);
					}
					this.ch.changeIcon(this.ch,nIcon);
					this.ch.moveRight();
				break;
				default:

				break;
			}


		}


     }

}