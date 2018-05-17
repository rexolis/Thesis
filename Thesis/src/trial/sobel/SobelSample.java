package trial.sobel;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class SobelDemoRun {
	
    public void run(String[] args) {
        // First we declare the variables we are going to use
    	String default_file = "images/ph2016.jpg";
        Mat src, src_gray = new Mat();
        Mat grad = new Mat();
        String window_name = "Sobel Demo - Simple Edge Detector";
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;
        // As usual we load our source image (src)
        // Check number of arguments
//        if (args.length == 0){
//            System.out.println("Not enough parameters!");
//            System.out.println("Program Arguments: [image_path]");
//            System.exit(-1);
//        }
        // Load the image
        src = Imgcodecs.imread(default_file);
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image: " + args[0]);
            System.exit(-1);
        }
        
        Mat src_copy = src;
        // Remove noise by blurring with a Gaussian filter ( kernel size = 3 )
        Imgproc.GaussianBlur( src_copy, src_copy, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT );
        // Convert the image to grayscale
       
        Imgproc.cvtColor( src_copy, src_gray, Imgproc.COLOR_RGB2GRAY );
        Mat grad_x = new Mat(), grad_y = new Mat();
        Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();
        //Imgproc.Scharr( src_gray, grad_x, ddepth, 1, 0, scale, delta, Core.BORDER_DEFAULT );
        Imgproc.Sobel( src_gray, grad_x, ddepth, 1, 0, 3, scale, delta, Core.BORDER_DEFAULT );
        //Imgproc.Scharr( src_gray, grad_y, ddepth, 0, 1, scale, delta, Core.BORDER_DEFAULT );
        Imgproc.Sobel( src_gray, grad_y, ddepth, 0, 1, 3, scale, delta, Core.BORDER_DEFAULT );
        // converting back to CV_8U
        Core.convertScaleAbs( grad_x, abs_grad_x );
        Core.convertScaleAbs( grad_y, abs_grad_y );
        Core.addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad );
        
        /*//CANNY EDGE
        Imgproc.Canny(grad, grad, 20, 100, 3, true);
        
        Mat circles = new Mat();
        Imgproc.HoughCircles(grad, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)grad.rows()/8, // change this value to detect circles with different distances to each other
                200.0, 30.0, 0, 30); // change the last two parameters
                // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
        }*/
        HighGui.imshow( window_name, grad );
        HighGui.waitKey(0);
        System.exit(0);
    }
}
public class SobelSample {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new SobelDemoRun().run(args);
    }
}