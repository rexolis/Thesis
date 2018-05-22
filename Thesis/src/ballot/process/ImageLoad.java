package ballot.process;

import java.util.List;
import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;

import ballot.view.MainPanel;

public class ImageLoad{
	
	private Mat src, cropped, srcColored;
	private ExtractVoteArea extract;
	public List<MatOfPoint> contours;
	
	public ImageLoad(String args, MainPanel frame) {
		
		String filename = "images/" + args;
        // Load an image in a grayscale format
        src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        
        //Load a colored version of image
        srcColored = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
        System.out.println("Grayscale: " + src + "\nOriginal: " + srcColored);
        
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + filename +"] \n");
            System.exit(-1);
        }
        
        extract = new ExtractVoteArea(this.getImg(), true, this, frame, new File(filename));
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
		
		return this.cropped;
		
	}
	
	//processes the cropped image to update the contours for further processing
	public void setContoursCropped() {
		
		//this.cropped is the updated image
		//false means that this is not the original image
		extract.detectVotingArea(this.cropped, false);
		contours = extract.getContours();
		
	}
}