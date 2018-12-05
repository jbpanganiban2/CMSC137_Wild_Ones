import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Lobby{
	static JFrame lobbyFrame;
	JPanel chatArea;
	JPanel chatContainer;
	JButton chatBar;
	JPanel card1;
    JTextField usernameChooser;
    CardLayout cl ;
    Chat chat;
    Lobby(){
    	createLobby();
    }

	public void createLobby() {
        lobbyFrame = new JFrame("Lobby");
        lobbyFrame.setResizable(false);
        this.chat = new Chat();
       	this.chat.setOpaque(false);

       	ImageIcon startIcon = new ImageIcon("./src/START.png");
		JButton start = new JButton();
		start.setPreferredSize(new Dimension(160,50));
	 	start.setOpaque(false);
		start.setContentAreaFilled(false);
		start.setBorderPainted(false);
	 	start.setIcon(startIcon);

	 	start.addActionListener(new startGame());

	 	GridBagConstraints right = new GridBagConstraints();
	 	JPanel top = new JPanel();
	 	top.setLayout(new GridBagLayout());
	 	right.insets = new Insets(30,520,0,0);  //top padding
	 	top.add(start,right);
	 	top.setOpaque(false);

	 	JPanel center = new JPanel();
	 	center.setOpaque(false);


        ImageIcon icon = new ImageIcon("./src/LobbyBG.png"); 
		Image newimg = icon.getImage().getScaledInstance(730, 700,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		ImagePanel bg = new ImagePanel(newIcon.getImage());
		bg.setPreferredSize(new Dimension(730,700));
		bg.setLayout(new BorderLayout());

        // chatContainer.add(chatArea,BorderLayout.EAST);
        bg.add(top,BorderLayout.NORTH); 
        bg.add(center,BorderLayout.CENTER);     
        bg.add(this.chat,BorderLayout.SOUTH); 
        lobbyFrame.setSize(730, 700);
        lobbyFrame.setVisible(true);
        lobbyFrame.add(bg);
        lobbyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // chatBar.addActionListener(new openChat());
    }

    String username;

    class openChat implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent event) {	
        	cl.next(chatArea);
        }
    }

    class startGame implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent event) {
        	// Character ch = new Character();
            Game game = new Game();

            lobbyFrame.setVisible(false);
        }
    }

    class ImagePanel extends JPanel {

    	private Image img;
	  	public ImagePanel(Image img) {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	@Override
	 public void paintComponent(Graphics g) {
	    g.drawImage(img, 0, 0, null);
	  }
	}
}