package src;

/**
 * Object used to hold information about the current level
 * 
 * @author Riley Power
 * @version May 2, 2021
 */
public class LevelInfo extends ScreenElement {
	// Current stroke so CTRL Z works after a load
	private int currentStroke = 0;
	// The Player's starting coordinates
	private int playerStartX = 0;
	private int playerStartY = 0;

	/**
	 * LevelInfo constructor
	 */
	public LevelInfo() {
		// Required to be a ScreenElement
		super(0, 0, "lvlinfo");
	}// LevelInfo

	/**
	 * Sets the currentStroke to the value given
	 * 
	 * @param stroke The value to set the currentStroke to
	 */
	public void setCurrentStroke(int stroke) {
		currentStroke = stroke;
	}// setCurrentStroke

	/**
	 * gets the last known stroke number
	 * 
	 * @return currentStroke
	 */
	public int getCurrentStroke() {
		return currentStroke;
	}// getCurrentStroke

	/**
	 * Returns the spawn X coordinate of the player
	 * 
	 * @return spawnX
	 */
	public int getSpawnX() {
		return playerStartX;
	}// getSpawnX

	/**
	 * Returns the spawn X coordinate of the player
	 * 
	 * @return spawnX
	 */
	public int getSpawnY() {
		return playerStartY;
	}// getSpawnY

	/**
	 * Sets where the player should spawn on the X axis
	 * 
	 * @param coord The coordinate to set
	 */
	public void setSpawnX(int coord) {
		playerStartX = coord;
	}// setSpawnY

	/*
	 * Sets where the player should spawn on the X axis
	 * 
	 * @param coord The coordinate to set
	 */
	public void setSpawnY(int coord) {
		playerStartY = coord;
	}// setSpawnY

}// LevelInfo
