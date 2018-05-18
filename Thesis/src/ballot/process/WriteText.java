package ballot.process;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteText {
	
	public PrintWriter textWriter(String filename) {
		
	    PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream("text/" + filename), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return pw;
		
	}
}