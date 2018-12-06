import proto.*;

import java.awt.*;
import java.net.*;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class ChatGameWindow extends JFrame{

     //
     //  Attributes
     //

     JFrame mainGUI;
     JPanel mainPanel;   // panel that contains the cardPanel and chatPanel
     JPanel cardPanel;   // panel that alter
     JPanel chatPanel;

     // Chat and Game Related Attributes

     //   Chat needed Attributes
     public static final String SERVERNAME = "202.92.144.45";
     public static final int PORT = 80;
     boolean isFinished;
     Lobby lobby;
     Chat chat;
     Game game;
     Socket server;
     Player user;

     //
     //  Constructors
     //

     ChatGameWindow(JFrame mainGUI, String name){          // host ChatGameWindow constructor
          super("Wild Ones");

          this.setFocusable(false);                         // prevents program to listen to the ChatGameWindow

          this.mainGUI = mainGUI;
          this.cardPanel = new JPanel(); // cardpanel is instantiated here since the CardLayout is needed to be passed to lobby
          this.cardPanel.setLayout(new CardLayout());

          this.user = new Player(name);
          this.server = null;
          try{
               this.server = new Socket(SERVERNAME, PORT);
          }catch(Exception e){
               e.printStackTrace();
          };

          this.lobby = new Lobby(this);
          if(this.lobby.connected())
          createWindow();

     }

     ChatGameWindow(JFrame mainGUI, String name, String lID){      // client ChatGameWindow constructor 
          super("Wild Ones");

          this.mainGUI = mainGUI;
          this.cardPanel = new JPanel();
          this.cardPanel.setLayout(new CardLayout());

          this.user = new Player(name);
          this.server = null;
          try{
               this.server = new Socket(SERVERNAME, PORT);
          }catch(Exception e){
               e.printStackTrace();
          };

          this.lobby =  new Lobby(this, lID);
          if(this.lobby.connected())
               createWindow();
     }

     public void createWindow(){

          this.isFinished = true;

          this.game = new Game(this);

          this.chat = new Chat(this.server,  this.user.getName());
          this.chat.setPreferredSize(new Dimension(730,150));

          this.cardPanel.setOpaque(false); 
          this.cardPanel.setPreferredSize(new Dimension(730,550));
          this.cardPanel.add(lobby,"LOBBY");
          this.cardPanel.add(game,"GAME");
          this.cardPanel.setBounds(0,-10,730,550);

          this.chatPanel = new JPanel();
          this.chatPanel.setFocusable(false);
          this.chatPanel.setPreferredSize(new Dimension(730,150));
          this.chatPanel.setOpaque(false);
          this.chatPanel.add(this.chat);
          this.chat.requestFocus();
          this.chatPanel.setBounds(0,540,730,150);

          this.mainPanel = new JPanel();
          this.mainPanel.setFocusable(false);
          this.mainPanel.setLayout(null);
          this.mainPanel.setPreferredSize(new Dimension(730,700));
          this.mainPanel.setOpaque(false);
          this.mainPanel.add(chatPanel);
          this.mainPanel.add(cardPanel);

          this.setSize(730,700);
          this.setResizable(false);
          this.setVisible(true);
          this.add(mainPanel);   
          this.pack();               //add to frame
          this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     }

     //
     //  Methods
     //

     public JPanel getCardPanel(){             
          return this.cardPanel;
     }

     public Player getUser(){
          return this.user;
     }

     public Chat getChat(){
          return this.chat;
     }

     public Socket getServer(){
          return this.server;
     }

     public JFrame getMainGUI(){
          return this.mainGUI;
     }

     public Game getGame(){
          return this.game;
     }

     //
     //  Internal Classes
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
}