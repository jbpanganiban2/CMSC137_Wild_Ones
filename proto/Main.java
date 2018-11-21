/**
Author: Aaron Magnaye,  Antonette Porca, Joshua Panganiban
Subject: CMSC 137 Protobuf Milestone
Description: Main Class that runs the whole chatting system
  -- this will most probably moved to a class that would work like this
    ChatSystem c = new ChatSystem();
    c.start();
    ^-- still unsure about this
**/

import proto.*;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Main{

     private static CLPacket createLobbyPacketReceived = null;
     private static ConnectPacket connectPacketReceived = null;
     private static boolean lobbyfull = false;
     private static boolean chatting = false;
     private static Player[] online = null;
     private static int oldPlayerCount = 0;

     public static int intAsker(String args){
          System.out.print(args+"> ");
          return Integer.parseInt(new Scanner(System.in).nextLine());
     }

     public static String stringAsker(String args){
          System.out.print(args+"> ");
          return (new Scanner(System.in)).nextLine();
     }

     public static void clear() {  
          System.out.print("\033[H\033[2J");  
          System.out.flush();  
     }

     public static void CreateNewLobby(Socket server, CLPacket toSend){
          OutputStream outToServer = null;
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());
          }catch(Exception e){
               System.out.println(e);
          }
     }

     public static void ConnectToLobby(Socket server, ConnectPacket toSend){
          OutputStream outToServer = null;
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());
          }catch(Exception e){
               System.out.println(e);
          }
     }

     public static void sendMessage(Socket server, CHPacket toSend){
          OutputStream outToServer = null;
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());      
          }catch(Exception e){
               System.out.println(e);
          }
     }

     public static void sendMessage(Socket server, String str){
          OutputStream outToServer = null;
          Player p = new Player();
          CHPacket toSend = new CHPacket(p,str);
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());      
          }catch(Exception e){
               System.out.println(e);
          }
     }

     public static void disconnect(Socket server, DCPacket toSend){
          OutputStream outToServer = null;
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());      
          }catch(Exception e){
               System.out.println(e);
          }
     }

     public static void getPlayers(Socket server){
          OutputStream outToServer = null;
          PLPacket toSend = new PLPacket();
          // System.out.println("senditng");
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());   
          }catch(Exception e){
               System.out.println(e);
          }
     }

     synchronized public static void getOnlinePlayers(Socket server){
          Thread thread = new Thread(){
               public void run(){
                    while(true){
                         // try{Thread.sleep(2000);}catch(Exception e){};
                         if(chatting == true){
                              getPlayers(server);
                         }
                         else System.out.println("");
                    }
               }
          };
          thread.start();
     }

     public static TcpPacketProtos.TcpPacket.PacketType packetType(int i){
          return TcpPacketProtos.TcpPacket.PacketType.forNumber(i);
     }

     synchronized public static void listenToServer(Socket server, Player user) {         // listens to all possible packets, then sets the according packet
          Thread thread = new Thread(){
               public void run(){

                    InputStream in = null;
                    TcpPacketProtos.TcpPacket res = null;
                    byte[] received = null;
 
                    while(true){
                         try{
                              in = server.getInputStream();
                              while(in.available() == 0){}                                     // waits for a server response
                              received = new byte[in.available()];                             // creates a byte array the size of the packet received
                              in.read(received);                                               // reads the inputstream to the byte array
                              
                              res = TcpPacketProtos.TcpPacket.parseFrom(received); 
                              
                              switch(res.getType()){                                      
                                   case DISCONNECT:                                            // if packetType is DISCONNECT
                                        System.out.println("disconnect packet received");
                                        DCPacket disconnectpacket = new DCPacket(received);
                                        System.out.println();
                                   break;
                                   case CONNECT:                                               // if packetType is CONNECT
                                        connectPacketReceived = new ConnectPacket(received);
                                        // String temp = "\n!ALERT! Player "+connectPacketReceived.getPlayerName()+" connected";     
                                        // sendMessage(server,temp);                              // sends a message to everyone that someone has connected
                                        // System.out.println(temp);                              // prints a message that someone has connected
                                   break;
                                   case CREATE_LOBBY:                                          // if packetType is CREATE_LOBBY
                                        createLobbyPacketReceived = new CLPacket(received);
                                   break;
                                   case CHAT:                                                  // if packetType is CHAT
                                        CHPacket chatpacket = new CHPacket(received);
                                        chatpacket.showMessage(user);
                                        // getPlayers(server);
                                   break;
                                   case PLAYER_LIST:                                           // if packetType is PLAYER_LIST
                                        // System.out.println("player list packet received");
                                        PLPacket r = new PLPacket(received);
                                        // System.out.println();
                                        int newPcount = r.getPlayerCount();
                                        System.out.println(newPcount + " > " + oldPlayerCount);
                                        if(newPcount > oldPlayerCount)
                                             System.out.println("player has joined boi");
                                             oldPlayerCount = newPcount;
                                   break;
                                   case ERR_LDNE:                                              // if packetType is ERR_LDNE                                        
                                        System.out.println("\nERROR: LOBBY DOES NOT EXIST");
                                   break;
                                   case ERR_LFULL:                                             // if packetType is ERR_LFULL
                                        System.out.println("\nERROR: LOBBY FULL");
                                        lobbyfull = true;
                                   break;
                                   case ERR:                                                   // if packetType is ERR
                                        System.out.println("\nERROR: SHUT YO DUMB ASS");
                                   break;
                              }

                         }catch(SocketTimeoutException s){
                              System.out.println("Socket timed out!");
                         }catch(IOException e){
                              e.printStackTrace();
                              System.out.println("Input/Output Error!");
                         }
                    }
               }
          };
          thread.start();       
     }

     public static void chatNow(Socket server, Player user, String lobby_id){

          ConnectPacket c = new ConnectPacket(user,lobby_id);    // packet to be sent to the server
          ConnectToLobby(server,c);
          System.out.println("Waiting for server response(CPacket)");
          
          while(connectPacketReceived == null){                  // waits for a connectpacket from the server, that confirms that the user is already connected
               System.out.print("\0");
               if(lobbyfull)return;
          }
          chatting = true;
          getOnlinePlayers(server);
          clear();

          System.out.println("Success! Connected to lobby "+lobby_id);
          System.out.println("You may now chat. Send \"!quit\" to DISCONNECT. ");

          String input = null;
          do{
               input = stringAsker(user.getName());
               if(input.equals("!quit"))break;
               sendMessage(server, new CHPacket(user,input));

          }while(true);

          System.out.println("Disconnecting to lobby...");
          chatting = false;
          disconnect(server, new DCPacket(user));
          // clear();
     }



     public static void main(String[] args) {

          String serverName = "202.92.144.45";
          int port = 80, opt = 0, opt1 = 0, opt2 = 0;

          try{

               Socket server = new Socket(serverName, port);
               String lobby_id = null;

               String name = stringAsker("Enter Name");
               Player user = new Player(name);
               listenToServer(server,user);                                                    //listens to all possible packets
               // getOnlinePlayers(server);
               clear();

               while(true) {
                    opt = intAsker("Welcome, "+name+"\nChoose:\n\t[0] HOST \n\t[1] CLIENT\n\t[2] Exit\n");
                    clear();

                    if(opt == 2)break;
                    switch(opt){
                         case 0:                                                               //Automatically send a CREATE_LOBBY PACKET and CONNECT_PACKET
                              CLPacket clpacket = new CLPacket(4);

                              System.out.println("Waiting for server response(clpacket)");
                              CreateNewLobby(server,clpacket);
                              while(createLobbyPacketReceived == null)                         // waiting to receive createlobbypacket
                                   System.out.print("\0");  
                              clpacket = createLobbyPacketReceived;
                              createLobbyPacketReceived = null;
                              clear();

                              if(clpacket != null){
                                   lobby_id = clpacket.getLobbyId();

                                   if(!lobby_id.equals("You are not part of any lobby."))      // if successfully created lobby
                                        chatNow(server,user,lobby_id);                    
                                   else                                                        
                                        System.out.println("Error: "+lobby_id);
                                   
                              }else System.out.println("LobbyId not received properly.");

                         break;
                         case 1: 
                              lobby_id = stringAsker("Welcome, "+name+"\nPlease enter the lobby_id");
                              clear();
                              
                              chatNow(server,user,lobby_id);                              

                         break;
                         default:
                              System.out.println("ERROR: Enter correct values.");
                         break;
                    } 
                    clear();
               }
               System.out.println("byee");
          }catch(Exception e){
               System.out.println(e);
          }
     }
}