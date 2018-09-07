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

public class Options extends JFrame implements ActionListener {
	
	global_vars options_menu_globe;
	JLabel statusbar = new JLabel(" 0");
	private Container cont;
	private JFrame _start_menu;
	private JPanel _local_panel;
	private JButton image_directory;
	private JButton Difficulty;
	private JButton hand_shake_port;
	private JButton back_to_main_menu;
	private JButton ball_1;
	private JButton ball_2;
	public JButton set_server_ip;
	
	public Options(global_vars globe){
		this.options_menu_globe = globe;
		_local_panel = new JPanel();
		update_panel();
		_start_menu = new JFrame();
		_start_menu.setLocation(400, 200);
		_start_menu.setSize(600,400);
        _start_menu.add(_local_panel);
        _start_menu.setVisible(true);
        _start_menu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //_start_menu.repaint();
	}
	
	
	public void update_panel(){
		_local_panel.setLayout(new GridLayout(5, 2, 10, 5));
		image_directory = new JButton("Resources directory");
		Difficulty = new JButton("Difficulty");
		hand_shake_port = new JButton("Change Hand Shake Port");
		back_to_main_menu = new JButton("Back");
		ball_1 = new JButton("1 Ball");
		ball_2 = new JButton("2 Balls");
		_local_panel.add(image_directory);
        _local_panel.add(Difficulty);
        _local_panel.add(hand_shake_port);
        _local_panel.add(back_to_main_menu);
        _local_panel.add(ball_1);
        _local_panel.add(ball_2);
        ball_1.addActionListener(this);
        ball_2.addActionListener(this);
        image_directory.addActionListener(this);
        Difficulty.addActionListener(this);
        hand_shake_port.addActionListener(this);
        back_to_main_menu.addActionListener(this);
        
  	}

	public void actionPerformed(ActionEvent arg0) {
		Object a = arg0.getSource();
		if(a == image_directory){
			String image_location = JOptionPane.showInputDialog("Please input image directory");
			this.options_menu_globe.image_location = image_location;
		}if(a == Difficulty){
			//new Difficulty();
		}
		if(a == hand_shake_port){
			String new_port = JOptionPane.showInputDialog("Input hand_shake port number : ");
			this.options_menu_globe.hand_shake_port = Integer.parseInt(new_port);
		}
		if(a == back_to_main_menu){
			this.dispose();
		}
		if (a== ball_1){
			this.options_menu_globe.num_balls = 1;
		}
		if (a== ball_2){
			this.options_menu_globe.num_balls = 2;
		}
	}
}