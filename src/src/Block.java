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
	 * @param x    The X position of the block
	 * @param y    The Y position of the block
	 * @param id   The id of this block to find it later
	 * @param type The type of block to be
	 */
	public Block(int x, int y, String id, String type, boolean canKill) {

		super(x, y, id);

		for (int i = 0; i < blockList.length; i++) {
			if (type.equals(blockList[i])) {
				blockCode = i;
				i = blockList.length;
			}
		}

		this.canKill = canKill;
		this.type = type;

		imageDir = "img/blocks/" + type + ".png";
		File tmpDir = new File(imageDir);
		if (tmpDir.exists()) {

		} else {
			imageDir = "img/blocks/" + type + ".gif";
		}
		height = 32;
		width = 32;
		rotation = 0;
	}// Block

	public Block(int x, int y, String id, String type, boolean canKill, int height, int width) {
		super(x, y, id);

		for (int i = 0; i < blockList.length; i++) {
			if (type.equals(blockList[i])) {
				blockCode = i;
				i = blockList.length;
			}
		}

		this.canKill = canKill;
		this.type = type;

		imageDir = "img/blocks/" + type + ".png";
		File tmpDir = new File(imageDir);
		if (tmpDir.exists()) {

		} else {
			imageDir = "img/blocks/" + type + ".gif";
		}
		this.height = height;
		this.width = width;
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

	public String getType() {
		return type;
	}

	public int getBlockCode() {
		return blockCode;
	}

	public boolean canKill() {
		return canKill;
	}

	public String[] getBlockList() {
		return blockList;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	
}// Block
