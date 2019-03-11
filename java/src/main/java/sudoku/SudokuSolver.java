package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuSolver {

	private long positions;
	
	public static void main(String[] args) {
		
		Board b = ExempleBoards.getBoardVeryHard1();
		launchSolver(b);
		
		b = ExempleBoards.getBoardHardest();
		launchSolver(b);
		
	}
	
	private static void launchSolver(Board b) {
		List<Move> moves= new ArrayList<>();
		SudokuSolver s = new SudokuSolver();
		
		long time = System.currentTimeMillis();
		s.solver(moves,b, false);
		long time2 = System.currentTimeMillis();
		System.out.println("Time: "+(time2-time)+" ms, "+s.getPositions()+" positions.");
	}
	
	//TODO finished is useless? 
	private void solver(List<Move> moves, Board board, boolean finished) {
		
		positions++;
		
		if ( isValid(board) ) {
			System.out.println("Initial free cells: "+moves.size());
			System.out.println(board);
			return;
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
			solver(moves, board, finished);
			if (finished)
				return;
			board.remove(next);
			moves.remove(next);
		}
	}
	
	private boolean isValid(Board board) {

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
		for (int bx = 0 ; bx < 3; bx++) {
			for (int by = 0 ; by < 3; by++) {
					
				int sumSubBoard =0;
				for (int i = 0 ; i < 3; i ++) {
					for (int j = 0 ; j < 3 ; j++) {
						sumSubBoard += board.get(bx*3+i, by*3+j);
					}
				}
				if (sumSubBoard != 45)
					return false;
			}
		}
		
		return true;
		
	}
	
	// can be null, if board is full for instance
	private Move getMostConstrainedCell(Board board) {
		
		int mostConstraint = 0;
		Move mostConstrained = null;
		
		Move[] freeCells = board.getFree();
		for (Move freeCell : freeCells) {
			int cons = getConstraintsOnCell(board, freeCell.getX(), freeCell.getY());
			if (cons > mostConstraint) {
				mostConstraint = cons;
				mostConstrained = freeCell;
			}
		}
		
		return mostConstrained;
	}
	
	private int[] getValidCandidates(Board board, Move move) {
		
		// TODO calculé 2 fois
		Set<Integer> constraints = getConstraintListOnCell(board, move.getX(), move.getY());
		
		int[] possible = new int[9-constraints.size()];
		int k = 0;
		for (int i = 1 ; i <= 9 ; i++) {
			if ( !constraints.contains(i) ) {
				possible[k]= i;
				k++;
			}
		}
		
		// TODO optimization: 
		// remove ones that invalidate the board
		return possible;
	}
	
	// cell must be empty
	private Set<Integer> getConstraintListOnCell(Board board, int x, int y) {
		
		if (board.get(x,y) != 0)
			throw new RuntimeException();
		
		Set<Integer> constraints = new HashSet<>();
		for (int i = 0 ; i < board.getSize() ; i++) {
			if (board.get(x, i) != 0)
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
	
	private int getConstraintsOnCell(Board board, int x, int y ) {
		return getConstraintListOnCell(board, x, y).size();
	}
	
	public long getPositions() {
		return positions;
	}
}
