package src;

import java.util.ArrayList;

/**
 * Places and removes blocks from screen, selects items and draws the cursor.
 * Anything to do with creation and editing of levels is found here
 * 
 * @author Riley Power
 * @version May 2, 2021
 */
public class LevelEditor {
	// IO Shit
	public Game game;
	public Screen screen;
	public MouseHandler mouse;
	public KeebHandler keeb;
	public GUIElements gooey;

	// Stroke ;)
	private boolean lastStrokeFlag = false;

	private int mouseWheelPos = 0;
	private int currentStroke;
	private int currentBlock = 0;
	private int scroll;
	public int rotation = 0;
	private int currentList = 0;

	// Flags
	private boolean undo = false;
	private boolean screenCleaned = true;

	// List Copies
	private String[] blockList = BlockTypes.blockList;
	private String[] enemyList = BlockTypes.enemyList;

	private String currentItem = blockList[mouseWheelPos];

	public LevelEditor(Game game, Screen screen, MouseHandler mouse, KeebHandler keeb, GUIElements gooey) {
		this.game = game;
		this.screen = screen;
		this.mouse = mouse;
		this.keeb = keeb;
		this.gooey = gooey;
	}

	public void levelEditor() {
		// Variables
		final int KEY_CTRL = 17;
		final int KEY_SHIFT = 16;
		scroll = game.getScroll();

		// Updates list type based on keyboard input
		if (keeb.getKey('1')) {
			currentList = 1;
		} else if (keeb.getKey('2')) {
			currentList = 2;
		} else if (keeb.getKey('3')) {
			currentList = 3;
		}

		// Removes cursor
		try {
			screen.removeID("cursor");
		} catch (Exception e) {
			// No Cursor
		}

		// CTRL Z
		if (keeb.getKey(KEY_CTRL) && keeb.getKey('Z') && !undo) {
			undoStroke();
			System.out.println("d");
		} else if (!keeb.getKey(KEY_CTRL) || !keeb.getKey('Z')) {
			undo = false;
		}

		// Apply Rotation
		if (keeb.getKey('R')) {
			rotation += 90;
			if (rotation >= 360) {
				rotation -= 360;
			}
			System.out.print("roatation" + rotation);
			keeb.setKey('R', false);
		}

		// Gets Mouse Wheel Inputs
		mouseWheel();

		// Does the action based on what buttons are pressed
		if (mouse.isMousePressed() && mouse.getMouseButton() == 'L' && keeb.getKey(KEY_SHIFT)) {
			removeBlocks();
		} else if (mouse.isMousePressed() && mouse.getMouseButton() == 'L' && keeb.getKey('Q')) {
			selectBlock();
		} else if (mouse.isMousePressed() && mouse.getMouseButton() == 'L') {
			paintBlocks();
		} else if (mouse.getMouseButton() == 'R' && mouse.isMousePressed()) {
			cursor();
		} else if (lastStrokeFlag) {
			// Increment the stroke
			currentStroke++;
			currentBlock = 0;
			lastStrokeFlag = false;
		}

	}// levelEditor

	/**
	 * Updates variables based on the mouse wheel's position
	 */
	public void mouseWheel() {
		if (mouse.getMouseWheel() == 'U') {
			mouseWheelPos--;
			mouse.resetMouseWheel();
		} else if (mouse.getMouseWheel() == 'D') {
			mouseWheelPos++;
			mouse.resetMouseWheel();
		}

		// Block
		if (currentList == 1) {

			if (mouseWheelPos < 0) {
				mouseWheelPos = blockList.length - 1;
			}
			if (mouseWheelPos > blockList.length - 1) {
				mouseWheelPos = 0;
			}

			currentItem = blockList[mouseWheelPos];

		} else if (currentList == 2) {
			// enemy

			if (mouseWheelPos < 0) {
				mouseWheelPos = enemyList.length - 1;
			}
			if (mouseWheelPos > enemyList.length - 1) {
				mouseWheelPos = 0;
			}

			currentItem = enemyList[mouseWheelPos];
		} else if (currentList == 3) {
			currentItem = "info";
		}
	}// mouseWheel

	/**
	 * Adds the block that is currently selected to the screen, as the type it is
	 * supposed to be
	 */
	public void paintBlocks() {
		final int KEY_CTRL = 17;
		final int KEY_SHIFT = 16;
		final int KEY_ALT = 18;
		try {
			screen.removeID("cursor");
		} catch (Exception e) {
			// No Cursor
		}

		boolean canKill = false;
		String[] lethalBlocks = BlockTypes.lethalBlocks;
		// If in the block list
		if (currentList == 1) {
			// Sees if the block is lethal
			for (int i = 0; i < lethalBlocks.length; i++) {
				if (currentItem.equals(lethalBlocks[i])) {
					canKill = true;
					i = lethalBlocks.length;
				}
			}

			if (keeb.getKey('E')) {
				// Moving Blocks
				MovingBlock block = new MovingBlock((int) mouse.getX() + scroll, (int) mouse.getY(),
						"MouseAdded " + currentStroke, currentItem, canKill);
				// Offsets by 32 pixels
				if (game.getFlag("grid")) {
					block = new MovingBlock(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
							"MouseAdded " + currentStroke + " " + currentBlock, currentItem, canKill);
				}
				block.setRotation(rotation);
				screen.add(block);
			} else if (keeb.getKey(KEY_ALT)) {
				// Background Blocks
				BackgroundBlock block = new BackgroundBlock((int) mouse.getX() + scroll, (int) mouse.getY(),
						"MouseAdded " + currentStroke, currentItem, canKill);

				// Offsets by 32 pixels
				if (game.getFlag("grid")) {
					block = new BackgroundBlock(((int) (mouse.getX() + scroll) >> 5) << 5,
							((int) mouse.getY() >> 5) << 5, "MouseAdded " + currentStroke + " " + currentBlock,
							currentItem, canKill);
				}
				block.setRotation(rotation);
				screen.add(block);
			} else {
				// New block is offset to start at the top left of the mouse
				Block block = new Block((int) mouse.getX() + scroll, (int) mouse.getY(), "MouseAdded " + currentStroke,
						currentItem, canKill);
				// Offsets by 32 pixels
				if (game.getFlag("grid")) {
					block = new Block(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
							"MouseAdded " + currentStroke + " " + currentBlock, currentItem, canKill);
				}
				block.setRotation(rotation);
				screen.add(block);
			}
			currentBlock++;
			System.out.print(currentBlock);
			screenCleaned = false;
		} else if (currentList == 2) {
			// Enemy List
			FireHopper hop = new FireHopper((int) mouse.getX() + scroll, (int) mouse.getY(),
					"MouseAdded " + currentStroke);
			if (game.getFlag("grid")) {
				hop = new FireHopper(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
						"MouseAdded " + currentStroke + " " + currentBlock);
			}
			currentBlock++;
			screenCleaned = false;
			screen.add(hop);
		} else if (currentItem.equals("info")) {
			// InfoList
			LevelInfo info = (LevelInfo) screen.getScreenElement("lvlinfo");
			screen.removeID("lvlinfo");
			if (game.getFlag("grid")) {
				info.setSpawnX(mouse.getX() + scroll >> 5 << 5);
				info.setSpawnY(mouse.getY() >> 5 << 5);
			} else {
				info.setSpawnX(mouse.getX() + scroll);
				info.setSpawnY(mouse.getY());
			}
			screen.add(info);
		}
		// If the CTRL key isn't pressed, release the mouse button (Forcibly)
		if (!keeb.getKey(KEY_CTRL)) {
			mouse.removePressedFlag();

		}
		// Shows the mouse button is still held
		lastStrokeFlag = true;

	}// paintBlocks

	/**
	 * Adds a block that follows the mouse to aid in positioning elements
	 */
	public void cursor() {
		// Block List
		if (currentList == 1) {
			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) (mouse.getX() + scroll), (int) mouse.getY(), "cursor", currentItem, false);

			if (game.getFlag("grid")) {
				block = new Block((int) mouse.getX() + scroll >> 5 << 5, (int) mouse.getY() >> 5 << 5, "cursor",
						currentItem, false);
			}
			block.setRotation(rotation);
			screen.add(block);
		} else if (currentList == 2) { // Enemy List
			FireHopper hop = new FireHopper((int) mouse.getX() + scroll, (int) mouse.getY() + 19, "cursor");
			if (game.getFlag("grid")) {
				hop = new FireHopper((int) mouse.getX() + scroll >> 5 << 5, (int) (mouse.getY() >> 5 << 5) + 19,
						"cursor");
			}
			screen.add(hop);
		} else if (currentItem.equals("info")) { // Info Block

			try {
				screen.removeID("cursor");
			} catch (Exception e) {

			}
			LevelInfo info = new LevelInfo();
			info.setID("cursor");
			if (game.getFlag("grid")) {
				info.setSpawnX(mouse.getX() + scroll >> 5 << 5);
				info.setSpawnY(mouse.getY() >> 5 << 5);
			} else {
				info.setSpawnX(mouse.getX() + scroll);
				info.setSpawnY(mouse.getY());
			}
			screen.add(info);
		}

	}// cursor

	/**
	 * Removes the top most block that has been clicked on
	 */
	public void removeBlocks() {
		final int KEY_CTRL = 17;
		// Loop through the block array back to front and removing the first block found
		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");
		for (int i = blocks.size() - 1; i >= 0; i--) {
			Block block = (Block) blocks.get(i);
			if (mouse.getX() + scroll >= block.getX() && mouse.getX() + scroll <= block.getX() + block.getWidth()
					&& mouse.getY() >= block.getY() && mouse.getY() <= block.getY() + block.getHeight()) {
				System.out.println("delete");

				// Removes duplicate blocks
				if (!screenCleaned) {
					screen.screenClean();
					screenCleaned = true;
				}

				screen.removeID(block.getID());

				i = -1;
			}
		}

		// Loop through the enemy array back to front and removing the first block found
		ArrayList<ScreenElement> enemies = screen.getAllOfType("enemy");
		System.out.println("delete");
		for (int i = enemies.size() - 1; i >= 0; i--) {
			Enemy enemy = (Enemy) enemies.get(i);
			if (mouse.getX() + scroll >= enemy.getX() && mouse.getX() + scroll <= enemy.getX() + enemy.getWidth()
					&& mouse.getY() >= enemy.getY() && mouse.getY() <= enemy.getY() + enemy.getHeight()) {
				System.out.println("delete");

				// Removes duplicate blocks
				if (!screenCleaned) {
					screen.screenClean();
					screenCleaned = true;
				}

				screen.removeID(enemy.getID());

				i = -1;
			}
		}

		// If the CTRL key isn't pressed, release the mouse button
		if (!keeb.getKey(KEY_CTRL)) {
			mouse.removePressedFlag();
		}
	}

	/**
	 * Finds the block that has been clicked on, and puts its info in the corner of
	 * the screen using GUIElements
	 */
	public void selectBlock() {
		final int KEY_CTRL = 17;
		// Loop through the block array back to front and showing info of the first
		// block found
		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");
		for (int i = blocks.size() - 1; i >= 0; i--) {
			Block block = (Block) blocks.get(i);
			if (mouse.getX() + scroll >= block.getX() && mouse.getX() + scroll <= block.getX() + block.getWidth()
					&& mouse.getY() >= block.getY() && mouse.getY() <= block.getY() + block.getHeight()) {
				gooey.setSelect(block);
			}
		}

		// Loop through the enemies array back to front and showing info of the first
		// block found
		ArrayList<ScreenElement> enemies = screen.getAllOfType("enemy");
		for (int i = enemies.size() - 1; i >= 0; i--) {
			Enemy enemy = (Enemy) enemies.get(i);
			if (mouse.getX() + scroll >= enemy.getX() && mouse.getX() + scroll <= enemy.getX() + enemy.getWidth()
					&& mouse.getY() >= enemy.getY() && mouse.getY() <= enemy.getY() + enemy.getHeight()) {
				gooey.setSelect(enemy);
			}
		}

		// Sees if the player was clicked on
		Player player = (Player) screen.getScreenElement("Player");

		if (mouse.getX() + scroll >= player.getX() && mouse.getX() + scroll <= player.getX() + 32
				&& mouse.getY() >= player.getY() && mouse.getY() <= player.getY() + 32) {

			gooey.setSelect(player);

		}

		// Sees if the LevelInfo holder was clicked on
		LevelInfo info = (LevelInfo) screen.getScreenElement("lvlinfo");
		if (mouse.getX() + scroll >= info.getSpawnX() && mouse.getX() + scroll <= info.getSpawnX() + 32
				&& mouse.getY() >= info.getSpawnY() && mouse.getY() <= info.getSpawnY() + 32) {
			gooey.setSelect(info);

		}

		mouse.removePressedFlag();
	}// selectBlock

	/**
	 * Removes the previous stroke, no matter how many blocks it was
	 */
	public void undoStroke() {
		currentStroke--;
		screen.removeAllID("MouseAdded " + currentStroke);
		// Makes it so it is only run once per key press
		undo = true;
	}// undoStroke

}// LevelEditor
