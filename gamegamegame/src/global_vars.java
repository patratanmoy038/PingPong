
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class global_vars {
	//network params
	public int _current_server_id;
	public String _current_server;
	public int _local_player;
	public int lives_left[];
	public int _game_type; // 1: single Player.... 2: multi Player
	public int _host_game_this; // -1: do not host but client .... 1: host
	public int _multiplayer_game_running;
	
	//general game params
	public String image_location;
	public int _board_size;
	public Timer _timer;
	public double rand = Math.random();
	public int paddle_thickness, paddle_length;
	public int corner_side;
	public int _total_players;
	public int _is_p1_active, _is_p2_active, _is_p3_active, _is_p4_active;
	public String _players_names[];
	public String _AI_level;
	public int num_balls;
	
	//Physics variables
	public int _ball_position_x, _ball_position_y;
	public double _ball_velocity_x, _ball_velocity_y;
	public int _paddles[];
	
	//Networking
	public String[] ips;
	public int[] ports;
	public int hand_shake_port;
	
	public global_vars(){
		this._timer = new Timer();
	}
	public void relocate (String message){
		List<String> items = Arrays.asList(message.split("\\s*,\\s*"));
		this._ball_position_x = Integer.parseInt(items.get(0));
		this._ball_position_y = Integer.parseInt(items.get(1));
		for (int i = 0;i < 3;i++){
			if (this._local_player != i+1){
				this._paddles[i] = Integer.parseInt(items.get(i+2));
			}
		}
	}
	public String encode_game_state(){
		String result = Integer.toString(_ball_position_x) + "," + Integer.toString(_ball_position_y) + "," + Integer.toString(_paddles[0]) + "," + Integer.toString(_paddles[1]) + "," + Integer.toString(_paddles[2]) + "," + Integer.toString(_paddles[3]); 
		return result;
	}
	public global_vars(int board_size, int current_server_id, String current_server, int ball_position_x, int ball_position_y, int paddle1, int paddle2, int paddle3, int paddle4, double ball_velocity_x, double ball_velocity_y){
		this.image_location = "C:/Users/pulkit sharma/Documents/workspace/GameGameGame/res/background/bgd_01.jpg";
		this._board_size = board_size;
		this._current_server_id = current_server_id;
		this._current_server = current_server;
		this._local_player = 1;
		
		this._ball_position_x = ball_position_x;
		this._ball_position_y = ball_position_y;
		this._ball_velocity_x = ball_velocity_x;
		this._ball_velocity_y = ball_velocity_y;
		this._paddles = new int[4];
		_paddles[0] = paddle1; _paddles[1] = paddle2; _paddles[2] = paddle3; _paddles[3] = paddle4;
		this._is_p1_active = 1; this._is_p2_active = 0; this._is_p3_active = 0; this._is_p4_active = 0; 
		this._timer = new Timer();
		//Default initialization of names
		_players_names = new String[4];
		_players_names[0] = "A"; _players_names[1] = "B"; _players_names[2] = "C"; _players_names[3] = "D";
		
		this.paddle_thickness = 15;
		this.paddle_length = 80;
		this.corner_side = paddle_thickness + 8;
		
		this.lives_left = new int[4];
		for (int i = 0; i < 4; i++){
			lives_left[i] = 5;
		}
		this._total_players = 0;
		_game_type = 1;
		this._host_game_this = -1;
		this.num_balls = 1;
		_multiplayer_game_running = 0;
		
		ips = new String[4];
		ports = new int[4];
		for (int i = 0; i< 4; i++){
			ips[i] = new String("NA");
		}		
		hand_shake_port = 9901;
	}
	
	// add or remove players with their id
	public boolean add_player(int player_no){
		if (_total_players != 4){
			this._total_players += 1;
			switch(player_no){
				case 0:
					this._is_p1_active = 1;
				case 1:
					this._is_p2_active = 1;
				case 2:
					this._is_p3_active = 1;
				case 3:
					this._is_p4_active = 1;
				}
			return true;
		}
		return false;
	}
	public boolean remove_player(int player_no){
		if (_total_players != 0){
			this._total_players -= 1;
			if (player_no == 0)
				_is_p1_active = 0;
			if (player_no == 1)
				_is_p2_active = 0;
			if (player_no == 2)
				_is_p3_active = 0;
			if (player_no == 3)
				_is_p4_active = 0;
			return true;
		}
		return false;
	}
	
	public void set_server(String server_url){this._current_server = server_url;}
	
	public void set_board_size(int board_size){this._board_size = board_size;}
	
	public void set_ball_velocity(double vx, double vy){
		this._ball_velocity_x = vx;
		this._ball_velocity_y = vy;
	}
	public void set_ball_position(int x, int y){
		this._ball_position_x = x;
		this._ball_position_y = y;
	}
	
	public void update(int ball_position_x, int ball_position_y, int paddle1, int paddle2, int paddle3, int paddle4, double ball_velocity_x, double ball_velocity_y){
		this._ball_position_x = ball_position_x;
		this._ball_position_y = ball_position_y;
		this._ball_velocity_x = ball_velocity_x;
		this._ball_velocity_y = ball_velocity_y;
		_paddles[0] = paddle1; _paddles[1] = paddle2; _paddles[2] = paddle3; _paddles[3] = paddle4;
	}
	
	public void update_paddle(int paddle_id, int paddle_pos){
		_paddles[paddle_id] = paddle_pos;
	}
	
	public void decrease_lives(int player){this.lives_left[player] -= 1;}
}
