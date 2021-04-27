package src;

public class LevelInfo extends ScreenElement{
	private int currentStroke = 0;
	public int playerStartX = 0;
	public int playerStartY = 0;
	
	
	public LevelInfo() {
		super(0,0,"lvlinfo");
	}
	
	public void setCurrentStroke(int n) {
		currentStroke = n;
	}
	
	public void getCurrentStroke() {
		
	}
	
	
}
