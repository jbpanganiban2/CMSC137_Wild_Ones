import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UDPClient
{

   private DatagramSocket clientSocket;
   private InetAddress ipaddress;
   private int port;
   private String name;

   private boolean alive;
   // private Character c;
   private Game g;

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

   public UDPClient(String name, Game g){
      try{
         this.clientSocket = new DatagramSocket();
         this.ipaddress = InetAddress.getByName("localhost");
         this.alive = true;
         this.port = 1975;
         this.name = name;
         this.g = g;
      }catch(Exception e){
         e.printStackTrace();
      }
   }

   private Point extractPoint(String s){
      String[] position = s.split("\\(|,|\\)");
      int x = Integer.parseInt(position[1]);
      int y = Integer.parseInt(position[2]);

      return new Point(x,y);
   }

   private void commandChar(String command){
      String[] commandArray = command.split("\\.");
      // if( != )
      // this.g.commandChar()
      // System.out.println("length: "+ commandArray.length);
      switch(commandArray.length){
         case 2:
            System.out.println("etners movecerh");
            this.g.moveChar(commandArray[0],extractPoint(commandArray[1]));
         break;

         case 3:
            System.out.println("enters deployCharRocket");
            this.g.deployCharRocket(commandArray[0], extractPoint(commandArray[1]), extractPoint(commandArray[2]));
         break;

         default:
            System.out.println("error received packet");
      }
   }


   public synchronized void startReceiving(){
      // System.out.println("called");
      (new Thread(){
         DatagramPacket receivePacket;
         String modifiedSentence;

         @Override
         public void run(){
            while(alive){

               try{

                  byte[] receiveData = new byte[128];
                  receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  clientSocket.receive(receivePacket);

                  if(receivePacket.getLength() > 0){

                     modifiedSentence = new String(receivePacket.getData());

                     System.out.println(modifiedSentence);
                     commandChar(modifiedSentence);
                     // String[] toPrint = modifiedSentence.split("\\.");

                     // if(toPrint.length > 1){
                     //    System.out.print(toPrint[0]+": ");
                     //    System.out.println(toPrint[1]);
                     // }
                  }

               }catch( Exception e ){
                  e.printStackTrace();
               }
            }

            clientSocket.close();
            System.out.println("udpclient died. ");
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

   public void send(Point p){ // send a character movement packet
      String toSend = "("+Integer.toString((int)p.getX())+","+Integer.toString((int)p.getY())+")";
      this.send(toSend);
   }

   public void send(Point o, Point p){ // send a rocket Deployment packet
      String toSend = "("+Integer.toString((int)o.getX())+","+Integer.toString((int)o.getY())+").";
      toSend += "("+Integer.toString((int)p.getX())+","+Integer.toString((int)p.getY())+")";

      this.send(toSend);
   }

   public void kill(){
      this.alive = false;
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