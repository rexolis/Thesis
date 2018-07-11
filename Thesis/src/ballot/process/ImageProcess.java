package ballot.process;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
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
	public Mat thresholdBinary(Mat img) {
		
		Mat binarized = new Mat();
		Imgproc.threshold(img, binarized, 200, 255, Imgproc.THRESH_BINARY);
		
		return binarized;
	}
	
	/**
	 * 
	 * @param img is the grayscale input image
	 * @return returns the truncated image
	 */
	public Mat thresholdTruncate(Mat img) {
		
		Mat truncate = new Mat();
		Imgproc.threshold(img, truncate, 200, 255, Imgproc.THRESH_TRUNC);
		
		return truncate;
	}
	
	/**
	 * 
	 * @param img is the grayscale input image
	 * @return returns the blurred image
	 */
	public Mat gaussianBlur(Mat img) {
		
		Mat gaussianBlur = new Mat();
		Imgproc.GaussianBlur(img, gaussianBlur, new Size(3,3), 0, 0);
		//Imgproc.GaussianBlur(img, gaussianBlur, new Size(5,5), 0, 0);
		return gaussianBlur;
	}
	
	public Mat sharpen(Mat img, Mat blur) {
		
		//dest and img must have same size of rows,cols,&type
		//Mat dest = new Mat(img.rows(), img.cols(), img.type());
		Core.addWeighted(img, 0.8, blur, 0.6, 0, img); 
		
		return img;
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
	
	public static Mat createMatImg(BufferedImage in){
		Mat out;
		byte[] data;
		int r, g, b;
		if(in.getType() == BufferedImage.TYPE_INT_RGB)
		{
		    out = new Mat(240, 320, CvType.CV_8UC3);
		    data = new byte[320 * 240 * (int)out.elemSize()];
		    int[] dataBuff = in.getRGB(0, 0, 320, 240, null, 0, 320);
		    for(int i = 0; i < dataBuff.length; i++)
		    {
		        data[i*3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
		        data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
		        data[i*3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
		    }
		}
		else
		{
		    out = new Mat(240, 320, CvType.CV_8UC1);
		    data = new byte[320 * 240 * (int)out.elemSize()];
		    int[] dataBuff = in.getRGB(0, 0, 320, 240, null, 0, 320);
		    for(int i = 0; i < dataBuff.length; i++)
		    {
		      r = (byte) ((dataBuff[i] >> 16) & 0xFF);
		      g = (byte) ((dataBuff[i] >> 8) & 0xFF);
		      b = (byte) ((dataBuff[i] >> 0) & 0xFF);
		      data[i] = (byte)((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
		    }
		 }
		 out.put(0, 0, data);
		 return out;
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
	 * @param newSize new dimension of the image
	 * @param origSize original dimension of the image
	 * @return returns the aspect ratio of the image with respect to the component it is in
	 */
	public static double imgRatio(int newSize, int origSize) {

		double imgRatio;
		imgRatio = (double)(newSize) / (double)(origSize);
		
		return imgRatio;
	}
	
	public List<Mat> getNamesMat(List<MatOfPoint> contours, Mat src) {
		
		Mat rectangleName;
		List<Mat> names = new ArrayList<Mat>(); 
		
		for(int i = 0/*, j = 1*/; i < contours.size(); i++){
			if (Imgproc.contourArea(contours.get(i)) > 4000 ){

				Rect rect = Imgproc.boundingRect(contours.get(i));
				
				//prevents the largest contour to be included which is the whole picture
				if (rect.height > 28 && rect.x != 0 && rect.y != 0){
					//System.out.println(i+ ". Current rect: " + rect);
					rectangleName = cropImage(src, rect);
					names.add(rectangleName);
					//saveImgRect("temp/name" + j++ + ".png" );
					
				}
			}
		}
		return names;
		
		//extractText();
	}
	
	//traverse each files in the directory and add it to the List
    public static List<File> doListing(File dirName) {

    	List<File> files = new ArrayList<>();
    	
        File[] fileList = dirName.listFiles();
        
        for (File file : fileList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                files.add(file);
                doListing(file);
            }
        }
        return files;
    }
    
    public Mat deskew(Mat src, double angle){
		Point center = new Point(src.width()/2, src.height()/2);
		Mat rotImage = Imgproc.getRotationMatrix2D(center, angle, 1.0);
		//1.0 means 100 % scale
		Size size = new Size(src.width(), src.height());
		Imgproc.warpAffine(src, src, rotImage, size );//, Imgproc.INTER_LINEAR + Imgproc.CV_WARP_FILL_OUTLIERS);
		
		//new ShowResults().saveImage(src, "skewed/imgskewed.png");
		return src;
		
		
	}

}