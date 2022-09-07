# JEU DE LA VIE

Ce projet est une implémentation modifiée du jeu de la vie, qui permet à l'utilisateur de jouer avec ses propres règles.


## Principe du jeu :

Le jeu de la vie est un jeu inventé par John Horton Conway en 1970 qui met en jeu un automate cellulaire. Cet automate cellulaire prend
la forme d’une grille infinie à deux dimensions composée de cellules pouvant être dans deux états : mortes ou
vivantes. À chaque itération, l'état des cellules est déterminé en fonction de l'état de ses 8 cellules voisines.
Il en résulte une animation visuelle représentant l’évolution de ce système.


## Fonctionnalités :


*   Le jeu permet de suivre l'évolution des cellules au cours des générations, à l'aide d'un bouton Play et d'un bouton Pause.
    
*   Il permet également de modifier la vitesse de l'animation.
   
*   L'utilisateur peut entrer ses propres règles, à l'aide de cases à cocher qui correspondent au nombre de cellules voisines vivantes qu'il       faut à une cellule pour vivre ou mourir.
   
*  L'utilisateur peut charger des grilles déjà existantes, et sauvegarder ses grilles de jeu.
   
*   Il peut également placer ou retirer une cellule en cliquant sur le plateau avec le click gauche, ou maintenir le click droit pour en placer plusieurs à la suite.
   
*   Il peut aussi zoomer/dézoomer avec la molette de la souris et se déplacer sur le plateau en maintenant le click gauche enfoncé.
    
Le projet suit une architecture MVC (Modèle, Vue , Controlleur).


### Pré-requis :

L'utilisateur doit avoir une version récente de [Java](https://www.java.com/fr/download/) installée sur son ordinateur. 


### Installation :

    
1.   Il faut cloner ce dépot depuis ce [lien](https://gaufre.informatique.univ-paris-diderot.fr/pickern/jeu-de-la-vie.git)
2.   Soit importer le projet dans l'IDE Eclipse et lancer le jeu dans la classe Lanceur, soit se placer dans le répertoire racine et exécuter la commande suivante :

	*  Sous Linux :
		javac lanceur/Lanceur.java
		java lanceur.Lanceur
		
	*  Sous Windows(depuis une console BASH):
		javac lanceur/Lanceur.java
		java lanceur/Lanceur


## Auteurs :

    
*  Marius CAHAGNE
   
*  Yann PICKERN
   
*  Alexis N'GABALA
   
*  François NGY

*  Thibault ROLLAND


