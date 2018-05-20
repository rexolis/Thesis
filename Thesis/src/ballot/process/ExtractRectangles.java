package ballot.process;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import ballot.view.ShowResults;


public class ExtractRectangles extends ImageProcess {
	
	public Mat src, rectangleName;
	

	public ExtractRectangles(Mat src) {
		
		this.src = gaussianBlur(src);
		
	}
	
	public void getNamesRect(List<MatOfPoint> contours) {
		
		for(int i = 0, j = 1; i < contours.size(); i++){
			if (Imgproc.contourArea(contours.get(i)) > 4000 ){

				Rect rect = Imgproc.boundingRect(contours.get(i));
				
				//prevents the largest contour to be included which is the whole picture
				if (rect.height > 28 && rect.x != 0 && rect.y != 0){
					//System.out.println(i+ ". Current rect: " + rect);
					rectangleName = cropImage(src, rect);
					saveImgRect("temp/name" + j++ + ".png" );
					
				}
			}
		}
		
		
	}
	
	public void saveImgRect(String filename) {
		
		new ShowResults().saveImage(rectangleName, filename);
	}
	
	public void extractText() {
		
		new OCR().fileTraverse();
	}

}
