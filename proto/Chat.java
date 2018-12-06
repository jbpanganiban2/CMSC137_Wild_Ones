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
    
    //
    //
    //  FIX CHAT ENABLING PROBLEM
    //
    //

    public static final int ENTER = 0;

    static JButton sendMessage;
    static JScrollPane jpane;
    static JTextArea chatBox;
    static JTextField messageBox;
    static JPanel southPanel;
   
    static Socket server;
    static String pusername;                            // the "player" username
    static String rusername;                            // the username of other players that will invoke addMessageToBox function
    static boolean chatting;
    static Character c;
    //
    //  CONSTRUCTORS
    //

    Chat(Socket s, String pu){
        
        createChat(s, pu);

    }

    public void createChat(Socket s, String pu) {
        server = s;
        pusername = pu;

        messageBox = new JTextField(50);
        messageBox.setOpaque(false);
        this.chatting = true;
        // messageBox.setBorder(BorderFactory.createLineBorder(new Color(150, 75, 0)));
        this.sendMessage = new JButton("send");
        this.sendMessage.setOpaque(false);
        this.sendMessage.addActionListener(new sendMessageButtonListener());
        this.chatBox = newChatBox();
        this.jpane = newJPane();
        this.southPanel = newSouthPanel(messageBox, sendMessage);
        this.setLayout(new BorderLayout());    
        this.add(BorderLayout.SOUTH, southPanel);
        this.add(jpane, BorderLayout.CENTER);
        // this.setPreferredSize(new Dimension(250, 270));

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), ENTER);
        this.getActionMap().put(ENTER, new Move());

    }

    public synchronized void setCharacter(Character charac){
        this.c = charac;
        (new Thread(){                      // thread that allows the messagebox to be used ONLY when player is not playing
            @Override
            public void run(){
                int i = 0;
                while(true){
                    if(charac.isEnable()){
                        messageBox.setFocusable(false);
                    }else{
                        messageBox.setFocusable(true);
                    }
                }
            } 
        }).start();
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

    class Move extends AbstractAction {             //  sets the chatbox focus
        @Override
        public void actionPerformed(ActionEvent e) {
            if(c.isEnable())return;
            if(messageBox.hasFocus()){
                if(messageBox.getText().equals(""))return;
                ChatUtils.sendMessage(server, messageBox.getText());
                messageBox.setText("");
                chatting = true;
            }else{
                messageBox.requestFocus();
                chatting = false;
            }
        }
     }

    //
    // METHODS
    //

    synchronized public static void setChatterUsername(String newName){
        rusername = newName;
    } 

    public static void addMessageToBox(String username, String message){    // function that puts the name and the message of sender to the chatbox
        if (message.length() >= 1) {
            if (message.equals(".clear")) {
                chatBox.setText("");
            } else {
                chatBox.append(username + ":  " + message + "\n");
                System.out.println(username + ":  " + message);
            }
        }
    }

    public boolean isChatting(){
        return this.chatting;
    }

    //
    //  GUI INITIALIZATION METHODS
    //
    
    private static JTextArea newChatBox(){                                 // Initializes a new Chatbox
        JTextArea chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setBorder(BorderFactory.createLineBorder(new Color(150, 75, 0)));
        chatBox.setOpaque(false);
        chatBox.setLineWrap(true);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        return chatBox;
    }

    private static JScrollPane newJPane(){
        JScrollPane jpane = new JScrollPane(chatBox);
        jpane.getViewport().setOpaque(false);
        jpane.setOpaque(false);
        return jpane;
    }
    private static JPanel newSouthPanel(JTextField messageBox, JButton sendMessage){
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        southPanel.setOpaque(false);
        southPanel.add(messageBox);
        southPanel.add(sendMessage);
        return southPanel;
    }
}