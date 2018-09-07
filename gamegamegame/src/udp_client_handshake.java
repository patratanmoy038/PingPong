import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.net.*;

public class udp_client_handshake extends Thread{
	 private InetAddress ipAddress;
	 private DatagramSocket socket;
	 private global_vars globe;
	 public int client_port;
	 
	 private boolean trigger = false;
	 
	 public udp_client_handshake(global_vars globe, String ipAddress, int port){
		 this.globe = globe;
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
				this.sendData(this.ipAddress.getAddress().toString().getBytes());
				if (this.globe._multiplayer_game_running == 1 && trigger == false){
					//this.socket.close();
					/***************** Start JFrame ******************/
					JFrame frame = new JFrame();
			        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			        frame.setLayout(new BorderLayout());

			        Board pong_board = null;
					try {
						pong_board = new Board(this.globe._local_player, globe);
						frame.add(pong_board, BorderLayout.CENTER);
					} catch (IOException e) {
						e.printStackTrace();
					}

			        frame.setSize(pong_board.B_SIDE + 17, pong_board.B_SIDE + 40);
			        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
			        //frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
			        frame.setVisible(true);
					
					trigger = true;
				}
				Thread.sleep(200);
				socket.receive(packet);
			} catch (IOException | InterruptedException e){
				e.printStackTrace();
			}
			
			System.out.println("SERVER > " + new String(packet.getData()).trim() + " Server IP : " + packet.getAddress() + " Server Port : " + packet.getPort());
			if (packet.getData() != null){
				String game_state_rx = (new String(packet.getData()).trim());
				String[] game_state_rx_ar = game_state_rx.split(", ");
				
				this.globe.ips[0] = game_state_rx_ar[1].trim();
				this.globe.ips[1] = game_state_rx_ar[3].trim();
				this.globe.ips[2] = game_state_rx_ar[5].trim();
				this.globe.ips[3] = game_state_rx_ar[7].trim();
				this.globe._local_player = Integer.parseInt(game_state_rx_ar[9].trim()) + 1;
				
				// Trigger game start
				int mptrigger = Integer.parseInt(game_state_rx_ar[11].trim());
				if (mptrigger == 1){
					this.globe._multiplayer_game_running = 1;
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
}

////Source:http://www.binarytides.com/udp-socket-programming-in-java/