package ballot.process;

import java.util.List;
import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;

import ballot.view.MainPanel;
import ballot.view.ShowResults;

public class ImageLoad{
	
	private Mat src, cropped, srcColored, userSelection;
	private ExtractVoteArea extract;
	public List<MatOfPoint> contours;
	
	public ImageLoad(String args, MainPanel frame) {
		
		String filename = "images/" + args;
		//String filename = "images/selection.png";
        // Load an image in a grayscale format
        src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        new ShowResults().saveImage(src, "input.png");
        //Load a colored version of image
        srcColored = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
        System.out.println("Grayscale: " + src + "\nOriginal: " + srcColored);
        
        // Check if image is loaded fine
        if( src.empty() ) {
        	System.out.println("src: " + src);
        	System.out.println("srcColored: " + srcColored);
            System.err.println("Error opening image!");
            System.err.println("Program Arguments: [image_name -- default "
                    + filename +"] \n");
            System.exit(-1);
        }
        
        extract = new ExtractVoteArea(this.getImg(), true, this, frame);
        //extract.showResultsGetInstance();
	}
	
	public Mat getImg () {
		return src;
	}
	
	//updates the image with the voting area only
	public void setCroppedImage (Mat cropped) {
		this.cropped = cropped;
	}
	
	//returns the cropped image (with the voting area)
	public Mat getCroppedImage () {
		return cropped;
	}
	
	public void setUserSelection(Mat userSelection) {
		this.userSelection = userSelection;
	}
	
	public Mat getUserSelection() {
		return userSelection;
	}
	
	//processes the cropped image to update the contours for further processing
	public void setContoursCropped() {
		
		//this.cropped is the updated image
		//false means that this is not the original image
		extract.detectCandidateCells(cropped, false);
		contours = extract.getContours();
		
	}
}