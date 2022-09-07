package model;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import controller.Controller;

public class WorkingModel extends JPanel implements MouseListener, MouseMotionListener {

	public Controller controller;
	public QuadTree board;
	// public LinkedList<Cellule> board;
	public Rectangle boundary;
	public int capacity;
	public int count = 0;

	public WorkingModel(Controller controller, Rectangle boundary, int capacity) {
		this.controller = controller;
		this.board = new QuadTree(boundary, capacity);
		// this.board = new LinkedList<>();
		this.boundary = boundary;
		this.capacity = capacity;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public WorkingModel(Controller controller) {
		this(controller, new Rectangle(-5000, -5000, 10000, 10000), 20);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		this.board.paintRectangle(g);
		this.paintCell(g);
		this.paintAllInRect(g, new Rectangle (200, 300, 947, 619));
	}
	public void paintCell(Graphics g) {
		g.setColor(Color.RED);
		for (Cellule c : this.board.getList(this.boundary)) {
			g.drawOval(c.getX(), c.getY(), 2, 2);
		}
		// for (Cellule c : this.board) {
		// 	g.drawOval(c.getX(), c.getY(), 2, 2);
		// }
	}
	public void paintAllInRect(Graphics g, Rectangle field) {
		g.setColor(Color.GREEN);
		g.drawRect((int)field.getX(), (int)field.getY(), (int)field.getWidth(), (int)field.getHeight());
		for (Cellule c : this.board.getList(field)) {
			g.fillOval(c.getX(), c.getY(), 4, 4);
		}
	}
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {
		this.count++;
		this.board.add(new Cellule(e.getX(), e.getY()));
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
}