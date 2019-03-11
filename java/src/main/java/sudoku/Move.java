package sudoku;

import java.util.Set;

public class Move {

	private int x;
	private int y;
	private int value;
	private Set<Integer> constraints;
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
		value = -1;
	}
	
	public Set<Integer> getConstraints() {
		return constraints;
	}
	public void setConstraints(Set<Integer> constraints) {
		this.constraints = constraints;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		if (value < 0)
			throw new InvalidBoardException();
		if (value > 9)
			throw new InvalidBoardException();
		
		this.value = value;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append('(').append(x).append(',').append(y).append(")->").append(value);
		return b.toString();
	}
	
}
