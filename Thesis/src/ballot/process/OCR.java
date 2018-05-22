package ballot.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCR {

	ITesseract tess;
	private static List<File> files = new ArrayList<>();
	
	public OCR() {
		 tess = new Tesseract(); 
	}
	
	public void writeToTxt(File imageFile) {
		
		System.out.println(imageFile.getName());
		try {
            String result = tess.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
	}
	    
    public void fileTraverse() {

        String dirName = "C:\\Users\\olis_\\git\\ThesisGit\\Thesis\\images\\temp";
        File file = new File(dirName);
        List<File> myfiles = doListing(file);
        
        for(int i=0; i<myfiles.size(); i++) {
        	
        	writeToTxt(myfiles.get(i));
        }
    }

    //traverse each files in the directory and add it to the List
    public static List<File> doListing(File dirName) {
        File[] fileList = dirName.listFiles();
        
        for (File file : fileList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                files.add(file);
                doListing(file);
            }
        }
        return files;
    }
}
