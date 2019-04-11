
/**
 * The main program for the Othello final project of CPSC 427
 */
public class Othello {
	public static final String WINDOW_TITLE = "Othello";
	
	private boolean isPlayerBlack;
	private OthelloGame othello;
	
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
		othello = new OthelloGame(!dialog.useDefaultBoard());
		dialog.setVisible(false);
		System.out.println("Done Initializing!");
	}
	
	public void run() {
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("sun.java2d.opengl", "true");		
		Othello game = new Othello();
		game.init();
		game.run();
		
	}

}
