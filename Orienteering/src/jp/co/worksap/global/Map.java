/**
 * 
 */
package jp.co.worksap.global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dell
 * 
 */
public class Map {

	private char[][] map;
	private List<Position> checkpoint;
	private boolean[][] covered;

	public List<Position> getCheckpoint() {
		return checkpoint;
	}

	private final int height;
	private final int width;
	private Position start;
	private Position goal;

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Position getGoal() {
		return goal;
	}

	public Map(int width, int height) {
		super();
		this.height = height;
		this.width = width;
		map = new char[height][width];
		checkpoint = new ArrayList<Position>();
		covered = new boolean[this.height][this.width];
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int i = 0;
		while (i < this.height) {
			int j = 0;
			while (j < this.width) {
				int c;
				covered[i][j]=false;
				try {
					c = br.read();
					if (c != '\n' && c != '\r') {
						map[i][j] = (char) c;
						if (c == '@') {
							Position temp = new Position(i, j);
							checkpoint.add(temp);
						}

						if (c == 'S')
							this.start = new Position(i, j);
						if (c == 'G')
							this.goal = new Position(i, j);
					} else
						continue;
				} catch (IOException e) {

				}
				j++;
			}
			i++;
		}
		

	}

	

	public boolean[][] getCovered() {
		return covered;
	}

	public void showMap() {
		System.out.print("  ");
		for (int i = 0; i < this.width; i++)
			System.out.print(i);
		System.out.println();
		for (int i = 0; i < this.height; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < this.width; j++) {
				System.out.print(map[i][j]);
			}

			System.out.println("");
		}
	}

	public Position getStart() {
		return start;
	}

	public void setStart(Position start) {
		this.start = start;
	}

	public void updateMap(Position p, char ch) {
		map[p.getRow()][p.getCol()] = ch;
	}

	public char getChar(Position p) {
		return this.map[p.getRow()][p.getCol()];
	}
}
