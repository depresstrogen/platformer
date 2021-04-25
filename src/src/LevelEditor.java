package src;

public class LevelEditor {
	public Game game;
	public Screen screen;
	public MouseHandler mouse;
	public KeebHandler keeb;
	
	private boolean lastStrokeFlag = false;
	
	private int mouseWheelPos = 0;
	
	private int currentStroke;
	private int scroll;
	private boolean undo = false;
	
	private String[] blockList = { "ground", "dirt", "brick", "grid", "lavatop", "lavabase"};
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
		final int KEY_CTRL = 17;
		scroll = game.getScroll();
		
		if (keeb.getKey(KEY_CTRL) && keeb.getKey('Z') && !undo) {
			undoStroke();
			System.out.println("d");
		} else if (!keeb.getKey(KEY_CTRL) || !keeb.getKey('Z')) {
			undo = false;
		}
		
		if (mouse.isMousePressed() && mouse.getMouseButton() == 'L') {
			paintBlocks();
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
		
		if (mouse.isMousePressed() && mouse.getMouseButton() == 'L') {
			paintBlocks();
		}
		
		else if (mouse.getMouseButton() == 'R' && mouse.isMousePressed()) {
			cursor();
		} else if (lastStrokeFlag) {
				// Increment the stroke
				currentStroke++;
				lastStrokeFlag = false;
		}
		
	}// levelEditor
	
	public void paintBlocks() {
		final int KEY_CTRL = 17;
		
		try {
			screen.removeID("cursor");
		} catch (Exception e) {
			// No Cursor
		}
		if (mouse.isMousePressed() && mouse.getMouseButton() == 'L') {
			boolean canKill = false;
			if(currentItem.equals("lavatop")) {
				canKill = true;
			}
			
			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) mouse.getX() + scroll, (int) mouse.getY(), "MouseAdded " + currentStroke,
					currentItem, canKill);

			if (game.getFlag("grid")) {
				block = new Block(((int) (mouse.getX() + scroll) >> 5) << 5, ((int) mouse.getY() >> 5) << 5,
						"MouseAdded " + currentStroke, currentItem, canKill);
			}
			screen.add(block);
			screen.screenClean();
			// If the CTRL key isn't pressed, release the mouse button
			if (!keeb.getKey(KEY_CTRL)) {
				mouse.removePressedFlag();
			}
			// Shows the mouse button is still held
			lastStrokeFlag = true;
		}
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
