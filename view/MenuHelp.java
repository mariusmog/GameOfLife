package view;

import controller.Controller;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MenuHelp extends JPanel{
    Controller controller;
    public JComboBox<String> menuDeroulant;
    public JButton Valider;

    public MenuHelp(Controller c){
        this.controller=c;
        this.setSize(200,200);
        this.setVisible(true);
        menuDeroulant=new JComboBox<>();
        String SE = System.getProperty("os.name").toLowerCase();
		String chemin ="";
		if(SE.indexOf("win") >= 0) {
			chemin ="ressources\\structures";
		}else {
			chemin="ressources/structures";
		}
		File dossier=new File(chemin);
        for (File file : dossier.listFiles()) {
        	if(file.getName().substring(file.getName().length()-4).equals(".txt")) {
        		menuDeroulant.addItem(file.getName().substring(0,file.getName().length()-4));
        	}
        }
        Valider = new JButton("Valider");
        this.add(new JLabel("Selectionnez une grille : "));
        this.add(menuDeroulant);
        this.add(Valider);
        this.setBackground(new Color(200,200,200));
    }
    
    public void addValiderTemplatesButtonListener(ActionListener l) {
		Valider.addActionListener(l);
	}

}
