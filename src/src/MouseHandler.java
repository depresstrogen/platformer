package src;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Gets the state of the mouse and returns it to whatever needs the input
 * 
 * @author Riley Power
 * @version April 19 2021
 */
public class MouseHandler extends JPanel implements MouseListener, MouseWheelListener {

	// Mouse coordinates
	private double mouseX = 0;
	private double mouseY = 0;

	// Mouse button flag
	private boolean isMousePressed = false;

	private char mouseButton = ' ';
	// Screen object
	private Frame frame;

	private char mouseWheel = ' ';
	private long mouseWheelTime = 0;

	/**
	 * Starts the mouse handler (listener) using the provided JFrame as the screen
	 * 
	 * @param frame The frame to listen to
	 */
	public void start(Frame frame) {
		this.frame = frame;
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
		mouseButton = determineMouseButton(e);
		mouseX = e.getX();
		mouseY = e.getY();
		isMousePressed = true;
	}// mousePressed

	/**
	 * Activates when the mouse button is released, updates the coordinates and sets
	 * off the appropriate flags
	 * 
	 * @param e The mouse event that triggers the method
	 */
	public void mouseReleased(MouseEvent e) {
		mouseButton = determineMouseButton(e);
		mouseX = e.getX();
		mouseY = e.getY();
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
		mouseX = mse.getX() - frm.getX();
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
		mouseY = mse.getY() - frm.getY();
		return (int) mouseY;
	}// getY

	private char determineMouseButton(MouseEvent e) {
		char mb = 'L';
		if (SwingUtilities.isLeftMouseButton(e)) {
			mb = 'L';
		} else if (SwingUtilities.isMiddleMouseButton(e)) {
			mb = 'M';
		} else if (SwingUtilities.isRightMouseButton(e)) {
			mb = 'R';
		}
		return mb;

	}

	public char getMouseButton() {
		return mouseButton;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println("m");

		if (e.getWheelRotation() < 0) {
			mouseWheel = 'U';
			System.out.println("mouse wheel Up");
		} else {
			mouseWheel = 'D';
			System.out.println("mouse wheel Down");
		}
		mouseWheelTime = System.currentTimeMillis();
	}

	public char getMouseWheel() {
		return mouseWheel;
	}
	
	public long getMouseWheelTime() {
		return mouseWheelTime;
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
