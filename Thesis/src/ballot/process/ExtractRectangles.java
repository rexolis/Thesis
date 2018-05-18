package ballot.preprocess;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import ballot.view.ShowResults;


public class ExtractRectangles extends ImageProcess {
	
	public Mat src, rectangleName;
	public List<MatOfPoint> contours;
	

	public ExtractRectangles(Mat src) {
		
		this.src = src;
	}
	
	public void getNamesRect(Mat src, List<MatOfPoint> contours) {
		
		//contours = findContours(src);
		for(int i = 0, j = 1; i < contours.size(); i++){
			if (Imgproc.contourArea(contours.get(i)) > 4000 ){

				Rect rect = Imgproc.boundingRect(contours.get(i));
				
				if (rect.height > 28){
					System.out.println(i+ ". Current rect: " + rect);
					rectangleName = cropImage(src, rect);
					saveImgRect("temp/name" + j++ + ".png" );
					
				}
			}
		}
		
		
	}
	
	public void saveImgRect(String filename) {
		
		new ShowResults().saveImage(rectangleName, filename);
	}
	
	public void setContours(List<MatOfPoint> contours) {
		
		this.contours = contours;
	}

}
