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
		frame = new JFrame("Platformer | By Riley Power");
		frame.add(this);
		frame.setSize(windowX, windowY);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadBlocks();
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
	/**
	 *
	 */
	/**
	 *
	 */
	public void paintComponent(Graphics g) {
		// Main Paint Loop
		super.paintComponent(g);
		Graphics2D g2d;

		int scroll = game.getScroll();

		BufferedImage screenImg = new BufferedImage(BASEX, BASEY, BufferedImage.TYPE_INT_RGB);

		if (ratioX == 1.0 && ratioY == 1.0) {
			g2d = (Graphics2D) g;
		} else {
			g2d = screenImg.createGraphics();
		}

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, BASEX, BASEY);

		// Loops through each item in the ArrayList and paints it depending which object
		// type it is
		if (game.getFlag("grid")) {
			g2d.setColor(Color.BLACK);
			for (double i = 0; i < 720; i += 32) {
				g2d.drawLine(0, (int) i, (int) (1280), (int) i);
			}
			for (double i = -1; i < 1280; i += 32) {
				g2d.drawLine((int) (i - (scroll % 32)), 0, (int) (i - (scroll % 32)), (int) (720));
			}
		}

		for (int i = 0; i < elements.size(); i++) {
			ScreenElement element = elements.get(i);
			if (element instanceof Block) {
				if (element.getX() < scroll - 30 || element.getX() > scroll + 1280) {
				} else {
					Block block = (Block) element;
					g2d.drawImage(blockPics[block.getBlockCode()], (int) ((block.getX() - scroll)),
							(int) (block.getY()), null);
				}
			} else if (element instanceof Player) {

				try {
					Player player = (Player) element;
					Image img = Toolkit.getDefaultToolkit().getImage(player.getImage());

					g2d.drawImage(img, (int) ((player.getX() - scroll)), (int) (player.getY()),
							(int) (img.getHeight(this) * player.getScale()),
							(int) (img.getWidth(this) * player.getScale()), null);

				} catch (Exception e) {

				}

			} else if (element instanceof TextButton) {
				TextButton button = (TextButton) element;
				g2d.setColor(button.getColor());
				g2d.fillRect((int) ((button.getX())), (int) (button.getY()), (int) (button.getHeight()),
						(int) (button.getWidth()));
				int size = button.getWidth() / 2;
				g2d.setColor(Color.black);
				g2d.setFont(new Font(button.getFont(), Font.PLAIN, size));
				g2d.drawString(button.getText(), button.getX() + 16,
						button.getY() + (int) ((button.getWidth() + (size / 2)) / 2));
			} else if (element instanceof Button) {
				Button button = (Button) element;
				g2d.setColor(button.getColor());
				g2d.fillRect((int) ((button.getX())), (int) (button.getY()), (int) (button.getHeight()),
						(int) (button.getWidth()));
			} else if (elements.get(i) instanceof Text) {
				Text text = (Text) elements.get(i);
				g2d.setColor(text.getColor());
				g2d.setFont(new Font(text.getFont(), Font.PLAIN, text.getFontSize()));
				g2d.drawString(text.getText(), text.getX(), text.getY());
			}

		}

		if (ratioX == 1 && ratioY == 1) {

		} else {
			Graphics2D paint = (Graphics2D) g;
			paint.drawImage(screenImg, 0, 0, (int) (1280 * ratioX), (int) (720 * ratioY), null);
		}
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
		Thread gameThread = new Thread() {
			public void run() {
				game.start();
			}
		};
		gameThread.start();

		Thread keebThread = new Thread() {
			public void run() {
				keeb.startKeyListener(frame);
			}
		};
		keebThread.start();

	}// runGame

	public GUIElements guiElements() {
		return gooey;
	}

	public void loadBlocks() {
		String[] blockList = BlockTypes.blockList;
		for (int i = 0; i < blockList.length; i++) {
			Block block = new Block(-1, -1, "loadblock", blockList[i], false);
			Image image = Toolkit.getDefaultToolkit().getImage(block.getImage());
			blockPics[i] = image;
		}
	}
	
	public Image[] getBlockPics() {
		return blockPics;
	}

	public void screenClean() {
		int n = 0;
		System.out.println(elements.size());

		for (int i = 0; i < elements.size(); i++) {
			for (int j = i + 1; j < elements.size(); j++) {
				if (elements.get(i).getX() == elements.get(j).getX()
						&& elements.get(i).getY() == elements.get(j).getY()) {
					elements.remove(i);

					n++;
					j = elements.size();
				}
			}
		}

		System.out.println(elements.size());
		System.out.println("Removed " + n + " useless elements");

		if (n != 0) {
			screenClean();
		}

	}
}// Screen