import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TrieTest extends TestCase{

	private Trie<Toponym> trie;
	private ArrayList<Toponym> elements;
	
	@Before
	public void setUp() throws Exception {
		trie = new Trie<Toponym>();
		elements = new ArrayList<Toponym>(10) ;
		elements.add(0,new Toponym("Murcia","Murcia",0,0,null));
		elements.add(1,new Toponym("Barcelona","Barcelona",0,0,null));
		elements.add(2,new Toponym("Barcelona","Barcelona",0,0,null));
		elements.add(3,new Toponym("Barceloneta","Barceloneta",0,0,null));
		elements.add(4,new Toponym("Barceloneta","Barceloneta",0,0,null));
		elements.add(5,new Toponym("Castellón","Castellón",0,0,null));
		elements.add(6,new Toponym("Castellón de la Plana","Castellón de la Plana",0,0,null));
		elements.add(7,new Toponym("Corea del Norte","Corea del Norte",0,0,null));
		elements.add(8,new Toponym("Corea del Norte","Corea del Norte",0,0,null));
		elements.add(9,new Toponym("Corea del Sur","Corea del Sur",0,0,null));
		
		for(Toponym e : elements)
			trie.put(e,e.getNameUTF());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTrie() {
		System.out.println("Testing Trie()...");
		System.out.print("\tConstructor does not return null...");
		assertNotNull("The constructor of a Trie returns null",new Trie<Toponym>());
		System.out.println("OK");
		
		System.out.print("\tConstructor returns an empty trie...");
		assertFalse("The new Trie is not empty",(new Trie<Toponym>()).iterator().hasNext());
		System.out.println("OK");
	}

	@Test
	public void testPut() {
		System.out.println("Testing put(G,String)...");
		ArrayList<Toponym> toponymsGotten = new ArrayList<Toponym>(10);
		
		System.out.print("\tput(null,null) must return false...");
		assertFalse(trie.put(null, null));
		System.out.println("OK");
		
		System.out.print("\tput(null,\"\") must return false...");
		assertFalse(trie.put(null, ""));
		System.out.println("OK");
		
		System.out.print("\tput(null,INVALID_KEY) must return false...");
		assertFalse(trie.put(null, "INVALID_KEY"));
		System.out.println("OK");
		
		System.out.print("\tput(Object,null) must return false...");
		assertFalse(trie.put(new Toponym("TOPONYM","TOPONYM",0,0,null),null));
		System.out.println("OK");
		
		System.out.print("\tput(Object,\"\") must return false...");
		assertFalse(trie.put(new Toponym("TOPONYM","TOPONYM",0,0,null),""));
		System.out.println("OK");
		
		Iterator<Toponym> it=trie.iterator();
		while(it.hasNext()) {
			toponymsGotten.add(it.next());
		}
		System.out.print("\tTesting if there are 10 elements into the trie...");
		assertEquals("There are different number of elements into the trie",toponymsGotten.size(),elements.size());
		System.out.println("OK");
		
		System.out.print("\tThe elements into the trie are the same than has been inserted...");
		assertTrue("Trie has not exactly the same elements as has been inserted",toponymsGotten.containsAll(elements));
		System.out.println("OK");
	}

	@Test
	public void testGetList() {
		System.out.println("Testing getList(String)...");
		List<Toponym> toponymsGotten;
		
		System.out.print("\tChecking elements indexed for \"Murcia\"...");
		toponymsGotten=trie.getList("Murcia");
		assertTrue(toponymsGotten.size()==1 && toponymsGotten.containsAll(elements.subList(0,0)));
		System.out.println("OK");
		
		System.out.print("\tChecking elements indexed for \"Barcelona\"...");
		toponymsGotten=trie.getList("Barcelona");
		assertTrue(toponymsGotten.size()==2 && toponymsGotten.containsAll(elements.subList(1,2)));
		System.out.println("OK");
		
		System.out.print("\tChecking elements indexed for \"Barceloneta\"...");
		toponymsGotten=trie.getList("Barceloneta");
		assertTrue(toponymsGotten.size()==2 && toponymsGotten.containsAll(elements.subList(3,4)));
		System.out.println("OK");
		
		System.out.print("\tChecking elements indexed for \"Castellón\"...");
		toponymsGotten=trie.getList("Castellón");
		assertTrue(toponymsGotten.size()==1 && toponymsGotten.containsAll(elements.subList(5,5)));
		System.out.println("OK");
		
		System.out.print("\tChecking elements indexed for \"Castellón de la Plana\"...");
		toponymsGotten=trie.getList("Castellón de la Plana");
		assertTrue(toponymsGotten.size()==1 && toponymsGotten.containsAll(elements.subList(6,6)));
		System.out.println("OK");
		
		System.out.print("\tChecking elements indexed for \"Corea del Norte\"...");
		toponymsGotten=trie.getList("Corea del Norte");
		assertTrue(toponymsGotten.size()==2 && toponymsGotten.containsAll(elements.subList(7,8)));
		System.out.println("OK");
		
		System.out.print("\tChecking elements indexed for \"Corea del Norte\"...");
		toponymsGotten=trie.getList("Corea del Sur");
		assertTrue(toponymsGotten.size()==1 && toponymsGotten.containsAll(elements.subList(9,9)));
		System.out.println("OK");
	}

	@Test
	public void testGetStringString() {
		System.out.println("Testing get(String,String)...");
		
		System.out.print("\tget(null,null) must return null...");
		assertNull(trie.get(null,null));
		System.out.println("OK");
		
		System.out.print("\tget(null,\"\") must return null...");
		assertNull(trie.get(null,""));
		System.out.println("OK");
		
		System.out.print("\tget(\"\",null) must return null...");
		assertNull(trie.get("",null));
		System.out.println("OK");
		
		System.out.print("\tget(\"\",\"\") must return null...");
		assertNull(trie.get("",""));
		System.out.println("OK");
		
		System.out.print("\tget(\"INVALID NAME\",null) must return null...");
		assertNull(trie.get("INVALID_NAME",null));
		System.out.println("OK");
		
		System.out.print("\tget(null,\"INVALID_ID\") must return null...");
		assertNull(trie.get(null,"INVALID_ID"));
		System.out.println("OK");
		
		System.out.print("\tget(\"INVALID_NAME\",\"INVALID_ID\") must return null...");
		assertNull(trie.get("INVALID_NAME","INVALID_ID"));
		System.out.println("OK");
		
		System.out.print("\tget(VALID_NAME,\"\") must return null...");
		assertNull(trie.get(elements.get(2).getNameUTF(),""));
		System.out.println("OK");
		
		System.out.print("\tget(VALID_NAME,INVALID_ID) must return null...");
		assertNull(trie.get(elements.get(2).getNameUTF(),"INVALID_ID"));
		System.out.println("OK");
		
		System.out.print("\tget(VALID_NAME,VALID_ID) must return the specified toponym ...");
		assertEquals(trie.get(elements.get(2).getNameUTF(),elements.get(2).getId()),elements.get(2));
		System.out.println("OK");
	}

	@Test
	public void testGetString() {
		System.out.println("Testing get(String)...");
		
		System.out.print("\tget(null) must return null...");
		assertNull(trie.get(null));
		System.out.println("OK");
		
		System.out.print("\tget(\"\") must return null...");
		assertNull(trie.get(""));
		System.out.println("OK");
		
		System.out.print("\tget(INVALID_NAME) must return null...");
		assertNull(trie.get("INVALID_NAME"));
		System.out.println("OK");
		
		System.out.print("\tget(VALID_NAME) must return a toponym with the specified name...");
		assertEquals(trie.get(elements.get(0).getNameUTF()).getNameUTF(),elements.get(0).getNameUTF());
		System.out.println("OK");
		
		System.out.print("\tget(VALID_NAME) must return a toponym with the specified name...");
		assertEquals(trie.get(elements.get(1).getNameUTF()).getNameUTF(),elements.get(1).getNameUTF());
		System.out.println("OK");
	}

	@Test
	public void testRemoveStringString() {
		System.out.println("Testing remove(String)...");
		
		System.out.print("\tremove(\"\",\"\") must return false...");
		assertFalse(trie.remove("",""));
		System.out.println("OK");
		
		System.out.print("\tremove(\"\",null) must return false...");
		assertFalse(trie.remove("",(String)null));
		System.out.println("OK");
		
		System.out.print("\tremove(null,\"\") must return false...");
		assertFalse(trie.remove(null,""));
		System.out.println("OK");
		
		System.out.print("\tremove(null,null) must return false...");
		assertFalse(trie.remove(null,(String)null));
		System.out.println("OK");
		
		System.out.print("\tremove(INVALID_NAME,INVALID_ID) must return false...");
		assertFalse(trie.remove("INVALID_NAME","INVALID_ID"));
		System.out.println("OK");
		
		System.out.print("\tremove(VALID_NAME,INVALID_ID) must return false...");
		assertFalse(trie.remove(elements.get(0).getNameUTF(),"INVALID_ID"));
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(0).getNameUTF()+"\","+elements.get(0).getId()+") must return true...");
		assertTrue(trie.remove(elements.get(0).getNameUTF(),elements.get(0).getId()));
		System.out.println("OK");
		
		System.out.print("\tThere are 2 elements with name \""+elements.get(1).getNameUTF()+"\"...");
		assertEquals(trie.getList(elements.get(1).getNameUTF()).size(),2);
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(1).getNameUTF()+"\","+elements.get(1).getId()+") must return true...");
		assertTrue(trie.remove(elements.get(1).getNameUTF(),elements.get(1).getId()));
		System.out.println("OK");
		
		System.out.print("\tThere is not \""+elements.get(1).getNameUTF()+"\"("+elements.get(1).getId()+") in the trie...");
		assertFalse(trie.getList(elements.get(1).getNameUTF()).contains(elements.get(1)));
		System.out.println("OK");
		
		System.out.print("\tThere is 1 element with name \""+elements.get(1).getNameUTF()+"\"...");
		assertEquals(trie.getList(elements.get(1).getNameUTF()).size(),1);
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(2).getNameUTF()+"\","+elements.get(2).getId()+") must return true...");
		assertTrue(trie.remove(elements.get(2).getNameUTF(),elements.get(2).getId()));
		System.out.println("OK");
		
		System.out.print("\tThere is no element with name \""+elements.get(2).getNameUTF()+"\"...");
		assertEquals(trie.getList(elements.get(2).getNameUTF()).size(),0);
		System.out.println("OK");
		
		System.out.print("\tThere is 1 element with name \"Castellon\"...");
		assertEquals(trie.getList("Castellón").size(),1);
		System.out.println("OK");
		
		System.out.print("\tThere is 1 element with name \"Castellon de la Plana\"...");
		assertEquals(trie.getList("Castellón de la Plana").size(),1);
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(5).getNameUTF()+"\","+elements.get(5).getId()+") must return true...");
		assertTrue(trie.remove(elements.get(5).getNameUTF(),elements.get(5).getId()));
		System.out.println("OK");
		
		System.out.print("\tThere is no element with name \""+elements.get(5).getNameUTF()+"\"...");
		assertEquals(trie.getList("Castellón").size(),0);
		System.out.println("OK");
		
		System.out.print("\tThere is 1 element with name \""+elements.get(6).getNameUTF()+"\"...");
		assertEquals(trie.getList("Castellón de la Plana").size(),1);
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(6).getNameUTF()+"\","+elements.get(6).getId()+") must return true...");
		assertTrue(trie.remove(elements.get(6).getNameUTF(),elements.get(6).getId()));
		System.out.println("OK");
		
		System.out.print("\tThere is no element with name \""+elements.get(6).getNameUTF()+"\"...");
		assertEquals(trie.getList("Castellón de la Plana").size(),0);
		System.out.println("OK");
	}

	@Test
	public void testRemoveStringG() {
		System.out.println("Testing remove(String,G)...");
		
		System.out.print("\tremove(\"\",null) must return false...");
		assertFalse(trie.remove("",(Toponym)null));
		System.out.println("OK");
		
		System.out.print("\tremove(null,null) must return false...");
		assertFalse(trie.remove(null,(Toponym)null));
		System.out.println("OK");
		
		System.out.print("\tremove(\"\",\"\") must return false...");
		assertFalse(trie.remove("",""));
		System.out.println("OK");
		
		System.out.print("\tremove(INVALID_NAME,null) must return false...");
		assertFalse(trie.remove("INVALID_NAME",(Toponym)null));
		System.out.println("OK");
		
		System.out.print("\tremove(null,VALID_TOPONYM) must return false...");
		assertFalse(trie.remove(null,elements.get(0)));
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(0).getNameUTF()+"\","+elements.get(0).getNameUTF()+"("+elements.get(0).getId()+")) must return false...");
		assertFalse(trie.remove(elements.get(0).getNameUTF(),elements.get(9)));
		System.out.println("OK");
		
		System.out.print("\tThere are 2 elements with name \""+elements.get(1).getNameUTF()+"\"...");
		assertEquals(trie.getList(elements.get(1).getNameUTF()).size(),2);
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(1).getNameUTF()+"\","+elements.get(1).getNameUTF()+"("+elements.get(1).getId()+")) must return true...");
		assertTrue(trie.remove(elements.get(1).getNameUTF(),elements.get(1)));
		System.out.println("OK");
		
		System.out.print("\tThere is 1 element with name \""+elements.get(1).getNameUTF()+"\"...");
		assertEquals(trie.getList(elements.get(1).getNameUTF()).size(),1);
		System.out.println("OK");
		System.out.print("\tThe element is not "+elements.get(1).getNameUTF()+"("+elements.get(1).getId()+")...");
		assertFalse(trie.getList(elements.get(1).getNameUTF()).contains(elements.get(1)));
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(2).getNameUTF()+"\","+elements.get(2).getNameUTF()+"("+elements.get(2).getId()+")) must return true...");
		assertTrue(trie.remove(elements.get(2).getNameUTF(),elements.get(2)));
		System.out.println("OK");
		System.out.print("\tThere is no element with name \""+elements.get(1).getNameUTF()+"\"...");
		assertEquals(trie.getList(elements.get(2).getNameUTF()).size(),0);
		System.out.println("OK");
	}

	@Test
	public void testRemoveString() {
		System.out.println("Testing remove(String)...");
		
		System.out.print("\tremove(null) must return 0...");
		assertEquals(trie.remove(null),0);
		System.out.println("OK");
		
		System.out.print("\tremove(\"\") must return 0...");
		assertEquals(trie.remove(""),0);
		System.out.println("OK");
		
		System.out.print("\tremove(\"INVALID NAME\") must return 0...");
		assertEquals(trie.remove("INVALID_NAME"),0);
		System.out.println("OK");
		
		System.out.print("\tremove(\"Barcelon\") must return 0...");
		assertEquals(trie.remove("Barcelon"),0);
		System.out.println("OK");
		
		System.out.print("\tThere are 2 elements with name "+elements.get(1).getNameUTF()+"...");
		assertEquals(trie.getList(elements.get(1).getNameUTF()).size(),2);
		System.out.println("OK");
		
		System.out.print("\tThere ire 2 elements with name "+elements.get(3).getNameUTF()+"...");
		assertEquals(trie.getList(elements.get(3).getNameUTF()).size(),2);
		System.out.println("OK");
		
		System.out.print("\tremove(\""+elements.get(1).getNameUTF()+"\") must return 2...");
		assertEquals(trie.remove(elements.get(1).getNameUTF()),2);
		System.out.println("OK");
		
		System.out.print("\tThere is no element with name "+elements.get(1).getNameUTF()+"...");
		assertTrue(trie.getList(elements.get(1).getNameUTF()).isEmpty());
		System.out.println("OK");
		
		System.out.print("\tThere is 2 elements with name "+elements.get(3).getNameUTF()+"...");
		assertEquals(trie.getList(elements.get(3).getNameUTF()).size(),2);
		System.out.println("OK");
	}

	@Test
	public void testIterator() {
		System.out.println("Testing iterator()...");
		
		Iterator<Toponym> it=trie.iterator();
		Toponym toponym=null;
		Toponym previousToponym=null;
		ArrayList<Toponym> toponyms=new ArrayList<Toponym>();
		System.out.print("\tChecking if iterator iterates elemens alfabetically...");
		while(it.hasNext()) {
			previousToponym=toponym;
			toponym=it.next();
			if (previousToponym!=null)
				assertTrue(previousToponym.getNameUTF().compareTo(toponym.getNameUTF())<=0);
			toponyms.add(toponym);
		}
		System.out.println("OK");
		
		System.out.print("\tIterator returns as many elements as the trie has...");
		assertEquals(elements.size(),toponyms.size());
		System.out.println("OK");
		
		System.out.print("\tAll iterated elements are the same than has been inserted...");
		assertTrue(toponyms.containsAll(elements));
		System.out.println("OK");
		
		
		it = trie.iterator();
		int i = 0;
		while(it.hasNext()) {
			Toponym itToponym=it.next();
			if (itToponym.getId().equals(elements.get(0).getId()) || itToponym.getId().equals(elements.get(2).getId()) || itToponym.getId().equals(elements.get(5).getId()))
				it.remove();
			i++;
		}
		
		System.out.print("\tChecking if the iterator iterates along whole Trie when remove() is used...");
		assertEquals(i, 10);
		System.out.println("OK");
		
		System.out.print("\tChecking if the removed elements are not into the Trie...");
		assertNull(trie.get(elements.get(0).getNameUTF(),elements.get(0).getId()));
		assertNull(trie.get(elements.get(2).getNameUTF(),elements.get(2).getId()));
		assertNull(trie.get(elements.get(5).getNameUTF(),elements.get(5).getId()));
		System.out.println("OK");
		
		System.out.print("\tChecking if the remaining non removed elements are still into the Trie...");
		ArrayList<Toponym> elementsStillInTrie=new ArrayList<Toponym>();
		for (Toponym itToponym : trie) {
			elementsStillInTrie.add(itToponym);
		}
		ArrayList<Toponym> elementsShouldBeInTrie=new ArrayList<Toponym>(elements);
		elementsShouldBeInTrie.remove(elements.get(0));
		elementsShouldBeInTrie.remove(elements.get(2));
		elementsShouldBeInTrie.remove(elements.get(5));
		assertTrue(elementsStillInTrie.containsAll(elementsShouldBeInTrie) && elementsShouldBeInTrie.containsAll(elementsStillInTrie));
		System.out.println("OK");
		

	}

	@Test
	public void testIteratorPrefix() {
		System.out.println("Testing iteratorPrefix()...");
		
		Toponym toponym;
		Toponym previousToponym;
		ArrayList<Toponym> toponyms;
		
		Iterator<Toponym> it=trie.iteratorPrefix(null);
		assertFalse(it.hasNext());
		
		it=trie.iteratorPrefix("");
		previousToponym=null;
		toponym=null;
		toponyms=new ArrayList<Toponym>();
		System.out.print("\tChecking if iteratorPrefix(\"\") iterates elements alfabetically...");
		while(it.hasNext()) {
			previousToponym=toponym;
			toponym=it.next();
			if (previousToponym!=null)
				assertTrue(previousToponym.getNameUTF().compareTo(toponym.getNameUTF())<=0);
			toponyms.add(toponym);
		}
		System.out.println("OK");
		
		System.out.print("\tIterator returns as many elements as the trie has...");
		assertEquals(elements.size(),toponyms.size());
		System.out.println("OK");
		
		System.out.print("\tAll iterated elements are the same than has been inserted...");
		assertTrue(toponyms.containsAll(elements));
		System.out.println("OK");
		
		it=trie.iteratorPrefix("C");
		previousToponym=null;
		toponym=null;
		toponyms=new ArrayList<Toponym>();
		System.out.print("\tChecking if iteratorPrefix(\"C\") iterates elements alfabetically...");
		while(it.hasNext()) {
			previousToponym=toponym;
			toponym=it.next();
			if (previousToponym!=null)
				assertTrue(previousToponym.getNameUTF().compareTo(toponym.getNameUTF())<=0);
			toponyms.add(toponym);
		}
		System.out.println("OK");
		
		System.out.print("\tIterator returns 5 elements that begins with \"C\"...");
		assertEquals(toponyms.size(),5);
		System.out.println("OK");
		
		System.out.print("\tIterated elements are the same 5 that has been inserted at trie...");
		assertTrue(toponyms.containsAll(elements.subList(5,9)));
		System.out.println("OK");
		
		it=trie.iteratorPrefix("Corea");
		previousToponym=null;
		toponym=null;
		toponyms=new ArrayList<Toponym>();
		System.out.print("\tChecking if iteratorPrefix(\"Corea\") iterates elements alfabetically...");
		while(it.hasNext()) {
			previousToponym=toponym;
			toponym=it.next();
			if (previousToponym!=null)
				assertTrue(previousToponym.getNameUTF().compareTo(toponym.getNameUTF())<=0);
			toponyms.add(toponym);
		}
		System.out.println("OK");
		
		System.out.print("\tIterator returns 3 elements that begins with \"Corea\"...");
		assertEquals(toponyms.size(),3);
		System.out.println("OK");
		
		System.out.print("\tIterated elements are the same 3 that has been inserted at trie...");
		assertTrue(toponyms.containsAll(elements.subList(7,9)));
		System.out.println("OK");
	}
}
