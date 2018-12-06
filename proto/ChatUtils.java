/**
Author: Aaron Magnaye,  Antonette Porca, Joshua Panganiban
Subject: CMSC 137
Description: Class that runs the whole chatting system
**/

import proto.*;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatUtils{

     public static CLPacket createLobbyPacketReceived = null;
     public static ConnectPacket connectPacketReceived = null;
     public static boolean lobbyfull = false;
     public static boolean ldne = false;
     public static boolean chatting = false;
     public static Player[] online = null;
     public static int oldPlayerCount = 0;
     public static Chat chat;

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

     public static void setChat(Chat toSet){
          ChatUtils.chat = toSet;
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
                                        String temp = "\n!ALERT! Player "+connectPacketReceived.getPlayerName()+" connected";     
                                        sendMessage(server,temp);                              // sends a message to everyone that someone has connected
                                        // System.out.println(temp);                              // prints a message that someone has connected
                                   break;
                                   case CREATE_LOBBY:                                          // if packetType is CREATE_LOBBY
                                        createLobbyPacketReceived = new CLPacket(received);
                                   break;
                                   case CHAT:                                                  // if packetType is CHAT
                                        CHPacket chatpacket = new CHPacket(received);
                                        // chatpacket.showMessage(user);
                                        chatpacket.addMessageToBox(chatpacket.getMessage(), chat);
                                        // getPlayers(server);
                                   break;
                                   case PLAYER_LIST:                                           // if packetType is PLAYER_LIST
                                        System.out.println("player list packet received");
                                        // PLPacket r = new PLPacket(received);
                                        // System.out.println();
                                        // int newPcount = r.getPlayerCount();
                                        // System.out.println(newPcount + " > " + oldPlayerCount);
                                        // if(newPcount > oldPlayerCount)
                                        //      System.out.println("player has joined boi");
                                        //      oldPlayerCount = newPcount;
                                   break;
                                   case ERR_LDNE:                                              // if packetType is ERR_LDNE                                        
                                        System.out.println("\nERROR: LOBBY DOES NOT EXIST");
                                        ldne = true;
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

          ConnectPacket c = new ConnectPacket(user,lobby_id);                                  // packet to be sent to the server
          ConnectToLobby(server,c);
          System.out.println("Waiting for server response(CPacket)");
          
          while(connectPacketReceived == null){                                                // waits for a connectpacket from the server, that confirms that the user is already connected
               System.out.print("\0");
               if(lobbyfull)return;
          }
          chatting = true;
          // getOnlinePlayers(server);
          clear();

          System.out.println("Success! Connected to lobby "+lobby_id);
          System.out.println("You may now chat. Send \"!quit\" to DISCONNECT. ");

          String input = null;
          do{
               input = stringAsker(user.getName());
               if(input.equals("!quit"))break;
               sendMessage(server, new CHPacket(user,input));

          }while(true);

          invokeDisconnect(server, user);
          // clear();
     }

     public static boolean chatNowGUI(Socket server, Player user, String lobby_id){               // function that ensures the user is connected to the chat server
          // returns false if not connected

          ConnectPacket c = new ConnectPacket(user,lobby_id);                                  // packet to be sent to the server
          ConnectToLobby(server,c);
          System.out.println("Waiting for server response(CPacket)");
          
          while(connectPacketReceived == null){                                                // waits for a connectpacket from the server, that confirms that the user is already connected
               System.out.print("\0");
               if(lobbyfull){
                    lobbyfull = false;
                    return false;
               }if(ldne){     // lobby does not exist
                    ldne = false;
                    return false;
               }
          }
          chatting = true;

          System.out.println("Success! Connected to lobby "+lobby_id);
          System.out.println("You may now chat.");

          return true;
     }

     public static void invokeDisconnect(Socket server, Player user){                          // function that disconnects the user from the chat server
          System.out.println("Disconnecting to lobby...");
          
          chatting = false;
          resetPackets();
          disconnect(server, new DCPacket(user));

     }

     private static void resetPackets(){
          createLobbyPacketReceived = null;
          connectPacketReceived = null;
          lobbyfull = false;
          ldne = false;
          chatting = false;
          online = null;
          oldPlayerCount = 0;
     }
}