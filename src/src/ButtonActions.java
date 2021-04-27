package src;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Performs any action associated with a button press
 * 
 * @author Riley Power
 * @version April 26 2021
 */
public class ButtonActions {
	// Interface Objects
	private Screen screen;
	private JFrame frame;
	private Game game;

	public ButtonActions(Screen screen, JFrame frame, Game game) {
		this.screen = screen;
		this.frame = frame;
		this.game = game;
	}// ButtonActons

	/**
	 * Takes the string associated with the button and does the associated action
	 * 
	 * @param action The button's id
	 */
	public void performAction(String action) {
		// Declare variables
		JFileChooser fc;
		int returnVal;

		// Make case irrelevant
		action = action.toLowerCase();

		switch (action) {

		// Load from file button in level editor
		case ("load"):
			// Java File Chooser Window
			fc = new JFileChooser();
			// Sets default directory to inside the game
			fc.setCurrentDirectory(new File("lvl"));
			// Says if a file was chosen or not
			returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// If file is chosen load file
				File file = fc.getSelectedFile();
				System.out.println(file.getPath());
				game.loadLevel(file.getPath());
			} else {
				// canceled
			}
			break;

		// Save file button in level editor
		case ("save"):
			// Remove overlapping blocks to reduce file size
			screen.screenClean();
			// Java File Chooser Window
			fc = new JFileChooser();
			// Sets default directory to inside the game
			fc.setCurrentDirectory(new File("lvl"));
			// Removes buttons from screen so they don't save too
			screen.guiElements().levelEdit(false);
			// Pause game so the array isn't updated while trying to save
			game.setFlag("paused", true);
			returnVal = fc.showSaveDialog(null);
			// Says if a file was chosen or not
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// If file is chosen save file
				File file = fc.getSelectedFile();
				// Add .lvl to the end
				String dir = file.getPath();
				if (!dir.substring(dir.length() - 4).equals(".lvl")) {
					dir = dir + ".lvl";
				}
				// Save to that file
				game.saveLevel(dir);

			} else {
				// canceled
			}
			// Resume game
			game.setFlag("paused", false);
			break;

		// Toggle Grid button in level editor
		case ("grid"):
			// Update game grid flag to its opposite value
			boolean gridFlag = game.getFlag("grid");
			game.setFlag("grid", !gridFlag);

		}
	}// performAction

}// ButtonActions
