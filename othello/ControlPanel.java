import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener{
	public static final int WIDTH = 160;
	
	private static final String PLAYERS_TURN_TEXT = "<html><u><b>Player's Turn:</b></u></html>";
	private static final String AIS_TURN_TEXT = "<html><u><b>AI's Turn:</b></u></html>";
	private static final String CONFIRM_BUTTON_TEXT = "Confirm";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";
	private static final String AI_SELECTING_TEXT = "AI selecting...";
	private static final String PLAYER_MAKE_MOVE_TEXT = "Make a move...";
	private static final String SELECTED_TEXT = " Selected!";
	private static final String NO_MOVE_AVAILABLE_TEXT = "No move available!";
	private static final String CONTINUE_BUTTON_TEXT = "Continue";
	private static final String START_BUTTON_TEXT = "Start";
	private static final String AI_FORFEIT_TEXT = "<html>AI took too long!<br />Player wins!<html>";
	private static final String GAME_OVER_TEXT = "<html><u><b>Game Over!</b></u></html>";
	private static final String NO_MOVES_LEFT_TEXT = "No moves left";
	private static final String SECONDS_REMAINING_TEXT = "Seconds Left: ";
	
	private JLabel blackScoreLabel, whiteScoreLabel;
	private JLabel turnLabel, textLabel;
	private JLabel timerLabel;
	private JButton confirmButton, cancelButton, startButton, continueButton;
	private int aiTimer = 10;
	
	public ControlPanel() {
		// initialize settings for this panel
		setPreferredSize(new Dimension(WIDTH, MainWindow.HEIGHT));
		setAlignmentY(TOP_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//add scoreboard
		JPanel scoreboardContainer = new JPanel();
		scoreboardContainer.setLayout(new GridLayout(0,2));
		scoreboardContainer.setMaximumSize(new Dimension(ControlPanel.WIDTH, 200));
		scoreboardContainer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
//		scoreboardContainer.setOpaque(false);
		
		JLabel blackLabel = new JLabel("Black");
		blackLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		blackLabel.setHorizontalAlignment(JLabel.CENTER);
		blackLabel.setPreferredSize(new Dimension(0, 30));
		scoreboardContainer.add(blackLabel);

		JLabel whiteLabel = new JLabel("White");
		whiteLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
		whiteLabel.setHorizontalAlignment(JLabel.CENTER);
		scoreboardContainer.add(whiteLabel);
		
		blackScoreLabel = new JLabel("2");
		blackScoreLabel.setHorizontalAlignment(JLabel.CENTER);
		blackScoreLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		whiteScoreLabel = new JLabel("2");
		whiteScoreLabel.setHorizontalAlignment(JLabel.CENTER);
		whiteScoreLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
		scoreboardContainer.add(blackScoreLabel);
		scoreboardContainer.add(whiteScoreLabel);
		
		
		
		JPanel filler1 = new JPanel();
		filler1.setMaximumSize(new Dimension(ControlPanel.WIDTH, 60));
		filler1.setPreferredSize(new Dimension(ControlPanel.WIDTH, 60));
//		filler1.setBorder(BorderFactory.createLineBorder(Color.GREEN));
//		filler1.setOpaque(false);
		
		JPanel moveContainer = new JPanel();
		moveContainer.setLayout(new GridLayout(0,1));
		moveContainer.setMaximumSize(new Dimension(150, 300));
//		moveContainer.setOpaque(false);
//		moveContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		turnLabel = new JLabel("<html><u><b>Player's Turn:</b></u></html>");
		turnLabel.setHorizontalAlignment(JLabel.CENTER);
		turnLabel.setPreferredSize(new Dimension(0, 35));
		turnLabel.setVerticalAlignment(JLabel.BOTTOM);
		
		moveContainer.add(turnLabel);
		
		textLabel = new JLabel("Make a move");
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		moveContainer.add(textLabel);
		
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridBagLayout());
//		buttonContainer.setBorder(BorderFactory.createLineBorder(Color.PINK));
		buttonContainer.setMaximumSize(new Dimension(ControlPanel.WIDTH, 50));
		GridBagConstraints c = new GridBagConstraints();
		confirmButton = new JButton(CONFIRM_BUTTON_TEXT);
		cancelButton = new JButton(CANCEL_BUTTON_TEXT);
		confirmButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		c.ipadx = -5;
		c.gridx = 0;
		c.gridy = 0;
		buttonContainer.add(confirmButton, c);
		c.gridx = 1;
		buttonContainer.add(cancelButton, c);
		//moveContainer.add(buttonContainer);		
		
		JPanel filler2 = new JPanel();
//		filler2.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		JPanel extraButtonsContainer = new JPanel();
		extraButtonsContainer.setLayout(new GridBagLayout());
		extraButtonsContainer.setMaximumSize(new Dimension(ControlPanel.WIDTH, 100));
//		extraButtonsContainer.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		startButton = new JButton(START_BUTTON_TEXT);
		continueButton = new JButton(CONTINUE_BUTTON_TEXT);
		startButton.setPreferredSize(new Dimension(100, 30));
		continueButton.setPreferredSize(new Dimension(100, 30));
		startButton.addActionListener(this);
		continueButton.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		extraButtonsContainer.add(startButton, c);
		c.gridy = 1;
		extraButtonsContainer.add(continueButton, c);
		
		
		JPanel timerContainer = new JPanel();
//		timerContainer.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		timerContainer.setLayout(new GridLayout(0, 1));
		timerContainer.setPreferredSize(new Dimension(ControlPanel.WIDTH, 40));
		
		timerLabel = new JLabel(SECONDS_REMAINING_TEXT);
		timerLabel.setHorizontalAlignment(JLabel.LEFT);
		timerLabel.setVerticalAlignment(JLabel.BOTTOM);
		timerLabel.setPreferredSize(new Dimension(ControlPanel.WIDTH, 40));
//		timerLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		timerContainer.add(timerLabel);
		
		add(scoreboardContainer);
		add(filler1);
		add(moveContainer);	
		add(buttonContainer);
		add(extraButtonsContainer);
		add(filler2);
		add(timerContainer);
	}

	private void updateScores() {
		OthelloGame game = Othello.getInstance().getGame();
		blackScoreLabel.setText(String.valueOf(game.getBlackScore()));
		whiteScoreLabel.setText(String.valueOf(game.getWhiteScore()));
	}
	
	public void decrementAITimer() {
		if(Othello.getInstance().getCurrentGameState() == Othello.GameState.AI_SELECTING && aiTimer > 0) {
			aiTimer--;
			writeTimerLabel();
			revalidate();
			repaint();
		}
	}
	
	private void writeTimerLabel() {
		timerLabel.setText(SECONDS_REMAINING_TEXT + String.valueOf(aiTimer));
	}
	
	public void updateState(Othello.GameState newState) {
		String selectedCell;
		
		//default:
		timerLabel.setVisible(false);
		
		switch(newState) {
		case PLAYER_SELECTING:
			turnLabel.setText(PLAYERS_TURN_TEXT);
			textLabel.setText(PLAYER_MAKE_MOVE_TEXT);
			confirmButton.setVisible(false);
			cancelButton.setVisible(false);
			startButton.setVisible(false);
			continueButton.setVisible(false);
			break;
		case PLAYER_CONFIRMING:
			turnLabel.setText(PLAYERS_TURN_TEXT);
			selectedCell = String.valueOf((char) ('A' + Othello.getInstance().getSelectedCol()))
					+ String.valueOf(Othello.getInstance().getSelectedRow() + 1);
			textLabel.setText(selectedCell + SELECTED_TEXT);
			confirmButton.setVisible(true);
			cancelButton.setVisible(true);
			startButton.setVisible(false);
			continueButton.setVisible(false);
			break;
		case AI_WAITING:
			turnLabel.setText(AIS_TURN_TEXT);
			textLabel.setText("");
			confirmButton.setVisible(false);
			cancelButton.setVisible(false);
			startButton.setVisible(true);
			continueButton.setVisible(false);
			break;
		case AI_SELECTING:
			turnLabel.setText(AIS_TURN_TEXT);
			textLabel.setText(AI_SELECTING_TEXT);
			confirmButton.setVisible(false);
			cancelButton.setVisible(false);
			startButton.setVisible(false);
			continueButton.setVisible(false);
			
			//for timer
			aiTimer = 10;
			writeTimerLabel();
			timerLabel.setVisible(true);
			break;
		case AI_CONFIRMING:
			turnLabel.setText(AIS_TURN_TEXT);
			selectedCell = String.valueOf((char) ('A' + Othello.getInstance().getSelectedCol()))
					+ String.valueOf(Othello.getInstance().getSelectedRow() + 1);
			textLabel.setText(selectedCell + SELECTED_TEXT);
			confirmButton.setVisible(true);
			cancelButton.setVisible(true);
			startButton.setVisible(false);
			continueButton.setVisible(false);
			break;
		case AI_FORFEIT:
			turnLabel.setText(AIS_TURN_TEXT);
			textLabel.setText(AI_FORFEIT_TEXT);
			confirmButton.setVisible(false);
			cancelButton.setVisible(false);
			startButton.setVisible(false);
			continueButton.setVisible(false);
			break;
		case AI_UNABLE_TO_MOVE:
			turnLabel.setText(AIS_TURN_TEXT);
			textLabel.setText(NO_MOVE_AVAILABLE_TEXT);
			confirmButton.setVisible(false);
			cancelButton.setVisible(false);
			startButton.setVisible(false);
			continueButton.setVisible(true);
			break;
		case PLAYER_UNABLE_TO_MOVE:
			turnLabel.setText(PLAYERS_TURN_TEXT);
			textLabel.setText(NO_MOVE_AVAILABLE_TEXT);
			confirmButton.setVisible(false);
			cancelButton.setVisible(false);
			startButton.setVisible(false);
			continueButton.setVisible(true);
			break;
		case GAME_OVER:
			turnLabel.setText(GAME_OVER_TEXT);
			textLabel.setText(NO_MOVES_LEFT_TEXT);
			confirmButton.setVisible(false);
			cancelButton.setVisible(false);
			startButton.setVisible(false);
			continueButton.setVisible(false);
			break;
		}
		
		updateScores();
		revalidate();
		repaint();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		switch(action) {
		case CONFIRM_BUTTON_TEXT:
			Othello.getInstance().signalEvent(Othello.GameEvent.CONFIRM);
			break;
		case CANCEL_BUTTON_TEXT:
			Othello.getInstance().signalEvent(Othello.GameEvent.CANCEL);
			break;
		case CONTINUE_BUTTON_TEXT:
			Othello.getInstance().signalEvent(Othello.GameEvent.CONTINUE);
			break;
		case START_BUTTON_TEXT:
			Othello.getInstance().signalEvent(Othello.GameEvent.START);
			break;
		default:
			return;			
		}
		
		
	}
}
