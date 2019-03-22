package sudoku;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DancingSudokuSolver {

    private static final int SUB_BOARD_SIZE = 3;
    private static final int BOARD_SIZE = SUB_BOARD_SIZE * SUB_BOARD_SIZE;
	
    private static Logger LOGGER = null;
    
    static {
        InputStream stream = DancingSudokuSolver.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            LOGGER = Logger.getLogger(DancingSudokuSolver.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            builder.append("("+x+","+y+")         "+left.x+" < ("+x+") > "+right.x+" || "+up.y+" ^ ("+y+") v "+down.y);
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
        
        DancingSudokuSolver solver = new DancingSudokuSolver();
        
        int[][] matrix = solver.generateTestCoverMatrix();
        Node[] columns = solver.generateNodes(matrix);
        System.out.println("Solving sample:");
        printMatrix(matrix);

        // launch recursive solve
        Stack<Node> solution = new Stack<Node>();
        boolean found = solver.solve(columns[0], solution);

        if (found) {
            solver.printMatrixSolution(solution, matrix);
        } else {
            System.out.println("No solution found.");
        }

        System.out.println();
        System.out.println("Solving sample:");
        Board board = ExempleBoards.getBoardHardest();
        System.out.println(board);
        matrix = solver.generateSudokuCoverMatrix();
        columns = solver.generateNodes(matrix);
        LOGGER.log(Level.FINE, "All set");
        // cover parts of solution. 
        solver.coverClues(columns, board);
        LOGGER.log(Level.FINE, "Covered");
        solution = new Stack<Node>();
        found = solver.solve(columns[0], solution);
        if (found) {
            solver.printBoardSolution(solution, board);
        } else {
            System.out.println("No solution found.");
        }
        
    }    
    
    private void coverClues(Node[] columns, Board board){

        for (int x = 0 ; x < BOARD_SIZE ; x++) {
            for (int y=0; y < BOARD_SIZE; y++) {
                int value = board.get(x,y);
                if (value != 0) {
                    cover(columns[BOARD_SIZE*y+x+1]); // pas la bonne, on couvre 2 fois la même
                } 
            }
        }
    }

    private boolean solve(Node head, Stack<Node> solution) {
        
        if (head.right == head) {
            return true;
        }

        Node candidateColumn = head.right;
        Node column = candidateColumn;
        int maxSize = BOARD_SIZE*BOARD_SIZE*BOARD_SIZE*BOARD_SIZE;
        while (candidateColumn != head) {
            if (candidateColumn.column.size < maxSize) {
                maxSize = candidateColumn.column.size;
                column = candidateColumn;
            }
            candidateColumn = candidateColumn.right;
        }
        cover(column.column);

        Node node = column.down;
        while (node != column) {
            solution.push(node);
            Node row = node.right;
            while (row != node) {
                cover(row.column);
                row = row.right;
            }
            boolean found = solve(head, solution);
            if (found) {
                return true;
            }
            row = solution.pop();
            node = row.left;
            while (node != row) {
                uncover(node.column);
                node = node.left;
            }
            node = node.down;
        }

        uncover(column.column);
        return false;
    }

    private void printMatrixSolution( List<Node> solution, int[][] matrix) {
        
        System.out.println("Solution: ");
        for (Node row : solution) {
            printMatrixLine(row.y, matrix[row.y-1]);
        }
    }

    private void printBoardSolution( List<Node> solution, Board board) {
        
        System.out.println("Solution: ");
        for (Node row : solution) {
            System.out.println(row.y);
        }
    }

    private void cover(Node n) {

        ColumnNode column = n.column;
        //LOGGER.log(Level.INFO, "Covering "+column.id);

        column.removeFromRow();
        
        Node colNode = column.down;
   
        while (colNode != column) {
            Node rowNode = colNode.right;
            
            while (rowNode != colNode) {
                rowNode.removeFromColumn();
                
                rowNode = rowNode.right;
            }
            colNode = colNode.down;
        }
        //LOGGER.log(Level.INFO, "Covered "+column.id);
    }

    private void uncover(ColumnNode node) {

        ColumnNode column = node.column;
        //LOGGER.log(Level.INFO, "Uncovering "+column.id);

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

    private Node[] generateNodes(int[][] matrix) {

        // create parent column
        ColumnNode head = new ColumnNode(0);

        Node[] columns = new Node[matrix[0].length+1];
        columns[0] = head;
        // create all the columns, and link them (circularly)
        for (int i = 1 ; i <= matrix[0].length ; i++) {
            ColumnNode column = new ColumnNode(i);
            column.x=i;
            column.y=0;
            columns[i-1].insertRight(column);
            columns[i] = column;
        }

        // parse the matrix ; if a 1 is found, insert it into the proper column, with its 4 links
        for (int i = 0 ; i < matrix.length ; i++) {
            Node currentLine = null; 
            for (int j = 0 ; j < matrix[0].length ; j++) {
                if (matrix[i][j] != 0) {
                    Node node = new Node();
                    node.x = j+1;
                    node.y = i+1;
                    Node column = columns[j+1];
                    column.insertDown(node); // inserer au bon endroit dans la col...
                    columns[j+1] = node;
                    if (currentLine != null) {
                        currentLine.insertRight(node);
                    } 
                    currentLine = node;
                }
            }
        }

        return columns;
    }

    private int[][] generateSudokuCoverMatrix() {
        
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
    
    private int[][] generateTestCoverMatrix() {
        
        int[][] coverMatrix = {
            {0, 0, 1, 0, 1, 1, 0},
            {1, 0, 0, 1, 0, 0, 1},
            {0, 1, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 1, 1, 0, 1}
        };

        return coverMatrix;
    }

    private static void printMatrix(int[][] matrix) {
        
        for (int i = 0; i < matrix.length ; i++) {
                printMatrixLine(i+1, matrix[i]);
        }
        System.out.println();
    }

    private static void printMatrixLine(int number, int[] line) {
        System.out.print(number+" >    ");
        for (int j = 0; j < line.length; j++) {
            System.out.print(line[j] + " ");
        }
        System.out.println();
    }
}