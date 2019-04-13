import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BoardPanel extends JPanel{

	private BoardCell[][] board;
	
	public BoardPanel() {
		// initialize settings for this panel
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(MainWindow.WIDTH - ControlPanel.WIDTH, MainWindow.HEIGHT));
		setLayout(new GridBagLayout());
		
		createBoardLabels();
		
		createBoard();		
	}
	
	private void createBoardLabels() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		
		JLabel[] topLabels = new JLabel[OthelloGame.BOARD_SIZE];
		JLabel[] leftLabels = new JLabel[OthelloGame.BOARD_SIZE];
		
		for(int i = 0; i < OthelloGame.BOARD_SIZE; i++) {
			c.gridx = i+1;
			c.gridy = 0;
			topLabels[i] = new JLabel(String.valueOf((char) ('A' + i)));
//			topLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			topLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			topLabels[i].setVerticalAlignment(SwingConstants.BOTTOM);
			topLabels[i].setPreferredSize(new Dimension(40, 20));
			add(topLabels[i], c);
			
			c.gridx = 0;
			c.gridy = i+1;
			leftLabels[i] = new JLabel(String.valueOf(i+1));
//			leftLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
			leftLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
			leftLabels[i].setVerticalAlignment(SwingConstants.CENTER);
			leftLabels[i].setPreferredSize(new Dimension(20, 40));
			add(leftLabels[i], c);
			
		}
	}
	
	private void createBoard() {
		JPanel boardContainer = new JPanel();
		boardContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		boardContainer.setBackground(Color.LIGHT_GRAY);
		boardContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		board = new BoardCell[OthelloGame.BOARD_SIZE][OthelloGame.BOARD_SIZE];
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				c.gridx = j;
				c.gridy = i;
				board[i][j] = new BoardCell(i, j);
				boardContainer.add(board[i][j], c);
			}
		}
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 8;
		c.gridwidth = 8;
		c.fill = GridBagConstraints.BOTH;
		add(boardContainer, c);
	}
	
	
	public void updateState(Othello.GameState newState) {
		switch(newState) {
		case PLAYER_SELECTING:
			updateBoard(Othello.getInstance().getGame(), true, false);
			break;
		case PLAYER_CONFIRMING:
			updateBoard(Othello.getInstance().getGame(), false, true);
			break;
		case AI_CONFIRMING:
			updateBoard(Othello.getInstance().getGame(), false, true);
			break;
		default:
			updateBoard(Othello.getInstance().getGame(), false, false);
			break;
		}
		
		revalidate();
		repaint();
	}
	
	
	public void updateBoard(OthelloGame game, boolean isSelectable, boolean highlightChanges) {
		char[][] board = game.getBoard();
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				this.board[i][j].setHighlight(highlightChanges && this.board[i][j].getValue() != board[i][j]);
				this.board[i][j].setValue(board[i][j]);
				this.board[i][j].setIsSelectable(isSelectable && game.isValidMove(i, j));
			}
		}
	}
}
