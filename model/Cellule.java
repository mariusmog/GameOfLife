package model;

import java.awt.Color;
import java.awt.Point;

public class Cellule {
	
	private int x;
	private int y;
	private int tempsDeVie = 1;
	private Color couleur = Color.WHITE;
	
	public Cellule(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Cellule(Point p) {
		this.x = (int) p.getX();
		this.y = (int) p.getY();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor(){
        return couleur;
    }
	
	public int getTempsDeVie() {
		return this.tempsDeVie;
	}
	
    public void update(){
        tempsDeVie++;
        int up = 255-(tempsDeVie*30);
        if(up<0){
            up=0;
        }
        couleur= new Color(up,up,up);
    }
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Cellule) {
			Cellule c = (Cellule) obj;
			return c.x == x && c.y == y;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}
	
}
