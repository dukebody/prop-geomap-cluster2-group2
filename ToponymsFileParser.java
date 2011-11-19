import java.util.*;
import java.io.*;

class ToponymsFileParser extends FileParser {

    public ToponymsFileParser(File file) {
        try {
            _reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("The file was unable to be read due to: " + e);
        }
    }

    public Iterator<HashMap<String,String>> getIterator() {
	ToponymsFileParserIterator top=null;
	try {
            top= new ToponymsFileParserIterator(_reader);
	} catch (IOException e) {
	    System.out.println("There has been an IOException");
	}
	return top;
    }
 
}