import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Obstacles extends JPanel implements GameObject{
	int block;
	int xPos;
	int yPos;
	int width;
	int height;

	Obstacles(int num){
		this.block=num;
		createObstacle(this.block);
		this.setBackground(Color.BLACK);
		this.setOpaque(false);
	}

	public int getX(){
		return this.xPos;
	}
	public int getY(){
		return this.yPos;
	}
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	public Rectangle getRectangle(){
		return new Rectangle(new Point(this.xPos, this.yPos), new Dimension(this.width,this.height));
	}

	public boolean intersects(GameObject o){
		return this.getRectangle().intersects(o.getRectangle());
	}

	public void render(int a, int b, int c, int d){
		this.setPreferredSize(new Dimension(c,d));
		this.xPos = a;
		this.yPos = b;
		this.width = c;
		this.height = d;
	}

	public void createObstacle(int blk){
		switch(blk){
			case 1:
				render(0,466,277,87);
				break;
			case 2:
				render(344,373,119,190);
				break;
			case 3:
				render(607,347,124,194);
				break;
			case 4:
				render(524,411,84,131);
				break;
			case 5:
				render(0,208,200,24);
				break;
			case 6:
				render(78,60,252,20);
				break;
			case 7:
				render(377,124,64,40);
				break;
			// case 8:
			// 	render(193,269,78,78);
			// 	break;
			case 9:
				render(462,248,140,17);
				break;
			case 10:
				render(650,320,82,28);
				break;
			// case 11:
			// 	render(644,294,27,27);
			// 	break;
			case 12:
				render(252,252,127,19);
				break;
			case 13:
				render(584,156,127,9);
				break;
			case 14:
				render(500,220,60,30);
				break;
			case 15:
				render(500,190,30,30);
				break;
			case 16:
				render(44,436,90,30);
				break;
			case 17:
				render(40,180,30,30);
				break;
			case 18:
				render(704,292,27,27);
				break;
			case 19:
				render(183,438,90,30);
				break;
			case 20:
				render(214,406,60,30);
				break;
			case 21:
				render(244,376,30,30);
				break;
			default:
				break;
		}
	}
}