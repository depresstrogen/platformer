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
 * @version April 19 2021
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

		updatePlayer();
		while (true) {
			// Main game actions
			//collisionCheck();
			keebActions();
			lvledit.levelEditor();
			scrollCheck();
			gravity();
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
				blocksCollided ++;
				// Update the position to right outside the block's width
				// Top (Gets priority as it can cancel actions)
				if (block.getY() - BLOCK_WIDTH < playerY && block.getY() - (BLOCK_WIDTH / 2) > playerY) {

					up = true;

				}
				// Bottom (Stops Velocity)
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

					// if on edge of block dont do that
					if (block.getX() - playerX == 288 - 261) {

					} else if (block.getX() - playerX == 256 - 283) {

					} else {
						playerY = block.getY() - (BLOCK_WIDTH - 1);
						if (velocity > 0) {
							velocity = 0;
						}
						inJump = false;
						onBlock = true;
					}

				} else if (down) {
					if (block.getX() - playerX == 288 - 261) {

					} else if (block.getX() - playerX == 256 - 283) {

					} else {
						playerY = block.getY() + BLOCK_WIDTH;
						if (velocity < 0) {
							velocity = 0;
						}
					}
				} else if (left) {
					playerX = block.getX() - BLOCK_WIDTH;

				} else if (right) {
					playerX = block.getX() + BLOCK_WIDTH;
				}

				if(!block.canKill()) 
					die = false;
				}
				
				
			}
			// This gives the newest block placed collision priority
			this.onBlock = onBlock;
			
			if(die && blocksCollided > 0) {
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

	public boolean getFlag(String flag) {
		if (flag.equals("grid")) {
			return grid;
		}

		return false;
	}

	public void setFlag(String flag, boolean val) {
		if (flag.equals("paused")) {
			paused = val;
		}
		if (flag.equals("grid")) {
			grid = val;
		}
	}

	public void die() {
		playerX = 200;
		playerY = 0;
		scroll = 0;
	}
	
	// Medal System?
	// World Map?
}
