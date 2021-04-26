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

	private boolean screenCleaned = true;
	
	private String[] blockList = BlockTypes.blockList;
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
		blockList = block.getBlockList();
		currentItem = blockList[mouseWheelPos];
		final int KEY_CTRL = 17;
		final int KEY_SHIFT = 16;
		scroll = game.getScroll();

		if (keeb.getKey(KEY_CTRL) && keeb.getKey('Z') && !undo) {
			undoStroke();
			System.out.println("d");
		} else if (!keeb.getKey(KEY_CTRL) || !keeb.getKey('Z')) {
			undo = false;
		}

		if (true) {
			int wheelCap = blockList.length - 1;
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
				mouseWheelPos = wheelCap;
			}
			if (mouseWheelPos > wheelCap) {
				mouseWheelPos = 0;
			}
			currentItem = blockList[mouseWheelPos];
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
		if (currentItem.equals("lavatop")) {
			canKill = true;
		}

		// New block is offset to start at the top left of the mouse
		Block block = new Block((int) mouse.getX() + scroll, (int) mouse.getY(), "MouseAdded " + currentStroke,
				currentItem, canKill);

		if (game.getFlag("grid")) {
			block = new Block(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
					"MouseAdded " + currentStroke + " " + currentBlock, currentItem, canKill);
		}
		currentBlock ++;
		System.out.print(currentBlock);
		screenCleaned = false;
		screen.add(block);
		// If the CTRL key isn't pressed, release the mouse button
		if (!keeb.getKey(KEY_CTRL)) {
			mouse.removePressedFlag();
			
		}
		// Shows the mouse button is still held
		lastStrokeFlag = true;

	}

	public void cursor() {
		if (mouse.getMouseButton() == 'R' && mouse.isMousePressed()) {
			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) (mouse.getX() + scroll), (int) mouse.getY(), "cursor", currentItem, false);

			if (game.getFlag("grid")) {
				block = new Block((int) mouse.getX() + scroll >> 5 << 5, (int) mouse.getY() >> 5 << 5, "cursor",
						currentItem, false);
			}

			screen.add(block);

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
				
				if(!screenCleaned) {
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
