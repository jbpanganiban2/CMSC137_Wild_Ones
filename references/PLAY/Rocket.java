import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Rocket extends MovingObject{

     //
     //  Attributes
     //

     private Character ch;
     private int type;                       // 0 if gravity rocket, 1 if normal rocket
     private ArrayList<Point> trajectory;
     private Point cursorPosition;

     //
     //  Constructors
     //

     public Rocket(Character ch, Point cursorPosition, JFrame gameFrame, int type){
          super(ch.getPosition(), new Dimension(10, 10), gameFrame);
          this.ch = ch;
          this.type = type;
          this.trajectory = new ArrayList<Point>();
          this.cursorPosition = cursorPosition;

          this.getTrajectory();


          //  runs a thread that immediately starts and moves the rocket to a certain angle relative to the getPostition
          //  When it hits object or bounds, explodes
          //    if object is hit, damages the object
     }

     //
     //  Methods
     //

     private void getTrajectory(){
          int start = (int)this.ch.getPosition().getX();
          int end = (int)this.cursorPosition.getX();

          System.out.println(start + " < " + end);

          if(start < end){
               for(int i = start; i < end; i++ ){
                    System.out.println(i);
                    this.trajectory.add(getPositionAtX(i));
               }
          }
     }

     private Point getPositionAtX(int x){

          double x1 = this.ch.getPosition().getX();
          double y1 = this.ch.getPosition().getY();
          double x2 = this.cursorPosition.getX();
          double y2 = this.cursorPosition.getY();

          double slope = (y2 - y1) / (x2 - x1);

          return new Point(x, (int)( y1 + ( slope ) * (x - x1) ) );
     }

     public void printTrajectory(){
          for(Point p : this.trajectory){
               System.out.println(p);
          }
     }

     public void deploy(){

          for(Point p : this.trajectory){
               try{Thread.sleep(100);}catch(Exception e){e.printStackTrace();};
               // System.out.println(p);
               this.setLoc(p);
               // this.repaint();
               // this.movePosition(, );
          }
     }

}