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
		// this.setBackground(Color.BLACK);
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
				render(597,347,134,194);
				break;
			case 4:
				render(514,411,84,131);
				break;
			case 5:
				render(0,208,200,57);
				break;
			case 6:
				render(78,60,250,45);
				break;
			case 7:
				render(426,158,64,64);
				break;
			case 8:
				render(193,269,78,78);
				break;
			case 9:
				render(482,248,140,50);
				break;
			case 10:
				render(630,320,82,28);
				break;
			case 11:
				render(644,294,27,27);
			case 12:
				render(300,118,127,19);
				break;
			case 13:
				render(584,156,127,19);
				break;
			case 14:
				render(500,220,60,30);
				break;
			case 15:
				render(500,190,30,30);
				break;
			case 16:
				render(88,436,90,30);
				break;
			case 17:
				render(40,180,30,30);
				break;
			case 18:
				render(644,292,27,27);
				break;
			case 19:
				render(216,438,90,30);
				break;
			case 20:
				render(246,406,60,30);
				break;
			case 21:
				render(276,376,30,30);
				break;
			default:
				break;
		}
	}
}