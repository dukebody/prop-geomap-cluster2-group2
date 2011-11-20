import java.util.*;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class ToponymsControllerTest extends TestCase {

    private ToponymsController tc;

    @Before
    public void setUp() throws Exception {
        tc = new ToponymsController();

        tc.createTypeToponym("City A", "City", "CA");
        tc.createTypeToponym("City B", "City", "CB");

        tc.addToponym("Madrid", "Madrid", 34.3332, 12.4333, "CA");
        tc.addToponym("Barcelona", "Barcelona", 12.14, 1.2333, "CA");
        tc.addToponym("Norilandia", "Ñorilandia", 345.2, 46.24, "CA");
        tc.addToponym("Norilandia", "Norilandia", 346.2, 49.24, "CB");
        tc.addToponym("Mandril", "Mandril", 45.14, 22.563, "CB");
        tc.addToponym("Mandril", "Mandril", 45.18, 22.580, "CB");
    }

    @After
    public void tearDown() throws Exception {
    }

    // public static junit.framework.Test suite() { 
    //     return new JUnit4TestAdapter(ToponymsControllerTest.class); 
    // }

    @Test
    public void testgetMap() {
        System.out.println("Testing getMap for single toponym...");


        TypeToponym cityA = new TypeToponym("City A", "City", "CA");
        HashMap<String,String> map = tc.getMap(new Toponym("Madrid", "Madrid", 34.3332, 12.4333, cityA));
        assertEquals("Madrid", map.get("nameASCII"));
        assertEquals("Madrid", map.get("nameUTF"));
        assertEquals("34.3332", map.get("latitude"));
        assertEquals("12.4333", map.get("longitude"));
        assertEquals("CA", map.get("type"));
    }

    @Test
    public void testUniqueGetToponymsByName() {
        System.out.println("Testing getToponymByName for unique names...");

        List<HashMap<String,String>> toponyms = tc.getToponymsByName("Madrid");
        System.out.print("\tGetting an unique existing toponym by ascii name should return a one-toponym list with that name...");
        assertEquals("Madrid", toponyms.get(0).get("nameASCII"));
        assertEquals(1, toponyms.size());
        System.out.println("OK");

        List<HashMap<String,String>> nonexistentToponym = tc.getToponymsByName("NonExistent");
        System.out.print("\tGetting an non-existing toponym should return null...");
        assertTrue(nonexistentToponym.isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testSeveralGetToponymsByName() {
        System.out.println("Testing getToponymByName for names matching more than one element...");

        List<HashMap<String,String>> toponyms = tc.getToponymsByName("Mandril");
        System.out.print("\tGetting an non-unique existing toponym by ascii name should return a list with the toponyms matching that name...");
        assertEquals(2, toponyms.size());
        System.out.println("OK");
    }

    @Test
    public void testSeveralGetToponymByNameAndId() {
        System.out.println("Testing getToponymByName for names matching more than one element...");

        List<HashMap<String,String>> toponyms = tc.getToponymsByName("Mandril");
        HashMap<String,String> secondToponym = toponyms.get(1);
        String secondId = secondToponym.get("id");
        HashMap<String,String> target = tc.getToponymByNameAndId("Mandril", secondId);
        System.out.print("\tGetting an non-unique existing toponym by ascii name and id should return the toponyms with that name and id...");
        // The last inserted toponym is the last one by id in the associated Trie
        assertEquals(secondToponym.get("latitude"), target.get("latitude"));
        System.out.println("OK");
    }

    @Test
    public void testAddToponym() {
        System.out.println("Testing addToponym...");

        System.out.print("\tChecking that adding a toponym in a free location returns true...");
        assertTrue(tc.addToponym("Zaragoza", "Zaragoza", 45.19, 22.580, "CA"));
        System.out.println("OK");

        HashMap<String,String> zaragoza = tc.getToponymsByName("Zaragoza").get(0);
        System.out.print("\tChecking that it was really added...");
        assertEquals("Zaragoza", zaragoza.get("nameASCII"));
        System.out.println("OK");
    }

    @Test
    public void testAddToponymOnExistingOneFails() {
        System.out.println("Testing addToponym on top on an existing one (location-wise)...");

        System.out.print("\tChecking that this returns false...");
        assertFalse(tc.addToponym("Zaragoza", "Zaragoza", 12.14, 1.2333, "CA"));
        System.out.println("OK");

        HashMap<String,String> barcelona = tc.getToponymsByName("Barcelona").get(0);
        System.out.print("\tChecking that the old toponym remains at its place...");
        assertEquals("12.14", barcelona.get("latitude"));
        System.out.println("OK");
    }

    @Test
    public void testModifyToponym() {
        System.out.println("Testing modifyToponym with an existing toponym...");

        String id = tc.getToponymsByName("Madrid").get(0).get("id");
        System.out.print("\tChecking that modifyToponym(valid_id, *valid_data) returns True...");
        assertTrue(tc.modifyToponym("Madrid", id, "Almeria", "Almería", 33.2224, 24.5555, "CB"));
        System.out.println("OK");
        
        System.out.print("\tChecking that the old toponym doesn't exist...");
        assertTrue(tc.getToponymsByName("Madrid").isEmpty());
        System.out.println("OK");
        
        System.out.print("\tChecking that the new toponym does exist...");
        assertFalse(tc.getToponymsByName("Almeria").isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testModifyNonExistingToponym() {
        System.out.println("Testing modifyToponym for an unexisting toponym...");

        System.out.print("\tChecking that modifyToponym(invalid_id, *valid_data) returns False...");
        assertFalse(tc.modifyToponym("Madrid", "3000", "Almeria", "Almería", 33.2224, 24.5555, "CA"));
        System.out.println("OK");

        System.out.print("\tChecking that the old toponym still exists...");
        assertFalse(tc.getToponymsByName("Madrid").isEmpty());
        System.out.println("OK");

        System.out.print("\tChecking that the new toponym doesn't exist...");
        assertTrue(tc.getToponymsByName("Almeria").isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testModifyExistingToponymWrongData() {
        System.out.println("Testing modifyToponym with wrong data...");

        String id = tc.getToponymsByName("Madrid").get(0).get("id");
        System.out.print("\tChecking that modifyToponym(valid_id, *invalid_data) returns False...");
        assertFalse(tc.modifyToponym("Madrid", id, "Almeria", "Almería", 6000.00, 43.2, "CB"));
        System.out.println("OK");

        System.out.print("\tChecking that the old toponym still exists...");
        assertFalse(tc.getToponymsByName("Madrid").isEmpty());
        System.out.println("OK");

        System.out.print("\tChecking that the new toponym doesn't exist...");
        assertTrue(tc.getToponymsByName("Almeria").isEmpty());
        System.out.println("OK");
    }
}