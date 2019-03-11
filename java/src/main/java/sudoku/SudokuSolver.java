package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuSolver {

	private long positions;
	
	public static void main(String[] args) {
		
		Board b = ExempleBoards.getBoardTimingSample();
		launchSolver(b);
		
	}
	
	private static void launchSolver(Board b) {
		List<Move> moves= new ArrayList<>();
		SudokuSolver s = new SudokuSolver();
		
		long start = System.currentTimeMillis();
		long start2 = System.nanoTime();

		s.solver(moves,b, false);
		long end2 = System.nanoTime();
		long end = System.currentTimeMillis();
		
		System.out.println("Time: "+(end-start)+" ms, "+s.getPositions()+" positions.");
		System.out.println("Time: "+(end2-start2)/1000+" µs");
		
		System.out.println();
	}
	
	private boolean solver(List<Move> moves, Board board, boolean finished) {
		
		positions++;
		
		if ( isFullAndValid(board) ) {
			System.out.println("Initial free cells: "+moves.size());
			System.out.println(board);
			return true;
		}
		
		Move next = getMostConstrainedCell(board);
		
		if (next == null) {
			return;
		}

		int[] candidates = getValidCandidates(board, next);
		for (int i : candidates) {
			next.setValue(i);
			moves.add(next);
			board.add(next);
			finished = solver(moves, board, finished);
			if (finished)
				return finished;
			board.remove(next);
			moves.remove(next);
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
	private Move getMostConstrainedCell(Board board) {
		
		int mostConstraint = 0;
		Move mostConstrained = null;
		
		Move[] freeCells = board.getFree();
		for (Move freeCell : freeCells) {
			Set<Integer> constraints = getConstraintListOnCell(board, freeCell);
			freeCell.setConstraints(constraints);
			int cons = constraints.size();
			if (cons > mostConstraint) {
				mostConstraint = cons;
				mostConstrained = freeCell;
			}
		}
		
		return mostConstrained;
	}
	
	private int[] getValidCandidates(Board board, Move move) {
		
		Set<Integer> constraints = move.getConstraints();
		
		int[] possible = new int[9-constraints.size()];
		int k = 0;
		for (int i = 1 ; i <= 9 ; i++) {
			if ( !constraints.contains(i) ) {
				possible[k]= i;
				k++;
			}
		}
		
		return possible;
	}
	
	// cell must be empty
	private Set<Integer> getConstraintListOnCell(Board board, Move cell) {
		
		Set<Integer> constraints = new HashSet<>();
		for (int i = 0 ; i < board.getSize() ; i++) {
			if (board.get(cell.getX(), i) != 0)
				constraints.add(new Integer(board.get(cell.getX(),i)));
			if (board.get(i, cell.getY()) != 0)
				constraints.add(new Integer(board.get(i,cell.getY())));
		}
		
		int bx = cell.getX() / 3;
		int by = cell.getY() / 3;
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
