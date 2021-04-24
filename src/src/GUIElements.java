package src;

import java.awt.Color;

import javax.swing.JFrame;

public class GUIElements {
	private Screen screen;
	public GUIElements (Screen screen, JFrame frame) {
		this.screen = screen;
	}
	
	public void levelEdit(boolean option) {
		screen.removeAllID("load");
		screen.removeAllID("save");
		screen.removeAllID("grid");
		
		if (option) {
			TextButton load = new TextButton(16, 16, 50, 90, Color.RED, "load", "Load");
			screen.add(load);
			TextButton save = new TextButton(160, 16, 50, 90, Color.BLUE, "save", "Save");
			screen.add(save);
			TextButton grid = new TextButton(1050, 16, 50, 165, Color.GREEN, "grid", "Toggle Grid");
			screen.add(grid);
		}
	}
	
}
