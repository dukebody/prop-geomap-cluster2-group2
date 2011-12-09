import java.io.*;
import java.lang.*;
import java.util.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CitiesDeserializerTest extends TestCase {

    private CitiesDeserializer cd;
    private CountryController countryc;
    private ZonesController zonesc;
    private CitiesController citiesc;

    @Before
    public void setUp() throws Exception {
        countryc = new CountryController();
        zonesc = new ZonesController();
        citiesc = new CitiesController();

        File f = new File("Toponims10000.txt");
        ToponymsFileParser parser = new ToponymsFileParser(f);
        Iterator<HashMap<String,String>> itr = parser.getIterator();

        cd = new CitiesDeserializer(itr, citiesc);
        cd.generate();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNCitiesStartingWithA() {
        Iterator itr = citiesc.getCitiesPrefixIterator("A");
        int n = 0;
        while (itr.hasNext()) {
            itr.next();
            n++;
        }
        System.out.print("Checking that there are 185 cities starting with A...");
        assertEquals(185, n);
        System.out.println("OK");
    }
}