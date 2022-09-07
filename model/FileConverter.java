package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

/**
 * Classe regroupant des methodes de conversion de fichiers en liste de cellules et inversement.
 */
public class FileConverter {
	
	/**
	 * Personne ne doit pouvoir creer une instance de cette classe.
	 */
	private FileConverter() {}
	
	/**
	 * Lit un fichier .txt et le lit ligne par ligne:
	 * par ligne, chaque caractere est lu, si celui-ci est different de ' ' (espace) alors il est considere comme un point
	 * et est donc ajoute a la liste.
	 * Le fichier passe en parametre est donc cense etre une representation visuelle d'un ensemble de cellules.
	 * 
	 * @param file un fichier txt
	 * @return la liste des cellules correspondante
	 */
	public static CopyOnWriteArrayList<Cellule> txtToCellList_a(File file) {
		var l = new CopyOnWriteArrayList<Cellule>();
		try {
			
			var br = new BufferedReader(new FileReader(file));
			int y = 0;
			String line;
			while ((line = br.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) != ' ') l.add(new Cellule(i, y));
				}
				y--;
			}
			br.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return l;
	}
	
	/**
	 * Convertit un fichier txt en sa liste de cellules correspondante.
	 * Le format attendu est:
	 * x1,y1
	 * x2,y2
	 * ...
	 * xn,yn
	 * pour n cellules.
	 * 
	 * @param f le fichier a lire contenant les cellules
	 * @return la liste des cellules du fichier
	 */
	public static CopyOnWriteArrayList<Cellule> txtToCellList_b(File file) {
		var l = new CopyOnWriteArrayList<Cellule>();
		try { 
			
			var br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String [] coordinates = line.split(",");
				try {
					l.add(new Cellule(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			br.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return l;
	}
	
	/**
	 * Lit a travers une image et convertit chaque pixel noir ((R, G, B) = (0, 0, 0)) en une cellule a sa coordonnee correspondante.
	 * 
	 * @param file un fichier image
	 * @return la liste des cellules correspondantes
	 */
	public static CopyOnWriteArrayList<Cellule> pngToCellLit(File file) {
		var l = new CopyOnWriteArrayList<Cellule>();
		try {
			
			var bi = ImageIO.read(file);
			var raster = bi.getData();
			
			for (int y = 0; y < raster.getHeight(); y++) {
				for (int x = 0; x < raster.getWidth(); x++) {
					if (!new Color(bi.getRGB(x, y)).equals(new Color(255, 255, 255))) l.add(new Cellule(x, -y));
				}
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return l;
	}
	
	/**
	 * Transforme une liste de cellules en sa representation visuelle sous forme de fichier png.
	 * 
	 * @param l la liste des cellules
	 * @param fileName le nom du fichier png voulu
	 * @return une fichier au format png representant la liste de cellules
	 * @throws IOException
	 */
	public static File cellListToPng(String fileName,CopyOnWriteArrayList<Cellule> l) {
		File f = null;
		try {
			int width;
			int height;
			if(l.size()>0) {
				Cellule left = l.get(0);
				Cellule right = l.get(0);
				Cellule bottom = l.get(0);
				Cellule top = l.get(0);
				
				/* On recherche ici le point le plus a gauche, a droite, en bas et en haut de notre liste
				 * pour en faire par la suite un rectangle avec ces donnees */
				for (Cellule c : l) {
					if (c.getX() < left.getX()) {
						left = c;
					}
					else if (c.getX() > right.getX()) {
						right = c;
					}
					else if (c.getY() < bottom.getY()) {
						bottom = c;
					} else if (c.getY() > top.getY()) {
						top = c;
					}
				}
				width = Math.abs(left.getX() - right.getX()) + 3;
				height = Math.abs(bottom.getY() - top.getY()) + 3;
				var buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				
				/* On met tous les pixels de notre image en blanc */
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						buffImg.setRGB(i, j, -1);
					}
				}
				
				/* On parcourt de nouveau notre liste pour les ecrire dans notre image
				 * Un point de la liste = un pixel noir
				 * Une transformation est necessaire pour chaque point de la liste pour qu'il soit dans les bornes de notre image */
				for (Cellule c : l) {
					buffImg.setRGB(c.getX() - left.getX() + 1, height - (c.getY() - bottom.getY()) - 2, 0);
				}
				f = new File(fileName+".png");
				ImageIO.write(buffImg, "png", f);
				f.createNewFile();
				
			}else {
				width=1;
				height=1;
				var buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				buffImg.setRGB(0, 0, -1);
				f = new File(fileName+".png");
				ImageIO.write(buffImg, "png", f);
				f.createNewFile();
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return f; 
	}
	
	/**
	 * Convertit une liste de cellules en un fichier txt.
	 * La liste [(12, -6), (13, 7), (-78126,-9182718)] donnera un fichier dont le contenu sera:
	 * 12,-6
	 * 13,7
	 * -78126,-9182718
	 * 
	 * @param l la liste de cellules
	 * @return son fichier associe
	 */
	public static File cellListToTxt(String fileName,CopyOnWriteArrayList<Cellule> l) {
		File f = null;
		try {
			
			f = new File(fileName+".txt");
			var writer = new FileWriter(fileName+".txt");
			for (Cellule c : l) {
				writer.write(c.getX() + "," + c.getY() + "\n");
			}
			writer.close();
			f.createNewFile();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return f;
	}

}
