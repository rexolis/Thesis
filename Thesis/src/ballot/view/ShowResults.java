package ballot.view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.ImageWindow;
import org.opencv.imgcodecs.Imgcodecs;

import ballot.process.ImageProcess;
import ballot.view.MainPanel;

public class ShowResults extends ImageProcess{
	
	public Dimension imgSize, newImgSize;
	public double widthRatio, heightRatio;
	
	public ShowResults() {}
	
	public void showWindow(Mat img, String filename) {
		
		// Show results
        HighGui.namedWindow("Results", ImageWindow.WINDOW_NORMAL);
        HighGui.resizeWindow("Results", 600, 670);
        Imgcodecs.imwrite("images/" + filename, img);
        HighGui.imshow("Results", img);
        // Wait and Exit
        HighGui.waitKey();
        System.exit(0);
        
	}
	
	public void showImage(BufferedImage img, MainPanel frame) {
		
		imgSize = new Dimension(img.getWidth(), img.getHeight());
		Dimension boundary = new Dimension(950, 2300);
		
		newImgSize = getScaledDimension(imgSize, boundary);
		
		widthRatio = imgRatio(newImgSize.width, imgSize.width);
		heightRatio = imgRatio(newImgSize.height, imgSize.height);
		
		frame.showLabel2.setImgRatio(widthRatio, heightRatio);
		
		Image scaledImg = img.getScaledInstance(newImgSize.width, newImgSize.height,
		        Image.SCALE_DEFAULT);
		frame.showLabel2.getJLabel().setIcon(new ImageIcon(scaledImg));
	}
	
	public void saveImage(Mat src, String filename) {

        Imgcodecs.imwrite("images/" + filename, src);
	}
	
	
}