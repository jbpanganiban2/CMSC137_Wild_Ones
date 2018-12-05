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

	private static final int MOVEMENT = 5;
	private final HashSet<Integer> moves;
	private boolean alive;
	
	//
	//	Constructors
	//

	public Character(String name, Point init, JPanel gamePanel, JFrame gameFrame){
		super(name, init, new Dimension(30, 50), gamePanel);

		this.moves = new HashSet<Integer>();
		this.alive = true;

		this.setLoc();
		this.gamePanel.add(this);
		this.gamePanel.addMouseListener(new RocketListener());
		gameFrame.addKeyListener(new MovementListener(this));
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

     class MovementListener extends KeyAdapter{
		// Set of currently pressed keys
		private final Set<Integer> pressed;
		private Character ch;


		MovementListener(Character ch){
			super();
			this.ch = ch;
			this.pressed = ch.getMoves();
			System.out.println("movementListener added");
		}

		@Override
		public synchronized void keyPressed(KeyEvent e) {
			pressed.add(e.getKeyCode());
			if(ch.alive){
				for( Integer k : pressed ){
					switch(k){
						case KeyEvent.VK_A:
							ch.moveLeft();
						break;
						case KeyEvent.VK_D:
							ch.moveRight();
						break;
						case KeyEvent.VK_W:
							ch.moveUp();
						break;
						case KeyEvent.VK_S:
							ch.moveDown();
						break;
						case KeyEvent.VK_SPACE:
							ch.moveUp();			// supposed to be jump
						break;
					}
				}
			}	
		}

		@Override
		public synchronized void keyReleased(KeyEvent e) {
			pressed.remove(e.getKeyCode());
		}

		@Override
		public void keyTyped(KeyEvent e) {/* Not used */ }
	}
}