import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Lobby{
	JFrame lobbyFrame;
	JPanel chatArea;
	JPanel chatContainer;
	JButton chatBar;
	JPanel card1;
  JTextField usernameChooser;
  CardLayout cl ;
  Chat chat;

  Lobby(String username){
    createLobby(username);
  }

  public void createLobby(String username) {

    lobbyFrame = new JFrame("Lobby");

    this.chat = new Chat("asdasd");
    this.chat.setOpaque(false);

    ImageIcon startIcon = new ImageIcon("./src/START.png");
    ImageIcon exitIcon = new ImageIcon("./src/EXIT.png");

    ImageIcon icon = new ImageIcon("./src/LobbyBG.png"); 
    Image newimg = icon.getImage().getScaledInstance(730, 700,  java.awt.Image.SCALE_SMOOTH);
    ImageIcon newIcon = new ImageIcon(newimg);
    
    JButton start = new JButton();
    start.setPreferredSize(new Dimension(160,50));
    start.setOpaque(false);
    start.setContentAreaFilled(false);
    start.setBorderPainted(false);
    start.setIcon(startIcon);

    JButton exit = new JButton();
    exit.setPreferredSize(new Dimension(160,50));
    exit.setOpaque(false);
    exit.setContentAreaFilled(false);
    exit.setBorderPainted(false);
    exit.setIcon(exitIcon);


    GridBagConstraints left = new GridBagConstraints();
    GridBagConstraints right = new GridBagConstraints();
    left.anchor = GridBagConstraints.LINE_START;
    left.insets = new Insets(0,0,0,370);  //top padding
    right.anchor = GridBagConstraints.LINE_END;
    // right.insets = new Insets(0,0,0,0);  //top padding

    
    JPanel top = new JPanel();
    top.setLayout(new GridBagLayout());
    top.setPreferredSize(new Dimension(730,70));
    top.add(exit,left);   
    top.add(start,right);
    top.setOpaque(false);

    ImagePanel bg = new ImagePanel(newIcon.getImage());
    bg.setPreferredSize(new Dimension(730,700));
    bg.setLayout(new BorderLayout());
    bg.add(top,BorderLayout.NORTH); 
    bg.add(this.chat,BorderLayout.SOUTH); 

    lobbyFrame.add(bg);
    lobbyFrame.setVisible(true);
    lobbyFrame.setSize(730, 700);
    lobbyFrame.setResizable(false);
    lobbyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }


  class openChat implements ActionListener {
  	@Override
      public void actionPerformed(ActionEvent event) {	
      	cl.next(chatArea);
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