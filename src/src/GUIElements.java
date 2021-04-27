package src;

import java.awt.Color;

import javax.swing.JFrame;

/**
 * Every command to show GUI elements like buttons on the screen
 * 
 * @author Riley Power
 * @version April 26 2021
 */
public class GUIElements {
	// IO Objects
	private Screen screen;

	/**
	 * Constructor class for the GUIElements class
	 * 
	 * @param screen The screen to show the elements on
	 * @param frame  The frame associated with the screen
	 */
	public GUIElements(Screen screen, JFrame frame) {
		this.screen = screen;
	}// GUIElements

	/**
	 * Displays or hides the level edit GUI Elements according to the parameter
	 * 
	 * @param option True to show the elements, false to hide them
	 */
	public void levelEdit(boolean option) {
		// Delete so no memory leaks
		screen.removeAllID("load");
		screen.removeAllID("save");
		screen.removeAllID("grid");
		// Shows them
		if (option) {
			TextButton load = new TextButton(16, 16, 50, 90, Color.RED, "load", "Load");
			screen.add(load);
			TextButton save = new TextButton(160, 16, 50, 90, Color.BLUE, "save", "Save");
			screen.add(save);
			TextButton grid = new TextButton(1050, 16, 50, 165, Color.GREEN, "grid", "Toggle Grid");
			screen.add(grid);
		}
	}// levelEdit
}// GUIElements
