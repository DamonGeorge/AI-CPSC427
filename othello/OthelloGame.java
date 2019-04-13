import java.util.ArrayList;

/**
 * The Othello game class, holding functionality regarding playing a game of Othello,
 * such as making and confirming/canceling moves, generating child states, 
 * and getting the current state and score of the game. 
 */
public class OthelloGame {
	public static final int BOARD_SIZE = 8;
	public static final char WHITE = 'W';
	public static final char BLACK = 'B';
	public static final char EMPTY = 0;

	private char[][] board;
	private int moveNumber;
	private int whiteScore;
	private int blackScore;

	private char[][] pendingBoard;
	private boolean movePending;

	OthelloGame(boolean mirrorInitialConfiguration) {
		moveNumber = 0;
		board = new char[BOARD_SIZE][BOARD_SIZE];
		whiteScore = 2;
		blackScore = 2;

		if(mirrorInitialConfiguration) {
			board[3][3] = BLACK;
			board[3][4] = WHITE;
			board[4][3] = WHITE;
			board[4][4] = BLACK;
		}else {
			board[3][3] = WHITE;
			board[3][4] = BLACK;
			board[4][3] = BLACK;
			board[4][4] = WHITE;
		}

		movePending = false;
		pendingBoard = new char[BOARD_SIZE][BOARD_SIZE];
	}

	/**
	 * Private utility to copy a board from the src to dest
	 */
	private void copyBoard(char[][] src, char[][] dest) {
		for(int i = 0; i < src.length; i++) {
			System.arraycopy(src[i], 0, dest[i], 0, src[i].length);
		}
	}

	/**
	 * Private utility for copying the current board to the pending Board
	 */
	private void copyCurrentToPending() {
		copyBoard(board, pendingBoard);
	}

	/**
	 * Private utility for copying the pending board to the current Board
	 */
	private void copyPendingToCurrent() {
		copyBoard(pendingBoard, board);
	}

	/**
	 * Private utility for calculating the scores on the current board
	 */
	private void updateScores() {
		blackScore = 0;
		whiteScore = 0;

		//Loop through all spaces, incrementing scores when a tile is found
		for(char[] arr : board) {
			for(char val : arr) {
				if(val == BLACK) blackScore++;
				else if (val == WHITE) whiteScore++;
			}
		}
	}

	/**
	 * Returns true if it is black's move. False if it is white's move.
	 */
	public boolean isBlacksMove() {
		return (moveNumber % 2) == 0;
	}

	/**
	 * Check if the provided row and col fit inside the board's dimensions
	 */
	public boolean isValidPosition(int row, int col) {
		if(row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE)
			return false;
		else
			return true;
	}

	//Basic getters for the white and black scores
	public int getWhiteScore() {
		return whiteScore;
	}
	public int getBlackScore() {
		return blackScore;
	}

	/**
	 * Return a copy of the current board, or the pending board if a move is pending
	 */
	public char[][] getBoard() {
		char[][] boardCopy = new char[BOARD_SIZE][BOARD_SIZE];
		if(movePending)
			copyBoard(pendingBoard, boardCopy);
		else
			copyBoard(board, boardCopy);
		
		return boardCopy;
	}

	/**
	 * Make a move at the given position using whoever's turn it currently is in the game.
	 * Returns true if a move was found. False if no move was available
	 */
	public boolean move(int row, int col) {
		boolean success = move(board, row, col, isBlacksMove(), pendingBoard);
		if(success) movePending = true;

		return success;
	}

	/**
	 * Advanced move function for making a move on the current board at the given position,
	 * using either black or white depending on the param isBlacksMove. 
	 * resultingBoard holds the board after the move is made, if a valid move was possible.
	 * Returns true if a valid move was possible. 
	 */
	public boolean move(char[][] currentBoard, int row, int col, boolean isBlacksMove, char[][] resultingBoard) {
		if(!isValidPosition(row, col)) //check if the position is valid
			return false;

		if(currentBoard[row][col] != EMPTY) //check if the position is empty
			return false;

		//get the current teams
		char currentColor = isBlacksMove ? BLACK : WHITE;
		char opposingColor = isBlacksMove ? WHITE : BLACK;

		boolean moveFound = false;

		//copy the current board to the result
		copyBoard(currentBoard, resultingBoard);

		//loop through all spaces adjacent to the given position
		for(int i = row - 1; i <= row + 1; i++) {
			for(int j = col - 1; j <= col + 1; j++) {
				//if the surrounding space is valid and is of the opposite color, 
				//check for a possible move
				if(isValidPosition(i, j) && currentBoard[i][j] == opposingColor) {
					int deltaRow = i - row;
					int deltaCol = j - col;

					int currentRow = i + deltaRow;
					int currentCol = j + deltaCol;

					boolean flip = false;

					//loop checking for a possible move in the current direction
					//away from the given position.
					//If the position if off the board or empty, no move is possible from this angle.
					//Else if we see the opposing color until we see the current color, a move is possible.
					while(isValidPosition(currentRow, currentCol)) {
						if(currentBoard[currentRow][currentCol] == EMPTY) {
							break;
						}else if(currentBoard[currentRow][currentCol] == currentColor) {
							flip =  true;
							break;
						} else {
							currentRow += deltaRow;
							currentCol += deltaCol;
						}
					}

					//if a move is possible, set the flag and flip all the spaces we traversed above.
					if(flip) {
						if(!moveFound) moveFound = true;

						currentRow -= deltaRow;
						currentCol -= deltaCol;
						while(currentRow != row || currentCol != col) {
							resultingBoard[currentRow][currentCol] = currentColor;
							currentRow -= deltaRow;
							currentCol -= deltaCol;
						}
					}
				}
			}
		}
		//add tile to the space we moved on
		if(moveFound) resultingBoard[row][col] = currentColor;
		
		return moveFound;
	}

	/**
	 * Skip a move. Simply moves to the next player
	 */
	public void skipMove() {
		moveNumber++;
	}
	
	/**
	 * Confirm a previously calculated move. This is irreversible.
	 */
	public void confirmMove() {
		if(movePending) {
			copyPendingToCurrent();
			updateScores();
			moveNumber++;
		}
	}

	/**
	 * Cancels a previously calculated move.
	 */
	public void cancelMove() {
		if(movePending) movePending = false;
	}

	/**
	 * Get all the board states possible from the current board
	 */
	public ArrayList<char[][]> getChildStates(){
		return getChildStates(board,  isBlacksMove());
	}

	/**
	 * Get all the board states possible from the given board,
	 * depending on whose turn it is.
	 */
	public ArrayList<char[][]> getChildStates(char[][] board, boolean isBlacksMove) {
		ArrayList<char[][]> childStates = new ArrayList<>();

		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				char[][] newBoard = new char[BOARD_SIZE][BOARD_SIZE];
				boolean success = move(board, i, j, isBlacksMove, newBoard);

				if(success) childStates.add(newBoard);
			}
		}

		return childStates;
	}



	public boolean isAnyValidMoveAvailable() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(isValidMove(i, j))
					return true;
			}
		}
		return false;
	}



	public boolean isValidMove(int row, int col) {
		if(!isValidPosition(row, col))
			return false;

		if(board[row][col] != EMPTY)
			return false;

		char currentColor = isBlacksMove() ? BLACK : WHITE;
		char opposingColor = isBlacksMove() ? WHITE : BLACK;

		for(int i = row - 1; i <= row + 1; i++) {
			for(int j = col - 1; j <= col + 1; j++) {
				if(isValidPosition(i, j) && board[i][j] == opposingColor) {
					int deltaRow = i - row;
					int deltaCol = j - col;

					int currentRow = i + deltaRow;
					int currentCol = j + deltaCol;

					while(isValidPosition(currentRow, currentCol)) {
						if(board[currentRow][currentCol] == EMPTY) {
							break;
						}else if(board[currentRow][currentCol] == currentColor) {
							return true;
						} else {
							currentRow += deltaRow;
							currentCol += deltaCol;
						}
					}
				}
			}
		}

		return false;
	}


}