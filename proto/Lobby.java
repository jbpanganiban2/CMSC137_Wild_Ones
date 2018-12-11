import proto.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Scanner;


public class Lobby extends JPanel{

     //
     // ATTRIBUTES
     //
     private final static Icon PIG_STANDBY = new ImageIcon("src/pig/pigStandby.gif");
     private final static Icon LUB_STANDBY = new ImageIcon("src/lubglub/lubStandby.gif");
     private final static Icon DYNA_STANDBY = new ImageIcon("src/dyna/dynaStandby.gif");
     private final static Icon PIG_ATTACK = new ImageIcon("src/pig/pigAttack.gif");
     private final static Icon LUB_ATTACK = new ImageIcon("src/lubglub/lubAttack.gif");
     private final static Icon DYNA_ATTACK = new ImageIcon("src/dyna/dynaAttack.gif");

     // gui components

     JButton choice1;
     JButton choice2;
     JButton choice3;

     Image newimg;
     ImageIcon startIcon;
     ImageIcon exitIcon;
     ImageIcon icon;
     ImageIcon newIcon;
     ImagePanel bg;

     ChatGameWindow cgw;
     JPanel top;
     JPanel center;
     JPanel mainPanel;
     static JButton start;
     static JButton exit;
     CardLayout cardLayout;

     GridBagConstraints left;
     GridBagConstraints right;


     // chat needed Components
     Chat chat;
     static Socket server;
     
     UDPServer udpserver;

     UDPClient udpclient;
     
     InetAddress serverAddress;
     Game g;

     Player user;
    


     boolean connected = false;
     boolean isSet = false;

     int selectedChar = 0;

     HashMap nameType;

     boolean enableStart;

     //
     //  CONSTRUCTORS
     //

     Lobby(ChatGameWindow cgw){                                                              //   host constructor
               
          this.enableStart=true;

          this.cgw = cgw;

          this.mainPanel = cgw.getCardPanel();
          this.cardLayout = (CardLayout)this.mainPanel.getLayout();

          this.server = cgw.getServer();
          this.user = cgw.getUser();

          this.nameType = new HashMap();
          
          createLobby();
     }

     Lobby(ChatGameWindow cgw, String lobby_Id){                                             //   client constructor
          this.enableStart=false;
          this.cgw = cgw;
          
          this.mainPanel = cgw.getCardPanel();
          this.cardLayout = (CardLayout)this.mainPanel.getLayout();

          this.server = cgw.getServer();
          this.user = cgw.getUser();


          this.serverAddress = null;

          this.nameType = new HashMap();


          connectToLobby(lobby_Id);
     }

     public void createLobby() {
                                                   
          String lobby_id = null;
          ChatUtils.listenToServer(server, user);

          CLPacket clpacket = new CLPacket(5);
          System.out.println("Waiting for server response(clpacket)");
          ChatUtils.CreateNewLobby(server,clpacket);
          while(ChatUtils.createLobbyPacketReceived == null)System.out.print("\0");                      // waiting to receive packet  
          clpacket = ChatUtils.createLobbyPacketReceived;
          ChatUtils.createLobbyPacketReceived = null;

          this.udpserver = new UDPServer(this);
          this.udpserver.start();
          this.serverAddress = this.udpserver.getInetAddress();

          if(clpacket != null){    // if there is no clpacket received
               lobby_id = clpacket.getLobbyId();

               if(!lobby_id.equals("You are not part of any lobby.")){     

                    ChatUtils.setUdpAddress(this.serverAddress);
                    boolean connected = ChatUtils.chatNowGUI(server,user,lobby_id);
                    if(!connected || this.serverAddress == null){
                         new Prompt("Error Creating to Lobby", 750);
                         return;
                    }

                    this.connected = true;


               }else{
                    System.out.println("Error: "+lobby_id);
               }

          }else System.out.println("LobbyId not received properly.");

          this.initUIComponents();
                                                                 //   initializes all ui components
          while(this.serverAddress == null){System.out.print("\0");}

          this.udpclient = new UDPClient(user.getName(), this, this.serverAddress);
          System.out.println(this.serverAddress);
          this.udpclient.start();
     }

     public void connectToLobby(String lobby_id){

          ChatUtils.listenToServer(server, user);  
                             

          if(!lobby_id.equals("You are not part of any lobby.")){               // if successfully created lobby
               
               boolean connected = ChatUtils.chatNowGUI(server,user,lobby_id);
               
               if(!connected){
                         // create prompt that shows error
                         new Prompt("Error Connecting to Lobby", 750);
                         return;
               }

               this.connected = true;

               // this.serverAddress = null;

          }else System.out.println("Error: "+lobby_id);

          this.initUIComponents();
          
          while(this.isSet == false){System.out.print("\0");}

          this.udpclient = new UDPClient(user.getName(), this, this.serverAddress);
          this.udpclient.start();
     }

     //
     //   Methods
     //

     public HashMap getHashMap(){
          return this.nameType;
     }

     public boolean connected(){
          return this.connected;
     }

     public Chat getChat(){
          return this.chat;
     }

     public Game getActiveGame(){
          return this.g;
     }

     public JPanel getGamePanel(){
          return this.mainPanel;
     }

     public UDPServer getUDPServer(){
          return this.udpserver;
     }

     public UDPClient getUDPClient(){
          return this.udpclient;
     }

     public void setServerAddress(InetAddress i){
          System.out.println("server address is set");
          this.isSet = true;
          this.serverAddress = i;
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

     private Player getUser(Player[] online){
          for(Player p : online){
               if(p == null)continue;
               if(this.user.getName().equals(p.getName()))return p;
          }return user;
     }

 


     private void newGame(){
          Player[] online = ChatUtils.getOnlinePlayers(server);
          
          this.udpclient.sendStart();

          if(online.length <= 1){
               new Prompt("ADD MORE PLAYERS", 1000);
               return;
          }

          Player realUser = getUser(online);

          this.g = new Game(this);
          this.g.addUserPlayer(realUser, selectedChar);
          this.g.init_Players(online, this.nameType);
          
          mainPanel.add(this.g, "GAME");
          cardLayout.next(mainPanel);
          this.g.deploy();    
     }


     public void startHostGame(){
          Player[] online = ChatUtils.getOnlinePlayers(server);

          Player realUser = getUser(online);

          this.g = new Game(this);
          this.g.addUserPlayer(realUser, selectedChar);
          this.g.init_Players(online,this.nameType);

          mainPanel.add(this.g, "GAME");
          cardLayout.next(mainPanel);
          this.g.deploy();
     }


     class startGame implements ActionListener {
          @Override
          public void actionPerformed(ActionEvent event) {
               newGame();
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

               ChatUtils.removeChat(chat);
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

          this.chat = new Chat(this, server, user.getName());
          ChatUtils.addConnectedChat(this.chat);

          // this.udpserver = new UDPServer(this);

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

          this.choice1 = createChoice(PIG_STANDBY,PIG_ATTACK,0);
          this.choice2 = createChoice(LUB_STANDBY,LUB_ATTACK,1);
          this.choice3 = createChoice(DYNA_STANDBY,DYNA_ATTACK,2);

          
          this.top = newTop(this.start, this.exit, this.right, this.left);
          this.center = newCenter(this.choice1,this.choice2,this.choice3);
          this.bg = newBG(this.newIcon, this.top, this.center, this.chat);

          this.setPreferredSize(new Dimension(730,550));
          this.setOpaque(false);
          this.add(bg);
     }

     private JButton createChoice( Icon icn, Icon icn2, int choiceType){
          JButton btn = new JButton(icn);
          btn.setRolloverEnabled(true);
          btn.setRolloverIcon(icn2);
          btn.setContentAreaFilled(false);
          btn.setBorderPainted(false);
          btn.addActionListener((new SetChoice(choiceType, this)));
          btn.setPreferredSize(new Dimension(50,50));
          return btn;
     }

     static class SetChoice implements ActionListener{

          int value = 0;
          Lobby l;

          public SetChoice(int i, Lobby l){
               this.value = i;
               this.l = l;
               // return this;
          }

          @Override
          public void actionPerformed(ActionEvent e){
               l.selectedChar = this.value;

               l.getUDPClient().sendType(l.selectedChar);

          }
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

     private JPanel newCenter(JButton a, JButton b, JButton c){
          JPanel center = new JPanel();
          center.setOpaque(false);
          center.setLayout(null);
          center.setPreferredSize(new Dimension(730,480));

          a.setBounds(136,325,70,50);
          b.setBounds(338,325,70,50);
          c.setBounds(538,325,70,50);

          center.add(a);
          center.add(b);
          center.add(c);


          return center;
     }

     private JPanel newTop(JButton start, JButton exit, GridBagConstraints right, GridBagConstraints left){       // creates a new Top
          JPanel top = new JPanel();
          top.setLayout(new GridBagLayout());
          top.setPreferredSize(new Dimension(730,70));
          top.add(exit,left);


          if(this.enableStart==true){
               top.add(start,right);
          }

          top.setOpaque(false);
          return top;
     }

     private ImagePanel newBG(ImageIcon newIcon, JPanel top, JPanel center, Chat chat){   // creates a new Background
          ImagePanel bg = new ImagePanel(newIcon.getImage());
          bg.setPreferredSize(new Dimension(730,700));
          bg.setLayout(new BorderLayout());

          bg.add(top,BorderLayout.NORTH); 
          bg.add(center, BorderLayout.CENTER);
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