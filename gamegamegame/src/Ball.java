import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import sun.audio.*;
import javax.sound.sampled.*;

public class Ball extends Thread {

	private Board _game;
	private double speed;
	private int ball_x, ball_y, ball_radius;
	public double velocity_x, velocity_y, omega;
	
	public void run(){
		while(true){
			try {
				this.move_ball(_game.globe);
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Ball(Board game){
		ball_x = game.B_SIDE/2 - 5;
		ball_y = game.B_SIDE/2 - 5;
		ball_radius = 12;
		speed = 6;
		velocity_x = 0;
		velocity_y = 2;
		this._game = game;
	}
	
	public Ball(int x, int y, double vx, double vy, global_vars globe, Board game){
		ball_x = x;
		ball_y = y;
		ball_radius = 15;
		speed = 6;//Math.sqrt(vx*vx + vy*vy);
		velocity_x = vx;
		velocity_y = vy;
		this._game = game;
	}
	
	public void render(Graphics g){
		g.setColor(new Color(220, 110, 100));
		g.fillOval(this.ball_x, this.ball_y, ball_radius, ball_radius);
	}
	
	public void move_ball(global_vars ball_globe){
		//move the coordinates of the ball
		this.ball_x += this.velocity_x;
		this.ball_y += this.velocity_y;
		
		//Check for collisions
		this.check_wall_collision();
		this.check_corner_collision();
		this.check_paddle_collision();
		
		//Update global vars
		this._game.globe.set_ball_position(this.ball_x, this.ball_y);
		this._game.globe.set_ball_velocity(this.velocity_x, this.velocity_y);
	}
	
	public void check_corner_collision(){
		int corner_side = this._game.globe.corner_side;
		if (ball_x >= this._game.globe._board_size - ball_radius - 2 - corner_side && ball_y <= corner_side + 3)
		{
			this.ball_x = this._game.globe._board_size - ball_radius - 2 - corner_side + 1;
			this.ball_y = corner_side + 3 + 1;			
			this.velocity_x = -speed*Math.cos(Math.toRadians(30));
			this.velocity_y = speed*Math.sin(Math.toRadians(30));
		}
		if (ball_x >= this._game.globe._board_size - ball_radius - 2 - corner_side && ball_y >= this._game.globe._board_size - ball_radius - 2 - corner_side)
		{
			this.ball_x = this._game.globe._board_size - ball_radius - 2 - corner_side + 1;
			this.ball_y = this._game.globe._board_size - ball_radius - 2 - corner_side + 1;			
			this.velocity_x = -speed*Math.cos(Math.toRadians(30));
			this.velocity_y = -speed*Math.sin(Math.toRadians(30));
		}
		if (ball_x <= corner_side + 3 && ball_y <= corner_side + 3)
		{
			this.ball_x = corner_side + 3 + 1;
			this.ball_y = corner_side + 3 + 1;		
			this.velocity_x = speed*Math.cos(Math.toRadians(30));
			this.velocity_y = speed*Math.sin(Math.toRadians(30));
		}
		if (ball_x <= corner_side + 3 && ball_y >= this._game.globe._board_size - ball_radius - 2 - corner_side)
		{
			this.ball_x = corner_side + 3 + 1;
			this.ball_y = this._game.globe._board_size - ball_radius - 2 - corner_side;	
			this.velocity_x = speed*Math.cos(Math.toRadians(30));
			this.velocity_y = speed*Math.sin(Math.toRadians(30));
		}
	}
	
	public void check_wall_collision(){
		//Wall collision rebound
			if (ball_x >= this._game.globe._board_size - 12 || ball_x <= 0)
			{
				this.velocity_x *= -1;
				_game.globe.set_ball_velocity(velocity_x, velocity_y);			
				if (ball_x <= 0){
					int delta = 0 - ball_x;
					this.ball_x += delta + 1;
					if (this._game.globe.lives_left[1] > 0)
						this._game.globe.decrease_lives(1);
				}
				else{
					int delta = ball_x - _game.globe._board_size + ball_radius + 2;
					this.ball_x -= delta + 1;
					if (this._game.globe.lives_left[3] > 0)
						this._game.globe.decrease_lives(3);
				}
				_game.globe.rand = Math.random();
			}
			if (ball_y >= this._game.globe._board_size - 12 || ball_y <= 0)
			{
				this.velocity_y *= -1;
				_game.globe.set_ball_velocity(velocity_x, velocity_y);
				if (ball_y <= 0){
					int delta = 0 - ball_y;
					this.ball_y += delta + 1;
					if (this._game.globe.lives_left[2] > 0)
						this._game.globe.decrease_lives(2);
				}
				else{
					int delta = ball_y - _game.globe._board_size + ball_radius + 2;
					this.ball_y -= delta + 1;
					if (this._game.globe.lives_left[0] > 0)
						this._game.globe.decrease_lives(0);
				}
				_game.globe.rand = Math.random();
			}
	}
	
	public int check_paddle_collision(){
		if (this._game.paddles[0].getBounds().intersects(this.getBounds()) && this._game._show_paddles[0] == 1){
			int l = (ball_x - _game.globe._paddles[0] - _game.globe.paddle_length/2 + 5);
			this.hit(1, l, 0);
			_game.globe.rand = Math.random();
			return 1;
		}
		if (this._game.paddles[1].getBounds().intersects(this.getBounds()) && this._game._show_paddles[1] == 1){
			int l = (ball_y + ball_radius/2 - this._game.globe._paddles[1] - _game.globe.paddle_length/2 + 5);
			this.ball_x = _game.paddles[1].getX() + _game.globe.paddle_thickness + ball_radius -10;
			this.hit(2, l, 0);
			_game.globe.rand = Math.random();
			return 2;
		}
		if (this._game.paddles[2].getBounds().intersects(this.getBounds()) && this._game._show_paddles[2] == 1){
			int l = -1*(ball_x + ball_radius/2 - _game.globe._paddles[2] - (_game.globe.paddle_length/2) + 5);
			this.ball_y = _game.paddles[2].getY() + _game.globe.paddle_thickness + ball_radius -7;
			this.hit(3, l, 0);
			_game.globe.rand = Math.random();
			return 3;
		}
		if (this._game.paddles[3].getBounds().intersects(this.getBounds()) && this._game._show_paddles[3] == 1){
			int l = -1*(ball_y + ball_radius/2 - _game.globe._paddles[3] - _game.globe.paddle_length/2 - 5);
			this.ball_x = _game.paddles[3].getX() - ball_radius - _game.globe.paddle_thickness + 1;
			this.hit(4, l, 0);
			_game.globe.rand = Math.random();
			return 4;
		}
		return 0;
	}
	
	/************ Change the trajectory of ball after being hit at distance l from center of a paddle ************/
	// type:: 1: paddle, 2: wall
	public void hit(int player_id, int l, int type){
		int direction = 1;
		if (l < 0)
			direction = -1;
		if (l >= 0)
			direction = 1;
		//reflection angles
		//double theta_r = (Math.PI)*(0.5- (((double)(l*direction))/_game.globe.paddle_length) - (direction*0.02));
		//double theta_r = (Math.PI/2)*Math.cos((l*Math.PI)/(_game.globe.paddle_length));
		if (l >= 40 || l <= -40)
			l = direction*39;
		double theta_r = Math.acos((double) (direction*l*1.8)/(_game.globe.paddle_length));
		if (player_id == 1){
			this.velocity_x = direction*this.speed*(Math.cos(theta_r));
			this.velocity_y = -1*this.speed*(Math.sin(theta_r));
		}
		else if (player_id == 2){
			this.velocity_y = direction*this.speed*(Math.cos(theta_r));
			this.velocity_x = this.speed*(Math.sin(theta_r));
		}
		else if (player_id == 3){
			this.velocity_x = -1*direction*this.speed*(Math.cos(theta_r));
			this.velocity_y = this.speed*(Math.sin(theta_r));
		}
		else if (player_id == 4){
			this.velocity_y = -1*direction*this.speed*(Math.cos(theta_r));
			this.velocity_x = -1*this.speed*(Math.sin(theta_r));	
		}
		
		// increase the number of hits by 1
		this._game._number_of_hits += 1;
		if (_game._number_of_hits%20 == 0 && speed < 10){
			speed += 0.5;
		}
	}
	
	public Rectangle getBounds(){return new Rectangle(this.ball_x, this.ball_y, ball_radius, ball_radius);}
	
	public void relocate(int x, int y){
		this.ball_x = x;
		this.ball_y = y;
	}
}
