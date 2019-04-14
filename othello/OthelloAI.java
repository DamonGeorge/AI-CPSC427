import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

/**
 * The Othello AI used in this program. 
 * This is implemented as a SwingWorker Thread that executes in the background.
 */
public class OthelloAI extends SwingWorker<Integer[], Void>{
	private OthelloGame game;
	
	/** 
	 * Simple Constructor using the current OthelloGame being played 
	 */
	public OthelloAI(OthelloGame game) {
		this.game = game;
	}
	
	/**
	 * Make a move. This is the main purpose of the AI. 
	 * Currently this just picks the first available move, if one exists
	 * @param result The resulting move's row and col indexes
	 * @return True if a move is found, false if no move is found or if the task was interrupted
	 */
	public boolean move(int[] result) {
		//sleep a random number of seconds from 1 to 15
		//return if cancelled
		Random rand = new Random();
		int n = rand.nextInt(15);
		try {
			Thread.sleep(1000 * n);
		}catch(InterruptedException e) {
			System.out.println("AI Cancelled");
			return false;
		}
		
		//find first valid move and return it
		for(int i = 0; i < OthelloGame.BOARD_SIZE; i++) {
			for(int j = 0; j < OthelloGame.BOARD_SIZE; j++) {
				if(game.isValidMove(i, j)) {
					result[0] = i;
					result[1] = j;
					return true;
				}
			}
		}
		//if we get here, no move was found
		return false;
	}
	

	/**
	 * This is the function called as background thread when this object is executed.
	 * It just returns the calculated move to the Event Dispatch Thread, if a move was found.
	 */
	@Override
	protected Integer[] doInBackground() throws Exception {
		int[] result = new int[2];
		boolean success = move(result);
		if(success)
			return Arrays.stream(result).boxed().toArray(Integer[]::new);
		else
			return null;
	}
	
	/**
	 * This is the function called in the Event Dispatch Thread when the doInBackground() function finishes.
	 * It tries to get the result of the AI thread and signals the main Othello instance of either the selection 
	 * or the failure.
	 */
	@Override
	public void done() {
		try {
			//try and get the result of the background task
			Integer[] result = get();
			
			Othello othello = Othello.getInstance();
			//if no result, signal a failure
			if(result == null) {
				othello.signalEvent(Othello.GameEvent.AI_FAIL);
			}else { //else a result was found, signal the select event to the main Othello Instace
				othello.setSelectedCell(result[0], result[1]);
				othello.signalEvent(Othello.GameEvent.SELECT);
			}
		} 
		catch (InterruptedException ignore) {} //do nothing if interrupted
	    catch (ExecutionException e) { //print execution error
	    	System.out.println("Execution Exception in AI Thread:");
	    	e.printStackTrace();
	    }
		catch(CancellationException e){} //do nothing if the ai thread was cancelled
	}
}
