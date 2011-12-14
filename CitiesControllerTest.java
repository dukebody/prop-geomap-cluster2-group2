import java.util.*;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class CitiesControllerTest extends TestCase {

    private CitiesController tc;

    @Before
    public void setUp() throws Exception {
        tc = new CitiesController();

        tc.createToponymType("City A", "City", "CA");
        tc.createToponymType("City B", "City", "CB");

        tc.addCity("Madrid", "Madrid", 34.3332, 12.4333, "CA", 22);
        tc.addCity("Barcelona", "Barcelona", 12.14, 1.2333, "CA", 155);
        tc.addCity("Norilandia", "Ñorilandia", 35.2, 46.24, "CA", 65);
        tc.addCity("Norilandia", "Norilandia", 36.2, 49.24, "CB", 80980);
        tc.addCity("Mandril", "Mandril", 45.14, 22.563, "CB", 980);
        tc.addCity("Mandril", "Mandril", 45.18, 22.580, "CB", 7484);
    }

    @After
    public void tearDown() throws Exception {
    }

    // public static junit.framework.Test suite() { 
    //     return new JUnit4TestAdapter(CitiesControllerTest.class); 
    // }

    @Test
    public void testGetToponymType() {
        System.out.println("Testing getToponymType...");

        System.out.print("Checking that getting an existing city by code returns the related map...");
        HashMap<String,String> type = tc.getToponymType("CA");
        assertEquals("City A", type.get("name"));
        assertEquals("City", type.get("category"));
        assertEquals("CA", type.get("code"));
        System.out.println("OK");

        System.out.print("\tChecking that getting a non-existing city by code returns null...");
        assertNull(tc.getToponymType("BADCODE"));
        System.out.println("OK");
    }

    @Test 
    public void testGetToponymTypesIterator() {
        System.out.println("Testing getToponymTypesIterator...");

        Iterator<HashMap<String,String>> types = tc.getToponymTypesIterator();
        int count = 0;
        while (types.hasNext()) {
            types.next();
            count++;
        }
        System.out.print("\tChecking getToponymTypesIterator returns an iterator to all the city types...");
        assertEquals(2, count);
        System.out.println("OK");
    }


    @Test
    public void testGetMapCity() {
        System.out.print("Testing getMap for a single city...");

        TypeToponym cityA = new TypeToponym("City A", "City", "CA");
        HashMap<String,String> map = tc.getMap(new City("Madrid", "Madrid", 34.3332, 12.4333, cityA, 99));
        assertEquals("Madrid", map.get("nameASCII"));
        assertEquals("Madrid", map.get("nameUTF"));
        assertEquals("34.3332", map.get("latitude"));
        assertEquals("12.4333", map.get("longitude"));
        assertEquals("CA", map.get("type"));
        System.out.println("OK");
    }

    @Test
    public void testGetMapToponymType() {
        System.out.print("Testing getMap for a single toponymtype...");

        TypeToponym cityA = new TypeToponym("City A", "City", "CA");
        HashMap<String,String> map = tc.getMap(cityA);
        assertEquals("City A", map.get("name"));
        assertEquals("City", map.get("category"));
        assertEquals("CA", map.get("code"));
        System.out.println("OK");
    }

    @Test
    public void testUniqueGetCitiesByName() {
        System.out.println("Testing getCityByName for unique names...");

        List<HashMap<String,String>> cities = tc.getCitiesByName("Madrid");
        System.out.print("\tGetting an unique existing city by ascii name should return a one-city list with that name...");
        assertEquals("Madrid", cities.get(0).get("nameASCII"));
        assertEquals(1, cities.size());
        System.out.println("OK");

        List<HashMap<String,String>> nonexistentCity = tc.getCitiesByName("NonExistent");
        System.out.print("\tGetting an non-existing city should return null...");
        assertTrue(nonexistentCity.isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testSeveralGetCitiesByName() {
        System.out.println("Testing getCityByName for names matching more than one element...");

        List<HashMap<String,String>> cities = tc.getCitiesByName("Mandril");
        System.out.print("\tGetting an non-unique existing city by ascii name should return a list with the cities matching that name...");
        assertEquals(2, cities.size());
        System.out.println("OK");
    }

    @Test
    public void testSeveralGetCityByNameAndId() {
        System.out.println("Testing getCityByName for names matching more than one element...");

        List<HashMap<String,String>> cities = tc.getCitiesByName("Mandril");
        HashMap<String,String> secondCity = cities.get(1);
        String secondId = secondCity.get("id");
        HashMap<String,String> target = tc.getCityByNameAndId("Mandril", secondId);
        System.out.print("\tGetting an non-unique existing city by ascii name and id should return the cities with that name and id...");
        // The last inserted city is the last one by id in the associated Trie
        assertEquals(secondCity.get("latitude"), target.get("latitude"));
        System.out.println("OK");
    }

    @Test
    public void testCitiesPrefixIterator() {
        System.out.println("Testing getCityByNamePrefix...");

        Iterator<HashMap<String,String>> cities = tc.getCitiesPrefixIterator("Ma");
        
        System.out.print("\tLooking for a prefix matching one or more elements should return them...");
        while (cities.hasNext()) {
            HashMap<String,String> city = cities.next();
            assertTrue(city.get("nameUTF").startsWith("Ma"));
        }
        System.out.println("OK");

        cities = tc.getCitiesPrefixIterator("NotMatchingPrefix");
        System.out.print("\tLooking for a prefix not matching any element should return null...");
        assertFalse(cities.hasNext());
        System.out.println("OK");
    }

    @Test
    public void testAddCity() {
        System.out.println("Testing addCity...");

        System.out.print("\tChecking that adding a city in a free location returns true...");
        assertTrue(tc.addCity("Zaragoza", "Zaragoza", 45.19, 22.580, "CA", 444));
        System.out.println("OK");

        HashMap<String,String> zaragoza = tc.getCitiesByName("Zaragoza").get(0);
        System.out.print("\tChecking that it was really added...");
        assertEquals("Zaragoza", zaragoza.get("nameASCII"));
        System.out.println("OK");
    }

    @Test
    public void testAddCityWithWrongData() {
        System.out.println("Testing addCity with invalid data...");

        System.out.print("\tChecking that adding a city in a free location returns true...");
        assertFalse(tc.addCity("Zaragoza", "Zaragoza", 555.19, 22.580, "CA", 55));
        System.out.println("OK");
    }

    @Test
    public void testAddCityOnExistingOneFails() {
        System.out.println("Testing addCity on top on an existing one (location-wise)...");

        System.out.print("\tChecking that this returns false...");
        assertFalse(tc.addCity("Zaragoza", "Zaragoza", 12.14, 1.2333, "CA", 455));
        System.out.println("OK");

        HashMap<String,String> barcelona = tc.getCitiesByName("Barcelona").get(0);
        System.out.print("\tChecking that the old city remains at its place...");
        assertEquals("12.14", barcelona.get("latitude"));
        System.out.println("OK");
    }

    @Test
    public void testModifyCity() {
        System.out.println("Testing modifyCity with an existing city...");

        String id = tc.getCitiesByName("Madrid").get(0).get("id");
        System.out.print("\tChecking that modifyCity(valid_id, *valid_data) returns True...");
        assertTrue(tc.modifyCity("Madrid", id, "Almeria", "Almería", 33.2224, 24.5555, "CB", 244));
        System.out.println("OK");
        
        System.out.print("\tChecking that the old city doesn't exist...");
        assertTrue(tc.getCitiesByName("Madrid").isEmpty());
        System.out.println("OK");
        
        System.out.print("\tChecking that the new city does exist...");
        assertFalse(tc.getCitiesByName("Almería").isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testModifyNonExistingCity() {
        System.out.println("Testing modifyCity for an unexisting city...");

        System.out.print("\tChecking that modifyCity(invalid_id, *valid_data) returns False...");
        assertFalse(tc.modifyCity("Madrid", "3000", "Almeria", "Almería", 33.2224, 24.5555, "CA", 877));
        System.out.println("OK");

        System.out.print("\tChecking that the old city still exists...");
        assertFalse(tc.getCitiesByName("Madrid").isEmpty());
        System.out.println("OK");

        System.out.print("\tChecking that the new city doesn't exist...");
        assertTrue(tc.getCitiesByName("Almeria").isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testModifyExistingCityWrongData() {
        System.out.println("Testing modifyCity with wrong data...");

        String id = tc.getCitiesByName("Madrid").get(0).get("id");
        System.out.print("\tChecking that modifyCity(valid_id, *invalid_data) returns False...");
        assertFalse(tc.modifyCity("Madrid", id, "Almeria", "Almería", 6000.00, 43.2, "CB", 189));
        System.out.println("OK");

        System.out.print("\tChecking that the old city still exists...");
        assertFalse(tc.getCitiesByName("Madrid").isEmpty());
        System.out.println("OK");

        System.out.print("\tChecking that the new city doesn't exist...");
        assertTrue(tc.getCitiesByName("Almeria").isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testDeleteCity() {
        System.out.println("Testing deleteExistentCity...");

        String id = tc.getCitiesByName("Madrid").get(0).get("id");
        System.out.print("\tChecking that deleteCity(valid_name, valid_id) returns True...");
        assertTrue(tc.deleteCity("Madrid", id));
        System.out.println("OK");
    
        System.out.print("\tChecking that the deleted city doesn't exist...");
        assertTrue(tc.getCitiesByName("Almeria").isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testDeleteUnexistentCity() {
        System.out.println("Testing deleteUnexistentCity...");

        System.out.print("\tChecking that deleteCity(valid_name, invalid_id) returns True...");
        assertFalse(tc.deleteCity("Madrid", "30024"));
        System.out.println("OK");

        String id = tc.getCitiesByName("Madrid").get(0).get("id");
        System.out.print("\tChecking that deleteCity(valid_name, invalid_id) returns True...");
        assertFalse(tc.deleteCity("Madrida", id));
        System.out.println("OK");
    }
}