import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Paddle {
	public int paddle_player;//1,2,3,4;
	private int x, y;
	public int orientation;// 1 : horizontal; 2 ; vertical
	public int length, width, speed, _score, _lives;
	private int is_playing;
	Board _game;
	
	public Paddle(Board game){
		paddle_player = 0;
		x = -150;
		y = 0;
		length = 80; width = 15;
		this.speed = 2;
		orientation = 1;
		this._game = game;
		this._score = 0;
		this._lives = 3;
		this.is_playing = 1;
	}
	
	public Paddle(int x, int y, int player_id, int orientation, Board game){
		this.paddle_player = player_id;
		this.x = x;
		this.y = y;
		length = 80; width = 15;
		this.speed = 7;
		this.orientation = orientation;
		this._game = game;
		this._score = 0;
		this._lives = 3;
		this.is_playing = 1;
	}
	public void relocate(int position, int player_id){
		//_game.globe._paddles[player_id] = position;
		if (orientation == 1){
			x = position;
		}
		else {
			y = position;
		}
	}
	public void move(int dist, int player_id){
		// left, down(-1) or right, up(1)
		int direction = 0;
		int curr_speed = speed;
        if (dist >= 0)
        	direction = 1;
        if (dist < 0)
        	direction = -1;        
        if(Math.abs(dist) < curr_speed){
        	curr_speed = dist;
        }
		if (orientation == 1){
			if (direction == 1){
				// If the x coordinate gets greater than size of screen
				if (x + curr_speed + length <= this._game.globe._board_size - _game.globe.corner_side){
					x += curr_speed;
					_game.globe._paddles[player_id] = x;
				}
			}
			if (direction == -1){
				if (x - curr_speed >= _game.globe.corner_side){
					x -= curr_speed;
					_game.globe._paddles[player_id] = x;
				}
			}
		}
		else{
			if (direction == 1){
				// If the y coordinate gets greater than size of screen
				if (y + curr_speed + length <= this._game.globe._board_size - _game.globe.corner_side){
					y += curr_speed;
					_game.globe._paddles[player_id] = y;
				}
			}
			if (direction == -1){
				if (y - curr_speed >= _game.globe.corner_side){
					y -= curr_speed;
					_game.globe._paddles[player_id] = y;
				}
			}
		}
	}

	public void paddle_AI(String diff){
		/*********** Take the current ball position ***********/
		int ball_position_x = this._game.globe._ball_position_x;
		int ball_position_y = this._game.globe._ball_position_y;
		int board_size = this._game.B_SIDE;

		if (diff.equals("easy")){
			int dist_hit = (int) ((_game.globe.rand) * (double) _game.globe.paddle_length);
			if (this.paddle_player == 2){
				int dist_from_paddle = ball_position_x;
				int dist = ball_position_y - _game.globe._paddles[1] - dist_hit;
				if (dist_from_paddle < board_size/4){
					move(dist, 1);
				}
				else if (dist_from_paddle > 3*board_size/4){
				
				}
				else if ((ball_position_y > board_size/4) && (ball_position_y < 3*board_size/4) ){
					move(dist, 1);
				}
				else{
					
				}
			}
			if (this.paddle_player == 3){				
				int dist_from_paddle = ball_position_y;
				int dist = ball_position_x - _game.globe._paddles[2] - dist_hit;
				if (dist_from_paddle < board_size/4){
					move(dist, 2);
				}
				else if (dist_from_paddle > 3*board_size/4){
					
				}
				else if ((ball_position_x > board_size/4) && (ball_position_x < 3*board_size/4) ){
					move(dist, 2);
				}
				else{
					
				}
			}
			if (this.paddle_player == 4){	
				int dist_from_paddle = ball_position_x;
				int dist = ball_position_y - _game.globe._paddles[3] - dist_hit;
				if (dist_from_paddle < board_size/4){
				
				}
				else if (dist_from_paddle > 3*board_size/4){
					move(dist, 3);
				}
				else if ((ball_position_y > board_size/4) && (ball_position_y < 3*board_size/4) ){
					move(dist, 3);
				}
				else{
					
				}
			}
		}
		if (diff.equals("medium")){
			if (this.paddle_player == 2){
				int dist = ball_position_y - _game.globe._paddles[1] - _game.globe.paddle_length/2;
				move(dist, 1);
			}
			if (this.paddle_player == 3){
				int dist = ball_position_x - _game.globe._paddles[2] - _game.globe.paddle_length/2;
				move(dist, 2);
			}
			if (this.paddle_player == 4){
				int dist = ball_position_y - _game.globe._paddles[3] - _game.globe.paddle_length/2;
				move(dist, 3);
			}
		}
		if (diff.equals("hard")){
			if (this.paddle_player == 2){
				int dist = ball_position_y - _game.globe._paddles[1] - _game.globe.paddle_length/2;
				move(dist, 1);
			}
			if (this.paddle_player == 3){
				int dist = ball_position_x - _game.globe._paddles[2] - _game.globe.paddle_length/2;
				move(dist, 2);
			}
			if (this.paddle_player == 4){
				int dist = ball_position_y - _game.globe._paddles[3] - _game.globe.paddle_length/2;
				move(dist, 3);
			}
		}
	}
	
	// Render paddle graphics
	public void render(Graphics g){
		g.setColor(new Color(200, 220, 150, 200));
		if (orientation == 1)
			g.fillRoundRect(x, y, length, 15, 50, 80);
		if (orientation == 2)
			g.fillRoundRect(x, y, 15, length, 80, 50);
	}
	
	// returns x and y params of paddle
	public int getX(){return this.x;}
	public int getY(){return this.y;}
	//get bounds to check collisions
	public Rectangle getBounds(){
		if(this.orientation == 1)
			return new Rectangle(x, y, length, width);
		else
			return new Rectangle(x, y, width, length);
	}
}
