import junit.framework.TestCase;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ToponymTest extends TestCase{

	TypeToponym tt;
	Toponym t1, t2, t3;

	@Before
	public void setUp() throws Exception {
		tt = new TypeToponym("name","cat","code");
		t1 = new Toponym("name1","UTFname1",2,2);
		t2 = new Toponym("name2","UTFname2",2,2,tt);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToponym() {
		System.out.println("Testing Toponym()...");
		System.out.print("\tConstructor 1 does not return null...");
		assertNotNull("The constructor of a BorderPoint returns null",new Toponym("nameASCII","nameUTF",2,2,tt));
		System.out.println("OK");

		System.out.print("\tConstructor 2 does not return null...");
		assertNotNull("The constructor of a BorderPoint returns null",new Toponym("nameASCII","nameUTF",2,2));
		System.out.println("OK");
	}

	@Test
	public void testSetGetNameASCII() {
		System.out.println("Testing set/getNameASCII() method...");

		System.out.print("\tgetNameASCII() returns the correct string...");
		assertEquals("Different names",t1.getNameASCII(),"name1");
		System.out.println("OK");

		System.out.print("\tsetNameASCII() gives new name and getNameASCII() returns it...");
		t2.setNameASCII("name3");
		assertEquals("Different names",t2.getNameASCII(),"name3");
		System.out.println("OK");
	}

	@Test
	public void testSetGetNameUTF() {
		System.out.println("Testing set/getNameUTF() method...");

		System.out.print("\tgetNameUTF() returns the correct string...");
		assertEquals("Different names",t1.getNameUTF(),"UTFname1");
		System.out.println("OK");

		System.out.print("\tsetNameUTF() gives new name and getNameUTF() returns it...");
		t2.setNameUTF("UTFname3");
		assertEquals("Different names",t2.getNameUTF(),"UTFname3");
		System.out.println("OK");
	}

	@Test
	public void testSetGetType() {
		System.out.println("Testing set/getTypeToponym() method...");

		System.out.print("\tgetTypeToponym() returns the correct type...");
		assertEquals("Different types",t2.getType(),tt);
		System.out.println("OK");

		System.out.print("\tgetTypeToponym() with a toponym without type returns null...");
		assertNull(t1.getType());
		System.out.println("OK");

		System.out.print("\tsetTypeToponym() gives new type and getTypeToponym() returns it...");
		t1.setType(tt);
		assertEquals("Different types",t1.getType(),tt);
		System.out.println("OK");
	}

	@Test
	public void testGetID() {
		System.out.println("Testing getId() method...");

		System.out.print("\tEach getId() returns a different id ...");
		assertTrue(t1.getId()!=t2.getId());
		System.out.println("OK");

	}


}
