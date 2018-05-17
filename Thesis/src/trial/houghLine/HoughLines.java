package trial.houghLine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.ImageWindow;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


class HoughLinesRun {
    public void run(String[] args) {
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP, srcCopy;
        String default_file = "images/test01.png";
        String filename = ((args.length > 0) ? args[0] : default_file);
        // Load an image
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }
        
        File file = new File("text/file.txt");
        FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PrintWriter pw = new PrintWriter(fw);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        
        //Binarization
        Mat fullBlack = new Mat();
        Imgproc.threshold(src, fullBlack, 200, 255, Imgproc.THRESH_BINARY);
        
        //Finding contours
        Imgproc.findContours(fullBlack, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        
        double currLargest=0, largest=0, secondLargest=0;
        Rect votingArea=null, document=null;
        srcCopy = src.clone();

        Imgproc.cvtColor(src, srcCopy, Imgproc.COLOR_GRAY2BGR);
        
        for(int i=0; i< contours.size();i++){
	        System.out.println("Contour Area: " + Imgproc.contourArea(contours.get(i)));
	        if (Imgproc.contourArea(contours.get(i)) > 4000 ){
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            //System.out.println(rect.height);
	            currLargest = Imgproc.contourArea(contours.get(i));
	            if (rect.height > 28){
		            //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
		            pw.println(i + ". " + rect.x +","+rect.y+","+rect.height+","+rect.width + " [Contour Area: " + Imgproc.contourArea(contours.get(i)) + "]");
		            Imgproc.rectangle(srcCopy, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255), 1, Imgproc.LINE_AA, 0);
	            }
	            if(currLargest > largest) {
	            	secondLargest = largest;
	            	votingArea = document;
	            	
	            	largest = currLargest;
	            	document = rect;
	            }
	        }
	    }
        System.out.println(votingArea.x +","+votingArea.y+","+votingArea.height+","+votingArea.width + " [Contour Area: " + secondLargest + "]");
	    //Imgcodecs.imwrite("images/rectangleNew1Final.png",srcCopy);
        
        // Edge detection
        Imgproc.Canny(src, dst, 50, 200, 3, false);
        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        
        
        cdstP = cdst.clone();
        //cdstP = fullBlack.clone();

        //HighGui.imshow("Full Black", srcCopy);
        
        /*// Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 1, Imgproc.LINE_AA, 0);
        }*/
        
        
        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        //Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 5); // runs the actual detection
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 200, 50, 3); // runs the actual detection
        // Draw the lines
        System.out.println("Number of rows = " + linesP.rows());
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA, 0);
            //System.out.println(x + ". " + "Points: " +  new Point(l[0], l[1]) + " " + new Point(l[2], l[3]));
            //pw.println(x + ". " + "Points: " +  new Point(l[0], l[1]) + " " + new Point(l[2], l[3]));
            
        }
        
        pw.close();
        
        Mat cropped = new Mat(srcCopy, votingArea);
        
        
        // Show results
        //HighGui.imshow("Source", src);
        HighGui.namedWindow("Probabilistic Line Transform", ImageWindow.WINDOW_NORMAL);
        HighGui.resizeWindow("Probabilistic Line Transform", 600, 600);
        //Imgcodecs.imwrite("images/lines.png", cdstP);
        //Imgcodecs.imwrite("images/rectangleNew1Final.png",srcCopy);
        HighGui.imshow("Probabilistic Line Transform", cropped);
        // Wait and Exit
        HighGui.waitKey();
        System.exit(0);
    }
}
public class HoughLines {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new HoughLinesRun().run(args);
    }
}