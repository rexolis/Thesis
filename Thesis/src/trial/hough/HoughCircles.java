package trial.hough;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


class HoughCirclesRun {
    public void run(String[] args) {
        String default_file = "images/ph2016.jpg";
        String filename = ((args.length > 0) ? args[0] : default_file);
        // Load an image
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }
        Mat gray = new Mat();
        
        //THRESHOLDING
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        //MEDIAN BLURR
        //Imgproc.medianBlur(gray, gray, 5);
        
        //GAUSSIAN BLURR
        Imgproc.GaussianBlur(gray, gray, new Size(5,5), 0, 0);
        
        /*//CANNY EDGE
        Imgproc.Canny(gray, gray, 30, 100, 3, true);*/
        
        //HOUGH CIRCLE
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/8, // change this value to detect circles with different distances to each other
                100.0, 35.0, 0, 30); // change the last two parameters
                // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // circle center
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
        }
        HighGui.imshow("canny 20 3x3 gaussian", src);
        HighGui.waitKey();
        System.exit(0);
    }
}
public class HoughCircles {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new HoughCirclesRun().run(args);
    }
}