import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class Chat extends JPanel{
	
    JButton sendMessage;
    JTextField messageBox;
    JTextArea chatBox;
    String username;
    JScrollPane jpane;
   
	Chat(){
		
		createChat();
	}

    public void createChat() {
        this.setPreferredSize(new Dimension(250, 270));
		this.setLayout(new BorderLayout());    
        JPanel southPanel = new JPanel();

        this.add(BorderLayout.SOUTH, southPanel);
        
        southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        messageBox = new JTextField(50);
        sendMessage = new JButton("send");
        southPanel.setOpaque(false);
        messageBox.setOpaque(false);
        sendMessage.setOpaque(false);

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setBorder(null);
        chatBox.setOpaque(false);
     

     	this.jpane = new JScrollPane(chatBox);
        this.add(jpane, BorderLayout.CENTER);
        jpane.getViewport().setOpaque(false);
        jpane.setOpaque(false);
      

        chatBox.setLineWrap(true);

    

        southPanel.add(messageBox);
        southPanel.add(sendMessage);


        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        sendMessage.addActionListener(new sendMessageButtonListener());
     
    }

 
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing 
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                chatBox.append("<" + username + ">:  " + messageBox.getText() + "\n");
                messageBox.setText("");
            }
        }
    }
}