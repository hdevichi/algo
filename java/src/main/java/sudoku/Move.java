package sudoku;

import java.util.Set;

public class Move {

	private int x;
	private int y;
	private byte value;
	private Set<Byte> constraints;
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
		value = -1;
	}
	
	public Set<Byte> getConstraints() {
		return constraints;
	}
	public void setConstraints(Set<Byte> constraints) {
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
	public byte getValue() {
		return value;
	}
	public void setValue(byte value) {
		if (value < 0)
			throw new InvalidBoardException();
		if (value > 9)
			throw new InvalidBoardException();
		
		this.value = value;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('(').append(x).append(',').append(y).append(")->").append(value);
		return b.toString();
	}
	
}
