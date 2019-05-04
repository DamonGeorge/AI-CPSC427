/**
 * Basic class for representing the row and column 
 * of a move in Othello. 
 * We use this in the OthelloNode class so that a 
 * node can have a null value for its move if 
 * a move was skipped to get to that node.
 */
public class OthelloMove {
	private int row;
	private int col;
	
	public OthelloMove(int row, int col) {
		this.row = row;
		this.col = col;
	}

	//getters and setters
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	
	public boolean equals(OthelloMove move) {
		if(move == null)
			return false;
		
		if(this.row == move.getRow() && this.col == move.getCol())
			return true;
		else
			return false;
	}
}
