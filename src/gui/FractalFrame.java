package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import math.Analyzer;
import math.ComplexNumber;
import math.Function;

public class FractalFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8393018619441523500L;

	JLabel functionLabel;
	JTextField functionTextField;
	Analyzer functionAnalyzer;
	JButton refreshButton;

	JLabel topLeftLabel0;
	JTextField topLeftTextField0;
	JLabel topLeftLabel1;
	JTextField topLeftTextField1;
	JLabel topLeftLabel2;

	JLabel botRightLabel0;
	JTextField botRightTextField0;
	JLabel botRightLabel1;
	JTextField botRightTextField1;
	JLabel botRightLabel2;

	JLabel rootTolLabel;
	JTextField rootTolTextField;

	JLabel groupTolLabel;
	JTextField groupTolTextField;

	JLabel maxIterLabel;
	JTextField maxIterTextField;

	JPanel optionPanel;

	FractalPanel fracPanel;

	public FractalFrame() {
		fracPanel = new FractalPanel();

		functionLabel = new JLabel("f(z) = ");
		functionTextField = new JTextField();
		functionTextField.setText("z^3-1");
		functionAnalyzer = new Analyzer();
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(this);

		JPanel optionPanelTop = new JPanel();
		optionPanelTop.setLayout(new BorderLayout());
		optionPanelTop.add(functionLabel, BorderLayout.LINE_START);
		optionPanelTop.add(functionTextField, BorderLayout.CENTER);
		optionPanelTop.add(refreshButton, BorderLayout.LINE_END);

		topLeftLabel0 = new JLabel("top left corner = (");
		topLeftTextField0 = new JTextField(3);
		topLeftTextField0.setText("-2");
		topLeftLabel1 = new JLabel(",");
		topLeftTextField1 = new JTextField(3);
		topLeftTextField1.setText("2");
		topLeftLabel2 = new JLabel(")");

		botRightLabel0 = new JLabel("| bottom right corner = (");
		botRightTextField0 = new JTextField(3);
		botRightTextField0.setText("2");
		botRightLabel1 = new JLabel(",");
		botRightTextField1 = new JTextField(3);
		botRightTextField1.setText("-2");
		botRightLabel2 = new JLabel(")");

		rootTolLabel = new JLabel("Tolerance (root) = ");
		rootTolTextField = new JTextField(4);
		rootTolTextField.setText("0.001");

		groupTolLabel = new JLabel("| Tolerance (grouping) = ");
		groupTolTextField = new JTextField(4);
		groupTolTextField.setText("0.1");

		maxIterLabel = new JLabel("| max. Iteration = ");
		maxIterTextField = new JTextField(4);
		maxIterTextField.setText("20");

		JPanel optionPanelMid = new JPanel();
		optionPanelMid.add(rootTolLabel);
		optionPanelMid.add(rootTolTextField);

		optionPanelMid.add(groupTolLabel);
		optionPanelMid.add(groupTolTextField);

		optionPanelMid.add(maxIterLabel);
		optionPanelMid.add(maxIterTextField);

		JPanel optionPanelBot = new JPanel();
		optionPanelBot.add(topLeftLabel0);
		optionPanelBot.add(topLeftTextField0);
		optionPanelBot.add(topLeftLabel1);
		optionPanelBot.add(topLeftTextField1);
		optionPanelBot.add(topLeftLabel2);

		optionPanelBot.add(botRightLabel0);
		optionPanelBot.add(botRightTextField0);
		optionPanelBot.add(botRightLabel1);
		optionPanelBot.add(botRightTextField1);
		optionPanelBot.add(botRightLabel2);

		optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(3,1));
		optionPanel.add(optionPanelTop);
		optionPanel.add(optionPanelMid);
		optionPanel.add(optionPanelBot);

		setTitle("Newton Fractal");
		setSize(900, 950);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		add(optionPanel, BorderLayout.PAGE_START);
		add(fracPanel, BorderLayout.CENTER);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == refreshButton) {
			Function currentFunction = new Function(functionAnalyzer.createFuncFromString(functionTextField.getText()));
			currentFunction.debugTypePrint();

			ComplexNumber topLeftCorner = new ComplexNumber(-2,2);
			ComplexNumber botRightCorner = new ComplexNumber(2,-2);
			double rootTol = 0.001;
			double groupTol = 0.1;
			int maxIter = 100;

			try {
				topLeftCorner.setReal(Double.parseDouble(topLeftTextField0.getText()));
				topLeftCorner.setImag(Double.parseDouble(topLeftTextField1.getText()));
				botRightCorner.setReal(Double.parseDouble(botRightTextField0.getText()));
				botRightCorner.setImag(Double.parseDouble(botRightTextField1.getText()));
				rootTol = Double.parseDouble(rootTolTextField.getText());
				groupTol = Double.parseDouble(groupTolTextField.getText());
				maxIter = Integer.parseInt(maxIterTextField.getText());
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Error", "Invalid inputs!", JOptionPane.ERROR_MESSAGE);
			}


			fracPanel.createFractalAnimation(currentFunction, topLeftCorner, botRightCorner, rootTol, groupTol, maxIter);
		}
	}

}
