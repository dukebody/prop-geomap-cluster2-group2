import java.io.*;
import java.util.*;


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