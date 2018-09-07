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

public class Start_Menu extends JFrame implements ActionListener {
	
	public global_vars overall_globals;
	
	JLabel statusbar = new JLabel(" 0");
	private Container cont;
	private JFrame _start_menu;
	private JPanel _local_panel;
	private JButton startButton;
	private JButton startButton1;
	public JButton set_server_ip;
	private JButton optionsButton;
	private JButton exitButton;
	
	public Start_Menu(){
		int B_SIDE = 700;
		overall_globals = new global_vars(B_SIDE, 1, "192.168.189.1", B_SIDE/2 - 5, B_SIDE/2 -5, B_SIDE/2-40, B_SIDE/2-40, B_SIDE/2 - 40, B_SIDE/2-40, 0, 2);
		_local_panel = new JPanel();
		update_panel();
		_start_menu = new JFrame();
		_start_menu.setLocation(400, 200);
		_start_menu.setSize(400,400);
        _start_menu.add(_local_panel);
        _start_menu.setVisible(true);
        _start_menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //_start_menu.repaint();
	}
	
	
	public void update_panel(){
		_local_panel.setLayout(new GridLayout(5, 2, 500, 5));
		startButton = new JButton("SINGLE PLAYER");
		startButton1 = new JButton("MULTIPLAYER");
		set_server_ip = new JButton("SET SERVER IP");
		optionsButton = new JButton("OPTIONS");
		exitButton = new JButton("EXIT");
		_local_panel.add(startButton);
        _local_panel.add(startButton1);
        _local_panel.add(set_server_ip);
        _local_panel.add(optionsButton);
        _local_panel.add(exitButton);
        startButton.addActionListener(this);
        exitButton.addActionListener(this);
        startButton1.addActionListener(this);
        optionsButton.addActionListener(this);
        set_server_ip.addActionListener(this);
        
        //String image_location = JOptionPane.showInputDialog("Please input image directory");
        
	}

	public void actionPerformed(ActionEvent arg0) {
//		int id =.getKeyCode();
		Object a = arg0.getSource();
		if(a == startButton){
				this._start_menu.dispose();
				JFrame frame = new JFrame();
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setLayout(new BorderLayout());
		        
		        Board pong_board = null;
				try {
					pong_board = new Board(this.overall_globals._local_player, this.overall_globals);
					frame.add(pong_board, BorderLayout.CENTER);
					//pong_board.start();
				} catch (IOException e) {
					e.printStackTrace();
				}

		        frame.setSize(pong_board.B_SIDE + 17, pong_board.B_SIDE + 40);
		        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		        //frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		        frame.setVisible(true);
		}if(a == exitButton){
			System.exit(0);
		}
		if (a == startButton1){
			new Multiplayer(this.overall_globals);
			
		}if(a == optionsButton){
			new Options(this.overall_globals);this.dispose();
		}
		if (a == set_server_ip){
			String inputValue = JOptionPane.showInputDialog("Please input Server IP");
			this.overall_globals.set_server(inputValue.trim());
			String input_port = JOptionPane.showInputDialog("Please input your Server port");
			this.overall_globals.hand_shake_port = Integer.parseInt(input_port.trim());
		}		
	}
	
	public static void main(String args[]) {
		new Start_Menu();
	}
}