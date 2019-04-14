import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Control Panel for the Main GUI. 
 * This holds the scoreboard, current turn information, and buttons 
 * for advancing through states of the game
 */
@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener{
	public static final int WIDTH = 160;
	
	//All the text used in the control panel
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
	
	//components that change their text
	private JLabel blackScoreLabel, whiteScoreLabel;
	private JLabel turnLabel, textLabel;
	private JLabel timerLabel;
	private JButton confirmButton, cancelButton, startButton, continueButton;
	
	//Value of the ai timer
	private int aiTimer = 10;
	
	/**
	 * Create the control panel and all its components
	 */
	public ControlPanel() {
		// initialize settings for this panel
		setPreferredSize(new Dimension(WIDTH, MainWindow.HEIGHT));
		setAlignmentY(TOP_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//create scoreboard container with bottom border
		JPanel scoreboardContainer = new JPanel();
		scoreboardContainer.setLayout(new GridLayout(0,2));
		scoreboardContainer.setMaximumSize(new Dimension(ControlPanel.WIDTH, 200));
		scoreboardContainer.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		
		//create scoreboard labels with borders in between
		JLabel blackLabel = new JLabel("Black");
		JLabel whiteLabel = new JLabel("White");
		blackLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		whiteLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
		blackLabel.setHorizontalAlignment(JLabel.CENTER);
		whiteLabel.setHorizontalAlignment(JLabel.CENTER);
		blackLabel.setPreferredSize(new Dimension(0, 30));
		scoreboardContainer.add(blackLabel);
		scoreboardContainer.add(whiteLabel);
		
		//create scoreboard labels for the actual scores
		blackScoreLabel = new JLabel("2");
		blackScoreLabel.setHorizontalAlignment(JLabel.CENTER);
		blackScoreLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		whiteScoreLabel = new JLabel("2");
		whiteScoreLabel.setHorizontalAlignment(JLabel.CENTER);
		whiteScoreLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
		scoreboardContainer.add(blackScoreLabel);
		scoreboardContainer.add(whiteScoreLabel);
		
		//Filler panel for preventing the changing visible components from moving
		JPanel filler1 = new JPanel();
		filler1.setMaximumSize(new Dimension(ControlPanel.WIDTH, 60));
		filler1.setPreferredSize(new Dimension(ControlPanel.WIDTH, 60));
		
		//Container for holding the current turn information
		JPanel moveContainer = new JPanel();
		moveContainer.setLayout(new GridLayout(0,1));
		moveContainer.setMaximumSize(new Dimension(150, 300));
		
		//Label for displaying whose turn it is
		turnLabel = new JLabel(PLAYERS_TURN_TEXT);
		turnLabel.setHorizontalAlignment(JLabel.CENTER);
		turnLabel.setPreferredSize(new Dimension(0, 35));
		turnLabel.setVerticalAlignment(JLabel.BOTTOM);
		moveContainer.add(turnLabel);
		
		//Label for displaying info for the current turn
		textLabel = new JLabel(PLAYER_MAKE_MOVE_TEXT);
		textLabel.setHorizontalAlignment(JLabel.CENTER);
		moveContainer.add(textLabel);
		
		//container for holding confirm/cancel button
		JPanel buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridBagLayout());
		buttonContainer.setMaximumSize(new Dimension(ControlPanel.WIDTH, 50));
		
		//create confirm and cancel buttons and their layout constraints
		GridBagConstraints c = new GridBagConstraints();
		confirmButton = new JButton(CONFIRM_BUTTON_TEXT);
		cancelButton = new JButton(CANCEL_BUTTON_TEXT);
		confirmButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		//add buttons to their container using the layout constraints
		c.ipadx = -5;
		c.gridx = 0;
		c.gridy = 0;
		buttonContainer.add(confirmButton, c);
		c.gridx = 1;
		buttonContainer.add(cancelButton, c);
		
		//another filler panel
		JPanel filler2 = new JPanel();

		//another button container for the start/continue buttons
		JPanel extraButtonsContainer = new JPanel();
		extraButtonsContainer.setLayout(new GridBagLayout());
		extraButtonsContainer.setMaximumSize(new Dimension(ControlPanel.WIDTH, 100));

		//create start and continue buttons, and set their sizes to prevent bugs
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
		
		//Container for holding the timer label
		JPanel timerContainer = new JPanel();
		timerContainer.setLayout(new GridLayout(0, 1));
		timerContainer.setPreferredSize(new Dimension(ControlPanel.WIDTH, 40));
		
		//create timer label
		timerLabel = new JLabel(SECONDS_REMAINING_TEXT);
		timerLabel.setHorizontalAlignment(JLabel.LEFT);
		timerLabel.setVerticalAlignment(JLabel.BOTTOM);
		timerLabel.setPreferredSize(new Dimension(ControlPanel.WIDTH, 40));
		timerContainer.add(timerLabel);
		
		
		//add all containers to this control panel
		add(scoreboardContainer);
		add(filler1);
		add(moveContainer);	
		add(buttonContainer);
		add(extraButtonsContainer);
		add(filler2);
		add(timerContainer);
	}

	/**
	 * Update the score labels in the scoreboard from the current score 
	 * in the Othello instance's game object
	 */
	private void updateScores() {
		OthelloGame game = Othello.getInstance().getGame();
		blackScoreLabel.setText(String.valueOf(game.getBlackScore()));
		whiteScoreLabel.setText(String.valueOf(game.getWhiteScore()));
	}
	
	/**
	 * Decrement the AI timer label. This is queued from the AITimer Thread.
	 */
	public void decrementAITimer() {
		if(Othello.getInstance().getCurrentGameState() == Othello.GameState.AI_SELECTING && aiTimer > 0) {
			aiTimer--;
			writeTimerLabel();
			revalidate();
			repaint();
		}
	}
	
	/**
	 * Write the timer label
	 */
	private void writeTimerLabel() {
		timerLabel.setText(SECONDS_REMAINING_TEXT + String.valueOf(aiTimer));
	}
	
	/**
	 * Update the state of the control panel 
	 */
	public void updateState(Othello.GameState newState) {
		String selectedCell;
		
		//defaults:
		confirmButton.setVisible(false);
		cancelButton.setVisible(false);
		startButton.setVisible(false);
		continueButton.setVisible(false);
		timerLabel.setVisible(false);
		
		//for each new state,
		//set the value of the turn and text labels
		//and also set and necessary buttons to be visible
		
		switch(newState) {
		case PLAYER_SELECTING:
			turnLabel.setText(PLAYERS_TURN_TEXT);
			textLabel.setText(PLAYER_MAKE_MOVE_TEXT);
			break;
		case PLAYER_CONFIRMING:
			turnLabel.setText(PLAYERS_TURN_TEXT);
			selectedCell = String.valueOf((char) ('A' + Othello.getInstance().getSelectedCol()))
					+ String.valueOf(Othello.getInstance().getSelectedRow() + 1);
			textLabel.setText(selectedCell + SELECTED_TEXT);
			confirmButton.setVisible(true);
			cancelButton.setVisible(true);
			break;
		case AI_WAITING:
			turnLabel.setText(AIS_TURN_TEXT);
			textLabel.setText("");
			startButton.setVisible(true);
			break;
		case AI_SELECTING:
			turnLabel.setText(AIS_TURN_TEXT);
			textLabel.setText(AI_SELECTING_TEXT);

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
			break;
		case AI_FORFEIT:
			turnLabel.setText(AIS_TURN_TEXT);
			textLabel.setText(AI_FORFEIT_TEXT);
			break;
		case AI_UNABLE_TO_MOVE:
			turnLabel.setText(AIS_TURN_TEXT);
			textLabel.setText(NO_MOVE_AVAILABLE_TEXT);
			continueButton.setVisible(true);
			break;
		case PLAYER_UNABLE_TO_MOVE:
			turnLabel.setText(PLAYERS_TURN_TEXT);
			textLabel.setText(NO_MOVE_AVAILABLE_TEXT);
			continueButton.setVisible(true);
			break;
		case GAME_OVER:
			turnLabel.setText(GAME_OVER_TEXT);
			textLabel.setText(NO_MOVES_LEFT_TEXT);
			break;
		}
		
		//update scores and repaint everything
		updateScores();
		revalidate();
		repaint();
	}


	/**
	 * The action listener for all button presses in this control panel.
	 * This just notifies the Othello instance of the event. 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		//signal the event to the Othello instance
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
