package ballot.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ballot.preprocess.ExtractRectangles;
import ballot.preprocess.ImageLoad;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame = new JFrame("Ballot Template Generator");
	JPanel mainPanel = new JPanel();
	JPanel sidePanel = new JPanel();
	JPanel showPanel = new JPanel();
    JFileChooser fc;

	JButton btnSelectBallot = new JButton("Select Ballot");
	JButton btnGetVoteArea = new JButton("Extract Vote Area");
	JButton btnBallotTemplate = new JButton("Generate Ballot Template");
	JButton btnGetNames = new JButton("Extract Names");
	
	public JLabel lbl1;
	private ImageLoad il;
	private ExtractRectangles er;
	
	
	
	public MainFrame() {
		
        //Create a file chooser
        fc = new JFileChooser();
        
        //new ExtractVoteArea(this);
        
		mainPanel.setLayout(new MigLayout());
		sidePanel.setLayout(new MigLayout("gap 5 5, ins 10, wrap 3"));
		showPanel.setLayout(new MigLayout("wrap 3", "[]10[]"));
		
		//JScrollPane showScrollPane = new JScrollPane();
		
		sidePanel.setBorder(
                BorderFactory.createTitledBorder("Options")
        );
		showPanel.setBorder(
                BorderFactory.createTitledBorder("Output")
        );
		
		
		sidePanel.add(btnSelectBallot, "wrap");
		
		
		lbl1 = new JLabel();
        lbl1.setBorder(
            BorderFactory.createEtchedBorder()
        );
        
        showPanel.add(lbl1, "w 600, h 670");
        //showPanel.add(showScrollPane);
		

		mainPanel.add(sidePanel, "dock west");
		mainPanel.add(showPanel);
		

		btnSelectBallot.addActionListener(this);
		btnGetVoteArea.addActionListener(this);
		btnGetNames.addActionListener(this);
		
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSelectBallot) {
			
			int returnVal = fc.showOpenDialog(MainFrame.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
                il = new ImageLoad(file.getName(), this);

        		sidePanel.add(btnGetVoteArea, "wrap");
        		
        		// TODO needs to be furnished. button sizes should be uniform
        		mainPanel.validate();
        		mainPanel.repaint();
            }
			
		}
		
		//this can only be accessed if there is a ballot loaded in the program 
		else if (e.getSource() == btnGetVoteArea) {

			il.setContoursCropped();
    		sidePanel.add(btnGetNames, "wrap");
		}
		
		
		//this can only be accessed if there is a ballot is already cropped
		else if (e.getSource() == btnGetNames) {
			
			if (er == null) {
				//System.out.println("GOOOOO");
				er = new ExtractRectangles(il.getCroppedImage());
				er.getNamesRect(il.getCroppedImage(), il.contours);
				
			}

		}
	}

}
