
/**
 * The main program for the Othello final project of CPSC 427
 */
public class Othello {
	public static final String WINDOW_TITLE = "Othello";
	public static Othello othello;
	
	enum GameState {PLAYER_SELECTING, PLAYER_CONFIRMING, PLAYER_LOSS, AI_WAITING, AI_SELECTING, AI_CONFIRMING, AI_LOSS}
	
	public boolean isPlayerBlack;
	public OthelloGame othelloGame;
	public MainWindow window;
	private GameState currentState;
	
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
		window.setVisible(true);
	}
	
	public void run() {
		boolean running = true;
		try {
			while(running) {
				System.out.println("yo");
				if(isPlayersTurn()) {
					
				}else {
					 
				}
				Thread.sleep(5000);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("sun.java2d.opengl", "true");	
		othello = getInstance();
		othello.init();
		othello.initGUI();
		othello.run();
		
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

}
