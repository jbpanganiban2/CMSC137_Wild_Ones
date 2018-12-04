import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
	
	//
	//	Constructors
	//

	public Character(Point init, JFrame gameFrame){
		super(init, new Dimension(30, 50), gameFrame);
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
}