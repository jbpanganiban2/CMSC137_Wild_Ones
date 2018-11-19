import proto.*;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Main{

	public static int intAsker(String args){
		System.out.print(args+"> ");
        return Integer.parseInt(new Scanner(System.in).nextLine());
	}

	public static void sendPacket(/** Socket server, **/byte[] toSend){
            
        System.out.println("PACKET SENT TO SERVER\n");


        // OutputStream outToServer = server.getOutputStream();
        // DataOutputStream out = new DataOutputStream(outToServer);
        // out.writeUTF("Client " + server.getLocalSocketAddress()+" says: " +message);
    }

    public static void listenToServer(Socket server) {
        Thread thread = new Thread(){
            public void run(){
                while(true) {
                    try{
                        InputStream inFromServer = server.getInputStream();
                        DataInputStream in = new DataInputStream(inFromServer);
                        System.out.println("Server says " + in.readUTF());
                    } catch(SocketTimeoutException s){
                        System.out.println("Socket timed out!");
                    } catch(IOException e){
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
			// Socket server = new Socket(serverName, port);
   			// listenToServer(server);
			System.out.println("Connected!");

            while(true) {
                opt = intAsker("CHOOSE:\n[0] HOST \n[1] CLIENT\nenter option");

                switch(opt){
                    case 0: //HOST
                        //Automatically send a CREATE_LOBBY PACKET and CONNECT_PACKET

                    	//creates a CLPacket, and sends it 
	                    CLPacket c = new CLPacket(4);
						c.self();
						byte[] toSend = c.serialize();

                        sendPacket(toSend); 

                        // if successful

                        opt1 = intAsker("[0] CHAT\nenter option");
                        if(opt1==2){ 
                            sendPacket(null);
                            System.out.println("entered chat\n");
                        }

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