package src;

import java.util.ArrayList;

import javax.swing.JFrame;

public class Game {
	public Screen screen;
	public KeebHandler keeb;
	public MouseHandler mouse;
	public int playerX = 200;
	public int playerY = 0;
	
	public int playerSX = 0;
	public int playerSY = 0;
	
	public int velocity = 0;
	public boolean inJump = false;
	private boolean paint = false;
	private int scroll = 0;
	private int moveSpeed = 10;
	
	public Game(Screen screen, KeebHandler keeb, MouseHandler mouse) {
		this.screen = screen;
		this.keeb = keeb;
		this.mouse = mouse;
	}

	public void start() {
		int fps = 60;
		long nextFrame = System.currentTimeMillis() + (1000 / fps);

		for (int i = 0; i < 8*32; i++) {
			Block block = new Block(i, 200, "2", "ground");
			screen.add(block);
			System.out.print("added");
		}
		
		Player player = new Player(50, 50, "Player", "idle", "img/player/idle.gif", 1);
		screen.add(player);
		
		while (true) {
			
			if (keeb.getKey('A')) {
				playerX -= moveSpeed;
			}
			if (keeb.getKey((int) 'D')) {
				playerX += moveSpeed; 
			}
			if (keeb.getKey('W')) {
				playerY--;
			}
			if (keeb.getKey('S')) {
				playerY++;
			}
			if(keeb.getKey(0) && !inJump) {
				jump();
				inJump = true;
			}
			if(keeb.getKey(1)) {
				paint = true;
			} else {
				paint = false;
			}
			
			gravity();
			collisionCheck();
			scrollCheck();
			

			while (nextFrame > System.currentTimeMillis()) {
			}
			
			player.setX(playerX);
			player.setY(playerY);
			screen.remove(screen.getIndex("Player"));
			screen.add(player);
			nextFrame = System.currentTimeMillis() + (1000 / fps);
			
			if (mouse.isMousePressed()) {
				Block block = new Block(
						(int)((mouse.getX() - 8) / screen.getXRatio() + scroll),
						(int)((mouse.getY() - 32) / screen.getYRatio()),
						"MouseAdded", "ground");
				screen.add(block);
				System.out.print("added");
				if(!paint) {
					mouse.removePressedFlag();
				}
			}
		}

	}

	public void gravity() {
		velocity += 1;
		playerY += velocity;
		if (velocity > 10) {
			velocity = 10;
		}
	}
	
	
	public void jump() {
		playerY --;
		velocity = -15;
	}
	
	public void collisionCheck() {
		final int CHAR_WIDTH = 32;
		
		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");
		
		for (int i = 0; i < blocks.size(); i++) {
			Block block = (Block) blocks.get(i);
			if (block.getY() - CHAR_WIDTH < playerY && block.getY() + CHAR_WIDTH > playerY && block.getX() - CHAR_WIDTH < playerX
					&& block.getX() + CHAR_WIDTH > playerX) {
				
				//Top
				if (block.getY() - CHAR_WIDTH < playerY && block.getY() - (CHAR_WIDTH / 2) > playerY) {
					playerY = block.getY() - (CHAR_WIDTH - 1);
					velocity = 0;
					inJump = false;
				}
				//bottom
				else if (block.getY() + CHAR_WIDTH > playerY && block.getY() + (CHAR_WIDTH / 2) < playerY) {
					playerY = block.getY() + CHAR_WIDTH;
					velocity = 0;
				}
				//left
				else if (block.getX() - CHAR_WIDTH < playerX && block.getX() - (CHAR_WIDTH / 2) > playerX) {
					
						playerX = block.getX() - CHAR_WIDTH;
					
				}
				//right
				else if (block.getX() + CHAR_WIDTH > playerX && block.getX() + (CHAR_WIDTH / 2) < playerX) {
					
						playerX = block.getX() + CHAR_WIDTH;
					
				}
			}
		}

	}

	public void scrollCheck() {
		playerSX = (playerX - scroll);
		
		if(playerSX < 200) {
			scroll -= moveSpeed;
		}
		
		if(playerSX > 1000) {
			scroll += moveSpeed;
		}
		
		System.out.println(scroll);
	}
	
	public int getScroll() {
		return scroll;
	}
	// Medal System
}
