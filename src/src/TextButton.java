package src;

import java.awt.Color;

public class TextButton extends Button {

	String text;
	String font;
	public TextButton(int x, int y, int width, int height, Color color, String id, String text) {
		super(x, y, width, height, color ,id);
		this.text = text;
		font = "Helvetica";
	}
	
	public String getFont() {
		return font;
	}// getFont
	
	public String getText() {
		return text;
	}

}
