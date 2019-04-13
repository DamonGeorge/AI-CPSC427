import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BoardCell extends JPanel implements MouseListener{
	public static int CELL_SIZE = 40;
//	private boolean highlight;
	private char value;
	private final int row;
	private final int col;
	private boolean isValid;
	
	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		
		isValid = false;
				
		setOpaque(false);
		setHighlight(false);
		setValue(OthelloGame.WHITE);
		setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
		addMouseListener(this);
	}
	
	public void setHighlight(boolean highlight) {
		if(highlight) 
			setBorder(BorderFactory.createLineBorder(Color.RED));
		else 
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	public void setValue(char val) {
		value = val;
	}
	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	 @Override
	protected void paintComponent(Graphics g) {
	   super.paintComponent(g);
	   // draw the rectangle here
	   if(value == OthelloGame.BLACK) {
		   g.setColor(Color.BLACK);
		   g.fillOval(5, 5, 30, 30);
	   }else if (value == OthelloGame.WHITE) {
		   g.setColor(Color.WHITE);
		   g.fillOval(5, 5, 30, 30);
	   }
	  
	}

	 
	 
	 
    //Mouse Listeners:
	 
	 
	 
	@Override
	public void mouseClicked(MouseEvent e) {
		if(isValid) {
			// TODO do somethings
		}
	}

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) {
		if(isValid)
			setHighlight(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setHighlight(false);
	}
}