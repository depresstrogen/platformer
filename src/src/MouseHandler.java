package src;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class MouseHandler  extends JPanel implements MouseListener{
	
	public void start(Frame frame) {
		frame.addMouseListener(this);
	}
	
	public void mouseInputs(Screen screen) {
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.print("Press ");
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		System.out.print("Release ");
		
	}
}
