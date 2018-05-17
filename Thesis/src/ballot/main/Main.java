package ballot.main;

import javax.swing.SwingUtilities;

import org.opencv.core.Core;

import ballot.view.MainFrame;

public class Main{
	
	public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame();
			}
		});
    }
}