package src;

import java.awt.image.BufferedImage;

public class Player extends ScreenElement {
	private String state;
	private String imageDir;
	private double scale;
	public Player(int x, int y, String id) {
		super(x, y, id);
		this.state = "idle";
	}
	
	public Player(int x, int y, String id, String state, String imageDir, double scale) {
		super(x, y, id);
		this.state = state;
		this.imageDir = imageDir;
		this.scale = scale;
		// TODO Auto-generated constructor stub
	}
	
	public String getState () {
		return state;
	}
	
	public String getImage() {
		return imageDir;
	}

	public double getScale() {
		return scale;
	}
	
}
