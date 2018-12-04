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

    Chat(Socket s,String pu){
        
        createChat(s, pu);
    }

    public void createChat(Socket s, String pu) {
        server = s;
        pusername = pu;

        messageBox = new JTextField(50);
        messageBox.setOpaque(false);
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
        this.setPreferredSize(new Dimension(250, 270));
    }

    //
    //  INTERNAL CLASSES
    //
 
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            ChatUtils.sendMessage(server, messageBox.getText());
            // addMessageToBox("test", messageBox.getText());
            messageBox.setText("");
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

    //
    //  GUI INITIALIZATION METHODS
    //
    
    private static JTextArea newChatBox(){                                 // Initializes a new Chatbox
        JTextArea chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setBorder(BorderFactory.createLineBorder(new Color(150, 75, 0)));
        chatBox.setOpaque(true);
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