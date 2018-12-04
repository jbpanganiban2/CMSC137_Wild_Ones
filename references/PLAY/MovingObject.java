import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//
//   MovingObject
//        a general class that is extended by all components to be rendered in-game
//

public class MovingObject extends JPanel{

     //
     //   Attributes
     //

     protected JFrame gameFrame;
     protected Point position;
     protected Dimension size;
     protected Rectangle rect;

     //
     //   Constructors
     //

     public MovingObject(Point initial, Dimension init_size, JFrame gameFrame){
          this.position = initial;
          this.size = init_size;

          this.setBackground(Color.BLACK);
          this.setSize(this.size); 
          this.rect = new Rectangle(this.position, this.size);

          this.gameFrame = gameFrame;
          // this.gameFrame.add(this);
     }

     //
     //   Methods
     //
     
     private void refresh(){                      // positions the panel in the mainpanel, called every after setLocation
          this.setBounds((int)this.position.getX(), (int)this.position.getY(), (int)this.size.getWidth(), (int)this.size.getHeight());
          // this.gameFrame.add(this);
     }

     //   methods used locally
     
     protected void movePosition(int x, int y){
          this.position.translate(x, y);
          this.refresh();
     }

     //   methods used by other players

     public void setLoc(Point p){
          // System.out.println(p);
          this.position.setLocation(p);
          // System.out.println("rocket position"+this.position);
          this.refresh();
     }

     public void setLoc(){ // sets location of object in its init point
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

}