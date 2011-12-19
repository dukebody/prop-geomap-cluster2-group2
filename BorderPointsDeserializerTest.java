import java.io.*;
import java.lang.*;
import java.util.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BorderPointsDeserializerTest extends TestCase {

    private BorderPointsDeserializer bpd;
    private CountryController cc;
    private ZonesController zc;

    @Before
    public void setUp() throws Exception {
        DataStorage ds = new DataStorage();
        cc = new CountryController(ds);
        zc = new ZonesController(ds);

        File f = new File("data/Fronteres/test/Fronteres_A.txt");
        BordersFileParser parser = new BordersFileParser(f);
        Iterator<HashMap<String,String>> itr = parser.getIterator();

        bpd = new BorderPointsDeserializer(itr, cc, zc);
        bpd.generate();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTotalNCountries() {
        Iterator itr = cc.getAllCountriesIterator();
        int n = 0;
        while (itr.hasNext()) {
            itr.next();
            n++;
        }
        System.out.print("Checking that there are 12 countries in total...");
        assertEquals(12, n);
        System.out.println("OK");
    }

    @Test
    public void testNCountriesStartingWithA() {
        Iterator itr = cc.getPrefixCountriesIterator("A");
        int n = 0;
        while (itr.hasNext()) {
            itr.next();
            n++;
        }
        System.out.print("Checking that there are 15 countries starting with A...");
        assertEquals(12, n);
        System.out.println("OK");
    }

    @Test
    public void testAandBHasTwoAreas() {
        Country country = cc.getRawCountry("Antigua_and_Barbuda"); // XXX: This should be done by the zones controller!
        ArrayList<Zone> zones = country.getZones();
        System.out.print("Check that Antigua and Barbuda has 2 areas as expected...");
        assertEquals(2, zones.size());
        System.out.println("OK");
    }

    @Test
    public void testAandBHasCorrectAreas() {
        Country country = cc.getRawCountry("Antigua_and_Barbuda"); // XXX: This should be done by the zones controller!
        ArrayList<Zone> zones = country.getZones();
        Zone z1 = zones.get(0);
        Zone z2 = zones.get(1);
        List<BorderPoint> bps1 = z1.getBorderpoints();
        List<BorderPoint> bps2 = z2.getBorderpoints();

        System.out.print("Check that Antigua and Barbuda has the correct number of points in each area...");
        assertEquals(25, bps1.size());
        assertEquals(23, bps2.size());
        System.out.println("OK");

        BorderPoint bp1i = bps1.get(0);
        BorderPoint bp2i = bps2.get(0);
        BorderPoint bp1f = bps1.get(bps1.size()-1);
        BorderPoint bp2f = bps2.get(bps2.size()-1);
        System.out.print("Check that each zone of A and B has the correct initial and final bps...");
        assertEquals(new Double("17.54055400"), bp1i.getLatitude());
        assertEquals(new Double("16.98971900"), bp2i.getLatitude());
        assertEquals(new Double("17.54055400"), bp1f.getLatitude());
        assertEquals(new Double("16.98971900"), bp2f.getLatitude());
        System.out.println("OK");
    }

    @Test
    public void testArubaHas19Points() {
        Country country = cc.getRawCountry("Aruba"); // XXX: This should be done by the zones controller!
        Zone zone = country.getZones().get(0);
        System.out.print("Check that check that Aruba has 19 border points as expected...");
        assertEquals(19, zone.getBorderpoints().size());
        System.out.println("OK");
    }
}