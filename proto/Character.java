import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


//
//	Possible starting points
//		p1 = 75,555
//		p2 = 635,555
//		p3 = 
//
//

public class Character extends MovingObject{

	//
	//	Attributes
	//
	private final static Icon ATTACK = new ImageIcon("src/pig/pigAttackLeft.gif");
	private final static Icon STANDBY = new ImageIcon("src/pig/pigStandby.gif");
	private final static Icon WALKLEFT = new ImageIcon("src/pig/pigWalkLeft.gif");
	private final static Icon WALKRIGHT = new ImageIcon("src/pig/pigWalkRight.gif");
	private static JLabel charr;
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	static final int UP = 1;
	static final int DOWN = 2;
	static final int LEFT = 3;
	static final int RIGHT = 4;
	private static final int MOVEMENT = 5;
	private final HashSet<Integer> moves;
	private boolean alive;
	
	//
	//	Constructors
	//

	public Character(String name, Point init, JPanel gamePanel){
		super(name, init, new Dimension(30, 50), gamePanel);

		this.moves = new HashSet<Integer>();
		this.alive = true;

		this.setLoc();
		this.gamePanel.add(this);
		this.gamePanel.addMouseListener(new RocketListener());

		this.charr = new JLabel();
		this.charr.setOpaque(true);
		this.charr.setIcon(STANDBY);
		
		this.add(charr);

		this.addKeyBindings();
	}

	//
	//	Methods
	//

	public void moveRight(){
		this.movePosition(MOVEMENT, 0);
	}
	public void moveLeft(){
		this.movePosition(-MOVEMENT, 0);
	}
	public void moveUp(){
		this.movePosition(0, -MOVEMENT);
	}
	public void moveDown(){
		this.movePosition(0, MOVEMENT);
	}

	public HashSet<Integer> getMoves(){
		return this.moves;
	}

	public void addKeyBindings(){

		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("W"), UP);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("S"), DOWN);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("A"), LEFT);
		this.getInputMap(IFW).put(KeyStroke.getKeyStroke("D"), RIGHT);

		this.getActionMap().put(UP, new Move(this, UP));
        	this.getActionMap().put(DOWN, new Move(this, DOWN));
        	this.getActionMap().put(LEFT, new Move(this, LEFT));
        	this.getActionMap().put(RIGHT, new Move(this, RIGHT));

	}

	public void disable(){

		System.out.println("disabled");
		Action a = null; 
		ActionMap aa = this.getActionMap();
		for(Object o : aa.allKeys()){
			a = aa.get(o);
			a.setEnabled(false);
		}

	}

	public void enable(){

		Action a = null; 
		ActionMap aa = this.getActionMap();
		for(Object o : aa.allKeys()){
			a = aa.get(o);
			a.setEnabled(true);
		}

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
               Rocket r = new Rocket("rocket", new Point(position), e.getPoint(), gamePanel, 0);
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
			switch(this.moveType){
				case UP:
					this.ch.moveUp();
				break;
				case DOWN:
					this.ch.moveDown();
				break;
				case LEFT:
					this.ch.moveLeft();
				break;
				case RIGHT:
					this.ch.moveRight();
				break;
				default:

				break;
			}
		}


     }

}