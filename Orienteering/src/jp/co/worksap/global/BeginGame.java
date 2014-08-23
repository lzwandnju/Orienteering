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
		showCheckPointDetails(heuristic);
	}

	public static void showCheckPointDetails(Heuristic heuristic) {
		for (Position p : heuristic.getHeuristicTable().keySet()) {
			Position.showPosition(p);
			System.out.println("Heuristic Value= "
					+ heuristic.getHeuristicTable().get(p));
		}
	}

	/*
	 * Making all the nodes as not visited
	 */
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

	public void move(Position newposition, char ch) {
		map.updateMap(new Position(map.getStart().getRow(), map.getStart()
				.getCol()), '.');
		map.setStart(newposition);
		// Goal should not be moved from its position
		if (ch != 'G')
			map.updateMap(newposition, 'S');
	}

	/*
	 * Get the Position of S after Movement M from Position P
	 */
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

	public int SearchCheckpoint(Position p) {
		visited[map.getStart().getRow()][map.getStart().getCol()] = true;
		PriorityQueue<Position> pq = new PriorityQueue<Position>();
		Position temp, newposition;
		boolean reluctantPath = false;
		while (hasReached(p) == false) {
			boolean hasFound = false;
			if (pq.isEmpty() == true)
				newposition = map.getStart();
			else
				newposition = pq.remove();
			for (Movement m : Movement.values()) {
				temp = getPosition(newposition, m);
				if ((map.getChar(temp) != '#')
						&& visited[getPosition(temp, m).getRow()][getPosition(
								temp, m).getCol()] == false) {
					/*
					 * CHeck if the next movement will decrease the heuristic
					 * value or if there is a checkpoint there
					 */
					if (heuristic.heuristicFunction(temp, p, map.getGoal()) <= heuristic
							.getHeuristicValue(p) || map.getChar(temp) == '@') {
						pq.add(temp);
						hasFound = true;
						heuristic.setCheckPointHeuristic(temp);
						heuristic.setGoalHeuristic(temp);
						if (map.getChar(temp) == '@') {
							move(temp, map.getChar(temp));
							visited[temp.getRow()][temp.getCol()] = true;
							map.showMap();
							coverCheckPoint(temp);
							heuristic.getHeuristicTable().remove(temp);
							break;
						}
					}
				}
			}
			try {

				if (map.getCovered()[map.getStart().getRow()][map.getStart()
						.getCol()] == false) {
					move(pq.element(), map.getChar(pq.element()));
					visited[pq.element().getRow()][pq.element().getCol()] = true;
					map.showMap();
				}
				// Thread.sleep(1000);
			} catch (NullPointerException e) {
				System.out.println("Queue Empty");
			}
			/*
			 * not even a single move is possible Possible when 1. S is bounded
			 * by closed path on all sides 2. S has visited all the paths in its
			 * vicinity
			 */
			if (hasFound == false) {
				for (Movement m : Movement.values()) {
					if (map.getChar(getPosition(newposition, m)) == '.') {
						if (visited[getPosition(newposition, m).getRow()][getPosition(
								newposition, m).getCol()] == false) {
							reluctantPath = true;
						}
						hasFound = true;
					}
				}
				if (reluctantPath == false && hasFound == true) {
					InitVisited();
					visited[map.getStart().getRow()][map.getStart().getCol()] = true;
				} else {
					if (hasFound == false)
						return -1;
					;
				}
			}
		}
		return 1;
	}

	public void coverCheckPoint(Position p) {
		map.getCovered()[p.getRow()][p.getCol()] = true;
	}

	public void start() {
		map.showMap();
		for (Position p : map.getCheckpoint()) {
			if (map.getCovered()[p.getRow()][p.getCol()] == false) {
				System.out.print("Searching for ");
				Position.showPosition(p);
				SearchCheckpoint(p);
				break;
			}

		}
		InitVisited();
		visited[map.getStart().getRow()][map.getStart().getCol()] = true;
		// Search(map.getGoal());
	}
}
