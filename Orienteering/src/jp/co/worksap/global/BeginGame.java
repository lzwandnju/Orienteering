package jp.co.worksap.global;

import java.util.PriorityQueue;

public class BeginGame {

	private static Map map;
	private boolean[][] visited;
	private Heuristic heuristic;

	enum Movement {
		UP, DOWN, LEFT, RIGHT
	}

	public BeginGame(int w, int h) {
		map = new Map(w, h);
		visited = new boolean[h][w];
		InitVisited();
		heuristic = new Heuristic(map.getCheckpoint(), map.getStart(),
				map.getGoal());
		for (Position p : heuristic.getHeuristicTable().keySet()) {
			Position.showPosition(p);
			// System.out.println(heuristic.getHeuristicTable().get(p));
		}
	}

	public void InitVisited() {
		for (int i = 0; i < map.getHeight(); i++) {
			for (int j = 0; j < map.getWidth(); j++) {
				visited[i][j] = false;
			}
		}
	}

	/*
	 * Place appropriate character where S was initially and then place S in new
	 * position
	 */

	public void move(Movement m, char ch) {
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), ch);
		switch (m) {
		case UP: {
			map.setStart(new Position(map.getStart().getRow() - 1, map
					.getStart().getCol()));
			break;
		}
		case DOWN: {
			map.setStart(new Position(map.getStart().getRow() + 1, map
					.getStart().getCol()));
			break;
		}
		case LEFT: {
			map.setStart(new Position(map.getStart().getRow(), map.getStart()
					.getCol() - 1));
			break;
		}
		case RIGHT: {
			map.setStart(new Position(map.getStart().getRow(), map.getStart()
					.getCol() + 1));
			break;
		}
		}
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), 'S');

	}

	public void move(Position newposition, char ch) {
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), ch);
		map.setStart(newposition);
		//Goal should not be moved from its position 
		if(ch!='G') map.updateMap(newposition, 'S');
	}

	public Position getPosition(Position p, Movement m) {
		switch (m) {
		case UP: {
			return new Position(p.getRow() - 1, p.getCol());
		}
		case DOWN: {
			return new Position(p.getRow() + 1, p.getCol());
		}
		case LEFT: {
			return new Position(p.getRow(), p.getCol() - 1);
		}
		case RIGHT: {
			return new Position(p.getRow(), p.getCol() + 1);
		}
		}
		return null;
	}

	public boolean hasReached(Position p) {
		if (getPosition(map.getStart(), Movement.UP).equals(p) == true
				|| getPosition(map.getStart(), Movement.DOWN).equals(p) == true
				|| getPosition(map.getStart(), Movement.LEFT).equals(p) == true
				|| getPosition(map.getStart(), Movement.RIGHT).equals(p) == true) {
			return true;
		} else
			return false;

	}

	/*
	 * A* algorithm for finding optimal path to the checkpoint and then the Goal
	 * In between if any checkpoint is encountered it is take care Basic idea
	 * here is that only when the heuristic value of the next move is less than
	 * the heuristic value of current position then only that move is made
	 */

	
	public int Search(Position p) {

		boolean hasFound = false;
		/*
		 * this path is taken when there is no position which will give less
		 * value of the admissible heuristic function
		 */
		boolean reluctantPath = false;
		visited[map.getStart().getRow()][map.getStart().getCol()] = true;
		PriorityQueue<Position> pq = new PriorityQueue<Position>();
		Position temp;

		while (hasReached(p) == false) {

			if (pq.isEmpty() == true) {

				for (Movement m : Movement.values()) {
					temp = getPosition(map.getStart(), m);
					if (map.getChar(temp) != '#') {
						if (map.getChar(temp) == '@') {
							pq.add(temp);
							coverCheckPoint(temp);
							map.showMap();
							move(m, '.');
							heuristic.updateHeuristic(map.getStart(), '@');
							visited[temp.getRow()][temp.getCol()] = true;
						} else {
							if (heuristic.getHeuristicValue(p) != -1) {
								if (heuristic.heuristicFunction(temp, p,
										map.getGoal(), map.getChar(temp)) <= heuristic
										.getHeuristicValue(p)
										|| reluctantPath == true) {
									pq.add(temp);
									map.showMap();
									move(m, map.getChar(temp));
									heuristic.updateHeuristic(map.getStart(),
											map.getChar(temp));
									visited[temp.getRow()][temp.getCol()] = true;
								}
							}

						}
					}
				}

			} else {
				Position newposition = pq.remove();
				// move(newposition);
				for (Movement m : Movement.values()) {
					temp = getPosition(newposition, m);
					if ((map.getChar(temp) != '#')
							&& visited[getPosition(temp, m).getRow()][getPosition(
									temp, m).getCol()] == false) {
						/*
						 * If a checkpoint is found in the vicinity then first
						 * take that
						 */
						if (map.getChar(temp) == '@') {

							pq.add(temp);
							move(m, '.');
							coverCheckPoint(temp);
							heuristic.updateHeuristic(map.getStart(), '@');
							map.showMap();
							hasFound = true;
							visited[temp.getRow()][temp.getCol()] = true;

						} else {
							if (heuristic.heuristicFunction(temp, p,
									map.getGoal(), map.getChar(temp)) <= heuristic
									.getHeuristicValue(p)
									|| reluctantPath == true) {
								pq.add(temp);
								move(m, map.getChar(temp));
								heuristic.updateHeuristic(map.getStart(),
										map.getChar(temp));
								map.showMap();
								hasFound = true;
								visited[temp.getRow()][temp.getCol()] = true;
							}

						}

					}
				}

				/*
				 * not even a single move is possible Possible when 1. S is
				 * bounded by closed path on all sides 2. S has visited all the
				 * paths in its vicinity
				 */
				if (hasFound == false) {
					for (Movement m : Movement.values()) {
						if (map.getChar(getPosition(newposition, m)) == '.')
							hasFound = true;
					}
					if (hasFound == true) {
						for (Movement m : Movement.values()) {
							if (visited[getPosition(map.getStart(), m).getRow()][getPosition(
									map.getStart(), m).getCol()] == false) {
								reluctantPath = true;
								break;
							}
						}
						if (reluctantPath == false) {
							InitVisited();
							visited[map.getStart().getRow()][map.getStart()
									.getCol()] = true;
						}
					}
				}
			}
		}

		move(p, '.');
		map.showMap();
		return 1;
	}

	public void coverCheckPoint(Position p) {
		map.getCovered()[p.getRow()][p.getCol()] = true;
	}

	public void start() {
		map.showMap();
		for (Position p : map.getCheckpoint()) {
			if (map.getCovered()[p.getRow()][p.getCol()] == false)
				Search(p);
		}
		InitVisited();
		visited[map.getStart().getRow()][map.getStart().getCol()] = true;
		Search(map.getGoal());

	}

}
