package ballot.process;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import ballot.view.MainPanel;
import ballot.view.ShowResults;
import ballot.view.ShowSelection;

public class ExtractSelection extends ImageProcess {

	public Mat userSelection, skewSelection, src;
	private Mat cell, blur, sharp, binary, thinned, eroded;
	private Rect rect;
	private List<Mat> names;
    private List<MatOfPoint> contours;
    private List<File> candidateCells;
    private List<Rect> candidates;

	int j=10;
	
	public ExtractSelection(Mat src) {
		
		this.src = src;
		
	}
	
	public void extractSelection() {
		
		userSelection = cropImage(src, rect);
		skewSelection = deskewSelection();
		//Core.copyMakeBorder(userSelection, userSelection, 3, 3, 3, 3, Core.BORDER_CONSTANT, new Scalar(0,0,0));
		//System.out.println(userSelection);
		getNames();
	}
	
	public Mat deskewSelection() {
		Mat rotImage;
		rotImage = deskew(userSelection, getSkewAngle(userSelection));
		return rotImage;		
	}

	public Mat getUserSelection () {
		return skewSelection;
	}
	
	public void setRect(Rect rect) {
		this.rect = rect;
	}
	
	public void getNames() {

		contours = findContours(thresholdBinary(skewSelection));
		//names = getNamesMat(contours, skewSelection);
		skewSelection = toRGB(skewSelection);
		drawContours();
		new ShowResults().saveImage(skewSelection, "selection" + ".png" );
		
		/*names = getSortedCandidates(candidates, skewSelection);
		
		for(int i=0,j=1; i < names.size(); i++) {
			new ShowResults().saveImage(names.get(i), "temp/candidate" + j++ + ".png" );
		}*/
	}
	
	public void drawContours() {
		List<Rect> candidates = new ArrayList<Rect>();
		
		//Finding the contours 
		for(int i = 0; i < contours.size(); i++){
	        if (Imgproc.contourArea(contours.get(i)) > 4000 ){
	            Rect rect1 = Imgproc.boundingRect(contours.get(i));
	            if (rect1.height > 28){
		            //System.out.println(i+". "+rect1);
		            candidates.add(rect1);
		            
		            for(int j = i+1; j < contours.size(); j++) {
		            	if (Imgproc.contourArea(contours.get(j)) > 4000 ){
		            		Rect rect2 = Imgproc.boundingRect(contours.get(j));
		            		if (rect2.height > 28){
		            			try {
		            				if(Math.abs(rect1.y - rect2.y) < 9) {
		            		            //System.out.println(j+". "+rect2);
		            					candidates.add(rect2);
		            				}
		
		            			}catch(IndexOutOfBoundsException exception) {
		    		        	    System.err.println("end of contours");
		    	            	}
		            		}
		            		
		            	}
		            }
		            i += candidates.size()-1; //starting off where j ended 
					//System.out.println("new start: " + (i+1));
	            }
	            //System.out.println("to be sorted: " + candidates);
	            this.candidates = candidates;
	            //sort here
	            sortRect(0, candidates.size()-1);
	            //System.out.println("sorted: " + this.candidates);
	            drawRect();
	            
	            candidates.clear();
	            //System.out.println("resetting the list: " + candidates);
	        }
        }
	}
	
	public void drawRect() {

		//System.out to .txt file
		WriteText wt = new WriteText();
		PrintWriter pw = wt.textWriter("selection.txt");
		Mat cell = new Mat();
		
		for(int i = 0; i < candidates.size(); i++){
            //Draw rectangle into the image
        	pw.println(i + ". " + candidates.get(i).x +","+candidates.get(i).y+","+candidates.get(i).height+","+candidates.get(i).width);
            Imgproc.rectangle(skewSelection, new Point(candidates.get(i).x,candidates.get(i).y), new Point(candidates.get(i).x+candidates.get(i).width,candidates.get(i).y+candidates.get(i).height),new Scalar(0,0,255), 1, Imgproc.LINE_AA, 0);
            
            cell = cropImage(skewSelection, candidates.get(i));
            new ShowResults().saveImage(cell, "temp/candidate" + j++ + ".png" );
		}
	}
	
	public void sortRect(int low, int high) {
		
		if (candidates == null || candidates.size() == 0)
			return;
 
		if (low >= high)
			return;
 
		// pick the pivot
		int middle = low + (high - low) / 2;
		int pivot = candidates.get(middle).x;
 
		// make left < pivot and right > pivot
		int i = low, j = high;
		while (i <= j) {
			while (candidates.get(i).x < pivot) {
				i++;
			}
 
			while (candidates.get(j).x > pivot) {
				j--;
			}
 
			if (i <= j) {
				/*int temp = candidates.get(i).x;
				candidates.get(i).x = candidates.get(j).x;
				candidates.get(j).x = temp;*/

				Rect tempRect = candidates.get(i);
				candidates.set(i, candidates.get(j));
				candidates.set(j, tempRect);
				
				i++;
				j--;
			}
		}
 
		// recursively sort two sub parts
		if (low < j)
			sortRect(low, j);
 
		if (high > i)
			sortRect(i, high);
		
	}
	
	public void showSelection(MainPanel mp) {
		
		new ShowSelection(getUserSelection(), mp);
	}
	
	public void extractText() {
		
		new OCR().fileTraverse();
	}
	
	public void preprocessCells(boolean binaryCheck, boolean truncCheck, boolean gaussianCheck, boolean sharpenCheck) {
		String dirName = "C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\temp";
		candidateCells = doListing(new File(dirName));
		
		for(int i=0, j=1; i<candidateCells.size(); i++, j++) {
        	cell = Imgcodecs.imread(candidateCells.get(i).getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
        	blur = gaussianBlur(cell, 1);
        	sharp = sharpen(cell, blur);
        	//binary = thresholdBinary(sharp);
        	binary = thresholdBinary(sharp);
        	//blur = gaussianBlur(binary, 1);
        	
        	binary = cropImage(binary, new Rect(3, 3, 
        										binary.width()-6,
        										binary.height()-6));
        	
        	thinned = new ZhangSuenThinning(binary).getImage();
        	
        	//the image is not inverted binary so erosion 
        	//works like dilation
        	eroded = erode(thinned);
        	//blur = gaussianBlur(sharp);
        	//trunc = thresholdTruncate(blur);
        	new ShowResults().saveImage(sharp, "sharp/candidate" + j + ".png" );
        	new ShowResults().saveImage(binary, "tempNew/candidate" + j + ".png" );
        	new ShowResults().saveImage(thinned, "thinned/candidate" + j + ".png" );
        	new ShowResults().saveImage(eroded, "dilated/candidate" + j + ".png" );
        	
        }
		extractText();
	}
	
} 
