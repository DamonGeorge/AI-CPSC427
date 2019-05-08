import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AITimer implements ActionListener{
	
	private Timer timer;
	private int count;
	
	public AITimer() {
		count = 10;
		timer = new Timer(1000, this);
		timer.start();
	}
	
	public void cancel() {
		timer.stop();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		count -= 1;
		
		if(count > 0)
			Othello.getInstance().getMainWindow().getControlPanel().decrementAITimer();
		else {
			timer.stop();
			Othello.getInstance().signalEvent(Othello.GameEvent.TIMER_EXPIRED);
		}
			
		
	}

}
