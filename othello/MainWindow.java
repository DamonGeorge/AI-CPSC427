import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
	public static final int WIDTH = 502, HEIGHT = 342;
	
	private BoardPanel boardPanel;
	private ControlPanel controlPanel;
	
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
	
	public void updateState(Othello.GameState newState) {
		boardPanel.updateState(newState);
		controlPanel.updateState(newState);
	}
	
	public ControlPanel getControlPanel() {
		return controlPanel;
	}
	
	
}
