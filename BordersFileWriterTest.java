import java.io.*;
import java.lang.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BordersFileWriterTest extends TestCase {

    private boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
    // returns true if the contents of the two inputs are equal, false otherwise
    if (!(input1 instanceof BufferedInputStream))
    {
        input1 = new BufferedInputStream(input1);
    }
    if (!(input2 instanceof BufferedInputStream))
    {
        input2 = new BufferedInputStream(input2);
    }
    int ch = input1.read();
    while (-1 != ch) {
      int ch2 = input2.read();
      if (ch != ch2)
      {
        return false;
      }
      ch = input1.read();
    }

    int ch2 = input2.read();
    return (ch2 == -1);
    }

    @Before
    public void setUp() throws Exception {
        File file = new File("bords.txt");
        File file2 = new File("data/Fronteres/test/BordersTest.txt");
        BordersFileWriter writer= new BordersFileWriter(file);
        BordersFileParser parser = new BordersFileParser(file2);
        writer.write(parser.getIterator());
    }

    @After
    public void tearDown() throws Exception {
        // delete generated file
        File f = new File("bords.txt");
        f.delete();
    }

    @Test
    public void testEqualFiles() throws Exception {
        String thisLine1;
        FileInputStream fin1 =  new FileInputStream("bords.txt");
        FileInputStream fin2 =  new FileInputStream("data/Fronteres/test/BordersTest.txt");

        System.out.print("Checking that convertion from file to hashmaps and back returns identical contents...");
        assertEquals(true, contentEquals(fin1, fin2));
        System.out.println("OK");
    }
}



