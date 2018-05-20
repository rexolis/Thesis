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
import ballot.view.MainFrame;

public class ShowResults extends ImageProcess{
	
	public ShowResults() {
		
	}
	
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
	
	public void showImage(BufferedImage img, MainFrame frame, File filename) {

		Dimension imgSize = new Dimension(img.getWidth(), img.getHeight());
		Dimension boundary = new Dimension(950, 2300);
		
		Dimension newImgSize = getScaledDimension(imgSize, boundary);
		
		//System.out.println("Scaled image: " + newImgSize);
		
		Image scaledImg = img.getScaledInstance((int)newImgSize.getWidth(), (int)newImgSize.getHeight(),
		        Image.SCALE_DEFAULT);
		frame.lbl1.setIcon(new ImageIcon(scaledImg));
		
		
		
		/*Desktop d = Desktop.getDesktop();
		
		try {
			d.open(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
	
	}
	
	public void saveImage(Mat src, String filename) {

        Imgcodecs.imwrite("images/" + filename, src);
	}
}