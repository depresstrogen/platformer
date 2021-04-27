package src;

import java.io.File;

/**
 * Object that holds everything needed to display and use a block
 * 
 * @author Riley Power
 * @version April 26 2021
 */
public class Block extends ScreenElement {
	private String imageDir;
	private boolean canKill;
	private String type;
	private int blockCode;
	private int height;
	private int width;

	private int rotation;

	private String[] blockList = BlockTypes.blockList;

	public Block() {
		super(0, 0, "dummy");
	}

	/**
	 * Block constructor
	 * 
	 * @param x       The X position of the block
	 * @param y       The Y position of the block
	 * @param id      The id of this block to find it later
	 * @param type    The type of block to be
	 * @param canKill If the block is lethal to the player or not
	 */
	public Block(int x, int y, String id, String type, boolean canKill) {
		// Sets obvious variables
		super(x, y, id);
		this.canKill = canKill;
		this.type = type;

		// Sets the block code according to the lookup table
		for (int i = 0; i < blockList.length; i++) {
			if (type.equals(blockList[i])) {
				blockCode = i;
				i = blockList.length;
			}
		}

		// See if the image is a png or gif
		imageDir = "img/blocks/" + type + ".png";
		File tmpDir = new File(imageDir);
		if (tmpDir.exists()) {
		} else {
			imageDir = "img/blocks/" + type + ".gif";
		}

		// Sets Defaults
		height = 32;
		width = 32;
		rotation = 0;
	}// Block

	/**
	 * Block constructor with specified height and width
	 * 
	 * @param x       The X position of the block
	 * @param y       The Y position of the block
	 * @param id      The id of this block to find it later
	 * @param type    The type of block to be
	 * @param canKill If the block is lethal to the player or not
	 * @param height  Specify the height if it is not 32
	 * @param width   Specify the width if it is not 32
	 */
	public Block(int x, int y, String id, String type, boolean canKill, int height, int width) {
		// Sets obvious variables
		super(x, y, id);
		this.canKill = canKill;
		this.type = type;
		this.height = height;
		this.width = width;

		// Sets the block code according to the lookup table
		for (int i = 0; i < blockList.length; i++) {
			if (type.equals(blockList[i])) {
				blockCode = i;
				i = blockList.length;
			}
		}

		// See if the image is a png or gif
		imageDir = "img/blocks/" + type + ".png";
		File tmpDir = new File(imageDir);
		if (tmpDir.exists()) {
		} else {
			imageDir = "img/blocks/" + type + ".gif";
		}

		// Sets Defaults
		rotation = 0;
	}// Block

	/**
	 * Returns the directory of the image this block corresponds to
	 * 
	 * @return imageDir
	 */
	public String getImage() {
		return imageDir;
	}// getImage

	/**
	 * Returns the type of block that the block is
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}// getType

	/**
	 * Returns the code that identifies the block on the lookup table
	 * 
	 * @return blockCode
	 */
	public int getBlockCode() {
		return blockCode;
	}// getBlockCode

	/**
	 * Returns if the block can kill the player or not
	 * 
	 * @return canKill
	 */
	public boolean canKill() {
		return canKill;
	}// canKill

	/**
	 * Returns the blockList
	 * 
	 * @return blockList
	 */
	public String[] getBlockList() {
		return blockList;
	}// getBlockList

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

	/**
	 * Returns how much to rotate the block clockwise in degrees
	 * 
	 * @return rotation
	 */
	public int getRotation() {
		return rotation;
	}// getRotation

	/**
	 * Sets how much to rotate the block clockwise in degrees
	 * 
	 * @param rotation The amount the block should be rotated by
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}// setRotation

}// Block
