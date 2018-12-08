import java.io.*;
import java.net.*;
import java.util.*;

public class UDPHost extends UDPClient{
     private UDPServer server;

     public UDPHost(String name){
          super(name);
          this.server = new UDPServer();
          this.server.start();

     }

     public void endGame(){
          this.server.stopServer();
     }

     public static void main(String[] args) {
          new UDPHost(args[0]).start();
     }

}