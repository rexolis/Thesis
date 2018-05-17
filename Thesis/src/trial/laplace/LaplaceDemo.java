package trial.laplace;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
class LaplaceDemoRun {
    public void run(String[] args) {
        // Declare the variables we are going to use
        Mat src, src_gray = new Mat(), dst = new Mat();
        int kernel_size = 3;
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;
        String window_name = "Laplace Demo";
        String imageName = ((args.length > 0) ? args[0] : "images/ph2016.jpg");
        src = Imgcodecs.imread(imageName, Imgcodecs.IMREAD_COLOR); // Load an image
        
        //copy original image
        //Mat src_copy = new Mat();
        //src_copy = src.clone();
        
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image");
            System.out.println("Program Arguments: [image_name -- default ../data/lena.jpg] \n");
            System.exit(-1);
        }
        // Reduce noise by blurring with a Gaussian filter ( kernel size = 3 )
        Imgproc.GaussianBlur( src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT );
        // Convert the image to grayscale
        Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_RGB2GRAY );
        Mat abs_dst = new Mat();
        Imgproc.Laplacian( src_gray, dst, ddepth, kernel_size, scale, delta, Core.BORDER_DEFAULT );
        // converting back to CV_8U
        Core.convertScaleAbs( dst, abs_dst );
        
        /*//CANNY EDGE
        Mat gray = new Mat();
        Imgproc.Canny(abs_dst, gray, 20, 100, 3, true);
        
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/8, // change this value to detect circles with different distances to each other
                200.0, 30.0, 0, 30); // change the last two parameters
                // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src_copy, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src_copy, center, radius, new Scalar(255,0,255), 3, 8, 0 );
        }*/
        
        
        HighGui.imshow( window_name, abs_dst);
        HighGui.waitKey(0);
        System.exit(0);
    }
}
public class LaplaceDemo {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new LaplaceDemoRun().run(args);
    }
}
