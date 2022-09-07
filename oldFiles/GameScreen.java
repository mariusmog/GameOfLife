package oldFiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import controller.Controller;
import model.Cellule;

public class GameScreen extends JPanel implements KeyListener, MouseWheelListener, MouseMotionListener, MouseListener{

	private static final long serialVersionUID = 1L;
	protected int midWidth;
	protected int midHeight;

	//Pour le moment la taille des Cellules est fixee a 10
	protected int sizeCell = 10;
	protected int maxX;
	protected int maxY;

	//Ces deux entiers representent des coordonnees x et y, elles sont utilisees pour
	//decaler les cellules dans la vue, les cellules ne sont pas alterees
	private int decalageX = 0;
	private int decalageY = 0;

	private Point prevPoint = new Point(0, 0);
	private Point currentPoint = new Point(0, 0);
	
	protected Controller controller;

	public GameScreen(Controller controller) {
		this.midWidth = this.getSize().width/2;
		this.midHeight = this.getSize().height/2;
		this.controller = controller;
		//La taille de maxX et maxY depend de sizeCell et la taille du JPanel
		//IL faudras que ca evolue avec la taille du JPanel
		maxX = this.getSize().width / this.sizeCell;
		maxY = this.getSize().height / this.sizeCell;
	}
	public void paintComponent(Graphics g) {
		//On actualize les valeurs de midWidth et midHeight
		this.actualizeSize();
		//On clear le panel
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		//En gros on repaint en blanc un rectangle de la taille du JPanel
		g.setColor(Color.BLACK);
		this.paintCell(g);
	}

	private void actualizeSize() {
		this.midWidth = this.getSize().width/2;
		this.midHeight = this.getSize().height/2;

		maxX = this.getSize().width / this.sizeCell;
		maxY = this.getSize().height / this.sizeCell;
	}
	private void paintCell(Graphics g) {
		for (model.Cellule c : this.controller.model.getCellulesVivantes()) {
			if (isValid(c)) {
				g.fillRect(this.midWidth + (c.getX()+this.decalageX)*sizeCell, this.midHeight + (c.getY()+this.decalageY)*sizeCell, this.sizeCell, this.sizeCell);
			}
		}
	}
	private boolean isValid(model.Cellule c) {
		//La verif consiste a verifier si la cellule est comprise
		//entre (-maxX, -maxY) et (maxX, maxY)
		return (c.getX()+this.decalageX < this.maxX && c.getY()+this.decalageY < this.maxY) && (c.getX()+this.decalageX > -this.maxX && c.getY()+this.decalageY > -this.maxY);
	}


	//On est oblige de redefinir cette methode mais elle ne nous est pas utile
	public void keyReleased(KeyEvent e) {}
	//De meme pour celle ci
	public void keyTyped(KeyEvent e) {}


	//Les valeurs 37, 38, 39, et 40 representent les fleches gauche, haut, droite et bas respectivement
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case 37 :
				this.decalageX++;
				break;
			case 38 :
				this.decalageY++;
				break;
			case 39 :
				this.decalageX--;
				break;
			case 40 :
				this.decalageY--;
				break; 
		}
		this.repaint();
	}
	//getWheelRotation() retourne 1 si l'on zoom vers le haut et -1 vers le bas
	public void mouseWheelMoved(MouseWheelEvent e) {
		//Ici 1000 represente la taille maximale en pixel d'une cellule
		if (e.getWheelRotation() > 0 && this.sizeCell < 1000) {
			this.sizeCell++;
		//Ici 1 represente la taille minimale en pixel d'une cellule
		} else if (e.getWheelRotation() < 0 && this.sizeCell > 1){
			this.sizeCell--;
		}
		this.repaint();
	}
	//Lorsque la souris est appuye qqPart sur le JPanel on garde en memoire un point
	public void mousePressed(MouseEvent e) {
		this.prevPoint = e.getPoint();
	}
	//Lorsque le click de la souris est maintenu et la souris est deplace on ajoute aux valeurs
	//decalageX et decalageY (qui sont deja utilise pour deplacer les cellules avec les fleches du 
	//clavier) la distance entre le point de click et la position actuelle du curseur sur l'ecran
	public void mouseDragged(MouseEvent e) {
		this.currentPoint = e.getPoint();
		this.decalageX += (int)(this.prevPoint.getX() - this.currentPoint.getX());
		this.decalageY += (int)(this.prevPoint.getY() - this.currentPoint.getY());
		this.prevPoint = this.currentPoint;
		this.repaint();
	}


	public void mouseMoved(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
}