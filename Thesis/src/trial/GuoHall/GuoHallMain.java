package trial.GuoHall;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import ballot.process.ImageProcess;
import trial.zhangsuen.Point;
import trial.zhangsuen.ThinningService;

public class GuoHallMain extends ImageProcess{

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat src, bw, result;
		BufferedImage image;
		
		src = Imgcodecs.imread("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\lorem.jpg", Imgcodecs.IMREAD_GRAYSCALE);
		image = createAwtImage(thresholdBinaryInv(src));
		
		if (src.empty())
			System.exit(-1);

	    int[][] imageData = new int[image.getHeight()][image.getWidth()];
        //Color c;
        for (int y = 0; y < imageData.length; y++) {
            for (int x = 0; x < imageData[y].length; x++) {
 
                if (image.getRGB(x, y) == Color.BLACK.getRGB()) {
                    imageData[y][x] = 1;
                } else {
                    imageData[y][x] = 0;
 
                }
            }
        }
 

	    thinningGuoHall(imageData);
	    
        for (int y = 0; y < imageData.length; y++) {
 
            for (int x = 0; x < imageData[y].length; x++) {
 
                if (imageData[y][x] == 1) {
                    image.setRGB(x, y, Color.BLACK.getRGB());
 
                } else {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                }
 
 
            }
        }
       
        System.out.println(image);
        result = createMatImage(image);
        //result = thresholdOtsu(result);
        Imgcodecs.imwrite("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\otsu.png", result);
 
        ImageIO.write(image, "jpg", new File("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\skeleton.png"));
 
	
	}

	private static int[][] thinningGuoHall(final int[][] givenImage) {
		int[][] binaryImage;
        binaryImage = givenImage;
        
        int c, n, n1, n2, p6, p7, p8, p9;
        List<Point> pointsToChange = new LinkedList<Point>();
        boolean hasChange;
        do {
            hasChange = false;
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    c = getC(binaryImage, y, x);
                    n1 = getN1(binaryImage, y, x);
                    n2 = getN2(binaryImage, y, x);
                    n = n1 < n2 ? n1 : n2;
                    

                    p6 = binaryImage[y + 1][x];
                    p7 = binaryImage[y + 1][x - 1];
                    p8 = binaryImage[y][x - 1];
                    p9 = binaryImage[y - 1][x - 1];
                    
                    if(p9 == 0) 
                    	p9 = 1;
                    else
                    	p9 = 0;
                    // m = 0 = ((p6 | p7 | !p9) & p8)

                    //if (C == 1 && (N >= 2 && N <= 3) & m == 0)
    	            //    marker.set(i,j) = 1;
                    if(binaryImage[y][x] == 1 && (n >= 2 && n <= 3) 
                    		&& ((p6 == 1 || p7 == 1 || p9 == 1) && p8 == 1)
                    		&& c == 1) {
                    	 pointsToChange.add(new Point(x, y));
                         hasChange = true;
                    }
                }
            }
            for (Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
        } while (hasChange);
        return binaryImage;
		
		
		/*// TODO Auto-generated method stub
		Mat marker;
		int c, n1, n2;
		
		marker = Mat.zeros(im.size(), CvType.CV_8UC1);
		
		//cv::Mat marker = cv::Mat::zeros(im.size(), CV_8UC1); 

	    for (int y = 1; y < im.rows(); y++)
	    {
	        for (int x = 1; x < im.cols(); x++)
	        {
	            c = getC(im, y, x);
	        	
	        	double[] p2 = im.get(i-1, j);
	            double[] p3 = im.get(i-1, j+1);
	            double[] p4 = im.get(i, j+1);
	            double[] p5 = im.get(i+1, j+1);
	            double[] p6 = im.get(i+1, j);
	            double[] p7 = im.get(i+1, j-1);
	            double[] p8 = im.get(i, j-1); 
	            double[] p9 = im.get(i-1, j-1);

	            int C  = (!p2 & (p3 | p4)) + (!p4 & (p5 | p6)) +
	                     (!p6 & (p7 | p8)) + (!p8 & (p9 | p2));
	            int N1 = (p9 | p2) + (p3 | p4) + (p5 | p6) + (p7 | p8);
	            int N2 = (p2 | p3) + (p4 | p5) + (p6 | p7) + (p8 | p9);
	            int N  = N1 < N2 ? N1 : N2;
	            int m  = iter == 0 ? ((p6 | p7 | !p9) & p8) : ((p2 | p3 | !p5) & p4);

	            if (C == 1 && (N >= 2 && N <= 3) & m == 0)
	                marker.set(i,j) = 1;
	        }
	    }

	    Core.bitwise_not(marker, marker);
	    Core.bitwise_and(im, marker, im);
	    //im &= ~marker;
*/	}
	
	private static int getC(int[][] binaryImage, int y, int x) {
		int count = 0;
		int p2, p4, p6, p8;
		p2=p4=p6=p8=0;
		//!p2
		if(binaryImage[y - 1][x] == 0) {
			p2 = 1;
		}
		//!p4
		if(binaryImage[y][x + 1] == 0) {
			p2 = 1;
		}
		//!p6
		if(binaryImage[y + 1][x] == 0) {
			p2 = 1;
		}
		//!p8
		if(binaryImage[y][x - 1] == 0) {
			p2 = 1;
		}
		
		/*int C  = (!p2 & (p3 | p4)) + (!p4 & (p5 | p6)) +
        (!p6 & (p7 | p8)) + (!p8 & (p9 | p2));*/
		
		//(!p2 & (p3 | p4))
		if(p2 == 1 && (binaryImage[y - 1][x + 1] == 1 || binaryImage[y][x + 1] == 1)) {
			count++;
		}
		//(!p4 & (p5 | p6))
		if(p4 == 1 && (binaryImage[y + 1][x + 1] == 1 || binaryImage[y + 1][x] == 1)) {
			count++;
		}
		//(!p6 & (p7 | p8))
		if(p6 == 1 && (binaryImage[y + 1][x - 1] == 1 || binaryImage[y][x - 1] == 1)) {
			count++;
		}
		//(!p8 & (p9 | p2))
		if(p8 == 1 && (binaryImage[y - 1][x - 1] == 1 || binaryImage[y - 1][x] == 1)) {
			count++;
		}
	
		return count;
	}

	private static int getN1(int[][] binaryImage, int y, int x) {
		int count = 0;
		// int N1 = (p9 | p2) + (p3 | p4) + (p5 | p6) + (p7 | p8);
		

		//(p9 | p2)
		if(y - 1 >= 0 || x - 1 >= 0 || binaryImage[y - 1][x - 1] == 0 || binaryImage[y - 1][x - 1] == 1 || binaryImage[y - 1][x] == 1) {
			count++;
		}
		//(p3 | p4)
		if(y - 1 >= 0 || x + 1 < binaryImage[y].length || binaryImage[y - 1][x + 1] == 1 || binaryImage[y][x + 1] == 1) {
			count++;
		}
		//(p5 | p6)
		if(y + 1 < binaryImage.length || x + 1 < binaryImage[y].length || binaryImage[y + 1][x + 1] == 1 || binaryImage[y + 1][x] == 1) {
			count++;
		}
		//(p7 | p8)
		if(y + 1 < binaryImage.length || x - 1 >= 0 || binaryImage[y + 1][x - 1] == 1 || binaryImage[y][x - 1] == 1) {
			count++;
		}

		return count;
	}
	private static int getN2(int[][] binaryImage, int y, int x) {
		int count = 0;
		//int N2 = (p2 | p3) + (p4 | p5) + (p6 | p7) + (p8 | p9);
		

		//(p2 | p3)
		if(y - 1 >= 0 || x + 1 < binaryImage[y].length || binaryImage[y - 1][x] == 1 || binaryImage[y - 1][x +1] == 1) {
			count++;
		}
		//(p4 | p5)
		if(y + 1 < binaryImage.length || x + 1 < binaryImage[y].length || binaryImage[y][x + 1] == 1 || binaryImage[y + 1][x + 1] == 1) {
			count++;
		}
		//(p6 | p7)
		if(y + 1 < binaryImage.length || x - 1 >= 0 || binaryImage[y + 1][x] == 1 || binaryImage[y + 1][x - 1] == 1) {
			count++;
		}
		//(p8 | p9)
		if(y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0 && binaryImage[y][x - 1] == 1 || binaryImage[y - 1][x - 1] == 1) {
			count++;
		}

		return count;
	}

}
