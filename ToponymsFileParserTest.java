import java.io.*;
import java.util.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing the parsing for the toponyms file
 * @author Guillermo Tapia
 */
public class ToponymsFileParserTest {
    public static void main(String args[]) {
      org.junit.runner.JUnitCore.main("ToponymsFileParserTest");
    }

    @Test
    public void testOKFile() {
        try {
            File f = new File("data/Toponims/test/Toponims_short.txt");
            ToponymsFileParser parser = new ToponymsFileParser(f);
            Iterator<HashMap<String,String>> itr = parser.getIterator();
            HashMap<String,String> map = new HashMap<String,String>();
            if (itr.hasNext()) {
                map = itr.next();
            }
            
            HashMap<String,String> expectedmap = new HashMap<String,String>();
            expectedmap.put("Nom_UTF", "Costa_de_Xurius");
            expectedmap.put("Nom_ASCII", "Costa_de_Xurius");
            expectedmap.put("Noms_Alternatius", "NULL");
            expectedmap.put("Latitud", "42.5");
            expectedmap.put("Longitud", "1.48333");
			expectedmap.put("CodiToponim", "SLP");
            expectedmap.put("Poblacio", "0");

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
            File f = new File("data/Toponims/test/ToponimsMiddleWrong.txt");
            ToponymsFileParser parser = new ToponymsFileParser(f);
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
            File f = new File("data/Toponims/test/ToponimsBlankEndingLine.txt");
            ToponymsFileParser parser = new ToponymsFileParser(f);
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
            File f = new File("data/Toponims/test/RandomTextFile.txt");
            ToponymsFileParser parser = new ToponymsFileParser(f);
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
            File f = new File("data/Toponims/test/ToponimsWrongHeaders.txt");
            ToponymsFileParser parser = new ToponymsFileParser(f);
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
            File f = new File("data/Toponims/test/Toponims_short.txt");
            ToponymsFileParser parser = new ToponymsFileParser(f);
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
        ToponymsFileParser parser = new ToponymsFileParser(f);

    }
}