import java.util.*;
import java.io.*;

class ToponymsFileParser extends FileParser {

    public ToponymsFileParser(File file) throws FileNotFoundException {
        _reader = new BufferedReader(new FileReader(file));
    }

    public Iterator<HashMap<String,String>> getIterator() throws IOException {
		return new ToponymsFileParserIterator(_reader);
		
    }
 
}