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
    private LineController linec;

    @Before
    public void setUp() throws Exception {
        DataStorage ds = new DataStorage();
        countryc = new CountryController(ds);
        zonesc = new ZonesController(ds);
        citiesc = new CitiesController(ds);
        linec = new LineController(ds);

        File fFronteres = new File("data/Fronteres/test/Fronteres_OurEurope.txt");
        BordersFileParser parserFronteres = new BordersFileParser(fFronteres);
        Iterator<HashMap<String,String>> itrFronteres = parserFronteres.getIterator();


        File fTypes = new File("data/Toponims/FeatureCodes_Cities.txt");
        TypesFileParser typesParser = new TypesFileParser(fTypes);
        Iterator<HashMap<String,String>> itrTypes = typesParser.getIterator();

        File fToponyms = new File("data/Toponims/test/Toponims_PopulatedShort.txt");
        ToponymsFileParser parserToponyms = new ToponymsFileParser(fToponyms);
        Iterator<HashMap<String,String>> itrToponyms = parserToponyms.getIterator();

        bpd = new BorderPointsDeserializer(itrFronteres, countryc, zonesc);
        bpd.generate();

        ttd = new ToponymTypesDeserializer(itrTypes, citiesc);
        ttd.generate();

        cd = new CitiesDeserializer(itrToponyms, citiesc);
        cd.generate();
    }

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

    @Test
    public void testSharedBorderLengthWith() {
        System.out.print("Checking that Spain has a lengthy border with France...");
        assertTrue(countryc.getSharedBorderLengthWith("Spain", "France") > 0);
        System.out.println("OK");

        System.out.print("Checking that Spain has a lengthy border with Portugal...");
        assertTrue(countryc.getSharedBorderLengthWith("Spain", "Portugal") > 0);
        System.out.println("OK");

        System.out.print("Checking that Spain has a lengthy border with Gibraltar...");
        assertTrue(countryc.getSharedBorderLengthWith("Spain", "Gibraltar") > 0);
        System.out.println("OK");

        System.out.print("Checking that Spain has a lengthy border with Andorra...");
        assertTrue(countryc.getSharedBorderLengthWith("Spain", "Andorra") > 0);
        System.out.println("OK");
    }

    @Test
    public void testSharedBorderLengthTotalCorrect() {
        Double individualSum = countryc.getSharedBorderLengthWith("Spain", "France") + countryc.getSharedBorderLengthWith("Spain", "Portugal") +
            countryc.getSharedBorderLengthWith("Spain", "Gibraltar") + countryc.getSharedBorderLengthWith("Spain", "Andorra");
        Double expectedTotal = countryc.getTotalSharedBorderLength("Spain");

        System.out.print("Checking that the sum of all individual shared borders equals to the calculated total...");
        assertEquals(individualSum, expectedTotal);
        System.out.println("OK");
    }

    @Test
    public void testPointIsInsideZone() {
        Zone peninsula = countryc.getRawCountry("Spain").getZones().get(15);
        System.out.print("Checking that a point inside the Peninsula is detected as so...");
        assertTrue(linec.checkIfPointIsInsideZone(new Point(40.0, -3.0), peninsula));
        System.out.println("OK");

        Zone island = countryc.getRawCountry("Spain").getZones().get(14);
        System.out.print("Checking that a point outside the Peninsula is detected as so...");
        assertFalse(linec.checkIfPointIsInsideZone(new Point(40.0, -3.0), island));
        System.out.println("OK");
    }

    @Test
    public void testGetMainCitiesByType() {
        List<String> typeCodes = new ArrayList<String>();
        typeCodes.add("PPLA");
        List<HashMap<String,String>> mainCities = countryc.getMainCitiesByType("Andorra", typeCodes);

        System.out.print("Checking getMainCitiesByType with Andorra, expecting 4 cities...");
        assertEquals(4, mainCities.size());
        System.out.println("OK");

        typeCodes.add("PPL");
        mainCities = countryc.getMainCitiesByType("France", typeCodes);
        System.out.print("Checking getMainCitiesByType with France, expecting 1 cities...");
        assertEquals(1, mainCities.size());
        System.out.println("OK");
    }

    @Test
    public void testGetMainCitiesByPopulation() {
        List<String> typeCodes = new ArrayList<String>();
        typeCodes.add("PPLA");

        List<HashMap<String,String>> mainCities = countryc.getMainCitiesByPopulation("Andorra", 3);
        System.out.print("Checking getMainCitiesByPopulation with Andorra, expecting 3 cities...");
        assertEquals(3, mainCities.size());
        System.out.println("OK");

        mainCities = countryc.getMainCitiesByPopulation("Andorra", 10);
        System.out.print("Checking getMainCitiesByType never returns more cities than available...");
        assertEquals(5, mainCities.size());
        System.out.println("OK");
    }

    @Test
    public void testGetMainCoastalBorderCities() {
        List<HashMap<String,String>> mainCities = countryc.getMainCoastalBorderCities("Andorra", 0.01);
        System.out.print("Checking getMainCoastalBorder with Andorra and 0.01 distance, expecting 1 cities...");
        assertEquals(1, mainCities.size());
        System.out.println("OK");

        mainCities = countryc.getMainCoastalBorderCities("Andorra", 10.0);
        System.out.print("Checking getMainCoastalBorder with Andorra and 10.0 distance, expecting 5 cities...");
        assertEquals(5, mainCities.size());
        System.out.println("OK");
    }

    @Test
    public void testGetCountryBorderPointsForDrawing() {
        ArrayList<ArrayList<Double[]>> allPoints = countryc.getCountryBorderPointsForDrawing("Spain");
        System.out.print("Checking that getCountryBorderPointsForDrawing for Spain is not empty...");
        assertFalse(allPoints.isEmpty());
        System.out.println("OK");

        System.out.print("Checking that getCountryBorderPointsForDrawing for Spain has 16 zones...");
        assertEquals(16, allPoints.size());
        System.out.println("OK");

        System.out.print("Checking that getCountryBorderPointsForDrawing for Spain has no empty zones...");
        for (ArrayList<Double[]> zonePoints: allPoints) {
            assertFalse(zonePoints.isEmpty());
        }
        System.out.println("OK");

    }

    @Test
    public void testGetCountryExtremeValues() {
        ArrayList<Double> countryExtreme = countryc.getCountryExtremeValues("Spain");
        Country country = countryc.getRawCountry("Spain");

        System.out.print("Checking that getCountryExtremeValues returns the most extreme values of all zones...");
        for (Zone zone: country.getZones()) {
            ArrayList<Double> zoneExtreme = linec.getZoneExtremeValues(zone);
            assertTrue(countryExtreme.get(0) >= zoneExtreme.get(0));
            assertTrue(countryExtreme.get(1) <= zoneExtreme.get(1));
            assertTrue(countryExtreme.get(2) >= zoneExtreme.get(2));
            assertTrue(countryExtreme.get(3) <= zoneExtreme.get(3));
            }
        System.out.println("OK");
        }

    @Test
    public void testGetCountriesInTheSameArea() {
        List<HashMap<String,String>> countries = countryc.getCountriesInTheSameArea("Spain");
        System.out.print("Checking that getCountriesInTheSameaArea returns the countries in the same area...");
        assertEquals(5, countries.size());
        System.out.println("OK");

        countries = countryc.getCountriesInTheSameArea("UNEXISTENT");
        System.out.print("Checking that getCountriesInTheSameaArea returns an empty list if the country does not exist...");
        assertTrue(countries.isEmpty());
        System.out.println("OK");
    }

}