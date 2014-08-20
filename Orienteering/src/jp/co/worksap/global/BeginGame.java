package jp.co.worksap.global;

import java.util.PriorityQueue;

public class BeginGame {

	private static Map map;
	private boolean[][] visited;

	public BeginGame(int w, int h) {
		map = new Map(w, h);
		visited = new boolean[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				visited[i][j] = false;
			}
		}
	}

	public void moveUP() {
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), '.');
		map.setStart(new Position(map.getStart().getRow() - 1, map.getStart()
				.getCol()));
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), 'S');

	}

	public void moveDOWN() {
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), '.');
		map.setStart(new Position(map.getStart().getRow() + 1, map.getStart()
				.getCol()));
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), 'S');
	}

	public void moveLEFT() {
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), '.');
		map.setStart(new Position(map.getStart().getRow(), map.getStart()
				.getCol() - 1));
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), 'S');
	}

	public void moveRIGHT() {
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), '.');
		map.setStart(new Position(map.getStart().getRow(), map.getStart()
				.getCol() + 1));
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), 'S');
	}

	public void move(Position newposition) {
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), map.getChar(newposition));
		map.setStart(newposition);
		map.updateMap(newposition, 'S');
	}

	public Position getUPPosition(Position p) {
		return new Position(p.getRow() - 1, p.getCol());
	}

	public Position getDownPosition(Position p) {
		return new Position(p.getRow() + 1, p.getCol());
	}

	public Position getLeftPosition(Position p) {
		return new Position(p.getRow(), p.getCol() - 1);
	}

	public Position getRightPosition(Position p) {
		return new Position(p.getRow(), p.getCol() + 1);
	}

	public boolean hasReached() {
		if (getUPPosition(map.getStart()).equals(map.getGoal()) == true
				|| getDownPosition(map.getStart()).equals(map.getGoal()) == true
				|| getLeftPosition(map.getStart()).equals(map.getGoal()) == true
				|| getRightPosition(map.getStart()).equals(map.getGoal()) == true) {
			return true;
		} else
			return false;

	}

	public int Search() {
		// ap.showMap();
		boolean hasfound = false;
		visited[map.getStart().getRow()][map.getStart().getCol()] = true;
		PriorityQueue<Position> pq = new PriorityQueue<Position>();
		if (map.getChar(getUPPosition(map.getStart())) == '.') {
			pq.add(getUPPosition(map.getStart()));
			visited[getUPPosition(map.getStart()).getRow()][getUPPosition(
					map.getStart()).getCol()] = true;

		}
		if (map.getChar(getDownPosition(map.getStart())) == '.') {
			pq.add(getDownPosition(map.getStart()));
			visited[getDownPosition(map.getStart()).getRow()][getUPPosition(
					map.getStart()).getCol()] = true;

		}
		if (map.getChar(getLeftPosition(map.getStart())) == '.') {
			pq.add(getLeftPosition(map.getStart()));
			visited[getLeftPosition(map.getStart()).getRow()][getUPPosition(
					map.getStart()).getCol()] = true;
		}
		if (map.getChar(getRightPosition(map.getStart())) == '.') {
			pq.add(getRightPosition(map.getStart()));
			visited[getRightPosition(map.getStart()).getRow()][getUPPosition(
					map.getStart()).getCol()] = true;
		}
		for (Position p : pq) {
			System.out.println(p.getRow() + " " + p.getCol());
		}
		while (hasReached() == false) {
			Position newposition = pq.remove();
			move(newposition);
			Position temp = getUPPosition(newposition);
			if (map.getChar(temp) == '.'
					&& visited[temp.getRow()][temp.getCol()] == false) {
				pq.add(temp);
				moveUP();
				map.showMap();
				hasfound = true;
				visited[temp.getRow()][temp.getCol()] = true;
				System.out.println();
			}
			temp = getDownPosition(newposition);
			if (map.getChar(getDownPosition(newposition)) == '.'
					&& visited[temp.getRow()][temp.getCol()] == false) {
				pq.add(getDownPosition(newposition));
				moveDOWN();
				map.showMap();
				hasfound = true;
				visited[temp.getRow()][temp.getCol()] = true;
				System.out.println();
			}
			temp = getLeftPosition(newposition);
			if (map.getChar(getLeftPosition(newposition)) == '.'
					&& visited[temp.getRow()][temp.getCol()] == false) {
				pq.add(getLeftPosition(newposition));
				moveLEFT();
				map.showMap();
				hasfound = true;
				visited[temp.getRow()][temp.getCol()] = true;
				System.out.println();
			}
			temp = getRightPosition(newposition);
			if (map.getChar(getRightPosition(newposition)) == '.'
					&& visited[temp.getRow()][temp.getCol()] == false) {
				pq.add(getRightPosition(newposition));
				moveRIGHT();
				map.showMap();
				hasfound = true;
				visited[temp.getRow()][temp.getCol()] = true;
				System.out.println();
			}
			if (hasfound == false) {
				if (map.getChar(getUPPosition(newposition)) != '.'
						&& map.getChar(getDownPosition(newposition)) != '.'
						&& map.getChar(getLeftPosition(newposition)) != '.'
						&& map.getChar(getRightPosition(newposition)) != '.') {
					return -1;
				} else {
					for (int i = 0; i < map.getHeight(); i++) {
						for (int j = 0; j < map.getWidth(); j++) {
							visited[i][j] = false;
						}
					}
					visited[map.getStart().getRow()][map.getStart().getCol()] = true;
				}
			}
			//
			map.showMap();

		}
		return 1;
	}

	public void start() {

		Search();
	}

}
