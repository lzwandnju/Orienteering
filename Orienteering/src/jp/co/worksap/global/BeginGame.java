package jp.co.worksap.global;

import java.util.PriorityQueue;

public class BeginGame {

	private static Map map;
	private boolean[][] visited;
	private Heuristic heuristic;
	private Position currentCheckpoint;
	
	public BeginGame(int w, int h) {
		map = new Map(w, h);
		visited = new boolean[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				visited[i][j] = false;
			}
		}
		heuristic=new Heuristic(map.getCheckpoint(), map.getStart(), map.getGoal());
		for(Position  p:heuristic.getHeuristicTable().keySet()){
			Position.showPosition(p);
			System.out.println(heuristic.getHeuristicTable().get(p));
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

	public boolean hasReached(Position p) {
		if (getUPPosition(map.getStart()).equals(p) == true
				|| getDownPosition(map.getStart()).equals(p) == true
				|| getLeftPosition(map.getStart()).equals(p) == true
				|| getRightPosition(map.getStart()).equals(p) == true) {
			return true;
		} else
			return false;

	}

	public int Search(Position p) {
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
			visited[getDownPosition(map.getStart()).getRow()][getDownPosition(
					map.getStart()).getCol()] = true;

		}
		if (map.getChar(getLeftPosition(map.getStart())) == '.') {
			pq.add(getLeftPosition(map.getStart()));
			visited[getLeftPosition(map.getStart()).getRow()][getLeftPosition(
					map.getStart()).getCol()] = true;
		}
		if (map.getChar(getRightPosition(map.getStart())) == '.') {
			pq.add(getRightPosition(map.getStart()));
			visited[getRightPosition(map.getStart()).getRow()][getRightPosition(
					map.getStart()).getCol()] = true;
		}
		
		while (hasReached(p) == false) {
			Position newposition = pq.remove();
			move(newposition);
			Position temp = getUPPosition(newposition);
			if (map.getChar(temp) == '.'
					&& visited[temp.getRow()][temp.getCol()] == false) {
				pq.add(temp);
				moveUP();
				//map.showMap();
				hasfound = true;
				visited[temp.getRow()][temp.getCol()] = true;
				System.out.println();
			}
			temp = getDownPosition(newposition);
			if (map.getChar(temp) == '.'
					&& visited[temp.getRow()][temp.getCol()] == false) {
				pq.add(temp);
				moveDOWN();
				//map.showMap();
				hasfound = true;
				visited[temp.getRow()][temp.getCol()] = true;
				System.out.println();
			}
			temp = getLeftPosition(newposition);
			if (map.getChar(temp) == '.'
					&& visited[temp.getRow()][temp.getCol()] == false) {
				pq.add(temp);
				moveLEFT();
				//map.showMap();
				hasfound = true;
				visited[temp.getRow()][temp.getCol()] = true;
				System.out.println();
			}
			temp = getRightPosition(newposition);
			if (map.getChar(temp) == '.'
					&& visited[temp.getRow()][temp.getCol()] == false) {
				pq.add(temp);
				moveRIGHT();
				//map.showMap();
				hasfound = true;
				visited[temp.getRow()][temp.getCol()] = true;
				System.out.println();
			}
			/*
			 * not even a single move is possible
			 * Possible when
			 * 1. S is bounded by closed path on all sides
			 * 2. S has visited all the paths in its vicinity
			 */
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
		}
		return 1;
	}

	/*
	 * admissible heuristic function f(n)=h(n) + g(n) where g(n)=distance
	 * between current position of 'S' and checkpoint C and h(n) is the distance
	 * between checkpoint C and goal G At any point the next move is selected
	 * such that f(n) is less than its previous value and checkpoint C is
	 * selected such that it has highest value of f(n) to first the checkpoint
	 * which is farthest
	 */

	public void start() {
		//for(Position p:map.getCheckpoint())
			Search(map.getGoal());
	}

}
