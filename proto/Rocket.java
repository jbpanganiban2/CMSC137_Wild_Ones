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
     private Character c;

     //
     //  Constructors
     //

     public Rocket(String name, Character c, Point charPosition, Point cursorPosition, Game g, int type){
          super(name, charPosition, new Dimension(10, 10), g);
          this.type = type;

          this.charPosition = charPosition;
          this.cursorPosition = cursorPosition;
          this.c = c;

          this.setBackground(Color.BLACK);
          this.setLoc(charPosition);
          this.gamePanel.add(this);

          this.trajectory = new ArrayList<Point>();
          this.getTrajectory();
          // this.printTrajectory();
          this.deploy();

     }

     //
     //  Methods
     //

     private void getTrajectory(){

          int start = (int)this.charPosition.getX();                  //   character X pos
          int cX = (int)this.cursorPosition.getX();                        //   cursor X pos
          System.out.println(start);
          System.out.println(cX);
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

          Point y = null;
          int yVal;

          // (new Thread(){

          //      int start;
          //      int end;
          //      int increment;

          //      public Thread setValues(int s, int e, int i){
          //           this.start = s;
          //           this.end = e;
          //           this.increment = i;
          //           return this;
          //      }

          //      @Override
          //      public void run(){
          //           for(int i = this.start; i != this.end; i+=this.increment ){
          //                trajectory.add(getPositionAtX(i));
          //           }
          //      }
          // }).setValues(start, end, increment).start();
          for(int i = start; i != end; i+=increment ){
               trajectory.add(getPositionAtX(i));
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
               System.out.print(p);
          }
     }

     public synchronized GameObject hasCollision(ArrayList<GameObject> m){     // returns the GameObject collided with, else returns null
          try{
               for(GameObject o : m){
                    if(o instanceof MovingObject){
                         MovingObject mo = (MovingObject)o;
                         if(this.intersects(mo) && !mo.equals(this) && !mo.equals(this.c)){
                              /*
                                   invoke some things
                               */
                              return mo;
                         }
                    }
                    else if(o instanceof Obstacles){
                         Obstacles mo = (Obstacles)o;
                         if(this.intersects(mo) && !mo.equals(this) && !mo.equals(this.c)){
                              /*
                                   invoke some things
                               */
                              return mo;
                         }
                    }
               }return null;
          }catch(Exception e){}
          return null;
     }

     @Override
     public void hasCollided(MovingObject m){                    // what will happen after this hasCollided with a MovingObject m
          // explosion animation
          this.alive = false;
          this.collided = true;
          if(m instanceof Character)
               ((Character)m).damaged();
     }

     @Override
     public void hasCollided(Obstacles m){                       // what will happen after this hasCollided with Obstacle
          System.out.println(this.name+" hit an obstacle");
          this.alive = false;
          this.collided = true;
     }

     public void run(){
          System.out.println(trajectory.size());
          for(Point p : trajectory){

               if(this.alive){
                    try{Thread.sleep(5);}catch(Exception e){e.printStackTrace();};
                    this.setLoc(p);
               }
               
          }
          this.g.getGameObjects().remove(this);
          // this.c.setTimeZero();
          setVisible(false);
     }

     //
     //   Internal Classes
     //


}

