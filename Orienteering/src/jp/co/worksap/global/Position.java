package jp.co.worksap.global;

public class Position implements Comparable<Position>{

	private int row;
	private int col;

	public Position(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean equals(Position p) {
		if (this.row == p.row && this.col == p.col)
			return true;
		else
			return false;
	}

	public Position getPosition(){
		return this;
	}
	public static int distance(Position p1, Position p2) {
		return (Math.abs(p1.getRow() - p2.getRow()) + Math.abs(p2.getCol()
				+ p1.getCol()));
	}

	@Override
	public int compareTo(Position o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
