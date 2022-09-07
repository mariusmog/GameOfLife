package view;

import controller.Controller;
import model.Cellule;
import model.ModelQuadTree;
import view.MenuPanel;

import java.awt.Point;
import java.util.LinkedList;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Color;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel implements KeyListener, MouseWheelListener, MouseMotionListener, MouseListener{

	private static final long serialVersionUID = 1L;

	private int width;
	private int height;
	private int midWidth;
	private int midHeight;
	//Pour le moment la taille des Cellules est fixee a 10
	protected int sizeCell = 30;

	//Ces deux entiers representent des coordonnees x et y, elles sont utilisees pour
	//decaler les cellules dans la vue, les cellules ne sont pas alterees
	private int decalageX = 0;
	private int decalageY = 0;

	private Point prevPoint = new Point(0, 0);
	private Point currentPoint = new Point(0, 0);

	private Point currentMousePos;
	private boolean leftClickMode = false;
	private boolean rightClickMode = false;

	private Controller controller;
	
	public GamePanel(Controller controller) {
		this.controller = controller;
		this.setBackground(new Color(115, 115, 115));

		this.setSize(775,700);
		// addKeyListener(this);
	    addMouseMotionListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);

		this.setFocusable(true);

		this.updateSize();
	}

	public void paintComponent(Graphics g) {
		//On update les valeurs de midWidth et midHeight
		//Ainsi que le nombre max de Cellules affichable
		this.updateSize();
		//On clear le panel
		super.paintComponent(g);
		this.paintGrid(g);
		//En gros on repaint avec la couleur du fond un rectangle de la taille du JPanel
		this.paintCell(g);
	}

	private void updateSize() {
		this.width = this.getSize().width;
		this.height = this.getSize().height;
		this.midWidth = this.width/2;
		this.midHeight = this.height/2;
	}
	public void resetDecalage() {
		this.decalageX = 0;
		this.decalageY = 0;
	}
	private void drawCross(Graphics g) {
		g.setColor(Color.RED);
		g.drawLine(this.midWidth, 0, this.midWidth, (int) (this.getSize().getHeight() * 2));
		g.drawLine(0, this.midHeight, (int) (this.getSize().getWidth() * 2), this.midHeight);
	}
	private void paintGrid(Graphics g) {
		this.setColorOpacity(g);
		if (this.sizeCell > 20) {
			for (int x = this.midWidth; x >= 0; x -= this.sizeCell) {
				for (int y = this.midHeight; y >= 0; y -= this.sizeCell) {
					g.drawLine(x, 0, x, (int) (this.getSize().getHeight() * 2));
					g.drawLine(0, y, (int) (this.getSize().getWidth() * 2), y);
				}
			}
			for (int x = this.midWidth; x <= this.getSize().width; x += this.sizeCell) {
				for (int y = this.midHeight; y <= this.getSize().height; y += this.sizeCell) {
					g.drawLine(x, 0, x, (int) (this.getSize().getHeight() * 2));
					g.drawLine(0, y, (int) (this.getSize().getWidth() * 2), y);
				}
			}
		}
	}
	private void setColorOpacity(Graphics g) {
		int rgb = 0;
		if (this.sizeCell >= 20) {
			switch(this.sizeCell) {
				case 20:
					rgb = 115;
					break;
				case 21:
					rgb = 105;
					break;
				case 22:
					rgb = 95;
					break;
				case 23:
					rgb = 85;
					break;
				case 24:
					rgb = 75;
					break;
				case 25:
					rgb = 65;
					break;
				case 26:
					rgb = 55;
					break;
				case 27:
					rgb = 45;
					break;
				case 28:
					rgb = 35;
					break;
				case 29:
					rgb = 25;
					break;
				case 30:
					rgb = 15;
					break;
				case 31:
					rgb = 5;
					break;
				default :
					rgb = 0;
					break;
			}
		}
		g.setColor(new Color(rgb, rgb ,rgb));
	}
	private void paintCell(Graphics g) {
		for (Cellule c : this.controller.model.getCellulesVivantes()) {
			Cellule cellToPaint = convertToPixel(c);
			if (isValid(cellToPaint)) {
				g.setColor(c.getColor());
				g.fillRect((int) cellToPaint.getX(), (int) cellToPaint.getY(), this.sizeCell, this.sizeCell);
			}
		}
	}

	private boolean isValid(Cellule c) {
		//La verif consiste a verifier si la cellule est comprise
		//entre (-maxX, -maxY) et (maxX, maxY)
		return (c.getX() < this.width && c.getY() < this.height) && (c.getX() > -this.width && c.getY() > -this.height);
	}

	private Cellule convertToPixel(Cellule c) {
		return new Cellule(this.midWidth + (c.getX()+this.decalageX)*sizeCell, this.midHeight + (-c.getY()+this.decalageY)*sizeCell);
	}
	private Point convertToActualCoordinate(Point p) {
		return new Point((int)(Math.floor((p.getX()-this.midWidth) / this.sizeCell) - this.decalageX), -(int)(Math.floor((p.getY()-this.midHeight) / this.sizeCell) - this.decalageY));
	}

	public void mouseClicked(MouseEvent e) {
		this.addCell(convertToActualCoordinate(e.getPoint()));
	}
	private void addCell(Point p) {
		Cellule c = new Cellule(p);
	    if(this.controller.model.estVivante(c)) {
	    	controller.model.retirerCellule(c);
	    }else {
	    	controller.model.ajouterCellule(c);
	    }
	}
	
	
	//Lorsque la souris est appuye qqPart sur le JPanel on garde en memoire un point
	public void mousePressed(MouseEvent e) {
		this.prevPoint = this.convertToActualCoordinate(e.getPoint());
		switch (e.getButton()) {
			case 1:
				this.leftClickMode = true;
				this.rightClickMode = false;
				break;
			case 3:
				this.rightClickMode = true;
				this.leftClickMode = false;
				break;
			default:
				this.leftClickMode = false;
				this.rightClickMode = false;
				break;
		}
	}
	
	//Lorsque le click de la souris est maintenu et la souris est deplace on ajoute aux valeurs
	//decalageX et decalageY (qui sont deja utilise pour deplacer les cellules avec les fleches du 
	//clavier) la distance entre le point de click et la position actuelle du curseur sur l'ecran
	public void mouseDragged(MouseEvent e) {
		this.currentPoint = this.convertToActualCoordinate(e.getPoint());
		if (!this.currentPoint.equals(this.prevPoint)) {
			if (this.leftClickMode) {
				this.decalageX += (int)(-this.prevPoint.getX() - -this.currentPoint.getX());
				this.decalageY += (int)(this.prevPoint.getY() - this.currentPoint.getY());
				this.prevPoint = this.convertToActualCoordinate(e.getPoint());
			} else if (this.rightClickMode) {
				Point pointToAdd = new Point((int)this.currentPoint.getX(), (int)this.currentPoint.getY());
				this.addCell(pointToAdd);
				this.prevPoint = this.convertToActualCoordinate(e.getPoint());
			}
		}
	}

	//getWheelRotation() retourne 1 si l'on zoom vers le haut et -1 vers le bas
	public void mouseWheelMoved(MouseWheelEvent e) {
		//Ici 1 represente la taille minimale en pixel d'une cellule
		if (e.getWheelRotation() > 0 && this.sizeCell > 1) {
			this.sizeCell--;
			
		//Ici 1000 represente la taille maximale en pixel d'une cellule
		} else if (e.getWheelRotation() < 0 && this.sizeCell < 500){
			this.sizeCell++;
		}
	}
	
	//Les valeurs 37, 38, 39, et 40 representent les fleches gauche, haut, droite et bas respectivement
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case 37 :
				this.decalageX--;
				break;
			case 38 :
				this.decalageY--;
				break;
			case 39 :
				this.decalageX++;
				break;
			case 40 :
				this.decalageY++;
				break; 
		}
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void mouseMoved(MouseEvent e) {
		this.currentMousePos = e.getPoint();
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
}