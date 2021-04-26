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
		JFileChooser fc;
		int returnVal;
		
		switch (action) {

		case ("load"):
			fc = new JFileChooser();
			fc.setCurrentDirectory(new File ("lvl"));
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
			screen.screenClean();
			fc = new JFileChooser();
			fc.setCurrentDirectory(new File ("lvl"));
			screen.guiElements().levelEdit(false);
			game.setFlag("paused", true);
			returnVal = fc.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String dir = file.getPath();
				if(!dir.substring(dir.length() - 4).equals(".lvl")) {
					dir = dir + ".lvl";
				}
				game.saveLevel(dir);

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
