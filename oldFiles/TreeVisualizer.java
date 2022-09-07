package model;

import controller.Controller;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TreeVisualizer extends JFrame {

	public WorkingModel workingModel;

	public TreeVisualizer(Controller controller) {
		this.workingModel = new WorkingModel(controller);

	    frameSetUp(1920, 1080);
	}

	private void frameSetUp(int width, int height) {
		this.setTitle("Tree Visualizer");
		this.setSize(width, height);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().add(this.workingModel);
		this.setVisible(true);
	}
	@Override
	public void repaint() {
		super.repaint();
		this.workingModel.repaint();
	}
}