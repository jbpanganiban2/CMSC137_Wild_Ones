import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Obstacles extends JPanel{
	int block;
	int xPos;
	int yPos;
	int width;
	int height;
	Obstacles(int num){
		this.block=num;
		createObstacle(this.block);
		this.setBackground(Color.BLACK);
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
				render(0,466,312,83);
				break;
			case 2:
				render(351,373,119,190);
				break;
			case 3:
				render(600,353,134,194);
				break;
			case 4:
				render(518,418,84,131);
				break;
			case 5:
				render(0,208,200,57);
				break;
			case 6:
				render(480,96,250,45);
				break;
			case 7:
				render(400,155,64,64);
				break;
			case 8:
				render(193,298,78,78);
				break;
			case 9:
				render(480,266,140,50);
				break;
			case 10:
				render(630,320,82,28);
				break;
			case 11:
				render(644,294,27,27);
				break;
			default:
				break;
		}
	}
}