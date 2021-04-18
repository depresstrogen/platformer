package src;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class MouseHandler  extends JPanel implements MouseListener{
	
	private double mouseX = 0;
	private double mouseY = 0;
	private boolean isMousePressed = false;
	private Frame frame;
	public void start(Frame frame) {
		this.frame = frame;
		frame.addMouseListener(this);
		
	}
	
	public void mouseInputs(Screen screen) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Press " + e.getX() + " " + e.getY());
		mouseX  = e.getX();
		mouseY = e.getY();
		isMousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Release ");
		mouseX  = e.getX();
		mouseY = e.getY();
		isMousePressed = false;
	}
	
	public boolean isMousePressed() {
		return isMousePressed;
	}
	
	public void removePressedFlag() {
		isMousePressed = false;
	}
	
	public int getX() {
		Point frm = frame.getLocation();
		Point mse = MouseInfo.getPointerInfo().getLocation();
		mouseX = mse.getX() - frm.getX();
		return (int) mouseX;
	}
	
	public int getY() {
		Point frm = frame.getLocation();
		Point mse = MouseInfo.getPointerInfo().getLocation();
		mouseY = mse.getY() - frm.getY();
		return (int) mouseY;
	}
}
