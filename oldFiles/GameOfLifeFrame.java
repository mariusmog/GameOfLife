package oldFiles;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

import controller.Controller;
import model.Cellule;

public class GameOfLifeFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public int initWindowWidth;
	public int initWindowHeight;
	public GameScreen gameScreen;

	public GameOfLifeFrame(Controller controller, int width, int height) {
		//Le rectangle recupere possede 4 champs, x, y, width et height
		//Il represente la taille maximale disponible pour afficher une fenetre en pixels
		Rectangle tmpBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.gameScreen = new GameScreen(controller);
		this.gameScreen.addKeyListener(this.gameScreen);
		this.gameScreen.addMouseWheelListener(this.gameScreen);
		this.gameScreen.addMouseMotionListener(this.gameScreen);
		this.gameScreen.addMouseListener(this.gameScreen);
		this.gameScreen.setFocusable(true);
		frameSetUp(width, height);
	}
	private void frameSetUp(int width, int height) {
		this.setTitle("Game of Life");
		this.setSize(width, height);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().add(this.gameScreen);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		Controller board = new Controller();
		board.model.ajouterCellule(new Cellule(10, 5));
		board.model.ajouterCellule(new Cellule(4, 9));
		board.model.ajouterCellule(new Cellule(7, 12));
		board.model.ajouterCellule(new Cellule(-50, -23));

		new GameOfLifeFrame(board, 1000, 500);
	}
}