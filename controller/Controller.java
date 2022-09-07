package controller;

import java.awt.GraphicsDevice;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import model.Cellule;
import model.FileConverter;
import model.ModelQuadTree;
import view.GameFrame;
import view.HelpFrame;
import view.MenuReglesFrame;

import javax.swing.ImageIcon;

/**
 * 
 * @author 
 *
 */
public class Controller implements Runnable {
	
	public GameFrame view;
	private ImageIcon icon;
	public ModelQuadTree model;

	private Thread gameLoopThread;
	
	private int ups = 1;
	private double timePerUpdate = 1000000000.0 / ups;
	private final int FPS_SET = 144;
	private double timePerFrame = 1000000000.0 / FPS_SET;
	private static int nbSaves=0;
	// Le jeu est en pause par default
	private boolean suspended = true;
	

	private GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();    
	private GraphicsDevice device = graphics.getDefaultScreenDevice();


	public Controller() {
		this.model = new ModelQuadTree(this);
		loadIcon();
		this.view = new GameFrame(this, icon);
		gameLoopThread = new Thread(this);
		addPlayButtonListener();	
		addPauseButtonListener();
		addFullscreenButtonListener();
		addEscapeFullscreenButtonListener();
		addRulesButtonListener();
		addClearButtonListener();
		addtemplatesButtonListener();
		addValiderTemplatesButtonListener();
		addValiderReglesButtonListener();
		addSaveButtonListener();
		String SE = System.getProperty("os.name").toLowerCase();
		String chemin ="";
		if(SE.indexOf("win") >= 0) {
			chemin ="ressources\\structures";
		}else {
			chemin="ressources/structures";
		}
		File dossier=new File(chemin);
        for (File file : dossier.listFiles()) {
        	if(file.getName().contains("save")) {
        		nbSaves++;
        	}
        }
		startGameLoop();
	}
	private void loadIcon() {
		this.icon = new ImageIcon("ressources"+File.separator+"gameTextures"+File.separator+"gol.png");
	}
	/**
	 * Pour l'instant: fait jouer 1 seul tour par click.
	 */
	private void addPlayButtonListener() {
		view.menuPanel.addPlayButtonListener(e -> {
			resumeUpdateLoop();

		});
	} 
	
	private void addPauseButtonListener() {
		view.menuPanel.addPauseButtonListener(e -> {
			stopUpdateLoop();
		});
	}
	
	private void addClearButtonListener() {
		view.menuPanel.addClearButtonListener(e -> {
			stopUpdateLoop();
			view.resetDecalage();
			model.setGeneration(0);
			model.clear();
		});
	}
	
	private void addFullscreenButtonListener() {
		view.menuPanel.addFullscreenButtonListener(e -> {
			this.device.setFullScreenWindow(this.view);
			this.view.menuPanel.fullscreenButton.setEnabled(false);
			this.view.menuPanel.escapeFullscreenButton.setEnabled(true);
		});
	}
	private void addEscapeFullscreenButtonListener() {
		view.menuPanel.addEscapeFullscreenButtonListener(e -> {
			this.device.setFullScreenWindow(null);
			this.view.menuPanel.fullscreenButton.setEnabled(true);
			this.view.menuPanel.escapeFullscreenButton.setEnabled(false);
		});
	}
	private void addRulesButtonListener() {
		view.menuPanel.addRulesButtonListener(e-> {
			this.view.menuPanel.rulesButton.setEnabled(false);
			view.menuPanel.reglesFrame=new MenuReglesFrame(this);
		});
	}
	
	private void addtemplatesButtonListener() {
		view.menuPanel.addtemplatesButtonListener(e-> {
			this.view.menuPanel.templatesButton.setEnabled(false);
			view.menuPanel.helpFrame=new HelpFrame(this);
		});
	}
	
	private void addSaveButtonListener() {
		view.menuPanel.addSaveButtonListener(e-> {
			nbSaves++;
			String SE = System.getProperty("os.name").toLowerCase();
			String chemin ="";
			if(SE.indexOf("win") >= 0) {
				chemin ="ressources\\structures\\save";
			}else {
				chemin="ressources/structures/save";
			}
			FileConverter.cellListToTxt(chemin+nbSaves+"", model.cellulesVivantes.getList());
			view.menuHelp.menuDeroulant.addItem("save"+nbSaves+"");
			
		});
	}
	
	private void addValiderTemplatesButtonListener() {
		view.menuHelp.addValiderTemplatesButtonListener(e-> {
			 String selectedTemplate =view.menuHelp.menuDeroulant.getItemAt(view.menuHelp.menuDeroulant.getSelectedIndex());
			 String SE = System.getProperty("os.name").toLowerCase();
			 String chemin ="";
			 if(SE.indexOf("win") >= 0) {
				 chemin ="ressources\\structures\\"+selectedTemplate + ".txt";
			 }else {
				 chemin="ressources/structures/"+selectedTemplate + ".txt";
			 }
			 File fichier = new File(chemin);
			 CopyOnWriteArrayList<Cellule> template= FileConverter.txtToCellList_b(fichier);
			 model.clear();
			 model.cellulesVivantes.addAll(template);
			 view.menuPanel.helpFrame.dispose();
			 this.view.menuPanel.templatesButton.setEnabled(true);
		});
	}
	private void addValiderReglesButtonListener() {
		view.menuRegles.addValiderReglesButtonListener(e-> {
			for(int i=1;i<9;i++) {
		  	   	boolean selected1=view.menuRegles.cocheVivantes[i-1].isSelected();
		  	   	boolean contains1=model.getRegles().getVivanteResteEnVie().contains(i);
		  	   	boolean selected2=view.menuRegles.cocheMortes[i-1].isSelected();
		  		boolean contains2=model.getRegles().getMortePrendVie().contains(i);
		  	   	if(selected1) {
		  	   		if(!contains1)
		  	   			model.getRegles().getVivanteResteEnVie().add((Integer)i);
		  	   	}else {
		  	   		if(contains1) {
		  	   			model.getRegles().getVivanteResteEnVie().remove((Integer)i);
		  	   		}
		  	   	}
		  	  if(selected2) {
		  	   		if(!contains2)
		  	   			model.getRegles().getMortePrendVie().add((Integer)i);
		  	   	}else {
		  	   		if(contains2) {
		  	   			model.getRegles().getMortePrendVie().remove((Integer)i);
		  	   		}
		  	   	}
		    }
			view.menuPanel.reglesFrame.dispose();
			this.view.menuPanel.rulesButton.setEnabled(true);
			
		});
	}
	
	public void run() {

		// On a 2 champs pour traquer les updates
		int updates = 0;
		double updateTracker = 0;
		// Ainsi que 2 champs pour traquer les frames
		int frames = 0;
		double frameTracker = 0;


		// Ces 2 champs sont pour compter le temps en nanoSeconde entre deux passage dans 
		// la boucle while
		long lastCheck = System.currentTimeMillis();
		long previousTime = System.nanoTime();
		

		while(true) {
			long currentTime = System.nanoTime();

			frameTracker += (currentTime - previousTime) / timePerFrame;
			// On ne mets jamais en pause l'affichage
			if (frameTracker >= 1) {
				this.view.repaint();
				frames++;
				frameTracker--;
			}



			updateTracker += (currentTime - previousTime) / timePerUpdate;
			// Si le jeu est en pause alors suspended = true
			if (updateTracker >= 1) {
				if (!suspended) {
					update();
					this.view.menuPanel.updateRound();
					updates++;
				}
				updateTracker--;
			}
			// On ne rentre dans ce if que si au moins 1 seconde viens de passer
			// Puis on affiche les FPS ainsi que les UPS
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				//System.out.println("FPS :"+frames+"\nUPS :"+updates);	
				updates = 0;
				frames = 0;
			}
			previousTime = currentTime;

		}
	}
	private void startGameLoop() {
		gameLoopThread.start();
	}

	private void resumeUpdateLoop() {
		if (suspended) {
			this.suspended = false;
			this.view.menuPanel.playButton.setEnabled(false);
			this.view.menuPanel.pauseButton.setEnabled(true);
		}
		
	}
	private void stopUpdateLoop() {
		if (!suspended) {
			this.suspended = true;
			this.view.menuPanel.playButton.setEnabled(true);
			this.view.menuPanel.pauseButton.setEnabled(false);
		}
	}
	
	private void update() {
		this.model.update();
	}
	public void setUps(int newUps) {
		this.ups = newUps;
		this.timePerUpdate = 1000000000.0 / ups;
	}


}
