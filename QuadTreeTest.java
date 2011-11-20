import java.util.*;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class QuadTreeTest extends TestCase {

    private QuadTree<Integer,String> qt;

    @Before
    public void setUp() throws Exception {
        qt = new QuadTree<Integer,String>();

        qt.insert(0, 0, "P1");
        qt.insert(1, 1, "P2");
        qt.insert(4, 5, "P3");
        qt.insert(7, 2, "P4");
        qt.insert(-4, -4, "P5");
        qt.insert(3, -5, "P6");
        qt.insert(3, 2, "P7");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetExactPoint() {
        System.out.println("Testing query(x, y)...");

        System.out.print("\tChecking that query(x, y) gets an existing point...");
        assertNotNull(qt.query(7, 2));
        System.out.println("OK");

        System.out.print("\tChecking that query(x, y) returns null for a non-existing point...");
        assertNotNull(qt.query(7, 3));
        System.out.println("OK");
    }

    @Test
    public void testGetRange() {
        System.out.println("Testing query2D(rect)...");

        Interval<Integer> intX = new Interval(-1,1);
        Interval<Integer> intY = new Interval(-1,1);
        Interval2D<Integer> rect = new Interval2D(intX, intY);
        System.out.print("\tChecking that query2D(rect) gets overlapping points...");
        assertEquals(2, qt.query2D(rect).size());
        System.out.println("OK");

        intX = new Interval(9,10);
        intY = new Interval(9,10);
        rect = new Interval2D(intX, intY);
        System.out.print("\tChecking that query2D(rect) returns an empty list of points when there isn't any...");
        assertTrue(qt.query2D(rect).isEmpty());
        System.out.println("OK");
    }

    @Test
    public void testInsert() {
        System.out.println("Testing insert(x, y, value)...");

        qt.insert(0, 0, "P7");
        System.out.print("\tChecking that insert(x, y, value) doesn't update the existing node if there's already one at that place...");
        assertEquals("P1", qt.query(0, 0).value);
        System.out.println("OK");
    }

    @Test
    public void testRemove() {
        System.out.println("Testing remove(x, y)...");

        qt.remove(0, 0);
        System.out.print("\tChecking that remove(x, y) deletes existing nodes successfully...");
        assertNull(qt.query(0, 0));
        System.out.println("OK");

        System.out.print("\tChecking that remove(x, y) doesn't fail if trying to delete an unexistent node...");
        qt.remove(7, 25);
        System.out.println("OK");
    }
}
