import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The GUI Panel holding the othello Board.
 * This holds a table of BoardCells which have mouselisteners for hovering and clicking.
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel{
	//the table of cells
	private BoardCell[][] board;
	
	/**
	 * Create the GUI and its children
	 */
	public BoardPanel() {
		// initialize settings for this panel
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(MainWindow.WIDTH - ControlPanel.WIDTH, MainWindow.HEIGHT));
		setLayout(new GridBagLayout());
		
		createBoardLabels();
		createBoard();		
	}
	
	/**
	 * Create the Labels for the Othello Board
	 */
	private void createBoardLabels() {
		//create the layout constraints object
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		
		//arrays of labels to display
		JLabel[] topLabels = new JLabel[OthelloGame.BOARD_SIZE];
		JLabel[] leftLabels = new JLabel[OthelloGame.BOARD_SIZE];
		
		//loop through the list of labels
		for(int i = 0; i < OthelloGame.BOARD_SIZE; i++) {
			//set the coordinates in the grid for this top label
			c.gridx = i+1;
			c.gridy = 0;
			//for top label, set text to a Charactor
			//and align to the bottom of its area
			topLabels[i] = new JLabel(String.valueOf((char) ('A' + i)));
			topLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			topLabels[i].setVerticalAlignment(SwingConstants.BOTTOM);
			topLabels[i].setPreferredSize(new Dimension(40, 20));
			add(topLabels[i], c);
			
			//set coordinates for this side label
			c.gridx = 0;
			c.gridy = i+1;
			//set text to be integer
			leftLabels[i] = new JLabel(String.valueOf(i+1));
			//align to the right
			leftLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
			leftLabels[i].setVerticalAlignment(SwingConstants.CENTER);
			leftLabels[i].setPreferredSize(new Dimension(20, 40));
			add(leftLabels[i], c);
			
		}
	}
	
	/**
	 * Create the Othello Board as a 2d list of BoardCells.
	 */
	private void createBoard() {
		//Create container for the board and use GridBagLayout
		JPanel boardContainer = new JPanel();
		boardContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		boardContainer.setBackground(Color.LIGHT_GRAY);
		boardContainer.setLayout(new GridBagLayout());
		
		//create layout constraints object
		GridBagConstraints c = new GridBagConstraints();
		
		//initialize 2d list of BoardCells
		board = new BoardCell[OthelloGame.BOARD_SIZE][OthelloGame.BOARD_SIZE];
		
		//loop through 2d list
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				//set layout coordinates
				c.gridx = j;
				c.gridy = i;
				//create object and add to container
				board[i][j] = new BoardCell(i, j);
				boardContainer.add(board[i][j], c);
			}
		}
		
		//edit constraints for the board container
		//and add boardContainer to the BoardPanel (this)
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 8;
		c.gridwidth = 8;
		c.fill = GridBagConstraints.BOTH;
		add(boardContainer, c);
	}
	
	/**
	 * Update the BoardCells in the GUI from the given game object
	 * @param game The current game object
	 * @param isSelectable True if the user should be able to select a move
	 * @param highlightChanges True if the GUI should highlight changes between the last board and this board
	 */
	private void updateBoard(OthelloGame game, boolean isSelectable, boolean highlightChanges) {
		char[][] board = game.getBoard();
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				this.board[i][j].setHighlight(highlightChanges && this.board[i][j].getValue() != board[i][j]);
				this.board[i][j].setValue(board[i][j]);
				this.board[i][j].setIsSelectable(isSelectable && game.isValidMove(i, j));
			}
		}
	}
	
	/**
	 * Update the state of the board GUI
	 * @param newState
	 */
	public void updateState(Othello.GameState newState) {
		switch(newState) {
		case PLAYER_SELECTING:
			//if selecting, update the board and allowing cells to be selected
			updateBoard(Othello.getInstance().getGame(), true, false);
			break;
		case PLAYER_CONFIRMING:
			//if confirming, highlight differences on the board
			updateBoard(Othello.getInstance().getGame(), false, true);
			break;
		case AI_CONFIRMING:
			//if confirming, highlight differences on the board
			updateBoard(Othello.getInstance().getGame(), false, true);
			break;
		default:
			//otherwise, just update the board normally
			updateBoard(Othello.getInstance().getGame(), false, false);
			break;
		}
		
		//repaint the board
		revalidate();
		repaint();
	}
}
