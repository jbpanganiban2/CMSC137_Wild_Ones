import proto.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Chat extends JPanel{

    //
    //  ATTRIBUTES
    //
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;  
    static final int ENTER0 = 01;
    static final int ENTER1 = 02;
    private boolean chatting;
    
    static Lobby l;
    static JButton sendMessage;
    static JScrollPane jpane;
    static JTextArea chatBox;
    static JTextField messageBox;
    static JPanel southPanel;
   
    static Socket server;
    static String pusername;                            // the "player" username
    static String rusername;                            // the username of other players that will invoke addMessageToBox function

    //
    //  CONSTRUCTORS
    //

    Chat(Lobby l, Socket s, String pu){
        
        this.l = l;
        createChat(s, pu);
    }

    public void createChat(Socket s, String pu) {
        BorderLayout bl = new BorderLayout();
        bl.setVgap(-3);
        server = s;
        pusername = pu;

        messageBox = new JTextField(35);
        messageBox.setBorder(BorderFactory.createLineBorder(new Color(150, 75, 0), 2,true));
        messageBox.setBackground(new Color(0,0,0,0));
        messageBox.setOpaque(false);
        messageBox.requestFocus();
        this.sendMessage = new JButton("send");
        this.sendMessage.setContentAreaFilled(false);
        this.sendMessage.setOpaque(false);
        this.sendMessage.addActionListener(new sendMessageButtonListener());
        this.sendMessage.setBorder(BorderFactory.createLineBorder(new Color(150, 75, 0), 2,true));
       
        this.chatBox = newChatBox();
        this.jpane = newJPane();

        this.southPanel = newSouthPanel(messageBox, sendMessage);

        this.setLayout(bl);    
        this.add(southPanel,BorderLayout.CENTER);
        this.add(jpane, BorderLayout.NORTH);
        this.setPreferredSize(new Dimension(250, 150));
        this.setBackground(new Color(255,206,120));

        this.addKeyBindings();
    }

    //
    //  INTERNAL CLASSES
    //
 
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            ChatUtils.sendMessage(server, messageBox.getText());
            messageBox.setText("");
        }
    }

    class Move extends AbstractAction{

        @Override
        public void actionPerformed(ActionEvent e){
            enterPressed();
        }

    }

    //
    // METHODS
    //

    synchronized public static void setChatterUsername(String newName){
        rusername = newName;
    }

    public void enterPressed(){
        if(this.chatting){
            ChatUtils.sendMessage(server, messageBox.getText());
            this.messageBox.setText("");
            this.chatting = false;
            try{
                this.l.getActiveGame().getUserCharacter().enable();
                System.out.println("enabled");
            }catch(Exception e){}
        }else{
            this.messageBox.requestFocus();
            this.chatting = true;
            try{
                this.l.getActiveGame().getUserCharacter().disable();
                System.out.println("disabled");
            }catch(Exception e){}
        }
    }

    public static void addMessageToBox(String username, String message){    // function that puts the name and the message of sender to the chatbox
        

        // System.out.println("message received == "+message);
        if(message.contains("!ALERT!")){
            int index = message.indexOf("\n");
            message = message.substring(index+1, message.length());
        }else if(message.contains("<SERVERIPADDRESS>")){
            int index = message.indexOf("/");
            message = message.substring(index+1, message.length());

            System.out.println(message + " is the message");

            InetAddress serverAddress = null;
            try{serverAddress = InetAddress.getByName(message);}catch(Exception e){}
            
            l.setServerAddress(serverAddress);
            return;
        }else{
            message = username + ":  " + message;                
        }

        if (message.length() >= 1) {
            if (message.equals(".clear")) {
                chatBox.setText("");
            } else {
                chatBox.append(message + "\n");
                System.out.println(message);
            }
        }
    }

    public void addKeyBindings(){
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), ENTER0);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), ENTER1);

        this.getActionMap().put(ENTER0, new Move());
        this.getActionMap().put(ENTER1, new Move()); 
    }

    //
    //  GUI INITIALIZATION METHODS
    //
    
    private static JTextArea newChatBox(){                                 // Initializes a new Chatbox
        JTextArea chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setOpaque(false);
        chatBox.setLineWrap(true);
        chatBox.setFont(new Font("Serif", Font.BOLD, 15));
        return chatBox;
    }

    private static JScrollPane newJPane(){
        JScrollPane jpane = new JScrollPane(chatBox);
        jpane.setBorder(BorderFactory.createLineBorder(new Color(150, 75, 0), 2,true));
        jpane.setPreferredSize(new Dimension(250,120));
        jpane.getViewport().setOpaque(false);
        jpane.setOpaque(false);
        return jpane;
    }
    private static JPanel newSouthPanel(JTextField messageBox, JButton sendMessage){
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        southPanel.setPreferredSize(new Dimension(250,20));
        southPanel.setBorder(BorderFactory.createLineBorder(new Color(150, 75, 0), 2,true)); 
        southPanel.setOpaque(false);
        southPanel.add(messageBox);
        southPanel.add(sendMessage);
        return southPanel;
    }
}