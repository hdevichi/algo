use std::time::Instant;

const HEIGHT : usize = 9;
const WIDTH : usize = 9;
const SIZE : usize = HEIGHT * WIDTH;

struct Sudoku {
    empty: usize,
    values: [usize; SIZE ]
}

fn init_board(val : [usize; SIZE]) -> Sudoku {

	let mut board = Sudoku {
		values: val,
		empty: 0
	};
	
	for y in 0..HEIGHT {
		for x in  0..WIDTH {
			if board.values[y * WIDTH + x] == 0 {
				board.empty = board.empty + 1
			}
		}
	}
	 
	return board;
}

fn clear_position(board : &mut Sudoku, x : usize, y : usize) {
	board.values[y * WIDTH + x] = 0;
	board.empty = board.empty + 1;
}

fn set_position(board : &mut Sudoku, x : usize, y : usize, value: usize) {
	board.values[y * WIDTH + x] = value;
	board.empty = board.empty - 1;
}

fn get_position(board : &Sudoku, x : usize, y : usize) -> usize {
	return board.values[y * WIDTH + x];
}

fn is_value_valid(board : &Sudoku, x : usize, y : usize, value: usize) -> bool {

	// check line
	for i in 0..WIDTH {
		if i == x {
			continue;
		}
		if value == get_position(board, i, y) {
			return false;
		}
	}

	// check column
	for j in 0..HEIGHT {
		if j == y {
			continue;
		}
		if value == get_position(board, x, j) {
			return false;
		}
	}

	// check sub grid (0-3) (3-6) (6-9)
	let section_x = 3 * (x / 3);
	let section_y = 3 * (y / 3);
	for i in section_x..section_x+3 {
		for j in section_y..section_y+3 {
			if (x == i) && (y == j) {
				continue;
			}
			if value == get_position(board, i, j) {
				return false;
			}
		}
	}
	return true;
}

// test validity, but not completion
fn is_valid(board: &Sudoku) -> bool {

	for x in 0..WIDTH {
		for y in 0..HEIGHT {
			let value = get_position(board, x, y);
			if value == 0 {
				continue;
			}
			if is_value_valid(board, x, y, value) == false {
				return false;
			}
		}
	}

	return true;
}

fn compute_valid_values_for_position(board : &Sudoku, x : usize, y : usize) -> Vec<usize> {

	let value = get_position(board, x, y);
	let mut valid : Vec<usize> = Vec::new();
    
    if value == 0 { 
        for candidate in 1..=WIDTH {
            if is_value_valid(board, x, y, candidate) {
                valid.push(candidate);
            }
        }
	}

	return valid;
}

fn solve(board : &mut Sudoku) {

	if is_valid(board) == false {
		println!("Invalid start solution for solve!");
	} else {
    	let result = recursive_solve(board);
    	if result {
			println!("Solution found !");
			for y in 0..HEIGHT {
            	for x in 0..WIDTH {
			    	print!("{} ",board.values[y * WIDTH + x]);
            	}
            	println!("");
			}
		} else {
			println!("No solution found.");
		}
    }
}

fn recursive_solve(board : &mut Sudoku) -> bool {


	if board.empty == 0 {
		return true;
	}

	// find next most constrained position (candidate)
	let mut candidate_x : usize = 0;
    let mut candidate_y : usize = 0;
	let mut number_of_valid_values : usize = 9;
	let mut valid : Vec<usize> = Vec::new();
	for x in  0..WIDTH {
		for y in 0..HEIGHT {
			let valid_values = compute_valid_values_for_position(board, x, y);
			if valid_values.len() == 0 {
				continue;
			}
			if valid_values.len() < number_of_valid_values {
				candidate_x = x;
				candidate_y = y;
				number_of_valid_values = valid_values.len();
				valid = valid_values;
			}
		}
	}

	for value in valid {
		set_position(board, candidate_x, candidate_y, value);
		let result = recursive_solve(board);
		if result {
			return true;
		}
		clear_position(board, candidate_x, candidate_y);
	}
	return false;
}

fn main() {
    
	let start = Instant::now();

	let mut board = init_board([
				8, 0, 0,   0, 0, 0,   0, 0, 0,
				0, 0, 3,   6, 0, 0,   0, 0, 0,
				0, 7, 0,   0, 9, 0,   2, 0, 0,
	
				0, 5, 0,   0, 0, 7,   0, 0, 0,
				0, 0, 0,   0, 4, 5,   7, 0, 0,
				0, 0, 0,   1, 0, 0,   0, 3, 0,
	
				0, 0, 1,   0, 0, 0,   0, 6, 8,
				0, 0, 8,   5, 0, 0,   0, 1, 0,
				0, 9, 0,   0, 0, 0,   4, 0, 0
	]);

/*
	let mut board = init_board([
		0, 0, 0, 0, 0, 0, 0, 0, 0,
		9, 1, 4, 5, 7, 0, 0, 2, 6,
		5, 2, 8, 6, 0, 1, 4, 9, 0,
		0, 0, 0, 0, 0, 3, 2, 0, 4,
		0, 6, 3, 1, 0, 9, 5, 7, 0,
		8, 0, 1, 2, 0, 0, 0, 0, 3,
		0, 5, 2, 7, 0, 4, 0, 0, 9,
		6, 3, 0, 9, 0, 0, 8, 4, 0,
		4, 0, 0, 3, 2, 0, 0, 5, 0
        ]);
*/
	solve(&mut board);

	let end = Instant::now();
	println!("Solution found in {:?}", end.duration_since(start));
}