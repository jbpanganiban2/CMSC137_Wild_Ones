import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class MainGUI {
	
	static JFrame mainFrame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        create();
        // Lobby lobby = new Lobby();
    }

   	static void create(){

	 	mainFrame = new JFrame("Wild Ones");
	 	
	 	//buttons and textfields
	 	ImageIcon hostIcon = new ImageIcon("./src/HOST.png");
	 	ImageIcon clientIcon = new ImageIcon("./src/CLIENT.png"); 
	 	JTextField name = new JTextField();
	 	JButton host = new JButton("HOST");
	 	JButton client = new JButton("CLIENT");
	 	name.setPreferredSize(new Dimension(135,50));
	 	name.setOpaque(false);
	 	name.setText("username");
	 	host.setPreferredSize(new Dimension(140,50));
	 	host.setOpaque(false);
		host.setContentAreaFilled(false);
		host.setBorderPainted(false);
	 	host.setIcon(hostIcon);
	 	client.setPreferredSize(new Dimension(150,50));
	 	client.setOpaque(false);
		client.setContentAreaFilled(false);
		client.setBorderPainted(false);
	 	client.setIcon(clientIcon);

	 	host.addActionListener(new hostChar());


	 	
	 	//container
	 	JPanel container = new JPanel();
	 	container.setLayout(new GridBagLayout());
	 	container.setPreferredSize(new Dimension(150,170));
	 	GridBagConstraints center = new GridBagConstraints();
	 	container.setOpaque(false);
	 	center.gridx = 0;
		center.insets = new Insets(0,0,10,0);  //top padding
        center.anchor = GridBagConstraints.PAGE_START;
	 	container.add(name,center);
        center.anchor = GridBagConstraints.CENTER;
		container.add(host,center);
        center.anchor = GridBagConstraints.PAGE_END;
		center.insets = new Insets(0,0,0,0);  //top padding
		container.add(client,center);

	 	// for background
		ImageIcon icon = new ImageIcon("./src/BG.png"); 
		Image newimg = icon.getImage().getScaledInstance(600, 600,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		ImagePanel thumb = new ImagePanel(newIcon.getImage());
		thumb.setPreferredSize(new Dimension(600,600));
		thumb.setLayout(new GridBagLayout());
		center.insets = new Insets(150,0,0,0);  //top padding
		thumb.add(container, center);


    	mainFrame.setVisible(true);
    	mainFrame.setResizable(false);
    	mainFrame.setSize(600,600);
    	mainFrame.add(thumb);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    static class ImagePanel extends JPanel {

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

	static class hostChar implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent event) {
            Lobby lobby = new Lobby();
            mainFrame.setVisible(false);
        }

    }

}