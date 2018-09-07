import java.io.*;
import java.net.*;

public class udp_server_handshake extends Thread {
	 private DatagramSocket socket;
	 private global_vars globe;
	 public int _port;

	 public udp_server_handshake(global_vars globe, int port_no){
		 this.globe = globe;
		 _port = port_no;
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
			socket.receive(packet);
			
			String message = new String(packet.getData()).trim();
			if (this.globe._multiplayer_game_running != 1)
				System.out.println("CLIENT > " + message + " CLIENT IP : " + packet.getAddress() + " CLIENT PORT > " + packet.getPort());			
		} catch (IOException e){
			e.printStackTrace();
		}
		
		/************* Initial Hand Shake ***************/
		if (_port == globe.hand_shake_port){
			int id = 0;
			while (!this.globe.ips[id].equals(new String("NA")) && id < 4 && !this.globe.ips[id].equals(packet.getAddress().toString().substring(1))){
				//System.out.println(_game.globe.ips[id]);
				if (globe.ips[id].equals(new String("NA")) || id >= 4)
					break;
				id++;				
			}
			if (id < 4){
				globe.ips[id] = packet.getAddress().toString().substring(1);
				globe.add_player(id);
				globe._players_names[id] = packet.getData().toString();
				
				// Send list of all connected ips
				String game_state = new String(
						"IP1, " + globe.ips[0] + 
						", IP2, " + globe.ips[1] + 
						", IP3, " + globe.ips[2] + 
						", IP4, " + globe.ips[3] + 
						", your_id, " + id +
						", start_game, " + globe._multiplayer_game_running
				);
				
				this.sendData(game_state.getBytes(), packet.getAddress(), packet.getPort());
//				if (globe._multiplayer_game_running == 1)
//					this._mp_status_sent = 1;
			}
		}
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
}