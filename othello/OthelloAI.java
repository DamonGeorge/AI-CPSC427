import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OthelloAI {

	private int[][][] zobristTable;
//	private OthelloGame game;
	private OthelloNode currentOthelloNode;
	private HashMap<OthelloNode, Double> heuristicMap;
	
	
	public OthelloAI(OthelloGame game) {
		initZobristTable();
		heuristicMap = new HashMap<>(1000);
		
		currentOthelloNode = new OthelloNode(game.getBoard(), game.isBlacksMove(), calculateZobristHash(game.getBoard()));
		
	}
	
	//TODO: Calc zobrist hash here
	public void calculateChildNodes(OthelloNode node) {
		ArrayList<OthelloNode> children = new ArrayList<>();
		char[][] currentBoard = node.getBoard();
		char[][] resultingBoard = new char[OthelloGame.BOARD_SIZE][OthelloGame.BOARD_SIZE];
		int resultingZobristHash = node.getZobristHash();
		
		//get the current teams
		char currentColor = node.getIsBlacksMove() ? OthelloGame.BLACK : OthelloGame.WHITE;
		char opposingColor = node.getIsBlacksMove() ? OthelloGame.WHITE : OthelloGame.BLACK;
		
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
										resultingZobristHash ^= zobristTable[i][j][opposingColorZobristIndex];
										resultingZobristHash ^= zobristTable[i][j][currentColorZobristIndex];
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
						
						children.add(new OthelloNode(resultingBoard, !node.getIsBlacksMove(), resultingZobristHash, row, col));
						resultingBoard = new char[OthelloGame.BOARD_SIZE][OthelloGame.BOARD_SIZE]; //allocate new board for next iteration
					}
				}
			}
		}
		
		//if this is a skip state, create a singular child with a null move leading to it
		if(children.isEmpty() && OthelloGame.isAnyValidMoveAvailable(currentBoard, !node.isBlacksMove())) {
			OthelloGame.copyBoard(currentBoard, resultingBoard);
			children.add(new OthelloNode(resultingBoard, !node.getIsBlacksMove(), node.getZobristHash()));
			
		}
		
		node.setChildren(children);
	}
	
	
	
	
	
	/**
	 * Get the heuristic value for the given node,
	 * either by finding it in the current HashMap,
	 * or by calculating it.
	 * @param node
	 * @return
	 */
	public Double getHeuristicValue(OthelloNode node) {
		Double value = heuristicMap.get(node);
		
		if(value == null) {
			value = node.calculateFullHeuristic();
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
