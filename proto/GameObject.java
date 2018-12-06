import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public interface GameObject{

     public Rectangle getRectangle();
     public boolean intersects(GameObject g);
     
}