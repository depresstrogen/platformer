package src;

import java.util.ArrayList;

import javax.swing.JFrame;

public class Game {
	public Screen screen;
	public KeebHandler keeb;
	public MouseHandler mouse;
	public int playerX = 0;
	public int playerY = 0;
	public int velocity = 0;
	
	public boolean inJump = false;
	public Game(Screen screen, KeebHandler keeb, MouseHandler mouse) {
		this.screen = screen;
		this.keeb = keeb;
	}

	public void start() {
		int fps = 60;
		long nextFrame = System.currentTimeMillis() + (1000 / fps);

		for (int i = 0; i < 8; i++) {
			Block block = new Block(i * 32, 200, "2", "ground");
			screen.add(block);
			System.out.print("added");
		}

		Block block1 = new Block(320, 200, "2", "ground");
		screen.add(block1);
		Block block2 = new Block(256, 350, "2", "ground");
		screen.add(block2);
		Block block3 = new Block(560, 350, "2", "ground");
		screen.add(block3);
		Block block4 = new Block(460, 400, "2", "ground");
		screen.add(block4);
		Block block5 = new Block(692, 270, "2", "ground");
		screen.add(block5);
		Block block6 = new Block(660, 170, "2", "ground");
		screen.add(block6);
		Block block7 = new Block(500, 150, "2", "ground");
		screen.add(block7);
		
		Player player = new Player(50, 50, "Player", "idle", "img/player/idle.gif", 1);
		screen.add(player);

		while (true) {
			int moveSpeed = 5;
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

			player.setX(playerX);
			player.setY(playerY);
			screen.remove(screen.getIndex("Player"));
			screen.add(player);

			
			collisionCheck();
			
			player.setX(playerX);
			player.setY(playerY);
			screen.remove(screen.getIndex("Player"));
			screen.add(player);

			while (nextFrame > System.currentTimeMillis()) {

			}

			nextFrame = System.currentTimeMillis() + (1000 / fps);
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
		ArrayList<ScreenElement> blocks = screen.getAllOfType("block");

		boolean doGrav = true;
		for (int i = 0; i < blocks.size(); i++) {
			Block block = (Block) blocks.get(i);
			if (block.getY() - 32 < playerY && block.getY() + 32 > playerY && block.getX() - 32 < playerX
					&& block.getX() + 32 > playerX) {
				
				//Top
				if (block.getY() - 32 < playerY && block.getY() - 16 > playerY) {
					playerY = block.getY() - 31;
					doGrav = false;
					velocity = 0;
					inJump = false;
				}
				//bottom
				if (block.getY() + 32 > playerY && block.getY() + 16 < playerY) {
					playerY = block.getY() + 32;
				}
				//left
				if (block.getX() - 32 < playerX && block.getX() - 16 > playerX) {
					if(doGrav) {
						playerX = block.getX() - 32;
					}
				}
				//right
				if (block.getX() + 32 > playerX && block.getX() + 16 < playerX) {
					if (doGrav) {
						playerX = block.getX() + 32;
					}
				}
			}
		}
		
		if(doGrav) {
			gravity();
		}

	}

	// Medal System
}
