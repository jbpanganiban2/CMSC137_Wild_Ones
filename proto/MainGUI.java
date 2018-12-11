import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//
//   MainGUI
//        the class that produces the main "login" window as well as has the main method
//

//   List of things to do
//        merge Character to Player Class
//             fix startbutton bug
//             find way to make sure only user is enabled
//             
//             merge login player to one player in game player list
//        create udp server
//        

public class MainGUI{

     private static JFrame mainFrame;
     private static JTextField name;
     private static JButton host;
     private static JButton client;
     private static JButton manual;
     private static JPanel container;


     MainGUI(){
          mainFrame = new JFrame("Wild Ones");
          create();
     }

     void create(){                                                   // sets the Main GUI

          name = newNameTextField();
          host = newHostButton();
          client = newClientButton();
          manual = newManualButton();
          container = newGridContainer();

          name.setBounds(0,0,135,50);
          host.setBounds(0,60,150,50);
          client.setBounds(0,120,150,50);
          manual.setBounds(0,180,150,50);

          container.add(name);
          container.add(host);
          
          container.add(client);
          container.add(manual);

          GridBagConstraints center = new GridBagConstraints();
                                                                      // for background
          ImageIcon icon = new ImageIcon("./src/BG.png"); 
          Image newimg = icon.getImage().getScaledInstance(600, 600,  java.awt.Image.SCALE_SMOOTH);
          ImageIcon newIcon = new ImageIcon(newimg);
          ImagePanel thumb = new ImagePanel(newIcon.getImage());
          thumb.setPreferredSize(new Dimension(600,600));
          thumb.setLayout(new GridBagLayout());
          center.weighty=4.0;
          center.insets = new Insets(200,0,0,0);                      //top padding
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

     static class hostCharMouse extends MouseAdapter{
          @Override
          public void mouseEntered(MouseEvent e){
               ImageIcon hostIcon = new ImageIcon("./src/HOSTHOVER.png");
               host.setIcon(hostIcon);
          }
          @Override
          public void mouseExited(MouseEvent e){
               ImageIcon hostIcon = new ImageIcon("./src/HOST.png");
               host.setIcon(hostIcon);
          }
     }

     static class hostChar implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent event) {

               if(name.getText().equals(""))return;

               ChatGameWindow window = new ChatGameWindow(mainFrame, name.getText());
               mainFrame.setVisible(false);
          }

     }


     static class clientCharMouse extends MouseAdapter{
          @Override
          public void mouseEntered(MouseEvent e){
               ImageIcon clientIcon = new ImageIcon("./src/CLIENTHOVER.png");
               client.setIcon(clientIcon);
          }
          @Override
          public void mouseExited(MouseEvent e){
               ImageIcon clientIcon = new ImageIcon("./src/CLIENT.png");
               client.setIcon(clientIcon);
          }
     }


     static class clientChar implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent event) {


               if(name.getText().equals(""))return;

               JTextField lobby_id_area = newLobbyAreaTextField();
               JButton connect = new JButton("Connect");
               JFrame getLobbyId = newGetLobbyIdFrame(lobby_id_area, connect);

               connect.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){

                         String l_id = lobby_id_area.getText();

                         if(l_id.equals(""))return;

                         ChatGameWindow window = new ChatGameWindow(mainFrame, name.getText(), l_id);
                         getLobbyId.dispose();
                         // mainFrame.setVisible(false);
                         
                    }
               });
          }
     }
     static class manualChar implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent event) {

               // if(name.getText().equals(""))return;

               // ChatGameWindow window = new ChatGameWindow(mainFrame, name.getText());
               // mainFrame.setVisible(false);
          }

     }

     static class manualCharMouse extends MouseAdapter{
          @Override
          public void mouseEntered(MouseEvent e){
               ImageIcon manualIcon = new ImageIcon("./src/MANUALHOVER.png");
               manual.setIcon(manualIcon);
          }
          @Override
          public void mouseExited(MouseEvent e){
               ImageIcon manualIcon = new ImageIcon("./src/MANUAL.png");
               manual.setIcon(manualIcon);
          }
     }

     //
     //   UI Inintialization Methods
     //

     private JTextField newNameTextField(){
          JTextField name = new JTextField();
          name.setPreferredSize(new Dimension(135,50));
          name.setOpaque(false);
          name.requestFocus();
          return name;
     }

     private JButton newHostButton(){
          ImageIcon hostIcon = new ImageIcon("./src/HOST.png");
          JButton host = new JButton("HOST");
          host.setPreferredSize(new Dimension(150,50));
          host.setOpaque(false);
          host.setContentAreaFilled(false);
          host.setBorderPainted(false);
          host.setIcon(hostIcon);
          host.addMouseListener(new hostCharMouse());
          host.addActionListener(new hostChar());
          return host;
     }

     private JButton newClientButton(){
          ImageIcon clientIcon = new ImageIcon("./src/CLIENT.png"); 
          JButton client = new JButton("CLIENT");
          client.setPreferredSize(new Dimension(150,50));
          client.setOpaque(false);
          client.setContentAreaFilled(false);
          client.setBorderPainted(false);
          client.setIcon(clientIcon);
          client.addActionListener(new clientChar());
          client.addMouseListener(new clientCharMouse());
          return client;
     }

     private JButton newManualButton(){
          ImageIcon clientIcon = new ImageIcon("./src/MANUAL.png"); 
          JButton client = new JButton("MANUAL");
          client.setPreferredSize(new Dimension(150,50));
          client.setOpaque(false);
          client.setContentAreaFilled(false);
          client.setBorderPainted(false);
          client.setIcon(clientIcon);
          client.addActionListener(new manualChar());
          client.addMouseListener(new manualCharMouse());
          return client;
     }

     private JPanel newGridContainer(){
          JPanel container = new JPanel();
          // container.setLayout(null);
          container.setPreferredSize(new Dimension(150,230));
          container.setOpaque(false);
          return container;
     }

     private static JFrame newGetLobbyIdFrame(JTextField lobby_id_area, JButton connect){
          JFrame getLobbyId = new JFrame("Enter Lobby ID");
          getLobbyId.setLayout(new FlowLayout());
          getLobbyId.setResizable(false);
          getLobbyId.setDefaultCloseOperation(0);
          getLobbyId.add(lobby_id_area);
          getLobbyId.add(connect);
          getLobbyId.setSize(300,200);
          getLobbyId.setVisible(true);
          return getLobbyId;
     }

     private static JTextField newLobbyAreaTextField(){
          JTextField lobby_id_area = new JTextField();
          lobby_id_area.setPreferredSize(new Dimension(300,150));
          lobby_id_area.setOpaque(false);
          lobby_id_area.requestFocus();
          return lobby_id_area;
     }

     //
     //   Main Method
     //

     public static void main(String[] args) {                    // starts the running of the program
          try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) {
               e.printStackTrace();
          }

          MainGUI mg = new MainGUI();
     }

}