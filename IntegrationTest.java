import java.io.*;
import java.lang.*;
import java.util.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.textui.TestRunner;


public class IntegrationTest extends TestCase {

    public static void main(String[] args) {
        TestSuite suite = new TestSuite();
        TestRunner runner = new TestRunner();
        suite.addTestSuite(IntegrationTest.class);
        runner.doRun(suite);
    }

    private CitiesDeserializer cd;
    private BorderPointsDeserializer bpd;
    private ToponymTypesDeserializer ttd;

    private CountryController countryc;
    private ZonesController zonesc;
    private CitiesController citiesc;

    @Before
    public void setUp() throws Exception {

        countryc = new CountryController();
        zonesc = new ZonesController(countryc.getBorderPointsQuadTree());
        citiesc = new CitiesController();

        File fFronteres = new File("Fronteres_OurEurope.txt");
        BordersFileParser parserFronteres = new BordersFileParser(fFronteres);
        Iterator<HashMap<String,String>> itrFronteres = parserFronteres.getIterator();


        File fTypes = new File("FeatureCodes_Cities.txt");
        TypesFileParser typesParser = new TypesFileParser(fTypes);
        Iterator<HashMap<String,String>> itrTypes = typesParser.getIterator();
        

        File fToponyms = new File("Toponims10000.txt");
        ToponymsFileParser parserToponyms = new ToponymsFileParser(fToponyms);
        Iterator<HashMap<String,String>> itrToponyms = parserToponyms.getIterator();

        cd = new CitiesDeserializer(itrToponyms, citiesc);
        bpd = new BorderPointsDeserializer(itrFronteres, countryc, zonesc);
        ttd = new ToponymTypesDeserializer(itrTypes, citiesc);

        //ttd.generate();
        bpd.generate();
        //cd.generate();
    }

    // @Test
    // public void testNToponymTypes() {
    //     Iterator<HashMap<String,String>> itr = citiesc.getToponymTypesIterator();
    //     int n = 0;
    //     while (itr.hasNext()) {
    //         itr.next();
    //         n++;
    //     }
    //     System.out.print("Checking that there are 15 city-like toponym types...");
    //     assertEquals(15, n);
    //     System.out.println("OK");
    // }

    @Test
    public void testGetNeighborCountries() {
    	List<HashMap<String,String>> neighborCountries = countryc.getNeighborCountries("Spain");
    	HashSet<String> countryNames = new HashSet<String>();
    	for (HashMap<String,String> map: neighborCountries) {
    		countryNames.add(map.get("name"));
    	}
    	System.out.print("Checking that Spain limits with France, Portugal, Gibraltar and Andorra...");
        assertTrue(countryNames.contains("France"));
        assertTrue(countryNames.contains("Portugal"));
        assertTrue(countryNames.contains("Gibraltar"));
        assertTrue(countryNames.contains("Andorra"));
        System.out.println("OK");
    }

	@Test
    public void testTotalCoastLineLength() {
    	System.out.print("Checking that Spain has coast...");
        assertTrue(countryc.getTotalCoastlineLength("Spain") > 0);
        System.out.println("OK");
    }

    @Test
    public void testTotalSharedBorderLength() {
    	System.out.print("Checking that Spain has shared border length...");
        assertTrue(countryc.getTotalSharedBorderLength("Spain") > 0);
        System.out.println("OK");
    }

}