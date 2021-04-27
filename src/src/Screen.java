package src;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Everything to do with drawing the screen, and anything to do with the main
 * window is performed in this class
 * 
 * @version April 19, 2021
 * @author Riley Power
 *
 */
public class Screen extends JPanel {
	// The JFrame to draw on
	private JFrame frame;

	// Everything to be displayed on screen is stored here
	private ArrayList<ScreenElement> elements = new ArrayList<ScreenElement>();

	private Image blockPics[] = new Image[BlockTypes.blockList.length];

	// The game runs at 720p then scales
	private final int BASEX = 1280;
	private final int BASEY = 720;

	// The size of the window
	private int windowX;
	private int windowY;

	// The ratio of the window to 720p
	private double ratioX = 1;
	private double ratioY = 1;

	// The game object
	public Game game;
	public Screen screen = this;

	// IO objects
	private MouseHandler mouse = new MouseHandler();
	private KeebHandler keeb = new KeebHandler();
	private ButtonActions actions;
	private GUIElements gooey;

	// Time management (Literally)
	private int fps = 60;
	private long nextFrame = System.currentTimeMillis() + (1000 / fps);

	// FPS Counter
	private int accFPS = 0;
	private long lastFPS = System.currentTimeMillis();

	/**
	 * Constructs the jFrame, mouse listener and key listener
	 * 
	 * @param height How many pixels tall the window will be
	 * @param width  How many pixels wide the window will be
	 */
	public Screen(int height, int width) {
		// Deal with parameters
		windowX = width;
		windowY = height;

		// Start JFrame
		frameSetup();
		runGame();

		// lol while true go brrr
		while (true) {
			ratio();
			repaint();

			while (nextFrame > System.currentTimeMillis()) {

			}
			if (System.currentTimeMillis() - lastFPS >= 1000) {

				System.out.println("Screen FPS: " + accFPS);
				accFPS = 0;
				lastFPS = System.currentTimeMillis();
			}

			nextFrame = System.currentTimeMillis() + (1000 / fps);
		}

	}// Screen

	/**
	 * Constructs the JFrame, all other required objects
	 */
	private void frameSetup() {
		// Frame Loading
		frame = new JFrame("Platformer | By Riley Power");
		frame.add(this);
		frame.setSize(windowX, windowY);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Image Loading
		loadBlocks();
		// Object Loading
		gooey = new GUIElements(this, frame);
		game = new Game(this, keeb, mouse, gooey);
		actions = new ButtonActions(this, frame, game);
		mouse.start(frame, this, actions);

	}// frameSetup

	/**
	 * Lmao JFrame buttons look like shit let me just re make an existing code base
	 * Displays everything in the elements ArrayList according to it's parameters
	 * 
	 * @param g The canvas to paint every object to
	 */
	public void paintComponent(Graphics g) {
		// Main Paint Loop
		// Tell JPanel we drawing
		super.paintComponent(g);

		// Enhanced Graphics
		Graphics2D g2d;

		// Image lookup table
		Image blockTable[][] = new Image[blockPics.length][4];

		// Rotate images in advance
		for (int i = 0; i < blockPics.length; i++) {
			blockTable[i][0] = blockPics[i];
			blockTable[i][1] = ImageTools.rotateImage(blockTable[i][0], 90);
			blockTable[i][2] = ImageTools.rotateImage(blockTable[i][0], 180);
			blockTable[i][3] = ImageTools.rotateImage(blockTable[i][0], 270);
		}

		// Set the block and player offset
		int scroll = game.getScroll();

		// Image to draw on if screen is scaling
		BufferedImage screenImg = new BufferedImage(BASEX, BASEY, BufferedImage.TYPE_INT_RGB);

		// Draw to JPanel if native resolution
		// Draw to image if scaling
		if (ratioX == 1.0 && ratioY == 1.0) {
			g2d = (Graphics2D) g;
		} else {
			g2d = screenImg.createGraphics();
		}

		// Set Backgrounf
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, BASEX, BASEY);

		// Displays grid for level editor
		if (game.getFlag("grid")) {
			g2d.setColor(Color.BLACK);
			for (double i = 0; i < 720; i += 32) {
				g2d.drawLine(0, (int) i, (int) (1280), (int) i);
			}
			for (double i = -1; i < 1280; i += 32) {
				g2d.drawLine((int) (i - (scroll % 32)), 0, (int) (i - (scroll % 32)), (int) (720));
			}
		}
		// Loops through each item in the ArrayList and paints it depending which object
		// type it is
		for (int i = 0; i < elements.size(); i++) {
			ScreenElement element = elements.get(i);
			if (element instanceof Block) {
				// Draw only if on screen
				if (element.getX() < scroll - 30 || element.getX() > scroll + 1280) {
				} else {
					Block block = (Block) element;
					Image image;
					// Get appropriate image
					if (block.getRotation() == 0) {
						image = blockTable[block.getBlockCode()][0];
					} else {
						image = blockTable[block.getBlockCode()][block.getRotation() / 90];
					}
					// Draw that bitch
					g2d.drawImage(image, (int) ((block.getX() - scroll)), (int) (block.getY()), null);
				}
			} else if (element instanceof Player) {
					Player player = (Player) element;
					// Can load here as it only draws once
					Image img = Toolkit.getDefaultToolkit().getImage(player.getImage());
					// Draw your bitch
					g2d.drawImage(img, (int) ((player.getX() - scroll)), (int) (player.getY()),
							(int) (img.getHeight(this) * player.getScale()),
							(int) (img.getWidth(this) * player.getScale()), null);
			} else if (element instanceof TextButton) {
				//Draw a button then text in it
				TextButton button = (TextButton) element;
				g2d.setColor(button.getColor());
				//Draw button according to its size
				g2d.fillRect((int) ((button.getX())), (int) (button.getY()), (int) (button.getHeight()),
						(int) (button.getWidth()));
				int size = button.getWidth() / 2;
				g2d.setColor(Color.black);
				//Set font
				g2d.setFont(new Font(button.getFont(), Font.PLAIN, size));
				//Draw text within the confines of the button's height
				g2d.drawString(button.getText(), button.getX() + 16,
						button.getY() + (int) ((button.getWidth() + (size / 2)) / 2));
			} else if (element instanceof Button) {
				//Draw a normal button
				Button button = (Button) element;
				g2d.setColor(button.getColor());
				//Draw button according to its size
				g2d.fillRect((int) ((button.getX())), (int) (button.getY()), (int) (button.getHeight()),
						(int) (button.getWidth()));
			} else if (elements.get(i) instanceof Text) {
				Text text = (Text) elements.get(i);
				g2d.setColor(text.getColor());
				//Set font
				g2d.setFont(new Font(text.getFont(), Font.PLAIN, text.getFontSize()));
				//Draw standalone text
				g2d.drawString(text.getText(), text.getX(), text.getY());
			}

		}
		
		// If its being scaled draw the buffered image to the screen now
		if (ratioX == 1 && ratioY == 1) {

		} else {
			Graphics2D paint = (Graphics2D) g;
			paint.drawImage(screenImg, 0, 0, (int) (1280 * ratioX), (int) (720 * ratioY), null);
		}
		//FPS counter
		accFPS++;
	}// paint

	/**
	 * Gets the ratio one must multiply the current window dimensions to scale to
	 * 720p
	 */
	public void ratio() {
		windowX = frame.getWidth();
		windowY = frame.getHeight();

		ratioX = (double) windowX / BASEX;
		ratioY = (double) windowY / BASEY;

		repaint();
	}// ratio

	/**
	 * Adds the ScreenElement to the elements ArrayList
	 * 
	 * @param se Adds the ScreenElement to the elements ArrayList
	 */
	public void add(ScreenElement se) {
		elements.add(se);
		repaint();
	}// add

	/**
	 * Replaces the ScreenElement in the elements ArrayList of the index provided
	 * with the ScreenElement provided
	 * 
	 * @param se    The ScreenElement to be inserted
	 * @param index The index to replace with the new ScreenElement
	 */
	public void replace(ScreenElement se, int index) {
		elements.set(index, se);
		repaint();
	}// replace

	/**
	 * Removes the ScreenElement in the elements ArrayList of the index provided
	 * 
	 * @param index The index to remove
	 */
	public void remove(int index) {
		elements.remove(index);
	}// remove

	/**
	 * Removes the ScreenElement in the elements ArrayList of the id provided
	 * 
	 * @param id The index to remove
	 */
	public void removeID(String id) {
		elements.remove(getIndex(id));
	}// removeID

	/**
	 * Returns an ArrayList containing every instance of a certain object type
	 * 
	 * @param type The object type to search for
	 * @return The ArrayList containing every instance of the desired type
	 */
	public ArrayList<ScreenElement> getAllOfType(String type) {
		ArrayList<ScreenElement> typeArr = new ArrayList<ScreenElement>();
		// You gotta add your type to the list if its not here
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i) instanceof Block) {
				if (type.equals("block")) {
					typeArr.add(elements.get(i));
				}
			}
			if (elements.get(i) instanceof Button) {
				if (type.equals("button")) {
					typeArr.add(elements.get(i));
				}
			}

		}
		return typeArr;
	}// getAllOfType

	/**
	 * Removes all ScreenElements in the elements ArrayList of the id provided
	 * 
	 * @param id The index to remove
	 */
	public void removeAllID(String id) {
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getID().contains(id)) {
				elements.remove(i);
				i--;
			}
		}
	}// removeAllID

	/**
	 * Gets the index in the ArrayList elements of whatever id is given
	 * 
	 * @param id The id to search for in the ArrayList elements
	 * @return the index of the id in the ArrayList elements
	 */
	public int getIndex(String id) {
		int index = -1;
		for (int i = 0; i < elements.size(); i++) {
			ScreenElement element = elements.get(i);
			if (element.getID().equals(id)) {
				index = i;
				i = elements.size();
			}
		}
		return index;
	}// getIndex

	/**
	 * @param id The id to get the ScreenElement of
	 * @return a copy of the ScreenElement at that id
	 */
	public ScreenElement getScreenElement(String id) {
		int index = getIndex(id);
		return elements.get(index);
	}// getScreenElement

	/**
	 * Saves the ArrayList elements to the given directory
	 * 
	 * @param file The directory to save the elements to
	 */
	public void saveElements(String file) {
		ScreenFile io = new ScreenFile();
		io.writeArrayList(elements, file);
	}// saveElements

	/**
	 * Reads the ArrayList elements to from given directory
	 * 
	 * @param file The directory to read the elements from
	 */
	public void loadElements(String file) {
		ScreenFile io = new ScreenFile();
		elements = io.readArrayList(file);
	}// loadElements

	/**
	 * Removes every item from the ArrayList elements, thus making the screen black
	 */
	public void clearScreen() {
		elements.clear();
	}// clearScreen

	/**
	 * Returns the frame object
	 * 
	 * @return The frame object
	 */
	public JFrame getFrame() {
		return frame;
	}// getFrame

	/**
	 * Returns the ratio one must multiply the current windowX to scale to 720p
	 * 
	 * @return The X ratio
	 */
	public double getXRatio() {
		return ratioX;
	}// getXRatio

	/**
	 * Returns the ratio one must multiply the current windowY to scale to 720p
	 * 
	 * @return The Y ratio
	 */
	public double getYRatio() {
		return ratioY;
	}// getYRatio

	/**
	 * Starts a new thread and makes the game start
	 */
	public void runGame() {
		//Start game thread
		Thread gameThread = new Thread() {
			public void run() {
				game.start();
			}
		};
		gameThread.start();

		//Start keyboard thread
		Thread keebThread = new Thread() {
			public void run() {
				keeb.startKeyListener(frame);
			}
		};
		keebThread.start();

	}// runGame

	/**
	 * Returns the GUIElements that the screen made
	 * @return gooey
	 */
	public GUIElements guiElements() {
		return gooey;
	}// guiElements

	/**
	 * Loads the base blocks into an array according to BlockTypes
	 */
	public void loadBlocks() {
		String[] blockList = BlockTypes.blockList;
		for (int i = 0; i < blockList.length; i++) {
			Block block = new Block(-1, -1, "loadblock", blockList[i], false);
			//Load block image
			Image image = Toolkit.getDefaultToolkit().getImage(block.getImage());
			//Slap that shit in
			blockPics[i] = image;
		}
	}//loadBlocks

	/**
	 * Returns the images for each block
	 * @return blockPics
	 */
	public Image[] getBlockPics() {
		return blockPics;
	}// getBlockPics

	
	/**
	 * Removes overlapping items on screen
	 */
	public void screenClean() {
		int n = 0;
		System.out.println(elements.size());
		// Every item, searching through each item infront of it
		for (int i = 0; i < elements.size(); i++) {
			for (int j = i + 1; j < elements.size(); j++) {
				//If they have the same coordinates
				if (elements.get(i).getX() == elements.get(j).getX()
						&& elements.get(i).getY() == elements.get(j).getY()) {
					//Yeet that bitch
					elements.remove(i);
					//Deletion count
					n++;
				}
			}
		}
		//Tell me why
		System.out.println(elements.size());
		System.out.println("Removed " + n + " useless elements");

		//Loop if that bitch isnt done
		if (n != 0) {
			screenClean();
		}

	}//screenClean
}// Screen