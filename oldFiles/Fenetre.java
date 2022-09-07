package oldFiles;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;

public class Fenetre extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public Grille grille;
	public Menu menu;
	
	@SuppressWarnings("unused")
	private Controller controller;
	
	public Fenetre(Controller controller) {
	    this.setSize(1000,740);
	    setContentPane(new JPanel(null));
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    this.setBackground(Color.BLUE);
	    this.setLayout(null);
	    this.setVisible(true);
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    grille = new Grille(controller);
	    grille.setVisible(true);
	    menu = new Menu(controller);
	    menu.setVisible(true); 
	    this.getContentPane().add(grille);
	    this.getContentPane().add(menu);
	}

	public void menuRepaint(){
		menu.repaint();
	}

	
}