import proto.*;

import java.net.*;
import java.io.*;
import java.util.Scanner;

// import ChatUtils.;

public class Main {
  public static void main(String[] args) {

          String serverName = "202.92.144.45";
          int port = 80, opt = 0, opt1 = 0, opt2 = 0;

          try{
               MainGUI main = new MainGUI();
               main.create();

               Socket server = new Socket(serverName, port);
               String lobby_id = null;

               String name = ChatUtils.stringAsker("Enter Name");
               Player user = new Player(name);
               ChatUtils.listenToServer(server,user);                                                    //listens to all possible packets
               // getOnlinePlayers(server);
               ChatUtils.clear();
               while(true) {

                    opt = ChatUtils.intAsker("Welcome, "+name+"\nChoose:\n\t[0] HOST \n\t[1] CLIENT\n\t[2] Exit\n");
                    ChatUtils.clear();

                    if(opt == 2)break;
                    switch(opt){
                         case 0:                                                               //Automatically send a CREATE_LOBBY PACKET and CONNECT_PACKET
                              CLPacket clpacket = new CLPacket(4);
                              System.out.println("Waiting for server response(clpacket)");
                              ChatUtils.CreateNewLobby(server,clpacket);
                              while(ChatUtils.createLobbyPacketReceived == null)                         // waiting to receive createlobbypacket
                                   System.out.print("\0");  
                              clpacket = ChatUtils.createLobbyPacketReceived;
                              ChatUtils.createLobbyPacketReceived = null;
                              ChatUtils.clear();

                              if(clpacket != null){
                                   lobby_id = clpacket.getLobbyId();

                                   if(!lobby_id.equals("You are not part of any lobby."))      // if successfully created lobby
                                        ChatUtils.chatNow(server,user,lobby_id);                    
                                   else                                                        
                                        System.out.println("Error: "+lobby_id);
                                   
                              }else System.out.println("LobbyId not received properly.");

                         break;
                         case 1: 
                              lobby_id = ChatUtils.stringAsker("Welcome, "+name+"\nPlease enter the lobby_id");
                              ChatUtils.clear();
                              
                              ChatUtils.chatNow(server,user,lobby_id);                              

                         break;
                         default:
                              System.out.println("ERROR: Enter correct values.");
                         break;
                    } 
                    ChatUtils.clear();
               }
               System.out.println("byee");
          }catch(Exception e){
               System.out.println(e);
          }
     }
}