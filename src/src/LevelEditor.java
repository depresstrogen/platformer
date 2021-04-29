package src;

import java.util.ArrayList;

public class LevelEditor {
	public Game game;
	public Screen screen;
	public MouseHandler mouse;
	public KeebHandler keeb;

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

	public LevelEditor(Game game, Screen screen, MouseHandler mouse, KeebHandler keeb) {
		this.game = game;
		this.screen = screen;
		this.mouse = mouse;
		this.keeb = keeb;
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
			int wheelCap = blockList.length +  - 1;
			if (mouse.getMouseWheel() == 'U') {
				mouseWheelPos--;
				System.out.print("sc");
				mouse.resetMouseWheel();
			} else if (mouse.getMouseWheel() == 'D') {
				mouseWheelPos++;
				System.out.print("sd");
				mouse.resetMouseWheel();
			}

			if (mouseWheelPos < 0) {
				mouseWheelPos = wheelCap + 1;
			}
			if (mouseWheelPos > wheelCap + 1) {
				mouseWheelPos = 0;
			}
			//Block
			if(mouseWheelPos <  blockList.length +  - 1) {
				currentItem = blockList[mouseWheelPos];
			} else if (mouseWheelPos <  wheelCap + 1) {
				//enemy
				currentItem = enemyList[mouseWheelPos - blockList.length + 1];
			} else if (mouseWheelPos == wheelCap + 1){
				currentItem = "info";
			} else {
				currentItem = blockList[mouseWheelPos];
			}
			
		}

		if (mouse.isMousePressed() && mouse.getMouseButton() == 'L' && keeb.getKey(KEY_SHIFT)) {
			removeBlocks();
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
		if (mouseWheelPos < blockList.length - 1) {
			for (int i = 0; i < lethalBlocks.length; i++) {
				if (currentItem.equals(lethalBlocks[i])) {
					canKill = true;
					i = lethalBlocks.length;
				}
			}

			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) mouse.getX() + scroll, (int) mouse.getY(), "MouseAdded " + currentStroke,
					currentItem, canKill);

			if (game.getFlag("grid")) {
				block = new Block(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
						"MouseAdded " + currentStroke + " " + currentBlock, currentItem, canKill);
			}
			block.setRotation(rotation);
			currentBlock++;
			System.out.print(currentBlock);
			screenCleaned = false;
			screen.add(block);
		} else if (mouseWheelPos < blockList.length + enemyList.length - 1) {
			FireHopper hop = new FireHopper((int) mouse.getX() + scroll, (int) mouse.getY(), "MouseAdded " + currentStroke);
			if (game.getFlag("grid")) {
				hop = new FireHopper(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
						"MouseAdded " + currentStroke + " " + currentBlock);
			}
			currentBlock++;
			screenCleaned = false;
			screen.add(hop);
		}
		else if (currentItem.equals("info")) {
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
		if (mouseWheelPos < blockList.length - 1) {
			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) (mouse.getX() + scroll), (int) mouse.getY(), "cursor", currentItem, false);

			if (game.getFlag("grid")) {
				block = new Block((int) mouse.getX() + scroll >> 5 << 5, (int) mouse.getY() >> 5 << 5, "cursor",
						currentItem, false);
			}
			block.setRotation(rotation);
			screen.add(block);

		} else if (mouseWheelPos < blockList.length + enemyList.length - 1) {
			FireHopper hop = new FireHopper((int) mouse.getX() + scroll, (int) mouse.getY() + 19, "cursor");
			if (game.getFlag("grid")) {
				hop = new FireHopper((int) mouse.getX() + scroll >> 5 << 5, (int) (mouse.getY() >> 5 << 5 ) + 19, "cursor");
			}
			screen.add(hop);
		}
		else if (currentItem.equals("info")) {
		
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
		// If the CTRL key isn't pressed, release the mouse button
		if (!keeb.getKey(KEY_CTRL)) {
			mouse.removePressedFlag();
		}
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
