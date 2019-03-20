package sudoku;

public class DancingSudokuSolver {

    private static final int SUB_BOARD_SIZE = 3;
    private static final int BOARD_SIZE = SUB_BOARD_SIZE * SUB_BOARD_SIZE;

    public class Node {
        public Node left;
        public Node right;
        public Node up;
        public Node down;
        public ColumnNode column;
    }

    public class ColumnNode extends Node {
        public int id;
        public int size;
    }

	public static void main(String[] args) {
		
		Board board = ExempleBoards.getBoardHardest();
        
        DancingSudokuSolver solver = new DancingSudokuSolver();

        // A matrix for the exact cover problem
        int[][] coverMatrix = solver.generateCoverMatrix();
        ColumnNode nodes = solver.generateNodes(coverMatrix);
    }
    
    private Node cover(Node n) {
        throw new IllegalStateException();
    }

    private Node uncover(Node n) {
        throw new IllegalStateException();
    }

    private ColumnNode generateNodes(int[][] matrix) {
        throw new IllegalStateException();

        // create parent column
        // create all the columns, and link them (circularly)

        // parse the matrix ; if a 1 is found, insert it into the proper column, with its 4 links
    }

    private int[][] generateCoverMatrix() {
        
        int[][] coverMatrix = new int[BOARD_SIZE*BOARD_SIZE*BOARD_SIZE][4*BOARD_SIZE*BOARD_SIZE];

        
        // We populate the matrix (generic for a sudoku bard)
        // the lines are the different 'candidates' for each case, in the following order:
        // C1/1, C1/2, ... , C1/9, C2/1, ... Ci/j is the candidate 'j' in the square 'i' of the board
        // the columns are the constraints to satisfy: 
        // - a single digit per case
        // - a single occurence of each digit in each column
        // - a single occurnece in lines
        // - a single in sub boards
        for (int x = 0 ; x < BOARD_SIZE ; x++) {
            for (int y  = 0 ; y < BOARD_SIZE ; y++) {
                for (int digit = 1 ; digit <= BOARD_SIZE ; digit++) {
                // placing a digit satisfies 4 constraints : 
                // unicity in the square
                coverMatrix[(y*BOARD_SIZE+x)*BOARD_SIZE+digit-1][y*BOARD_SIZE+x] = 1;
                // position of the digit in column // constraint col x, digit is in pit
                coverMatrix[(y*BOARD_SIZE+x)*BOARD_SIZE+digit-1][BOARD_SIZE*BOARD_SIZE+x*BOARD_SIZE+digit] = 1;
                // position of the digit in line
                coverMatrix[(y*BOARD_SIZE+x)*BOARD_SIZE+digit-1][2*BOARD_SIZE*BOARD_SIZE+y*BOARD_SIZE+digit] = 1;
                // position in subgrid
                int subX = x / SUB_BOARD_SIZE;
                int subY = y / SUB_BOARD_SIZE;
                coverMatrix[(y*BOARD_SIZE+x)*BOARD_SIZE+digit-1][3*BOARD_SIZE*BOARD_SIZE+SUB_BOARD_SIZE*(SUB_BOARD_SIZE*subY+subX)+digit] = 1;
                }
            }
        }

        return coverMatrix;
    }
    
    private static void printMatrix(int[][] matrix) {
        
        System.out.println(matrix.length);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
            System.out.println();
        }
    }
}