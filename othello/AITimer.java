import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class AITimer extends SwingWorker<Void, Void> {
	
	@Override
	protected Void doInBackground() throws Exception {
		for(int i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Othello.getInstance().getMainWindow().getControlPanel().decrementAITimer();
					}
				});
			}catch(InterruptedException e) {
				//we've been stopped
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
