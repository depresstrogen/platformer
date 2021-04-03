package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;


public class KeebHandler {

	
	private boolean keyboard[] = new boolean[255];

	
	public void startKeyListener(JFrame frame) {
		frame.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_A) {
					keyboard['A'] = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_D) {
					keyboard['D'] = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_W) {
					keyboard['W'] = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					keyboard['S'] = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					keyboard[0] = true;
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_A) {
					keyboard['A'] = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_D) {
					keyboard['D'] = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_W) {
					keyboard['W'] = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					keyboard['S'] = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					keyboard[0] = false;
				}
			}
		});
	}

	public boolean[] getKeys() {
		return keyboard;
	}
	
	public boolean getKey(int key) {
		return keyboard[key];
	}
	
	public void inputPrint() {
		for(int i = 0; i < keyboard.length; i++) {
			if (keyboard[i]) {
				System.out.print((char) i + " ");
			}
		}
	}
}
