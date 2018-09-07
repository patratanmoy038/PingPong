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

public class Multiplayer extends JFrame implements ActionListener {
	
	//logistics
	public global_vars multiplayer_menu_globe;
	private udp_client_handshake socketClientcl;
	private udp_server_handshake socketServerhost;
	
	// Graphics
	JLabel statusbar = new JLabel(" 0");
	private Container cont;
	private JFrame _start_menu;
	private JPanel _local_panel;
	private JButton Host_a_game;
	private JButton connect;
	private JButton start_game;

	
	public Multiplayer(global_vars globe){
		multiplayer_menu_globe = globe;
		_local_panel = new JPanel();
		update_panel();
		_start_menu = new JFrame();
		_start_menu.setLocation(400, 200);
		_start_menu.setSize(400,400);
        _start_menu.add(_local_panel);
        _start_menu.setVisible(true);
        _start_menu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //_start_menu.repaint();
        start_game = new JButton("Start game");
    	start_game.addActionListener(this);
	}
	
	
	public void update_panel(){
		_local_panel.setLayout(new GridLayout(5, 2, 500, 5));
		Host_a_game = new JButton("HOST A GAME");
		connect = new JButton("CONNECT");
		_local_panel.add(Host_a_game);
        _local_panel.add(connect);
        Host_a_game.addActionListener(this);
        connect.addActionListener(this);
  	}

	public void actionPerformed(ActionEvent arg0) {
		Object a = arg0.getSource();
		if(a == Host_a_game){
			this.multiplayer_menu_globe._game_type = 2;
			this.multiplayer_menu_globe._host_game_this = 1;
			String input_ip = JOptionPane.showInputDialog("Put your IP address : ");
			this.multiplayer_menu_globe._current_server = input_ip.trim();
			
			/******************* Hand shaking stuff *******************/
			this.multiplayer_menu_globe.ips[0] = new String(this.multiplayer_menu_globe._current_server);
			socketServerhost = new udp_server_handshake(this.multiplayer_menu_globe, this.multiplayer_menu_globe.hand_shake_port);
			socketServerhost.start();
			
			/*********** Add button to start the game ***********/
			JLabel loading = new JLabel("hosting game ...");
			_local_panel.add(loading);
			//_local_panel.add(start_game);
			this.revalidate();
			
			new Lobby(this.multiplayer_menu_globe);
			
			if (this.multiplayer_menu_globe._multiplayer_game_running == 1){
				/***************** Start JFrame ******************/
				this._start_menu.dispose();
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
					frame.add(new Board(this.multiplayer_menu_globe._local_player, this.multiplayer_menu_globe));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}if(a == connect){
			String input_server_ip = JOptionPane.showInputDialog("Put server ip : ");
			this.multiplayer_menu_globe._current_server = input_server_ip;
			this.multiplayer_menu_globe._game_type = 2;
			this.multiplayer_menu_globe._host_game_this = -1;
			
			/***************** Hand Shaking Stuff ******************/
			socketClientcl = new udp_client_handshake(multiplayer_menu_globe, this.multiplayer_menu_globe._current_server, this.multiplayer_menu_globe.hand_shake_port);
			socketClientcl.start();
			
			/*********** Add button to start the game ***********/
			JLabel loading = new JLabel("hosting game ...");
			_local_panel.add(loading);
			//_local_panel.add(start_game);
			this.revalidate();
			
			//new Lobby_client(this.multiplayer_menu_globe);
			
			if (this.multiplayer_menu_globe._multiplayer_game_running == 1){
				/***************** Start JFrame ******************/
				this._start_menu.dispose();
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
					frame.add(new Board(this.multiplayer_menu_globe._local_player, this.multiplayer_menu_globe));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (a == start_game){
			this.multiplayer_menu_globe._multiplayer_game_running = 1;
		}
	}
}