import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.net.*;

public class udp_client extends Thread{
	 private InetAddress ipAddress;
	 private DatagramSocket socket;
	 public Board _game;
	 public int client_port;
	 
	 public udp_client(Board game, String ipAddress, int port){
		 this._game = game;
		 try{
			 this.socket = new DatagramSocket();
			 this.ipAddress = InetAddress.getByName(ipAddress);
		 } catch (SocketException e){
			 e.printStackTrace();
		 } catch (UnknownHostException e){
			 e.printStackTrace();
		 }
		 this.client_port = port;
	 }

	public void run(){
		while(true){
			byte[] data = new byte[256];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try{
				if (this.client_port != _game.globe.hand_shake_port){
					this.sendData(("player, " + _game.local_player
							+ ", position, " + this._game.globe._paddles[_game.local_player - 1]
							+ ", ball_x, " + this._game.globe._ball_position_x
							+ ", ball_y, " + this._game.globe._ball_position_y
							+ ", ball_velocity_x, " + this._game.globe._ball_velocity_x
							+ ", ball_velocity_y, " + this._game.globe._ball_velocity_y).getBytes());
					Thread.sleep(20);
					//socket.receive(packet);
				}
				else{
					this.sendData(_game.globe._players_names[_game.local_player - 1].getBytes());
					Thread.sleep(2000);
					socket.receive(packet);
				}
			} catch (IOException | InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, client_port);
		try{
			socket.send(packet);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}

/********************** Client Server ************************/
/*public class udp_client extends Thread{
	 private InetAddress ipAddress;
	 private DatagramSocket socket;
	 public Board _game;
	 public int client_port;
	 
	 public udp_client(Board game, String ipAddress, int port){
		 this._game = game;
		 try{
			 this.socket = new DatagramSocket();
			 this.ipAddress = InetAddress.getByName(ipAddress);
		 } catch (SocketException e){
			 e.printStackTrace();
		 } catch (UnknownHostException e){
			 e.printStackTrace();
		 }
		 this.client_port = port;
	 }

	public void run(){
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try{
				if (this.client_port != _game.globe.hand_shake_port)
					socket.receive(packet);
				else{
					this.sendData(_game.globe._players_names[_game.local_player - 1].getBytes());
					Thread.sleep(2000);
					socket.receive(packet);
				}
				Thread.sleep(10);
			} catch (IOException | InterruptedException e){
				e.printStackTrace();
			}
			
			System.out.println("SERVER > " + new String(packet.getData()).trim() + " Server IP : " + packet.getAddress() + " Server Port : " + packet.getPort());
			
			if (this.client_port != _game.globe.hand_shake_port){
				String game_state_rx = (new String(packet.getData()).trim());
				String[] game_state_rx_ar = game_state_rx.split(", ");
				this._game.globe._ball_position_x = Integer.parseInt(game_state_rx_ar[1]);
				this._game.globe._ball_position_y = Integer.parseInt(game_state_rx_ar[3]);
				if (_game.local_player != 1)
					this._game.globe._paddles[0] = Integer.parseInt(game_state_rx_ar[5]);
				if (_game.local_player != 2)	
					this._game.globe._paddles[1] = Integer.parseInt(game_state_rx_ar[7]);
				if (_game.local_player != 3)	
					this._game.globe._paddles[2] = Integer.parseInt(game_state_rx_ar[9]);
				if (_game.local_player != 4)	
					this._game.globe._paddles[3] = Integer.parseInt(game_state_rx_ar[11]);
			}
			else{
				String game_state_rx = (new String(packet.getData()).trim());
				String[] game_state_rx_ar = game_state_rx.split(", ");
				
				this._game.globe.ips[0] = game_state_rx_ar[1].trim();
				this._game.globe.ips[1] = game_state_rx_ar[3].trim();
				this._game.globe.ips[2] = game_state_rx_ar[5].trim();
				this._game.globe.ips[3] = game_state_rx_ar[7].trim();
				
				int mptrigger = Integer.parseInt(game_state_rx_ar[11].trim());
				if (mptrigger == 1){
					this._game.start_multiplayer_game(1);
				}
			}
		}
	}
	
	public void sendData(byte[] data){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, client_port);
		try{
			socket.send(packet);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}*/
////Source:http://www.binarytides.com/udp-socket-programming-in-java/