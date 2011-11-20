import java.util.*;
import java.io.*;
import java.lang.*;


public class BordersFileParserIterator implements Iterator<HashMap<String,String>> {
    /* Iterator for the borders file parser.

       
    */
    private BufferedReader _reader;
    private String _currentLine;
    private HashMap<String,String> _currentBorderPoint = new HashMap<String,String>();
 
    public BordersFileParserIterator(BufferedReader reader) throws IOException, RuntimeException {
        /* @reader: BufferedReader pointing to the borders file (or stream) to read from.

           Throws RuntimeException if the first line of the file doesn't have 5 columns,
           or IOException if there were any problems reading the file lines.
        */

        _reader = reader;
        _currentLine = _reader.readLine();  // header

        if (!rightFormat())
            throw new RuntimeException();

        _currentLine = _reader.readLine();
        _currentBorderPoint = getCurrentBorderPoint();
    }
    
    private boolean rightFormat() {
        // Return true if the first line of the file (header) contains 5 columns
        String[] fields = _currentLine.split(" ");
        if (fields.length == 5) return true;
        return false; // otherwise
    }

    public boolean hasNext() {
        /* Return true if there's a next line in the file

           This doesn't mean that there's really a new element, only that there could be. This 
           is so because we want to throw RuntimeExceptions if the next line is
           not a valid border point instead of swallowing them.
        */
        return _currentLine != null;
    }
 
    public HashMap<String,String> next() throws NoSuchElementException, RuntimeException {
        /*
        Return the new element as a map.

        Throws NoSuchElementException if we're at the end of the file or there was
        an IOError. Throws RuntimeException if the format of the read line is wrong and
        cannot be parsed.
        */

        if (_currentBorderPoint == null)
            throw new NoSuchElementException();

        HashMap<String,String> temp = (HashMap<String,String>) _currentBorderPoint.clone();

        try {
            // prepare new point
            _currentLine = _reader.readLine();  // advance one line for the next iteration
        } catch (IOException e) {
            _currentLine = null;
            _currentBorderPoint = null;
        }

        if (_currentLine != null) {
            _currentBorderPoint = getCurrentBorderPoint();
        } else { // EOF
            _currentBorderPoint = null;
        }

        // note that we don't catch RuntimeException on purpose to throw it
        // if format errors are found

        return temp;
    }

    private HashMap<String,String> getCurrentBorderPoint() throws RuntimeException {

        String[] fields = _currentLine.split(" ");
        if (fields.length != 5) throw new RuntimeException();

        try {
            Integer id_zone = new Integer(fields[0]);
            _currentBorderPoint.put("id_zone", fields[0]);

            Double latitude = new Double(fields[1]);
            _currentBorderPoint.put("latitude", fields[1]);

            Double longitude = new Double(fields[2]);
            _currentBorderPoint.put("longitude", fields[2]);

            String id_country = fields[3];
            _currentBorderPoint.put("id_country", fields[3]);

            String name_country = fields[4];
            _currentBorderPoint.put("name_country", fields[4]);
        } catch (NumberFormatException e) {
            throw new RuntimeException();
        }

        return _currentBorderPoint;
    }
 
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This is a readonly iterator.");
    }

}