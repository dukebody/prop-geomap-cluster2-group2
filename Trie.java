import java.text.Normalizer;
import java.util.*;

public class Trie<G extends IGetId> implements Iterable<G>{
	private HashMap<String,G> values;
	private TreeMap<Character,Trie<G>> children;
	
	public Trie() {
		values=new HashMap<String,G>();
		children = new TreeMap<Character,Trie<G>>();
	}
	
	private static String cleanString(String dirty) {
		return Normalizer.normalize(dirty, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toUpperCase();
	}
	
	private boolean addValue(G value) {
		if (this.values.containsKey(value.getId()))
			return false;
		this.values.put(value.getId(),value);
		return true;
	}
	
	private HashMap<String,G> getValues() {
		return this.values;
	}
	
	public boolean put(G object, String key) {
		if (key == null || key.length() == 0 || object == null)
			return false;
		return doPut(object, cleanString(key));
	}
	
	private boolean doPut(G object, String key) {
		//if the child does not exist is created
		if (children.get(key.charAt(0))==null) children.put(key.charAt(0), new Trie<G>());
		
		//get the correct child
		Trie<G> destinationNode=children.get(key.charAt(0));
		
		//object is added in the correct child
		if(key.length()==1)
			return destinationNode.addValue(object);
		//ask recursively to put the object in the correct subtrie
		else
			return destinationNode.doPut(object, key.substring(1));
	}
	
	private HashMap<String,G> getMap(String key){
		if (key==null) return new HashMap<String,G>();
		key=cleanString(key);
		//if the child does not exist the key is not in the Trie
		if (key.length()==0) return new HashMap<String,G>();
		
		return doGetMap(key);
	}
	
	private HashMap<String,G> doGetMap(String key){
		if (key==null) return new HashMap<String,G>();
		key=cleanString(key);
		//if the child does not exist the key is not in the Trie
		if (key.length()==0) return new HashMap<String,G>();
		
		//get the correct child
		Trie<G> destinationNode=children.get(key.charAt(0));
		
		if (destinationNode == null)
			return new HashMap<String, G>();
		
		//return the list from the correct child
		if(key.length()==1) return destinationNode.getValues();
		//ask recursively to get the list from the correct subtrie
		else return destinationNode.doGetMap(key.substring(1));
	}
	
	public List<G> getList(String key) {
		return new LinkedList<G>(getMap(key).values());
	}
	
	//NOT IN THE SPECIFICATIONS BUT COULD BE USEFUL
	public G get(String key, String id) {
		return getMap(key).get(id);
	}
	
	public G get(String key) {
		Iterator<G> iterator = getMap(key).values().iterator();
		if (iterator.hasNext()) return iterator.next();
		else return null;
	}
	
	public boolean remove(String key, String id) {
		if (key==null) return false;
		key=cleanString(key);
		//if the child does not exist the key is not in the Trie
		if (key.length()==0) return false;
		
		return doRemove(key, id) > 0;
	}
	
	public boolean remove(String key, G object) {
		if (object==null) return false;
		return remove(key,object.getId());
	}
	
	public int remove(String key) {
		if (key==null) return 0;
		key=cleanString(key);
		//if the child does not exist the key is not in the Trie
		if (key.length()==0) return 0;
		
		return doRemove(key, null);
	}
	
	private int doRemove(String key, String id) {
		//get the correct child
		Trie<G> destinationNode=children.get(key.charAt(0));
		if (destinationNode == null)
			return 0;
		
		if (key.length()==1) {
			if (id == null) {
				int removed = destinationNode.getValues().size();
				destinationNode.getValues().clear();
				if (destinationNode.children.isEmpty())
					this.children.remove(key.charAt(0));
				return removed;
			}
			else {
				boolean removed = destinationNode.getValues().remove(id) != null;
				if (destinationNode.getValues().isEmpty() && destinationNode.children.isEmpty())
					this.children.remove(key.charAt(0));
				return removed?1:0;
			}
		}
		
		//ask recursively to get the list from the correct subtrie
		else {
			int removed=destinationNode.doRemove(key.substring(1), id);
			if (destinationNode.getValues().isEmpty() && destinationNode.children.isEmpty())
				this.children.remove(key.charAt(0));
			return removed;
		}
	}
	
	public Iterator<G> iterator() {
		return new TrieIterator(this);
	}

	
	public Iterator<G> iteratorPrefix(String prefix) {
		if (prefix == null)
			return new TrieIterator(null);
		
		if (prefix.length() == 0)
			return new TrieIterator(this);
		
		prefix = cleanString(prefix);
		
		Trie<G> child = children.get(prefix.charAt(0));
		if (child == null)
			return new TrieIterator(null);
		
		return child.iteratorPrefix(prefix.substring(1));
	}
	
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
			
			this.valuesIterator = node.values.values().iterator();
			this.childrenIterator = node.children.values().iterator();
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
			
			return null;
		}

		@Override
		public void remove() {
			if (hadNext) {
				valuesIterator.remove();
				return;
			}
			
			trieIterator.remove();
			
			if (subTrie.values.isEmpty() && subTrie.children.isEmpty()) {
				childrenIterator.remove();
				if (childrenIterator.hasNext()) {
					subTrie = childrenIterator.next();
					trieIterator = new TrieIterator(subTrie);
				}
			}
		}
	}
	
}