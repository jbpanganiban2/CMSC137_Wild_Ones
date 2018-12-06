import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Prompt extends JFrame{

     Prompt(String s, int time){     
          super();    
          this.setFocusable(false);
          this.setLayout(new FlowLayout());
          this.add(new JLabel(s));
          this.setDefaultCloseOperation(0);
          this.setSize(new Dimension(s.length()*10,25));
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
