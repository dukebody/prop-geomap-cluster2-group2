import java.util.*;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class CountryControllerTest extends TestCase{

    private CountryController cc;
    private ArrayList<String> elements;
    
    @Before
    public void setUp() throws Exception {
        cc = new CountryController();
        elements = new ArrayList<String>(10);
        elements.add("Spain");
        elements.add("France");
        elements.add("Germany");
        elements.add("China");
        elements.add("Italy");
        elements.add("Morocco");
        elements.add("Bulgary");
        elements.add("Sweden");
        elements.add("Finland");
        elements.add("Norway");
        
        for(String e : elements)
            cc.addCountry(e);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetCountry() {
        System.out.println("Testing getCountry...");

        Country spain = cc.getCountry("Spain");
        System.out.print("\tGetting an existing country should return a country with that name...");
        assertEquals("Spain", spain.getName());
        System.out.println("OK");

        Country nonexistentCountry = cc.getCountry("NonExistent");
        System.out.print("\tGetting an non-existing country should return null...");
        assertNull(nonexistentCountry);
        System.out.println("OK");
    }

    @Test
    public void testGetCountriesIterator() {
        System.out.println("Testing getAllCountriesIterator and getPrefixCountriesIterator...");

        Iterator<Country> itr = cc.getAllCountriesIterator();
        System.out.print("\tTesting that the all-countries iterator returns all the existent countries...");
        while (itr.hasNext()) {
            assertTrue(elements.contains(itr.next().getName()));
        }
        System.out.println("OK");

        itr = cc.getPrefixCountriesIterator("S");
        System.out.print("\tTesting that the prefix-countries iterator returns all the existent countries with the specified prefix...");
        while (itr.hasNext()) {
            String name = itr.next().getName();
            assertTrue(name == "Sweden" | name == "Spain");
        }
        System.out.println("OK");
    }

    @Test
    public void addCountry() {
        System.out.println("Testing addCountry...");

        System.out.print("\tTesting that adding a country with an already registered name returns False...");
        assertFalse(cc.addCountry("Spain"));
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
        assertTrue(cc.modifyCountry("Spain", "Singapur"));
        System.out.println("OK");

        System.out.print("\tTesting that the modification actually worked...");
        assertNull(cc.getCountry("Spain"));
        assertNotNull(cc.getCountry("Singapur"));
        System.out.println("OK");

        System.out.print("\tTesting that modifying a non-existent country returns False...");
        assertFalse(cc.modifyCountry("Japan", "OMG"));
        System.out.println("OK");

        System.out.print("\tTesting that modifying an existing country with a null name returns False...");
        assertFalse(cc.modifyCountry("Italy", null));
        System.out.println("OK");

        System.out.print("\tTesting that modifying an existing country with a zero-length name returns False...");
        assertFalse(cc.modifyCountry("Italy", ""));
        System.out.println("OK");

        System.out.print("\tTesting that the country wasn't modified...");
        assertEquals("Italy", cc.getCountry("Italy").getName());
        System.out.println("OK");

        System.out.print("\tTesting that trying to change the name of the country to an existing one returns False...");
        assertFalse(cc.modifyCountry("Sweden", "Bulgary"));
        System.out.println("OK");

        System.out.print("\tTesting that the affected countries weren't modified...");
        assertEquals("Sweden", cc.getCountry("Sweden").getName());
        assertEquals("Bulgary", cc.getCountry("Bulgary").getName());
        System.out.println("OK");
    }

    @Test
    public void testGetZonesController() {
        System.out.println("Testing getZonesController...");

        System.out.print("\tTesting that getting the zone controller for an existing country is not null...");
        assertNotNull(cc.getZonesController("Spain"));
        System.out.println("OK");

        System.out.print("\tTesting that getting the zone controller for a non-existing country returns null...");
        assertNull(cc.getZonesController("NonExisting"));
        System.out.println("OK");
    }
}