package trial.rectangle;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

public class RectangleNew{
	
	public static void main(String args[]){
	    // Load the library

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	    // Consider the image for processing
		Mat image = Imgcodecs.imread("images/ph2016,jpg", Imgcodecs.IMREAD_GRAYSCALE);
		Mat imageHSV = new Mat(image.size(), CvType.CV_8UC4);
	    Mat imageBlurr = new Mat(image.size(), CvType.CV_8UC4);
	    Mat imageA = new Mat(image.size(), CvType.CV_32F);
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);

	    Imgcodecs.imwrite("images/rectangleNew1.jpg",imageBlurr);

	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
	    Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    //Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));
	    for(int i=0; i< contours.size();i++){
	        System.out.println(Imgproc.contourArea(contours.get(i)));
	        if (Imgproc.contourArea(contours.get(i)) > 50 ){
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            System.out.println(rect.height);
	            if (rect.height > 28){
	            //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
	            Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
	            }
	        }
	    }
	    Imgcodecs.imwrite("images/rectangleNew1Final.jpg",image);
	}
}