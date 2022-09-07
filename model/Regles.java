package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represente les regles du jeu.
 * 
 * @author 
 *
 */
public class Regles {
	
	/**
	 * Le rayon dans lequel les voisines d'une Cellule seront comptees.
	 * Vaut 1 dans les regles de base.
	 */
	private final int rayon;
	
	/**
	 * La liste des entiers positifs correspondant au nombre de cellules voisines vivantes qu'il faut pour qu'une cellule vivante reste en vie.
	 * Vaut {2, 3} dans les regles de base.
	 */
	private final LinkedList<Integer> vivanteResteEnVie; // Cette liste contient les nombres de cellules voisines en vie qu'il faut X une cellule vivante pour rester en vie.
	
	/**
	 * La liste des entiers positifs correspondant au nombre de cellules voisines vivantes qu'il faut pour qu'une cellule morte naisse.
	 * Vaut {3} dans les regles de base.
	 */
	private final LinkedList<Integer> mortePrendVie; // Cette liste contient les nombres de cellules voisines en vie qu'il faut X une cellule morte pour prendre vie.

	public Regles(int rayon, LinkedList<Integer> vivanteResteEnVie, LinkedList<Integer> mortePrendVie) {
		this.rayon = rayon;
		this.mortePrendVie = mortePrendVie;
		this.vivanteResteEnVie = vivanteResteEnVie;
	}
	
	public int getRayon() {
		return rayon;
	}
	
	public LinkedList<Integer> getMortePrendVie(){
		return this.mortePrendVie;
	}
	
	public LinkedList<Integer> getVivanteResteEnVie(){
		return this.vivanteResteEnVie;
	}

	/**
	 * Methode statique retournant les regles du jeu de base.
	 * 
	 * @return les regles du jeu de base
	 */
	public static Regles getReglesDeBase() {
		return new Regles(1, new LinkedList<Integer>(List.of(2, 3)), new LinkedList<Integer>(List.of(3)));
	}
}
