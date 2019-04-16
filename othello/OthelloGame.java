
/**
 * The Othello game class, holding functionality regarding playing a game of Othello,
 * such as making and confirming/canceling moves, generating child states, 
 * and getting the current state and score of the game. 
 */
public class OthelloGame {
	//***************************************************
	//Static Variables
	//***************************************************
	public static final int BOARD_SIZE = 8;
	public static final char WHITE = 'W';
	public static final char BLACK = 'B';
	public static final char EMPTY = 0;
	
	//***************************************************
	//Instance Variables 
	//***************************************************
	private char[][] board;
	private int moveNumber;
	private int whiteScore;
	private int blackScore;
	private char[][] pendingBoard;
	private boolean movePending;

	//***************************************************
	//Instance Functions
	//***************************************************
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
	 * Check if the given row and column are a valid move for the current player
	 * @return true if move is valid
	 */
	public boolean isValidMove(int row, int col) {
		return isValidMove(this.board, row, col, isBlacksMove());
	}

	/**
	 * Check if any valid move exists on the board for the current player
	 * @return true if any move exists, false if no move exists
	 */
	public boolean isAnyValidMoveAvailable() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(isValidMove(i, j))
					return true;
			}
		}
		return false;
	}

	//***************************************************
	//Static Utility Functions 
	//***************************************************
	/**
	 * Utility to copy a board from the src to dest
	 */
	public static void copyBoard(char[][] src, char[][] dest) {
		for(int i = 0; i < src.length; i++) {
			System.arraycopy(src[i], 0, dest[i], 0, src[i].length);
		}
	}
	
	/**
	 * Check if the provided row and col fit inside the board's dimensions
	 */
	public static boolean isValidPosition(int row, int col) {
		if(row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE)
			return false;
		else
			return true;
	}

	/**
	 * Check if the given board has the correct dimensions
	 */
	public static boolean isValidBoard(char[][] board) {
		return board.length == OthelloGame.BOARD_SIZE && board[0].length == OthelloGame.BOARD_SIZE;
	}
	
	/**
	 * Main utility for checking if a move is valid
	 * @param board	The othello board
	 * @param row	The row of the move to check
	 * @param col	The column of the move to check
	 * @param isBlacksMove	True if it is currently blacks move
	 * @return True if the given move is valid. 
	 */
	public static boolean isValidMove(char[][] board, int row, int col, boolean isBlacksMove) {
		if(!isValidBoard(board)) //check board dimensions
			return false;
		
		if(!isValidPosition(row, col)) //check if the given move fits in the board
			return false;

		if(board[row][col] != EMPTY) //check that the given space is empty
			return false;

		char currentColor = isBlacksMove ? BLACK : WHITE;
		char opposingColor = isBlacksMove ? WHITE : BLACK;

		//loop through all adjacent spaces
		for(int i = row - 1; i <= row + 1; i++) {
			for(int j = col - 1; j <= col + 1; j++) {
				//if an adjacent space has an opposing tile, it could have a move
				if(isValidPosition(i, j) && board[i][j] == opposingColor) {
					int deltaRow = i - row;
					int deltaCol = j - col;

					int currentRow = i + deltaRow;
					int currentCol = j + deltaCol;

					//loop in the direction of the adjacent space.
					//if an empty is found or the edge of the board is reached, no move is possible
					//else if a tile of the current player is found, a move is possible and return
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
		
		//if we get here, no move was found
		return false;
	}

	/**
	 * Check if any valid move exists on the board for the given player player
	 * @return true if any move exists, false if no move exists
	 */
	public static boolean isAnyValidMoveAvailable(char[][]board, boolean isBlacksMove) {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(isValidMove(board, i, j, isBlacksMove))
					return true;
			}
		}
		return false;
	}

	/**
	 * Main utility for making a move on the current board at the given position.
	 * @param currentBoard	The current board
	 * @param row	The row of the move to make
	 * @param col	The column of the move to make
	 * @param isBlacksMove	Whether it's black's move
	 * @param resultingBoard The board after the move is made. Only valid if the move is possible
	 * @return True if the move was possible.
	 */
	public static boolean move(char[][] currentBoard, int row, int col, boolean isBlacksMove, char[][] resultingBoard) {
		if(!isValidBoard(currentBoard)) //check board dimensions
			return false;
		
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
}