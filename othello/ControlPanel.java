import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ControlPanel extends JPanel{
	private int blackScore;
	private int whiteScore;
	
	public ControlPanel() {
		// initialize settings for this panel
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(150, 0));
		setLayout(new GridLayout(0,1));
		
		
		//add scoreboard
		JPanel scoreboardContainer = new JPanel();
		scoreboardContainer.setLayout(new GridLayout(0,2));
		scoreboardContainer.setPreferredSize(new Dimension(200, 100));
		scoreboardContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JLabel blackLabel = new JLabel("Black");
		blackLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreboardContainer.add(blackLabel);

		JLabel whiteLabel = new JLabel("White");
		whiteLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreboardContainer.add(whiteLabel);
		
		JLabel blackScoreLabel = new JLabel(String.valueOf(blackScore));
		blackScoreLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreboardContainer.add(blackScoreLabel);
		
		add(scoreboardContainer);
		
		JPanel playerContainer = new JPanel();
		playerContainer.setLayout(new GridLayout(0,2));
		
		JLabel playerLabel = new JLabel("Player's Turn:");
		playerContainer.add(playerLabel);
		
		add(playerContainer);
		
		JPanel filler = new JPanel();
		add(filler);
		
		
	}
}
