package src;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Gets the state of the mouse and returns it to whatever needs the input
 * 
 * @author Riley Power
 * @version April 26 2021
 */
public class MouseHandler extends JPanel implements MouseListener, MouseWheelListener {

	// Mouse coordinates
	private double mouseX = 0;
	private double mouseY = 0;

	// Mouse button flag
	private boolean isMousePressed = false;

	// The current mouse button pressed
	private char mouseButton = ' ';

	// IO objects
	private Frame frame;
	private Screen screen;
	private ButtonActions action;

	// Mouse wheel position
	private char mouseWheel = ' ';

	/**
	 * Starts the mouse handler (listener) using the provided JFrame as the screen
	 * 
	 * @param frame  The frame to listen to
	 * @param screen The screen to interface with
	 * @param action The object to call if a button is pressed
	 */
	public void start(Frame frame, Screen screen, ButtonActions action) {
		this.frame = frame;
		this.screen = screen;
		this.action = action;
		// Mouse Listeners
		frame.addMouseListener(this);
		frame.addMouseWheelListener(this);

	}// start

	/**
	 * Activates when the mouse button is pressed, updates the coordinates and sets
	 * off the appropriate flags
	 * 
	 * @param e The mouse event that triggers the method
	 */
	public void mousePressed(MouseEvent e) {
		// Sets the mouse button state
		mouseButton = determineMouseButton(e);
		// Looks for buttons to press
		if (!findClicks()) {
			// Updates mouse position
			mouseX = e.getX();
			mouseY = e.getY();
			// Set mouse to be pressed
			isMousePressed = true;
		}
	}// mousePressed

	/**
	 * Activates when the mouse button is released, updates the coordinates and sets
	 * off the appropriate flags
	 * 
	 * @param e The mouse event that triggers the method
	 */
	public void mouseReleased(MouseEvent e) {
		// Sets the mouse button state
		mouseButton = determineMouseButton(e);

		// Updates mouse position
		mouseX = e.getX();
		mouseY = e.getY();
		mouseY = e.getY();
		// Set mouse to be released
		isMousePressed = false;
	}// mouseReleased

	/**
	 * Returns if the mouse button is pressed down or not
	 * 
	 * @return isMousePressed
	 */
	public boolean isMousePressed() {
		return isMousePressed;
	}// isMousePressed

	/**
	 * Makes the mouse button virtually released
	 */
	public void removePressedFlag() {
		isMousePressed = false;
	}// removePressedFlag

	/**
	 * Returns the X position of the mouse on the screen
	 * 
	 * @return mouseX
	 */
	public int getX() {
		// Frame position
		Point frm = frame.getLocation();
		// Global mouse position
		Point mse = MouseInfo.getPointerInfo().getLocation();
		// Screen mouse position
		mouseX = (mse.getX() - frm.getX() - 8) / screen.getXRatio();
		return (int) mouseX;
	}// getX

	/**
	 * Returns the Y position of the mouse on the screen
	 * 
	 * @return mouseY
	 */
	public int getY() {
		// Frame position
		Point frm = frame.getLocation();
		// Global mouse position
		Point mse = MouseInfo.getPointerInfo().getLocation();
		// Screen mouse position
		mouseY = (mse.getY() - frm.getY() - 32) / screen.getYRatio();
		return (int) mouseY;
	}// getY

	/**
	 * Determines what button on the mouse was pressed
	 * 
	 * @param e The MouseEvent which triggered this
	 * @return The button pressed
	 */
	private char determineMouseButton(MouseEvent e) {
		// Default to left
		char mb = 'L';
		if (SwingUtilities.isMiddleMouseButton(e)) {
			mb = 'M';
		} else if (SwingUtilities.isRightMouseButton(e)) {
			mb = 'R';
		}
		return mb;
	}// determineMouseButton

	/**
	 * Returns the current mouse button pressed
	 * 
	 * @return mouseButton
	 */
	public char getMouseButton() {
		return mouseButton;
	}// getMouseButton

	/**
	 * Updates the mouse wheel position
	 * 
	 * @param e The MouseWheelEvent that triggered this
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			mouseWheel = 'U';
			// Mouse wheel up
		} else {
			mouseWheel = 'D';
			// Mouse wheel down
		}
	}

	/**
	 * Returns the state of the mouse wheel
	 * 
	 * @return mouseWheel
	 */
	public char getMouseWheel() {
		return mouseWheel;
	}// getMouseWheel

	/**
	 * Sets the mouse wheel to be blank
	 */
	public void resetMouseWheel() {
		mouseWheel = ' ';
	}

	/**
	 * Finds what button the user has clicked on
	 * 
	 * @return if the user clicked on a button or not
	 */
	public boolean findClicks() {
		boolean foundClick = false;
		// Get every button
		ArrayList<ScreenElement> buttons = screen.getAllOfType("button");
		// Update mouse coordinates
		getX();
		getY();
		// For every button
		for (int i = 0; i < buttons.size(); i++) {
			// Copy it for easy operation
			Button button = (Button) buttons.get(i);
			// If in the button's bounds
			if (mouseX > button.getX() && mouseX < button.getX() + button.getHeight() && mouseY > button.getY()
					&& mouseY < button.getY() + button.getWidth()) {
				// Call for the action to be found
				action.performAction(button.getID());
				// Let everyone know
				foundClick = true;
			}
		}
		return foundClick;
	}

	/**
	 * Unused but would activate when the mouse is clicked in any way
	 */
	public void mouseClicked(MouseEvent e) {
	}// mouseClicked

	/**
	 * Unused but would activate when the mouse enters the screen
	 */
	public void mouseEntered(MouseEvent e) {
	}// mouseEntered

	/**
	 * Unused but would activate when the mouse exits the screen
	 */
	public void mouseExited(MouseEvent e) {
	}// mouseExited
}// MouseHandler
