package trial.hilditch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ballot.process.ImageProcess;

public class HilditchThinning extends ImageProcess {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat result; 
        
        BufferedImage image = ImageIO.read(new File("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\cropped1.png"));
        
        int[][] imageData = new int[image.getHeight()][image.getWidth()];
        
	     
        doHilditchsThinning(imageData);
        //doZhangSuenThinning(imageData, true);
         
        /*for (int y = 0; y < imageData.length; y++) {
 
            for (int x = 0; x < imageData[y].length; x++) {
 
                if (imageData[y][x] == 1) {
                    image.setRGB(x, y, Color.BLACK.getRGB());
 
                } else {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                }
 
 
            }
        }*/
       
        //System.out.println(image);
        result = createMatImage(image);
        //result = thresholdOtsu(result);
        Imgcodecs.imwrite("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\otsu.png", result);
 
        ImageIO.write(image, "jpg", new File("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\skeleton.png"));

	}

	/**
	 *
	 * @author nayef
	 */
  public static int[][] doHilditchsThinning(int[][] binaryImage) {
    int a, b;
    boolean hasChange;
    do {
        hasChange = false;
        for (int y = 1; y + 1 < binaryImage.length; y++) {
            for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                a = getA(binaryImage, y, x);
                b = getB(binaryImage, y, x);
                if (binaryImage[y][x]==1 && 2 <= b && b <= 6 && a == 1
                        && ((binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0) || (getA(binaryImage, y - 1, x) != 1))
                        && ((binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0) || (getA(binaryImage, y, x + 1) != 1)))
                {
                    binaryImage[y][x] = 0;
                    hasChange = true;
                }
            }
        }
    } while (hasChange);
  
    return binaryImage;
  }
  
	private static int getA(int[][] binaryImage, int y, int x) {
	  
	    int count = 0;
	    //p2 p3
	    if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
	        count++;
	    }
	    //p3 p4
	    if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
	        count++;
	    }
	    //p4 p5
	    if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
	        count++;
	    }
	    //p5 p6
	    if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
	        count++;
	    }
	    //p6 p7
	    if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
	        count++;
	    }
	    //p7 p8
	    if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
	        count++;
	    }
	    //p8 p9
	    if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
	        count++;
	    }
	    //p9 p2
	    if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
	        count++;
	    }
	  
	    return count;
	}
	  
	private static int getB(int[][] binaryImage, int y, int x) { 
		
		return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + 
				binaryImage[y][x + 1] + binaryImage[y + 1][x + 1] + 
				binaryImage[y + 1][x] + binaryImage[y + 1][x - 1] + 
				binaryImage[y][x - 1] + binaryImage[y - 1][x - 1]; 
	} 
	
	

}
