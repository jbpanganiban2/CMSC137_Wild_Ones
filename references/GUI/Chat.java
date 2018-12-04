import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class Chat extends JPanel{
	
    static JButton sendMessage;
    static JTextField messageBox;
    static JTextArea chatBox;
    static JScrollPane jpane;
   
    static String pusername;
    static String rusername;

	Chat(String pu){
		
		createChat(pu);
	}

    public void createChat(String pu) {
        this.setPreferredSize(new Dimension(250, 270));
		this.setLayout(new BorderLayout());    
        JPanel southPanel = new JPanel();

        this.pusername = pu;

        this.add(BorderLayout.SOUTH, southPanel);
        
        messageBox = new JTextField(50);
        messageBox.setOpaque(false);
        
        sendMessage = new JButton("send");
        sendMessage.setOpaque(false);

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setBorder(null);
        chatBox.setOpaque(false);
        chatBox.setLineWrap(true);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));     

        this.jpane = new JScrollPane(chatBox);
        jpane.getViewport().setOpaque(false);
        jpane.setOpaque(false);

        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        southPanel.setOpaque(false);
        southPanel.add(messageBox);
        southPanel.add(sendMessage);

        this.add(jpane, BorderLayout.CENTER);

        sendMessage.addActionListener(new sendMessageButtonListener());
     
    }


    public static void addMessageToBox(String username, String message){
        if (message.length() > 1) {
            if (message.equals(".clear")) {
                chatBox.setText("");
            } else {
                chatBox.append("<" + username + ">:  " + messageBox.getText() + "\n");
            }
        }
    }

 
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            addMessageToBox(pusername, messageBox.getText());
            messageBox.setText("");
        }
    }
}