package sudoku;

public class Board {

	int size;
	byte[][] values;
	int freecells;
	
	public Board(byte[][] values) {
		
		// assume board is square
		size = values.length;
		this.values = values;
		
		freecells = size*size;
		for (int i = 0 ; i < values.length; i++) {
			for (int j = 0; j < values.length ; j++) {
				if (values[i][j] <0)
					throw new InvalidBoardException();
				if (values[i][j] >9)
					throw new InvalidBoardException();
				if (values[i][j] !=0)
					freecells--;
			}
		}
	}
	
	public int getSize() {
		return size;
	}

	public int getFreeCells() {
		return freecells;
	}

	// no check
	public byte get(int i, int j) {
		return values[i][j];
	}
	
	// no checks
	public void add(Move move) {
		freecells--;
		values[move.getX()][move.getY()] = move.getValue();
	}

	public void remove(Move move) {
		freecells++;
		values[move.getX()][move.getY()] = 0;
	}
		
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    for (int i= 0 ; i < size ; i++) {
	    	if (i%3 == 0 )
	    		sb.append(System.getProperty("line.separator"));
	    	for (int j = 0 ; j < size ; j++) {
	    		if (j%3 == 0 )
	    			sb.append("  ");
	    		sb.append(values[i][j]);
    			sb.append(' ');
	    	}
	    	sb.append(System.getProperty("line.separator"));
	    }
	    return sb.toString();
	}
}
