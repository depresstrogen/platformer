package src;

public class MovingBlock extends Block{
	
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
		super(x,y,id,type,canKill);
	}
	
	public void setDirection (boolean direction) {
		vertical = direction;
	}
	
	public boolean getDirection () {
		return vertical;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getSpeed() {
		return speed;
	}
	public void update() {
		super.setX(super.getX() + speed);
	}
}
