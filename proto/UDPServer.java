import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer implements Runnable{

   //
   // Attributes
   //

   private DatagramSocket serverSocket;
   private byte[] receiveData;
   private Client[] clients;
   private int clientNum = 0;
   private boolean gameRunning;
   private InetAddress serverAddress;
   private Lobby l;

   //
   // Constructors
   //
   
   public UDPServer(){

      try{
         this.serverSocket = new DatagramSocket(1975);
         this.receiveData = new byte[1024];
         this.clients = new Client[4];
         this.clientNum = 0;
         this.gameRunning = true;
      }catch(Exception e){}

   }

   public UDPServer(Lobby l){

      try(final DatagramSocket socket = new DatagramSocket()){                                       // gets the UDP server address
         socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
         String ss = socket.getLocalAddress().getHostAddress();
         // System.out.println(ss + " new serverAddress");
         this.serverAddress = InetAddress.getByName(ss);
      }catch(Exception e){}

      try{
         this.serverSocket = new DatagramSocket(1975);
         this.receiveData = new byte[128];
         this.clients = new Client[5];
         this.clientNum = 0;
         this.gameRunning = true;
         this.l = l;
      }catch(Exception e){}

   }

   //
   // Methods
   //

   public void stopServer(){
      this.gameRunning = false;
   }

   public static void sendToEveryone(String sender, String toSend, Client[] everyone, DatagramSocket serverSocket){
      (new Thread(){
         @Override
         public void run(){
            byte[] sendData = toSend.getBytes();
            // int length = 0;
            try{
               for(Client i : everyone){
                  if(i != null && !i.isEqual(sender)){ // sends only to receivers that are not the sender
                     // length++;
                     serverSocket.send(new DatagramPacket(sendData, sendData.length, i.address, i.port));
                  }
               }
               // System.out.println("sent to "+length+" people");
            }catch(Exception e){
               e.printStackTrace();
            }
         }
      }).start();
   }

   public InetAddress getInetAddress(){
      return this.serverAddress;
   }


   public synchronized void run(){

      System.out.println("Server is now Listening...");
      while(gameRunning){
         try{


            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            this.serverSocket.receive(receivePacket);

            int sentencelen = receivePacket.getLength();
            if(sentencelen > 0){

               String sentence = new String(receivePacket.getData()).substring(0, sentencelen);

               String[] res = sentence.split("\\.");
               String name = res[0];
               boolean isConnect = res[1].equals("connect");

               Client toAdd = new Client(name,receivePacket.getAddress(), receivePacket.getPort());
               if(isConnect /*clientNum == 0 || !toAdd.in(clients)*/){
                  this.clients[this.clientNum++] = toAdd;
                  System.out.println("someone connected");
               }else{
                  sendToEveryone(name, sentence, clients, serverSocket);
               }
            }
         }catch(Exception e){
            e.printStackTrace();
         }
      }

      System.out.println("Server closed... ");
   }

   public void start(){
      (new Thread(this)).start();      
   }

   //
   // Internal Classes
   //
   
   static class Client{
      InetAddress address;
      int port;
      String name;

      public Client(String name, InetAddress ad, int po){
         this.address = ad;
         this.port = po;
         this.name = name;
      }

      public boolean isEqual(Client e){
         return this.name.equals(e.name);
      }
      public boolean isEqual(String s){
         return this.name.equals(s);
      }

      public boolean in(Client[] c){
         for(Client e : c){
            if(e == null)continue;
            if(this.isEqual(e))return true;
         }
         return false;
      }

   }

   public static void main(String[] args) {
      (new UDPServer()).start();
   }
}