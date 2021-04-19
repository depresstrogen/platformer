package src;

/**
 * Holds everything needed to display and work with the player on screen
 * 
 * @author Riley Power
 * @version April 19 2021
 */
public class Player extends ScreenElement {
	// State of the player (idle, running, etc)
	private String state;
	// The directory of the image
	private String imageDir;
	// How much to scale the image (multiply)
	private double scale;

	/**
	 * Player constructor class
	 * 
	 * @param x  The x coordinate of the player
	 * @param y  The y coordinate of the player
	 * @param id The id to reference this player later
	 */
	public Player(int x, int y, String id) {
		super(x, y, id);
		this.state = "idle";
	}// Player

	/**
	 * Player constructor class
	 * 
	 * @param x        The x coordinate of the player
	 * @param y        The y coordinate of the player
	 * @param id       The id to reference this player later
	 * @param state    The starting state of the player
	 * @param imageDir The image to show the player as
	 * @param scale    How much to scale the player
	 */
	public Player(int x, int y, String id, String state, String imageDir, double scale) {
		super(x, y, id);
		this.state = state;
		this.imageDir = imageDir;
		this.scale = scale;
	}// Player

	/**
	 * Returns the current state of the player
	 * 
	 * @return state
	 */
	public String getState() {
		return state;
	}// getState

	/**
	 * Returns the directory of the player's image
	 * 
	 * @return imageDir
	 */
	public String getImage() {
		return imageDir;
	}// getImage

	/**
	 * Returns how much to scale the image
	 * 
	 * @return scale
	 */
	public double getScale() {
		return scale;
	}// getScale
}// Player
