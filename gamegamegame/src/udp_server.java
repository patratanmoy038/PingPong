import java.io.*;
import java.net.*;

public class udp_server extends Thread {
	 private DatagramSocket socket;
	 private Board _game;
	 public int _port;
	 private String local_ip;
	 
	 public udp_server(Board game, int port_no){
		 this._game = game;
		 _port = port_no;
		 local_ip = new String("");
		 try{
			 this.socket = new DatagramSocket(port_no);
		 } catch (SocketException e){
			 e.printStackTrace();
		 }
	 }

public void run(){
	while(true){
		byte[] data = new byte[256];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try{
			//Thread.sleep(15);
			socket.setSoTimeout(5000);
			socket.receive(packet);
			String message = new String(packet.getData()).trim();
			local_ip = packet.getAddress().toString().trim();
			System.out.println("CLIENT > " + message + " CLIENT IP : " + packet.getAddress() + " CLIENT PORT > " + packet.getPort());			
		} catch (IOException e){
			System.out.println("Out");
			//player dropped
			_game.drop_player(this.local_ip);
			e.printStackTrace();
		}

		/****************** Parse response *********************/
		String game_state_rx = (new String(packet.getData())).trim();
		String[] game_state_rx_ar = game_state_rx.split(", ");
		int player = Integer.parseInt(game_state_rx_ar[1]);
		int position = Integer.parseInt(game_state_rx_ar[3]);
		int ballx = Integer.parseInt(game_state_rx_ar[5]);
		int bally = Integer.parseInt(game_state_rx_ar[7]);
		double ballvelx = Double.parseDouble(game_state_rx_ar[9]);
		double ballvely = Double.parseDouble(game_state_rx_ar[11]);

		//UPDATE GAME STATE
		if (this._game.local_player != player){
			this._game.globe.update_paddle(player - 1, position);
			this._game.paddles[player - 1].relocate(position, player - 1);
			if (this._game.globe._current_server.equals(packet.getAddress().toString().substring(1))){
				this._game.globe._ball_position_x = ballx;
				this._game.globe._ball_position_y = bally;
				this._game.ball.velocity_x = ballvelx;
				this._game.ball.velocity_y = ballvely;
				this._game.ball.relocate(ballx, bally);
			}
		}
	}
		
//		if (_port != _game.globe.hand_shake_port){
//			/****************** Parse response *********************/
//			String game_state_rx = (new String(packet.getData())).trim();
//			String[] game_state_rx_ar = game_state_rx.split(", ");
//			int player = Integer.parseInt(game_state_rx_ar[1]);
//			int position = Integer.parseInt(game_state_rx_ar[3]);
//	
//			//UPDATE GAME STATE
//			this._game.globe.update_paddle(player - 1, position);
//			this._game.paddles[player - 1].relocate(position, player - 1);
//		}
//		/************* Initial Hand Shake ***************/
//		if (_port == _game.globe.hand_shake_port){
//			int id = 0;
//			while (!this._game.globe.ips[id].equals(new String("NA")) && id < 4 && !this._game.globe.ips[id].equals(packet.getAddress().toString())){
//				//System.out.println(_game.globe.ips[id]);
//				if (_game.globe.ips[id].equals(new String("NA")) || id >= 4)
//					break;
//				id++;				
//			}
//			if (id < 4){
//				_game.globe.ips[id] = packet.getAddress().toString();
//				
//				_game.globe._players_names[id] = packet.getData().toString();
//				
//				// Send list of all connected ips
//				String game_state = new String(
//						"IP1, " + _game.globe.ips[0] + 
//						", IP2, " + _game.globe.ips[1] + 
//						", IP3, " + _game.globe.ips[2] + 
//						", IP4, " + _game.globe.ips[3] + 
//						", start_game, " + _game.multiplayer_game_running
//				);
//				
//				this.sendData(game_state.getBytes(), packet.getAddress(), packet.getPort());
//			}
//		}
//	}
}

public void sendData(byte[] data, InetAddress ipAddress, int port){
	DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
	try{
		socket.send(packet);
	} catch (IOException e){
		e.printStackTrace();
	}
}
}
////Source : http://www.binarytides.com/udp-socket-programming-in-java/

/*public class udp_server extends Thread {
	 private DatagramSocket socket;
	 private Board _game;
	 public int _port;
	 
	 public udp_server(Board game, int port_no){
		 this._game = game;
		 _port = port_no;
		 try{
			 this.socket = new DatagramSocket(port_no);
		 } catch (SocketException e){
			 e.printStackTrace();
		 }
	 }

public void run(){
	while(true){
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try{
			socket.receive(packet);
			
			String message = new String(packet.getData()).trim();
			System.out.println("CLIENT > " + message + " CLIENT IP : " + packet.getAddress() + " CLIENT PORT > " + packet.getPort());
			//UPDATE GAME STATE
			//this._game.globe._paddles[_game.local_player] = Integer.parseInt(message);
			
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
		if (_port != _game.globe.hand_shake_port){
			*//****************** Parse response *********************//*
			String game_state_rx = (new String(packet.getData())).trim();
			String[] game_state_rx_ar = game_state_rx.split(", ");
			int player = Integer.parseInt(game_state_rx_ar[1]);
			int position = Integer.parseInt(game_state_rx_ar[3]);
	
			//UPDATE GAME STATE
			this._game.globe.update_paddle(player - 1, position);
			this._game.paddles[player - 1].relocate(position, player - 1);
		}
		*//************* Initial Hand Shake ***************//*
		if (_port == _game.globe.hand_shake_port){
			int id = 0;
			while (!this._game.globe.ips[id].equals(new String("NA")) && id < 4 && !this._game.globe.ips[id].equals(packet.getAddress().toString())){
				//System.out.println(_game.globe.ips[id]);
				if (_game.globe.ips[id].equals(new String("NA")) || id >= 4)
					break;
				id++;				
			}
			if (id < 4){
				_game.globe.ips[id] = packet.getAddress().toString();
				
				//String 
				sendData("You are in".getBytes(), packet.getAddress(), packet.getPort());
			}
		}
		// UPDATED GAME STATE TO BE SENT
		String game_state = new String(
				"ball_x, " + this._game.globe._ball_position_x
				+ ", ball_y, " + this._game.globe._ball_position_y
				+ ", paddle_1, " + this._game.globe._paddles[0]
				+ ", paddle_2, " + this._game.globe._paddles[1]
				+ ", paddle_3, " + this._game.globe._paddles[2]
				+ ", paddle_4, " + this._game.globe._paddles[3]
		);
		
		// SEND GAME STATE
		sendData(game_state.getBytes(), packet.getAddress(), packet.getPort());
	}
}

public void receiveData(){
	byte[] data = new byte[1024];
	DatagramPacket packet = new DatagramPacket(data, data.length);
	try{
		socket.receive(packet);
		
		String message = new String(packet.getData()).trim();
		System.out.println("CLIENT > " + message + " CLIENT IP : " + packet.getAddress() + " CLIENT PORT > " + packet.getPort());
		//UPDATE GAME STATE
		//this._game.globe._paddles[_game.local_player] = Integer.parseInt(message);
		
		if (_port != _game.globe.hand_shake_port){
			*//****************** Parse response *********************//*
			String game_state_rx = (new String(packet.getData())).trim();
			String[] game_state_rx_ar = game_state_rx.split(", ");
			int player = Integer.parseInt(game_state_rx_ar[1]);
			int position = Integer.parseInt(game_state_rx_ar[3]);
	
			//UPDATE GAME STATE
			this._game.globe.update_paddle(player - 1, position);
			this._game.paddles[player - 1].relocate(player - 1, position);
			
			// UPDATED GAME STATE TO BE SENT
			String game_state = new String(
					"ball_x, " + this._game.globe._ball_position_x
					+ ", ball_y, " + this._game.globe._ball_position_y
					+ ", paddle_1, " + this._game.globe._paddles[0]
					+ ", paddle_2, " + this._game.globe._paddles[1]
					+ ", paddle_3, " + this._game.globe._paddles[2]
					+ ", paddle_4, " + this._game.globe._paddles[3]
			);
			
			this.sendData(game_state.getBytes(), packet.getAddress(), packet.getPort());
		}
		if (_port == _game.globe.hand_shake_port){
			int id = 0;
			while (this._game.globe.ips[id].equals("NA") && id < 4){
				id++;
			}
			_game.globe._players_names[id] = message;
			_game.globe.ips[id] = packet.getAddress().toString();
			
			// Send list of all connected ips
			String game_state = new String(
					"IP1, " + _game.globe.ips[0] + 
					", IP2, " + _game.globe.ips[1] + 
					", IP3, " + _game.globe.ips[2] + 
					", IP4, " + _game.globe.ips[3] + 
					", start_game, " + _game.multiplayer_game_running
			);
			
			this.sendData(game_state.getBytes(), packet.getAddress(), packet.getPort());
		}
		Thread.sleep(10);
	} catch (IOException | InterruptedException e){
		e.printStackTrace();
	}
}

public void sendData(byte[] data, InetAddress ipAddress, int port){
	DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
	try{
		socket.send(packet);
	} catch (IOException e){
		e.printStackTrace();
	}
}
}*/