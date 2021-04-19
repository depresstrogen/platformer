package src;

import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * Contains everything to do with the actual game such as - Player movement -
 * Gravity - Collision - Level Editor
 * 
 * @author Riley Power
 * @version April 19 2021
 */
public class Game {
	// IO Objects
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

	// Game timing
	private int fps = 60;
	private long nextFrame = System.currentTimeMillis() + (1000 / fps);

	/**
	 * 
	 * @param screen The screen to display to
	 * @param keeb   The keyboard to get input from
	 * @param mouse  The mouse to get input from
	 */
	public Game(Screen screen, KeebHandler keeb, MouseHandler mouse) {
		this.screen = screen;
		this.keeb = keeb;
		this.mouse = mouse;
	}// Game

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
	}// start

	/**
	 * Checks for input and does the associated actions
	 */
	public void keebActions() {
		// Move Left
		if (keeb.getKey('A')) {
			playerX -= moveSpeed;
		}

		// Move Right
		if (keeb.getKey((int) 'D')) {
			playerX += moveSpeed;
		}

		// Maybe on ladders or something
//		if (keeb.getKey('W')) {
//			playerY--;
//		}
//		if (keeb.getKey('S')) {
//			playerY++;
//		}

		// Jump (Space)
		if (keeb.getKey(0) && !inJump) {
			jump();
			inJump = true;
		}

		// Brush paint (CTRL)
		if (keeb.getKey(1)) {
			paint = true;
		} else {
			paint = false;
		}

		// Undo paint (CTRL + Z)
		if (keeb.getKey(1) && keeb.getKey('Z') && !undo) {
			undoStroke();
			System.out.println("d");
		} else if (!keeb.getKey(1) || !keeb.getKey('Z')) {
			undo = false;
		}
	}// keebActions

	/**
	 * Updates the player object on the screen
	 */
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
	}// updatePlayer

	/**
	 * Loads the current level
	 */
	public void loadLevel() {
		for (int i = 0; i < 8 * 32; i++) {
			Block block = new Block(i, 200, "2", "ground");
			screen.add(block);
		}
	}// loadLevel

	/**
	 * Applies gravity to the player, using the velocity
	 */
	public void gravity() {
		velocity += 1;
		playerY += velocity;
		if (velocity > 10) {
			velocity = 10;
		}
	}// gravity

	/**
	 * Makes the player jump by making it's velocity negative
	 */
	public void jump() {
		playerY--;
		velocity = -15;
	}// jump

	/**
	 * Checks if the player is colliding with any block and makes it so it is not
	 */
	public void collisionCheck() {
		// Can be changed to the current block's size
		final int BLOCK_WIDTH = 32;

		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");

		for (int i = 0; i < blocks.size(); i++) {
			// Gets current block
			Block block = (Block) blocks.get(i);
			// If in the block's width
			if (block.getY() - BLOCK_WIDTH < playerY && block.getY() + BLOCK_WIDTH > playerY
					&& block.getX() - BLOCK_WIDTH < playerX && block.getX() + BLOCK_WIDTH > playerX) {
				// Update the position to right outside the block's width
				// Top (Gets priority as it can cancel actions)
				if (block.getY() - BLOCK_WIDTH < playerY && block.getY() - (BLOCK_WIDTH / 2) > playerY) {
					playerY = block.getY() - (BLOCK_WIDTH - 1);
					velocity = 0;
					inJump = false;
				}
				// Bottom (Stops Velocity)
				else if (block.getY() + BLOCK_WIDTH > playerY && block.getY() + (BLOCK_WIDTH / 2) < playerY) {
					playerY = block.getY() + BLOCK_WIDTH;
					velocity = 0;
				}
				// Left
				else if (block.getX() - BLOCK_WIDTH < playerX && block.getX() - (BLOCK_WIDTH / 2) > playerX) {
					playerX = block.getX() - BLOCK_WIDTH;
				}
				// Right
				else if (block.getX() + BLOCK_WIDTH > playerX && block.getX() + (BLOCK_WIDTH / 2) < playerX) {
					playerX = block.getX() + BLOCK_WIDTH;
				}
			}
			// This gives the newest block placed collision priority
		}
	}// collisionCheck

	/**
	 * Scrolls the screen if the player is approaching the edge
	 */
	public void scrollCheck() {
		int playerSX = (playerX - scroll);

		// Left of screen
		if (playerSX < 200) {
			scroll -= moveSpeed;
		}

		// Right of screen
		if (playerSX > 1000) {
			scroll += moveSpeed;
		}
	}// scrollCheck

	/**
	 * Returns how much the screen has scrolled (negative is left positive is right)
	 * 
	 * @return scroll How much the screen has scrolled
	 */
	public int getScroll() {
		return scroll;
	}// getScroll

	/**
	 * Runs the level editor, allows you to place blocks with the mouse
	 */
	public void levelEditor() {
		if (mouse.isMousePressed()) {
			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) ((mouse.getX() - 8) / screen.getXRatio() + scroll),
					(int) ((mouse.getY() - 32) / screen.getYRatio()), "MouseAdded " + currentStroke, "ground");
			screen.add(block);
			// If the CTRL key isn't pressed, release the mouse button
			if (!paint) {
				mouse.removePressedFlag();
			}
			// Shows the mouse button is still held
			lastStrokeFlag = true;
		} else {
			// If the mouse button was held last frame
			if (lastStrokeFlag) {
				// Increment the stroke
				currentStroke++;
				lastStrokeFlag = false;
			}
		}
	}// levelEditor

	/**
	 * Removes the previous stroke, no matter how many blocks it was
	 */
	public void undoStroke() {
		currentStroke--;
		screen.removeAllID("MouseAdded " + currentStroke);
		// Makes it so it is only run once per key press
		undo = true;
	}// undoStroke

	// Medal System
}
