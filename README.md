# algo
This project contains algorithmic exercises
* self reproducing code
* sudoku solver (in multiple languages)

Comparisons : Sudoku Solver runs in about
 * 50ms in python (48-52)
 * 15ms sec in java (12-17) [ sur le mac ; 2 - 6 sur PC]
 * 550 µs in go (550-800)
 * 450 µs in rust (450-900)

 To run from command line : 

 Java : 
 cd java
 mvn compile
 java -cp ./target/classes sudoku.SudokuSolver
(ou mvn exec:java -Dexec.mainClass="sudoku.SudokuSolver")

note: sous windows le sous système linux semble plus rapide en ligne de commande.
