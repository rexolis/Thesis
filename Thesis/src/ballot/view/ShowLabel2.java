package ballot.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import ballot.process.ExtractRectangles;
import ballot.process.ExtractSelection;
import net.miginfocom.swing.MigLayout;

public class ShowLabel2 {

	private double widthRatio, heightRatio;
	private List<Point> points = new ArrayList<Point>();
	//private int x, y, w, h; //x,y start | w,h dimension
	
	public Rect upscaledDim;
    JScrollPane sp;
	public JLabel lbl1;
	public JPanel thisPanel = new JPanel();
	

	public ExtractRectangles er;
	public ExtractSelection es;
	
	
	
	public ShowLabel2() {
		
		thisPanel.setLayout(new MigLayout());
		lbl1 = new JLabel();
		lbl1.setAutoscrolls(true);
		
        /*lbl1.setBorder(
            BorderFactory.createEtchedBorder()
        );*/
        
		thisPanel.setFocusable(true);
        thisPanel.add(lbl1);
        //this.setSize(lbl1.getHeight(), lbl1.getWidth());

        sp = new JScrollPane(lbl1);
        /*sp.getViewport().addChangeListener(new ChangeListener() {
        	
        	@Override
            public void stateChanged(ChangeEvent e) {
                //System.err.println("Change in " + e.getSource());
               // System.err.println("Vertical visible? " + sp.getVerticalScrollBar().isVisible());
               // System.err.println("Horizontal visible? " + sp.getHorizontalScrollBar().isVisible());
                ////System.err.println("Horizontal y? " + sp.getHorizontalScrollBar().get);
                if(downward) {

                	//yS--;
                	repaint();
                	
                }else if(!downward) {

                	//yS++;
                	repaint();
                	
                }
        		
            }
        	
        });*/
        thisPanel.add(sp, "w 1000, h 670");
		
	}
	
	public void addListeners() {

		MyMouseListener listenerLabel = new MyMouseListener();
      	lbl1.addMouseListener(listenerLabel);
      	lbl1.addMouseMotionListener(listenerLabel);
	}
	
	public JLabel getJLabel() {
		
		return lbl1;
	}
	
	public void setImgRatio(double width, double height) {
		
		widthRatio = width;
		heightRatio = height;
		//System.out.println("Image Ratio: " + widthRatio + ", " + heightRatio);
		
	}
	

    public void setScaledSelection() {
    	//er.setScaledSelection(upscaledDim);
    	es.setRect(upscaledDim);
    }

    public void setESInstance(ExtractSelection es) {
    	this.es = es;
	}
	
	public void setERInstance(ExtractRectangles er) {
    	
    	this.er = er;
    	 //create mouse listener
        //x = y = x2 = y2 = prevY2 = 0; //initialize var for drawing
        //xS = yS = xD = yD = 0; //initialize var for drawing
      	
    }
	
	public void setPoints(Point point) {
		System.out.println("Point: " + point);
		points.add(point);
		
		if(points.size() == 4) {
			System.out.println("four points");
			getRect();
		}
	}
	
	public void getRect() {
		int x,y,w,h, sum, currSum=0,
				sum1, currSum1=0;
		Point start, end;

		//assume the start/end point is the first item in the list
		start = points.get(0);
		end = points.get(0);
		
		
		/* the start point always has the smallest sum
		 * between x & y
		 * the end point always has the highest sum 
		 * between x & y
		 */
		sum = (int)points.get(0).x + (int)points.get(0).y;
		sum1 = (int)points.get(0).x + (int)points.get(0).y;
		
		for(int i=1; i<points.size(); i++) {
			
			currSum = (int)points.get(i).x + (int)points.get(i).y;
			if(currSum < sum) {
				start = points.get(i);
				System.out.println("Current start point: " + start);
				sum = currSum;
			}
			
			currSum1 = (int)points.get(i).x + (int)points.get(i).y;
			if(currSum1 > sum1) {
				end = points.get(i);
				System.out.println("Current end point: " + end);
				sum1 = currSum1;
			}
	
		}
		x = (int) start.x;
		y = (int) start.y;
		w = (int) Math.abs(x - end.x);
		h = (int) Math.abs(y - end.y);
		
		
		upscaledDim = new Rect(
	        		(int)(x/widthRatio), 
	        		(int)(y/heightRatio), 
	        		(int)(w/widthRatio), 
	        		(int)(h/heightRatio)
					);
		
		System.out.println("upsacled rect: " + upscaledDim);
		
		setScaledSelection();
		
	}
	class MyMouseListener extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			
		}
		public void mouseReleased(MouseEvent e) {
			setPoints(new Point(e.getX(), (int)e.getY()));
		}
		public void mouseDragged(MouseEvent e) {
			
		}
		public void mouseMoved(MouseEvent e) {
			
		}
	}
}
