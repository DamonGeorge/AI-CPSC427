
/**
 * The main program for the Othello final project of CPSC 427
 */
public class Othello {
	public static final String WINDOW_TITLE = "Othello";
	public static Othello othello;
	
	enum GameState {PLAYER_SELECTING, PLAYER_CONFIRMING, PLAYER_UNABLE_TO_MOVE, AI_WAITING, AI_SELECTING, AI_CONFIRMING, AI_UNABLE_TO_MOVE, AI_FORFEIT, GAME_OVER}
	enum GameEvent {CONFIRM, CANCEL, START, CONTINUE, SELECT, AI_FAIL, TIMER_EXPIRED}
	
	public boolean isPlayerBlack;
	public OthelloGame othelloGame;
	public MainWindow window;
	private GameState currentState;
	private AITimer timer;
	private OthelloAI ai;
	
	private int selectedRow, selectedCol;
	
	public static Othello getInstance() {
		if(othello == null)
			othello = new Othello();
		return othello;
	}
	
	public void init() {
		StartDialog dialog = new StartDialog(WINDOW_TITLE);
		dialog.setVisible(true);
		
		String command;
		boolean done = false;
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
		
		othelloGame = new OthelloGame(!dialog.useDefaultBoard());
		
		if(isPlayerBlack) 
			currentState = GameState.PLAYER_SELECTING;
		else
			currentState = GameState.AI_WAITING;
		
		dialog.setVisible(false);
		System.out.println("Done Initializing!");
	}
	
	public void initGUI() {
		window = new MainWindow(WINDOW_TITLE);
		window.updateState(currentState);
		window.setVisible(true);
	}
	

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");	
		othello = getInstance();
		othello.init();
		othello.initGUI();
		
	}

	private void changeState(GameState newState) {
		currentState = newState;
		window.updateState(currentState);
	}
	
	public void signalEvent(GameEvent event) {
		switch(currentState) {
		case PLAYER_SELECTING:
			if(event == GameEvent.SELECT) {
				boolean success = othelloGame.move(selectedRow, selectedCol);
				if(success) {
					changeState(GameState.PLAYER_CONFIRMING);
				}
			}
			break;
		case AI_SELECTING:
			if(event == GameEvent.SELECT) {
				boolean success = othelloGame.move(selectedRow, selectedCol);
				if(success) {
					timer.cancel(true); //cancel timer if running
					changeState(GameState.AI_CONFIRMING);
				}
			}else if (event == GameEvent.AI_FAIL) {
				changeState(GameState.AI_UNABLE_TO_MOVE);
			}else if (event == GameEvent.TIMER_EXPIRED) {
				ai.cancel(true);
				changeState(GameState.AI_FORFEIT);
			}
 			break;
		case PLAYER_CONFIRMING:
			if(event == GameEvent.CONFIRM) {
				othelloGame.confirmMove();
				
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
			if(event == GameEvent.CONFIRM) {
				othelloGame.confirmMove();
				
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
			if(event == GameEvent.CONTINUE) {
				othelloGame.skipMove();
				if(othelloGame.isAnyValidMoveAvailable())
					changeState(GameState.PLAYER_SELECTING);
				else
					changeState(GameState.GAME_OVER);
			}
			break; 
		case PLAYER_UNABLE_TO_MOVE:
			if(event == GameEvent.CONTINUE) {
				othelloGame.skipMove();
				if(othelloGame.isAnyValidMoveAvailable())
					changeState(GameState.AI_WAITING);
				else
					changeState(GameState.GAME_OVER);
			}
			break;
		case AI_WAITING:
			if(event == GameEvent.START) {
				changeState(GameState.AI_SELECTING);
				//TODO: call ai and timer
				timer = new AITimer();
				ai = new OthelloAI(othelloGame);
				timer.execute();
				ai.execute();
			}
			break;
		default:
			break;
		}
	}
	
	
	
	
	public OthelloGame getGame() {
		return othelloGame;
	}
	
	public boolean isPlayersTurn() {
		return isPlayerBlack && othelloGame.isBlacksMove();
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

}
