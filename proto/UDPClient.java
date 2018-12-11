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
   private Lobby l;

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

   public UDPClient(String name, Lobby l){
      try{
         this.clientSocket = new DatagramSocket();
         this.ipaddress = InetAddress.getByName("localhost");
         this.alive = true;
         this.port = 1975;
         this.name = name;
         this.l = l;
      }catch(Exception e){
         e.printStackTrace();
      }
   }

   public UDPClient(String name, Lobby l, InetAddress i){
      try{
         this.clientSocket = new DatagramSocket();
         this.ipaddress = i;
         this.alive = true;
         this.port = 1975;
         this.name = name;
         this.l = l;
      }catch(Exception e){
         e.printStackTrace();
      }
      System.out.println(i);
   }

   private Point extractPoint(String s){
      String[] position = s.split("\\(|,|\\)");
      int x = Integer.parseInt(position[1]);
      int y = Integer.parseInt(position[2]);

      return new Point(x,y);
   }

   private void commandChar(String command){



      System.out.println("command = "+command);

      String[] commandArray = command.split("\\.");



      // System.out.println("command = "+command);

      try{
         switch(commandArray[1]){
            case "start":
               System.out.println("startGame");
               this.l.startHostGame();
            break;
            case "cType":   
               System.out.println("setCharacterType");
               this.l.getHashMap().put(commandArray[0],Integer.parseInt(commandArray[2]));
               // System.out.println("type = "+commandArray[2]);
            break;
            case "cmove":
               System.out.println("moveChar");
               this.l.getActiveGame().moveChar(commandArray[0],extractPoint(commandArray[2]));
            break;
            case "rocket":
               System.out.println("deployRocket");
               this.l.getActiveGame().deployCharRocket(commandArray[0], extractPoint(commandArray[2]), extractPoint(commandArray[3]),Integer.parseInt(commandArray[4]));
            break;
            default:
               System.out.println("error packet received");
         }

   
      }catch(Exception e){}

   }


   public synchronized void startReceiving(){
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

                     int sentencelen = receivePacket.getLength();
                     if(sentencelen > 0){

                        String sentence = new String(receivePacket.getData()).substring(0, sentencelen);

                        // System.out.println(sentence);
                        commandChar(sentence);
                     }
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

   public void sendStart(){
      this.send("start");
   }

   public void send(Point p){ // send a character movement packet
      String toSend = "cmove.("+Integer.toString((int)p.getX())+","+Integer.toString((int)p.getY())+")";
      this.send(toSend);
   }

   public void send(Point o, Point p, int damage){ // send a rocket Deployment packet
      String toSend = "rocket.("+Integer.toString((int)o.getX())+","+Integer.toString((int)o.getY())+")";
      toSend += ".("+Integer.toString((int)p.getX())+","+Integer.toString((int)p.getY())+")";
      toSend += "."+Integer.toString(damage);
      this.send(toSend);
   }

   public void sendType(int typ){
      String toSend = "cType."+Integer.toString(typ);
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