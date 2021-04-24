package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 * Handles all keyboard inputs (More a KeebListener but who the fuck is keeping
 * track? no one thats what i thought)
 * 
 * @author Riley Power
 * @version April 19 2021
 */
public class KeebHandler {

	// Stores the state of each key
	private boolean keyboard[] = new boolean[255];

	/**
	 * Starts the key listener
	 * 
	 * @param frame The frame with the key listener
	 */
	public void startKeyListener(JFrame frame) {
		frame.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			/**
			 * Updates the flag corresponding to the key pressed
			 * 
			 * @param e The current key
			 */
			public void keyPressed(KeyEvent e) {
			
				
				System.out.println(e.getKeyCode());
				keyboard[e.getKeyCode()] = true;
			}// keyPressed

			/**
			 * Updates the flag corresponding to the key pressed
			 * 
			 * @param e The current key
			 */
			public void keyReleased(KeyEvent e) {
				keyboard[e.getKeyCode()] = false;
			}// keyReleased
		});
	}// startKeyListener

	/**
	 * Returns the state of every key
	 * 
	 * @return The keyboard array
	 */
	public boolean[] getKeys() {
		return keyboard;
	}// getKeys

	/**
	 * Returns the state of the key at the index
	 * 
	 * @param key the number (or char) of the desired key
	 * @return The state of the key specified by the key variable
	 */
	public boolean getKey(int key) {
		return keyboard[key];
	}// getKey
	
	public void setKey (int key, boolean value) {
		keyboard[key] = value;
	}

	/**
	 * Prints whenever a key is held down
	 */
	public void inputPrint() {
		for (int i = 0; i < keyboard.length; i++) {
			if (keyboard[i]) {
				System.out.print((char) i + " ");
			}
		}
	}// inputPrint
}// KeebHandler
