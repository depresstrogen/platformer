package src;

/**
 * The block class but speed
 * 
 * @author Riley Power
 * @version May 2, 2021
 */
public class MovingBlock extends Block {

	public boolean vertical = false;
	public boolean direction = false;
	public int speed = 3;

	/**
	 * Block constructor
	 * 
	 * @param x       The X position of the block
	 * @param y       The Y position of the block
	 * @param id      The id of this block to find it later
	 * @param type    The type of block to be
	 * @param canKill If the block is lethal to the player or not
	 */
	public MovingBlock(int x, int y, String id, String type, boolean canKill) {
		super(x, y, id, type, canKill);
	}// MovingBlock

	/**
	 * Sets the direction the platform should move
	 * 
	 * @param direction
	 */
	public void setDirection(boolean direction) {
		vertical = direction;
	}// setDirection

	/**
	 * Returns the direction the block is moving
	 * 
	 * @return vertical
	 */
	public boolean getDirection() {
		return vertical;
	}// getDirection

	/**
	 * Sets the speed the block should move at (Pixels per second)
	 * 
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}// setSpeed

	/**
	 * Retruns the speed of the block
	 * 
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}// getSpeed

	/**
	 * Updates the blocks position
	 */
	public void update() {
		super.setX(super.getX() + speed);
	}// update
}// MovingBlock
