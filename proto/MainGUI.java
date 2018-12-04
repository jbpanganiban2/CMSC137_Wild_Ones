import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class MainGUI {

     static JFrame mainFrame;
     static JTextField name;

     static void create(){                             // sets the Main GUI

          mainFrame = new JFrame("Wild Ones");

                                                          //buttons and textfields
          ImageIcon hostIcon = new ImageIcon("./src/HOST.png");
          ImageIcon clientIcon = new ImageIcon("./src/CLIENT.png"); 

          name = new JTextField();
          name.setPreferredSize(new Dimension(135,50));
          name.setOpaque(false);
          name.requestFocus();

          JButton host = new JButton("HOST");
          host.setPreferredSize(new Dimension(140,50));
          host.setOpaque(false);
          host.setContentAreaFilled(false);
          host.setBorderPainted(false);
          host.setIcon(hostIcon);
          host.addActionListener(new hostChar());
          
          JButton client = new JButton("CLIENT");
          client.setPreferredSize(new Dimension(150,50));
          client.setOpaque(false);
          client.setContentAreaFilled(false);
          client.setBorderPainted(false);
          client.setIcon(clientIcon);
          client.addActionListener(new clientChar());
                                                          //container
          JPanel container = new JPanel();
          container.setLayout(new GridBagLayout());
          container.setPreferredSize(new Dimension(150,170));
          container.setOpaque(false);

          GridBagConstraints center = new GridBagConstraints();
          center.gridx = 0;

          center.insets = new Insets(0,0,10,0);                    //top padding
          center.anchor = GridBagConstraints.PAGE_START;
          container.add(name,center);
          center.anchor = GridBagConstraints.CENTER;
          container.add(host,center);
          center.anchor = GridBagConstraints.PAGE_END;
          
          center.insets = new Insets(0,0,0,0);                     //top padding
          container.add(client,center);

                                                                 // for background
          ImageIcon icon = new ImageIcon("./src/BG.png"); 
          Image newimg = icon.getImage().getScaledInstance(600, 600,  java.awt.Image.SCALE_SMOOTH);
          ImageIcon newIcon = new ImageIcon(newimg);
          ImagePanel thumb = new ImagePanel(newIcon.getImage());
          thumb.setPreferredSize(new Dimension(600,600));
          thumb.setLayout(new GridBagLayout());
          center.insets = new Insets(150,0,0,0);                  //top padding
          thumb.add(container, center);

          mainFrame.setVisible(true);
          mainFrame.setResizable(false);
          mainFrame.setSize(600,600);
          mainFrame.add(thumb);
          mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

     static class hostChar implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent event) {

               if(name.getText().equals(""))return;

               Lobby lobby = new Lobby(name.getText());
               mainFrame.setVisible(false);
          }
     }

     static class clientChar implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent event) {

               // System.out.println("cliecker"+name.getText());

               if(name.getText().equals(""))return;

               JTextField lobby_id_area = new JTextField();
               lobby_id_area.setPreferredSize(new Dimension(300,150));
               lobby_id_area.setOpaque(false);
               lobby_id_area.requestFocus();
               
               JButton connect = new JButton("Connect");

               JFrame getLobbyId = new JFrame();
               getLobbyId.setLayout(new FlowLayout());
               getLobbyId.setResizable(false);
               getLobbyId.setDefaultCloseOperation(0);
               getLobbyId.add(lobby_id_area);
               getLobbyId.add(connect);

               getLobbyId.setSize(300,200);
               getLobbyId.setVisible(true);

               connect.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){

                         String l_id = lobby_id_area.getText();
                         // System.out.println(l_id+" eoasdgasd");

                         if(l_id.equals(""))return;

                         Lobby lobby = new Lobby(name.getText(), l_id);
                         getLobbyId.dispose();
                    }
               });

               mainFrame.setVisible(false);
          }
     }



     public static void main(String[] args) {                    // starts the running of the program
          try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) {
               e.printStackTrace();
          }create();
     }

}