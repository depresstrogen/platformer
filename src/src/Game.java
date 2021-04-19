package src;

import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * Contains everything to do with the actual game such as - Player movement -
 * Gravity - Collision - Level Editor
 * 
 * @author Riley Power
 *
 */
public class Game {
	private Screen screen;
	private KeebHandler keeb;
	private MouseHandler mouse;

	// Player coordinates
	private int playerX = 200;
	private int playerY = 0;

	// Player
	private int velocity = 0;

	// Player move speed
	private int moveSpeed = 5;

	// Screen scroll
	private int scroll = 0;

	// For level editor, allows for CTRL Z
	private int currentStroke = 0;

	// Flags
	private boolean inJump = false;
	private boolean paint = false;
	private boolean undo = false;
	private boolean onGrid = false;
	private boolean lastStrokeFlag = false;

	private int fps = 60;
	private long nextFrame = System.currentTimeMillis() + (1000 / fps);

	public Game(Screen screen, KeebHandler keeb, MouseHandler mouse) {
		this.screen = screen;
		this.keeb = keeb;
		this.mouse = mouse;
	}

	/**
	 * Starts the game, Runs the main game loop
	 */
	public void start() {
		updatePlayer();
		loadLevel();
		while (true) {
			// Main game actions
			keebActions();
			levelEditor();
			gravity();
			collisionCheck();
			scrollCheck();
			updatePlayer();

			while (nextFrame > System.currentTimeMillis()) {
				// Wait for next frame
			}

			// Set next frame time
			nextFrame = System.currentTimeMillis() + (1000 / fps);
		}
	}

	public void keebActions() {
		if (keeb.getKey('A')) {
			playerX -= moveSpeed;
		}
		if (keeb.getKey((int) 'D')) {
			playerX += moveSpeed;
		}
		if (keeb.getKey('W')) {
			playerY--;
		}
		if (keeb.getKey('S')) {
			playerY++;
		}
		if (keeb.getKey(0) && !inJump) {
			jump();
			inJump = true;
		}
		if (keeb.getKey(1)) {
			paint = true;
		} else {
			paint = false;
		}

		if (keeb.getKey(1) && keeb.getKey('Z') && !undo) {
			undoStroke();
			System.out.println("d");
		} else if (!keeb.getKey(1) || !keeb.getKey('Z')) {
			undo = false;
		}
	}

	public void updatePlayer() {
		Player player = new Player(50, 50, "Player", "idle", "img/player/idle.gif", 1);
		player.setX(playerX);
		player.setY(playerY);
		try {
			screen.remove(screen.getIndex("Player"));
		} catch (Exception e) {
			// Player Created
		}
		screen.add(player);
	}

	public void loadLevel() {
		for (int i = 0; i < 8 * 32; i++) {
			Block block = new Block(i, 200, "2", "ground");
			screen.add(block);
		}
	}

	public void gravity() {
		velocity += 1;
		playerY += velocity;
		if (velocity > 10) {
			velocity = 10;
		}
	}

	public void jump() {
		playerY--;
		velocity = -15;
	}

	public void collisionCheck() {
		final int CHAR_WIDTH = 32;

		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");

		for (int i = 0; i < blocks.size(); i++) {
			Block block = (Block) blocks.get(i);
			if (block.getY() - CHAR_WIDTH < playerY && block.getY() + CHAR_WIDTH > playerY
					&& block.getX() - CHAR_WIDTH < playerX && block.getX() + CHAR_WIDTH > playerX) {

				// Top
				if (block.getY() - CHAR_WIDTH < playerY && block.getY() - (CHAR_WIDTH / 2) > playerY) {
					playerY = block.getY() - (CHAR_WIDTH - 1);
					velocity = 0;
					inJump = false;
				}
				// bottom
				else if (block.getY() + CHAR_WIDTH > playerY && block.getY() + (CHAR_WIDTH / 2) < playerY) {
					playerY = block.getY() + CHAR_WIDTH;
					velocity = 0;
				}
				// left
				else if (block.getX() - CHAR_WIDTH < playerX && block.getX() - (CHAR_WIDTH / 2) > playerX) {

					playerX = block.getX() - CHAR_WIDTH;

				}
				// right
				else if (block.getX() + CHAR_WIDTH > playerX && block.getX() + (CHAR_WIDTH / 2) < playerX) {

					playerX = block.getX() + CHAR_WIDTH;

				}
			}
		}

	}

	public void scrollCheck() {
		int playerSX = (playerX - scroll);

		if (playerSX < 200) {
			scroll -= moveSpeed;
		}

		if (playerSX > 1000) {
			scroll += moveSpeed;
		}
	}

	public int getScroll() {
		return scroll;
	}

	public void levelEditor() {
		if (mouse.isMousePressed()) {
			Block block = new Block((int) ((mouse.getX() - 8) / screen.getXRatio() + scroll),
					(int) ((mouse.getY() - 32) / screen.getYRatio()), "MouseAdded " + currentStroke, "ground");
			screen.add(block);
			System.out.println("added " + currentStroke);
			if (!paint) {
				mouse.removePressedFlag();
			}
			lastStrokeFlag = true;
		} else {
			if (lastStrokeFlag) {
				currentStroke++;
				lastStrokeFlag = false;
			}
		}
	}

	public void undoStroke() {
		currentStroke--;
		screen.removeAllID("MouseAdded " + currentStroke);
		undo = true;

	}

	// Medal System
}
