import java.awt.Point;

/**
 *  Contains the information about a location in the maze,
 *  and its neighbors.
 *
 *  @author  Nicholas R. Howe
 *  @version CSC 112, 20 March 2006
 */
public class MazeLocation {
    /** The row number */
    private int row;

    /** The column number */
    private int col;

    /** Constructor from two ints */
    public MazeLocation(int row, int col) {
	this.row = row;
	this.col = col;
    }

    /** Copy constructor */
    public MazeLocation(MazeLocation p) {
	row = p.row;
	col = p.col;
    }

    /** accessor for the row number */
    public int getRow() {
	return row;
    }

    /** Accessor for the column number */
    public int getCol() {
	return col;
    }

    /** Manipulator for the row number */
    public void setRow(int row) {
	this.row = row;
    }

    /** Manipulator for the column number */
    public void setCol(int col) {
	this.col = col;
    }

    /** 
     *  Test for equality between coordinates
     *
     *  @param loc The coordinate to compare to
     *  @return T/F whether this equals the current coordinate
     */
    public boolean equals(Object loc) {
	    return (loc instanceof MazeLocation) ? ((this.row == ((MazeLocation)loc).row)&&(this.col == ((MazeLocation)loc).col)) : false;
    }

    /** 
     *  Hashcode that respects the equality definition above
     *
     *  @return the code
     */
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.row;
        hash = 37 * hash + this.col;
        return hash;
    }

    /** 
     *  Return a neighboring coordinate in the chosen direction
     *
     *  @param dir Direction to move
     *  @return The new coordinate
     */
    public MazeLocation neighbor(MazeDirection dir) {
	MazeLocation result = new MazeLocation(this);
	switch (dir) {
	case NORTH:
	    result.col--;
	    break;
	case SOUTH:
	    result.col++;
	    break;
	case EAST:
	    result.row++;
	    break;
	case WEST:
	    result.row--;
	    break;
	}
	return result;
    }

    /** 
     *  Return a neighboring coordinate in the reverse of the 
     *  specified direction
     *
     *  @param dir Direction to move in reverse to
     *  @return The new coordinate
     */
    public MazeLocation reverse(MazeDirection dir) {
	MazeLocation result = new MazeLocation(this);
	switch (dir) {
	case NORTH:
	    result.col++;
	    break;
	case SOUTH:
	    result.col--;
	    break;
	case EAST:
	    result.row--;
	    break;
	case WEST:
	    result.row++;
	    break;
	}
	return result;
    }

    /**
     *  Converts the coordinate to a string representation
     *
     *  @return A string representation:  (row,col)
     */
    public String toString() {
	return "("+row+","+col+")";
    }
}
