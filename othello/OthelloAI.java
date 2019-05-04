import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The AI for playing Othello that uses alpha beta pruning, Zobrist Hashing, and heuristic evaluation
 * to decide the next best move in the current game.
 */
public class OthelloAI {

	private int[][][] zobristTable; //the table of random values for hashing game boards
	private OthelloNode currentOthelloNode; //the current node in the Othello state space
	private HashMap<OthelloNode, Double> heuristicMap; //the hashmap for heuristics
	private boolean isAIBlack; //whether the AI is the black player or not
	
	/**
	 * Create an Othello AI instance using the given game
	 * and whether the AI is black.
	 */
	public OthelloAI(OthelloGame game, boolean isAIBlack) {
		this.isAIBlack = isAIBlack;
		
		//initialize the zobrist hashing
		initZobristTable();
		heuristicMap = new HashMap<>(1000);
		
		//create the starting othello node
		currentOthelloNode = new OthelloNode(game.getBoard(), game.isBlacksMove(), calculateZobristHash(game.getBoard()));
		
//		System.out.println(currentOthelloNode);
	}
	
	/**
	 * Signal the AI the make a move at the given row and column.
	 * Returns true if the move was valid and a new child state was found.
	 */
	public boolean move(int row, int col) {
		return move(new OthelloMove(row, col));
	}
	/**
	 * Signal the AI to skip the current turn if possible
	 * Returns true if the skip was valid and a new child state was found.
	 */
	public boolean skipMove() {
		return move(null);
	}
	
	/**
	 * Make the given move and advance to the child node created by that move.
	 * Returns true if the move was valid and the corresponding child node was found.
	 */
	public boolean move(OthelloMove move) {
		ArrayList<OthelloNode> children = getChildren(currentOthelloNode);
		
		//if null move, this is a skip
		//check if only one child exists and move to it
		if(move == null) {
			if(children.size() == 1) {
				currentOthelloNode = children.get(0);
				return true;
			} else {
				return false;
			}
		}
		
		//otherwise loop through children until the correct child is found
		for(OthelloNode child : children) {
			if(child.getPreviousMove().equals(move)) {
				currentOthelloNode = child;
				return true;
			}
		}
		
		//if we get here no child was found using the given move
		return false;
	}
	
	/**
	 * Is the given node during this AI's turn
	 */
	public boolean isAIsTurn(OthelloNode node) {
		return !(node.isBlacksMove() ^ isAIBlack);
	}
	
	
	
	/**
	 * This is the main functionality of this AI: to find the next best move using alpha beta pruning.
	 * This returns the OthelloNode that is the best child of the current node.
	 * This can be null if the depthLimit is 0, if the current node is an end state,
	 * or if the current node is not this AI's turn.
	 */
	public OthelloNode findBestMove(int depthLimit) {
//		System.out.println(currentOthelloNode);
		
		if(depthLimit == 0) return null;
		
		ArrayList<OthelloNode> children = getChildren(currentOthelloNode);
		
		//check if end state
		if(children.isEmpty()) return null;

		//initialize values for alpha beta search
		OthelloNode bestChild = null;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		
		//only search if it's the AI's turn
		if(isAIsTurn(this.currentOthelloNode)) {
			for(OthelloNode child : children) {
//				System.out.println(child);
				
				//do the search
				double value = alphaBetaSearch(child, alpha, beta, depthLimit, 1);
				
				//if we've found a better alpha, update the best child
				if(value > alpha) {
					alpha = value;
					bestChild = child;
				}
				
				//prune
				if(alpha >= beta)
					break;
			}
		}
		
		return bestChild; //null if nothing found or if not AI's turn
	}
	
	/**
	 * The alpha beta pruning search algorithm.
	 */
	private double alphaBetaSearch(OthelloNode node, double alpha, double beta, int depthLimit, int currentDepth) {
		if(currentDepth == depthLimit)
			return getHeuristicValue(node);
		
		ArrayList<OthelloNode> children = getChildren(node);
		
		if(children.isEmpty())
			return node.getLeafScore(isAIBlack);
				
		if(isAIsTurn(node)) { //MAX
			for(OthelloNode child : children) {
//				System.out.println(child);
				
				alpha = Math.max(alpha, alphaBetaSearch(child, alpha, beta, depthLimit, currentDepth+1));
				if(alpha >= beta)
					return beta;
			}
			return alpha;
			
		}else { //MIN
			for(OthelloNode child : children) {
//				System.out.println(child);
				
				beta = Math.min(beta, alphaBetaSearch(child, alpha, beta, depthLimit, currentDepth+1));
				if(alpha >= beta)
					return alpha;
			}
			return beta;
		}
	}
	
	
	
	/**
	 * Utility for getting the child nodes from the given node.
	 * This either gets the children from the provided node,
	 * or generates them for the node.
	 */
	public ArrayList<OthelloNode> getChildren(OthelloNode node) {
		ArrayList<OthelloNode> children = node.getChildren();
		if(children == null) {
			calculateChildren(node);
		}
		
		return node.getChildren();
	}
	
	/**
	 * Generate the child nodes in the Othello state space for the given node.
	 * This calculates all possible moves, creates new nodes for the results
	 * of each of those moves, and calculates the new nodes Zobrist hash.
	 */
	public void calculateChildren(OthelloNode node) {
		ArrayList<OthelloNode> children = new ArrayList<>();
		char[][] currentBoard = node.getBoard();
		char[][] resultingBoard = new char[OthelloGame.BOARD_SIZE][OthelloGame.BOARD_SIZE];
		int resultingZobristHash = node.getZobristHash();
		
		//get the current teams
		char currentColor = node.isBlacksMove() ? OthelloGame.BLACK : OthelloGame.WHITE;
		char opposingColor = node.isBlacksMove() ? OthelloGame.WHITE : OthelloGame.BLACK;
		
		//get corresponding zobrist table indexes;
		int currentColorZobristIndex = getZobristTableIndex(currentColor);
		int opposingColorZobristIndex = getZobristTableIndex(opposingColor);
		int emptyZobristIndex = getZobristTableIndex(OthelloGame.EMPTY);
		
		for(int row = 0; row < OthelloGame.BOARD_SIZE; row++) {
			for(int col = 0; col < OthelloGame.BOARD_SIZE; col++) {
				if(currentBoard[row][col] == OthelloGame.EMPTY) { //make sure the position is empty
					boolean moveFound = false;
					
					//loop through all spaces adjacent to the given position
					for(int i = row - 1; i <= row + 1; i++) {
						for(int j = col - 1; j <= col + 1; j++) {
							//if the surrounding space is valid and is of the opposite color, 
							//check for a possible move
							if(OthelloGame.isValidPosition(i, j) && currentBoard[i][j] == opposingColor) {
								int deltaRow = i - row;
								int deltaCol = j - col;

								int currentRow = i + deltaRow;
								int currentCol = j + deltaCol;

								boolean flip = false;

								//loop checking for a possible move in the current direction
								//away from the given position.
								//If the position if off the board or empty, no move is possible from this angle.
								//Else if we see the opposing color until we see the current color, a move is possible.
								while(OthelloGame.isValidPosition(currentRow, currentCol)) {
									if(currentBoard[currentRow][currentCol] == OthelloGame.EMPTY) {
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
									if(!moveFound)  {
										moveFound = true;
										
										//copy the current board to the result
										OthelloGame.copyBoard(currentBoard, resultingBoard);
										
										//copy zobrist Hash
										resultingZobristHash = node.getZobristHash();
									}
	
									currentRow -= deltaRow;
									currentCol -= deltaCol;
									while(currentRow != row || currentCol != col) {
										//set tile
										resultingBoard[currentRow][currentCol] = currentColor;
										//update zobrist
										resultingZobristHash ^= zobristTable[currentRow][currentCol][opposingColorZobristIndex];
										resultingZobristHash ^= zobristTable[currentRow][currentCol][currentColorZobristIndex];
										//update position
										currentRow -= deltaRow;
										currentCol -= deltaCol;
									}
								}
							}
						}
					}
					
					//add tile to the space we moved on
					if(moveFound) {
						//put down tile
						resultingBoard[row][col] = currentColor;
						
						//update zobrist
						resultingZobristHash ^= zobristTable[row][col][emptyZobristIndex];
						resultingZobristHash ^= zobristTable[row][col][currentColorZobristIndex];
						
						children.add(new OthelloNode(resultingBoard, !node.isBlacksMove(), resultingZobristHash, row, col));
						resultingBoard = new char[OthelloGame.BOARD_SIZE][OthelloGame.BOARD_SIZE]; //allocate new board for next iteration
					}
				}
			}
		}
		
		//if this is a skip state, create a singular child with a null move leading to it
		if(children.isEmpty() && OthelloGame.isAnyValidMoveAvailable(currentBoard, !node.isBlacksMove())) {
			OthelloGame.copyBoard(currentBoard, resultingBoard);
			children.add(new OthelloNode(resultingBoard, !node.isBlacksMove(), node.getZobristHash()));
		}
		
//		//sort children to help speed up alpha beta: currently taking too long
//		children.sort(new Comparator<OthelloNode>() {
//			@Override
//			public int compare(OthelloNode o1, OthelloNode o2) {
//				double firstValue = getHeuristicValue(o1);
//				double secondValue = getHeuristicValue(o2);
//				//sort descending
//				if(firstValue < secondValue)
//					return 1;
//				else if (firstValue > secondValue)
//					return -1;
//				else
//					return 0;
//			}
//		});
		
		node.setChildren(children);
	}
	
	
	/**
	 * Get the heuristic value for the given node,
	 * either by finding it in the HashMap,
	 * or by calculating it.
	 * @param node
	 * @return
	 */
	public Double getHeuristicValue(OthelloNode node) {
		Double value = heuristicMap.get(node);
		
		if(value == null) {
			value = node.calculateFullHeuristic(isAIBlack);
			heuristicMap.put(node, value);
		}
		return value;
	}
	
	/**
	 * Fill the Zobrist table with random integers.
	 */
	private void initZobristTable() {
		zobristTable = new int[OthelloGame.BOARD_SIZE][OthelloGame.BOARD_SIZE][3];
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		for(int i = 0; i < OthelloGame.BOARD_SIZE; i++) {
			for(int j = 0; j < OthelloGame.BOARD_SIZE; j++) {
				for(int k = 0; k < 3; k++) {
					zobristTable[i][j][k] = random.nextInt();
				}
			}
		}
	}
	
	/**
	 * Get the index in the 3rd dimension of the Zobrist Table
	 * corresponding to the given value of the board tile.
	 * @return 0, 1, or 2
	 */
	public int getZobristTableIndex(char c) {
		if(c == OthelloGame.BLACK)
			return 2;
		else if(c == OthelloGame.WHITE)
			return 1;
		else 
			return 0;
	}
	
	/**
	 * Calculate the Zobrist Hash for the given board.
	 * This XORs a random value from the Zobrist Table
	 * for each space on the board
	 */
	public int calculateZobristHash(char[][] board) {
		int result = 0;
		
		for(int i = 0; i < board.length; i++) {
        	for(int j = 0; j < board[0].length; j++) {
        		result ^= zobristTable[i][j][getZobristTableIndex(board[i][j])];
        	}
        }
		
		return result;
	}
}