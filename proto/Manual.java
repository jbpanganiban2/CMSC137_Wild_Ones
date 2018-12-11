import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Manual extends JFrame{
	ImagePanel bg;

	Manual(){
		ImageIcon icon = new ImageIcon("./src/manGB.png"); 
        Image newimg = icon.getImage().getScaledInstance(500, 500,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
	    this.bg = new ImagePanel(newIcon.getImage());

		this.setSize(500,500);
		this.setVisible(true);
		this.add(bg);
	}


static class ImagePanel extends JPanel {

          private Image img;
          public ImagePanel(Image img) {
               this.img = img;
               Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
               setPreferredSize(size);
               setMinimumSize(size);
               setMaximumSize(size);
               setSize(size);
               setLayout(null);
          }
          @Override
          public void paintComponent(Graphics g) {
               g.drawImage(img, 0, 0, null);
          }
}

}