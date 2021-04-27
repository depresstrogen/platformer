package src;

import java.awt.Color;

/**
 * Combination ~~pizza hut and taco bell~~ Button and Text objects
 * 
 * @author Riley Power
 * @version April 26 2021
 */
public class TextButton extends Button {
	// Text to display
	String text;
	// Font to use
	String font;

	/**
	 * TextButton constructor
	 * 
	 * @param x      The x coordinate of the button on screen (Top Left)
	 * @param y      The y coordinate of the button on screen (Top Left)
	 * @param width  The width of the button
	 * @param height The height of the button
	 * @param color  The color of the button
	 * @param id     The id of the button
	 * @param text   The text the button should show
	 */
	public TextButton(int x, int y, int width, int height, Color color, String id, String text) {
		// Hehe button
		super(x, y, width, height, color, id);
		// Hehe text
		this.text = text;
		font = "Helvetica";
	}

	/**
	 * Returns the font the text should be
	 * 
	 * @return font
	 */
	public String getFont() {
		return font;
	}// getFont

	/**
	 * Returns the text to be displayed
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}// getText
}
