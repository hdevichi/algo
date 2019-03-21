package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DancingSudokuSolver {

    private static final int SUB_BOARD_SIZE = 3;
    private static final int BOARD_SIZE = SUB_BOARD_SIZE * SUB_BOARD_SIZE;
	
	private static final Logger LOGGER = Logger.getLogger( DancingSudokuSolver.class.getName() );
	
    public class Node {
        public int x;
        public int y;
        public Node left;
        public Node right;
        public Node up;
        public Node down;
        public ColumnNode column;

        public Node() {
            left = this;
            right = this;
            up = this;
            down = this;
        }

        public void insertRight(Node node) {
            node.left = this;
            node.right = this.right;
            this.right.left = node;
            this.right = node;
        }

        public void insertDown(Node node) {
            node.up = this;
            node.down = this.down;
            this.down.up = node;
            this.down = node;
            node.column = column;
            column.size = column.size +1;
        }

        public void removeFromRow() {
            left.right = this.right;
            right.left = this.left;
        }

        public void removeFromColumn() {
            up.down = this.down;
            down.up = this.up;
            this.column.size = this.column.size - 1;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Col: "+column.id+" ("+x+","+y+")   ||   "+left.x+" < > "+right.x+" | "+up.y+" ^ v "+down.y);
            return builder.toString();
        }
    }

    public class ColumnNode extends Node {
        public int id;
        public int size;

        public ColumnNode(int id) {
            super();
            this.column = this; 
            this.id = id;
            this.size = 0;
        }
    }

	public static void main(String[] args) {
		
		Board board = ExempleBoards.getBoardHardest();
        
        DancingSudokuSolver solver = new DancingSudokuSolver();
        ColumnNode[] columns = solver.generateNodes();

        LOGGER.log(Level.INFO, "All set");
        // cover parts of solution. 
        solver.coverClues(columns, board);
        LOGGER.log(Level.INFO, "Covered");
        // launch recursive solve
        Stack<Node> solution = new Stack<Node>();
        solver.solve(columns[0], 0, solution, board);
    }    
    
    private void coverClues(ColumnNode[] columns, Board board){

        for (int i = 0 ; i < BOARD_SIZE ; i++) {
            for (int j=0; j < BOARD_SIZE; j++) {
                int value = board.get(i,j);
                if (value != 0) {
                    cover(columns[j+1]);
                } 
            }
        }
    }

    private void solve(ColumnNode head, int index, Stack<Node> solution, Board board) {
            
        Node column = head.right;
        if (column == head) {
            printSolution(board, solution);
            return;
        }

        cover(column.column);

        // TODO optimization, choose c
        Node node = column.down;
        while (node != column) {
            solution.push(node);
            Node row = node.right;
            while (row != node) {
                cover(row.column);
            }
            solve(head, index+1, solution, board);
            row = solution.pop();
            node = row.left;
            while (node != row) {
                uncover(node.column);
            }
        }

        uncover(column.column);
        return;
    }

    private void printSolution(Board board, List<Node> solution) {
        System.out.println(board);
        System.out.println(solution);
    }

    private void cover(ColumnNode n) {

        LOGGER.log(Level.INFO, "Covering "+n.id);

        ColumnNode column = n.column;
        column.removeFromRow();

        Node colNode = column.down;
        System.out.println(colNode);
        while (colNode != column) {
            Node rowNode = colNode.right;
            System.out.println(rowNode);
            while (rowNode != colNode) {
                rowNode.removeFromColumn();
                rowNode = rowNode.right;
            }
            colNode = colNode.down;
        }
   
    }

    private void uncover(ColumnNode node) {

        ColumnNode column = node.column;

        Node colNode = column.up;
        while (colNode != node) {
            Node rowNode = colNode.left;
            while (rowNode != colNode) {
                rowNode.up.insertDown(rowNode);
                rowNode = rowNode.left;
            }
            colNode = colNode.up;
        }

        column.left.insertRight(column);
    }

    private ColumnNode[] generateNodes() {

        int[][] matrix = generateCoverMatrix();
     
        // create parent column
        ColumnNode head = new ColumnNode(0);

        ColumnNode[] columns = new ColumnNode[matrix[0].length+1];
        columns[0] = head;
        // create all the columns, and link them (circularly)
        for (int i = 1 ; i <= matrix[0].length ; i++) {
            ColumnNode column = new ColumnNode(i);
            column.x=i+1;
            columns[i-1].insertRight(column);
            columns[i] = column;
        }

        // parse the matrix ; if a 1 is found, insert it into the proper column, with its 4 links
        for (int i = 0 ; i < matrix[0].length ; i++) {
            Node currentLine = null; 
            for (int j = 0 ; j < matrix.length ; j++) {
                if (matrix[j][i] != 0) {
                    Node node = new Node();
                    node.x = j+1;
                    node.y = i+1;
                    columns[i+1].insertDown(node);
                    if (currentLine != null) {
                        currentLine.insertRight(node);
                    } 
                    currentLine = node;
                }
            }
        }


        return columns;
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