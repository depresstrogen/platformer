package src;

import java.io.File;

/**
 * Object that holds everything needed to display and use a block
 * 
 * @author Riley Power
 * @version April 19 2021
 */
public class Block extends ScreenElement {
	private String imageDir;
	private boolean canKill;
	/**
	 * Block constructor
	 * 
	 * @param x    The X position of the block
	 * @param y    The Y position of the block
	 * @param id   The id of this block to find it later
	 * @param type The type of block to be
	 */
	public Block(int x, int y, String id, String type, boolean canKill) {
		super(x, y, id);
		this.canKill = canKill;
		imageDir = "img/blocks/" + type + ".png";
		File tmpDir = new File(imageDir);
		if(tmpDir.exists()) {
			
		} else {
			imageDir = "img/blocks/" + type + ".gif";
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
	
	public boolean canKill() {
		return canKill;
	}
	
}// Block
