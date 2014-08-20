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
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int i = 0;
		while (i < this.height) {
			int j = 0;
			while (j < this.width) {
				int c;
				try {
					c = br.read();
					if (c != '\n' && c != '\r') {
						map[i][j] = (char) c;
						if (c == '@')
							checkpoint.add(new Position(i, j));
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

	public void showMap() {
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}

	public Position getStart() {
		return start;
	}

	public void setStart(Position start) {
		this.start = start;
	}
	public void updateMap(Position p, char ch){
		map[p.getRow()][p.getCol()]=ch;
	}

	public char getChar(Position p){
		return this.map[p.getRow()][p.getCol()];
	}
}
