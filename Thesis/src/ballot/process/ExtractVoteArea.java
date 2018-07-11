package ballot.process;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.recognition.software.jdeskew.ImageDeskew;

import ballot.view.MainPanel;
import ballot.view.ShowResults;

public class ExtractVoteArea extends ImageProcess {
	
	private Mat src;
	private PrintWriter pw;
	private File filename;
	private boolean newImage;
	
	private ImageLoad imageLoad;
	private MainPanel frame;
	private ShowResults showResults;
	
	//variables to hold the details of the second largest contour denoting the voting area
    private double currLargest, largest/*, secondLargest*/;
    private Rect votingArea, document;
    private List<MatOfPoint> contours;
    
    private BufferedImage img, srcBI;

	public ExtractVoteArea(Mat src, boolean newFile, ImageLoad imageLoad, MainPanel frame, File filename) {
		
		this.imageLoad = imageLoad;
		this.frame = frame;
		this.filename = filename;
		showResults = new ShowResults();
		detectVotingArea(src, newFile);
		
		
	}
    
    public void detectVotingArea(Mat src, boolean newImage) {
		
		Mat binarized, rotImage;
		List<MatOfPoint> contours;
		double skewAngle;
		
		this.newImage = newImage;
		
		//create a local input image
		this.src = src; 
		
		//System.out to .txt file
		WriteText wt = new WriteText();
		pw = wt.textWriter("output.txt");
         
        //Binarization of the grayscaled Mat src
		//CV_8UC1 format
        binarized = thresholdBinary(this.src);
        
        //Finding contours
        //needs CV_8UC1 image format or grayscale (Imgproc.BGR2GRAY)
		contours = findContours(binarized);
		this.contours = contours;

        //Resetting the image format to RBG from Grayscale before showing the detected rectangles in red
		//CV_8UC3 format
        this.src = toRGB(src);
        
        //deskew the input image
		srcBI = createAwtImage(this.src);
        ImageDeskew id = new ImageDeskew(srcBI);
		skewAngle = id.getSkewAngle();
		System.out.println("Skew Angle: " + skewAngle);
		
		rotImage = deskew(src, skewAngle);
		this.src = rotImage;
        
		//show the loaded image into frame
		img = createAwtImage(this.src);
		showResults.showImage(img, this.frame, filename);
        
        drawContours(contours, src);
        
        //extracts the voting area if it is the first call of this method
        if(newImage == true) {
        	//System.out.println("Voting Area: " + votingArea.x +","+votingArea.y+","+votingArea.height+","+votingArea.width + " [Contour Area: " + secondLargest + "]");
        	updateVotingArea(src);
        }
        //for cropped
        else {
    		showResults.saveImage(this.src, "cropped1.png");
        }
	}
	
	public void drawContours(List<MatOfPoint> contours, Mat src) {
		
		//Finding the contours 
        for(int i = 0; i < contours.size(); i++){
        	
	        //System.out.println("Contour Area: " + Imgproc.contourArea(contours.get(i)));
	        
	        if (Imgproc.contourArea(contours.get(i)) > 4000 ){
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            //System.out.println(rect.height);
	            currLargest = Imgproc.contourArea(contours.get(i));
	            //Draw rectangle into the image
	            if (rect.height > 28){
		            //System.out.println(i + ". " + rect.x +","+rect.y+","+rect.height+","+rect.width);
		            pw.println(i + ". " + rect.x +","+rect.y+","+rect.height+","+rect.width + " [Contour Area: " + Imgproc.contourArea(contours.get(i)) + "]");
		            Imgproc.rectangle(this.src, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255), 1, Imgproc.LINE_AA, 0);
	            }
	            //getting the second largest contour next to the whole document's contour
	            if(newImage)
	            	setVotingArea(rect);
	        }
        }
		
	}
	
	public void updateVotingArea(Mat src) {
		
		Mat cropped = cropImage(src, votingArea);
		imageLoad.setCroppedImage(cropped);
		//show output
        //showResults.showWindow(cropped, "cropped_VotingAreaPH2016.png");
	}
	
	public List<MatOfPoint> getContours() {
		
		return contours;
	}
	
	public void setVotingArea(Rect rect) {
		
		if(currLargest > largest) {
        	//secondLargest = largest;
        	votingArea = document;
        	
        	largest = currLargest;
        	document = rect;
        }
		
	}
}
