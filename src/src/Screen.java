package src;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Everything to do with drawing the screen, getting mouse movement, and
 * anything to do with the main window is performed in this class
 * 
 * @version January 24, 2021
 * @author Riley Power
 *
 */
public class Screen extends JPanel {
	// The JFrame to draw on
	private JFrame frame;

	// Everything to be displayed on screen is stored here
	private ArrayList<ScreenElement> elements = new ArrayList<ScreenElement>();

	// Starts the game but doesn't display it until the start method is called
	private Color backgroundColor = Color.WHITE;

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

	// IO objects
	private MouseHandler mouse = new MouseHandler();
	private KeebHandler keeb = new KeebHandler();

	// Time management (Literally)
	private int fps = 60;
	private long nextFrame = System.currentTimeMillis() + (1000 / fps);

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
				// Wait for next frame time
			}
			nextFrame = System.currentTimeMillis() + (1000 / fps);
		}

	}// Screen

	/**
	 * Constructs the JFrame, all other required objects
	 */
	private void frameSetup() {
		frame = new JFrame("Platformer | By Riley Power");
		frame.add(this);
		frame.setSize(windowX, windowY);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		keeb.startKeyListener(frame);
		mouse.start(frame);
		game = new Game(this, keeb, mouse);
	}// frameSetup

	/**
	 * Lmao JFrame buttons look like shit let me just re make an existing code base
	 * Displays everything in the elements ArrayList according to it's parameters
	 * 
	 * @param g The canvas to paint every object to
	 */
	public void paint(Graphics g) {
		// Main Paint Loop
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		// Loops through each item in the ArrayList and paints it depending which object
		// type it is
		for (int i = 0; i < elements.size(); i++) {
			int scroll = game.getScroll();
			ScreenElement element = elements.get(i);

			if (element instanceof Player) {
				Player player = (Player) element;
				Image image = Toolkit.getDefaultToolkit().getImage(player.getImage());
				g2d.drawImage(image, (int) ((player.getX() - scroll) * ratioX), (int) (player.getY() * ratioY),
						(int) (image.getHeight(this) * ratioX * player.getScale()),
						(int) (image.getWidth(this) * ratioY * player.getScale()), null);
			}

			if (element instanceof Block) {
				Block block = (Block) element;
				Image image = Toolkit.getDefaultToolkit().getImage(block.getImage());
				g2d.drawImage(image, (int) ((block.getX() - scroll) * ratioX), (int) (block.getY() * ratioY),
						(int) (image.getHeight(this) * ratioX), (int) (image.getWidth(this) * ratioY), null);
			}
		}

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

		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i) instanceof Block) {
				if (type.equals("block")) {
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
			if (elements.get(i).getID().equals(id)) {
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
	 * Sets the background color of the window
	 * 
	 * @param color The color to set the background to
	 */
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
		repaint();
	}// setBackgroundColor

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
		Thread gameThread = new Thread() {
			public void run() {
				game.start();
			}
		};
		gameThread.start();
	}// runGame
}// Screen