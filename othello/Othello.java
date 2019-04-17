
/**
 * The main program for the Othello final project of CPSC 427.
 * This class is a singleton. Call Othello.getInstance() to the get the current Othello object.
 */
public class Othello {
	public static final String WINDOW_TITLE = "Othello";
	public static Othello othello;
	
	//enums for the game state machine
	enum GameState {PLAYER_SELECTING, PLAYER_CONFIRMING, PLAYER_UNABLE_TO_MOVE, AI_WAITING, AI_SELECTING, AI_CONFIRMING, AI_UNABLE_TO_MOVE, AI_FORFEIT, GAME_OVER}
	enum GameEvent {CONFIRM, CANCEL, START, CONTINUE, SELECT, AI_FAIL, TIMER_EXPIRED}
	
	//private fields
	private boolean isPlayerBlack; //is the player black
	private OthelloGame othelloGame; //the game object
	private MainWindow window; //the main gui window
	private GameState currentState; //the currents state of the game
	private TimerThread timerThread; //holds reference to current timer task
	private AIThread aiThread; //holds reference to current ai task
	private int selectedRow, selectedCol; //the last selected row and col, either selected by player or AI
	private OthelloAI ai;
	
	/**
	 * Get the singleton object for this class
	 */
	public static Othello getInstance() {
		if(othello == null)
			othello = new Othello();
		return othello;
	}
	
	/**
	 * Initializes the Othello Game using the Start Dialog.
	 * This determines whether the player is black/white and what the starting board configuration is.
	 */
	private void init() {
		//create and show start dialog
		StartDialog dialog = new StartDialog(WINDOW_TITLE);
		dialog.setVisible(true);
		
		String command;
		boolean done = false;
		//wait for an action (button press) and respond accordingly
		try {
			while(!done) {
				command = dialog.getAction();
				
				if(command != null) {
					isPlayerBlack = command.equals(StartDialog.BLACK_BUTTON_TITLE);
					done = true;
				} else {
					Thread.sleep(100); //sleep if nothing happened
				}
			}
		}catch(Exception e) {}
		
		System.out.println("isPlayerBlack: " + isPlayerBlack);
		System.out.println("useDefaultBoard: " + dialog.useDefaultBoard());
		
		//create game object
		othelloGame = new OthelloGame(!dialog.useDefaultBoard());
		
		//create ai
		ai = new OthelloAI(othelloGame, !isPlayerBlack);
		
		//set starting game state
		if(isPlayerBlack) 
			currentState = GameState.PLAYER_SELECTING;
		else
			currentState = GameState.AI_WAITING;
		
		//remove the dialog
		dialog.setVisible(false);
		System.out.println("Done Initializing!");
	}
	
	/**
	 * Initialize the main Othello GUI by creating the window, updating its state, and making it visible.
	 */
	private void initGUI() {
		window = new MainWindow(WINDOW_TITLE);
		window.updateState(currentState);
		window.setVisible(true);
	}
	
	/**
	 * Change the game state. 
	 * This updates the current game state, and update the main window
	 * @param newState
	 */
	private void changeState(GameState newState) {
		currentState = newState;
		window.updateState(currentState);
	}
	
	/**
	 * The state machine for the game. This should be called from the Event Dispatcher Thread.
	 * Events in the ControlPanel or BoardPanel are sent to this method to update the game and the GUI.
	 */
	public void signalEvent(GameEvent event) {
		switch(currentState) {
		case PLAYER_SELECTING:
			//if player is selecting and chooses a move,
			//make the move if it is valid and move to the confirming state for the player
			if(event == GameEvent.SELECT) {
				boolean success = othelloGame.move(selectedRow, selectedCol);
				if(success) {
					changeState(GameState.PLAYER_CONFIRMING);
				}
			}
			break;
		case AI_SELECTING:
			//if the ai is selecting, either it can successfully make a move and proceed to the confirmation state,
			//or it can fail and move to the unable to move state,
			//or it can expire and move to the foreit state
			if(event == GameEvent.SELECT) {
				boolean success = othelloGame.move(selectedRow, selectedCol);
				if(success) {
					timerThread.cancel(true); //cancel timer if running
					changeState(GameState.AI_CONFIRMING);
				}
			}else if (event == GameEvent.AI_FAIL) {
				changeState(GameState.AI_UNABLE_TO_MOVE);
			}else if (event == GameEvent.TIMER_EXPIRED) {
				aiThread.cancel(true);
				changeState(GameState.AI_FORFEIT);
			}
 			break;
		case PLAYER_CONFIRMING:
			//if the confirm button is pressed, move to the ai waiting state if a move is available,
			//otherwise go to the ai unable to move state
			//if cancel, go back to player selecting state
			if(event == GameEvent.CONFIRM) {
				othelloGame.confirmMove();
				ai.move(selectedRow, selectedCol);
				
				if(othelloGame.isAnyValidMoveAvailable())
					changeState(GameState.AI_WAITING);
				else
					changeState(GameState.AI_UNABLE_TO_MOVE);
				
			}else if (event == GameEvent.CANCEL) {
				othelloGame.cancelMove();
				changeState(GameState.PLAYER_SELECTING);
			}
			break;
		case AI_CONFIRMING:
			//same as the player confirming state, except for the AIs turn
			if(event == GameEvent.CONFIRM) {
				othelloGame.confirmMove();
				ai.move(selectedRow, selectedCol);
				
				if(othelloGame.isAnyValidMoveAvailable())
					changeState(GameState.PLAYER_SELECTING);
				else
					changeState(GameState.PLAYER_UNABLE_TO_MOVE);
			}else if (event == GameEvent.CANCEL) {
				othelloGame.cancelMove();
				changeState(GameState.AI_WAITING);
			}
			break;
		case AI_UNABLE_TO_MOVE:
			//if continue is pressed, go to players turn.
			//however if the player won't have a move,
			//the game ends
			if(event == GameEvent.CONTINUE) {
				othelloGame.skipMove();
				ai.skipMove();
				
				if(othelloGame.isAnyValidMoveAvailable())
					changeState(GameState.PLAYER_SELECTING);
				else
					changeState(GameState.GAME_OVER);
			}
			break; 
		case PLAYER_UNABLE_TO_MOVE:
			//same as the ai unable to move state, except during the players turn
			if(event == GameEvent.CONTINUE) {
				othelloGame.skipMove();
				ai.skipMove();
				
				if(othelloGame.isAnyValidMoveAvailable())
					changeState(GameState.AI_WAITING);
				else
					changeState(GameState.GAME_OVER);
			}
			break;
		case AI_WAITING:
			//if the start button is pressed, start the ai and timer threads,
			//and move to the ai selecting state
			if(event == GameEvent.START) {
				changeState(GameState.AI_SELECTING);
				//TODO: call ai and timer
				timerThread = new TimerThread();
				aiThread = new AIThread(ai);
				timerThread.execute();
				aiThread.execute();
			}
			break;
		default:
			break;
		}
	}
	
	
	
	//Simple Getters and Setters
	
	public OthelloGame getGame() {
		return othelloGame;
	}
	public GameState getCurrentGameState() {
		return currentState;
	}
	public void setSelectedCell(int row, int col) {
		selectedRow = row;
		selectedCol = col;
	}
	public int getSelectedRow() {
		return selectedRow;
	}
	public int getSelectedCol() {
		return selectedCol;
	}
	public MainWindow getMainWindow() {
		return window;
	}

	
	
	/**
	 * MAIN
	 */
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");	
		othello = getInstance();
		othello.init();
		othello.initGUI();
		
	}
}
