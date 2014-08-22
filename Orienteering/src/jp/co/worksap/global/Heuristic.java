package jp.co.worksap.global;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class Heuristic implements Comparator<Position> {

	private Hashtable<Position, Integer> heuristicTable;
	private Position Goal;
	private int goalHeuristic;

	public Hashtable<Position, Integer> getHeuristicTable() {
		return heuristicTable;
	}

	public Heuristic(List<Position> checkpoint, Position Start, Position Goal) {
		heuristicTable = new Hashtable<Position, Integer>();
		this.Goal = Goal;
		for (Position p : checkpoint) {
			heuristicTable.put(p, heuristicFunction(Start, p, Goal));
		}
		goalHeuristic = Position.distance(Start, Goal);
	}

	public void setCheckPointHeuristic(Position Start) {
		for (Position p : heuristicTable.keySet()) {
			heuristicTable.put(p, heuristicFunction(Start, p, Goal));
		}
	}

	public int getGoalHeuristic() {
		return goalHeuristic;
	}

	public void setGoalHeuristic(Position startpos) {
		this.goalHeuristic = Position.distance(startpos, Goal);
	}

	/*
	 * admissible heuristic function f(n)=h(n) + g(n) where g(n)=distance
	 * between current position of 'S' and checkpoint C and h(n) is the distance
	 * between checkpoint C and goal G At any point the next move is selected
	 * such that f(n) is less than its previous value and checkpoint C is
	 * selected such that it has highest value of f(n) to first the checkpoint
	 * which is farthest
	 */

	public int heuristicFunction(Position startPos, Position checkPoint,
			Position Goal) {
		return (Position.distance(startPos, checkPoint) + Position.distance(
				checkPoint, Goal));
	}

	//Get heuristic value of a checkpoint
	public int getHeuristicValue(Position checkpoint) {
		try {
			return heuristicTable.get(checkpoint);
		} catch (NullPointerException e) {
			return -1;
		}
	}

	@Override
	public int compare(Position o1, Position o2) {
		// TODO Auto-generated method stub
		if (heuristicTable.get(o1) > heuristicTable.get(o2))
			return -1;
		else
			return 1;
		// return 0;
	}

}
