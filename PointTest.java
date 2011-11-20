import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PointTest {
	Point libertyStatue;
	Point eiffelTower;
	double distance;
	@Before
	public void setUp() throws Exception {
		this.libertyStatue= new Point(40.6892, -74.0444);  //liberty statue
		this.eiffelTower= new Point(48.8583, 2.2945); // eiffel tower
		this.distance = 5843.95;
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test(expected=IllegalArgumentException.class)
	public void testPoint1() throws IllegalArgumentException{
		System.out.println("Testing constructor Point()");
		System.out.println("---------------------------");
		System.out.print("Checking illegal latitude(latitude <90º) throws IllegalArgumentException...");
		try {
			@SuppressWarnings("unused")
			Point p1 = new Point (95,45);
		}
		catch (IllegalArgumentException e) {
			System.out.println("OK");
			throw e;
		}
	}
	@Test(expected=IllegalArgumentException.class)
	public void testPoint2() throws IllegalArgumentException{
		
		System.out.print("Checking illegal latitude(latitude <90º) throws IllegalArgumentException...");
		try {
			@SuppressWarnings("unused")		
			Point p1 = new Point (-95,45);
		}
		catch (IllegalArgumentException e) {
			System.out.println("OK");
			throw e;
		}
	}
	@Test(expected=IllegalArgumentException.class)
	public void testPoint3() throws IllegalArgumentException{
		System.out.print("Checking illegal latitude(latitude > 180º) throws IllegalArgumentException...");
		try {
		@SuppressWarnings("unused")
		Point p1 = new Point (45,185);
		}
		catch (IllegalArgumentException e) {
			System.out.println("OK");
			throw e;
		}
	}
	@Test(expected=IllegalArgumentException.class)
	public void testPoint4() throws IllegalArgumentException{
		System.out.print("Checking illegal latitude(latitude < 180º) throws IllegalArgumentException...");
		try {
		@SuppressWarnings("unused")
		Point p1 = new Point (45,-185);
		}
		catch (IllegalArgumentException e) {
			System.out.println("OK");
			throw e;
		}
	}

	@Test
	public void testGetLinearDistanceTo() {
		double distance = this.libertyStatue.getLinearDistanceTo(this.eiffelTower);
		System.out.println("Testing Point functions)");
		System.out.println("---------------------------");
		System.out.print("Checking GetLinearDistanceTo():\n from libertyStatue("+libertyStatue.toString()+") \n to eiffelTower("+eiffelTower.toString()+")\n Expected distance:"+this.distance+"km --> Caluclated distance = "+distance +"km...");
		if ((distance>=this.distance)&&(distance<=this.distance+0.01))
			System.out.println("OK");
		else
			System.out.println("FAIL- "+distance);
		assertTrue((distance>=this.distance)&&(distance<=this.distance+0.01));
	}

}
