/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// package Tests;

// import Beans.City;
// import Beans.Country;
// import Beans.DataStorage;
// import Beans.Interval;
// import Beans.Interval2D;
// import Beans.Node;
// import Beans.Point;
// import Beans.Zone;
// import Controllers.CitiesController;
// import Controllers.CountryController;
// import Controllers.LineController;
// import Controllers.ZonesController;
// import Deserializers.BorderPointsDeserializer;
// import Deserializers.CitiesDeserializer;
// import Deserializers.ToponymTypesDeserializer;
// import FileParsers.BordersFileParser;
// import FileParsers.ToponymsFileParser;
// import FileParsers.TypesFileParser;
import java.lang.*;
import java.io.*;
import java.util.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.textui.TestRunner;

/**
 *
 * @author Guillermo
 */
public class IntegrationTest2 extends TestCase {

    public static void main(String[] args) {
        TestSuite suite = new TestSuite();
        TestRunner runner = new TestRunner();
        suite.addTestSuite(IntegrationTest.class);
        runner.doRun(suite);
    }

    private CitiesDeserializer cd;
    private BorderPointsDeserializer bpd;
    private ToponymTypesDeserializer ttd;

    private DataStorage ds = new DataStorage();

    private CountryController countryc;
    private ZonesController zonesc;
    private CitiesController citiesc;
    private LineController linec;

    @Before
    public void setUp() throws Exception {
        //System.out.println("CURRENT PATH: " + System.getProperty("user.dir"));
        //System.out.println("Marker #1");
        countryc = new CountryController(ds);
        zonesc = new ZonesController(ds);
        citiesc = new CitiesController(ds);
        linec = new LineController(ds);
        //System.out.println("Marker #2");

        File fFronteres = new File("Fronteres_OurEurope.txt");
        //File fFronteres = new File("Fronteres_Petits.txt");
        //File fFronteres = new File("Fronteres_A.txt");
        BordersFileParser parserFronteres = new BordersFileParser(fFronteres);
        Iterator<HashMap<String,String>> itrFronteres = parserFronteres.getIterator();
        //System.out.println("Marker #3");

        File fTypes = new File("FeatureCodes_Cities.txt");
        TypesFileParser typesParser = new TypesFileParser(fTypes);
        Iterator<HashMap<String,String>> itrTypes = typesParser.getIterator();
        //System.out.println("Marker #4");

        //File fToponyms = new File("TopTop.txt");
        File fToponyms = new File("TopTop.txt");
        ToponymsFileParser parserToponyms = new ToponymsFileParser(fToponyms);
        Iterator<HashMap<String,String>> itrToponyms = parserToponyms.getIterator();
        //System.out.println("Marker #5");

        bpd = new BorderPointsDeserializer(itrFronteres, countryc, zonesc);
        bpd.generate();
        //System.out.println("Marker #6");

        ttd = new ToponymTypesDeserializer(itrTypes, citiesc);
        ttd.generate();
        //System.out.println("Marker #7");

        cd = new CitiesDeserializer(itrToponyms, citiesc);
        cd.generate();
        //System.out.println("Marker #8");
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
    	System.out.print("Checking that Spain has coast: " + countryc.getTotalCoastlineLength("Spain"));
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

        List<HashMap<String,String>> mainCities = countryc.getMainCitiesByPopulation("Spain", 3);
        System.out.print("Checking getMainCitiesByPopulation with Spain, expecting 3 cities...");
        assertEquals(3, mainCities.size());
        System.out.println("OK");

        mainCities = countryc.getMainCitiesByPopulation("Spain", 10);
        System.out.print("Checking getMainCitiesByType never returns more cities than available...");
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
    public void testGetMainFrontierCities() {
        Country andorra = ds.getCountriesTrie().get("Andorra");
        Zone andorraZone = andorra.getZones().get(0);
        System.out.println("-All Andorra (" + andorra.getZones().size() + ") " + andorraZone.getId() + " bPoints: " + andorraZone.getBorderpoints().size());
        ArrayList<Double> extremeValues = linec.getZoneExtremeValues(andorraZone);
        Double maxLat = extremeValues.get(2);
        Double minLat = extremeValues.get(3);
        Double maxLong = extremeValues.get(0);
        Double minLong = extremeValues.get(1);
        System.out.println("-Extreme value 1: " + maxLong);
        System.out.println("-Extreme value 2: " + minLong);
        System.out.println("-Extreme value 3: " + maxLat);
        System.out.println("-Extreme value 4 " + ds.getCitiesQuadTree().isEmpty() + ": " + minLat);
        Node<City> nodeCity = null;
        City city = null;
        int cont = 0;

        Interval<Double> intX = new Interval(minLat, maxLat);
        Interval<Double> intY = new Interval(minLong, maxLong);
        Interval2D<Double> rect = new Interval2D(intX, intY);
        List<Node<City>> nodeCities = new LinkedList<Node<City>>();

        // get the cities belonging to this rectangle
        ArrayList<Node<City>> nodes = ds.getCitiesQuadTree().query2D(rect);
        Iterator<Node<City>> nodesItr = nodes.iterator();
        while (nodesItr.hasNext()) {
            nodeCity = nodesItr.next();
            city = nodeCity.value;
            if (city.getZone().getId().equals(andorraZone.getId())) {
                nodeCities.add(nodeCity);
            } else cont++;
        }
        ArrayList<City> mainCities = linec.getAllBorderCitiesFromZone(andorraZone, nodeCities, 30.0);
        System.out.println("-Checking getAllBorderCitiesFromZone with Spain and 30 km distance, expecting all cities...");
        System.out.println("-All Andorra cities1: " + nodes.size());
        System.out.println("-All Andorra cities2: " + nodeCities.size());
        System.out.println("-All Border cities: " + mainCities.size());
        System.out.println("-Outside cities: " + cont);
        assertEquals(nodeCities.size(), mainCities.size());
        System.out.println("OK");

        //mainCities = linec.getMainCoastalBorderCities("Andorra", 10.0);
        //System.out.print("Checking getMainCoastalBorder with Andorra and 10.0 distance, expecting 5 cities...");
        //assertEquals(5, mainCities.size());
        //System.out.println("OK");
    }

}