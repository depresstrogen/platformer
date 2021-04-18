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
	private JFrame frame;
	private ArrayList<ScreenElement> elements = new ArrayList<ScreenElement>();
	private boolean[] keyboard = new boolean[255];
	private MouseHandler mouse = new MouseHandler();
	// The id of the ScreenElement that was last clicked
	private String lastClick = "";
	// A copy of the ScreenElement that was last clicked
	private Object lastClickObject;
	// Used so that unless a new click is made the clicking methods are not executed
	private boolean newClick = false;
	// Starts the game but doesn't display it until the start method is called
	private Color backgroundColor = Color.WHITE;
	private int lastMouseX = 0;
	private int lastMouseY = 0;

	private int windowX;
	private int windowY;
	private  final int BASEX = 1280;
	private final int BASEY = 720;
	private double ratioX = 1;
	private double ratioY = 1;
	
	public Game game;
	
	private int num = 0;

	/**
	 * Constructs the jFrame, mouse listener and key listener
	 * 
	 * @param height How many pixels tall the window will be
	 * @param width  How many pixels wide the window will be
	 */
	public Screen(int height, int width) {
		windowX = width;
		windowY = height;
		int fps = 60;
		long nextFrame = System.currentTimeMillis() + (1000 / fps);
		// Start JFrame
		frame = new JFrame("Platformer | By Riley Power");
		frame.add(this);
		frame.setSize(height, width);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Keyboard Inputs
		// Would be in another method but it leeches off JFrame lol
		KeebHandler keeb = new KeebHandler();
		keeb.startKeyListener(frame);
		// addMouseListener(this);

		MouseHandler mouse = new MouseHandler();
		mouse.start(frame);

		BufferedImage playerPic;

		int i = 0;
		game = new Game(this, keeb, mouse);
		
		Thread gameThread = new Thread() {
			
			public void run() {
				game.start();
			}
			
		};
		
		gameThread.start();
		while (true) {
			windowX = frame.getWidth();
			windowY = frame.getHeight();

			ratioX = (double) windowX / BASEX;
			ratioY = (double) windowY / BASEY;

			//System.out.println(windowX + " " + windowY);
			//System.out.println(ratioX + " " + ratioY);
			System.out.print("");
			i++;
			while (nextFrame > System.currentTimeMillis()) {

			}
			//System.out.println("FPS = " + num*60);
			num = 0;
//			System.out.print("Frame " + i + " ");
//			keeb.inputPrint();
//			System.out.println();
//			
			nextFrame = System.currentTimeMillis() + (1000 / fps);
		}

	}// Screen

	/**
	 * @param g The canvas to paint every object to
	 */
	public void paint(Graphics g) {
		// Main Paint Loop
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(backgroundColor);

		g2d.setColor(Color.RED);

		// Loops through each item in the ArrayList and paints it depending which object

		for (int i = 0; i < elements.size(); i++) {
			int scroll = game.getScroll();
			ScreenElement element = elements.get(i);

			if (element instanceof Player) {
				Player player = (Player) element;
				Image image = Toolkit.getDefaultToolkit().getImage(player.getImage());
				g2d.drawImage(image,
						(int) ((player.getX() - scroll )* ratioX),
						(int) (player.getY() * ratioY),
						(int) (image.getHeight(this) * ratioX * player.getScale()),
						(int) (image.getWidth(this) * ratioY * player.getScale()), null);
			}
			if (element instanceof Block) {
				Block block = (Block) element;
				Image image = Toolkit.getDefaultToolkit().getImage(block.getImage());
				g2d.drawImage(image,
						(int) ((block.getX() - scroll) * ratioX),
						(int) (block.getY() * ratioY),
						(int) (image.getHeight(this) * ratioX),
						(int) (image.getWidth(this) * ratioY), null);
			}
		}
		num++;
		
	}// paint

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
	 * @see whatClicked
	 */
	public void replace(ScreenElement se, int index) {
		elements.set(index, se);
		repaint();
	}// replace

	public void remove(int index) {
		elements.remove(index);
	}
	
	public void removeID(String id) {
		elements.remove(getIndex(id));
	}
	
	public ArrayList<ScreenElement> getAllOfType(String type) {
		ArrayList<ScreenElement> typeArr = new ArrayList<ScreenElement>();
		
		for(int i = 0; i < elements.size(); i++) {
			if (elements.get(i) instanceof Block) {
				if (type.equals("block")) {
					typeArr.add(elements.get(i));
				}
			}
		}
		return typeArr;
	}
	
//	/**
//	 * Gets the x and y of the mouse then calls whatClicked
//	 * 
//	 * @param e The event which triggers this method
//	 * 
//	 */
//	public void mouseReleased(MouseEvent e) {
//		int mouseX = e.getX();
//		int mouseY = e.getY();
//		whatClicked(mouseX, mouseY);
//	}// mouseReleased
//
//	/**
//	 * Loops through every element in the array and if the mouseX and mouseY are
//	 * inside the ScreenElement it registers a click and sends it to the
//	 * MouseHandler
//	 * 
//	 * @param mouseX the x coordinate you would like to check for a clickable object
//	 * @param mouseY the y coordinate you would like to check for a clickable object
//	 * @see MouseHandler
//	 */
//	private void whatClicked(int mouseX, int mouseY) {
//		System.out.println(mouseX + " " + mouseY);
//		lastMouseX = mouseX;
//		lastMouseY = mouseY;
//		for (int i = 0; i < elements.size(); i++) {
//			
//		}
//	}// whatClicked

//	/**
//	 * Gets the id of the last clicked ScreenElement, and sets newClick to false
//	 * 
//	 * @return The id of the last clicked ScreenElement
//	 */
//	public String getLastClick() {
//		newClick = false;
//		return lastClick;
//	}// getLastClick
//
//	/**
//	 * 
//	 * @return A copy of the object which was last clicked
//	 */
//	public Object getLastClickObject() {
//		return lastClickObject;
//	}// getLastClickObject
//
//	/**
//	 * 
//	 * @return if the most recent click was "real" or not
//	 */
//	public boolean isNewClick() {
//		return newClick;
//	}// isNewClick

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
	 * Accessor Method for keyboard[][]
	 * 
	 * @return An array of each possible letter, and if it is currently pressed or
	 *         not
	 */
	public boolean[] getKeyboard() {
		return keyboard;
	}// getKeyboard

	
	public JFrame getFrame() {
		return frame;
	}
	
	public MouseListener getMouseListener() {
		return mouse;
	}
	
	public double getXRatio()  {
		return ratioX;
	}
	
	public double getYRatio()  {
		return ratioY;
	}
	
//	/**
//	 * Accessor Method for lasyMouseX
//	 * 
//	 * @return the mouseX of the last click
//	 */
//	public int getLastMouseX() {
//		return lastMouseX;
//	}// getLastMouseX
//
//	/**
//	 * Accessor Method for lasyMouseY
//	 * 
//	 * @return the mouseY of the last click
//	 */
//	public int getLastMouseY() {
//		return lastMouseY;
//	}// getLastMouseY
}// Screen