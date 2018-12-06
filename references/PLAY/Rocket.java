import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Rocket extends MovingObject{

     //
     //  Attributes
     //

     private Point charPosition;
     private Point cursorPosition;
     private int type;                       // 0 if gravity rocket, 1 if normal rocket
     private ArrayList<Point> trajectory;

     //
     //  Constructors
     //

     public Rocket(String name, Point charPosition, Point cursorPosition, JPanel gamePanel, int type){
          super(name, charPosition, new Dimension(10, 10), gamePanel);
          this.charPosition = charPosition;
          this.type = type;
          this.trajectory = new ArrayList<Point>();
          this.cursorPosition = cursorPosition;

          this.setLoc(charPosition);
          this.gamePanel.add(this);

          this.getTrajectory();
          this.deploy();

     }

     //
     //  Methods
     //

     private void getTrajectory(){
          int start = (int)this.charPosition.getX();                  //   character X pos
          int cX = (int)cursorPosition.getX();                        //   cursor X pos
          int right = 720;                                            //   right bound of the window 
          int left = 0;                                               //   left bound of the window
          int increment = 0;                                          //   increment of domain
          int end = 720;

          if(start < cX){
               System.out.println("going right");
               end = 720;
               increment = 1;
          }else if(start > cX){
               System.out.println("going left");
               end = 10;
               increment = -1;
          }else{
               // rocket is perfectly going down/up
               // setTrajectory to a downard/upward line
               return;
          }

          for(int i = start; i != end; i+=increment ){
               this.trajectory.add(getPositionAtX(i));
          }
     }

     private Point getPositionAtX(int x){

          double x1 = this.charPosition.getX();
          double y1 = this.charPosition.getY();
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

     public void run(){
          for(Point p : trajectory){

               try{Thread.sleep(5);}catch(Exception e){e.printStackTrace();};
               this.setLoc(p);
               
          }
          setVisible(false);
     }

     //
     //   Internal Classes
     //


}