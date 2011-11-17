import java.util.*;
import java.io.*;


// http://code.hammerpig.com/how-to-read-really-large-files-in-java.html <-- inspiration

class BordersFileParser extends FileParser {

//================================second option=================================

//{

    public BordersFileParser(File f) throws FileNotFoundException {
        _reader = new BufferedReader(new FileReader(f));

    }

    public Iterator<HashMap<String,String>> getIterator() throws IOException
    {
        return new BordersFileParserIterator(_reader);
    }
 
}