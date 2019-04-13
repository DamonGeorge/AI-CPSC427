import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainWindow extends JFrame{

	private BoardPanel boardPanel;
	private ControlPanel controlPanel;
	
	public MainWindow(String title) {
		super(title);
		
		boardPanel = new BoardPanel();
		controlPanel = new ControlPanel();
		
		Container rootPane = this.getContentPane();
		rootPane.add(boardPanel, BorderLayout.CENTER);
		rootPane.add(controlPanel, BorderLayout.EAST);
				
		setMinimumSize(new Dimension(500, 350));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		
	}
	
	
}
