package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import controller.Controller;

public class MenuReglesFrame extends JFrame implements WindowListener{
	Controller c;
	
	public MenuReglesFrame(Controller c) {
		this.c=c;
		this.setSize(600,220);
        this.setVisible(true);
		this.getContentPane().add(c.view.menuRegles);
		this.addWindowListener(this);
		this.setLocationRelativeTo(null);
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		c.view.menuPanel.rulesButton.setEnabled(true);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}
	


}
