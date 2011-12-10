import java.io.*;
import java.lang.*;
import java.util.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ToponymTypesDeserializerTest extends TestCase {

    private ToponymTypesDeserializer ttd;
    private CitiesController citiesc;

    @Before
    public void setUp() throws Exception {
        citiesc = new CitiesController();

        File ftypes = new File("FeatureCodes_Cities.txt");
        TypesFileParser typesParser = new TypesFileParser(ftypes);
        Iterator<HashMap<String,String>> typesItr = typesParser.getIterator();
        ttd = new ToponymTypesDeserializer(typesItr,citiesc);
        ttd.generate();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNToponymTypes() {
        Iterator<HashMap<String,String>> itr = citiesc.getToponymTypesIterator();
        int n = 0;
        while (itr.hasNext()) {
            itr.next();
            n++;
        }
        System.out.print("Checking that there are 15 city-like toponym types...");
        assertEquals(15, n);
        System.out.println("OK");
    }
}
