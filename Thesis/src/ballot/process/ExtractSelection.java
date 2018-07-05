package ballot.process;

import java.io.File;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import ballot.view.MainPanel;
import ballot.view.ShowResults;
import ballot.view.ShowSelection;

public class ExtractSelection extends ImageProcess {

	public Mat userSelection, src;
	private Rect rect;
	private List<Mat> names;
    private List<MatOfPoint> contours;
	
	public ExtractSelection(Mat src) {
		
		this.src = src;
		
	}
	
	public void extractSelection() {
		
		userSelection = cropImage(src, rect);
		Core.copyMakeBorder(userSelection, userSelection, 5, 5, 5, 5, Core.BORDER_REPLICATE, new Scalar(0,0,0));
		//System.out.println(userSelection);
		new ShowResults().saveImage(userSelection, "selection" + ".png" );
		getNames();
	}

	public Mat getUserSelection () {
		return userSelection;
	}
	
	public void setRect(Rect rect) {
		this.rect = rect;
	}
	
	public void getNames() {

		contours = findContours(thresholdBinary(userSelection));
		names = getNamesMat(contours, userSelection);
		
		for(int i=0,j=1; i < names.size(); i++) {
			new ShowResults().saveImage(names.get(i), "temp/candidate" + j++ + ".png" );
		}
	}
	
	public void showSelection(MainPanel mp) {
		
		new ShowSelection(getUserSelection(), mp);
	}
	
	public void extractText() {
		
		new OCR().fileTraverse();
	}
	
	public void preprocessCells() {
		Mat src, blur, sharp;
		String dirName = "C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\temp";
		List<File> candidateCells = doListing(new File(dirName));
		
		for(int i=0, j=1; i<candidateCells.size(); i++) {
        	src = Imgcodecs.imread(candidateCells.get(i).getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
        	blur = gaussianBlur(src);
        	
        	sharp = sharpen(blur);
        	new ShowResults().saveImage(sharp, "tempNew/candidate" + j++ + ".png" );
        	
			
			//writeToTxt(candidateCells.get(i));
        }
	}
}
