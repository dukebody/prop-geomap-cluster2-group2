//package domain;
import java.text.Normalizer;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author fernando.mora and aitor.esteve
 *
 * @param <G> Elements the Trie will store. Must implement IGetId.
 */
public class Trie<G extends IGetId> implements Iterable<G>{
	//private HashMap<String,G> values;
	//private TreeMap<Character,Trie<G>> children;
	private Object valuesObject;
	private Object childrenObject;
	
	/**
	 * Default constructor of Trie, creates a new empty Trie
	 */
	public Trie() {
		valuesObject = null;
		childrenObject = null;
	}
	
	@SuppressWarnings("unchecked")
	private boolean valuesContainsKey(String key) {
		if (valuesObject == null)
			return false;
		if (valuesObject instanceof IGetId)
			return ((IGetId)valuesObject).getId().equals(key);
		return ((HashMap<String,G>)valuesObject).containsKey(key);
	}
	
	@SuppressWarnings("unchecked")
	private void valuesPut(String key, G value) {
		if (valuesObject == null) {
			valuesObject = value;
			return;
		}
		
		if (valuesObject instanceof IGetId) {
			HashMap<String,G> temp = new HashMap<String,G>();
			temp.put(((IGetId)valuesObject).getId(), (G)valuesObject);
			temp.put(key, value);
			valuesObject = temp;
			return;
		}
		((HashMap<String,G>)valuesObject).put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	private int valuesClear() {
		if (valuesObject == null)
			return 0;
		if (valuesObject instanceof IGetId) {
			valuesObject = null;
			return 1;
		}
		int removed = ((HashMap<String,G>)valuesObject).size();
		valuesObject = null;
		return removed;
	}
	
	@SuppressWarnings("unchecked")
	private int valuesSize() {
		if (valuesObject == null)
			return 0;
		if (valuesObject instanceof IGetId)
			return 1;
		return ((HashMap<String,G>)valuesObject).size();
	}
	
	@SuppressWarnings("unchecked")
	private boolean valuesRemove(String key) {
		if (valuesObject == null)
			return false;
		if (valuesObject instanceof IGetId) {
			if (((G)valuesObject).getId().equals(key)) {
				valuesObject = null;
				return true;
			}
			return false;
		}
		HashMap<String,G> temp = (HashMap<String,G>)valuesObject;
		if (temp.size() > 2)
			return temp.remove(key) != null;
		
		if (temp.remove(key) != null) {
			valuesObject = temp.values().iterator().next();
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private Trie<G> childrenGet(Character key) {
		if (childrenObject == null)
			return null;
		if (childrenObject instanceof Object[]) {
			Object [] temp = (Object[])childrenObject;
			if (temp[0].equals(key))
				return (Trie<G>)(temp[1]);
			return null;
		}
		return ((TreeMap<Character,Trie<G>>)childrenObject).get(key);
	}
	
	@SuppressWarnings("unchecked")
	private void childrenPut(Character key, Trie<G> value) {
		if (childrenObject == null) {
			Object[] temp = new Object[2];
			temp[0] = key;
			temp[1] = value;
			childrenObject = temp;
			return;
		}
		if (childrenObject instanceof Object[]) {
			TreeMap<Character,Trie<G>> temp = new TreeMap<Character,Trie<G>>();
			temp.put((Character)((Object[])childrenObject)[0], (Trie<G>)((Object[])childrenObject)[1]);
			temp.put(key, value);
			childrenObject = temp;
			return;
		}
		((TreeMap<Character,Trie<G>>)childrenObject).put(key, value);
	}
	
	private boolean isLeaf() {
		return childrenObject == null;
	}
	
	@SuppressWarnings("unchecked")
	private void childrenRemove(Character key) {
		if (childrenObject == null)
			return;
		if ((childrenObject instanceof Object[]) && ((Object[])childrenObject)[0].equals(key)) {
			childrenObject = null;
			return;
		}
		if (childrenObject instanceof HashMap<?, ?>) {
			TreeMap<Character,Trie<G>> map = (TreeMap<Character,Trie<G>>)childrenObject;
			map.remove(key);
			if (map.size() == 1) {
				Entry<Character,Trie<G>> entry =
					((TreeMap<Character,Trie<G>>)childrenObject).entrySet().iterator().next();
				Object[] temp = new Object[2];
				temp[0] = entry.getKey();
				temp[1] = entry.getValue();
				childrenObject = temp;
			}
		}
	}
	
	/**
	 * Get a List containing all the elements in the current trie
	 * @return the map
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<G> getValuesList() {
		if (valuesObject == null)
			return new ArrayList<G>();
		
		if (valuesObject instanceof IGetId) {
			ArrayList<G> retval = new ArrayList<G>();
			G temp = (G)valuesObject;
			retval.add(temp);
			return retval;
		}
		return new ArrayList(((HashMap<String,G>)valuesObject).values());
	}
	
	/**
	 * Clean a String to remove all diacritical marks.
	 * @param dirty The String that is going to be cleared
	 * @return The cleared String without any diacritical mark
	 */
	private static String cleanString(String dirty) {
		return Normalizer.normalize(dirty, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();
	}

	/**
	 * Adds an element index into the current Trie root if there is no other element with the same id
	 * @param value Element that is going to be inserted
	 * @return true if the element has been inserted, false is there is already other element inserted with the same id
	 */
	private boolean addValue(G value) {
		if (valuesContainsKey(value.getId()))
			return false;
		valuesPut(value.getId(),value);
		return true;
	}
	
	/**
	 * Insert a element into the Trie by the specified key
	 * @param object the element that is going to be inserted
	 * @param key the key to index the element
	 * @return true if the element has been correctly inserted, false if the element has not been inserted because there is already other element with the same id in the same key
	 */
	public boolean put(G object, String key) {
		if (key == null || key.length() == 0 || object == null)
			return false;
		return doPut(object, cleanString(key));
	}
	
	/**
	 * This private method is called by all the put methods and have the real implementation to put an element in the Trie
	 * @param object the element that is going to be inserted
	 * @param key the key to index the element
	 * @return true if the element has been correctly inserted, false if the element has not been inserted because there is already other element with the same id in the same key
	 */
	private boolean doPut(G object, String key) {
		//if the child does not exist is created
		if (childrenGet(key.charAt(0))==null) childrenPut(key.charAt(0), new Trie<G>());
		
		//get the correct child
		Trie<G> destinationNode = childrenGet(key.charAt(0));
		
		//object is added in the correct child
		if(key.length()==1)
			return destinationNode.addValue(object);
		//ask recursively to put the object in the correct subtrie
		else
			return destinationNode.doPut(object, key.substring(1));
	}
	
	/**
	 * This private method is called by all the get methods and have the real implementation
	 * to get all the element from the Trie by its key.
	 * @param key the key where all the elements are indexed.
	 * @return a map that contains all the elements by the specified key indexed by their id.
	 */
	private ArrayList<G> doGetList(String key){
		//if the child does not exist the key is not in the Trie
		if (key.length() == 0) return new ArrayList<G>();
		
		//get the correct child
		Trie<G> destinationNode = childrenGet(key.charAt(0));
		
		if (destinationNode == null)
			return new ArrayList<G>();
		
		//return the list from the correct child
		if (key.length()==1) return destinationNode.getValuesList();
		//ask recursively to get the list from the correct subtrie
		else return destinationNode.doGetList(key.substring(1));
	}
	
	/**
	 * Get a list with all the elements of the Trie indexed with the specified key 
	 * @param key The key of the elements
	 * @return A list with all the elements indexed in the specified key. An empty list if there is no element in the specified key
	 */
	public List<G> getList(String key) {
		if (key == null) return new ArrayList<G>();
		key = cleanString(key);
		//if the child does not exist the key is not in the Trie
		if (key.length() == 0) return new ArrayList<G>();
		
		return doGetList(key);
	}
	
	/**
	 * Get the element of the Trie indexed with the specified key and the specified id
	 * @param key the key where the element is indexed
	 * @param id the id of the element
	 * @return the element or null if the element is not indexed with the specified key
	 */
	public G get(String key, String id) {
		List<G> list = getList(key);
		for (G obj : list)
			if (obj.getId().equals(id))
				return obj;
		return null;
	}
	
	/**
	 * Get an element of the Trie indexed with the specified key
	 * @param key the key where the element is indexed
	 * @return one arbitrary element of the Trie indexed by the specified key, the element that will return if there more than one element indexed with the same key is not defined. null if there is not any element indexed with the specied key
	 */
	public G get(String key) {
		Iterator<G> iterator = getList(key).iterator();
		if (iterator.hasNext()) return iterator.next();
		else return null;
	}
	
	/**
	 * Removes the element from the trie with the specified id
	 * @param key the key where the element is indexed
	 * @param id the id of the element is going to be removed
	 * @return true if the element has been removed. false if the element has not been found
	 */
	public boolean remove(String key, String id) {
		if (key == null) return false;
		key = cleanString(key);
		//if the child does not exist the key is not in the Trie
		if (key.length() == 0) return false;
		
		return doRemove(key, id) > 0;
	}
	
	/**
	 * Removes a specified element from the trie
	 * @param key the key where the element is indexed
	 * @param object the element is going to be removed
	 * @return true if the element has been removed. false if the element has not been found
	 */
	public boolean remove(String key, G object) {
		if (object == null) return false;
		return remove(key,object.getId());
	}
	
	/**
	 * Removes all the elements of the Trie indexed with the specified key
	 * @param key the key where the elements are indexed
	 * @return the number removed elements
	 */
	public int remove(String key) {
		if (key == null) return 0;
		key = cleanString(key);
		//if the child does not exist the key is not in the Trie
		if (key.length()==0) return 0;
		
		return doRemove(key, null);
	}
	
	/**
	 * Private method that do the actual implementation to remove a specified element from the trie.
	 * @param key The key of the element to remove.
	 * @param id The id of the element to remove, null if you wish to remove all the elements indexed
	 * with that key.
	 * @return The number removed elements.
	 */
	private int doRemove(String key, String id) {
		//get the correct child
		Trie<G> destinationNode = childrenGet(key.charAt(0));
		if (destinationNode == null)
			return 0;
		
		if (key.length() == 1) {
			if (id == null) {
				int removed = destinationNode.valuesClear();
				if (destinationNode.isLeaf())
					this.childrenRemove(key.charAt(0));
				return removed;
			}
			else {
				boolean removed = destinationNode.valuesRemove(id);
				if (destinationNode.valuesSize() == 0 && destinationNode.isLeaf())
					this.childrenRemove(key.charAt(0));
				return removed ? 1 : 0;
			}
		}
		
		//ask recursively to get the list from the correct subtrie
		else {
			int removed = destinationNode.doRemove(key.substring(1), id);
			if (destinationNode.valuesSize() == 0 && destinationNode.isLeaf())
				this.childrenRemove(key.charAt(0));
			return removed;
		}
	}
	
	/**
	 * Get the iterator to iterate all the elements in the current Trie alphabetically
	 */
	public Iterator<G> iterator() {
		return new TrieIterator(this);
	}

	/**
	 * Get an iterator to iterate all the elements in the current Trie indexed by a key that begins with the specified key alfabetically
	 * @param prefix The String that all the keys of the iterator must begin by
	 * @return the iterator
	 */
	public Iterator<G> iteratorPrefix(String prefix) {
		if (prefix == null)
			return new TrieIterator(null);
		
		if (prefix.length() == 0)
			return new TrieIterator(this);
		
		prefix = cleanString(prefix);
		
		Trie<G> child = childrenGet(prefix.charAt(0));
		if (child == null)
			return new TrieIterator(null);
		
		return child.iteratorPrefix(prefix.substring(1));
	}
	
	/**
	 * Simple iterator for Trie elements
	 * @author aitor.esteve
	 *
	 */
	private class TrieIterator implements Iterator<G> {
		private Iterator<G> valuesIterator;
		private Iterator<Trie<G>> childrenIterator;
		private TrieIterator trieIterator;
		private Trie<G> subTrie;
		boolean hadNext = true;
		
		private TrieIterator(Trie<G> node) {
			if (node == null) {
				valuesIterator = null;
				return;
			}
			
			this.valuesIterator = new ValuesIterator(node);
			this.childrenIterator = new ChildrenIterator(node);
			if (childrenIterator.hasNext()) {
				subTrie = childrenIterator.next();
				trieIterator = new TrieIterator(subTrie);
			}
			else {
				trieIterator = null;
				subTrie = null;
			}
		}
		
		@Override
		public boolean hasNext() {
			if (valuesIterator == null)
				return false;
			
			if (valuesIterator.hasNext())
				return true;
			
			hadNext = false;
			
			if (trieIterator == null)
				return false;
			
			if (trieIterator.hasNext())
				return true;
			
			if (childrenIterator.hasNext())
				return true;
			
			return false;
		}

		@Override
		public G next() {
			if (valuesIterator == null)
				return null;
			
			if (valuesIterator.hasNext())
				return valuesIterator.next();
			
			hadNext = false;
			
			if (trieIterator == null)
				return null;
			
			if (trieIterator.hasNext())
				return trieIterator.next();
			
			if (childrenIterator.hasNext()) {
				subTrie = childrenIterator.next();
				trieIterator = new TrieIterator(subTrie);
				return trieIterator.next();
			}
			
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			if (hadNext) {
				valuesIterator.remove();
				return;
			}
			
			trieIterator.remove();
			
			if (subTrie.valuesSize() == 0 && subTrie.isLeaf()) {
				childrenIterator.remove();
				if (childrenIterator.hasNext()) {
					subTrie = childrenIterator.next();
					trieIterator = new TrieIterator(subTrie);
				}
			}
		}
	}
	
	private class ValuesIterator implements Iterator<G> {
		private Iterator<G> iterator;
		private boolean iterated;
		private Trie<G> node;
		
		@SuppressWarnings("unchecked")
		private ValuesIterator(Trie<G> node) {
			this.node = node;
			if (this.node.valuesObject == null) {
				iterator = null;
				iterated = true;
				return;
			}
			if (this.node.valuesObject instanceof IGetId) {
				iterator = null;
				iterated = false;
				return;
			}
			iterator = ((HashMap<String,G>)this.node.valuesObject).values().iterator();
			iterated = false;
		}
		
		@Override
		public boolean hasNext() {
			if (iterator != null)
				return iterator.hasNext();
			return !iterated;
		}

		@SuppressWarnings("unchecked")
		@Override
		public G next() {
			if (iterator != null)
				return iterator.next();
			if (iterated)
				throw new NoSuchElementException();
			iterated = true;
			return (G)node.valuesObject;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void remove() {
			if (iterator != null) {
				iterator.remove();
				if (((HashMap<String,G>)node.valuesObject).size() == 1) {
					G temp = ((HashMap<String,G>)node.valuesObject).values().iterator().next();
					node.valuesObject = temp;
					if (iterator.hasNext())
						iterated = false;
					else
						iterated = true;
					iterator = null;
				}
				return;
			}
			if (!iterated || node.valuesObject == null)
				throw new IllegalStateException();
			
			node.valuesObject = null;
		}
		
	}
	
	private class ChildrenIterator implements Iterator<Trie<G>> {
		private Iterator<Trie<G>> iterator;
		private boolean iterated;
		private Trie<G> node;
		
		@SuppressWarnings("unchecked")
		private ChildrenIterator(Trie<G> node) {
			this.node = node;
			if (this.node.childrenObject == null) {
				iterator = null;
				iterated = true;
				return;
			}
			if (this.node.childrenObject instanceof Object[]) {
				iterator = null;
				iterated = false;
				return;
			}
			iterator = ((TreeMap<Character,Trie<G>>)this.node.childrenObject).values().iterator();
			iterated = false;
		}
		
		@Override
		public boolean hasNext() {
			if (iterator != null)
				return iterator.hasNext();
			return !iterated;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Trie<G> next() {
			if (iterator != null)
				return iterator.next();
			if (iterated)
				throw new NoSuchElementException();
			iterated = true;
			return (Trie<G>)((Object[])node.childrenObject)[1];
		}

		@SuppressWarnings("unchecked")
		@Override
		public void remove() {
			if (iterator != null) {
				iterator.remove();
				if (((TreeMap<Character,Trie<G>>)node.childrenObject).size() == 1) {
					Entry<Character,Trie<G>> temp =
						((TreeMap<Character,Trie<G>>)node.childrenObject).entrySet().iterator().next();
					Object[] temp2 = new Object[2];
					temp2[0] = temp.getKey();
					temp2[1] = temp.getValue();
					node.childrenObject = temp2;
					
					if (iterator.hasNext())
						iterated = false;
					else
						iterated = true;
					iterator = null;
				}
				return;
			}
			if (!iterated || node.childrenObject == null)
				throw new IllegalStateException();
			
			node.childrenObject = null;
		}
		
	}
}
