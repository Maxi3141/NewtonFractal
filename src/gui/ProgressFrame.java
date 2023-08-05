package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressFrame extends JFrame {
	
	// It would be really cool to have this as a working feature.
	// But the app also works without a progress bar, so it is not a priority for now.
	
	private static final long serialVersionUID = -6021908784958146059L;
	
	JLabel titleLabel;
	JProgressBar progBar;
	
	public ProgressFrame(int min, int max) {
		
		titleLabel = new JLabel("Computing...");
		progBar = new JProgressBar(min, max);
		
		setTitle("Progress");
		setSize(200,100);
		setLayout(new BorderLayout());
		
		add(titleLabel, BorderLayout.PAGE_START);
		add(progBar, BorderLayout.CENTER);
		
		setVisible(true);
		
	}
	
	public void setProgress(int prog) {
		progBar.setValue(prog);
	}
	
	public void closeProgBarWindow() {
		setVisible(false);
		dispose();
	}
	
}
