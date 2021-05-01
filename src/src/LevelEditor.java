package src;

import java.util.ArrayList;

public class LevelEditor {
	public Game game;
	public Screen screen;
	public MouseHandler mouse;
	public KeebHandler keeb;
	public GUIElements gooey;

	private boolean lastStrokeFlag = false;

	private int mouseWheelPos = 0;

	private int currentStroke;
	private int currentBlock = 0;
	private int scroll;
	private boolean undo = false;

	public int rotation = 0;

	private boolean screenCleaned = true;

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
		try {
			screen.removeID("cursor");
		} catch (Exception e) {
			// No Cursor
		}
		Block block = new Block();
		blockList = BlockTypes.blockList;
		final int KEY_CTRL = 17;
		final int KEY_SHIFT = 16;
		scroll = game.getScroll();

		if (keeb.getKey(KEY_CTRL) && keeb.getKey('Z') && !undo) {
			undoStroke();
			System.out.println("d");
		} else if (!keeb.getKey(KEY_CTRL) || !keeb.getKey('Z')) {
			undo = false;
		}

		if (keeb.getKey('R')) {
			rotation += 90;
			if (rotation >= 360) {
				rotation -= 360;
			}
			System.out.print("roatation" + rotation);
		}
		keeb.setKey('R', false);

		if (true) {
			int wheelCap = blockList.length + enemyList.length;
			if (mouse.getMouseWheel() == 'U') {
				mouseWheelPos--;
				mouse.resetMouseWheel();
			} else if (mouse.getMouseWheel() == 'D') {
				mouseWheelPos++;
				mouse.resetMouseWheel();
			}

			if (mouseWheelPos < 0) {
				mouseWheelPos = wheelCap;
			}
			if (mouseWheelPos > wheelCap) {
				mouseWheelPos = 0;
			}
			// Block
			if (mouseWheelPos < blockList.length) {
				currentItem = blockList[mouseWheelPos];
			} else if (mouseWheelPos < wheelCap) {
				// enemy
				currentItem = enemyList[mouseWheelPos - blockList.length];
			} else if (mouseWheelPos == wheelCap) {
				currentItem = "info";
			} else {
				currentItem = blockList[mouseWheelPos];
			}

		}

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

	public void paintBlocks() {
		final int KEY_CTRL = 17;
		final int KEY_SHIFT = 16;
		try {
			screen.removeID("cursor");
		} catch (Exception e) {
			// No Cursor
		}

		boolean canKill = false;
		String[] lethalBlocks = BlockTypes.lethalBlocks;
		if (mouseWheelPos < blockList.length) {
			for (int i = 0; i < lethalBlocks.length; i++) {
				if (currentItem.equals(lethalBlocks[i])) {
					canKill = true;
					i = lethalBlocks.length;
				}
			}
			if (!keeb.getKey('1')) {
				// New block is offset to start at the top left of the mouse
				Block block = new Block((int) mouse.getX() + scroll, (int) mouse.getY(), "MouseAdded " + currentStroke,
						currentItem, canKill);

				if (game.getFlag("grid")) {
					block = new Block(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
							"MouseAdded " + currentStroke + " " + currentBlock, currentItem, canKill);
				}
				block.setRotation(rotation);
				screen.add(block);
			} else {
				MovingBlock block = new MovingBlock((int) mouse.getX() + scroll, (int) mouse.getY(), "MouseAdded " + currentStroke,
						currentItem, canKill);
				if (game.getFlag("grid")) {
					block = new MovingBlock(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
							"MouseAdded " + currentStroke + " " + currentBlock, currentItem, canKill);
				}
				block.setRotation(rotation);
				screen.add(block);
			}
			currentBlock++;
			System.out.print(currentBlock);
			screenCleaned = false;
		} else if (mouseWheelPos < blockList.length + enemyList.length) {
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
		// If the CTRL key isn't pressed, release the mouse button
		if (!keeb.getKey(KEY_CTRL)) {
			mouse.removePressedFlag();

		}
		// Shows the mouse button is still held
		lastStrokeFlag = true;

	}

	public void cursor() {
		if (mouseWheelPos < blockList.length) {
			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) (mouse.getX() + scroll), (int) mouse.getY(), "cursor", currentItem, false);

			if (game.getFlag("grid")) {
				block = new Block((int) mouse.getX() + scroll >> 5 << 5, (int) mouse.getY() >> 5 << 5, "cursor",
						currentItem, false);
			}
			block.setRotation(rotation);
			screen.add(block);

		} else if (mouseWheelPos < blockList.length + enemyList.length) {
			FireHopper hop = new FireHopper((int) mouse.getX() + scroll, (int) mouse.getY() + 19, "cursor");
			if (game.getFlag("grid")) {
				hop = new FireHopper((int) mouse.getX() + scroll >> 5 << 5, (int) (mouse.getY() >> 5 << 5) + 19,
						"cursor");
			}
			screen.add(hop);
		} else if (currentItem.equals("info")) {

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

	}

	public void removeBlocks() {
		final int KEY_CTRL = 17;
		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");
		System.out.println("delete");
		for (int i = blocks.size() - 1; i >= 0; i--) {
			Block block = (Block) blocks.get(i);
			if (mouse.getX() + scroll >= block.getX() && mouse.getX() + scroll <= block.getX() + block.getWidth()
					&& mouse.getY() >= block.getY() && mouse.getY() <= block.getY() + block.getHeight()) {
				System.out.println("delete");

				if (!screenCleaned) {
					screen.screenClean();
					screenCleaned = true;
				}

				screen.removeID(block.getID());

				i = -1;
			}
		}

		ArrayList<ScreenElement> enemies = screen.getAllOfType("enemy");
		System.out.println("delete");
		for (int i = enemies.size() - 1; i >= 0; i--) {
			Enemy enemy = (Enemy) enemies.get(i);
			if (mouse.getX() + scroll >= enemy.getX() && mouse.getX() + scroll <= enemy.getX() + enemy.getWidth()
					&& mouse.getY() >= enemy.getY() && mouse.getY() <= enemy.getY() + enemy.getHeight()) {
				System.out.println("delete");

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

	public void selectBlock() {
		final int KEY_CTRL = 17;

		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");
		for (int i = blocks.size() - 1; i >= 0; i--) {
			Block block = (Block) blocks.get(i);
			if (mouse.getX() + scroll >= block.getX() && mouse.getX() + scroll <= block.getX() + block.getWidth()
					&& mouse.getY() >= block.getY() && mouse.getY() <= block.getY() + block.getHeight()) {
				gooey.setSelect(block);
				System.out.println(block.getX() + " " + block.getY());
			}
		}

		ArrayList<ScreenElement> enemies = screen.getAllOfType("enemy");
		for (int i = enemies.size() - 1; i >= 0; i--) {
			Enemy enemy = (Enemy) enemies.get(i);
			if (mouse.getX() + scroll >= enemy.getX() && mouse.getX() + scroll <= enemy.getX() + enemy.getWidth()
					&& mouse.getY() >= enemy.getY() && mouse.getY() <= enemy.getY() + enemy.getHeight()) {
				gooey.setSelect(enemy);
			}
		}

		Player player = (Player) screen.getScreenElement("Player");

		if (mouse.getX() + scroll >= player.getX() && mouse.getX() + scroll <= player.getX() + 32
				&& mouse.getY() >= player.getY() && mouse.getY() <= player.getY() + 32) {
			System.out.println("delete");

			gooey.setSelect(player);

		}

		LevelInfo info = (LevelInfo) screen.getScreenElement("lvlinfo");
		if (mouse.getX() + scroll >= info.getSpawnX() && mouse.getX() + scroll <= info.getSpawnX() + 32
				&& mouse.getY() >= info.getSpawnY() && mouse.getY() <= info.getSpawnY() + 32) {
			gooey.setSelect(info);

		}

		mouse.removePressedFlag();
	}

	/**
	 * Removes the previous stroke, no matter how many blocks it was
	 */
	public void undoStroke() {
		currentStroke--;
		screen.removeAllID("MouseAdded " + currentStroke);
		// Makes it so it is only run once per key press
		undo = true;
	}// undoStroke

}
