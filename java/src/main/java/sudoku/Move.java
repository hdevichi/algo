package sudoku;

public class Move {

	private int x;
	private int y;
	private int value;
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
		value = -1;
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
			throw new RuntimeException();
		if (value > 9)
			throw new RuntimeException();
		
		this.value = value;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('(').append(x).append(',').append(y).append(")->").append(value);
		return b.toString();
	}
	
}
