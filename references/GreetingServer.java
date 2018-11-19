/*
 * GreetingServer.java
 * CMSC137 Example for TCP Socket Programming
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class GreetingServer extends Thread{
    private ServerSocket serverSocket;
    ArrayList<Socket> client_array = new ArrayList<Socket>(); 

    public GreetingServer(int port) throws IOException{
        //binding a socket to a port
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(60000);
    }

    public void run() {
        while(true){
            try{
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket client = serverSocket.accept();
                client_array.add(client);
                System.out.println("Just connected to " + client.getRemoteSocketAddress());
                listenToClient(client);

            }catch(SocketTimeoutException s){
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Input/Output Error!");
                break;
            }
        }
    }

    public void listenToClient(Socket client) {
        Thread thread = new Thread(){
            public void run(){
                while(true) {
                    try{
                        DataInputStream in = new DataInputStream(client.getInputStream());
                        String msg = in.readUTF();
                        broadcast(msg);
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

    public void broadcast(String message){
        /* Send data to Client*/
        int numOfClients = client_array.size();
        for(int i=0; i < numOfClients ; i++){
            Socket currClient = client_array.get(i);
            try{
                DataOutputStream out = new DataOutputStream(currClient.getOutputStream());
                out.writeUTF(message);
            } catch(SocketTimeoutException s){
                System.out.println("Socket timed out!");
            } catch(IOException e){
                e.printStackTrace();
                System.out.println("Input/Output Error!");
            }
            
        }  
    }

    public static void main(String [] args){
        try{
            int port = Integer.parseInt(args[0]);
            Thread t = new GreetingServer(port);
            t.start();
        }catch(IOException e){
            //e.printStackTrace();
            System.out.println("Usage: java GreetingServer <port no.>\n"+
                    "Make sure to use valid ports (greater than 1023)");
        }catch(ArrayIndexOutOfBoundsException e){
            //e.printStackTrace();
            System.out.println("Usage: java GreetingServer <port no.>\n"+
                    "Insufficient arguments given.");
        }
    }
}
