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

public class Lobby extends JFrame implements ActionListener {
	
	//logistics
	public global_vars lobby_menu_globe;
	private udp_client_handshake socketClientcl;
	private udp_server_handshake socketServerhost;
	
	// Graphics
	JLabel statusbar = new JLabel(" 0");
	private Container cont;
	private JPanel _local_panel;
	private JButton start_game;
	private JButton refresh;
	private JButton cancel;
	
	private JLabel[] player_s;
	
	public Lobby(global_vars globe){
		lobby_menu_globe = globe;
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
		_local_panel.setLayout(new GridLayout(5, 2, 15, 10));
		start_game = new JButton("START GAME");
		refresh = new JButton("Refresh");
		cancel = new JButton("CANCEL");
		start_game.setPreferredSize(new Dimension(30, 10));
		//start_game.setLocation(50, 500);
        start_game.addActionListener(this);
        refresh.addActionListener(this);
        cancel.addActionListener(this);
        
        for(int i = 0; i < 4; i++){
        	player_s[i] = new JLabel("Empty");
        	player_s[i].setSize(50, 20);
        	_local_panel.add(player_s[i]);
        }
        
        _local_panel.add(start_game);
		_local_panel.add(refresh);
        _local_panel.add(cancel);
  	}

	public void update_panel(){
		for (int i = 0; i < this.lobby_menu_globe._total_players; i++){
			String s = this.lobby_menu_globe.ips[i];
			player_s[i].setText(s);
		}
		this.revalidate();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Object a = arg0.getSource();
		if(a == start_game){
			this.lobby_menu_globe._multiplayer_game_running = 1;
			this.dispose();
			JFrame frame = new JFrame();
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setLayout(new BorderLayout());
	        frame.setSize(500 + 17, 500 + 40);
	        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	        //frame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.RED));
	        frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
	        frame.setVisible(true);
			try {
				frame.add(new Board(this.lobby_menu_globe._local_player, this.lobby_menu_globe));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (a == refresh){
			this.update_panel();
		}
		if(a == cancel){
			this.dispose();
		}
	}
}