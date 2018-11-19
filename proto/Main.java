/**
Author: Aaron Magnaye,	Antonette Porca, Joshua Panganiban
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

	public static int intAsker(String args){
		System.out.print(args+"> ");
        return Integer.parseInt(new Scanner(System.in).nextLine());
	}

	public static String stringAsker(String args){
		System.out.println(args+"> ");
		return (new Scanner(System.in)).nextLine();
	}

	public static void sendPacket(byte[] toSend){
		System.out.println("wow");
	}

	public static void sendPacket(Socket server, byte[] toSend){
            
		OutputStream outToServer = null;
		DataOutputStream dos = null;

		// String s = toSend.toString();

		try{

	        outToServer = server.getOutputStream();
	        dos = new DataOutputStream(outToServer);
	        dos.writeInt(1024);
			dos.write(toSend);
			
		}catch(Exception e){
			System.out.println(e);
		}
	    System.out.println("PACKET SENT TO SERVER\n");


    }

    public static void listenToServer(Socket server) {
        Thread thread = new Thread(){

            public void run(){
            	BufferedReader br = null;
            	String ss = null;


                try{

                    br = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    ss = br.readLine();
                    // if(ss != null)
					System.out.println(ss);

                } catch(SocketTimeoutException s){
                    System.out.println("Socket timed out!");
                } catch(IOException e){
                    e.printStackTrace();
                    System.out.println("Input/Output Error!");
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
   			listenToServer(server);
			System.out.println("Connected!");

            while(true) {
                opt = intAsker("CHOOSE:\n[0] HOST \n[1] CLIENT\nenter option");

                switch(opt){
                    case 0: //HOST
                        //Automatically send a CREATE_LOBBY PACKET and CONNECT_PACKET

                    	//creates a CLPacket, and sends it 
	                    CLPacket c = new CLPacket(4);
						// c.self();
						byte[] toSend = c.serialize();

                        sendPacket(server, toSend); 

                        // if successful
                        System.out.println("Success: lobby_id - %% | You may now chat. Send \"logout\" to be stop chatting. ");

                        // opt1 = intAsker("[0] CHAT\nenter option");
                        // if(opt1==2){ 
                        //     sendPacket(null);
                        //     System.out.println("entered chat\n");
                        // }
                        String message = stringAsker("name");
                        do{
                        	message = stringAsker("name");
                        }while(!message.equals("logout"));

                        // if not successful	
                        	// enter code here

                        break;
                    case 1: //CLIENT
                        opt1 = intAsker("CHOOSE:\n[0] CONNECT\nenter option");

                        // client sends packet to host, given the lobby_id

                        if(opt1==0){
                            sendPacket(null);
                            System.out.println("CONNECTED TO A LOBBY\n");

                            opt2 = intAsker("CHOOSE:\n[0] DISCONNECT\n[1] CHAT\nenter option");

                            if(opt2==0){
                                sendPacket(null);
                                System.out.println("DISCONNECTED\n");
                            }

                            if(opt2==1){
                                sendPacket(null);
                                System.out.println("entered chat\n");
                            }
                        }

                        break;
                    default:
                        break;
                }

           }

		}catch(Exception e){
			System.out.println(e);
		}

	}
}