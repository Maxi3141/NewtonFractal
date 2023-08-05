package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import math.ComplexNumber;
import math.FractalGenerator;
import math.Function;

public class FractalPanel extends JPanel {

	private static final long serialVersionUID = -1504919482454813797L;

	FractalGenerator fracGenerator;

	ComplexNumber topLeftCorner;
	ComplexNumber botRightCorner;

	ArrayList<Double> colorBasis;

	Color[][] colorMap;

	FractalPanel() {
		setBackground(Color.WHITE);
		colorMap = new Color[0][0];
		fracGenerator = new FractalGenerator();

		topLeftCorner = new ComplexNumber(-2, 2);
		botRightCorner = new ComplexNumber(2, -2);

		colorBasis = new ArrayList<Double>();
		colorBasis.add(0.305);
		colorBasis.add(0.65);
		colorBasis.add(0.8472);
		colorBasis.add(0.4861);
		colorBasis.add(0.0917);
	}

	public void createFractalAnimation(Function map, ComplexNumber topLeftCorner, ComplexNumber botRightCorner, double rootTol, double groupTol,  int maxIter) {
		colorMap = fracGenerator.createFractal(getWidth(), getHeight(), topLeftCorner, botRightCorner, map, colorBasis, rootTol, groupTol, maxIter);
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for(int colIndex = 0; colIndex < colorMap.length; colIndex++) {
			for(int rowIndex = 0; rowIndex < colorMap[colIndex].length; rowIndex++) {
				g.setColor(colorMap[colIndex][rowIndex]);
				g.fillRect(colIndex, rowIndex, 1, 1);
			}
		}
	}

}
