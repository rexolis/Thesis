package ballot.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import ballot.process.ExtractRectangles;
import ballot.process.ImageLoad;
import net.miginfocom.swing.MigLayout;

public class MainPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame = new JFrame("Ballot Template Generator");
	//JPanel mainPanel = new JPanel();
	JPanel sidePanel = new JPanel();
	JPanel showPanel = new JPanel();
    JFileChooser fc;
    JScrollPane sp;
    //JLabel lbl1;

	JButton btnSelectBallot = new JButton("Select Ballot");
	JButton btnGetVoteArea = new JButton("Extract Vote Area");
	JButton btnBallotTemplate = new JButton("Generate Ballot Template");
	JButton btnGetNames = new JButton("Extract Names");
	
	private ImageLoad il;
	private ExtractRectangles er;
	public ShowLabel showLabel = new ShowLabel(showPanel);
	
	
	
	public MainPanel() {
		
        //Create a file chooser
        fc = new JFileChooser();
        ImagePreviewPanel preview = new ImagePreviewPanel();
        fc.setAccessory(preview);
        fc.addPropertyChangeListener(preview);
        
        //new ExtractVoteArea(this);
        //DrawRect drawRect = new DrawRect();
        //showLabel = new ShowLabel();
        
		this.setLayout(new MigLayout());
		sidePanel.setLayout(new MigLayout("gap 5 5, ins 10, wrap 3"));
		showPanel.setLayout(new MigLayout("wrap 3", "[]10[]"));
		
		sidePanel.setBorder(
                BorderFactory.createTitledBorder("Options")
        );
		showPanel.setBorder(
                BorderFactory.createTitledBorder("Output")
        );

		showPanel.add(showLabel, "w 1000, h 670");
		
		/*lbl1 = new JLabel();
		lbl1.setBorder(
             BorderFactory.createEtchedBorder()
		);*/
        //sp = new JScrollPane(showLabel.lbl1);
        //showPanel.add(sp, "w 1000, h 670");
		
        
		sidePanel.add(btnSelectBallot, "wrap");
		sidePanel.add(btnGetVoteArea, "wrap");
		sidePanel.add(btnGetNames, "wrap");
		
		btnGetVoteArea.setEnabled(false);
		btnGetNames.setEnabled(false);

        this.revalidate();

		this.add(sidePanel, "dock west");
		this.add(showPanel);

		btnSelectBallot.addActionListener(this);
		btnGetVoteArea.addActionListener(this);
		btnGetNames.addActionListener(this);
		
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
		repaint();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSelectBallot) {
			
			int returnVal = fc.showOpenDialog(MainPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
                il = new ImageLoad(file.getName(), this);

        		//sidePanel.add(btnGetVoteArea, "wrap");
                btnGetVoteArea.setEnabled(true);
        		
        		// TODO needs to be furnished. button sizes should be uniform
        		this.validate();
        		this.repaint();
            }
			
		}
		
		//this can only be accessed if there is a ballot loaded in the program 
		else if (e.getSource() == btnGetVoteArea) {

			il.setContoursCropped();
    		//sidePanel.add(btnGetNames, "wrap");
			btnGetNames.setEnabled(true);
		}
		
		
		//this can only be accessed if there is a ballot is already cropped
		else if (e.getSource() == btnGetNames) {
			
			//if (er == null) {
				//System.out.println("GOOOOO");
				er = new ExtractRectangles(il.getCroppedImage());
				er.getNamesRect(il.contours);
				er.extractText();
				
			//}

		}
	}
	

}
