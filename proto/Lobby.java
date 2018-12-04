import proto.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;


public class Lobby{

     //
     // ATTRIBUTES
     //

     // gui components
     ImageIcon startIcon;
     ImageIcon icon;
     ImageIcon newIcon;
     Image newimg;
     JFrame lobbyFrame;
     JButton start;
     JPanel top;
     GridBagConstraints right;
     ImagePanel bg;

     Chat chat;

     //
     //  CONSTRUCTORS
     //

     Lobby(String username){
          createLobby(username);
     }

     Lobby(String username, String lobby_Id){
          connectToLobby(username, lobby_Id);
     }

     public void createLobby(String username) {

          try{                                                                            // initializes the ChatLobby
               String serverName = "202.92.144.45";
               int port = 80;
               Socket server = new Socket(serverName, port);

               this.initUIComponents(server, username);                                   // initializes all ui components
                                                   
               String lobby_id = null;
               Player user = new Player(username);
               ChatUtils.listenToServer(server, user);

               CLPacket clpacket = new CLPacket(4);
               System.out.println("Waiting for server response(clpacket)");
               ChatUtils.CreateNewLobby(server,clpacket);
               while(ChatUtils.createLobbyPacketReceived == null)                         // waiting to receive createlobbypacket
               System.out.print("\0");  
               clpacket = ChatUtils.createLobbyPacketReceived;
               ChatUtils.createLobbyPacketReceived = null;

               if(clpacket != null){
                    lobby_id = clpacket.getLobbyId();

                    if(!lobby_id.equals("You are not part of any lobby.")){               // if successfully created lobby
                         ChatUtils.setChat(this.chat);
                         ChatUtils.chatNowGUI(server,user,lobby_id);

                         // while(ChatUtils.chatting == true){System.out.print("\0");}

                         // invokeDisconnect(server, user);

                         }else System.out.println("Error: "+lobby_id);

               }else System.out.println("LobbyId not received properly.");


          }catch(Exception e){
               e.printStackTrace();
          }

     }

     public void connectToLobby(String username, String lobby_id){
          try{
               String serverName = "202.92.144.45";
               int port = 80;
               Socket server = new Socket(serverName, port); 
                                                                                          // initializes the ChatLobby
               this.initUIComponents(server, username);
               Player user = new Player(username);
               ChatUtils.listenToServer(server, user);                                   // initializes all ui components

               if(!lobby_id.equals("You are not part of any lobby.")){               // if successfully created lobby
                    ChatUtils.setChat(this.chat);
                    ChatUtils.chatNowGUI(server,user,lobby_id);

                    // while(ChatUtils.chatting == true){System.out.print("\0");}

                    // invokeDisconnect(server, user);

               }else System.out.println("Error: "+lobby_id);

          }catch(Exception e){
               e.printStackTrace();
          }
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

     //
     //  METHODS FOR GUI COMPONENTS INITIALIZATION
     //
     
     private void initUIComponents(Socket server, String username){        // Inintializes all UI components
          this.chat = new Chat(server, username);
          this.chat.setOpaque(false);
          this.startIcon = new ImageIcon("./src/START.png");                    
          this.icon = new ImageIcon("./src/LobbyBG.png"); 
          this.newimg = this.icon.getImage().getScaledInstance(730, 700,  java.awt.Image.SCALE_SMOOTH);
          this.newIcon = new ImageIcon(this.newimg);
          this.start = createNewStart(this.startIcon); 
          this.right = new GridBagConstraints();
          this.right.insets = new Insets(30,520,0,0);
          this.top = newTop(this.start, this.right);
          this.bg = newBG(this.newIcon, this.top, this.chat);
          this.lobbyFrame = newLobbyFrame(this.bg);
     }
     


     private JButton createNewStart(ImageIcon startIcon){                  // creates a new Start button
          JButton toReturn = new JButton();
          toReturn.setOpaque(false);
          toReturn.setContentAreaFilled(false);
          toReturn.setPreferredSize(new Dimension(160,70));
          toReturn.setBorderPainted(false);
          toReturn.setIcon(startIcon);
          return toReturn;
     }

     private JPanel newTop(JButton start, GridBagConstraints right){       // creates a new Top
          JPanel top = new JPanel();
          top.setLayout(new GridBagLayout());
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