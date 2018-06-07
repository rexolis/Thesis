package ballot.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import org.opencv.core.Rect;

import ballot.process.ExtractRectangles;
import ballot.process.ExtractSelection;
import net.miginfocom.swing.MigLayout;

public class ShowLabel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y, x2, y2, prevY2; //coordinates for the JLabel
	//private int xp, yp, xp2, yp2; //coordinates for the JPanel
	private double widthRatio, heightRatio;
	private boolean downward;
	
	public Rect upscaledDim;
    JScrollPane sp;
	public JLabel lbl1;
	

	public ExtractRectangles er;
	public ExtractSelection es;
	
	public ShowLabel() {
		
		//this.er = new ExtractRectangles(new Mat());
		
		setLayout(new MigLayout());
		lbl1 = new JLabel();
		lbl1.setAutoscrolls(true);
        /*lbl1.setBorder(
            BorderFactory.createEtchedBorder()
        );*/
        
		this.setFocusable(true);
        this.add(lbl1);

        sp = new JScrollPane(lbl1);
//        sp = new JScrollPane(this);
        add(sp, "w 1000, h 670");

	}
	
	public JLabel getJLabel() {
		
		return lbl1;
	}
	
	public void setImgRatio(double width, double height) {
		
		widthRatio = width;
		heightRatio = height;
		//System.out.println("Image Ratio: " + widthRatio + ", " + heightRatio);
		
	}

	//LABEL
	
	public void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
        
        System.out.println("startPoint x: " + this.x + ", y: " + this.y);
    }

    public void setEndPoint(int x, int y) {
       
    	prevY2 = y2;
    	
    	x2 = (x);
        y2 = (y);
        
        if(prevY2 < y2) {
        	//do this
        	downward = true;
        	System.out.println("Downward");
        }else {
        	//upward
        	downward = false;
        	System.out.println("Upward");
        }
        
        System.out.println("endPoint x: " + x2 + ", y: " + y2);
    }


    public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw = Math.abs(x-x2);
        int ph = Math.abs(y-y2);
        g.drawRect(px, py, pw, ph);
        upscaledDim = new Rect(
        		(int)(px/widthRatio), 
        		(int)(py/heightRatio), 
        		(int)(pw/widthRatio), 
        		(int)(ph/heightRatio)
        		);
        //System.out.println("Rectangle: " + Math.min(x,x2) + ", " + Math.min(y,y2) + ", " + pw + ", " + ph);
        
    }
    
    public void setScaledSelection() {

    	er.setScaledSelection(upscaledDim);
    	es.setRect(upscaledDim);
    	//System.out.println("Can i access a mat instance from another class? " + er.src);
    }
    
    public void setERInstance(ExtractRectangles er) {
    	
    	this.er = er;
    	
    	 //create mouse listener
        x = y = x2 = y2 = prevY2 = 0; //initialize var for drawing
      	MyMouseListener listenerLabel = new MyMouseListener();
      	lbl1.addMouseListener(listenerLabel);
      	lbl1.addMouseMotionListener(listenerLabel);
    }

	public void setESInstance(ExtractSelection es) {
    	this.es = es;
	}

	@Override
    public void paint(Graphics g) {

        super.paint(g);
        g.setColor(Color.RED);
        drawPerfectRect(g, x, y, x2, y2);
        //System.out.println("FUCK YOU");
    }
    
    //LABEL
    
    class MyMouseListener extends MouseAdapter {
    	
    	Rectangle visible;
    	JViewport viewPort;
    	
        public void mousePressed(MouseEvent e) {
            setStartPoint(e.getX(), e.getY());
        }

        public void mouseDragged(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            
            Rectangle visible = getJLabel().getVisibleRect();
            //TODO get current location
            //if(mouse location gets nearer to the boundary, height)
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, getJLabel());
            System.out.println("Current visible view:  " + visible);

        	Rectangle view = viewPort.getViewRect();
        	System.out.println("Mouse position: " + e.getPoint().y);
        	if(e.getPoint().y > (view.height - 30) && downward) {
            	//determine if the mouse was dragged downwards or upwards
            	
            	view.x = 0;
            	view.y += 10;
            	
            	getJLabel().scrollRectToVisible(view);
            	
            }else if(((e.getPoint().y - view.y) < 30) && !downward) {
            	view.x = 0;
            	view.y = Math.abs(view.y - 10);
            	
            	getJLabel().scrollRectToVisible(view);
            	
            }
            
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            System.out.println("Rectangle: " + Math.min(x,x2) + ", " + Math.min(y,y2) + ", " + Math.abs(x-x2) + ", " + Math.abs(y-y2));
            setScaledSelection();
            
            repaint();
        }
        
        public void mouseMoved(MouseEvent e) {
        	
        }
    }
   

}
