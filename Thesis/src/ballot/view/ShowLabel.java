package ballot.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Rect;

import ballot.process.ExtractRectangles;
import ballot.process.ExtractSelection;
import net.miginfocom.swing.MigLayout;

/*
 * java class not in use anymore 
 * 
 * 
 */
public class ShowLabel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y, x2, y2, prevY2; //coordinates for the JLabel
	private int xS, yS, xD, yD; //coordinates for drawing only
	private double widthRatio, heightRatio;
	private boolean downward = false;
	
	public Rect upscaledDim;
    JScrollPane sp;
	public JLabel lbl1;
	

	public ExtractRectangles er;
	public ExtractSelection es;
	
	public ShowLabel() {
		
		//this.er = new ExtractRect angles(new Mat());
		
		setLayout(new MigLayout());
		lbl1 = new JLabel();
		lbl1.setAutoscrolls(true);
        /*lbl1.setBorder(
            BorderFactory.createEtchedBorder()
        );*/
        
		this.setFocusable(true);
        this.add(lbl1);
        //this.setSize(lbl1.getHeight(), lbl1.getWidth());

        sp = new JScrollPane(lbl1);
        sp.getViewport().addChangeListener(new ChangeListener() {
        	
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
        	
        });
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
	
	public void setStartPoint(int x, int y, int xS, int yS) {
        this.x = x;
        this.y = y;
        
        this.xS = xS;
        this.yS = yS;

        System.out.println("startPointLabel x: " + this.x + ", y: " + this.y);
        System.out.println("startPointPanel x: " + this.xS + ", y: " + this.yS);
    }

    public void setEndPoint(int x, int y, int xD, int yD) {
       
    	prevY2 = y2;
    	
    	x2 = (x);
        y2 = (y);
        
        this.xD = xD;
        this.yD = yD;
        
        if(prevY2 < y2) {
        	//do this
        	downward = true;
        	System.out.println("Downward");
        }else {
        	//upward
        	downward = false;
        	System.out.println("Upward");
        }

        System.out.println("endPointLabel x: " + x2 + ", y: " + y2);
        System.out.println("endPointPanel x: " + xD + ", y: " + yD);
    }


    public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2, int xS, int yS, int xD, int yD) {
        //for img coords
    	int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw = Math.abs(x-x2);
        int ph = Math.abs(y-y2);
        
        //for drawing
        int dx = Math.min(xS,xD);
        int dy = Math.min(yS,yD);
        int dw = Math.abs(xS-xD);
        int dh = Math.abs(yS-yD);
        
        g.drawRect(dx+8, dy+8, dw, dh);
        
        upscaledDim = new Rect(
        		(int)(px/widthRatio), 
        		(int)(py/heightRatio), 
        		(int)(pw/widthRatio), 
        		(int)(ph/heightRatio)
        		);
    }
    
    public void setScaledSelection() {
    	er.setScaledSelection(upscaledDim);
    	es.setRect(upscaledDim);
    }
    
    public void setERInstance(ExtractRectangles er) {
    	
    	this.er = er;
    	
    	 //create mouse listener
        x = y = x2 = y2 = prevY2 = 0; //initialize var for drawing
        xS = yS = xD = yD = 0; //initialize var for drawing
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
        drawPerfectRect(g, x, y, x2, y2, xS, yS, xD, yD);
        //System.out.println("FUCK YOU");
    }
    
    //LABEL
    
    class MyMouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
        	JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, getJLabel());
        	Rectangle view = viewPort.getViewRect();
            //getX, getY gets the location on the object with listener(JLabel in this case)
        	//we want this info
        	// getPoint() gets the location of the click with respect to the container
        	setStartPoint(e.getX(), e.getY(), e.getPoint().x, (e.getPoint().y - view.y));
        	

            //gets location on the component
        	//setStartPoint(e.getPoint().x, (e.getPoint().y - view.y));
            
        }

        public void mouseDragged(MouseEvent e) {
        	JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, getJLabel());
        	Rectangle view = viewPort.getViewRect();
        	//setEndPoint(e.getPoint().x, (e.getPoint().y - view.y));
            
            setEndPoint(e.getX(), e.getY(), e.getPoint().x, (e.getPoint().y - view.y));
            Rectangle visible = getJLabel().getVisibleRect();
            //TODO get current location
            //if(mouse location gets nearer to the boundary, height)
            
            System.out.println("Current visible view:  " + visible);

           
        	System.out.println("Mouse position: " + e.getPoint().y);
        	
        	//this part automatically scrolls the JLabel when dragged near the bounds
        	if(e.getPoint().y > (view.height - 30) && downward) {
            	
            	view.x = 0;
            	view.y += 10;
            	
            	yS -= 10;
            	
            	getJLabel().scrollRectToVisible(view);
            	
            }else if(((e.getPoint().y - view.y) < 30) && !downward) {
            	view.x = 0;
            	view.y = Math.abs(view.y - 10);
            	
            	yS +=10;
            	
            	getJLabel().scrollRectToVisible(view);
            	
            }
            
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
        	JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, getJLabel());
        	Rectangle view = viewPort.getViewRect();
            //setEndPoint(e.getPoint().x, (e.getPoint().y - view.y));
            setEndPoint(e.getX(), e.getY(), e.getPoint().x, (e.getPoint().y - view.y));
        	System.out.println("Rectangle: " + Math.min(x,x2) + ", " + Math.min(y,y2) + ", " + Math.abs(x-x2) + ", " + Math.abs(y-y2));
            setScaledSelection();
            
            repaint();
        }
        
        public void mouseMoved(MouseEvent e) {
        	
        }
    }
   

}
