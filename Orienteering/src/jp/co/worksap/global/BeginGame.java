package jp.co.worksap.global;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class BeginGame {

	private static Map map;
	private boolean[][] visited;
	private Heuristic heuristic;
	private int steps;
	LinkedList<Position> resultPath;

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
		steps = 0;
		resultPath = new LinkedList<Position>();
	}

	public void visitStart() {
		visited[map.getStart().getRow()][map.getStart().getCol()] = true;
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

	public void move(Position newposition) {

		if (map.getStart().equals(map.getGoal()))
			map.updateMap(new Position(map.getStart().getRow(), map.getStart()
					.getCol()), 'G');
		else {
			map.updateMap(new Position(map.getStart().getRow(), map.getStart()
					.getCol()), '.');
		}
		map.setStart(newposition);
		// Goal should not be moved from its position
		if (map.getChar(newposition) != 'G')
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
		PriorityQueue<Position> pq = new PriorityQueue<Position>();
		/*
		 * Temp denotes all the position in the vicinity of new position which
		 * are to be checked newposition is the current position of S
		 */
		Position temp = null, newposition;

		while (hasReached(p) == false) {
			if (pq.isEmpty() == true)
				newposition = map.getStart();
			else
				newposition = pq.remove();
			for (Movement m : Movement.values()) {
				temp = getPosition(newposition, m);
				if ((map.getChar(temp) != '#')
						&& visited[temp.getRow()][temp.getCol()] == false) {
					/*
					 * Check if the next movement will decrease the heuristic
					 * value or if there is a checkpoint there
					 */
					if (heuristic.heuristicFunction(temp, p, map.getGoal()) < heuristic
							.getHeuristicValue(p) || map.getChar(temp) == '@') {
						pq.add(temp);
						heuristic.setCheckPointHeuristic(temp);
						heuristic.setGoalHeuristic(temp);
						if (map.getChar(temp) == '@') {
							if (heuristic.heuristicFunction(temp, p,
									map.getGoal()) > heuristic
									.heuristicFunction(map.getStart(), p,
											map.getGoal())) {
								Position prev = map.getStart();
								move(temp);
								visitStart();
								steps++;
								resultPath.add(temp);
								move(prev);
								steps++;
								resultPath.add(temp);
							} else {
								move(temp);
								steps++;
								resultPath.add(temp);

							}

							visitStart();
							map.showMap();
							coverCheckPoint(temp);
							heuristic.getHeuristicTable().remove(temp);
							temp = null;
							break;
						}

					}
				}
			}
			if (pq.isEmpty() == false) {
				if (temp != null) {
					move(pq.element());
					visitStart();
					steps++;
					resultPath.add(pq.element());
					map.showMap();
				}
				// Thread.sleep(1000);
			} else {
				System.out.println("Queue Empty");
				Position nextBest = null;
				int nextBestHeuristic = 0;
				for (Movement m : Movement.values()) {
					if (map.getChar(getPosition(map.getStart(), m)) != '#'
							&& visited[getPosition(map.getStart(), m).getRow()][getPosition(
									map.getStart(), m).getCol()] == false) {
						nextBest = getPosition(map.getStart(), m);
						nextBestHeuristic = heuristic.heuristicFunction(
								getPosition(map.getStart(), m), p,
								map.getGoal());
						break;
					}

				}

				for (Movement m : Movement.values()) {
					Position tempPos = getPosition(map.getStart(), m);
					if (map.getChar(getPosition(map.getStart(), m)) != '#'
							&& visited[getPosition(map.getStart(), m).getRow()][getPosition(
									map.getStart(), m).getCol()] == false) {
						int tempVal = heuristic.heuristicFunction(tempPos, p,
								map.getGoal());
						if (tempVal <= nextBestHeuristic) {
							nextBest = tempPos;
							nextBestHeuristic = tempVal;
						}
					}

				}
				if (nextBest != null) {
					pq.add(nextBest);
					move(nextBest);
					visitStart();
					steps++;
					resultPath.add(nextBest);
					heuristic.setCheckPointHeuristic(nextBest);
					heuristic.setGoalHeuristic(nextBest);
					map.showMap();
				} else {
					for (Movement m : Movement.values()) {
						visited[getPosition(map.getStart(), m).getRow()][getPosition(
								map.getStart(), m).getCol()] = false;
					}
				}

			}
		}
		move(p);
		resultPath.add(p);
		steps++;
		visitStart();
		coverCheckPoint(map.getStart());
		heuristic.getHeuristicTable().remove(map.getStart());
		heuristic.setCheckPointHeuristic(map.getStart());
		heuristic.setGoalHeuristic(map.getStart());
		map.showMap();
		return 1;
	}

	public void SearchGoal() {
		PriorityQueue<Position> pq = new PriorityQueue<Position>();
		Position newPos, nextNewPos;
		while (hasReached(map.getGoal()) == false) {

			if (pq.isEmpty() == true)
				newPos = map.getStart();
			else {
				newPos = pq.remove();
			}
			for (Movement m : Movement.values()) {
				nextNewPos = getPosition(newPos, m);
				if (map.getChar(nextNewPos) != '#') {
					if (map.getChar(nextNewPos) != '#'
							&& visited[nextNewPos.getRow()][nextNewPos.getCol()] == false) {
						if (Position.distance(nextNewPos, map.getGoal()) < heuristic
								.getGoalHeuristic()) {
							heuristic.setGoalHeuristic(nextNewPos);
							pq.add(nextNewPos);

						}
					}
				}
			}

			if (pq.isEmpty() == false) {
				move(pq.element());
				resultPath.add(pq.element());
				steps++;
				visitStart();
				heuristic.setGoalHeuristic(map.getStart());
				map.showMap();
			} else {
				if (pq.isEmpty() == true) {
					System.out.println("Queue Empty");
					Position nextBest = null;
					int nextBestHeuristic = 0;
					for (Movement m : Movement.values()) {
						if (map.getChar(getPosition(map.getStart(), m)) != '#'
								&& visited[getPosition(map.getStart(), m)
										.getRow()][getPosition(map.getStart(),
										m).getCol()] == false) {
							nextBest = getPosition(map.getStart(), m);
							nextBestHeuristic = Position.distance(nextBest,
									map.getGoal());
							break;
						}
					}

					for (Movement m : Movement.values()) {
						Position tempPos = getPosition(map.getStart(), m);
						if (map.getChar(getPosition(map.getStart(), m)) != '#'
								&& visited[getPosition(map.getStart(), m)
										.getRow()][getPosition(map.getStart(),
										m).getCol()] == false) {
							int tempVal = Position.distance(nextBest,
									map.getGoal());
							if (tempVal <= nextBestHeuristic) {
								nextBest = tempPos;
								nextBestHeuristic = tempVal;
							}
						}

					}
					if (nextBest != null) {
						pq.add(nextBest);
						move(nextBest);
						visitStart();
						steps++;
						resultPath.add(nextBest);
						heuristic.setGoalHeuristic(nextBest);
						map.showMap();
					} else {
						for (Movement m : Movement.values()) {
							visited[getPosition(map.getStart(), m).getRow()][getPosition(
									map.getStart(), m).getCol()] = false;
						}
					}

				}
			}
		}
	}

	public void coverCheckPoint(Position p) {
		map.getCovered()[p.getRow()][p.getCol()] = true;
	}

	public void start() {

		/*
		 * Validate Map before making any moves 1. Start should not be
		 * surrounded by # on all sides 2. No Row/Column should be such that
		 * there is it forms two rectangles with Start on one side and
		 * Goal/Checkpoint on the other Side
		 */
		map.showMap();
		resultPath.add(map.getStart());

		for (Position p : map.getCheckpoint()) {
			if (map.getCovered()[p.getRow()][p.getCol()] == false) {
				System.out.print("Searching for ");
				Position.showPosition(p);
				InitVisited();
				visitStart();
				SearchCheckpoint(p);
			}
		}

		InitVisited();
		visitStart();
		System.out.println("Searching for Goal");
		SearchGoal();
		move(map.getGoal());
		resultPath.add(map.getGoal());
		System.out.print("Result path (x, y): ");
		int i = resultPath.size();
		for (Position p : resultPath) {
			System.out.print("( " + p.getCol() + ", " + p.getRow() + " )");
			if (--i > 0)
				System.out.print("==>");
		}
		System.out.println("Total Steps to Goal= " + (steps + 1));
	}
}
