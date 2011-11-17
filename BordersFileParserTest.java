import java.io.*;
import java.util.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BordersFileParserTest {
    public static void main(String args[]) {
      org.junit.runner.JUnitCore.main("BordersFileParserTest");
    }

    @Test
    public void testOKFile() {
        try {
            File f = new File("Fronteres.txt");
            BordersFileParser parser = new BordersFileParser(f);
            Iterator<HashMap<String,String>> itr = parser.getIterator();
            HashMap<String,String> map = new HashMap<String,String>();
            if (itr.hasNext()) { 
                map = itr.next();
            }
            
            HashMap<String,String> expectedmap = new HashMap<String,String>();
            expectedmap.put("id_zone", "0"); 
            expectedmap.put("latitude", "12.41111000"); 
            expectedmap.put("longitude", "-69.88223300"); 
            expectedmap.put("id_country", "AA");
            expectedmap.put("name_country", "Aruba");

            System.out.println("Checking that the mapping load works...");
            assertEquals(expectedmap, map);

        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (RuntimeException e) {
            System.out.println("The file format is wrong - Wrong!");
        }
    }

    @Test(expected=RuntimeException.class)
    public void testFileWithWrongMiddleFormat() {
        System.out.println("Checking that loading files with weird characters in the middle fails...");

        try {
            File f = new File("FronteresMiddleWrong.txt");
            BordersFileParser parser = new BordersFileParser(f);
            Iterator<HashMap<String,String>> itr = parser.getIterator();
            while (itr.hasNext()) { 
                HashMap<String,String> map = itr.next();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Test(expected=RuntimeException.class)
    public void testFileBlankEndingLine() {
        System.out.println("Checking that loading a file with a blank ending line fails...");

        try {
            File f = new File("FronteresBlankEndingLine.txt");
            BordersFileParser parser = new BordersFileParser(f);
            Iterator<HashMap<String,String>> itr = parser.getIterator();
            while (itr.hasNext()) { 
                HashMap<String,String> map = itr.next();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Test(expected=RuntimeException.class)
    public void testFileTotallyWrong() {
        System.out.println("Checking that loading files with a completely different format fails...");

        try {
            File f = new File("RandomTextFile.txt");
            BordersFileParser parser = new BordersFileParser(f);
            Iterator<HashMap<String,String>> itr = parser.getIterator();
            while (itr.hasNext()) { 
                HashMap<String,String> map = itr.next();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Test(expected=RuntimeException.class)
    public void testFileWrongHeaders() {
        System.out.println("Checking that loading files with wrong header format fails...");

        try {
            File f = new File("FronteresWrongHeaders.txt");
            BordersFileParser parser = new BordersFileParser(f);
            Iterator<HashMap<String,String>> itr = parser.getIterator();
            while (itr.hasNext()) { 
                HashMap<String,String> map = itr.next();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Test(expected=NoSuchElementException.class)
    public void testNextElementNotFound() {
        System.out.println("Checking that trying to get an element if hasNext() returns false fails...");

        try {
            File f = new File("Fronteres.txt");
            BordersFileParser parser = new BordersFileParser(f);
            Iterator<HashMap<String,String>> itr = parser.getIterator();
            while (itr.hasNext()) { 
                HashMap<String,String> map = itr.next();
            }
            HashMap<String,String> map = itr.next();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Test(expected=FileNotFoundException.class)
    public void testFileNotFound() throws FileNotFoundException {
        System.out.println("Checking that trying to read from a nonexistent file fails...");

        File f = new File("UNEXISTENT_FILE.txt");
        BordersFileParser parser = new BordersFileParser(f);

    }
}