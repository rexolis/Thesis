package ballot.process;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import ballot.view.ShowResults;

public class ExtractSelection extends ImageProcess {

	private Mat userSelection, src;
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

		contours = findContours(threshold(userSelection));
		names = getNamesMat(contours, userSelection);
		
		for(int i=0,j=1; i < names.size(); i++) {
			new ShowResults().saveImage(names.get(i), "tempNew/candidate" + j++ + ".png" );
		}
	}
}
