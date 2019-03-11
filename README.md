# algo
This project contains algorithmic exercises
* self reproducing code
* sudoku solver (in multiple languages)

Comparisons : Sudoku Solver runs in about

| Language        | Mac (mac book pro 13'' 2016 | PC (core i7 8th gen)    |
| ------------- |:-------------:| -----:|
| Python 3      | 50 ms (48-52) |  35 ms                         |
| Java 8        |     7ms sec            | ??      |
| Go            | 550  µs (500 - 800)| 420 µs (410 - 440)|
| Rust          | 450 µs in rust (450-900)| |



 To run from command line : 

 Java : 
 cd java
 mvn compile
 java -cp ./target/classes sudoku.SudokuSolver
(ou mvn exec:java -Dexec.mainClass="sudoku.SudokuSolver")

note: sous windows le sous système linux semble plus rapide en ligne de commande.

Go :
cd go
go run sudoku.go
