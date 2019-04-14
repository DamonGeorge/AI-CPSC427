import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


/**
 * The Timer used to prevent the AI from exceeding 10 seconds.
 * This is implemented as a SwingWorker Thread. 
 */
public class AITimer extends SwingWorker<Void, Void> {
	
	/**
	 * This is the background thread task that is run,
	 * when this object is executed. The timer counts down from 10,
	 * updating the control panel each second. If the timer expires,
	 * it notifies the Othello instance of the expire event. 
	 */
	@Override
	protected Void doInBackground() throws Exception {
		//loop for ten seconds
		for(int i = 0; i < 10; i++) {
			try {
				//sleep for a second
				Thread.sleep(1000);

				//update control panel's timer in the Event Dispatch Thread
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Othello.getInstance().getMainWindow().getControlPanel().decrementAITimer();
					}
				});
			}catch(InterruptedException e) {
				//we've been interrupted so quit
				System.out.println("TIMER cancelled");
				return null;
			}
		}
		//if we've gotten here, issue expiration event
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Othello.getInstance().signalEvent(Othello.GameEvent.TIMER_EXPIRED);
			}
		});
		
		return null;
	}

}
