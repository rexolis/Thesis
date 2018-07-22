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


import ballot.view.MainPanel;
import ballot.view.ShowResults;

public class ExtractVoteArea extends ImageProcess {
	
	private Mat src;
	private PrintWriter pw;
	private boolean newImage;
	
	private ImageLoad imageLoad;
	private MainPanel frame;
	private ShowResults showResults;
	
	//variables to hold the details of the second largest contour denoting the voting area
    private double currLargest, largest/*, secondLargest*/;
    private Rect votingArea, document;
    private List<MatOfPoint> contours;
    
    private BufferedImage img;
    
    public int row, col;

	public ExtractVoteArea(Mat src, boolean newFile, ImageLoad imageLoad, MainPanel frame) {
		
		this.imageLoad = imageLoad;
		this.frame = frame;
		showResults = new ShowResults();
		detectCandidateCells(src, newFile);
		
		
	}
    
    public void detectCandidateCells(Mat src, boolean newImage) {
		
		Mat binarized, rotImage, canny;
		List<MatOfPoint> contours;
		
		this.newImage = newImage;
		
		//System.out to .txt file
		WriteText wt = new WriteText();
		pw = wt.textWriter("output.txt");
		 
        /*canny = canny(thresholdBinary(src));
        showResults.saveImage(canny, "canny.png");*/
		
		//deskew the image and use the deskewed image hereafter
		rotImage = deskew(src, getSkewAngle(src));
		System.err.println("vote area yet? " + !newImage);
		System.out.println("skew angle: " + getSkewAngle(src));
		this.src = rotImage; 

        //Binarization of the grayscaled Mat src
		//CV_8UC1 format
        binarized = thresholdBinary(rotImage);
        showResults.saveImage(binarized, "binary.png");
        
        //Finding contours
        //needs CV_8UC1 image format or grayscale (Imgproc. BGR2GRAY)
		contours = findContours(binarized);
		this.contours = contours;
        
		//show the loaded image into frame
		img = createAwtImage(rotImage);
		showResults.showImage(img, this.frame);
        
		//Resetting the image format to RBG from Grayscale before showing the detected rectangles in red
		//CV_8UC3 format
        this.src = toRGB(binarized);
        drawContours(this.contours);
        
        //extracts the voting area if it is the first call of this method
        if(newImage == true) {
        	updateVotingArea(rotImage);
    		showResults.saveImage(this.src, "input with contours.png");
        }
        //for cropped
        else {
    		showResults.saveImage(this.src, "cropped1.png");
        }
	}
	
	public void drawContours(List<MatOfPoint> contours) {
		int row=0, col=1, trueRow=0, trueCol=0;
		Rect currRect, rect = new Rect();
		//Finding the contours 
        for(int i=0; i < contours.size(); i++){
	        if (Imgproc.contourArea(contours.get(i)) > 4000 ){
	        	currRect = rect;
	        	rect = Imgproc.boundingRect(contours.get(i));
	        	currLargest = Imgproc.contourArea(contours.get(i));
	            //Draw rectangle into the image
	            if (rect.height > 28){
	            	try {
		        		//nextRect = Imgproc.boundingRect(contours.get(i+1));

		        		//get the number of rows and columns
			            if ((Math.abs(rect.y - currRect.y) < 8)) {

				            //System.out.println(i + ". "+rect);
			        		//System.out.println(i+1 + ". "+currRect);
			            	col++;
				    		//System.out.println("cols: " + col);
				    		trueCol = col;
			            }
			            else {
				    		trueRow = row;
			            	row++;
			        		//System.out.println("rows: " + row);
			            	col=1;
			            }
		        	}
		        	catch(IndexOutOfBoundsException exception) {
		        	    System.err.println("end of contours");
		        	}
	            	
		            //System.out.println(i + ". " + rect.x +","+rect.y+","+rect.height+","+rect.width);
		            pw.println(i + ". " + rect.x +","+rect.y+","+rect.height+","+rect.width + " [Contour Area: " + Imgproc.contourArea(contours.get(i)) + "]");
		            Imgproc.rectangle(src, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255), 1, Imgproc.LINE_AA, 0);
	            }
	            //getting the second largest contour next to the whole document's contour
	            if(newImage)
	            	setVotingArea(rect);
	            
	        }
        }
		System.out.println("rows: " + trueRow);
		System.out.println("cols: " + trueCol);
		
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
        	//secondLargest
        	votingArea = document;
        	
        	largest = currLargest;
        	document = rect;
        }
		
	}
}
