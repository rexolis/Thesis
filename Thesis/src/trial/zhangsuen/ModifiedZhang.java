package trial.zhangsuen;
 
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ballot.process.ImageProcess;
	 
	/**
	 * Created by nayef on 1/26/15.
	 */
	public class ModifiedZhang extends ImageProcess{
	    
		public static void main(String[] args) throws IOException {
		    
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	        Mat result,src; 
	        src = Imgcodecs.imread("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\tempNew\\candidate10.png", Imgcodecs.IMREAD_GRAYSCALE);
	        BufferedImage image = createAwtImage(src);
	        
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
	 
	        ModifiedZhang thinningService = new ModifiedZhang();
	     
	        thinningService.doZhangSuenThinning(imageData, true);
	        //doZhangSuenThinning(imageData, true);
	         
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
	        Imgcodecs.imwrite("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\otsuMod.png", result);
	 
	        ImageIO.write(image, "jpg", new File("C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\skeletonMod.png"));
	 
	    }
		
		/**
		 * 
	     * @param givenImage
	     * @param changeGivenImage decides whether the givenArray should be modified or a clone should be used
	     * @return a 2D array of binary image after thinning using zhang-suen thinning algo.
	     */
	    public int[][] doZhangSuenThinning(final int[][] givenImage, boolean changeGivenImage) {
	        int[][] binaryImage;
	        if (changeGivenImage) {
	            binaryImage = givenImage;
	        } else {
	            binaryImage = givenImage.clone();
	        }
	        int a, b, sumP,
	        	an2, an4, an6, an8,
	        	bn2, bn4, bn6, bn8,
	        	and3, and5, and7, and9,
	        	bnd3, bnd5, bnd7, bnd9;
	        List<Point> pointsToChange = new LinkedList<Point>();
	        List<Point> pointsToPreserve = new LinkedList<Point>();
	        boolean hasChange, retained;
	        do {
	            hasChange = retained = false;
	            //System.out.println(hasChange);
	            for (int y = 1; y + 2 < binaryImage.length; y++) {
	                for (int x = 1; x + 2 < binaryImage[y].length; x++) {

	                	//pixel p
	                    a = getA(binaryImage, y, x);
	                    b = getB(binaryImage, y, x);
	                    
	                    //adjacent neighbors of pixel p
	                    if(y != 1) {
		                    an2 = getA(binaryImage, y-1, x);
		                    bn2 = getB(binaryImage, y-1, x);
	                    }else { an2=bn2=0; }
		                if(x != 1) {
		                	an8 = getA(binaryImage, y, x-1);
		                    bn8 = getB(binaryImage, y, x-1);
		                }else {an8=bn8=0; }
	                    an4 = getA(binaryImage, y, x+1);
	                    bn4 = getB(binaryImage, y, x+1);
	                    an6 = getA(binaryImage, y+1, x);
	                    bn6 = getB(binaryImage, y+1, x);
	                    
	                    //diagonal neighbors of pixel p
	                    if (y != 1) {
		                    and3 = getA(binaryImage, y-1, x+1);
		                    bnd3 = getB(binaryImage, y-1, x+1);
	                    } else { and3=bnd3=0; }
	                    and5 = getA(binaryImage, y+1, x+1);
	                    bnd5 = getB(binaryImage, y+1, x+1);
	                    if(x != 1) {
		                    bnd7 = getB(binaryImage, y+1, x-1);
		                    and7 = getA(binaryImage, y+1, x-1);
	                    } else {bnd7=and7=0; }
	                    if(x != 1 && y != 1) {
		                    and9 = getA(binaryImage, y-1, x-1);
		                    bnd9 = getB(binaryImage, y-1, x-1);
	                    }else {and9=bnd9=0; }

	                    sumP = getSumP(binaryImage, y, x);
	                    
	                    //diagonal preservation
	                    //for p2
	                    if(binaryImage[y][x] == 1 && (b==2 && a==1) && ((bn2==3 && an2==2) && (bnd3==4 && and3==2)
	                    											|| (bn2==3 && an2==2) && (bnd9==4 && and9==2))) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        retained = true;
	                    	System.out.println("X1d2:  " + (x));
	                    }
	                    //for p4
	                    if(binaryImage[y][x] == 1 && (b==2 && a==1) && ((bn4==3 && an4==2) && (bnd3==4 && and3==2)
	                    											|| (bn4==3 && an4==2) && (bnd5==4 && and5==2))) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        retained = true;
	                    	System.out.println("X1d4:  " + (x));
	                    }
	                    //for p6
	                    if(binaryImage[y][x] == 1 && (b==2 && a==1) && ((bn6==3 && an6==2) && (bnd5==4 && and5==2)
	                    											|| (bn6==3 && an6==2) && (bnd7==4 && and7==2))) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        retained = true;
	                    	System.out.println("X1d6:  " + (x));
	                    }
	                    //for p8
	                    if(binaryImage[y][x] == 1 && (b==2 && a==1) && ((bn8==3 && an8==2) && (bnd7==4 && and7==2)
	                    											|| (bn8==3 && an8==2) && (bnd9==4 && and9==2))) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        retained = true;
	                    	System.out.println("X1d8:  " + (x));
	                    }
	                    //box preservation
	                    if(binaryImage[y][x] == 1 && b==3 && a==1 && binaryImage[y][x+1] == 1 && binaryImage[y+1][x] == 1 && sumP == 0) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        hasChange = true;
	                    	System.out.println("X1b:  " + (x));
	                    }
	                    if ((binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
	                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0)
	                            && (binaryImage[y][x + 1] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)
	                            && retained == false)
	                    		) {
	                        pointsToChange.add(new Point(x, y));
	                        hasChange = true;
	                       
		                    
	                    	System.out.println("X1z:  " + (x));
	                    }
	                    //System.out.println("X1: " + (x+2));
	                }
	                retained = false;
                    //System.out.println("Y1: " + (y));
	            }
	            for (Point point : pointsToChange) {
	                binaryImage[point.getY()][point.getX()] = 0;
	            }
	            for (Point point : pointsToPreserve) {
	                binaryImage[point.getY()][point.getX()] = 1;
	            }
	            pointsToChange.clear();
	            pointsToPreserve.clear();
	            for (int y = 1; y + 2 < binaryImage.length; y++) {
	                for (int x = 1; x + 2 < binaryImage[y].length; x++) {
	                	//pixel p
	                    a = getA(binaryImage, y, x);
	                    b = getB(binaryImage, y, x);
	                    
	                    //adjacent neighbors of pixel p
	                    if(y != 1) {
		                    an2 = getA(binaryImage, y-1, x);
		                    bn2 = getB(binaryImage, y-1, x);
	                    }else { an2=bn2=0; }
		                if(x != 1) {
		                	an8 = getA(binaryImage, y, x-1);
		                    bn8 = getB(binaryImage, y, x-1);
		                }else {an8=bn8=0; }
	                    an4 = getA(binaryImage, y, x+1);
	                    bn4 = getB(binaryImage, y, x+1);
	                    an6 = getA(binaryImage, y+1, x);
	                    bn6 = getB(binaryImage, y+1, x);
	                    
	                    //diagonal neighbors of pixel p
	                    if (y != 1) {
		                    and3 = getA(binaryImage, y-1, x+1);
		                    bnd3 = getB(binaryImage, y-1, x+1);
	                    } else { and3=bnd3=0; }
	                    and5 = getA(binaryImage, y+1, x+1);
	                    bnd5 = getB(binaryImage, y+1, x+1);
	                    if(x != 1) {
		                    bnd7 = getB(binaryImage, y+1, x-1);
		                    and7 = getA(binaryImage, y+1, x-1);
	                    } else {bnd7=and7=0; }
	                    if(x != 1 && y != 1) {
		                    and9 = getA(binaryImage, y-1, x-1);
		                    bnd9 = getB(binaryImage, y-1, x-1);
	                    }else {and9=bnd9=0; }

	                    sumP = getSumP(binaryImage, y, x);
	                  //diagonal preservation
	                    //for p2
	                    if(binaryImage[y][x] == 1 && (b==2 && a==1) && ((bn2==3 && an2==2) && (bnd3==4 && and3==2)
	                    											|| (bn2==3 && an2==2) && (bnd9==4 && and9==2))) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        retained = true;
	                    	System.out.println("X2d2:  " + (x));
	                    }
	                    //for p4
	                    if(binaryImage[y][x] == 1 && (b==2 && a==1) && ((bn4==3 && an4==2) && (bnd3==4 && and3==2)
	                    											|| (bn4==3 && an4==2) && (bnd5==4 && and5==2))) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        retained = true;
	                    	System.out.println("X2d4:  " + (x));
	                    }
	                    //for p6
	                    if(binaryImage[y][x] == 1 && (b==2 && a==1) && ((bn6==3 && an6==2) && (bnd5==4 && and5==2)
	                    											|| (bn6==3 && an6==2) && (bnd7==4 && and7==2))) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        retained = true;
	                    	System.out.println("X2d6:  " + (x));
	                    }
	                    //for p8
	                    if(binaryImage[y][x] == 1 && (b==2 && a==1) && ((bn8==3 && an8==2) && (bnd7==4 && and7==2)
	                    											|| (bn8==3 && an8==2) && (bnd9==4 && and9==2))) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        retained = true;
	                    	System.out.println("X2d8:  " + (x));
	                    }
	                    //box preservation
	                    if(binaryImage[y][x] == 1 && b==3 && a==1 && binaryImage[y][x+1] == 1 && binaryImage[y+1][x] == 1 && sumP == 0) {
	                    	pointsToPreserve.add(new Point(x, y));
	                        hasChange = true;
	                    	System.out.println("X2b:  " + (x));
	                    }

	                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
	                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0)
	                            && (binaryImage[y - 1][x] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)
	                            && retained == false) {
	                        pointsToChange.add(new Point(x, y));
	                        hasChange = true;

	                    	System.out.println("X2z:  " + (x));
	                    }
	                }
	                retained = false;
                    //System.out.println("Y2: " + (y));
	            }
	            for (Point point : pointsToChange) {
	                binaryImage[point.getY()][point.getX()] = 0;
	            }
	            for (Point point : pointsToPreserve) {
	                binaryImage[point.getY()][point.getX()] = 1;
	            }
	            pointsToChange.clear();
	            pointsToPreserve.clear();
	        } while (hasChange);
	   
	        
	        //System.out.println(binaryImage.length);
	        //redundant pixel removal
	        do {
	            hasChange = retained = false;
	            for (int y = 1; y + 1 < binaryImage.length; y++) {
	                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
	                	
	                	//System.out.println((x+1) + " < " + binaryImage[y].length);
	                	if(/*template a*/ ((binaryImage[y][x] == 1 && binaryImage[y][x+1] == 0 && binaryImage[y+1][x] == 0 && binaryImage[y+1][x+1] == 0 && binaryImage[y-1][x] == 1  && binaryImage[y][x-1] == 1) || 
	                						(binaryImage[y][x] == 1 && binaryImage[y][x-1] ==  0 && binaryImage[y+1][x] == 0 && binaryImage[y+1][x-1] == 0 && binaryImage[y-1][x] == 1  && binaryImage[y][x+1] == 1)) ||
	                		/*template b*/((binaryImage[y][x] == 1 && binaryImage[y-1][x] == 0 && binaryImage[y][x+1] == 0 && binaryImage[y-1][x+1] == 0 && binaryImage[y][x-1] == 1  && binaryImage[y+1][x] == 1) ||
	                						(binaryImage[y][x] == 1 && binaryImage[y-1][x] == 0 && binaryImage[y][x-1] ==0 && binaryImage[y+1][x-1] == 0 && binaryImage[y][x+1] == 1  && binaryImage[y+1][x] == 1 ))
	                	
	                	) {
	                		//remove pixel
	                        pointsToChange.add(new Point(x, y));
	                		hasChange = true;
		                	System.out.println("XR1: " + x);
	                	}
	                	else if(/*template a*/ (((binaryImage[y][x] == 1 && (binaryImage[y][x+1] + binaryImage[y+1][x] == 1) && 
	                							(binaryImage[y-1][x+1] == 1 || binaryImage[y+1][x-1] == 1))) && binaryImage[y+1][x+1] == 0 &&
	                							(binaryImage[y-1][x] == 1  && binaryImage[y][x-1] == 1)) ||
	                			 
	                							(((binaryImage[y][x] == 1 && (binaryImage[y][x-1] + binaryImage[y+1][x] == 1) && 
	                							(binaryImage[y+1][x-1] == 1 || binaryImage[y+1][x+1] == 1))) && binaryImage[y+1][x-11] == 0 ) &&
	                							(binaryImage[y-1][x] == 1  && binaryImage[y][x+1] == 1) ||
	                							   
	                			/*template b*/ (((binaryImage[y][x] == 1 && (binaryImage[y-1][x] + binaryImage[y][x+1] == 1) &&
	                							(binaryImage[y+1][x-1] ==  1 || binaryImage[y+1][x+1] == 1))) && binaryImage[y-1][x+1] == 0 ) &&
	                							(binaryImage[y][x-1] == 1  && binaryImage[y+1][x] == 1) ||
	                							
	                							(((binaryImage[y][x] == 1 && (binaryImage[y-1][x] + binaryImage[y][x-1] == 1) &&
	                							(binaryImage[y-1][x+1] == 1 || binaryImage[y+1][x-1] == 1)))  && binaryImage[y-1][x-1] == 0 ) &&
	                							(binaryImage[y][x+1] == 1  && binaryImage[y+1][x] == 1)
	                	) {
	                         
	                		pointsToChange.add(new Point(x, y));
	                		hasChange = true;
		                	System.out.println("XR2: " + x + " y: " + y);
	                	} 

	    	            for (Point point : pointsToChange) {
	    	                binaryImage[point.getY()][point.getX()] = 0;
	    	            }

	    	            pointsToChange.clear();
	                
	                }          
	            }
	        }while(hasChange);
	        return binaryImage;
	    }

	    private int getA(int[][] binaryImage, int y, int x) {
	        int count = 0;
	//p2 p3
	        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && /*p2*/binaryImage[y - 1][x] == 0 && /*p3*/binaryImage[y - 1][x + 1] == 1) {
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
	 
	    private int getB(int[][] binaryImage, int y, int x) {
	        return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + binaryImage[y][x + 1]
	                + binaryImage[y + 1][x + 1] + binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
	                + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
	    }
	    
	    private int getSumP(int[][] binaryImage, int y, int x) {
	    	return binaryImage[y-1][x+2] + binaryImage[y][x+2] + binaryImage[y+1][x+2] + binaryImage[y+2][x+2] + 
	    			binaryImage[y+2][x+1] + binaryImage[y+2][x] + binaryImage[y+2][x-1];
	    }
	}
