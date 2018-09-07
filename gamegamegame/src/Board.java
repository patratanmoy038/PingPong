import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable{
	global_vars globe;
	Thread thread;
	private int whoseturn;
	public int local_player;
	private udp_client socketClient1, socketClient2, socketClient3;
	udp_client_handshake socketClientcl;
	private udp_server socketServer1, socketServer2, socketServer3;
	udp_server_handshake socketServerhost;
	public int _game_state;//0: not running; 1: Single player; 2: Multi-player ; 3: game-over
	public int _host_game; // 0 : none; 1 : host_game; -1: join game
	public int multiplayer_game_running;
	//Components
	int[] _show_paddles;
	Paddle[] paddles;
	Ball ball; Ball ball2;
	public static final int B_SIDE = 700;
	
	//image
	Image img = null;
	File imagefile = new File("C:/Users/pulkit sharma/Documents/workspace/GameGameGame/res/background/bgd_01.jpg");
	
	public int _number_of_hits;
	JButton start_multiplayer_game_button;
	
	public Board(int local_player_id, global_vars gloobe) throws IOException{
		//globe = new global_vars(B_SIDE, local_player_id, "192.168.189.1", B_SIDE/2 - 5, B_SIDE/2 -5, B_SIDE/2-40, B_SIDE/2-40, B_SIDE/2 - 40, B_SIDE/2-40, 0, 2);
		this.globe = gloobe;
		imagefile = new File(globe.image_location);
		load_image();
		whoseturn = 1;
		local_player = local_player_id;
		globe._local_player = local_player;
		multiplayer_game_running = 0;
		/********************** Create four paddles for each player *************************/
		paddles = new Paddle[4];
		paddles[0] = new Paddle(B_SIDE/2-40, B_SIDE-20, 1, 1, this);
		paddles[1] = new Paddle(10, B_SIDE/2-40, 2, 2, this);
		paddles[2] = new Paddle(B_SIDE/2 - 40, 10, 3, 1, this);
		paddles[3] = new Paddle(B_SIDE - 20, B_SIDE/2-40, 4, 2, this);
		/**********/
		ball = new Ball(B_SIDE/2 - 5, B_SIDE/2 - 5, 0, 3, globe, this);
		if (globe.num_balls != 1)
			ball2 = new Ball(B_SIDE/2 - 15, B_SIDE - 15, 3, 0, globe, this);
		
		_game_state = globe._game_type;
		_host_game = globe._host_game_this;
		_number_of_hits = 0;
		
		_show_paddles = new int[4];
		for (int i = 0; i < 4; i++){			
			_show_paddles[i] = 1;//Paddles to be shown = 1			
		}
		
		//make local players active
		globe._is_p1_active = 0;
		globe._is_p2_active = 0;
		globe._is_p3_active = 0;
		globe._is_p4_active = 0;
		make_active(local_player - 1);
		
		/************** Start Multi-player Game Button ***********/
		start_multiplayer_game_button = new JButton("Start it");
		start_multiplayer_game_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (_game_state == 2 && _host_game == 1)
					start_multiplayer_game(1);
			}
		});
		//TEMP vars
//		_host_game = 1;
//		_game_state = 2;
		start();
	}
	
	public void start(){
		thread = new Thread(this);
		thread.start();
		if (_game_state == 1){
			ball.start();
			if (globe.num_balls != 1)
				ball2.start();
			//make_inactive(0);
			make_inactive(1);
			make_inactive(2);
			make_inactive(3);
		}
		if (_game_state == 2){
			//this.add(start_multiplayer_game_button);
			if (globe._multiplayer_game_running == 1)
				this.start_multiplayer_game(1);
			/********************* After initial Handshakes ********************/
			/*if (_host_game == 1){
				globe.ips[0] = new String(globe._current_server);
				socketServerhost = new udp_server_handshake(this.globe, globe.hand_shake_port);
				socketServerhost.start();
			}
			if (_host_game == -1){
				socketClientcl = new udp_client_handshake(this.globe, globe._current_server, globe.hand_shake_port);
				socketClientcl.start();
			}*/
		}
	}
	
	
	@Override
	public void run(){
		boolean running = true;
		while (running){
			this.update();
			this.life_update();
			try{
				Thread.sleep(17);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	protected void paintComponent(Graphics g) {
	    setOpaque(false);
	    g.drawImage(img, 0, 0, this);
	    super.paintComponent(g);
	    setOpaque(true);
	    
	    /*********** Create a circle at center ****************/
	    Graphics2D g2d = (Graphics2D)g.create();
	    Shape oval = new Ellipse2D.Double(B_SIDE/2 - 50, B_SIDE/2-50, 100, 100);
	    g2d.setColor( Color.WHITE );
	    g2d.setStroke(new BasicStroke(4f));
	    g2d.draw(oval);
	    g2d.dispose();
	    
	    /***************** Paint all the paddles *******************/
	    for (int i = 0; i < 4; i++){
	    	if (_show_paddles[i] == 1)
	    		paddles[i].render(g);
	    }
	    /*********** Paint ball ***********/
	    ball.render(g);
	    if (globe.num_balls != 1){
	    	ball2.render(g);
	    }
	    
	    /*********** Corners at corners ***************************/
	    g.fillRect(0, 0, globe.corner_side, globe.corner_side);
	    g.fillRect(0, B_SIDE - globe.corner_side, globe.corner_side, globe.corner_side);
	    g.fillRect(B_SIDE - globe.corner_side, B_SIDE - globe.corner_side, globe.corner_side, globe.corner_side);
	    g.fillRect(B_SIDE - globe.corner_side, 0, globe.corner_side, globe.corner_side);
	    
	    /**************** Scores *********************/
	    g.setColor(Color.WHITE);
	    g.drawString("" + globe.lives_left[0], B_SIDE/2, B_SIDE - 30);
	    g.drawString("" + globe.lives_left[1], 30, B_SIDE/2);
	    g.drawString("" + globe.lives_left[2], B_SIDE/2, 30);
	    g.drawString("" + globe.lives_left[3], B_SIDE - 30, B_SIDE/2);
	}
	
//	public static void main(String[] args) {
//
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new BorderLayout());
//
//        Board pong_board;
//		try {
//			global_vars globee = new global_vars(B_SIDE, 0, "192.168.189.1", B_SIDE/2 - 5, B_SIDE/2 -5, B_SIDE/2-40, B_SIDE/2-40, B_SIDE/2 - 40, B_SIDE/2-40, 0, 6);
//			pong_board = new Board(1, globee);
//			frame.add(pong_board, BorderLayout.CENTER);
//			//pong_board.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        frame.setSize(B_SIDE + 17, B_SIDE + 40);
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
//        frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
//        frame.setVisible(true);
//
//    }
	
	private void load_image(){
		try {
			Image dimg = ImageIO.read(imagefile);
			img = dimg.getScaledInstance(B_SIDE, B_SIDE, Image.SCALE_SMOOTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start_multiplayer_game(int val){
		globe._multiplayer_game_running = 1;
		if (_game_state == 2 && globe._multiplayer_game_running == 1){
			multiplayer_game_running = 1;
			this.ball.start();
			if (multiplayer_game_running == 1){
				if (_host_game == 1){
					//this.ball.start();
					//this.socketServerhost.interrupt();
//					socketServer0 = new udp_server(this, 6904);
//					socketServer0.start();
				}
				if (_host_game == -1){
					//this.socketClientcl.interrupt();
//					socketClient0 = new udp_client(this, "192.168.189.1", 6904);
//					socketClient0.start();
				}
				//Initialize P2P ports for player 1
				if (local_player == 1){
					if (!this.globe.ips[1].equals("NA")){
						socketServer1 = new udp_server(this, 6904);
						socketServer1.start();
						socketClient1 = new udp_client(this, globe.ips[1], 6905);
						socketClient1.start();
					}
					if (!this.globe.ips[2].equals("NA")){
						socketServer2 = new udp_server(this, 6913);
						socketServer2.start();
						socketClient2 = new udp_client(this, globe.ips[2], 6906);
						socketClient2.start();
					}
					if (!this.globe.ips[3].equals("NA")){
						socketServer3 = new udp_server(this, 6912);
						socketServer3.start();
						socketClient3 = new udp_client(this, globe.ips[3], 6907);
						socketClient3.start();
					}
				}
				//Initialize ports for player 2
				else if (local_player == 2){
					if (!this.globe.ips[0].equals("NA")){
						socketServer1 = new udp_server(this, 6905);
						socketServer1.start();
						socketClient1 = new udp_client(this, globe.ips[0], 6904);
						socketClient1.start();
					}
					if (!this.globe.ips[2].equals("NA")){
						socketServer2 = new udp_server(this, 6914);
						socketServer2.start();
						socketClient2 = new udp_client(this, globe.ips[2], 6908);
						socketClient2.start();
					}
					if (!this.globe.ips[3].equals("NA")){
						socketServer3 = new udp_server(this, 6911);
						socketServer3.start();
						socketClient3 = new udp_client(this, globe.ips[3], 6909);
						socketClient3.start();
					}
				}
				//Initialize ports for player 3
				else if (local_player == 3){
					if (!this.globe.ips[0].equals("NA")){
						socketServer1 = new udp_server(this, 6906);
						socketServer1.start();
						socketClient1 = new udp_client(this, globe.ips[0], 6913);
						socketClient1.start();
					}
					if (!this.globe.ips[1].equals("NA")){
						socketServer2 = new udp_server(this, 6908);
						socketServer2.start();
						socketClient2 = new udp_client(this, globe.ips[1], 6914);
						socketClient2.start();
					}
					if (!this.globe.ips[3].equals("NA")){
						socketServer3 = new udp_server(this, 6910);
						socketServer3.start();
						socketClient3 = new udp_client(this, globe.ips[3], 6915);
						socketClient3.start();
					}
				}
				//Initialize ports for player 2
				else if (local_player == 4){
					if (!this.globe.ips[0].equals("NA")){
						socketServer1 = new udp_server(this, 6907);
						socketServer1.start();
						socketClient1 = new udp_client(this, globe.ips[0], 6912);
						socketClient1.start();
					}
					if (!this.globe.ips[1].equals("NA")){
						socketServer2 = new udp_server(this, 6909);
						socketServer2.start();
						socketClient2 = new udp_client(this, globe.ips[1], 6911);
						socketClient2.start();
					}
					if (!this.globe.ips[2].equals("NA")){
						socketServer3 = new udp_server(this, 6915);
						socketServer3.start();
						socketClient3 = new udp_client(this, globe.ips[2], 6910);
						socketClient3.start();
					}
				}
			}
		}
	}
	
	private void update(){
		this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
            	int dist = 0;
            	//When the mouse is moved, it will call on this function to change the paddle_y variable (Within the JFrame class).
            	if (local_player == 1 || local_player == 3)
            		dist = e.getX() - (paddles[local_player-1]).getX();
            	if (local_player == 2 || local_player == 4)
            		dist = e.getY() - (paddles[local_player-1]).getY();
                paddles[local_player-1].move(dist, local_player - 1);
                //pong_board.repaint();
            }
			@Override
			public void mouseDragged(MouseEvent arg0) {}
		});
		
		/******************************** UPDATE GAME BASED ON RECEIVED PARAMS ************************************/
		if (_game_state == 1){
			//this.ball.move_ball(globe);
			if (this.globe._is_p1_active == 0)
				this.paddles[0].paddle_AI("easy");	
			if (this.globe._is_p2_active == 0)
				this.paddles[1].paddle_AI("easy");
			if (this.globe._is_p3_active == 0)
				this.paddles[2].paddle_AI("easy");
			if (this.globe._is_p4_active == 0)
				this.paddles[3].paddle_AI("easy");
			/*for (int i = 0; i < 4; i++){
				if (i != local_player - 1)
					this.paddles[i].relocate(globe._paddles[i], i);
			}*/
			
			/***********game over *************/
			if (globe.lives_left[local_player - 1] == 0){
				this._game_state = 3;
			}
		}
		if (_game_state == 2){
			if (globe._multiplayer_game_running == 1 && this.multiplayer_game_running == 0){
				this.multiplayer_game_running = 1;
				this.start_multiplayer_game(1);
			}
			else if (this.multiplayer_game_running == 1){
				//Start AI for remaining players
//				if (this.globe._is_p1_active == 0)
//					this.paddles[0].paddle_AI("easy");	
//				if (this.globe._is_p2_active == 0)
//					this.paddles[1].paddle_AI("easy");
//				if (this.globe._is_p3_active == 0)
//					this.paddles[2].paddle_AI("easy");
//				if (this.globe._is_p4_active == 0)
//					this.paddles[3].paddle_AI("easy");
//				if (globe.lives_left[local_player - 1] == 0 || globe._total_players == 1){
//					this._game_state = 3;
//				}
			}
		}
		else if (this._game_state == 3){
			//game over
			if (this.globe.lives_left[local_player - 1] != 0){
				int val = JOptionPane.showConfirmDialog(this, "You Won. Quit?", "GAME OVER", JOptionPane.YES_NO_OPTION);
				if (val == 0){
					this.setVisible(false);
				}
			}
			else{
				int val = JOptionPane.showConfirmDialog(this, "You Lost. Quit?", "GAME OVER", JOptionPane.YES_NO_OPTION);
				if (val == 0){
					this.setVisible(false);
				}
			}
			Ball.interrupted();
			globe._multiplayer_game_running = 0;
			this.multiplayer_game_running = 0;
			this._game_state = 0;
		}

		this.repaint();
	}
	
	/********************** Update number of lives **************************/
	public void life_update(){
		for(int i = 0; i < 4; i++){
			if (globe.lives_left[i] == 0) {
				remove_paddle(i);
			}
		}
	}
	
	public void change_game_state(int new_state){this._game_state = new_state;}
	//Adding and removing players
	public void make_active(int player_to_be){this.globe.add_player(player_to_be);}
	public void make_inactive(int player_to_be){this.globe.remove_player(player_to_be);}
	public void remove_paddle(int player_to_be){
		this._show_paddles[player_to_be] = 0;
		if (_game_state == 2)
			this.globe.ips[player_to_be] = "NA";
	}
	
	/*************** Migrate Server ******************/
	public void migrate_server(){
		try {
			Ball.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		globe.ips[globe._current_server_id] = "NA";
		int new_server_id = 0;
		while (globe.ips[new_server_id].equals("NA")){
			new_server_id++;
			if (new_server_id >= 4)
				break;
		}
		if (new_server_id < 4){
			globe._current_server_id = new_server_id;
			globe._current_server = globe.ips[new_server_id];
			
			if (globe._current_server_id == local_player - 1)
			{
				this._host_game = 1;
				this.globe._host_game_this = 1;
			}
			else{

			}
		}
	}
	
	/*************** Handle a player drop ******************/
	public void drop_player(String ipaddress){
		int dropped_out_id = 0;
		while (globe.ips[dropped_out_id].equals(ipaddress) && dropped_out_id < 4)
			dropped_out_id++;
		//player Dropped
		if (dropped_out_id < 4){
			if (this.globe._current_server_id == dropped_out_id)
				this.migrate_server();
			else{
				remove_paddle(dropped_out_id);
				this.make_inactive(dropped_out_id);
			}
		}
	}
}