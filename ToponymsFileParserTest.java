import java.io.*;
import java.util.*;

public class ToponymsFileParserTest {

	public static void main(String[] args) {
	 	File testFile;
        ToponymsFileParser fileParse;
        
        String fileName = "ToponymFileTest.txt";
		testFile = new File(fileName);
        fileParse = new ToponymsFileParser(testFile);
		
        try {
            Iterator<HashMap<String,String>> iterate = fileParse.getIterator();
            while (iterate.hasNext()) { 
                HashMap<String,String> map = iterate.next();
                System.out.println(map.get("Nom_UTF")+" - "+map.get("Latitud")+" - "+map.get("Longitud"));
            }
        } catch (RuntimeException e) {
	    System.out.println("Incorrect format for "+fileName);
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
		
		/*while (iterate.hasNext()) {
	 		HashMap<String,String> map = iterate.next();
	 		System.out.println("--------------");
	 		System.out.println(map.get("Nom_UTF"));
	 	}*/
	}
}