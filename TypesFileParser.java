import java.util.*;
import java.io.*;


class TypesFileParser extends FileParser{

    public TypesFileParser(File f){
        try {
            _reader = new BufferedReader(new FileReader(f));
        }
        catch (FileNotFoundException e) {
            System.out.println("Exception: " + e);
        } 
    }

    public Iterator<HashMap<String,String>> getIterator() throws IOException{
    	return new TypesFileParserIterator(_reader);
    }
 
}