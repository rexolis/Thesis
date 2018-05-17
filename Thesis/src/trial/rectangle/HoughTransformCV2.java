package trial.rectangle;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.Imgproc;

public class HoughTransformCV2 {

    public static void main(String[] args) {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            Mat source = Imgcodecs.imread("images/ph2016.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
            Mat destination = new Mat(source.rows(), source.cols(), source.type());

            final List<MatOfPoint> points = new ArrayList<>();
            final Mat hierarchy = new Mat();
            

            Imgproc.cvtColor(source, destination, Imgproc.COLOR_RGB2GRAY);
            Imgproc.equalizeHist(destination, destination);
            Imgproc.GaussianBlur(destination, destination, new Size(5, 5), 0, 0, Core.BORDER_DEFAULT);

            //Imgproc.Canny(destination, destination, 50, 100);
            Imgproc.Canny(destination, destination, 100, 300);
            
            Imgproc.findContours(destination, points, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            
            //Imgproc.adaptiveThreshold(destination, destination, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 40);
            Imgproc.threshold(destination, destination, 0, 255, Imgproc.THRESH_BINARY);

            MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
            MatOfPoint2f approxCurve = new MatOfPoint2f();


            int i = 0;
            
            for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {
            	MatOfPoint contour = points.get(idx);
                Rect rect = Imgproc.boundingRect(contour);
                //double contourArea = Imgproc.contourArea(contour);
                matOfPoint2f.fromList(contour.toList());
                Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
                long total = approxCurve.total();
                if (total == 3) { // is triangle
                    // do things for triangle
                }
                if (total >= 4 && total <= 6) {
                    List<Double> cos = new ArrayList<>();
                    Point[] points1 = approxCurve.toArray();
                    for (int j = 2; j < total + 1; j++) {
                        cos.add(angle(points1[(int) (j % total)], points1[j - 2], points1[j - 1]));
                    }
                    Collections.sort(cos);
                    Double minCos = cos.get(0);
                    Double maxCos = cos.get(cos.size() - 1);
                    boolean isRect = total == 4 && minCos >= -0.1 && maxCos <= 0.3;
                    boolean isPolygon = (total == 5 && minCos >= -0.34 && maxCos <= -0.27) || (total == 6 && minCos >= -0.55 && maxCos <= -0.45);
                    if (isRect) {
                        double ratio = Math.abs(1 - (double) rect.width / rect.height);
                        drawText(rect.tl(), ratio <= 0.02 ? "SQU" : "RECT", source);
                        System.out.println(i++ + " RECTANGLE POTAAAAA");
                    }
                    if (isPolygon) {
                        drawText(rect.tl(), "Polygon", source);
                        System.out.println("ibaaaaa POTAAAAA");
                    }
                }
            }
            
            /*if (destination != null) {
                Mat lines = new Mat();
                Imgproc.HoughLinesP(destination, lines, 1, Math.PI / 180, 50, 30, 10);
                Mat houghLines = new Mat();
                houghLines.create(destination.rows(), destination.cols(), CvType.CV_8UC1);

                //Drawing lines on the image
                for (int i = 0; i < lines.cols(); i++) {
                    double[] points1 = lines.get(0, i);
                    double x1, y1, x2, y2;
                    x1 = points1[0];
                    y1 = points1[1];
                    x2 = points1[2];
                    y2 = points1[3];

                    Point pt1 = new Point(x1, y1);
                    Point pt2 = new Point(x2, y2);

                    //Drawing lines on an image
                    Imgproc.line(source, pt1, pt2, new Scalar(0, 0, 255), 4);
                }

            }*/

            //Imgcodecs.imwrite("rectangle_houghtransform.jpg", source);

    		HighGui.imshow("a rectangle hehe", source);
            // Wait and Exit
            HighGui.waitKey();
            System.exit(0);

        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }
    
    private static double angle(Point p1, Point p2, Point p0) {
		double dx1 = p1.x - p0.x;
		double dy1 = p1.y - p0.y;
		double dx2 = p2.x - p0.x;
		double dy2 = p2.y - p0.y;
		return (dx1 * dx2 + dy1 * dy2)
	    / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
	      + 1e-10);
	}
    private static void drawText(Point ofs, String text, Mat source) {
        Imgproc.putText(source, text, ofs, Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255,255,25));
    }
}
