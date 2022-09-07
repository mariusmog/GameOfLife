package oldFiles;
import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import controller.Controller;
import model.Cellule;
import view.MenuPanel;

public class Grille extends JPanel implements MouseInputListener{

	private static final long serialVersionUID = 1L;
	private Controller controller;
	
	public Grille(Controller controller){
		this.controller = controller;
	    this.setSize(700,700);
	    //this.setResizable(true);
	    this.setBackground(Color.gray);
	    this.setLayout(null);
	    this.setVisible(true);
	    addMouseListener(this);
	    addMouseMotionListener(this);
	}
	
	public void refresh() {
		removeAll();
	    for(Cellule c : controller.model.getCellulesVivantes()){
	        JPanel celluleV = new JPanel();
			c.update(); //pour rajouter +1 au temps de vie de la cellule
	        celluleV.setBackground(c.getColor());
	        celluleV.setBounds(c.getX()*20, c.getY()*20, 20, 20);
	        celluleV.setVisible(true);
	        this/*.getContentPane()*/.add(celluleV);
	    }
		Menu.updateRound();
		// controller.view.menuRepaint();
	    repaint();
	}

	public void PlacerCellule(){        //C'est la meme fonction que refresh sauf que ca incremente pas le tour 
		for(Cellule c : controller.model.getCellulesVivantes()){
	        JPanel celluleV = new JPanel();
	        celluleV.setBackground(c.getColor());
	        celluleV.setBounds(c.getX()*20, c.getY()*20, 20, 20);
	        celluleV.setVisible(true);
	        this/*.getContentPane()*/.add(celluleV);
	    }
		repaint();
	}
	public void RetirerCellule(){       
		removeAll();
		for(Cellule c : controller.model.getCellulesVivantes()){
	        JPanel celluleV = new JPanel();
	        celluleV.setBackground(c.getColor());
	        celluleV.setBounds(c.getX()*20, c.getY()*20, 20, 20);
	        celluleV.setVisible(true);
	        this/*.getContentPane()*/.add(celluleV);
	    }
		repaint();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	    int x = e.getX()-(e.getX()%20); // moins lui meme %30 pour obtenir un jolie chiffre pour travailler plus facilement apres ( ex : x=97 -> x=80) et aussi pour aligner les cellulles
	    int y = e.getY()-(e.getY()%20)-this.getInsets().top;
	    Cellule c= new Cellule(x/20, y/20);
	    if(this.controller.model.estVivante(c)) {
	    	controller.model.retirerCellule(c); 
	    	RetirerCellule();
	    }else {
	    	controller.model.ajouterCellule(c);
	    	PlacerCellule();
	    }
		System.out.println(Menu.nbr);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	    // TODO Auto-generated method stub
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	    // TODO Auto-generated method stub
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
	public void mouseDragged(MouseEvent e) {
	    // TODO Auto-generated method stub
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	    // TODO Auto-generated method stub
	}
}