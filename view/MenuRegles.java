package view;

import controller.Controller;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuRegles extends JPanel{
    public JPanel reglesVivantes=new JPanel(new GridLayout(1,8));
	public JPanel reglesMortes=new JPanel(new GridLayout(1,8));
	public JPanel validerPanel=new JPanel();
	public JButton valider=new JButton("Valider");
	public JLabel[] titre = new JLabel[2];
	public JCheckBox[] cocheVivantes=new JCheckBox[8];
	public JCheckBox[] cocheMortes=new JCheckBox[8];
	public Color bgColor=new Color(200,200,200);
    Controller controller;

    public MenuRegles(Controller c){
        this.controller=c;
        this.setSize(600,200);
        this.setVisible(true);
        this.setLayout(new GridLayout(5,1));
        titre[0]=new JLabel("Nombre de cellules voisines vivantes pour\n qu'une cellule vivante reste en vie :");
		titre[0].setOpaque(false);
	    titre[1]=new JLabel("Nombre de cellules voisines vivantes pour\n qu'une cellule morte revienne a la vie :");
		titre[1].setOpaque(false);
	    //reglesVivantes.add(titre[0]);
	    //reglesMortes.add(titre[1]);
	    reglesVivantes.setBackground(bgColor);
	    reglesVivantes.setVisible(true);
	    reglesMortes.setBackground(bgColor);
	    reglesMortes.setVisible(true);
	    for(int i=0;i<8;i++) {
	    	cocheVivantes[i]=new JCheckBox(Integer.toString(i+1));
	    	cocheMortes[i]=new JCheckBox(Integer.toString(i+1));
	    	reglesVivantes.add(cocheVivantes[i]);
	    	reglesMortes.add(cocheMortes[i]);
	    	cocheVivantes[i].setBackground(bgColor);
	    	cocheMortes[i].setBackground(bgColor);
	    }
	    cocheVivantes[1].setSelected(true);
	    cocheVivantes[2].setSelected(true);
	    cocheMortes[2].setSelected(true);
	    JPanel titre0=new JPanel();
	    titre0.setBackground(bgColor);
	    titre0.add(titre[0]);
	    JPanel titre1=new JPanel();
	    titre1.setBackground(bgColor);
	    titre1.add(titre[1]);
	    this.add(titre0);
	    this.add(reglesVivantes);
	    this.add(titre1);
	    this.add(reglesMortes);
	    validerPanel.add(valider);
	    validerPanel.setBackground(bgColor);
	    this.add(validerPanel);
	    

       

    }
    
    public void addValiderReglesButtonListener(ActionListener l) {
		valider.addActionListener(l);
	}
    

}
