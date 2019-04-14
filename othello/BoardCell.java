import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * The Board Cell Component used in the Othello GUI Board Table,
 * which has hover and click listeners, allowing the player to select a move in the GUI.
 */
@SuppressWarnings("serial")
public class BoardCell extends JPanel implements MouseListener{
	public static int CELL_SIZE = 40; //size of the square cell in pixels

	private char value;
	private final int row;
	private final int col; 
	private boolean isSelectable;
	
	/**
	 * Create the component and add mouse listener
	 */
	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		
		isSelectable = false;
				
		setOpaque(false);
		setHighlight(false);
		setValue(OthelloGame.WHITE);
		setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
		addMouseListener(this);
	}
	
	/**
	 * Highlight the cell. This changes the border color.
	 */
	public void setHighlight(boolean highlight) {
		if(highlight) 
			setBorder(BorderFactory.createLineBorder(Color.RED));
		else 
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	//Getter and setter for the value of the cell
	public void setValue(char val) {
		value = val;
	}
	public char getValue() {
		return value;
	}
	
	//setter for the isSelectable field of this object
	public void setIsSelectable(boolean isSelectable) {
		this.isSelectable = isSelectable;
	}
	
	/**
	 * The paint function called by swing.
	 * This draws a black or white circle if the value of the cell if black or white
	 */
	@Override
	protected void paintComponent(Graphics g) {
	   super.paintComponent(g);
	   
	   // draw the circle here
	   if(value == OthelloGame.BLACK) {
		   g.setColor(Color.BLACK);
		   g.fillOval(5, 5, 30, 30);
	   }else if (value == OthelloGame.WHITE) {
		   g.setColor(Color.WHITE);
		   g.fillOval(5, 5, 30, 30);
	   }
	  
	}

	 
    //Mouse Listeners:
	 
	/**
	 * The click listener. If the cell is selectable, 
	 * set the othello instance's selected cell variable,
	 * and notify the instance that a select event has occured.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(isSelectable) {
			Othello othello = Othello.getInstance();
			othello.setSelectedCell(row, col);
			othello.signalEvent(Othello.GameEvent.SELECT);
		}
	}

	/**
	 * If the cell is selectable, highlight it when the mouse hovers over it
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		if(isSelectable)
			setHighlight(true);
	}
	
	/**
	 * If the cell is selectable, unhighlight it when the mouse stops
	 * hovering over it. 
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		if(isSelectable)
			setHighlight(false);
	}
	
	//Ignored mouse events
	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }
}
