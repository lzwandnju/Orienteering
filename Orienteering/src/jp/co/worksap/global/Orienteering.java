/**
 * Main class for Orienteering application
 */
package jp.co.worksap.global;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dell
 *
 */
public class Orienteering {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map m=new Map(5,4);
		//m.createMap();
		//m.showMap();
		Hashtable<Character,List<Position>> hash=new Hashtable<Character, List<Position>>();
		List<Position> s=new LinkedList<Position>();
		s.add( new Position(3,4));
		s.add(new Position(5,6));
		hash.put('@', s);
		for(char ch:hash.keySet()){
			for(Position p:hash.get(ch)){
				System.out.println(p.getRow() + " "+ p.getCol());
				
			}
		}
		
		
	}

}
