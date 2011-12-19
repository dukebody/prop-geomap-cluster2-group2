/*
Author: Israel Saeta Pérez
*/

import java.util.*;
import java.io.*;


// http://code.hammerpig.com/how-to-read-really-large-files-in-java.html <-- inspiration

/**
This component takes a valid border points file and generates an iterator
with the data of the file.
*/
public class BordersFileParser extends FileParser {

    /**
    Create a new BordersFileParser using the specified filehandler.

    @param f File handler.
    */
    public BordersFileParser(File f) throws FileNotFoundException {
        _reader = new BufferedReader(new FileReader(f));

    }

    /**
    Get a iterator returning a HashMap with the contents of all fields
    in the file.

	If the file can't be loaded or the format is wrong IOException is thrown.
    */
    public Iterator<HashMap<String,String>> getIterator() throws IOException
    {
        return new BordersFileParserIterator(_reader);
    }
 
}