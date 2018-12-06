import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//
//   MovingObject
//        a general class that is extended by all components to be rendered in-game
//

public class MovingObject extends JPanel implements Runnable{

     //
     //   Attributes
     //

     protected JPanel gamePanel;
     protected Point position;
     protected Dimension size;
     protected Rectangle rect;
     protected String name;

     //
     //   Constructors
     //

     public MovingObject(String name, Point initial, Dimension init_size, JPanel gamePanel){
          this.position = initial;
          this.size = init_size;
          this.name = name;

          // this.setBackground(Color.BLACK);
          this.setPreferredSize(this.size); 
          this.rect = new Rectangle(this.position, this.size);
          
          // this.setOpaque(false);
          this.gamePanel = gamePanel;
     }

     //
     //   Methods
     //
     
     private void refresh(){                           // positions the panel in the mainpanel, called every after setLocation
          this.setBounds((int)this.position.getX(), (int)this.position.getY(), (int)this.size.getWidth(), (int)this.size.getHeight());
          this.gamePanel.repaint();
     }

     //   methods used locally
     
     protected void movePosition(int x, int y){
          this.position.translate(x, y);
          this.refresh();     
     }

     //   methods used by other players

     public void setLoc(Point p){
          this.position.setLocation(p);
          this.refresh();
     }

     public void setLoc(){                             // sets location of object in its init point
          this.refresh();
     }

     public void setX(int x){
          this.position.setLocation(x,this.position.getY());
     }

     public void setY(int y){
          this.position.setLocation(this.position.getX(),y);
     }

     // getters

     public Point getPosition(){
          return this.position;
     }

     public void run(){
          /*none*/
     }

     public void deploy(){
          Thread t = new Thread(this);
          t.start();
     }

}