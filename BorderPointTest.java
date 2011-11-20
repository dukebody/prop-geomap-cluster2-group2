import junit.framework.TestCase;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BorderPointTest extends TestCase{

	BorderPoint bp, empty_bp;
	Zone z1, z2, z3;
	Country c1;

	@Before
	public void setUp() throws Exception {
		c1 = new Country("country1");
		z1 = new Zone(c1);
		z2 = new Zone(c1);
		z3 = new Zone(c1);
		empty_bp = new BorderPoint(1,2);
		bp = new BorderPoint(1,2,z1);
		bp.addZone(z2);
		bp.addZone(z3);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBorderPoint() {
		System.out.println("Testing BorderPoint()...");
		System.out.print("\tConstructor 1 does not return null...");
		assertNotNull("The constructor of a BorderPoint returns null",new BorderPoint(2,2));
		System.out.println("OK");

		System.out.print("\tConstructor 2 does not return null...");
		assertNotNull("The constructor of a BorderPoint returns null",new BorderPoint(2,2,z1));
		System.out.println("OK");
	}

		@Test
	public void testAddZone() {
		System.out.println("Testing addZone() method...");

		System.out.print("\taddZone(null) must return false ...");
		assertFalse(empty_bp.addZone(null));
		System.out.println("OK");

		System.out.print("\taddZone(zone) must return true...");
		assertTrue(empty_bp.addZone(z1));
		System.out.println("OK");

		System.out.print("\taddZone(zone) must return true (multiple additions)...");
		assertTrue(empty_bp.addZone(z2));
		assertTrue(empty_bp.addZone(z3));
		System.out.println("OK");

		System.out.print("\tThe number of added zones are correct...");
		assertEquals("Different elements",empty_bp.getZones().size(),3);
		System.out.println("OK");

		System.out.print("\tThe zones in the BorderPoint are the added ones...");
		assertEquals("Different elements",empty_bp.getZones().get(0),z1);
		assertEquals("Different elements",empty_bp.getZones().get(1),z2);
		assertEquals("Different elements",empty_bp.getZones().get(2),z3);
		System.out.println("OK");
	}

	@Test
	public void testGetZones() {
		System.out.println("Testing getZones() method...");

		System.out.print("\tgetZones() must return a list with the inserted zones...");
		assertEquals("Different elements",bp.getZones().size(),3);
		assertEquals("Different elements",bp.getZones().get(0),z1);
		assertEquals("Different elements",bp.getZones().get(1),z2);
		assertEquals("Different elements",bp.getZones().get(2),z3);
		System.out.println("OK");

	}

	@Test
	public void testGetZonesIterator() {
		System.out.println("Testing getZonesIterator() method...");

		System.out.print("\tgetZonesIterator() must return a Iterator with the inserted zones...");
		Iterator it=bp.getZonesIterator();
		assertTrue(it.hasNext());
		assertEquals("Different elements",it.next(),z1);
		assertTrue(it.hasNext());
		assertEquals("Different elements",it.next(),z2);
		assertTrue(it.hasNext());
		assertEquals("Different elements",it.next(),z3);
		assertFalse(it.hasNext());
		System.out.println("OK");
	}

	@Test
	public void testRemovetZone() {
		System.out.println("Testing removeZone() method...");

		System.out.print("\tremoveZone(zone) of an existent zone...");
		assertTrue(bp.removeZone(z1));
		assertTrue(bp.removeZone(z2));
		assertTrue(bp.removeZone(z3));
		System.out.println("OK");

		System.out.print("\tremoveZone(zone) of an inexistent zone...");
		assertFalse(bp.removeZone(z1));
		assertFalse(bp.removeZone(z2));
		assertFalse(bp.removeZone(z3));
		System.out.println("OK");

	}


}
