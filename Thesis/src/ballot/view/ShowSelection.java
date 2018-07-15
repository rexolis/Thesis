package ballot.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ShowSelection extends ImageProcess implements ActionListener{

	//private Mat selection;
	private JFrame frame;
	private JPanel selectionPanel;
	private JLabel selectionLabel = new JLabel();
 
	private JButton btnAddTemplate = new JButton("Add to Template");
	private JButton btnCancel = new JButton("Cancel");
	private JLabel lblPosition = new JLabel("Position: ");
	private JLabel lblChoice = new JLabel("Maximum Choices: ");
	private JTextField position = new JTextField(30);
	private JTextField choice = new JTextField(30);
	
	private BufferedImage selectionAwt;
	
	public ShowSelection(Mat selection, MainPanel mp) {
		
		frame = new JFrame("User Selection");
		selectionPanel = new JPanel();
		//this.selection = selection;
		
		selectionAwt = createAwtImage(selection);
		//selection = createMatImage(selectionAwt);

		System.out.println(selection);
		
		createPanel();

		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().add(selectionPanel); 
		frame.pack();
		frame.setLocationRelativeTo(null);  // *** this will center your app ***
		frame.setVisible(true);
		
		
	}
	
	public void createPanel() {

		//String panelSize = "\"w " + selection.width() + ", h " + selection.height() + "\"";
		selectionPanel.setLayout(new MigLayout());

		selectionLabel.setIcon(new ImageIcon(selectionAwt));
		selectionPanel.add(new JScrollPane(selectionLabel), "span 10, wrap");
		selectionPanel.add(lblPosition);
		selectionPanel.add(position, "wrap");
		selectionPanel.add(lblChoice);
		selectionPanel.add(choice, "wrap");
		selectionPanel.add(btnAddTemplate, "skip 2");
		selectionPanel.add(btnCancel);
		
		btnAddTemplate.addActionListener(this);
		btnCancel.addActionListener(this);
		
		frame.add(selectionPanel);
	}
	
	public void showImage() {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == this.btnAddTemplate) {
			System.out.println("clicked add");
			//System.exit(1);
			frame.dispose();
			
		}
		else if(e.getSource() == this.btnCancel) {
			System.out.println("clicked cancel");
			frame.dispose();

			
		}
		
	}
}
