import java.util.ArrayList;

public class OthelloNode {
	//Todo: handle skip moves for the row and col members
	
	//***************************************************
	//Static Variables
	//***************************************************
	private static final int STABLE = 1;
	private static final int UNSTABLE = -1;
	private static final int SEMI_STABLE = 0;
	public static final double[] HEURISTIC_WEIGHTS = {0.05, 0.15, 0.3, -0.1, 0.1, 0.3, 0.2};
	
	//***************************************************
	//Instance Variables
	//***************************************************
	private char[][] board;
	private int previousMoveRow;
	private int previousMoveCol;
	private boolean isBlacksMove;
	private ArrayList<OthelloNode> children;
	private Double heuristicValue;

	//***************************************************
	//Instance Functions
	//***************************************************
	public OthelloNode(char[][] board, int previousMoveRow, int previousMoveCol, boolean isBlacksMove) {
		this.board = board;
		this.previousMoveRow = previousMoveRow;
		this.previousMoveCol = previousMoveCol;
		this.isBlacksMove = isBlacksMove;
		this.children = null;
		this.heuristicValue = null;
	}

	//Getters
	public ArrayList<OthelloNode> getChildren() {
		if(children == null) //calculate if necessary
			children = calculateChildNodes(board, isBlacksMove);
		
		return children;
	}
	public double getHeuristicValue() {
		if(heuristicValue == null) //calculate if necessary
			calculateFullHeuristic(board, isBlacksMove);
		
		return heuristicValue;
	}
	public int getPreviousMoveRow() {
		return previousMoveRow;
	}
	public int getPreviousMoveCol() {
		return previousMoveCol;
	}
	public boolean isBlacksMove() {
		return isBlacksMove;
	}
	
	
	//***************************************************
	//Static Utility Functions 
	//***************************************************
	
	public ArrayList<OthelloNode> calculateChildNodes(char[][] board, boolean isBlacksMove) {
		ArrayList<OthelloNode> children = new ArrayList<>();
		char[][] newBoard = null;
		for(int i = 0; i < OthelloGame.BOARD_SIZE; i++) {
			for(int j = 0; j < OthelloGame.BOARD_SIZE; j++) {
				newBoard = new char[OthelloGame.BOARD_SIZE][OthelloGame.BOARD_SIZE];
				boolean success = OthelloGame.move(board, i, j, isBlacksMove, newBoard);

				if(success) {
					children.add(new OthelloNode(newBoard, i, j, !isBlacksMove));
				}
			}
		}
		
		if(children.isEmpty()) {
			OthelloGame.copyBoard(board, newBoard);
			children.add(new OthelloNode(newBoard, 0, 0, !isBlacksMove));
		}
			
		
		return children;
	}
	
	public static double calculateHeuristicValue(int blacksValue, int whitesValue, boolean isBlacksMove) {
		int diff = blacksValue - whitesValue;
		if(!isBlacksMove) diff = -diff;
		
		int sum = blacksValue + whitesValue;
		
		if(sum != 0)
			return diff/sum;
		else
			return 0.0;
	}
	
	public static double calculateFullHeuristic(char[][]board, boolean isBlacksMove) {
		//for count heuristic
		int whiteScore = 0;
		int blackScore = 0;
		
		//for actual mobility
		int numLegalMovesBlack = 0;
		int numLegalMovesWhite = 0;
		
		//for stability
		int numStableWhite = 0;
		int numUnstableWhite = 0;
		int numSemiStableWhite = 0;
		int numStableBlack = 0;
		int numUnstableBlack = 0;
		int numSemiStableBlack = 0;
		
		//for corners
		int numCornersWhite = 0;
		int numPotentialCornersWhite = 0;
		int numCornersBlack = 0;
		int numPotentialCornersBlack = 0;
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] == OthelloGame.EMPTY) { //calculate mobility
					if(OthelloGame.isValidMove(board, i, j, true)) numLegalMovesBlack++;
					if(OthelloGame.isValidMove(board, i, j, false)) numLegalMovesWhite++;
					
					
				}else if(board[i][j] == OthelloGame.BLACK) {
					blackScore++; //for count heuristic
					
					//for stability heuristic
					int stability = getStability(board, i, j);
					if(stability == STABLE) numStableBlack++;
					else if(stability == UNSTABLE) numUnstableBlack++;
					else numSemiStableBlack++;
					
					
				}else {
					whiteScore++; //for count heuristic
					
					//for stability heuristic
					int stability = getStability(board, i, j);
					if(stability == STABLE) numStableWhite++;
					else if(stability == UNSTABLE) numUnstableWhite++;
					else numSemiStableWhite++;
					
				}
			}
		}
		
		//handle corners
		int[] cornerRow = {0, 0, OthelloGame.BOARD_SIZE-1, OthelloGame.BOARD_SIZE-1};
		int[] cornerCol = {0, OthelloGame.BOARD_SIZE-1, 0, OthelloGame.BOARD_SIZE-1};
		
		//loop corners
		for(int i = 0; i < 4; i++) {
			if(board[cornerRow[i]][cornerCol[i]] == OthelloGame.BLACK) {
				numCornersBlack++;
			}
			else if (board[cornerRow[i]][cornerCol[i]] == OthelloGame.WHITE) {
				numCornersWhite++;
			}
			else {
				if(OthelloGame.isValidMove(board, cornerRow[i], cornerCol[i], true))
					numPotentialCornersBlack++;
				if(OthelloGame.isValidMove(board, cornerRow[i], cornerCol[i], false))
					numPotentialCornersWhite++;
			}
		}
		
		//heuristic values
		double countHeuristic = calculateHeuristicValue(blackScore, whiteScore, isBlacksMove);
		double actualMobilityHeuristic = calculateHeuristicValue(numLegalMovesBlack, numLegalMovesWhite, isBlacksMove);
		double stableHeuristic = calculateHeuristicValue(numStableBlack, numStableWhite, isBlacksMove);
		double unstableHeuristic = calculateHeuristicValue(numUnstableBlack, numUnstableWhite, isBlacksMove);
		double semiStableHeuristic = calculateHeuristicValue(numSemiStableBlack, numSemiStableWhite, isBlacksMove);
		double cornersHeuristic = calculateHeuristicValue(numCornersBlack, numCornersWhite, isBlacksMove);
		double potentialCornersHeuristic = calculateHeuristicValue(numPotentialCornersBlack, numPotentialCornersWhite, isBlacksMove);
		
		//calculate final value
		return countHeuristic*HEURISTIC_WEIGHTS[0] + actualMobilityHeuristic*HEURISTIC_WEIGHTS[1] + stableHeuristic*HEURISTIC_WEIGHTS[2]
				+ unstableHeuristic*HEURISTIC_WEIGHTS[3] + semiStableHeuristic*HEURISTIC_WEIGHTS[4] + cornersHeuristic*HEURISTIC_WEIGHTS[5]
				+ potentialCornersHeuristic*HEURISTIC_WEIGHTS[6];
	}
	
	
	public static int getStability(char[][] board, int row, int col) {
		boolean isBlacksMove =  board[row][col] == OthelloGame.BLACK;
		char opposingColor = isBlacksMove ? OthelloGame.WHITE : OthelloGame.BLACK;
		
		int[] deltaRow = {0, 1, 1, -1};
		int[] deltaCol = {1, 0, -1, 1};
		
		int currentRow = row;
		int currentCol = col;
		
		//loop through each direction
		for(int i = 0; i < 4; i++) {
			boolean firstSideHasOpposingColor = false;
			boolean firstSideHasPossibleMove = false;
			boolean firstSideHasOpenSpace = false;
			boolean secondSideHasOpposingColor = false;
			boolean secondSideHasPossibleMove = false;
			boolean secondSideHasOpenSpace = false;
			
			//first side
			currentRow = row - deltaRow[i];
			currentCol = col - deltaCol[i];
			
			while(OthelloGame.isValidPosition(currentRow, currentCol)) {
				if(board[currentRow][currentCol] == opposingColor) {
					firstSideHasOpposingColor = true;
					break;
				} else if(board[currentRow][currentCol] == OthelloGame.EMPTY) {
					if(OthelloGame.isValidMove(board, currentRow, currentCol, !isBlacksMove)) {
						firstSideHasPossibleMove = true;
					} else {
						firstSideHasOpenSpace = true;
					}
					break;
				}
				
				currentRow -= deltaRow[i];
				currentCol -= deltaCol[i];
			}
			
			if(!firstSideHasOpposingColor && !firstSideHasPossibleMove && !firstSideHasOpenSpace) {
				continue; //no flip possible in this direction
			}else {
				//second side
				currentRow = row + deltaRow[i];
				currentCol = col + deltaCol[i];
				
				while(OthelloGame.isValidPosition(currentRow, currentCol)) {
					if(board[currentRow][currentCol] == opposingColor) {
						secondSideHasOpposingColor = true;
						break;
					}else if(board[currentRow][currentCol] == OthelloGame.EMPTY) {
						if(OthelloGame.isValidMove(board, currentRow, currentCol, !isBlacksMove)) {
							secondSideHasPossibleMove = true;
						} else {
							secondSideHasOpenSpace = true;
						}
						break;
					}
					
					currentRow += deltaRow[i];
					currentCol += deltaCol[i];
				}
			}
			if(!secondSideHasOpposingColor && !secondSideHasPossibleMove && !secondSideHasOpenSpace) {
				continue; //no flip possible in this direction
			}
			else if ((firstSideHasOpposingColor && secondSideHasPossibleMove) ||
					(secondSideHasOpposingColor && firstSideHasPossibleMove)) {
				return UNSTABLE;
			}
			else if ((firstSideHasPossibleMove || firstSideHasOpenSpace)
					&& (secondSideHasPossibleMove || secondSideHasOpenSpace)) {
				return SEMI_STABLE;
			}
		}
		
		return STABLE;
	}
	
 }
