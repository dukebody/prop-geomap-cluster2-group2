/*
Author: Israel Saeta Pérez
*/

import java.io.*;
import java.util.*;

/**
A FileParser is a data management component that takes a file and returns an iterator
over a HashMap with the contents of each line of the file.

@throws IOException if the file couldn't be read or its format is not valid to be parsed.
*/
public abstract class FileParser {

    protected BufferedReader _reader;
 
 
    public void Close() {
        try {
        _reader.close();
        }
        catch (Exception ex) {}
    }

	abstract Iterator<HashMap<String,String>> getIterator() throws IOException;
	// Return a iterator that will yield a Map for each line of the file with the information indexed by keys.

}