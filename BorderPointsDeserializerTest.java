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

        File f = new File("Fronteres_all.txt");
        BordersFileParser parser = new BordersFileParser(f);
        Iterator<HashMap<String,String>> itr = parser.getIterator();

        bpd = new BorderPointsDeserializer(itr, cc, zc);
        bpd.generate();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTotalNCountries() {
        System.out.print("Checking that there are 251 countries in total...");
        Iterator itr = cc.getAllCountriesIterator();
        int n = 0;
        while (itr.hasNext()) {
            itr.next();
            n++;
        }
        assertEquals(251, n);
        System.out.println("OK");
    }

    @Test
    public void testNCountriesStartingWithA() {
        bpd.generate();
        
        System.out.print("Checking that there are 15 countries starting with A...");
        Iterator itr = cc.getPrefixCountriesIterator("A");
        int n = 0;
        while (itr.hasNext()) {
            itr.next();
            n++;
        }
        assertEquals(15, n);
        System.out.println("OK");
    }

}