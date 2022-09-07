package model;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.Controller;

/**
 * La classe modele contenant la logique du jeu de la vie.
 */
public class Model {
	
	/**
	 * Le controleur associe au modele afin de lui passer les donnees.
	 */
	public Controller controller;
	
	/**
	 * Liste contenant tous les points des cellules vivantes.
	 */
	public CopyOnWriteArrayList<Cellule> cellulesVivantes = new CopyOnWriteArrayList<>();

	/**
	 * Represente la generation courante, c'est-a-dire a quel tour on en est.
	 */
	private int generation;
	
	/**
	 * Les regles du jeu.
	 */
	private Regles regles;
	
	/**
	 * Le constructeur du Model.
	 * 
	 * @param controller le controleur associe au Model
	 * @param regles les regles du jeu
	 * @throws IOException 
	 */
	public Model(Controller controller, Regles regles) {
		this.controller = controller;
		if (regles == null) this.regles = Regles.getReglesDeBase();
		else this.regles = regles;
	}
	
	/**
	 * Constructeur ne prenant en parametre que le controleur.
	 * 
	 * @param controller le controleur associe au modele
	 * @throws IOException 
	 */
	public Model(Controller controller) {
		this(controller, null);
	}
	
	/**
	 * Getter de liste des cellules vivantes.
	 * 
	 * @return la liste des cellules vivantes.
	 */
	public CopyOnWriteArrayList<Cellule> getCellulesVivantes(){
		return cellulesVivantes;
	}
	
	/**
	 * Getter sur le nombre de generations.
	 * 
	 * @return le nombre de generations passees
	 */
	public int getGeneration() {
		return generation;
	}
	
	/**
	 * Setter sur le nombre de generations.
	 * 
	 * @param le nombre de generations que l'on souhaite
	 */
	public void setGeneration(int gen) {
		this.generation = gen;
	}
	
	/**
	 * Change les regles du jeu.
	 * 
	 * @param regles les nouvelles regles
	 */
	public void changerRegles(Regles regles) {
		this.regles = regles;
	}
	
	/**
	 * Met a jour la liste de cellules.
	 */
	public void update() {
		CopyOnWriteArrayList<Cellule> cellulesASupprimer = cellulesASupprimer();
		CopyOnWriteArrayList<Cellule> cellulesAFaireNaitre = cellulesAFaireNaitre();
		supprimerCellulesMortes(cellulesASupprimer);
		ajouterCellulesAFaireNaitre(cellulesAFaireNaitre);
		updateColor();
		generation++;
	}
	public void updateColor() {
		for (Cellule c :this.cellulesVivantes) {
			c.update();
		}
	}
	
	/**
	 * Regarde si une cellule possede au moins une cellule voisine vivante.
	 * 
	 * @param une cellule c
	 * @return true si la cellule c possede une cellule voisine vivante, false sinon
	 */
	public boolean possedeVoisine(Cellule c) {
		return nbDecellulesVoisinesVivantes(c) > 0;
	}
	
	/**
	 * Verifie a partir d'une instance de Cellule si une cellule est vivante, c'est-a-dire si elle est presente dans la liste de cellules.
	 * 
	 * @param une cellule c
	 * @return true si c est contenue dans la liste de cellules, false sinon
	 */
	public boolean estVivante(Cellule c) {
		return cellulesVivantes.contains(c);
	}
	
	/**
	 * Verifie a partir de coordonnees si une cellule est vivante, c'est-a-dire si elle est presente dans la liste de cellules.
	 * 
	 * @param x l'entier sur l'axe des abscisses
	 * @param y l'entier sur l'axe des ordonnees
	 * @return true si c est contenue dans la liste de cellules, false sinon
	 */
	public boolean estVivante(int x, int y) {
		return estVivante(new Cellule(x, y));
	}
	
	/**
	 * Prend une Cellule c en argument et retourne la liste de ses voisines vivantes.
	 * 
	 * @param c la cellule dont on verifie les voisines
	 * @return la liste des cellules voisines vivantes de la Cellule c
	 */
	public CopyOnWriteArrayList<Cellule> getCellulesVoisinesVivantes(Cellule c){
		CopyOnWriteArrayList<Cellule> res = new CopyOnWriteArrayList<>();
		int r = regles.getRayon();
		for (int i = c.getX() - r; i <= c.getX() + r; i++)
			for (int j = c.getY() - r; j <= c.getY() + r; j++)
				if ((c.getX() != i || c.getY() != j))
					res.add(new Cellule(i, j));
		return res;
	}
	
	/**
	 * Prend une Cellule c en argument et retourne la liste de ses voisines mortes.
	 * 
	 * @param c la cellule dont on verifie les voisines
	 * @return la liste des cellules voisines mortes de la Cellule c
	 */
	public CopyOnWriteArrayList<Cellule> getCellulesVoisinesMortes(Cellule c) {
		CopyOnWriteArrayList<Cellule> res = new CopyOnWriteArrayList<>();
		int r = regles.getRayon();
		for (int i = c.getX() - r; i <= c.getX() + r; i++)
			for (int j = c.getY() - r; j <= c.getY() + r; j++)
				if ((c.getX() != i || c.getY() != j) && !estVivante(i, j) && !res.contains(new Cellule(i, j)))
					res.add(new Cellule(i, j));
		return res;
	}

	/**
	 * Ajoute une cellule a la liste si et seulement si celle-ci n'est pas d�j� pr�sente
	 * (c'est-a-dire si le couple d'entiers (c.x, c.y) n'existe pas deja dans la liste)
	 * 
	 * @param la cellule c a ajouter
	 */
	public boolean ajouterCellule(Cellule c){
	 	if (!cellulesVivantes.contains(c))
	 			return cellulesVivantes.add(c);
	 	return false;
	}
	
	/**
	 * Ajoute une liste de cellules aux cellules vivantes et retourne l'instance.
	 * 
	 * @param cellules les cellules a ajouter
	 * @return l'instance du Model sur laquelle est appelee cette methode
	 */
	public Model avecCellules(Cellule... cellules) {
		for (Cellule c : cellules)
			if (!cellulesVivantes.contains(c))
	 			cellulesVivantes.add(c);
	 	return this;
	}
	public static CopyOnWriteArrayList<Cellule> avecListe(CopyOnWriteArrayList<Cellule> liste, CopyOnWriteArrayList<Cellule> cellules) {
		for (Cellule c : cellules)
			if (!liste.contains(c))
	 			liste.add(c);
	 	return liste;
	}
	
	/**
	 * Version 2 de la methode removeCelluleV(int x, int y) :
	 * une methode existe deja : remove(Object o)
	 * 
	 * @param la Cellule c a retirer de la liste
	 * @return true si la cellule a bien ete retiree de la liste, false sinon
	 */
	public boolean retirerCellule(Cellule c) {
		return cellulesVivantes.remove(c);
	}
	/**
	 * Cette methode supprime toutes les cellules vivantes de la liste.
	 */
	public void clear() {
		this.cellulesVivantes.clear();
	}

	/**
	 * Calcule le nombre de cellules voisines vivantes d'une cellule.
	 * 
	 * @param la cellule c
	 * @return le nombre de cellules voisines vivantes de la cellule c
	 */
	public int nbDecellulesVoisinesVivantes(Cellule c) {
		int n = 0;
		int r = regles.getRayon();
		for (int i = c.getX() - r; i <= c.getX() + r; i++)
			for (int j = c.getY() - r; j <= c.getY() + r; j++)
				if ((c.getX() != i || c.getY() != j) && estVivante(new Cellule(i, j))) // 2 conditions : (i, j) =/= (c.x, c.y) && la liste contient bien le couple (i, j)
					n++;
		return n;
	}
	
	/**
	 * Calcule le nombre de cellules voisines mortes d'une cellule.
	 * Il suffit de retourner 8 - n.
	 * Ou 8 represente le nombre total de cellules voisines et n le nombre de cellules voisines vivantes.
	 * 
	 * @param la cellule c
	 * @return le nombre de cellules voisines mortes de la cellule c
	 */
	public int nbDecellulesVoisinesMortes(Cellule c) {
		return 8 - nbDecellulesVoisinesVivantes(c); 
	}
	
	/**
	 * Supprime de la liste des cellules vivantes celles mortes apres un tour.
	 * 
	 * @param l la liste des cellules a faire mourir
	 */
	private void supprimerCellulesMortes(CopyOnWriteArrayList<Cellule> cellulesMortes) {
		cellulesVivantes.removeAll(cellulesMortes);
	}
	
	/**
	 * Ajoute a la liste des cellules vivantes les nouvelles cellules qui doivent naitre.
	 * 
	 * @param l la liste des cellules a faire naitre
	 */
	private void ajouterCellulesAFaireNaitre(CopyOnWriteArrayList<Cellule> cellulesAFaireNaitre) {
		cellulesVivantes.addAll(cellulesAFaireNaitre);
	}
	
	/**
	 * Retourne la liste des cellules a supprimer.
	 * 
	 * @return
	 */
	private CopyOnWriteArrayList<Cellule> cellulesASupprimer() {
		CopyOnWriteArrayList<Cellule> cellulesASupprimer = new CopyOnWriteArrayList<>();
		for (Cellule c : cellulesVivantes) {
			if (!regles.getVivanteResteEnVie().contains(nbDecellulesVoisinesVivantes(c))) {
				cellulesASupprimer.add(c);
			}
		}
		return cellulesASupprimer;
	}
	
	/**
	 * Liste toutes les cellules a faire naitre.
	 * 
	 * @return la liste des cellules a ajouter a la liste des cellules vivantes
	 */
	public CopyOnWriteArrayList<Cellule> cellulesAFaireNaitre() {
		CopyOnWriteArrayList<Cellule> cellulesAFaireNaitre = new CopyOnWriteArrayList<>();
		CopyOnWriteArrayList<Cellule> cellulesMortesDejaVisitees = new CopyOnWriteArrayList<>(); // Liste des cellules mortes deja visitees pour inviter de traiter plusieurs fois un meme point
		for (Cellule c : cellulesVivantes) { // Pour toutes les cellules vivantes,
			for (Cellule d : getCellulesVoisinesMortes(c)) { // telles que pour toutes leurs cellules voisines mortes,
				if (!cellulesMortesDejaVisitees.contains(d)) { // si celle-ci n'a pas deja visitee
					cellulesMortesDejaVisitees.add(d);
					if (regles.getMortePrendVie().contains(nbDecellulesVoisinesVivantes(d)) && !cellulesAFaireNaitre.contains(d)) {
						cellulesAFaireNaitre.add(d);
					}
				}
			}
		}
		return cellulesAFaireNaitre;
	}
	
	@Override
	public String toString() {
		return cellulesVivantes.toString();
	}
	
	/**
	 * Place des cellules aleatoirement (par rapport au centre de coordonnees (0, 0)) dans un certain rectangle.
	 * 
	 * @param nbCellules
	 * @param largeur
	 * @param hauteur
	 */
	public void placerCellulesAleatoirement(int nbCellulesAPlacer, int largeur, int hauteur) {
		Random r = new Random();
		int cellulesPlacees = 0;
		while (cellulesPlacees != nbCellulesAPlacer) {
			int x = -(largeur / 2) + r.nextInt(largeur + 1);
			int y = -(hauteur / 2) + r.nextInt(hauteur + 1);
			if (!estVivante(x, y)) {
				ajouterCellule(new Cellule(x, y));
				cellulesPlacees++;
			}
		}
	}
	
	public Regles getRegles(){
		return this.regles;
	}

	public void setRegles(Regles nouvelleR){
		regles = nouvelleR;
	}
	
}
