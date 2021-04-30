package src;

import java.awt.Image;

public class Enemy extends ScreenElement {

	private int width = 32;
	private int height = 32;

	
	public Enemy(int x, int y, String id) {
		super(x, y, id);
		// TODO Auto-generated constructor stub
	}
	public Enemy(int x, int y, String id, int height, int width) {
		super(x, y, id);
		this.width = width;
		this.height = height;
		// TODO Auto-generated constructor stub
	}
	/**
	 * Returns the width of the block
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}// getWidth

	/**
	 * Returns the height of the block
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}// getHeight
	

}
