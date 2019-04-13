import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class OthelloAI extends SwingWorker<Integer[], Void>{
	private OthelloGame game;
	
	
	public OthelloAI(OthelloGame game) {
		this.game = game;
	}
	
	public boolean move(int[] result) {
		Random rand = new Random();
		int n = rand.nextInt(15);
		try {
			Thread.sleep(1000 * n);
		}catch(InterruptedException e) {
			System.out.println("AI Cancelled");
			return false;
		}
		
		for(int i = 0; i < OthelloGame.BOARD_SIZE; i++) {
			for(int j = 0; j < OthelloGame.BOARD_SIZE; j++) {
				if(game.isValidMove(i, j)) {
					result[0] = i;
					result[1] = j;
					return true;
				}
			}
		}
		return false;
	}
	

	@Override
	protected Integer[] doInBackground() throws Exception {
		int[] result = new int[2];
		boolean success = move(result);
		if(success)
			return Arrays.stream(result).boxed().toArray(Integer[]::new);
		else
			return null;
	}
	
	@Override
	public void done() {
		try {
			Integer[] result = get();
			
			Othello othello = Othello.getInstance();
			if(result == null) {
				othello.signalEvent(Othello.GameEvent.AI_FAIL);
			}else {
				othello.setSelectedCell(result[0], result[1]);
				othello.signalEvent(Othello.GameEvent.SELECT);
			}
		} 
		catch (InterruptedException ignore) {}
	    catch (ExecutionException e) {
	    	System.out.println("Execution Exception in AI Thread:");
	    	e.printStackTrace();
	    }
		catch(CancellationException e){} //do nothing if cancelled
	}
}
