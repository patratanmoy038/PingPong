import javax.swing.*;

public class scoreboard extends JPanel implements Runnable{
	public Board _score_game;
	Thread score_thread;
	JLabel players_names[];
	JLabel players_scores[];
	
	public scoreboard(Board game){
		this._score_game = game;
		score_thread = new Thread(this);
		players_names = new JLabel[4];
		players_scores = new JLabel[4];
		for (int i = 0; i < 4; i++){
			players_names[i] = new JLabel();
			players_names[i].setText(game.globe._players_names[i]);
			players_scores[i] = new JLabel();
			//System.out.println("HEE");
			players_scores[i].setText(Integer.toString(game.globe.lives_left[i]));
			this.add(players_names[i]);
			this.add(players_scores[i]);
		}
	}
	
	@Override
	public void run() {
		boolean running = true;
		while (running){
			this.update_scoreboard();
			try {
				Thread.sleep(11);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update_scoreboard(){
		for (int i = 0; i < 4; i++){
			players_names[i] = new JLabel();
			players_names[i].setText(_score_game.globe._players_names[i]);
			players_scores[i] = new JLabel();
			System.out.println(_score_game.globe.lives_left[1]);
			players_scores[i].setText(Integer.toString(_score_game.globe.lives_left[i]));
			this.add(players_names[i]);
			this.add(players_scores[i]);
		}
	}
}
