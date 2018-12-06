import proto.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;


public class Lobby extends JPanel{

     //
     // ATTRIBUTES
     //

     // gui components
     Image newimg;
     ImageIcon startIcon;
     ImageIcon exitIcon;
     ImageIcon icon;
     ImageIcon newIcon;
     ImagePanel bg;

     ChatGameWindow cgw;
     JPanel top;
     JPanel mainPanel;
     static JButton start;
     static JButton exit;
     CardLayout cardLayout;

     GridBagConstraints left;
     GridBagConstraints right;

     // chat needed Components
     Chat chat;
     Socket server;
     Player user;
     boolean connected = false;



     //
     //  CONSTRUCTORS
     //

     Lobby(ChatGameWindow cgw){                                                              //   host constructor
          
          this.cgw = cgw;

          this.mainPanel = cgw.getCardPanel();
          this.cardLayout = (CardLayout)this.mainPanel.getLayout();

          this.server = cgw.getServer();
          this.user = cgw.getUser();
          
          createLobby();
     }

     Lobby(ChatGameWindow cgw, String lobby_Id){                                             //   client constructor

          this.cgw = cgw;
          
          this.mainPanel = cgw.getCardPanel();
          this.cardLayout = (CardLayout)this.mainPanel.getLayout();

          this.server = cgw.getServer();
          this.user = cgw.getUser();

          connectToLobby(lobby_Id);
     }

     public void createLobby() {
                                                   
          String lobby_id = null;
          ChatUtils.listenToServer(server, user);

          CLPacket clpacket = new CLPacket(4);
          System.out.println("Waiting for server response(clpacket)");
          ChatUtils.CreateNewLobby(server,clpacket);
          while(ChatUtils.createLobbyPacketReceived == null)System.out.print("\0");                      // waiting to receive packet  
          clpacket = ChatUtils.createLobbyPacketReceived;
          ChatUtils.createLobbyPacketReceived = null;

          
          if(clpacket != null){    // if there is no clpacket received
               lobby_id = clpacket.getLobbyId();

               if(!lobby_id.equals("You are not part of any lobby.")){     

                    ChatUtils.setChat(this.chat);

                    boolean connected = ChatUtils.chatNowGUI(server,user,lobby_id);
                    if(!connected){
                         // create prompt that shows error
                         return;
                    }this.connected = true;

               }else{
                    System.out.println("Error: "+lobby_id);
               }

          }else System.out.println("LobbyId not received properly.");

          this.initUIComponents();                                                       //   initializes all ui components


     }

     public void connectToLobby(String lobby_id){

          ChatUtils.listenToServer(server, user);                               // initializes all ui components

          if(!lobby_id.equals("You are not part of any lobby.")){               // if successfully created lobby
               
               ChatUtils.setChat(this.chat);
               
               boolean connected = ChatUtils.chatNowGUI(server,user,lobby_id);
               
               System.out.println(connected);
               if(!connected){
                         // create prompt that shows error
                         return;
               }this.connected = true;

          }else System.out.println("Error: "+lobby_id);

          this.initUIComponents();
     }

     //
     //   Methods
     //

     public boolean connected(){
          return this.connected;
     }


     //
     //  INTERNAL CLASSES
     //
     
     class ImagePanel extends JPanel {

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

     class startGame implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent event) {
               cardLayout.next(mainPanel);
               cgw.getGame().deploy();
          }
     }

     static class startMouse extends MouseAdapter{
          @Override
          public void mouseEntered(MouseEvent e){
               ImageIcon startIcon = new ImageIcon("./src/STARTHOVER.png");
               start.setIcon(startIcon);
          }
          @Override
          public void mouseExited(MouseEvent e){
               ImageIcon startIcon = new ImageIcon("./src/START.png");
               start.setIcon(startIcon);
          }
     }

     class backToMainGUI implements ActionListener{
          @Override
          public void actionPerformed(ActionEvent e){

               ChatUtils.invokeDisconnect(server, user);
               cgw.setVisible(false);
               cgw.getMainGUI().setVisible(true);
          }
     }

     static class exitMouse extends MouseAdapter{
          @Override
          public void mouseEntered(MouseEvent e){
               ImageIcon exitIcon = new ImageIcon("./src/EXITHOVER.png");
               exit.setIcon(exitIcon);
          }
          @Override
          public void mouseExited(MouseEvent e){
               ImageIcon exitIcon = new ImageIcon("./src/EXIT.png");
               exit.setIcon(exitIcon);
          }
     }

     //
     //  METHODS FOR GUI COMPONENTS INITIALIZATION
     //
     
     private void initUIComponents(){        // Inintializes all UI components
          this.chat = new Chat(server, user.getName());
          this.chat.setOpaque(false);

          this.startIcon = new ImageIcon("./src/START.png");  
          this.exitIcon = new ImageIcon("./src/EXIT.png");                                      
          this.icon = new ImageIcon("./src/LobbyBG.png"); 
          this.newimg = this.icon.getImage().getScaledInstance(730, 700,  java.awt.Image.SCALE_SMOOTH);
          this.newIcon = new ImageIcon(this.newimg);

          this.start = createNewButton(this.startIcon);
          this.start.addActionListener(new startGame()); 
          this.start.addMouseListener(new startMouse());

          this.exit = createNewButton(this.exitIcon);
          this.exit.addActionListener(new backToMainGUI());
          this.exit.addMouseListener(new exitMouse());
          
          this.left = new GridBagConstraints(); 
          this.left.insets = new Insets(0,0,0,370);
          this.left.anchor = GridBagConstraints.LINE_START;
          
          this.right = new GridBagConstraints();
          this.right.anchor = GridBagConstraints.LINE_END;
          
          this.top = newTop(this.start, this.exit, this.right, this.left);
          this.bg = newBG(this.newIcon, this.top, this.chat);

          this.setPreferredSize(new Dimension(730,550));
          this.setOpaque(false);
          this.add(bg);
     }
     


     private JButton createNewButton(ImageIcon icn){                  // creates a new Start button
          JButton toReturn = new JButton();
          toReturn.setOpaque(false);
          toReturn.setContentAreaFilled(false);
          toReturn.setPreferredSize(new Dimension(160,50));
          toReturn.setBorderPainted(false);
          toReturn.setIcon(icn);
          return toReturn;
     }

     private JPanel newTop(JButton start, JButton exit, GridBagConstraints right, GridBagConstraints left){       // creates a new Top
          JPanel top = new JPanel();
          top.setLayout(new GridBagLayout());
          top.setPreferredSize(new Dimension(730,70));
          top.add(exit,left);
          top.add(start,right);
          top.setOpaque(false);
          return top;
     }

     private ImagePanel newBG(ImageIcon newIcon, JPanel top, Chat chat){   // creates a new Background
          ImagePanel bg = new ImagePanel(newIcon.getImage());
          bg.setPreferredSize(new Dimension(730,700));
          bg.setLayout(new BorderLayout());

          bg.add(top,BorderLayout.NORTH); 
          bg.add(this.chat,BorderLayout.SOUTH); 
          return bg;
     }

     private JFrame newLobbyFrame(ImagePanel bg){                          // creates a new LobbyFrame
          JFrame lobbyFrame = new JFrame();
          lobbyFrame.add(bg);
          lobbyFrame.setResizable(false);
          lobbyFrame.setSize(730, 700);
          lobbyFrame.setVisible(true);
          lobbyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          return lobbyFrame;
     }
}