package ballot.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GW extends JPanel implements ActionListener {
        
        /**
     * 
     */
    private static final long serialVersionUID = 1L;
        static final int height = 600;
        static final int width = 600;
    
        BufferedImage gWorld;
    
    
    public GW() throws InterruptedException, IOException {
        setBackground(Color.black);
        performEpisode();
        
    }
    
    public void performEpisode() throws InterruptedException{
            drawGame(Color.green);
            repaint();
         Thread.sleep(2000);
         drawGame(Color.red);
         repaint();
    }
    
    public void update(Graphics g){
        paintComponent(g);        
    }
    
    public void drawGame(Color col){
        BufferedImage bim = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics b = bim.getGraphics();
        b.setColor(col);
        for(int i = 1; i <= 3; i++){
            b.drawLine(i*600/4, 0, i*600/4, 600);
            b.drawLine(0, i*600/4, 600, i*600/4);
        }

        gWorld = bim;
    }
    
    
    public void paintComponent(Graphics g){
    	System.out.println("HEYYYYYYY");
        //g.drawImage(gWorld, 0, 0, this);
    }

    
    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame f = new JFrame();
        f.setSize(600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLocation(400,100);
        f.add(new GW());
        f.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
