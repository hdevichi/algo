# algo
This project contains algorithmic exercises
* self reproducing code
* sudoku solver (in multiple languages)

Comparisons : Sudoku Solver runs in about

| Language      | Mac (mac book pro 13'' 2016) | PC (core i7 8th gen) |
| ------------- |:-------------:| -----:|
| Python 3.6    | 50 ms (48-52) | 35 ms |
| Java 11       | 15ms sec (12-17)  | 2 - 6 ms  |
| Go 1.10       | 550  µs (500 - 800)| 420 µs (410 - 440)|
| Rust 1.33     | 450 µs (450-900) | 250 µs |


 To run from command line : 

 Java
 cd java
 mvn compile
 java -cp ./target/classes sudoku.SudokuSolver
(ou mvn exec:java -Dexec.mainClass="sudoku.SudokuSolver")

Note: sous windows le sous-système linux semble plus rapide en ligne de commande.

lancer sonar : mvn sonar:sonar

Go
cd go
go run sudoku.go

Rust
cd rust
cargo build --release
cargo run --release