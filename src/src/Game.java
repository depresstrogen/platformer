package src;

import java.util.ArrayList;
import java.util.Scanner;

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
	private boolean jumpAgain = false;
	private boolean inJump = false;
	private boolean paint = false;
	private boolean undo = false;
	private boolean grid = true;
	private boolean gridToggle = false;
	private boolean lastStrokeFlag = false;
	private boolean onBlock = false;
	// Game timing
	private int fps = 60;
	private long nextFrame = System.currentTimeMillis() + (1000 / fps);

	private int mouseWheelPos = 0;

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
			collisionCheck();
			keebActions();
			levelEditor();

			
			gravity();
			collisionCheck();
			updatePlayer();
			scrollCheck();

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
		} else if (!keeb.getKey(0)) {
			jumpAgain = true;
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

		if (keeb.getKey('G')) {
			if (gridToggle) {

			} else {
				grid = !grid;
				gridToggle = true;
			}
		} else {
			gridToggle = false;
		}

		if (keeb.getKey('L')) {
			System.out.print("Save or Load (S/L): ");
			Scanner sc = new Scanner(System.in);
			String type = sc.nextLine();
			System.out.println("");

			if (type.equals("S")) {
				System.out.print("Save to lvl/");
			}
			if (type.equals("L")) {
				System.out.print("Load from lvl/");
			}

			String dir = sc.nextLine();
			dir = "lvl/" + dir;
			ScreenFile sf = new ScreenFile();
			if (type.equals("S")) {
				screen.saveElements(dir);
			}
			if (type.equals("L")) {
				screen.loadElements(dir);
			}
		}

		// System.out.println(mouse.getMouseWheelTime() + " " + nextFrame + " ");
		if (mouse.getMouseWheelTime() > nextFrame - 32) {
			int wheelCap = 3;
			if (mouse.getMouseWheel() == 'U') {
				mouseWheelPos--;
			} else if (mouse.getMouseWheel() == 'D') {
				mouseWheelPos++;
			}

			if (mouseWheelPos < 0) {
				mouseWheelPos = wheelCap;
			}
			if (mouseWheelPos > wheelCap) {
				mouseWheelPos = 0;
			}

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
		for (int i = 0; i <= 8 * 32; i++) {
			Block block = new Block(i, 256, "MouseAdded -1", "ground");
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
		if(jumpAgain && onBlock) {
			playerY--;
			velocity = -15;
			jumpAgain = false;
		}
	}// jump

	/**
	 * Checks if the player is colliding with any block and makes it so it is not
	 */
	public void collisionCheck() {
		// Can be changed to the current block's size
		final int BLOCK_WIDTH = 32;

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
					System.out.println(block.getX() + " " + playerX);

					// if on edge of block dont do that
					if (block.getX() - playerX == 288 - 261) {
						
					} else if (block.getX() - playerX == 256 - 283) {
						
					} else {
						playerY = block.getY() - (BLOCK_WIDTH - 1);
						if (velocity > 0)  {
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
						velocity = 0;
						}
					}
				 else if (left) {
					playerX = block.getX() - BLOCK_WIDTH;

				} else if (right) {
					playerX = block.getX() + BLOCK_WIDTH;
				}
				
				
			}
			// This gives the newest block placed collision priority
			this.onBlock = onBlock;
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
		String[] blockList = { "ground", "dirt", "brick", "grid" };
		String currentItem = blockList[mouseWheelPos];
		try {
			screen.removeID("cursor");
		} catch (Exception e) {
			// No Cursor
		}
		if (mouse.isMousePressed() && mouse.getMouseButton() == 'L') {
			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) ((mouse.getX() - 8) / screen.getXRatio() + scroll),
					(int) ((mouse.getY() - 32) / screen.getYRatio()), "MouseAdded " + currentStroke, currentItem);

			if (grid) {
				block = new Block(((int) ((mouse.getX() - 8) / screen.getXRatio() + scroll) >> 5) << 5,
						((int) ((mouse.getY() - 32) / screen.getYRatio()) >> 5) << 5, "MouseAdded " + currentStroke,
						currentItem);
			}
			screen.add(block);
			// If the CTRL key isn't pressed, release the mouse button
			if (!paint) {
				mouse.removePressedFlag();
			}
			// Shows the mouse button is still held
			lastStrokeFlag = true;
		} else if (mouse.getMouseButton() == 'R' && mouse.isMousePressed()) {
			// New block is offset to start at the top left of the mouse
			Block block = new Block((int) ((mouse.getX() - 8) / screen.getXRatio() + scroll),
					(int) ((mouse.getY() - 32) / screen.getYRatio()), "cursor", currentItem);

			if (grid) {
				block = new Block(((int) ((mouse.getX() - 8) / screen.getXRatio() + scroll) >> 5) << 5,
						((int) ((mouse.getY() - 32) / screen.getYRatio()) >> 5) << 5, "cursor", currentItem);
			}

			screen.add(block);
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

	public boolean getFlag(String flag) {
		if (flag.equals("grid")) {
			return grid;
		}

		return false;
	}

	// Medal System
}
