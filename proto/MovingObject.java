import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

//
//   MovingObject
//        a general class that is extended by all components to be rendered in-game
//

public class MovingObject extends JPanel implements Runnable, GameObject{

     //
     //   Attributes
     //

     protected JPanel gamePanel;
     protected Point position;
     protected Dimension size;

     protected String name;
     protected boolean alive;
     protected boolean collided;

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
          this.collided = false;

          
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

     public String getUserName(){
          return this.name;
     }

     public Rectangle getRectangle(){
          return new Rectangle(this.position, this.size);
     }

     public synchronized boolean intersects(GameObject m){                       // checks rectangles of objects if the intersect
          return this.getRectangle().intersects(m.getRectangle());
     }

     public synchronized GameObject hasCollision(ArrayList<GameObject> m){     // returns the GameObject collided with, else returns null
          try{
               for(GameObject o : m){
                    if(o instanceof MovingObject){
                         MovingObject mo = (MovingObject)o;
                         if(this.intersects(mo) && !mo.equals(this)){
                              /*
                                   invoke some things
                               */
                              return mo;
                         }
                    }else if(o instanceof Obstacles){
                         Obstacles mo = (Obstacles)o;
                         if(this.intersects(mo)){
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
     
     public synchronized GameObject hasCollision(Rectangle test, ArrayList<GameObject> m){     // returns the GameObject collided with, else returns null
          try{
               for(GameObject o : m){
                    if(o instanceof MovingObject){
                         MovingObject mo = (MovingObject)o;
                         if(test.intersects(mo.getRectangle()) && !mo.equals(this)){
                              /*
                                   invoke some things
                               */
                              return mo;
                         }
                    }else if(o instanceof Obstacles){
                         Obstacles mo = (Obstacles)o;
                         if(test.intersects(mo.getRectangle())){
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


     public void hasCollided(MovingObject m){                  // main method that will do something with both the collided objects
          // System.out.println(this.name+" kaboomed with "+m.getObjName());
     }

     public void hasCollided(Obstacles m){                  // main method that will do something with both the collided objects
          // System.out.println(this.name+" hit an obstacle");
          this.collided = true;
     }

     protected synchronized void alwaysOnCollisionChecker(ArrayList<GameObject> m){     //   Checks which objects from the list this moving GameObject has collided with;

          (new Thread(){
               @Override
               public synchronized void run(){
                    GameObject test;
                    while(alive){
                         if((test = hasCollision(m)) != null){
                              if(test instanceof MovingObject)
                                   hasCollided((MovingObject)test);
                              else if(test instanceof Obstacles)
                                   hasCollided((Obstacles)test);

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