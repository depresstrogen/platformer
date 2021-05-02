package src;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Class for the FireHopper object
 * 
 * @author Riley Power
 * @version May 2, 2021
 */
public class FireHopper extends Enemy {

	private int velocity = 0;
	// Time the block last landed
	private long lastLand = 0;
	private int spawnX;
	private int spawnY;

	private String state = "up";

	/**
	 * @param x  The X Coordinate of the Fire Hopper
	 * @param y  The Y Coordinate of the Fire Hopper
	 * @param id The id to reference the Fire Hopper
	 */
	public FireHopper(int x, int y, String id) {
		// Variable Setup
		super(x, y, id);
		spawnX = x;
		spawnY = y;
	}

	/**
	 * Updates variables of the Fire Hopper based on the current time;
	 */
	public void update() {
		int timer = 1000;
		// Checks time and if youre stopped
		if (velocity == 0 && spawnY == this.getY() && lastLand < System.currentTimeMillis() - timer) {
			velocity = -20;
		}
		if (velocity < 0) {
			state = "up";
		} else {
			state = "down";
		}
		velocity++;

		this.setY(this.getY() + velocity);
		// Stop fire hopper if youre at spawn
		if (spawnY < this.getY()) {
			if (lastLand < System.currentTimeMillis() - timer) {
				lastLand = System.currentTimeMillis();
			}

			state = "up";
			this.setY(spawnY);
			velocity = 0;
		}
	}// update

	/**
	 * Returns the current state of the FireHopper
	 * 
	 * @return state;
	 */
	public String getState() {
		return state;
	}// getState

}// FireHopper
