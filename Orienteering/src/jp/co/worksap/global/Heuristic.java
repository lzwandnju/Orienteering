package jp.co.worksap.global;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class Heuristic implements Comparator<Position> {

	private Hashtable<Position, Integer> heuristicTable;
	Position Goal;
	public Hashtable<Position, Integer> getHeuristicTable() {
		return heuristicTable;
	}

	public Heuristic(List<Position> checkpoint, Position Start, Position Goal) {
		heuristicTable = new Hashtable<Position, Integer>();
		this.Goal=Goal;
		for (Position p : checkpoint) {
			heuristicTable.put(p, heuristicFunction(Start, p, Goal));
		}
	}

	public void updateHeuristic(Position Start){
		for (Position p : heuristicTable.keySet()) {
			heuristicTable.put(p, heuristicFunction(Start, p, Goal));
		}
	}
	
	public int heuristicFunction(Position startPos, Position checkPoint,
			Position Goal) {
		return (Position.distance(startPos, checkPoint) + Position.distance(
				checkPoint, Goal));
	}

	public int getHeuristicValue(Position checkpoint){
		return heuristicTable.get(checkpoint);
	}
	
	@Override
	public int compare(Position o1, Position o2) {
		// TODO Auto-generated method stub
		if(heuristicTable.get(o1)>heuristicTable.get(o2)) return -1;
		else return 1;
		//return 0;
	}

}
