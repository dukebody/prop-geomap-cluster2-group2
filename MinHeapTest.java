import junit.framework.TestCase;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MinHeapTest extends TestCase {

    MinHeap<Integer> mh;

    @Before
    public void setUp() throws Exception {
        mh = new MinHeap<Integer>();
        mh.add(6);
        mh.add(4);
        mh.add(3);
        mh.add(88);
        mh.add(34);
        mh.add(96);
        mh.add(34);
        mh.add(53);
        mh.add(73);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPeek() {
        System.out.print("Checking that peek() returns the smallest element...");
        assertEquals(3, mh.peek());
        System.out.println("OK");
    }

    @Test
    public void testRemove() {
        System.out.print("Checking that remove() returns the smallest element...");
        assertEquals(3, mh.remove());
        System.out.println("OK");

        System.out.print("Checking that after remove() the next smallest element is the expected one...");
        assertEquals(4, mh.peek());
        System.out.println("OK");
    }

    @Test
    public void testSize() {
        System.out.print("Checking that size() returns the correct number of elements in the heap...");
        assertEquals(9, mh.size());
        System.out.println("OK");
    }
}
