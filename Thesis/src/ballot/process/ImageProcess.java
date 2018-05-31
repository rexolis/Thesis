package ballot.process;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
/**
 * 
 * @author olis_
 *
 */
public abstract class ImageProcess {
	
	/**
	 * 
	 * @param img is the grayscale input image
	 * @return returns the binarized image (fully b&w instead of grayscale)
	 */
	public Mat threshold(Mat img) {
		
		Mat binarized = new Mat();
		Imgproc.threshold(img, binarized, 200, 255, Imgproc.THRESH_BINARY);
		
		return binarized;
	}
	
	/**
	 * 
	 * @param binarized is the binarized image to get contours from
	 * @param contours the array that hold the contour values of the binarized image
	 * @return the arraylist of contours
	 */
	public List<MatOfPoint> findContours(Mat binarized){
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(binarized, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		return contours;
	}

	/**
	 * 
	 * @param img is the grayscale (CV_8UC1) image source to be converted to rgb (CV_8UC3)
	 * @return returns the converted image (BGR format)
	 */
	public Mat toRGB(Mat img) {
		
		Mat rgb = new Mat();
		
		//Resetting the image format to RBG from Grayscale before showing the detected rectangles
        Imgproc.cvtColor(img, rgb, Imgproc.COLOR_GRAY2BGR);
        
		return rgb;
	}

	/**
	 * 
	 * @param img is the original image with the contours identified
	 * @param rect is the second largest contour 
	 * @return returns an area of the ballot given a specific Rect dimension of a given Mat image
	 */
	public Mat cropImage(Mat img, Rect rect) {
		
		//System.out.println("Voting Area" + votingArea);
		Mat cropped = new Mat(img, rect);
		
		return cropped;
		
	}
	
	/**
	 * 
	 * @param mat Mat image that is to be converted to BufferredImage
	 * @return returns a BufferredImage object from a Mat object to display to the Frame
	 */
	public static BufferedImage createAwtImage(Mat mat) {

	    int type = 0;
	    if (mat.channels() == 1) {
	        type = BufferedImage.TYPE_BYTE_GRAY;
	    } else if (mat.channels() == 3) {
	        type = BufferedImage.TYPE_3BYTE_BGR;
	    } else {
	        return null;
	    }

	    BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
	    WritableRaster raster = image.getRaster();
	    DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
	    byte[] data = dataBuffer.getData();
	    mat.get(0, 0, data);

	    return image;
	    
	    /*// Load image using Highgui or create Mat object other way you want
	     * https://stackoverflow.com/questions/22284823/opencv-output-using-mat-object-in-jpanel
	    Mat mat = Highgui.imread(".../img.jpg");

	    ImageIcon image = new ImageIcon(createAwtImage(mat));*/
	}
	
	/**
	 * 
	 * @param imgSize is the original size of the image 
	 * @param boundary is the dimension of the container that the image is to be put
	 * @return returns a new width and height of the picture with respect to the container
	 */
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_width = boundary.width;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;

	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height and width to maintain aspect ratio
	        new_height = bound_height;
	        new_width = (new_height * original_width) / original_height;
	    }

	    return new Dimension(new_width, new_height);
	}
	
	/**
	 * 
	 * @param img is the grayscale input image
	 * @return returns the blurred image
	 */
	public Mat gaussianBlur(Mat img) {
		
		Mat gaussianBlur = new Mat();
		Imgproc.GaussianBlur(img, gaussianBlur, new Size(3,3), 0, 0);
		
		return gaussianBlur;
	}
	
	public static double imgRatio(int newSize, int origSize) {

		double imgRatio;
		imgRatio = (double)(newSize) / (double)(origSize);
		
		return imgRatio;
	}

}