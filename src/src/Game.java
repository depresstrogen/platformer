package src;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

/**
 * Contains everything to do with the actual game such as - Player movement -
 * Gravity - Collision - Level Editor
 * 
 * CONTROLS WASD = Movement SPACE = Jump G = Toggle Grid CTRL = Paint Brush CTRL
 * + Z = Undo L = Save/Load T = Toggle GUI Shift = Delete R = Rotate
 * 
 * @author Riley Power
 * @version April 26 2021
 */
public class Game {
	// IO Objects
	private Screen screen;
	private KeebHandler keeb;
	private MouseHandler mouse;
	private GUIElements gooey;
	private LevelEditor lvledit;
	// Player coordinates
	private int playerX = 200;
	private int playerY = 0;

	// Player
	private int velocity = 0;

	// Player move speed
	private int moveSpeed = 5;

	private int jumpOffset = -16;

	// Screen scroll
	private int scroll = 0;

	// For level editor, allows for CTRL Z
	private int currentStroke = 0;

	// Flags
	private boolean paused = false;
	private boolean jumpAgain = false;
	private boolean inJump = false;
	private boolean grid = true;
	private boolean onBlock = false;
	private boolean tToggle = false;

	// Game timing
	private int fps = 60;
	private long nextFrame = System.currentTimeMillis() + (1000 / fps);

	/**
	 * 
	 * @param screen The screen to display to
	 * @param keeb   The keyboard to get input from
	 * @param mouse  The mouse to get input from
	 */
	public Game(Screen screen, KeebHandler keeb, MouseHandler mouse, GUIElements gooey) {
		this.screen = screen;
		this.keeb = keeb;
		this.mouse = mouse;
		this.gooey = gooey;
		lvledit = new LevelEditor(this, screen, mouse, keeb);
	}// Game

	/**
	 * Starts the game, Runs the main game loop
	 */
	synchronized void start() {
		for (int i = 0; i <= 8 * 32; i++) {
			Block block = new Block(i, 256, "MouseAdded -1", "ground", false);
			screen.add(block);
		}

		LevelInfo ele = new LevelInfo();
		ele.setSpawnX(0);
		ele.setSpawnY(0);
		screen.add(ele);
		
		updatePlayer();
		while (true) {
			// Main game actions
			keebActions();
			lvledit.levelEditor();
			scrollCheck();
			gravity();
			updateEnemies();
			collisionCheck();
			updatePlayer();
			screen.repaint();

			while (nextFrame > System.currentTimeMillis()) {
				if (paused) {
					nextFrame = System.currentTimeMillis() + (1000 / fps);
				}
			}

			// Set next frame time
			nextFrame = System.currentTimeMillis() + (1000 / fps);
		}
	}// start

	/**
	 * Checks for input and does the associated actions
	 */
	public void keebActions() {
		final int KEY_SPACE = 32;
		// Move Left
		if (keeb.getKey('A')) {
			playerX -= moveSpeed;
		}

		// Move Right
		if (keeb.getKey('D')) {
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
		if (keeb.getKey(KEY_SPACE) && !inJump) {
			jump();
			inJump = true;
		} else if (!keeb.getKey(KEY_SPACE)) {
			jumpAgain = true;
		}

		if (keeb.getKey('T')) {

			if (!tToggle) {
				gooey.levelEdit(true);
				tToggle = true;
			} else {
				gooey.levelEdit(false);
				tToggle = false;
			}
			keeb.setKey('T', false);
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
	 * Loads the selected level
	 */
	public void loadLevel(String dir) {
		screen.loadElements(dir);
		try {
		LevelInfo ele = (LevelInfo) screen.getScreenElement("lvlinfo");
		playerX = ele.getSpawnX();
		playerY = ele.getSpawnY();
		} catch (Exception e) {
			LevelInfo ele = new LevelInfo();
			ele.setSpawnX(0);
			ele.setSpawnY(0);
			screen.add(ele);
		}
		
	}// loadLevel

	/**
	 * Loads the current level
	 */
	public void saveLevel(String dir) {
		screen.saveElements(dir);
	}// saveLevel

	/**
	 * Applies gravity to the player, using the velocity
	 */
	public void gravity() {
		velocity += 1;
		playerY += velocity;
		if (velocity > 10) {
			velocity = 10;
		}
		if (playerY > 720) {
			playerY = 0;
		}
		if (velocity < 0) {
			inJump = true;
		}
	}// gravity

	/**
	 * Makes the player jump by making it's velocity negative
	 */
	public void jump() {
		if (jumpAgain && onBlock) {
			playerY--;
			velocity = jumpOffset;
			jumpAgain = false;
		}
	}// jump

	/**
	 * Checks if the player is colliding with any block and makes it so it is not
	 */
	public void collisionCheck() {
		// Can be changed to the current block's size
		final int BLOCK_WIDTH = 32;
		boolean die = true;
		int blocksCollided = 0;
		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");
		boolean onBlock = false;
		for (int i = 0; i < blocks.size(); i++) {

			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			// Gets current block
			Block block = (Block) blocks.get(i);
			// If in the block's width
			if (block.getY() - BLOCK_WIDTH < playerY && block.getY() + BLOCK_WIDTH > playerY
					&& block.getX() - BLOCK_WIDTH < playerX && block.getX() + BLOCK_WIDTH > playerX) {
				blocksCollided++;
				// Update the position to right outside the block's width
				// Top (Gets priority as it can cancel actions)
				if (block.getY() - BLOCK_WIDTH < playerY && block.getY() - (BLOCK_WIDTH / 2) > playerY) {
					up = true;
				}
				// Bottom
				if (block.getY() + BLOCK_WIDTH > playerY && block.getY() + (BLOCK_WIDTH / 2) < playerY) {
					down = true;
				}
				// Left
				if (block.getX() - BLOCK_WIDTH < playerX && block.getX() - (BLOCK_WIDTH / 2) > playerX) {
					left = true;
				}
				// Right
				if (block.getX() + BLOCK_WIDTH > playerX && block.getX() + (BLOCK_WIDTH / 2) < playerX) {
					right = true;
				}

				if (up) {
					// if on edge of block don't do that
					if (block.getX() - playerX == 27) {
					} else if (block.getX() - playerX == -27) {
					} else {
						// Snap player to top
						playerY = block.getY() - (BLOCK_WIDTH - 1);
						// Cancel Downward Speed
						if (velocity > 0) {
							velocity = 0;
						}
						// Cancel Jump
						inJump = false;
						onBlock = true;
					}
				} else if (down) {
					// If on edge of block, don't
					if (block.getX() - playerX == 288 - 261) {

					} else if (block.getX() - playerX == 256 - 283) {

					} else {
						// Snap to bottom
						playerY = block.getY() + BLOCK_WIDTH;
						// Cancel Upward Speed
						if (velocity < 0) {
							velocity = 0;
						}
					}
				} else if (left) {
					// Snap to left
					playerX = block.getX() - BLOCK_WIDTH;

				} else if (right) {
					// Snap to right
					playerX = block.getX() + BLOCK_WIDTH;
				}

				// You're on a safe block so you can't die
				if (!block.canKill())
					die = false;
			}

		}

		ArrayList<ScreenElement> enemies = screen.getAllOfType("enemy");
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = (Enemy) enemies.get(i);
			if (enemy.getY() - BLOCK_WIDTH < playerY && enemy.getY() + BLOCK_WIDTH > playerY
					&& enemy.getX() - BLOCK_WIDTH < playerX && enemy.getX() + BLOCK_WIDTH > playerX) {
				die();
			}
		}
		
		// This gives the newest block placed collision priority
		this.onBlock = onBlock;

		// Die Bitch
		if (die && blocksCollided > 0) {
			die();
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
	 * Returns the flag associated with the string given
	 * 
	 * @param flag The string with the flag to look for
	 * @return The state of the requested flag;
	 */
	public boolean getFlag(String flag) {
		if (flag.equals("grid")) {
			return grid;
		}

		return false;
	}// getFlag

	/**
	 * Allows a flag to be set from another object
	 * 
	 * @param flag The flag to edit
	 * @param val  The value to set the flag to
	 */
	public void setFlag(String flag, boolean val) {
		if (flag.equals("paused")) {
			paused = val;
		}
		if (flag.equals("grid")) {
			grid = val;
		}
	}// setFlag

	/**
	 * Returns the player to the spawned state
	 */
	public void die() {
		LevelInfo info = (LevelInfo) screen.getScreenElement("lvlinfo");
		playerX = info.getSpawnX();
		playerY = info.getSpawnY();
		scroll = 0;
	}// die

	public void updateEnemies() {
		ArrayList<ScreenElement> enemies = screen.getAllOfType("enemy");
		for(int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i) instanceof FireHopper) {
				FireHopper hop = (FireHopper) enemies.get(i);
				hop.update();
			}
		}
	}
	
	// Medal System?
	// World Map?
}
