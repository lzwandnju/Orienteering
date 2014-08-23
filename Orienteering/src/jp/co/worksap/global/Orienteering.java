/**
 * Main class for Orienteering application
 */
package jp.co.worksap.global;

import java.util.Scanner;

/**
 * @author Dell
 * 
 */
public class Orienteering {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Enter width and height");
		Scanner sc = new Scanner(System.in);
		int w = sc.nextInt();
		int h = sc.nextInt();
		new BeginGame(w, h).start();
		sc.close();
	}

}
