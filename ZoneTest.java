import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZoneTest {
	Country espana;
	Zone catalunya;
	BorderPoint bp1;

	@Before
	public void setUp() throws Exception {
		espana = new Country();
		catalunya = new Zone(espana);
		bp1 = new BorderPoint(1, 4, catalunya);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testZone() {
		System.out.println("Testing Zone()...");
		Zone zone = new Zone(espana);
		System.out.print("\tChecking new zone is not null...");
		assertNotNull(zone);
		System.out.println("OK");
		System.out
				.print("\tChecking country of the new zone is the one specified...");
		assertEquals(espana, zone.getCountry());
		System.out.println("OK");
		System.out
				.print("\tChecking border points list of the new zone is empty...");
		assertTrue(zone.getBorderpoints().isEmpty());
		System.out.println("OK");

	}

	@Test
	public void testGetId() {
		System.out.println("Testing GetId()...");
		System.out.print("\tChecking checking id not null..");
		assertNotNull(catalunya.getId());
		System.out.println("OK");
	}

	@Test
	public void testGetCountry() {
		System.out.println("Testing GetCountr()...");
		System.out.print("\tChecking Country is not null..");
		assertNotNull(catalunya.getId());
		System.out.println("OK");
		System.out
				.print("\tChecking getCountry is the same than expected country..");
		assertEquals(catalunya.getCountry(), espana);
		System.out.println("OK");
	}

	@Test
	public void testAddBorderPoint() throws IllegalArgumentException {
		System.out.println("Testing AddBorderPoint()...");

		System.out
				.print("\tChecking BorderPoints list is not empty afeter adding a bp...");
		for (int i = 0; i < 10; i++) {
			catalunya.addBorderPoint(new BorderPoint(i, i, catalunya), i);
		}
		assertFalse(catalunya.getBorderpoints().isEmpty());
		System.out.println("OK");

		System.out
				.print("\tChecking BorderPoint is added in the given index...");
		catalunya.addBorderPoint(bp1, 4);
		assertEquals(catalunya.getBorderpoint(4), bp1);
		System.out.println("OK");

		System.out
				.print("\tChecking the new number of borderpoints of the zone...");
		assertEquals(catalunya.getBorderpoints().size(), 11);
		System.out.println("OK");

		System.out
				.print("\tChecking addBorderPoint(null,index) must not change the state of the zone...");
		BorderPoint previousBp = catalunya.getBorderpoint(5);
		catalunya.addBorderPoint(null, 5);
		assertEquals(catalunya.getBorderpoint(5), previousBp);
		System.out.println("OK");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddBorderPointInvalidIndex() {
		System.out
				.print("\tChecking adding a point in invalid index throws a IllegalArgumentException...");
		try {
			catalunya.addBorderPoint(bp1, 5);
		} catch (IllegalArgumentException e) {
			System.out.println("OK");
			throw e;
		}
	}

	@Test
	public void testGetBorderpoints() {
		System.out.println("Testing GetBorderPoints()...");
		System.out.print("\tChecking the method returns a not empty list...");
		for (int i = 0; i < 10; i++) {
			catalunya.addBorderPoint(new BorderPoint(i, i, catalunya), i);
		}
		assertFalse(catalunya.getBorderpoints().isEmpty());
		System.out.println("OK");
		System.out.print("\tChecking the list size is the expected one...");
		assertEquals(catalunya.getBorderpoints().size(), 10);
		System.out.println("OK");
	}

	@Test
	public void testGetBorderpoint() {
		System.out.println("Testing GetBorderPoint()...");
		for (int i = 0; i < 10; i++) {
			catalunya.addBorderPoint(new BorderPoint(i, i, catalunya), i);
		}
		catalunya.addBorderPoint(bp1, 4);
		System.out.print("\tChecking the method returns the expected value...");
		assertEquals(bp1, catalunya.getBorderpoint(4));
		System.out.println("OK");

		System.out
				.print("\tChecking it returns null when the index is out of the array...");
		assertNull(catalunya.getBorderpoint(catalunya.getBorderpoints().size()));
		assertNull(catalunya.getBorderpoint(-1));
		System.out.println("OK");

	}

	@Test
	public void testRemoveBorderPointInt() {
		System.out.println("Testing RemoveBorderPointInt()...");
		for (int i = 0; i < 10; i++) {
			catalunya.addBorderPoint(new BorderPoint(i, i, catalunya), i);
		}
		catalunya.addBorderPoint(bp1, 4);
		System.out.print("\tChecking the method removes the expected point...");
		catalunya.removeBorderPoint(4);
		assertFalse(catalunya.getBorderpoints().contains(bp1));
		System.out.println("OK");
		
		System.out.print("\tChecking the new number of borderpoints...");
		assertEquals(catalunya.getBorderpoints().size(),10);
		System.out.println("OK");
	}

	@Test
	public void testRemoveBorderPointBorderPoint() {
		System.out.println("Testing RemoveBorderPointInt()...");
		for (int i = 0; i < 10; i++) {
			catalunya.addBorderPoint(new BorderPoint(i, i, catalunya), i);
		}
		catalunya.addBorderPoint(bp1, 4);
		System.out.print("\tChecking the method removes the expected point...");
		catalunya.removeBorderPoint(bp1);
		assertNotSame(bp1, catalunya.getBorderpoint(4));
		System.out.println("OK");
		System.out.print("\tChecking the new number of borderpoints...");
		assertEquals(catalunya.getBorderpoints().size(),10);
		System.out.println("OK");
		System.out.print("\tremoveBorderPoint(INVALID_POINT) mus return false...");
		assertFalse(catalunya.removeBorderPoint(new BorderPoint(0,0,catalunya)));
		System.out.println("OK");
		System.out.print("\tremoveBorderPoint(null) must return false...");
		assertFalse(catalunya.removeBorderPoint(null));
		System.out.println("OK");
	}
}
