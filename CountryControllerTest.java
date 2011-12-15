import java.util.*;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class CountryControllerTest extends TestCase {

    private CountryController cc;
    
    @Before
    public void setUp() throws Exception {
        DataStorage ds = new DataStorage();
        cc = new CountryController(ds);
        cc.addCountry("Spain", "ES");
        cc.addCountry("France", "FR");
        cc.addCountry("Germany", "GE");
        cc.addCountry("China", "CH");
        cc.addCountry("Italy", "IT");
        cc.addCountry("Morocco", "MO");
        cc.addCountry("Bulgary", "BU");
        cc.addCountry("Sweden", "SW");
        cc.addCountry("Finland", "FI");
        cc.addCountry("Norway", "NW");
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetCountry() {
        System.out.println("Testing getCountry...");

        HashMap<String,String> spain = cc.getCountry("Spain");
        System.out.print("\tGetting an existing country should return a country with that name...");
        assertEquals("Spain", spain.get("name"));
        System.out.println("OK");

        HashMap<String,String> nonexistentCountry = cc.getCountry("NonExistent");
        System.out.print("\tGetting an non-existing country should return null...");
        assertNull(nonexistentCountry);
        System.out.println("OK");
    }

    @Test
    public void testGetCountriesIterator() {
        System.out.println("Testing getAllCountriesIterator and getPrefixCountriesIterator...");

        Iterator<HashMap<String,String>> itr = cc.getAllCountriesIterator();
        int count = 0;
        while (itr.hasNext()) {
            itr.next();
            count++;
        }
        System.out.print("\tTesting that the all-countries iterator returns all the existent countries...");
        assertEquals(10, count);
        System.out.println("OK");

        itr = cc.getPrefixCountriesIterator("S");
        System.out.print("\tTesting that the prefix-countries iterator returns all the existent countries with the specified prefix...");
        while (itr.hasNext()) {
            String name = itr.next().get("name");
            assertTrue(name == "Sweden" || name == "Spain");
        }
        System.out.println("OK");
    }

    @Test
    public void addCountry() {
        System.out.println("Testing addCountry...");

        System.out.print("\tTesting that adding a country with an already registered name returns False...");
        assertFalse(cc.addCountry("Spain", "SA"));
        System.out.println("OK");

        System.out.print("\tTesting that adding a country with an empty name returns False...");
        assertFalse(cc.addCountry("", "IO"));
        System.out.println("OK");
    }

    @Test
    public void testDeleteCountry() {
        System.out.println("Testing removeCountry...");

        System.out.print("\tTesting that removing an existent country returns True...");
        assertTrue(cc.removeCountry("Spain"));
        System.out.println("OK");

        System.out.print("\tTesting that the removal actually worked...");
        assertNull(cc.getCountry("Spain"));
        System.out.println("OK");

        System.out.print("\tTesting that removing a unexisting country returns False...");
        assertFalse(cc.removeCountry("Unexistent"));
        System.out.println("OK");
    }

    @Test
    public void testModifyCountry() {
        System.out.println("Testing modifyCountry...");

        System.out.print("\tTesting that modifying an existent country returns True...");
        assertTrue(cc.modifyCountry("Spain", "Singapur", "SI"));
        System.out.println("OK");

        System.out.print("\tTesting that the modification actually worked...");
        assertNull(cc.getCountry("Spain"));
        assertNotNull(cc.getCountry("Singapur"));
        assertEquals("Singapur", cc.getCountry("Singapur").get("name"));
        System.out.println("OK");

        System.out.print("\tTesting that modifying a non-existent country returns False...");
        assertFalse(cc.modifyCountry("Japan", "OMG", "OMGA"));
        System.out.println("OK");

        System.out.print("\tTesting that modifying an existing country with a null name returns False...");
        assertFalse(cc.modifyCountry("Italy", null, "IT"));
        System.out.println("OK");

        System.out.print("\tTesting that modifying an existing country with a zero-length name returns False...");
        assertFalse(cc.modifyCountry("Italy", "", "IT"));
        System.out.println("OK");

        System.out.print("\tTesting that the country wasn't modified...");
        assertEquals("Italy", cc.getCountry("Italy").get("name"));
        System.out.println("OK");

        System.out.print("\tTesting that trying to change the name of the country to an existing one returns False...");
        assertFalse(cc.modifyCountry("Sweden", "Bulgary", "BU"));
        System.out.println("OK");

        System.out.print("\tTesting that the affected countries weren't modified...");
        assertEquals("Sweden", cc.getCountry("Sweden").get("name"));
        assertEquals("Bulgary", cc.getCountry("Bulgary").get("name"));
        System.out.println("OK");
    }

}