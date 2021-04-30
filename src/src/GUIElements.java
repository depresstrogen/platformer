package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Every command to show GUI elements like buttons on the screen
 * 
 * @author Riley Power
 * @version April 26 2021
 */
public class GUIElements {
	private Font font = new Font("Comic Sans MS", Font.PLAIN, 24);
	// IO Objects
	private Screen screen;
	private boolean selectedBlockOn = false;
	private Block selectedItem;

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

	public void selectedItem(Graphics2D g2d) {
		
		try {
			setSelect(screen.getScreenElement(selectedItem.getID()));
			Image img = Toolkit.getDefaultToolkit().getImage(selectedItem.getImage());
			
			g2d.setColor(Color.BLACK);
			g2d.drawRect(879, 552, 384, 128);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(880, 553, 383, 127);
			if(selectedItem.getHeight() == 180) {
				img = ImageTools.rotateImage(img, 180);
			}
			g2d.drawImage(img, 900, 570, 96, 96, null);
			g2d.setFont(font);
			g2d.setColor(Color.BLACK);
			g2d.drawString(selectedItem.getType(), 1020 , 590);
			if(selectedItem.getWidth() == 3) {
				g2d.setColor(Color.CYAN);
				g2d.fillRect(900, 570, 96, 96);
				g2d.setColor(Color.BLACK);
				g2d.drawString("Spawn X: " + selectedItem.getX(), 1020 , 630);
				g2d.drawString("Spawn Y: " + selectedItem.getY(), 1020 , 660);
				g2d.drawString("Spawn", 910, 625);
				
			} else {
				g2d.drawString("X: " + selectedItem.getX(), 1020 , 630);
				g2d.drawString("Y: " + selectedItem.getY(), 1020 , 660);
			}
		} catch (Exception e) {

		}
	}

	public void setSelect(ScreenElement element) {
		if(element instanceof Block) {
			selectedItem = (Block) element;
		}
		if (element instanceof FireHopper) {
			FireHopper enemy = (FireHopper) element;
			String type = "";
			String image = "";
			if(enemy instanceof FireHopper) {
				type = "firehopper";
			}
			int secret = 180;
			if(enemy.getState().equals("up")) {
				secret = 0;
			}
			Block block = new Block(enemy.getX(), enemy.getY(), enemy.getID(), type, true, secret, 1);
			block.setImage("img/enemy/firehopper.png");
			selectedItem = block;
		}
		if (element instanceof Player) {
			Player player = (Player) element;
			String type = "";
			String image = "";
			int secret = 180;
			if(player.getState().equals("up")) {
				secret = 0;
			}
			Block block = new Block(player.getX(), player.getY(), player.getID(), "player", true, secret, 2);
			block.setImage(player.getImage());
			selectedItem = block;
		}
		if (element instanceof LevelInfo) {
			LevelInfo info = (LevelInfo) element;
			Block block = new Block(info.getSpawnX(), info.getSpawnY(), info.getID(), "level info", true, 0, 3);
			selectedItem = block;
		}
	}

	private boolean getSelectedBlockOn() {
		return selectedBlockOn;
	}

	private void toggleSelectedBlock(boolean selectedBlock) {
		selectedBlockOn = selectedBlock;
	}

}// GUIElements
