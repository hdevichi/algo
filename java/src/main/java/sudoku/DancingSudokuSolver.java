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
        public Move value;

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

        public void removeFromColumn() {
            up.down = this.down;
            down.up = this.up;
            LOGGER.log(Level.FINE,"      Removing ("+x+","+y+") from column "+column.id);
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
        public Node[] columns;

        public ColumnNode(int id) {
            super();
            this.column = this; 
            this.id = id;
            this.size = 0;
        }

        public void removeFromColumnsRow() {
            left.right = this.right;
            right.left = this.left;
        }
    }

	public static void main(String[] args) {
        
        DancingSudokuSolver solver = new DancingSudokuSolver();
        

        int[][] matrix = solver.generateTestCoverMatrix();
        Node[] columns = solver.generateNodes(matrix,null);
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

        long start = System.currentTimeMillis();
        long start2 = System.nanoTime();

        System.out.println();
        System.out.println("Solving sample:");
        Board board = ExempleBoards.getBoardHardest();
        System.out.println(board);
        matrix = solver.generateSudokuCoverMatrix(board);
        //printMatrix(matrix);
        columns = solver.generateNodes(matrix, board);
        LOGGER.log(Level.FINE, "Matrix initialized.");
        solution = new Stack<Node>();
        found = solver.solve(columns[0], solution);
        if (found) {
            solver.printBoardSolution(solution);
        } else {
            System.out.println("No solution found.");
        }

        long end2 = System.nanoTime();
        long end = System.currentTimeMillis();

        System.out.println("Time: "+(end-start)+" ms"); //+s.getPositions()+" positions.");
        System.out.println("Time: "+(end2-start2)/1000+" µs");	
        
    }    

// arret sur covering 30 ko
// 30 est couverte au passage de la ligne 174, atteinte via couv de 8

// (pas de backtrack car on est dans les col de taille 1, les chfifres placés)
// il y a un bug en gros en examinant les chiffres plaxés, il trouve une contrainte
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
        LOGGER.log(Level.FINE, "Choosing "+column.column.id+ " - size " + column.column.size);

        cover(column.column); // au second passage, on couvre 24 qui a une longueur de 0
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

    private void printBoardSolution( List<Node> solution) {
        
        Board board = ExempleBoards.getBoardEmpty();
        System.out.println("Solution: ");
        for (Node row : solution) {
            board.add(row.value);
        }
        System.out.println(board.toString());
    }


    // quand on couvre 248 cela enleve un element dns 24 qui se retrouve taille 0 et provoque un fail de l'algo
    // pas normal: colonne 24 a normalement 9 1  !!!!
    private void cover(Node n) {

        ColumnNode column = n.column;
        LOGGER.log(Level.FINE, "Covering "+column.id+ " - size " + column.size);

        column.removeFromColumnsRow();
        
        Node colNode = column.down;
   
        while (colNode != column) {
            LOGGER.log(Level.FINE, "   Treating row: "+colNode.y);    
            Node rowNode = colNode.right;
            
            while (rowNode != colNode) {
                rowNode.removeFromColumn();
                
                rowNode = rowNode.right;
            }
            colNode = colNode.down;
        }
    }

    private void uncover(ColumnNode node) {

        ColumnNode column = node.column;
        LOGGER.log(Level.FINE, "Uncovering "+column.id);

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

    // TODO more generic implementation. Add a PRoblem objet, 
    // responsible for both adding the correct value to nodes, 
    // and to interpret them in the solution.
    private Node[] generateNodes(int[][] matrix, Board board) {

        List<Move> headers = generateSudokuRowHeaders(board);
        
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
        head.columns = columns;

        // parse the matrix ; if a 1 is found, insert it into the proper column, with its 4 links
        for (int i = 0 ; i < matrix.length ; i++) {
            Node currentLine = null; 
            Move move = null;
            if (headers != null) {
                move = headers.get(i);
            }
            for (int j = 0 ; j < matrix[0].length ; j++) {
                if (matrix[i][j] != 0) {
                    Node node = new Node();
                    node.value = move;
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

    private int[][] generateSudokuCoverMatrix(Board board) {
        
        List<int[]> coverMatrix = new ArrayList<int[]>();
        
        // We populate the matrix (generic for a sudoku bard)
        // the lines are the different 'candidates' for each case, in the following order:
        // C1/1, C1/2, ... , C1/9, C2/1, ... Ci/j is the candidate 'j' in the square 'i' of the board
        // the columns are the constraints to satisfy: 
        // - a single digit per case
        // - a single occurence of each digit in each column
        // - a single occurnece in lines
        // - a single in sub boards
        for (int y = 0 ; y < BOARD_SIZE ; y++) {
            for (int x  = 0 ; x < BOARD_SIZE ; x++) {
                int value = board.get(y,x);                
                if (value == 0 ) {
                    for (int digit = 0 ; digit < BOARD_SIZE ; digit++) {
                        // placing a digit satisfies 4 constraints : 
                        // unicity in the square
                        int[] line = new int[4*BOARD_SIZE*BOARD_SIZE];
                        
                        
                        line[y*BOARD_SIZE+x] = 1;
                        // position of the digit in line
                        line[BOARD_SIZE*BOARD_SIZE+y*BOARD_SIZE+digit] = 1;
                        // position of the digit in column // constraint col x, digit is in pit
                        line[2*BOARD_SIZE*BOARD_SIZE+x*BOARD_SIZE+digit] = 1;
                        // position in subgrid
                        int subX = x / SUB_BOARD_SIZE;
                        int subY = y / SUB_BOARD_SIZE;
                        line[3*BOARD_SIZE*BOARD_SIZE+BOARD_SIZE*(SUB_BOARD_SIZE*subX+subY)+digit] = 1;
                        coverMatrix.add(line);
                    }
                } else {
                    int[] line = new int[4*BOARD_SIZE*BOARD_SIZE];
                    // cell not empty
                    int col1 = y*BOARD_SIZE+x;
                    // position of the digit in column // constraint col x, digit is in pit
                    int col2 = BOARD_SIZE*BOARD_SIZE+y*BOARD_SIZE+value-1;
                    // position of the digit in line     
                    int col3 = 2*BOARD_SIZE*BOARD_SIZE+x*BOARD_SIZE+value-1;
                    int subX = x / SUB_BOARD_SIZE;
                    int subY = y / SUB_BOARD_SIZE;
                    // position in subgrid
                    int col4 = 3*BOARD_SIZE*BOARD_SIZE+BOARD_SIZE*(SUB_BOARD_SIZE*subX+subY)+value-1;
                    
                    line[col1] = 1;
                    line[col2] = 1; 
                    line[col3] = 1;
                    line[col4] = 1;
                    coverMatrix.add(line);
                }
            }
        }


        int [][] matrixAsArray = new int[coverMatrix.size()][4*BOARD_SIZE*BOARD_SIZE];
        for (int i = 0 ; i < coverMatrix.size(); i++) {
            matrixAsArray[i] = coverMatrix.get(i);
        }
        
        return matrixAsArray;
    }

    private List<Move> generateSudokuRowHeaders(Board board) {
        
        if (board == null)
            return null;

        List<Move> headers = new ArrayList<Move>();
        
        // attention : must be same order as matrix generation
        for (int y = 0 ; y < BOARD_SIZE ; y++) {
            for (int x  = 0 ; x < BOARD_SIZE ; x++) {
                int value = board.get(y,x);                
                if (value == 0 ) {
                    for (byte digit = 0 ; digit < BOARD_SIZE ; digit++) {
                        Move move = new Move(y, x);
                        move.setValue((byte)(digit+1));
                        headers.add(move);
                    }
                } else {
                    Move move = new Move(y, x);
                    move.setValue((byte)value);
                    headers.add(move);
                }
            }
        }
        return headers;
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
        
        System.out.println(matrix.length+"*"+matrix[0].length);
        for (int i = 0; i < matrix.length ; i++) {
                printMatrixLine(i+1, matrix[i]);
        }
        System.out.println();
    }

    private static void printMatrixLine(int number, int[] line) {
        if (number <= 99 ) {
            System.out.print(" ");
        }
        if (number <= 9 ) {
            System.out.print(" ");
        }
        System.out.print(number+" >    ");
        for (int j = 0; j < line.length; j++) {
            System.out.print(line[j]);
            if ( (j+1) % (BOARD_SIZE*BOARD_SIZE) == 0) {
                System.out.print(" ");
            }
           
        }
        System.out.println();
    }
}