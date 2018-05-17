package trial.gaussian;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class GaussianBlur {
	
	//private Button button;
	public static String path = "images/ph2016.jpg";
	
	public void blur() {
		
		//read the RGB image
		Mat src = Imgcodecs.imread(path, Imgcodecs.IMREAD_COLOR);
		
		//mat gray image holder
		Mat imgBlur = new Mat();
		
		//mat canny image
		//Mat imageCny = new Mat();
		
		Imgproc.GaussianBlur(src, imgBlur, new Size(3,3), 0, 0);
		HighGui.imshow("output", imgBlur);
        HighGui.waitKey();
        System.exit(0);
		
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		GaussianBlur gaussianBlur = new GaussianBlur();
		gaussianBlur.blur();
	}
}
