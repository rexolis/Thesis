package ballot.view;

import java.awt.event.*;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ballot.process.ExtractSelection;
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
	JPanel imgprocPanel = new JPanel();
	//JPanel categorize = new JPanel();
    JFileChooser fc;
    JScrollPane sp;
    //JLabel lbl1;

	JButton btnSelectBallot = new JButton("Select Ballot");
	JButton btnGetVoteArea = new JButton("Extract Vote Area");
	JButton btnSetSelection = new JButton("Set Selection");
	JButton btnExtractSelection = new JButton("Extract Selection");
	JButton btnPrintNames = new JButton("Print Names");

	JCheckBox binary = new JCheckBox("Binary");
	JCheckBox trunc = new JCheckBox("Truncate");
	JCheckBox gaussian = new JCheckBox("Gaussian Blur");
	JCheckBox sharpen = new JCheckBox("Sharpen");
	
	private ImageLoad il;
	//private ExtractRectangles er;
	private ExtractSelection es;
	//public ShowLabel showLabel = new ShowLabel();
	public ShowLabel2 showLabel2 = new ShowLabel2();
	
	
	
	public MainPanel() {
		
        //Create a file chooser
        fc = new JFileChooser();
        ImagePreviewPanel preview = new ImagePreviewPanel();
        fc.setAccessory(preview);
        fc.addPropertyChangeListener(preview);
        fc.setMultiSelectionEnabled(true);
        //File[] files = fc.getSelectedFiles();
        
        //new ExtractVoteArea(this);
        //DrawRect drawRect = new DrawRect();
        //showLabel = new ShowLabel();
        
		this.setLayout(new MigLayout());
		sidePanel.setLayout(new MigLayout("gap 5 5, ins 10, wrap 3"));
		showPanel.setLayout(new MigLayout());
		imgprocPanel.setLayout(new MigLayout());
		//categorize.setLayout(new MigLayout());
		
		sidePanel.setBorder(
                BorderFactory.createTitledBorder("Options")
        );
		showPanel.setBorder(
                BorderFactory.createTitledBorder("Output")
        );
		imgprocPanel.setBorder(
                BorderFactory.createTitledBorder("Image Processing")
        );

		showPanel.add(showLabel2.thisPanel, "w 1000, h 670");
		
		/*lbl1 = new JLabel();
		lbl1.setBorder(
             BorderFactory.createEtchedBorder()
		);*/
        //sp = new JScrollPane(showLabel.lbl1);
        //showPanel.add(sp, "w 1000, h 670");

		imgprocPanel.add(binary, "wrap -5");
		imgprocPanel.add(trunc, "wrap -5");
		imgprocPanel.add(gaussian, "wrap -5");
		imgprocPanel.add(sharpen, "wrap");
		
		/*ItemListener itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)
					handleItemEvent(e);
			}
		};

		binary.addItemListener(itemListener);
		trunc.addItemListener(itemListener);
		gaussian.addItemListener(itemListener);
		sharpen.addItemListener(itemListener);*/
		
		sidePanel.add(btnSelectBallot, "wrap");
		sidePanel.add(btnGetVoteArea, "wrap");
		sidePanel.add(btnSetSelection, "wrap");
		sidePanel.add(btnExtractSelection, "wrap");
		sidePanel.add(btnPrintNames, "wrap");
		//sidePanel.add(imgprocPanel);
		
		btnGetVoteArea.setEnabled(false);
		btnSetSelection.setEnabled(false);
		btnExtractSelection.setEnabled(false);
		btnPrintNames.setEnabled(false);

        this.revalidate();

		this.add(sidePanel, "dock west");
		this.add(showPanel);

		btnSelectBallot.addActionListener(this);
		btnGetVoteArea.addActionListener(this);
		btnSetSelection.addActionListener(this);
		btnExtractSelection.addActionListener(this);
		btnPrintNames.addActionListener(this);
		
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
		repaint();
		
	}
	
	/*protected void handleItemEvent(ItemEvent e) {
		if(e.getSource() == binary) {
			System.out.println("Binary Ticked");
		}
		else if(e.getSource() == trunc) {
			System.out.println("Truncate Ticked");
		}
		else if(e.getSource() == gaussian) {
			System.out.println("Gaussian Blur Ticked");
		}
		else if(e.getSource() == sharpen) {
			System.out.println("Sharpen Ticked");
		}
	}*/



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSelectBallot) {
			
			int returnVal = fc.showOpenDialog(MainPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				File file = fc.getSelectedFile();
				il = new ImageLoad(file.getName(), this);
				
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
			btnSetSelection.setEnabled(true);
		}
		
		//this can only be accessed if there is a ballot is already cropped
		else if (e.getSource() == btnSetSelection) {
			
			//er = new ExtractRectangles(il.getCroppedImage());
			es = new ExtractSelection(il.getCroppedImage());
			//showLabel2.setERInstance(er);
			showLabel2.addListeners();
			//showLabel2.points.clear();
			showLabel2.setESInstance(es);
			btnExtractSelection.setEnabled(true);

		}
		else if (e.getSource() == btnExtractSelection) {
			
			//er.setESInstance(es);
			es.extractSelection();

			es.showSelection(this);
			btnPrintNames.setEnabled(true);

		}

		else if (e.getSource() == btnPrintNames) {
			boolean binaryCheck, truncCheck, gaussianCheck, sharpenCheck;
			binaryCheck = truncCheck = gaussianCheck = sharpenCheck = false;
			
			if(binary.isSelected()) {
				binaryCheck = true;
			}
			if(trunc.isSelected()) {
				truncCheck = true;
			}
			if(gaussian.isSelected()) {
				gaussianCheck = true;
			}
			if(sharpen.isSelected()) {
				sharpenCheck = true;
			}
			
			es.preprocessCells(binaryCheck, truncCheck, gaussianCheck, sharpenCheck);
			//es.extractText();
			
			
//			er.setESInstance(es);
//			es.extractSelection();
//			btnPrintNames.setEnabled(true);

		}
		
		else {
			System.out.println("clicked something else");
			
		}
		
		//this can only be accessed if there is a ballot is already cropped
//		else if (e.getSource() == btnCategorizeNames) {
//			
//			int returnVal = fc.showOpenDialog(MainPanel.this);
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//				
//				File[] files = fc.getSelectedFiles();
//				
//				System.out.println("Number of files: " + files.length);
//                
//				for (File file : files) {
//					System.out.println(file.getName());
//                }
//				
//        		//sidePanel.add(btnGetVoteArea, "wrap");
//                btnGetVoteArea.setEnabled(true);
//        		
//        		this.validate();
//        		this.repaint();
//            }
//		}
	}
	
	

}
