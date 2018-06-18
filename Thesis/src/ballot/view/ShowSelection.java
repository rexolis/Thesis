package ballot.view;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.opencv.core.Mat;

import ballot.process.ImageProcess;
import net.miginfocom.swing.MigLayout;

public class ShowSelection extends ImageProcess{

	private Mat selection;
	private JFrame frame;
	private JPanel selectionPanel;
	private JLabel selectionLabel = new JLabel();
	
	private JButton addTemplate;
	private JTextField category;
	private JTextField choice;
	
	private String categorayName;
	private int choiceMax;
	private BufferedImage selectionAwt;
	
	private ShowResults showResults;
	
	public ShowSelection(Mat selection) {
		
		frame = new JFrame("User Selection");
		selectionPanel = new JPanel();
		this.selection = selection;
			
		selectionAwt = createAwtImage(selection);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().add(selectionPanel); 
		frame.pack();
		frame.setLocationRelativeTo(null);  // *** this will center your app ***
		frame.setVisible(true);
		
		createPanel();
		
	}
	
	public void createPanel() {

		String panelSize = "\"w " + selection.width() + ", h " + selection.height() + "\"";
		selectionPanel.setLayout(new MigLayout());

		//frame.setSize(selection.height(), selection.width());
		selectionLabel.setIcon(new ImageIcon(selectionAwt));
		
		selectionPanel.add(new JScrollPane(selectionLabel), "wrap");
		//selectionPanel.add(selectionLabel, "w 1524, h 128");
		frame.add(selectionPanel);
	}
	
	public void showImage() {
		
	}
}
