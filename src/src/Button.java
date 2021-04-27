package src;

import java.awt.Color;

/**
 * The class for every Button to be displayed on screen
 * 
 * @author Riley Power
 * @version April 26 2021
 *
 */
public class Button extends ScreenElement {
	private int width;
	private int height;
	private Color color;

	/**
	 * 
	 * @param x      The x coordinate of the button on screen (Top Left)
	 * @param y      The y coordinate of the button on screen (Top Left)
	 * @param width  The width of the button
	 * @param height The height of the button
	 * @param color  The color of the button
	 * @param id     The id of the button
	 */
	public Button(int x, int y, int width, int height, Color color, String id) {
		super(x, y, id);
		this.width = width;
		this.height = height;
		this.color = color;
	}// Button

	/**
	 * Returns the desired width of the button
	 * 
	 * @return the width of the button
	 */
	public int getWidth() {
		return width;
	}// getWidth

	/**
	 * Accessor Method for height
	 * 
	 * @return the height of the button
	 */
	public int getHeight() {
		return height;
	}// getHeight

	/**
	 * Accessor Method For color
	 * 
	 * @return the color of the button
	 */
	public Color getColor() {
		return color;
	}// getColor

	/**
	 * Mutator Method for color
	 * 
	 * @param color The color to make the button
	 */
	public void setColor(Color color) {
		this.color = color;
	}// setColor

}
