import junit.framework.TestCase;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CityTest extends TestCase {

    TypeToponym tt;
    City c1, c2, c3;
    Zone zone;
    Country country;

    @Before
    public void setUp() throws Exception {
        tt = new TypeToponym("name","cat","code");
        c1 = new City("name1","UTFname1",2,2,tt, 3000);
        c2 = new City("name2","UTFname2",2,2,tt, 5000);
        country = new Country("Spain");
        zone = new Zone(country);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSetGetPopulation() {
        System.out.println("Testing set/getPopulation() method...");

        System.out.print("\tgetPopulation() returns the correct int...");
        assertEquals(3000, c1.getPopulation());
        System.out.println("OK");

        System.out.print("\tsetPopulation() gives new population and getPopulation() returns it...");
        c2.setPopulation(1000);
        assertEquals(1000, c2.getPopulation());
        System.out.println("OK");
    }

    @Test
    public void testGetSetZone() {
        System.out.println("Testing set/getZone() method...");

        System.out.print("\tsetZone() sets a zone and getZone() returns it...");
        c2.setZone(zone);
        assertEquals(zone, c2.getZone());
        System.out.println("OK");
    }

}
