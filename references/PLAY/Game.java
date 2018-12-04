import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Game{
	
	//
	//	Attributes
	//


	JFrame gameFrame; 
	Character ch;  
	JPanel mainPanel;
	boolean isFinished;

	//
	//	Constructors
	//

	Game(){
		this.gameFrame = new JFrame("Game"); //instantiate
		this.ch = new Character(new Point(50,50), this.gameFrame);
		this.mainPanel = new JPanel();
		this.isFinished = true;
		createGame();

	}

	public void createGame(){
		

		//background
		ImageIcon icon = new ImageIcon("./src/LobbyBG.png"); 
		Image newimg = icon.getImage().getScaledInstance(730, 700,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		ImagePanel bg = new ImagePanel(newIcon.getImage());
		bg.setPreferredSize(new Dimension(730,700));
		bg.setLayout(new BorderLayout());

		Rocket r = new Rocket(ch, new Point(500,500), gameFrame, 0);

		this.ch.setLoc(); //method from Character class, sets position in the mainPanel
          r.setLoc();

          // r.printTrajectory();

		// Rocket r = new Rocket(ch, e.getPoint(), gameFrame, 0);
		
		mainPanel.setLayout(null); //set layout to null so character can be anywhere
		mainPanel.setOpaque(false); 
		mainPanel.setPreferredSize(new Dimension(730,700));
		
		mainPanel.add(ch); //add character to mainPanel
          mainPanel.add(r);
		bg.add(mainPanel); //add mainpanel to panel with bg
		
      	gameFrame.addKeyListener(new KeyListener(){		// listens to horizontal moves
			// Set of currently pressed keys
			private final Set<Integer> pressed = new HashSet<Integer>();

			@Override
			public synchronized void keyPressed(KeyEvent e) {
				pressed.add(e.getKeyCode());
				// Iterate over pressed to get the keys.
				for( Integer k : pressed ){
					switch(k){
						case KeyEvent.VK_LEFT:
							ch.moveLeft();
						break;
						case KeyEvent.VK_RIGHT:
							ch.moveRight();
						break;
						case KeyEvent.VK_UP:
							ch.moveUp();
						break;
						case KeyEvent.VK_DOWN:
							ch.moveDown();
						break;
						case KeyEvent.VK_SPACE:
							ch.moveUp();			// supposed to be jump
						break;
					}
				}
			}

			@Override
			public synchronized void keyReleased(KeyEvent e) {
				pressed.remove(e.getKeyCode());
			}

			@Override
			public void keyTyped(KeyEvent e) {/* Not used */ }
		});

		gameFrame.addMouseListener(new MouseAdapter() {
                private Color background;

                @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println("pressed at "+ e.getPoint());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // System.out.println("released at "+ e.getPoint());
                    // Rocket r = new Rocket(ch, e.getPoint(), gameFrame, 0);
                    // // r.printTrajectory();
                    // r.setLoc(ch.getPosition());
                    // mainPanel.add(r);
          		// r.deploy();
                    r.deploy();
                }
            });

		this.gameFrame.setSize(730,700);
		this.gameFrame.setResizable(false);
		this.gameFrame.setVisible(true);
		this.gameFrame.add(bg);	//add to frame
        	this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//
	//	Methods
	//

	public void startRepaint(){						// repaints the frames continuously
		this.isFinished = false;
		while(!this.isFinished){
			this.gameFrame.repaint();
		}
	}

	public void endGame(){							// sets the endgame condition
		this.isFinished = true;
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