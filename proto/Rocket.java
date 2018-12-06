import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Rocket extends MovingObject{

     //
     //  Attributes
     //
     private final  Icon ROCKET = new ImageIcon("src/fire/fire.gif");

     private Point charPosition;
     private Point cursorPosition;
     private ArrayList<Point> trajectory;
     private JLabel rocket;
     //
     //  Constructors
     //

     public Rocket(String name, Point charPosition, Point cursorPosition, JPanel gamePanel){
          super(name, charPosition, new Dimension(40, 50), gamePanel);
          this.charPosition = charPosition;
          this.trajectory = new ArrayList<Point>();
          this.cursorPosition = cursorPosition;

          this.rocket=new JLabel();
          this.rocket.setOpaque(false);
          this.rocket.setIcon(ROCKET);
          this.add(rocket);
          this.setLoc(charPosition);

          this.setOpaque(false);
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
               end = 720;
               increment = 1;
          }else if(start > cX){
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