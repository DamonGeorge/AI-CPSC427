import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The Main Window for the Othello GUI.
 * This holds the board panel and the control panel. 
 * @author damongeorge
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	public static final int WIDTH = 502, HEIGHT = 342;
	
	private BoardPanel boardPanel;
	private ControlPanel controlPanel;
	
	/**
	 * The window constructor which builds the enclosed board and control panels
	 * @param title
	 */
	public MainWindow(String title) {
		super(title);
		
		boardPanel = new BoardPanel();
		controlPanel = new ControlPanel();
		
		Container rootPane = this.getContentPane();
		rootPane.add(boardPanel, BorderLayout.CENTER);
		rootPane.add(controlPanel, BorderLayout.EAST);
				
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		
	}
	
	/**
	 * Update the state of GUI, namely update the state of the board and the control Panel
	 */
	public void updateState(Othello.GameState newState) {
		boardPanel.updateState(newState);
		controlPanel.updateState(newState);
	}
	
	//useful getter
	public ControlPanel getControlPanel() {
		return controlPanel;
	}
	
	
}
