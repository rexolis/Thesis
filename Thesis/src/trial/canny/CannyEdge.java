package trial.canny;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class CannyEdge {
	
	public static String path = "images/ph2016_2.jpg";
	
	public void detectEdges(){
		
		//read the RGB image
		Mat rgbImage = Imgcodecs.imread(path, Imgcodecs.IMREAD_COLOR);
		//mat gray image holder
		Mat imageGray = new Mat();
		//mat canny image
		Mat imageCny = new Mat();
		//Show the RGB Image
		
		
		Imgproc.cvtColor(rgbImage, imageGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(imageGray, imageCny, 10, 100, 3, true);
		HighGui.imshow("output", imageCny);
        HighGui.waitKey();
        System.exit(0);
	
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		CannyEdge edgeDetection = new CannyEdge();
		edgeDetection.detectEdges();
	}
}