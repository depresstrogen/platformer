package src;

import java.awt.Image;
import java.awt.Toolkit;

public class FireHopper extends Enemy {

	private int velocity = 0;
	private int spawnX;
	private int spawnY;
	private long lastLand = 0;
	
	private String state = "up";
	
	public FireHopper(int x, int y, String id) {
		super(x, y, id);
		spawnX = x;
		spawnY = y;
		
		
		
	}
	
	public void update() {
		int timer = 1000;
		if(velocity == 0 && spawnY == this.getY() && lastLand < System.currentTimeMillis() - timer) {
			velocity = -20;
		}
		if (velocity < 0) {
			state = "up";
		} else {
			state = "down";
		}
		
		velocity ++;
		this.setY(this.getY() + velocity);
		
		if(spawnY < this.getY()) {
			if (lastLand < System.currentTimeMillis() - timer) {
				lastLand = System.currentTimeMillis();
			}
			
			state = "up";
			this.setY(spawnY);
			velocity = 0;
		}
	}
	
	public String getState() {
		return state;
	}
	
}
