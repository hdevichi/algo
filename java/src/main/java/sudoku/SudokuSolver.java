package sudoku;

import java.util.HashSet;
import java.util.Set;

public class SudokuSolver {

	private long positions;
	
	public static void main(String[] args) {
		
		Board b = ExempleBoards.getBoardHardest();
		launchSolver(b);
	}
	
	private static void launchSolver(Board b) {

		long start = System.currentTimeMillis();
		long start2 = System.nanoTime();

		
		SudokuSolver s = new SudokuSolver();
		s.solver(b, false);
		

		long end2 = System.nanoTime();
		long end = System.currentTimeMillis();
		
		System.out.println("Time: "+(end-start)+" ms"); //+s.getPositions()+" positions.");
		System.out.println("Time: "+(end2-start2)/1000+" µs");	

		System.out.println(b);
	}
	
	private boolean solver(Board board, boolean finished) {
		
		positions++;
		
		if ( isFullAndValid(board) ) {
			return true;
		}
		
		Move next = getMostConstrainedCell(board);
		if (next == null) {
			return false;
		}

		byte[] candidates = getValidCandidates(next);
		for (byte i : candidates) {
			next.setValue(i);
			board.add(next);
			finished = solver(board, finished);
			if (finished)
				return finished;
			board.remove(next);
		}
		return false;
	}
	
	private boolean isFullAndValid(Board board) {

		// lines & columns
		for (int i = 0 ; i < board.getSize(); i++) {
			
			int sumLigne=0;
			int sumColumn=0;
			
			for (int  j =0; j < board.getSize(); j++) {
				sumLigne += board.get(i,j);
				sumColumn += board.get(j,i);
			}
			if (sumLigne != 45 || sumColumn != 45)
				return false;
		}
		
		// subboards
		for (int b = 0 ; b < 9; b++) {
	
			int sumSubBoard =0;
			for (int i = 0 ; i < 3; i ++) {
				for (int j = 0 ; j < 3 ; j++) {
					sumSubBoard += board.get( (b%3)*3+i, (b/3)*3+j);
				}
			}
			if (sumSubBoard != 45)
				return false;
		}
		
		
		return true;
		
	}
	
	// can be null, if board is full for instance
	// complexity - n^3
	private Move getMostConstrainedCell(Board board) {
		
		int mostConstraint = 0;
		Move mostConstrained = null;
		
		for (int i = 0 ; i < 9; i++) {
			for (int j = 0; j < 9 ; j++) {

				if (board.get(i,j) == 0) {
	
					Set<Byte> constraints = getConstraintListOnCell(board, i, j);
					int cons = constraints.size();
					if (cons > mostConstraint) {
						mostConstraint = cons;
						mostConstrained = new Move(i, j);
						mostConstrained.setConstraints(constraints);
					}
				}
			}
		}
		
		return mostConstrained;
	}
	
	private byte[] getValidCandidates(Move move) {
		
		Set<Byte> constraints = move.getConstraints();
		
		byte[] possible = new byte[9-constraints.size()];
		int k = 0;
		for (byte i = 1 ; i <= 9 ; i++) {
			if ( !constraints.contains(i) ) {
				possible[k]= i;
				k++;
			}
		}
		
		return possible;
	}
	
	// cell must be empty
	// complexity - n 
	private Set<Byte> getConstraintListOnCell(Board board, int x, int y ) {
		
		Set<Byte> constraints = new HashSet<>();
		for (int i = 0 ; i < board.getSize() ; i++) {
			if (board.get(x,i) != 0)
				constraints.add( board.get(x,i) );
			if (board.get(i, y) != 0)
				constraints.add( board.get(i,y) );
		}
		
		int bx = x / 3;
		int by = y / 3;
		for (int i = 0 ; i < 3; i ++) {
			for (int j = 0 ; j < 3 ; j++) {
				if (board.get(bx*3+i, by*3+j) != 0)
					constraints.add( board.get(bx*3+i, by*3+j) );
			}
		}
		return constraints;
	}
	
	public long getPositions() {
		return positions;
	}
}
