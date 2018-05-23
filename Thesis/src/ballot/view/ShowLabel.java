package ballot.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

public class ShowLabel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y, x2, y2;

    JScrollPane sp;
	
	public JLabel lbl1;
	
	public ShowLabel(JPanel showPanel) {
		
		setLayout(new MigLayout());
		//setFocusable(true);
		lbl1 = new JLabel();
        /*lbl1.setBorder(
            BorderFactory.createEtchedBorder()
        );*/
        
        this.add(lbl1);
        
        sp = new JScrollPane(lbl1);
        add(sp, "w 1000, h 670");

        //create mouse listener
      	x = y = x2 = y2 = 0; //initialize var
      	MyMouseListener listener = new MyMouseListener();
      	lbl1.addMouseListener(listener);
      	lbl1.addMouseMotionListener(listener);
		
	}
	
	public JLabel getJLabel() {
		
		return lbl1;
	}


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

    public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2) + 8;
        int py = Math.min(y,y2) + 8;
        int pw = Math.abs(x-x2);
        int ph = Math.abs(y-y2);
        g.drawRect(px, py, pw, ph);
        System.out.println("Rectangle: " + Math.min(x,x2) + ", " + Math.min(y,y2) + ", " + pw + ", " + ph);
    }

	//@Override
    public void paint(Graphics g) {

        super.paint(g);
        g.setColor(Color.RED);
        drawPerfectRect(g, x, y, x2, y2);
        System.out.println("FUCK YOU");
    }
    
    class MyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            setStartPoint(e.getX(), e.getY());
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

}
