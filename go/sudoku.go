package main

import (
	"fmt"
	"time"
)

var width, height int
var empty int
var values [][]int

func initBoard(val [][]int) {
	width = len(val[0])
	height = len(val)
	values = val
	empty = 0

	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {
			if values[y][x] == -1 {
				empty = empty + 1
			}
		}
	}
}

func clearPosition(x, y int) {
	values[y][x] = -1
	empty = empty + 1
}

func setPosition(x, y, value int) {
	values[y][x] = value
	empty = empty - 1
}

func getPosition(x, y int) int {
	return values[y][x]
}

func isValueValid(x, y, value int) bool {

	// check line
	for i := 0; i < width; i++ {
		if i == x {
			continue
		}
		if value == getPosition(i, y) {
			return false
		}
	}

	// check column
	for j := 0; j < height; j++ {
		if j == y {
			continue
		}
		if value == getPosition(x, j) {
			return false
		}
	}

	// check sub grid (0-3) (3-6) (6-9)
	sectionX := 3 * (x / 3)
	sectionY := 3 * (y / 3)
	for i := sectionX; i < sectionX+3; i++ {
		for j := sectionY; j < sectionY+3; j++ {
			if (x == i) && (y == j) {
				continue
			}
			if value == getPosition(i, j) {
				return false
			}
		}
	}
	return true
}

// test validity, but not completion
func isValid() bool {

	for x := 0; x < width; x++ {
		for y := 0; y < height; y++ {
			value := getPosition(x, y)
			if value == -1 {
				continue
			}
			if isValueValid(x, y, value) == false {
				return false
			}
		}
	}

	return true
}

func computeValidValuesForPosition(x, y int) []int {

	value := getPosition(x, y)
	valid := []int{}

	if value == -1 {
		for candidate := 1; candidate < 10; candidate++ {
			if isValueValid(x, y, candidate) {
				valid = append(valid, candidate)
			}
		}
	}

	return valid
}

func solve() {

	if isValid() == false {
		fmt.Println("Invalid start solution for solve!")
		return
	}

	result := recursiveSolve()
	if result {
		fmt.Println("Solution found !")
		for line := 0; line < height; line++ {
			fmt.Println(values[line])
		}
	} else {
		fmt.Println("No solution found.")
	}
}

func recursiveSolve() bool {

	if empty == 0 {
		return true
	}

	// find next most constrained position (candidate)
	candidateX, candidateY := 0, 0
	numberOfValidValues := 9
	valid := []int{}
	for x := 0; x < width; x++ {
		for y := 0; y < height; y++ {
			validValues := computeValidValuesForPosition(x, y)
			if len(validValues) == 0 {
				continue
			}
			if len(validValues) < numberOfValidValues {
				candidateX = x
				candidateY = y
				numberOfValidValues = len(validValues)
				valid = validValues
			}
		}
	}

	for _, value := range valid {
		setPosition(candidateX, candidateY, value)
		result := recursiveSolve()
		if result {
			return true
		}
		clearPosition(candidateX, candidateY)
	}

	return false
}

func timeTrack(start time.Time, name string) {
	elapsed := time.Since(start)
	fmt.Printf("%s took %s", name, elapsed)
	recover()
}

func main() {

	//initBoard([][]int{
	//	{-1, -1, -1, -1, -1, -1, -1, -1, -1},
	//	{9, 1, 4, 5, 7, -1, -1, 2, 6},
	//	{5, 2, 8, 6, -1, 1, 4, 9, -1},
	//	{-1, -1, -1, -1, -1, 3, 2, -1, 4},
	//	{-1, 6, 3, 1, -1, 9, 5, 7, -1},
	//	{8, -1, 1, 2, -1, -1, -1, -1, 3},
	//	{-1, 5, 2, 7, -1, 4, -1, -1, 9},
	//	{6, 3, -1, 9, -1, -1, 8, 4, -1},
	//	{4, -1, -1, 3, 2, -1, -1, 5, -1}})

	initBoard([][]int{
		{8, -1, -1, -1, -1, -1, -1, -1, -1},
		{-1, -1, 3, 6, -1, -1, -1, -1, -1},
		{-1, 7, -1, -1, 9, 1, 2, -1, -1},
		{-1, 5, -1, -1, -1, 7, -1, -1, -1},
		{-1, -1, -1, -1, 4, 5, 7, -1, -1},
		{-1, -1, -1, 1, -1, -1, -1, 3, -1},
		{-1, -1, 1, -1, -1, -1, -1, 6, 8},
		{-1, -1, 8, 5, -1, -1, -1, 1, -1},
		{-1, 9, -1, -1, -1, -1, 4, -1, -1}})

	defer timeTrack(time.Now(), "solve")
	solve()
}
