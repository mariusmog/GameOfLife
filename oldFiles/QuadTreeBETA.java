package model;

import controller.Controller;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.concurrent.CopyOnWriteArrayList;

public class QuadTreeBETA {
	private Rectangle boundary;
	private int capacity;
	private CopyOnWriteArrayList<Cellule> cellules;
	private QuadTreeBETA northEast;
	private QuadTreeBETA southEast;
	private QuadTreeBETA northWest;
	private QuadTreeBETA southWest;
	public boolean isSubdivided;
	private String name;

	public QuadTreeBETA(Rectangle boundary, int capacity, String name) {
		this.boundary = boundary;
		this.capacity = capacity;
		this.cellules = new CopyOnWriteArrayList<Cellule>();
		this.name = name;
	}

	public boolean insert(Cellule c) {
		if (this.boundary.contains(c.getX(), c.getY())) {
			if (this.cellules.size() < this.capacity) {
				return this.cellules.add(c);
			} else {
				this.subdivide();
				if (this.northEast.insert(c)) {
					return true;
				}
				if (this.southEast.insert(c)) {
					return true;
				}
				if (this.southWest.insert(c)) {
					return true;
				}
				if (this.northWest.insert(c)) {
					return true;
				}
			}
		}
		return false;
	}
	public void subdivide() {
		if (this.isSubdivided == false) {
			this.northEast = new QuadTreeBETA(new Rectangle((int)(this.boundary.width/2 + this.boundary.getX()), (int)this.boundary.getY(), (this.boundary.width/2), (this.boundary.height/2)), this.capacity, "northEast");
			this.southEast = new QuadTreeBETA(new Rectangle((int)(this.boundary.width/2 + this.boundary.getX()), (int)(this.boundary.height/2 + this.boundary.getY()), (this.boundary.width/2), (this.boundary.height/2)), this.capacity, "southEast");
			this.northWest = new QuadTreeBETA(new Rectangle((int)this.boundary.getX(), (int)this.boundary.getY(), (this.boundary.width /2), (this.boundary.height/2)), this.capacity, "northWest");
			this.southWest = new QuadTreeBETA(new Rectangle((int)this.boundary.getX(), (int)(this.boundary.height/2 + this.boundary.getY()), (this.boundary.width /2), (this.boundary.height/2)), this.capacity, "southWest");
			this.isSubdivided = true;
		}
	}
	public boolean contains(int x, int y) {
		if (!this.boundary.contains(x, y)) {
			return false;
		}
		for (Cellule c : this.cellules) {
			if (x == c.getX() && y == c.getY()) {
				return true;
			}
		}
		if (this.isSubdivided) {
			if (this.northEast.contains(x, y))	return true;
			if (this.southEast.contains(x, y))	return true;
			if (this.northWest.contains(x, y))	return true;
			if (this.southWest.contains(x, y))	return true;
		}
		return false;
	}
	public boolean contains(Cellule c) {
		return this.contains(c.getX(), c.getY());
	}
	public CopyOnWriteArrayList<Cellule> getCellIn(Rectangle field) {
		CopyOnWriteArrayList<Cellule> list = new CopyOnWriteArrayList<>();
		return this.getCellIn(field, list);
	}
	private CopyOnWriteArrayList<Cellule> getCellIn(Rectangle field, CopyOnWriteArrayList<Cellule> list) {
		if(this.boundary.intersects(field)) {
			for (Cellule c : this.cellules) {
				if (field.contains(c.getX(), c.getY())) {
					list.add(c);
				}
			}
			if (this.isSubdivided) {
				this.northEast.getCellIn(field, list);
				this.southEast.getCellIn(field, list);
				this.northWest.getCellIn(field, list);
				this.southWest.getCellIn(field, list);
			}
		}
		return list;
	}
	public QuadTreeBETA removeCell(int x, int y) {
		if (!this.contains(x, y)) {
			return this;
		}
		QuadTreeBETA newTree = new QuadTreeBETA(this.boundary, this.capacity, "newTree");
		for (Cellule c : this.getCellIn(this.boundary)) {
			if (c.getX() != x || c.getY() != y) {
				newTree.insert(c);
			}
		}
		return newTree;
	}
	// Peut être mieux ecris
	// Question => Comment convertir une liste de Cellules à supprimer en un Rectangle qui represente
	// uniquement les Cellules à supprimer
	public QuadTreeBETA removeCell(CopyOnWriteArrayList<Cellule> cellToRemove) {
		boolean contains = false;
		for (Cellule c : cellToRemove) {
			if (this.contains(c)) {
				contains = true;
			}
		}
		if(contains) {
			QuadTreeBETA newTree = new QuadTreeBETA(this.boundary, this.capacity, "newTree");
			for (Cellule c : this.getCellIn(this.boundary)) {
				if (!cellToRemove.contains(c)) {
					newTree.insert(c);
				}
			}
			return newTree;
		}
		return this;
	}

	public String toString() {
		String info = "";
		info += this.name + "\n";
		info += this.boundary.toString() + "\n";
		for (Cellule c : this.cellules) {
			info += c.toString() + "\n";
		}
		if (this.isSubdivided) {
			info += this.northEast.toString() + "\n";
			info += this.southEast.toString() + "\n";
			info += this.northWest.toString() + "\n";
			info += this.southWest.toString() + "\n";
		}
		return info;
	}

	public static void main(String[] args) {
		QuadTreeBETA tree = new QuadTreeBETA(new Rectangle(0, 0, 100, 100), 2, "root");
		Cellule a = new Cellule(1, 1);
		Cellule b = new Cellule(2, 2);
		Cellule c = new Cellule(6, 6);
		Cellule d = new Cellule(20, 20);
		Cellule e = new Cellule(40, 49);
		tree.insert(a);
		tree.insert(b);
		tree.insert(c);
		tree.insert(d);
		tree.insert(e);
		CopyOnWriteArrayList<Cellule> remove = new CopyOnWriteArrayList<>();
		remove.add(a);
		remove.add(d);
		remove.add(b);
		System.out.println(tree);
		System.out.println("============================");
		System.out.println(tree.removeCell(remove));
	}
}