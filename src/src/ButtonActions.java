package src;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class ButtonActions {

	private Screen screen;
	private JFrame frame;
	private Game game;

	public ButtonActions(Screen screen, JFrame frame, Game game) {
		this.screen = screen;
		this.frame = frame;
		this.game = game;
	}

	public void performAction(String action) {
		action = action.toLowerCase();
		final JFileChooser fc = new JFileChooser();
		int returnVal;
		switch (action) {

		case ("load"):
			returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				System.out.println(file.getPath());
				game.loadLevel(file.getPath());
			} else {
				// canceled
			}
			break;

		case ("save"):
			screen.guiElements().levelEdit(false);
			game.setFlag("paused", true);
			returnVal = fc.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				game.saveLevel(file);

			} else {
				// canceled
			}
			game.setFlag("paused", false);
			break;
			
		case("grid"):
			boolean gridFlag = game.getFlag("grid");
			game.setFlag("grid", !gridFlag);
			
		}
	}

}
