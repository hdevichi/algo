package sudoku;

public class ExempleBoards {

	private ExempleBoards() {
		throw new IllegalStateException("Utility class");
	}

	public static Board getBoardTimingSample() {

		byte[][] values = {
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{9, 1, 4, 5, 7, 0, 0, 2, 6},
				{5, 2, 8, 6, 0, 1, 4, 9, 0},
				{0, 0, 0, 0, 0, 3, 2, 0, 4},
				{0, 6, 3, 1, 0, 9, 5, 7, 0},
				{8, 0, 1, 2, 0, 0, 0, 0, 3},
				{0, 5, 2, 7, 0, 4, 0, 0, 9},
				{6, 3, 0, 9, 0, 0, 8, 4, 0},
				{4, 0, 0, 3, 2, 0, 0, 5, 0}
		};
		return new Board(values);
	}

public static Board getBoardEasy() {
		
		byte[][] values = {
				{4, 1, 0, 8, 0, 3, 0, 6, 9},
				{0, 0, 0, 0, 7, 0, 0, 0, 0},
				{0, 7, 0, 5, 0, 6, 0, 2, 0},
				{0, 0, 3, 6, 0, 5, 2, 0, 7},
				{6, 0, 0, 0, 3, 0, 0, 0, 8},
				{5, 0, 1, 7, 0, 2, 6, 0, 0},
				{0, 8, 0, 9, 0, 7, 0, 3, 0},
				{0, 0, 0, 0, 1, 0, 0, 0, 0},
				{7, 6, 0, 3, 0, 8, 0, 9, 4}
		};
		return new Board(values);
	}
	
	public static Board getBoardHard() {
		
		byte[][] values = {
				{0, 5, 0,   0, 4, 0, 0, 0, 9},
				{2, 0, 0,   0, 7, 0, 0, 3, 0},
				{0, 0, 0,   6, 0, 0, 7, 8, 0},
				
				{0, 6, 0,   0, 5, 0, 4, 0, 0},
				{1, 0, 0,   4, 0, 6, 0, 0, 3},
				{0, 0, 4,   0, 8, 0, 0, 5, 0},
				
				{0, 1, 9,   0, 0, 7, 0, 0, 0},
				{0, 3, 0,   0, 2, 0, 0, 0, 1},
				{6, 0, 0,   0, 1, 0, 0, 9, 0}
		};
		return new Board(values);
	}
	
public static Board getBoardVeryHard() {
		
		byte[][] values = {
				{2, 0, 0,   0, 0, 7,   6, 0, 0},
				{0, 0, 0,   9, 4, 0,   0, 1, 0},
				{0, 0, 0,   2, 0, 0,   0, 7, 8},
	
				{1, 0, 0,   0, 0, 0,   3, 0, 0},
				{9, 8, 0,   0, 0, 0,   0, 4, 7},
				{0, 0, 3,   0, 0, 0,   0, 0, 1},
	
				{8, 3, 0,   0, 0, 4,   0, 0, 0},
				{0, 6, 0,   0, 3, 8,   0, 0, 0},
				{0, 0, 9,   7, 0, 0,   0, 0, 6}
		};
		return new Board(values);
	}

	public static Board getBoardHardest() {
		
		byte[][] values = {
				{8, 0, 0,   0, 0, 0,   0, 0, 0},
				{0, 0, 3,   6, 0, 0,   0, 0, 0},
				{0, 7, 0,   0, 9, 0,   2, 0, 0},
	
				{0, 5, 0,   0, 0, 7,   0, 0, 0},
				{0, 0, 0,   0, 4, 5,   7, 0, 0},
				{0, 0, 0,   1, 0, 0,   0, 3, 0},
	
				{0, 0, 1,   0, 0, 0,   0, 6, 8},
				{0, 0, 8,   5, 0, 0,   0, 1, 0},
				{0, 9, 0,   0, 0, 0,   4, 0, 0}
		};
		return new Board(values);
	}

	public static Board getBoardEmpty() {
		
		byte[][] values = {
				{0, 0, 0,   0, 0, 0,   0, 0, 0},
				{0, 0, 0,   0, 0, 0,   0, 0, 0},
				{0, 0, 0,   0, 0, 0,   0, 0, 0},
	
				{0, 0, 0,   0, 0, 0,   0, 0, 0},
				{0, 0, 0,   0, 0, 0,   0, 0, 0},
				{0, 0, 0,   0, 0, 0,   0, 0, 0},
	
				{0, 0, 0,   0, 0, 0,   0, 0, 0},
				{0, 0, 0,   0, 0, 0,   0, 0, 0},
				{0, 0, 0,   0, 0, 0,   0, 0, 0}
		};
		return new Board(values);
	}
}
