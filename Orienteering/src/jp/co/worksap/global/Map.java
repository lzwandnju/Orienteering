/**
 * 
 */
package jp.co.worksap.global;

import java.util.Scanner;

/**
 * @author Dell
 * 
 */
public class Map {

	private char[][] map;
	private int height;
	private int width;

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Map(int width, int height) {
		super();
		this.height = height;
		this.width = width;
		map = new char[height][width];
	}

	public void createMap() {
		Scanner sc = new Scanner(System.in);
		int i = 0;
		while (i < this.height) {

			String str = sc.next();
			try {
				if (str.length() > width) {
					throw new Exception();
				} else {
					map[i++] = str.toCharArray();
				}
			} catch (Exception e) {
				System.out.println("Input width is more that Specified("
						+ this.width + ") ...Please Try Again");
			}
		}
		sc.close();
	}

	public void showMap() {
		for (int i = 0; i < this.height; i++) {
			System.out.println(map[i]);
		}
	}

}
