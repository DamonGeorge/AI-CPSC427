import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;


/**
 * The Dialog used to initalize the program and start a game of Othello
 */
@SuppressWarnings("serial")
public class StartDialog extends JFrame implements ActionListener{

	public static final String BLACK_BUTTON_TITLE = "Play as Black";
	public static final String WHITE_BUTTON_TITLE = "Play as White";
	
	public static final String DEFAULT_BOARD_LABEL = "<html>WB<br/>BW</html>";
	public static final String MIRRORED_BOARD_LABEL = "<html>BW<br/>WB</html>";

	
	private String action; //string to hold which button was clicked
	private JRadioButton defaultBoardButton;

	StartDialog(String title) {
		super(title);

		JPanel mainContainer = new JPanel();
		mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
		mainContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainContainer.setBorder(new EmptyBorder(new Insets(5,0,10,10)));
		
		JLabel startingBoardLabel = new JLabel("The starting board:");
		startingBoardLabel.setBorder(new EmptyBorder(new Insets(0,12,0,0)));//trying to align all items
		//startingBoardLabel.setAlignmentX(LEFT_ALIGNMENT); 
		mainContainer.add(startingBoardLabel);
		
		defaultBoardButton = new JRadioButton(DEFAULT_BOARD_LABEL);
	    JRadioButton mirroredBoardButton = new JRadioButton(MIRRORED_BOARD_LABEL);
		defaultBoardButton.setSelected(true);

	    ButtonGroup boardGroup = new ButtonGroup();
	    boardGroup.add(defaultBoardButton);
	    boardGroup.add(mirroredBoardButton);		
		
	    JPanel radioContainer = new JPanel();
	    radioContainer.setAlignmentX(LEFT_ALIGNMENT);
	    
	    radioContainer.add(defaultBoardButton);
	    radioContainer.add(mirroredBoardButton);
	    
	    
		mainContainer.add(radioContainer);
		
		JPanel buttonContainer = new JPanel();
		buttonContainer.setAlignmentX(LEFT_ALIGNMENT);
		
		JButton startAsWhiteButton = new JButton(WHITE_BUTTON_TITLE);
		JButton startAsBlackButton = new JButton(BLACK_BUTTON_TITLE);
		startAsWhiteButton.addActionListener(this);
		startAsBlackButton.addActionListener(this);
		
		buttonContainer.add(startAsBlackButton);
		buttonContainer.add(startAsWhiteButton);
		
		mainContainer.add(buttonContainer);
		
			
		
		//finish up 
		add(mainContainer);
		
		setResizable(false);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);	
		pack();
	}

	/**
	 * This is the action listener. This just sets which button was clicked
	 * so that the main thread can check and proceed with the serial initialization 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.action = e.getActionCommand();
	}
	
	public String getAction() {
		return this.action;
	}
	
	
	public boolean useDefaultBoard() {
		return defaultBoardButton.isSelected();
	}
}