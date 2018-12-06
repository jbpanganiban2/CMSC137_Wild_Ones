import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

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
     protected String name;

     protected boolean alive;

     protected static ArrayList<MovingObject> rnc = new ArrayList<MovingObject>();            // rockets 'n characters

     //
     //   Constructors
     //

     public MovingObject(String name, Point initial, Dimension init_size, JPanel gamePanel){
          this.position = initial;
          this.size = init_size;
          this.name = name;

          // this.setBackground(Color.BLACK);
          this.setPreferredSize(this.size); 
          this.alive = true;
          
          this.setOpaque(true);
          this.gamePanel = gamePanel;

          MovingObject.rnc.add(this);
     }

     //
     //   Methods
     //
     
     private void refresh(){                                          // positions the panel in the mainpanel, called every after setLocation
          this.setBounds((int)this.position.getX(), (int)this.position.getY(), (int)this.size.getWidth(), (int)this.size.getHeight());
     }

     public String getUserName(){
          return this.name;
     }

     public Rectangle getRectangle(){
          return new Rectangle(this.position, this.size);
     }

     public synchronized boolean intersects(MovingObject m){                       // checks rectangles of objects if the intersect
          return this.getRectangle().intersects(m.getRectangle());
     }

     public synchronized MovingObject hasCollision(ArrayList<MovingObject> m){     // returns the object collided with, else returns null
          for(MovingObject o : m){
               if(this.intersects(o)){
                    /*
                         invoke some things
                     */
                    return o;
               }
          }return null;
     }

     public void hasCollided(MovingObject m){                  // main method that will do something with both the collided objects
          System.out.println(this.name+" kaboomed with "+m.getObjName());
     }

     protected synchronized void alwaysOnCollisionChecker(ArrayList<MovingObject> m){     //   Checks which objects from the list this moving object has collided with;

          (new Thread(){
               @Override
               public synchronized void run(){
                    MovingObject test;
                    while(alive){
                         if((test = hasCollision(m)) != null){
                              hasCollided(test);
                         }
                    }
               }
          }).start();

     }

     public synchronized boolean isAlive(){
          return this.alive;
     }

     public String getObjName(){
          return this.name;
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