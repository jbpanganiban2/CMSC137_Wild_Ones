import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Prompt extends JFrame{

     public Prompt(String str, int time){
          super();
          this.setLayout(new FlowLayout());
          this.add(new JLabel(str));
          this.setSize(new Dimension(str.length() * 10,20));
          this.setDefaultCloseOperation(0);
          (new Thread(){
               @Override
               public void run(){
                    setVisible(true);
                    try{Thread.sleep(time);}catch(Exception e){e.printStackTrace();};
                    dispose();
               }
          }).start();
     }

}