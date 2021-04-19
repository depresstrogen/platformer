package src;

/**
 * Object that holds everything needed to display and use a block
 * 
 * @author Riley Power
 * @version April 19 2021
 */
public class Block extends ScreenElement {
	private String imageDir;

	/**
	 * Block constructor
	 * 
	 * @param x    The X position of the block
	 * @param y    The Y position of the block
	 * @param id   The id of this block to find it later
	 * @param type The type of block to be
	 */
	public Block(int x, int y, String id, String type) {
		super(x, y, id);
		if (type.equals("ground")) {
			imageDir = "img/blocks/ground.png";
		}
	}// Block

	/**
	 * Returns the directory of the image this block corresponds to
	 * 
	 * @return imageDir
	 */
	public String getImage() {
		return imageDir;
	}// getImage
}// Block
