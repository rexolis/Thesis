package ballot.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.opencv.core.Rect;

import net.miginfocom.swing.MigLayout;

public class ShowLabel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y, x2, y2; //coordinates for the JLabel
	private int xp, yp, xp2, yp2; //coordinates for the JPanel
	private double widthRatio, heightRatio;
	
	public Rect scaledSelection;

    JScrollPane sp;
	
	public JLabel lbl1;
	
	public ShowLabel(JPanel showPanel) {
		
		setLayout(new MigLayout());
		lbl1 = new JLabel();
        /*lbl1.setBorder(
            BorderFactory.createEtchedBorder()
        );*/
        
		this.setFocusable(true);
        this.add(lbl1);

        sp = new JScrollPane(lbl1);
//        sp = new JScrollPane(this);
        add(sp, "w 1000, h 670");

        //create mouse listener
        x = y = x2 = y2 = 0; //initialize var for drawing
      	MyMouseListener listenerLabel = new MyMouseListener();
      	lbl1.addMouseListener(listenerLabel);
      	lbl1.addMouseMotionListener(listenerLabel);
//	  	this.addMouseListener(listenerLabel);
//	  	this.addMouseMotionListener(listenerLabel);
      	
//		MyMouseListenerP listenerPanel = new MyMouseListenerP();
//		this.addMouseListener(listenerPanel);
//		this.addMouseMotionListener(listenerPanel);
		
	}
	
	public JLabel getJLabel() {
		
		return lbl1;
	}
	
	public void setImgRatio(double width, double height) {
		
		widthRatio = width;
		heightRatio = height;
		
	}

	//LABEL
	
	public void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
        
        System.out.println("startPoint x: " + this.x + ", y: " + this.y);
    }

    public void setEndPoint(int x, int y) {
        x2 = (x);
        y2 = (y);
        
        System.out.println("endPoint x: " + x2 + ", y: " + y2);
    }

//    //PANEL
//    
//	public void setStartPointP(int x, int y) {
//        this.xp = x;
//        this.yp = y;
//        
//        System.out.println("startPointP x: " + this.xp + ", y: " + this.yp);
//    }
//
//    public void setEndPointP(int x, int y) {
//        xp2 = (x);
//        yp2 = (y);
//        
//        System.out.println("endPointP x: " + xp2 + ", y: " + yp2);
//    }

//    public void setLabelSelection(int x, int y, int x2, int y2) {
//        int px = Math.min(x,x2);
//        int py = Math.min(y,y2);
//        int pw = Math.abs(x-x2);
//        int ph = Math.abs(y-y2);
//        //g.drawRect(px, py, pw, ph);
//        System.out.println("Rectangle: " + Math.min(x,x2) + ", " + Math.min(y,y2) + ", " + pw + ", " + ph);
//        
//    }

    public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw = Math.abs(x-x2);
        int ph = Math.abs(y-y2);
        g.drawRect(px, py, pw, ph);
        System.out.println("Rectangle: " + Math.min(x,x2) + ", " + Math.min(y,y2) + ", " + pw + ", " + ph);
        
    }

	@Override
    public void paint(Graphics g) {

        super.paint(g);
        g.setColor(Color.RED);
        drawPerfectRect(g, x, y, x2, y2);
//        drawPerfectRect(g, xp, yp, xp2, yp2);
        //System.out.println("FUCK YOU");
    }
    
    //LABEL
    
    class MyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            setStartPoint(e.getX(), e.getY());
            System.out.println("THIS IS A " + e.getSource().getClass());
        }

        public void mouseDragged(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            repaint();
        }
    }
    
    
//    //PANEL
//    
//    class MyMouseListenerP extends MouseAdapter {
//
//        public void mousePressed(MouseEvent e) {
//            setStartPointP(e.getX(), e.getY());
//        }
//
//        public void mouseDragged(MouseEvent e) {
//            setEndPointP(e.getX(), e.getY());
//            repaint();
//        }
//
//        public void mouseReleased(MouseEvent e) {
//            setEndPointP(e.getX(), e.getY());
//            repaint();
//        }
//    }

}
