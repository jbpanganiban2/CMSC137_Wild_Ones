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
          System.out.println("sent");
     }

     public static void ConnectToLobby(Socket server, ConnectPacket toSend){
          OutputStream outToServer = null;
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());
          }catch(Exception e){
               System.out.println(e);
          }
          System.out.println("sent");
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
                                   break;
                                   case CONNECT:                                               // if packetType is CONNECT
                                        // System.out.println("connect packet received");
                                        connectPacketReceived = new ConnectPacket(received);
                                        // System.out.println(connectPacketReceived.getPacket().getUpdate());

                                        if(connectPacketReceived.getPacket().getUpdate() == TcpPacketProtos.TcpPacket.ConnectPacket.Update.forNumber(1))System.out.println("\n"+connectPacketReceived.getPlayerName()+" connected to the Lobby.");

                                   break;
                                   case CREATE_LOBBY:                                          // if packetType is CREATE_LOBBY
                                        // System.out.println("create lobby packet received");
                                        createLobbyPacketReceived = new CLPacket(received);
                                   break;
                                   case CHAT:                                                  // if packetType is CHAT
                                        // System.out.println("chat packet received");
                                        CHPacket chatpacket = new CHPacket(received);
                                        chatpacket.showMessage(user);
                                   break;
                                   case PLAYER_LIST:                                           // if packetType is PLAYER_LIST
                                        System.out.println("player list packet received");
                                   break;
                                   case ERR_LDNE:                                              // if packetType is ERR_LDNE                                        System.out.println("connect packet received");
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
          ConnectPacket c = new ConnectPacket(user,lobby_id);   // packet to be sent to the server
          ConnectToLobby(server,c);
          System.out.println("Waiting for server response...");
          
          while(connectPacketReceived == null){
               System.out.print("\0");
               if(lobbyfull)return;
          }
          c = connectPacketReceived;
          connectPacketReceived = null;
          clear();

          System.out.println("Success! Connected to lobby "+lobby_id);
          System.out.println("You may now chat. Send \"!quit\" to DISCONNECT. ");

          CHPacket chatMessage = null;
          String input = null;
          do{
               input = stringAsker(user.getName());
               if(input.equals("!quit"))break;

               chatMessage = new CHPacket(user,input);
               sendMessage(server, chatMessage);

          }while(true);

          System.out.println("Disconnecting to lobby...");

          /***
               THIS IS WHERE SENDING THE DISCONNECT PACKET SHOULD BE
          ***/
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
               clear();

               while(true) {
                    opt = intAsker("Welcome, "+name+"\nChoose:\n\t[0] HOST \n\t[1] CLIENT\n");
                    clear();

                    switch(opt){
                         case 0:                                                               //Automatically send a CREATE_LOBBY PACKET and CONNECT_PACKET
                              CLPacket clpacket = new CLPacket(4);
                              // CreateLobby(server, c); 
                              System.out.println("Waiting for server response... clpacket");
     
                              CreateNewLobby(server,clpacket);
                              while(createLobbyPacketReceived == null)System.out.print("\0");
                              clpacket = createLobbyPacketReceived;
                              createLobbyPacketReceived = null;
                              clear();

                              if(clpacket != null){
                                   lobby_id = clpacket.getLobbyId();

                                   if(!lobby_id.equals("You are not part of any lobby.")) chatNow(server,user,lobby_id);     // if successfully created lobby
                                   else System.out.println("Error: "+lobby_id);
                                   
                              }else System.out.println("LobbyId not received properly.");

                         break;
                         case 1: //CLIENT
                              lobby_id = stringAsker("Welcome, "+name+"\nPlease enter the lobby_id");
                              clear();
                              
                              chatNow(server,user,lobby_id);                              

                         break;
                         default:
                         break;
                    } 
                    // clear();
               }
          }catch(Exception e){
               System.out.println(e);
          }
     }
}