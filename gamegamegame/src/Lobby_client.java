import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Lobby_client extends JFrame implements ActionListener {
	
	//logistics
	public global_vars lobby_menu_globe;
	private udp_client_handshake socketClientcl;
	private udp_server_handshake socketServerhost;
	private Thread refreshing;
	boolean running = true;
	
	// Graphics
	JLabel statusbar = new JLabel(" 0");
	private Container cont;
	private JPanel _local_panel;
	private JButton refresh;
	private JButton cancel;
	private int start_game; 
	
	private JLabel[] player_s;
	
	public Lobby_client(global_vars globe){
		this.lobby_menu_globe = globe;
		start_game = 0;
		_local_panel = new JPanel();
		player_s = new JLabel[4];
		make_panel();
		this.setLocation(100, 100);
		this.setSize(600, 600);
		this.add(_local_panel);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	
	public void make_panel(){
//		refreshing = new Thread(this);
//		refreshing.start();
		_local_panel.setLayout(new GridLayout(5, 2, 15, 10));
		refresh = new JButton("Refresh");
		cancel = new JButton("CANCEL");
		
		//start_game.setLocation(50, 500);
        refresh.addActionListener(this);
        cancel.addActionListener(this);
        
        for(int i = 0; i < 4; i++){
        	player_s[i] = new JLabel("Empty");
        	player_s[i].setSize(50, 20);
        	_local_panel.add(player_s[i]);
        }
        
		_local_panel.add(refresh);
        _local_panel.add(cancel);
  	}

	public void update_panel(){
		for (int i = 0; i < 4; i++){
			try{
			String s = this.lobby_menu_globe.ips[i];
			player_s[i].setText(s);
			} catch (NullPointerException e){
				e.printStackTrace();
			}
		}
		this.revalidate();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Object a = arg0.getSource();
		if (a == refresh){
			this.update_panel();
		}
		if(a == cancel){
			this.dispose();
		}
	}


//	@Override
//	public void run() {
//		while(running){
//			try {
//				this.update_panel();
//				if (this.lobby_menu_globe._multiplayer_game_running == 1 && this.start_game == 0){
//					this.start_game = 1;
//					this.lobby_menu_globe._game_type = 2;
//					this.dispose();
//					Thread.sleep(10);
////					/***************** Start JFrame ******************/
////					JFrame frame = new JFrame();
////			        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////			        frame.setLayout(new BorderLayout());
////			        frame.setSize(500 + 17, 500 + 40);
////			        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
////			        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
////			        //frame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.RED));
////			        frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
////			        frame.setVisible(true);
////					try {
////						frame.add(new Board(this.lobby_menu_globe._local_player, this.lobby_menu_globe));
////					} catch (IOException e) {
////						e.printStackTrace();
////					}
//					running = false;
//				}
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}