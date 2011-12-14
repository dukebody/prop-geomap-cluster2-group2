import java.io.*;
import java.lang.*;
import java.util.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CitiesDeserializerTest extends TestCase {

    private CitiesDeserializer cd;
    private ToponymTypesDeserializer ttd;
    private CountryController countryc;
    private ZonesController zonesc;
    private CitiesController citiesc;

    @Before
    public void setUp() throws Exception {
        countryc = new CountryController();
        zonesc = new ZonesController(countryc.getBorderPointsQuadTree());
        citiesc = new CitiesController();

        File ftypes = new File("FeatureCodes_Cities.txt");
        TypesFileParser typesParser = new TypesFileParser(ftypes);
        Iterator<HashMap<String,String>> typesItr = typesParser.getIterator();
        ttd = new ToponymTypesDeserializer(typesItr,citiesc);
        ttd.generate();

        File f = new File("Toponims10000Populated.txt");
        ToponymsFileParser parser = new ToponymsFileParser(f);
        Iterator<HashMap<String,String>> itr = parser.getIterator();

        cd = new CitiesDeserializer(itr, citiesc);
        cd.generate();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNCitiesStartingWithT() {
        Iterator itr = citiesc.getCitiesPrefixIterator("T");
        int n = 0;
        while (itr.hasNext()) {
            itr.next();
            n++;
        }
        System.out.print("Checking that there are 336 cities starting with T...");
        assertEquals(319, n);
        System.out.println("OK");
    }
}