package model;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


public class QuadTree implements Iterable<Cellule> {

	private Rectangle boundary;
	private int capacity;
	private CopyOnWriteArrayList<Cellule> points;
	public int size = 0;

	private QuadTree northEast;
	private QuadTree southEast;
	private QuadTree northWest;
	private QuadTree southWest;
	private boolean isSubdivided = false;
	private String treeName;


	private QuadTree parent;

	public QuadTree(Rectangle boundary, int capacity, QuadTree parent, String treeName) {
		this.boundary = boundary;
		this.capacity = capacity;
		this.parent = parent;
		this.points = new CopyOnWriteArrayList<>();
		this.treeName = treeName;
	}

	public QuadTree(Rectangle boundary, int capacity) {
		this(boundary, capacity, null, "root");
	}

	private QuadTree getBiggerTree() {
		return new QuadTree(new Rectangle(boundary.x * 2, boundary.y * 2, boundary.width * 2, boundary.height * 2), capacity);
	}
	private void replaceCurrentTree(QuadTree tmp) {
		boundary = tmp.boundary;
		points = tmp.points;
		isSubdivided = tmp.isSubdivided;
		northEast = tmp.northEast;
		southEast = tmp.southEast;
		northWest = tmp.northWest;
		southWest = tmp.southWest;
	}
	public boolean add(Cellule c) {
		if (parent == null) {
			
			while (!boundary.contains(c.getX(), c.getY())) {
				QuadTree tmp = getBiggerTree();
				for (Cellule cell : this) {
					tmp.add(cell);
				}
				replaceCurrentTree(tmp);
			}
		}
		return add(c, true);
	}
	public boolean add(Cellule c, boolean newCell) {
		if (boundary.contains(c.getX(), c.getY())) {
			if (points.size() < capacity && isSubdivided == false) {
				if (points.add(c)) {
					if (newCell)	size++;
					return true;
				}
			} else {
				if (isSubdivided == false) {
					subdivide();
					distribute();
				}
				if (northEast.add(c)) {
					if (newCell)	size++;
					return true;
				}
				if (southEast.add(c)) {
					if (newCell)	size++;
					return true;
				}
				if (southWest.add(c)) {
					if (newCell)	size++;
					return true;
				}
				if (northWest.add(c)) {
					if (newCell)	size++;
					return true;
				}
			}
		}
		return false;
	}
	public void addAll(CopyOnWriteArrayList<Cellule> list) {
		for (Cellule c : list) {
			add(c);
		}
	}
	private void subdivide() {
		double x = boundary.getX();
		double y = boundary.getY();
		int w = boundary.width;
		int h = boundary.height;
		if (isSubdivided == false) {
			northEast = new QuadTree(new Rectangle((int)((w/2) + x), (int)y, (int)Math.ceil((double)w/2), (int)Math.ceil((double)h/2)), capacity, this, "NE");
			southEast = new QuadTree(new Rectangle((int)((w/2) + x), (int)((h/2) + y), (int)Math.ceil((double)w/2), (int)Math.ceil((double)h/2)), capacity, this, "SE");
			northWest = new QuadTree(new Rectangle((int)x, (int)y, (int)Math.ceil((double)w /2), (int)Math.ceil((double)h/2)), capacity, this, "NW");
			southWest = new QuadTree(new Rectangle((int)x, (int)((h/2) + y), (int)Math.ceil((double)w /2), (int)Math.ceil((double)h/2)), capacity, this, "SW");
			isSubdivided = true;
		}
	}
	private void distribute() {
		if (isSubdivided) {
			for (Cellule c : points) {
				add(c, false);
			}
			points.clear();
		}
	}
	public boolean contains(Cellule c) {
		if (boundary.contains(c.getX(), c.getY())) {
			if (isSubdivided == false) {
				return points.contains(c);
			} else {
				if (northEast.contains(c)) {
					return true;
				}
				if (southEast.contains(c)) {
					return true;
				}
				if (southWest.contains(c)) {
					return true;
				}
				if (northWest.contains(c)) {
					return true;
				}
			}
		}
		return false;
	}
	public CopyOnWriteArrayList<Cellule> getList() {
		return containsAll(boundary);
	}
	public CopyOnWriteArrayList<Cellule> containsAll(Rectangle field) {
		if (boundary.intersects(field)) {
			CopyOnWriteArrayList<Cellule> pointsFound = new CopyOnWriteArrayList<>();
			return containsAllAux(field, pointsFound);
		}
		return null;
	}
	private CopyOnWriteArrayList<Cellule> containsAllAux(Rectangle field, CopyOnWriteArrayList<Cellule> pointsFound) {
		if (isSubdivided == false) {
			for (Cellule c : points) {
				if (field.contains(c.getX(), c.getY())) {
					pointsFound.add(c);
				}
			}
		} else {
			northEast.containsAllAux(field, pointsFound);
			southEast.containsAllAux(field, pointsFound);
			northWest.containsAllAux(field, pointsFound);
			southWest.containsAllAux(field, pointsFound);
		}
		return pointsFound;
	}
	private void decrementSize() {
		size--;
		if (parent != null) {
			parent.decrementSize();
		}
	}
	public boolean remove(Cellule c) {
		if (boundary.contains(c.getX(), c.getY())) {
			if (isSubdivided == false) {
				if (points.remove(c)) {
					decrementSize();
					return true;
				}
				return false;
			} else {
				boolean removed = false;
				if (northEast.remove(c) && removed == false) {
					removed = true;
				}
				if (southEast.remove(c) && removed == false) {
					removed = true;
				}
				if (southWest.remove(c) && removed == false) {
					removed = true;
				}
				if (northWest.remove(c) && removed == false) {
					removed = true;
				}
				if (canMerge()) {
					this.isSubdivided = false;
					northEast.merge();
					southEast.merge();
					northWest.merge();
					southWest.merge();
					this.isSubdivided = true;
					unSubdivide();
				} else if (childEmpty()) {
					unSubdivide();
				}
			}
		}
		return false;
	}
	public void removeAll(CopyOnWriteArrayList<Cellule> list) {
		for (Cellule c : list) {
			remove(c);
		}
	}
	private boolean canMerge() {
		if (isSubdivided) {
			return northEast.size + southEast.size + northWest.size + southWest.size <= capacity;
		}
		return false;
	}
	private void merge() {
		for (Cellule c : getList()) {
			parent.add(c, false);
		}
	}
	private boolean childEmpty() {
		if (isSubdivided) {
			return northEast.size == 0 && southEast.size == 0 && northWest.size == 0 && southWest.size == 0;
		}
		return true;
	}
	private void unSubdivide() {
		if (isSubdivided) {
			isSubdivided = false;
			northEast = null;
			southEast = null;
			northWest = null;
			southWest = null;
		}
	}
	public void clear() {
		if (isSubdivided)	unSubdivide();
		points.clear();
		size = 0;
	}

	public Iterator<Cellule> iterator() {
		return new QuadTreeIterator(this);
	}
	private QuadTree nextTree() {
		switch (treeName) {
		case "root" :
			return northWest;
		case "NW" :
			return parent.northEast;
		case "NE" :
			return parent.southWest;
		case "SW" :
			return parent.southEast;
		case "SE" :
			if (parent.treeName.equals("root") == false) {
				return parent.nextTree();
			}
		}
		return null;
	}

	private class QuadTreeIterator implements Iterator<Cellule> {

		public QuadTree currentTree;
		public int index = 0;

		public QuadTree hasNextTree;
		public int hasNextIndex;

		public QuadTreeIterator(QuadTree
		 root) {
			currentTree = root;
		}
		public Cellule next() {
			if (currentTree == null)	return null;

			if (currentTree.isSubdivided == false && index < currentTree.points.size()) {
				index++;
				return currentTree.points.get(index-1);
			} else {
				index = 0;
				if (currentTree.isSubdivided) {
					currentTree = currentTree.northWest;
				} else {
					currentTree = currentTree.nextTree();
				}
				return next();
			}
		}
		
		public boolean hasNext() {
			hasNextTree = currentTree;
			hasNextIndex = index;
			return hasNextAux();
		}
		private boolean hasNextAux() {
			if (hasNextTree != null) {
				if (hasNextTree.isSubdivided == false && hasNextIndex < hasNextTree.points.size()) {
					return true;
				} else {
					hasNextIndex = 0;
					if (hasNextTree.isSubdivided) {
						hasNextTree = hasNextTree.northWest;
					} else {
						hasNextTree = hasNextTree.nextTree();
					}
					return hasNextAux();
				}
			}
			return false;
		}
	}
	@Override
	public String toString() {
		String res = "";

		res += "[\n";
		for (Cellule c : points) {
			res += "\t" + c + "\n";
		}
		res += "]\n";
		if (isSubdivided) {
			res += "Fils {\n";
			res += "northWest\n" + northWest.toString();
			res += "northEast\n" + northEast.toString();
			res += "southWest\n" + southWest.toString();
			res += "southEast\n" + southEast.toString(); 
			res += "}\n";
		}
		return res;
	}
}