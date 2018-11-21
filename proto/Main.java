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

     // private static byte[] received;
     private static boolean toChat;

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

     public static CLPacket CreateNewLobby(Socket server, CLPacket toSend){
          OutputStream outToServer = null;
          InputStream in = null;
          CLPacket toret = null;
          byte[] received = null;
          
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());

               in = server.getInputStream();
               while(in.available() == 0){}                 // waits for a server response
               received = new byte[in.available()];         // creates a byte array the size of the packet received
               in.read(received);                           // reads the inputstream to the byte array

          }catch(SocketTimeoutException s){
               System.out.println("Socket timed out!");
          }catch(IOException e){
               e.printStackTrace();
               System.out.println("Input/Output Error!");
          }catch(Exception e){
               System.out.println(e);
          }

          if(received == null)System.out.println("Error receiving packet");
          else return new CLPacket(received);
          return null;
     }

     public static ConnectPacket ConnectToLobby(Socket server, ConnectPacket toSend){
          OutputStream outToServer = null;
          InputStream in = null;
          TcpPacketProtos.TcpPacket tcppacket = null;
          CLPacket toret = null;
          byte[] received = null;
          
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());

               in = server.getInputStream();
               while(in.available() == 0){}                 // waits for a server response
               received = new byte[in.available()];         // creates a byte array the size of the packet received
               in.read(received);                           // reads the inputstream to the byte array

          }catch(SocketTimeoutException s){
               System.out.println("Socket timed out!");
          }catch(IOException e){
               e.printStackTrace();
               System.out.println("Input/Output Error!");
          }catch(Exception e){
               System.out.println(e);
          }

          if(received == null)System.out.println("Error receiving packet");
          else return new ConnectPacket(received);
          return null;
     }

     public static void sendMessage(Socket server, CHPacket toSend){
          OutputStream outToServer = null;
          try{
               outToServer = server.getOutputStream();
               outToServer.write(toSend.serialize());      
          }catch(Exception e){
               System.out.println(e);
          }
          // System.out.println("PACKET SENT TO SERVER\n");
     }

     public static TcpPacketProtos.TcpPacket.PacketType packetType(int i){
          return TcpPacketProtos.TcpPacket.PacketType.forNumber(i);
     }

     synchronized public static void chatListen(Socket server) {
          Thread thread = new Thread(){
               public void run(){

                    InputStream in = null;
                    byte[] received = null;
                    CHPacket toPrint = null;
 
                    while(toChat == true){
                         // Main.received = null;
                         try{
                              in = server.getInputStream();
                              while(in.available() == 0){}                 // waits for a server response
                              received = new byte[in.available()];         // creates a byte array the size of the packet received
                              in.read(received);                           // reads the inputstream to the byte array
                              toPrint = new CHPacket(received);
                              toPrint.showMessage();

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



     public static void main(String[] args) {

          String serverName = "202.92.144.45";
          int port = 80, opt = 0, opt1 = 0, opt2 = 0;

          try{

               Socket server = new Socket(serverName, port);
               // listenToServer(server);                           //listens to clpackets

               // strings needed for chatting
               String lobby_id = null;
               String message = null;                            // to be sent to the server
               String input = null;                              // explicitly what the user has typed

               String name = stringAsker("Enter Name");
               Player user = new Player(name);
               clear();

               while(true) {
                    opt = intAsker("Welcome, "+name+"\nChoose:\n\t[0] HOST \n\t[1] CLIENT\n");
                    clear();

                    switch(opt){
                         case 0:                                                          //Automatically send a CREATE_LOBBY PACKET and CONNECT_PACKET
                              CLPacket c = new CLPacket(4);
                              // CreateLobby(server, c); 
                              System.out.println("Waiting for server response...");
     
                              CLPacket clpacket = CreateNewLobby(server,c);
                              clear();

                              if(clpacket != null){
                                   lobby_id = clpacket.getLobbyId();

                                   if(!lobby_id.equals("You are not part of any lobby.")){     // if successfully created lobby
                              
                                        ConnectPacket c2 = new ConnectPacket(user,lobby_id);   // packet to be sent to the server
                                        ConnectToLobby(server,c2);

                                        ConnectPacket cpacket = ConnectToLobby(server,c2);
                                        // Main.received = null;
                                        clear();

                                        System.out.println("Success! Connected to lobby "+lobby_id);
                                        System.out.println("You may now chat. Send \"!quit\" to DISCONNECT. ");
                             
                                        CHPacket chatMessage = null;
                                        toChat = true;
                                        chatListen(server);
                                        do{
                                             input = stringAsker(name);
                                             if(input.equals("!quit")){
                                                  toChat = false;
                                                  break;
                                             }

                                             message = name+": "+input;

                                             chatMessage = new CHPacket(user,input);
                                             sendMessage(server, chatMessage);

                                        }while(true);

                                        /***
                                             THIS IS WHERE SENDING THE DISCONNECT PACKET SHOULD BE
                                        ***/
                                        }else{
                                             System.out.println("Error: "+lobby_id);
                                        }
                              }else System.out.println("LobbyId not received properly.");

                         break;
                         case 1: //CLIENT
                              lobby_id = stringAsker("Welcome, "+name+"\nPlease enter the lobby_id");
                              clear();

                              ConnectPacket connectpacket = new ConnectPacket(user,lobby_id);
                              /***
                                   THIS IS WHERE SENDING THE CONNECT PACKET SHOULD BE
                               ***/

                              Thread.sleep(1000);

                              System.out.println("Success! lobby_id "+lobby_id);
                              System.out.println("You may now chat. Send \"!quit\" to DISCONNECT. ");
                           
                              do{

                                   input = stringAsker(name);
                                   message = name+": "+input;

                              }while(!input.equals("!quit"));

                              /***
                                   THIS IS WHERE SENDING THE DISCONNECT PACKET SHOULD BE
                              ***/

                         break;
                         default:
                         break;
                    } clear();
               }
          }catch(Exception e){
               System.out.println(e);
          }
     }
}