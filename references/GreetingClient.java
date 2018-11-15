/*
 * GreetingClient.java
 * CMSC137 Sample Code for TCP Socket Programming
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;


public class GreetingClient{
    public static void main(String [] args){
        try{
            String serverName = args[0]; //get IP address of server from first param
            int port = Integer.parseInt(args[1]); //get port from second param
            // String message = args[2]; //get message from the third param

            /* Open a ClientSocket and connect to ServerSocket */
            System.out.println("Connecting to " + serverName + " on port " + port);
            
			//creating a new socket for client and binding it to a port
            Socket server = new Socket(serverName, port);
            listenToServer(server);
                
            
            while(true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();

                OutputStream outToServer = server.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF("Client " + server.getLocalSocketAddress()+" says: " +message);
            }

            // System.out.println("Just connected to " + server.getRemoteSocketAddress());
            
			/* Send data to the ServerSocket */

            /* Receive data from the ServerSocket */
            // InputStream inFromServer = server.getInputStream();
            // DataInputStream in = new DataInputStream(inFromServer);
            // System.out.println("Server says " + in.readUTF());
            
			//closing the socket of the client
            // server.close();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Cannot find (or disconnected from) Server");
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GreetingClient <server ip> <port no.> '<your message to the server>'");
        }
    }

    // public void send(String message) {
    //     
    // }   

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

}
