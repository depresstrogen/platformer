package src;

public class Block extends ScreenElement {
	private String imageDir;
	public Block (int x, int y, String id, String type)   {
		super(x,y,id);
		
		if(type.equals("ground")) {
			imageDir = "img/blocks/ground.png";
		}
		
	}
	
	public String getImage() {
		return imageDir;
	}
}
