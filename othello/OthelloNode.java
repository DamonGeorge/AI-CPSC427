import java.util.ArrayList;

public class OthelloNode {
	//TODO: handle skip moves for the row and col members
	//TODO: handle leaf nodes in calcChildren and calcHeuristic
	
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
	private OthelloMove previousMove;
	private boolean isBlacksMove;
	private ArrayList<OthelloNode> children;
	private int zobristHash;
//	private OthelloAI parentAI;
//	private Double heuristicValue;
	
	//***************************************************
	//Instance Functions
	//***************************************************
	//overloaded constructors
	public OthelloNode(char[][] board, boolean isBlacksMove, int zobristHash) {
		this(board, isBlacksMove, zobristHash, null);
	}
//	public OthelloNode(char[][] board, boolean isBlacksMove, OthelloAI parentAI, Integer zobristHash) {
//		this(board, isBlacksMove, parentAI, null, zobristHash);
//	}
	public OthelloNode(char[][] board, boolean isBlacksMove, int zobristHash, int previousMoveRow, int previousMoveCol) {
		this(board, isBlacksMove, zobristHash, new OthelloMove(previousMoveRow, previousMoveCol));
	}
//	public OthelloNode(char[][] board, boolean isBlacksMove, OthelloAI parentAI, int previousMoveRow, int previousMoveCol, Integer zobristHash) {
//		this(board, isBlacksMove, parentAI, new OthelloMove(previousMoveRow, previousMoveCol), zobristHash);
//	}
//	public OthelloNode(char[][] board, boolean isBlacksMove, OthelloAI parentAI, OthelloMove previousMove) {
//		this(board, isBlacksMove, parentAI, previousMove, null);
//	}
	public OthelloNode(char[][] board, boolean isBlacksMove, int zobristHash, OthelloMove previousMove) {
		this.board = board;
		this.isBlacksMove = isBlacksMove;
		this.previousMove = previousMove;
		this.zobristHash = zobristHash;
//		this.parentAI = parentAI;
		this.children = null;
//		this.heuristicValue = null;
	}

	//Getters
	public char[][] getBoard() {
		return board;
	}
	public boolean getIsBlacksMove() {
		return isBlacksMove;
	}
	public ArrayList<OthelloNode> getChildren() {
		return children;
	}
//	public double getHeuristicValue() {
//		if(heuristicValue == null) //calculate if necessary
//			calculateFullHeuristic(board, isBlacksMove);
//		
//		return heuristicValue;
//	}
	public OthelloMove getPreviousMove() {
		return previousMove;
	}
	public boolean isBlacksMove() {
		return isBlacksMove;
	}
	
	public int getZobristHash() {
		return zobristHash;
	}
	
	
	//setters
	public void setChildren(ArrayList<OthelloNode> children) {
		this.children = children;
	}
	
	
	
	
	
	
	
	public Double calculateFullHeuristic() {
		return calculateFullHeuristic(board, isBlacksMove);
	}
	
	//Necessary for using as hashmap key
	@Override
	public int hashCode() {
		return getZobristHash();
	}
	
	@Override
	public boolean equals(Object o) {
		//basic checks
		if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        
        //check if boards are equal
        OthelloNode node = (OthelloNode) o;
        for(int i = 0; i < board.length; i++) {
        	for(int j = 0; j < board[0].length; j++) {
        		if(this.board[i][j] != node.board[i][j])
        			return false;
        	}
        }
        
        return true;
	}
	
	
	
	//***************************************************
	//Static Utility Functions 
	//***************************************************
	
	//TODO: move this stuff to OthelloAI class maybe?
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
		
		//if this is a leaf node
		if(numLegalMovesBlack == 0 && numLegalMovesWhite == 0) {
			return 100; //TODO: don't we need to know whether the AI is black or not to do this right?
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
