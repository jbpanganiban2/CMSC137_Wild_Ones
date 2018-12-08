import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient
{

   private DatagramSocket clientSocket;
   private InetAddress ipaddress;
   private int port;
   private String name;

   private boolean alive;

   public UDPClient(String name){
      try{
         this.clientSocket = new DatagramSocket();
         this.ipaddress = InetAddress.getByName("localhost");
         this.alive = true;
         this.port = 1975;
         this.name = name;
      }catch(Exception e){
         e.printStackTrace();
      }
   } 

   public void startReceiving(){
      // System.out.println("called");
      (new Thread(){
         DatagramPacket receivePacket;
         String modifiedSentence;

         @Override
         public void run(){
            while(alive){

               try{

                  byte[] receiveData = new byte[1024];
                  receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  clientSocket.receive(receivePacket);

                  if(receivePacket.getLength() > 0){

                     modifiedSentence = new String(receivePacket.getData());

                     // System.out.println(modifiedSentence);
                     String[] toPrint = modifiedSentence.split("\\.");

                     if(toPrint.length > 1){
                        System.out.print(toPrint[0]+": ");
                        System.out.println(toPrint[1]);
                     }
                  }

               }catch( Exception e ){
                  e.printStackTrace();
               }
            }

            clientSocket.close();
         }
      }).start();
   }

   public void startSending(){
      (new Thread(){
         @Override
         public void run(){
            while(alive){
               send((new Scanner(System.in)).nextLine());
            }
         }
      }).start();
   }

   private void connect(){
      String connect = this.name+".connect";
      byte[] sendData = connect.getBytes();
      try{
         clientSocket.send(new DatagramPacket(sendData, sendData.length, this.ipaddress, this.port));
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   public void send(String sentence){
      sentence = this.name+"."+sentence;
      byte[] sendData = sentence.getBytes();
      try{
         clientSocket.send(new DatagramPacket(sendData, sendData.length, this.ipaddress, this.port));
      }catch(Exception e){
         e.printStackTrace();
      }
   }

   public void start(){
      // this.startSending(); // comment out when not needed
      this.startReceiving();
      this.connect();
   }

   public static void main(String[] args) {
      (new UDPClient(args[0])).start();
   }

}