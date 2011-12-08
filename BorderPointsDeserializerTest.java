import java.io.*;
import java.lang.*;
import java.util.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BorderPointsDeserializerTest extends TestCase {

    private BorderPointsDeserializer bpd;
    private CountryController cc;
    private ZonesController zc;

    @Before
    public void setUp() throws Exception {
        cc = new CountryController();
        zc = new ZonesController();

        File f = new File("Fronteres_A.txt");
        BordersFileParser parser = new BordersFileParser(f);
        Iterator<HashMap<String,String>> itr = parser.getIterator();

        bpd = new BorderPointsDeserializer(itr, cc, zc);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGenerator() throws Exception {
        bpd.generate();
        
        System.out.print("Checking that there are N countries...");
        Iterator itr = cc.getAllCountriesIterator();
        int n = 0;
        while (itr.hasNext()) {
            itr.next();
            n++;
        }
        assertEquals(5, n);
        System.out.println("OK");
    }

}