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

   //
   // Methods
   //

   public void stopServer(){
      this.gameRunning = false;
   }

   public static void sendToEveryone(String toSend, Client[] everyone, DatagramSocket serverSocket){
      (new Thread(){
         @Override
         public void run(){
            byte[] sendData = toSend.getBytes();
            // int length = 0;
            try{
               for(Client i : everyone){
                  if(i != null){
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
               }else{
                  sendToEveryone(sentence, clients, serverSocket);
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