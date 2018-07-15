package trial.zhangsuen;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ballot.process.ImageProcess;
 
public class ZhangSuen extends ImageProcess{
 
    final static String[] image = {
        "                                                          ",
        " #################                   #############        ",
        " ##################               ################        ",
        " ###################            ##################        ",
        " ########     #######          ###################        ",
        "   ######     #######         #######       ######        ",
        "   ######     #######        #######                      ",
        "   #################         #######                      ",
        "   ################          #######                      ",
        "   #################         #######                      ",
        "   ######     #######        #######                      ",
        "   ######     #######        #######                      ",
        "   ######     #######         #######       ######        ",
        " ########     #######          ###################        ",
        " ########     ####### ######    ################## ###### ",
        " ########     ####### ######      ################ ###### ",
        " ########     ####### ######         ############# ###### ",
        "                                                          "};
 
    final static int[][] nbrs = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1},
        {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};
 
    final static int[][][] nbrGroups = {{{0, 2, 4}, {2, 4, 6}}, {{0, 2, 6},
        {0, 4, 6}}};
 
    static List<Point> toWhite = new ArrayList<>();
    //static char[][] grid;
    static int[][] grid;
   
 
    public static void main(String[] args) {
        /*grid = new char[image.length][];
        for (int r = 0; r < image.length; r++) {
            grid[r] = image[r].toCharArray();

        }*/

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
        
    	Mat img;
    	BufferedImage src;
    	
    	String filename = "C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\tempNew\\candidate1.png";
    	img = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
        src = createAwtImage(img);
        
        grid = convertTo2DUsingGetRGB(src);
        
        System.out.println("Grid Matrix: " + Arrays.deepToString(grid));
        //thinImage();
    }
    
    private static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
           for (int col = 0; col < width; col++) {
              result[row][col] = image.getRGB(col, row);
           }
        }

        return result;
     }
    
    /*private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
           final int pixelLength = 4;
           for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
              int argb = 0;
              argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
              argb += ((int) pixels[pixel + 1] & 0xff); // blue
              argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
              argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
              result[row][col] = argb;
              col++;
              if (col == width) {
                 col = 0;
                 row++;
              }
           }
        } else {
           final int pixelLength = 3;
           for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
              int argb = 0;
              argb += -16777216; // 255 alpha
              argb += ((int) pixels[pixel] & 0xff); // blue
              argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
              argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
              result[row][col] = argb;
              col++;
              if (col == width) {
                 col = 0;
                 row++;
              }
           }
        }

        return result;
     }*/
 
    static void thinImage() {
        boolean firstStep = false;
        boolean hasChanged;
 
        do {
            hasChanged = false;
            firstStep = !firstStep;
 
            for (int r = 1; r < grid.length - 1; r++) {
                for (int c = 1; c < grid[0].length - 1; c++) {
 
                    if (grid[r][c] != 1)
                        continue;
 
                    int nn = numNeighbors(r, c);
                    if (nn < 2 || nn > 6)
                        continue;
 
                    if (numTransitions(r, c) != 1)
                        continue;
 
                    if (!atLeastOneIsWhite(r, c, firstStep ? 0 : 1))
                        continue;
 
                    toWhite.add(new Point(c, r));
                    hasChanged = true;
                }
            }
 
            for (Point p : toWhite)
                grid[p.y][p.x] = 0;
            toWhite.clear();
 
        } while (firstStep || hasChanged);
 
        printResult();
    }
 
    static int numNeighbors(int r, int c) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grid[r + nbrs[i][1]][c + nbrs[i][0]] == 1)
                count++;
        return count;
    }
 
    static int numTransitions(int r, int c) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grid[r + nbrs[i][1]][c + nbrs[i][0]] == 0) {
                if (grid[r + nbrs[i + 1][1]][c + nbrs[i + 1][0]] == 1)
                    count++;
            }
        return count;
    }
 
    static boolean atLeastOneIsWhite(int r, int c, int step) {
        int count = 0;
        int[][] group = nbrGroups[step];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < group[i].length; j++) {
                int[] nbr = nbrs[group[i][j]];
                if (grid[r + nbr[1]][c + nbr[0]] == 0) {
                    count++;
                    break;
                }
            }
        return count > 1;
    }
 
    static void printResult() {
        for (int[] row : grid)
            System.out.println(row);
        

        //Imgcodecs.imwrite("images/" + filename, img);
    }
}
