import java.util.*;


class ToponymsController {
    
    private Trie<Toponym> toponymsTrie;
    private Trie<TypeToponym> typeToponymsTrie;
    private QuadTree<Double,Toponym> toponymsQuadTree;

    private class ToponymsIterator implements Iterator<HashMap<String,String>> {

        private Iterator<Toponym> trieIterator;

        public ToponymsIterator(Iterator trieIterator) {
            this.trieIterator = trieIterator;
        }

        public boolean hasNext() {
            return trieIterator.hasNext();
        }

        public HashMap<String,String> next() {
            return getMap(trieIterator.next());
        }

        public void remove() {
            trieIterator.remove();
        }
    }

    private class ToponymTypesIterator implements Iterator<HashMap<String,String>> {

        private Iterator<TypeToponym> trieIterator;

        public ToponymTypesIterator(Iterator trieIterator) {
            this.trieIterator = trieIterator;
        }

        public boolean hasNext() {
            return trieIterator.hasNext();
        }

        public HashMap<String,String> next() {
            return getMap(trieIterator.next());
        }

        public void remove() {
            trieIterator.remove();
        }
    }

    public ToponymsController() {
        toponymsTrie = new Trie<Toponym>();
        typeToponymsTrie = new Trie<TypeToponym>();
        toponymsQuadTree = new QuadTree<Double,Toponym>();
    }

    public boolean createToponymType(String name, String category, String code) {
        if (getToponymType(code) == null) {
            TypeToponym typeToponym = new TypeToponym(name, category, code);
            typeToponymsTrie.put(typeToponym, code);
            return true;
        }
        return false;  // already existing toponym type with that code
    }

    public HashMap<String,String> getToponymType(String code) {
        return getMap(typeToponymsTrie.get(code));
    }

    public Iterator<HashMap<String,String>> getToponymTypesIterator() {
        return new ToponymTypesIterator(typeToponymsTrie.iterator());
    }

    public HashMap<String,String> getMap(Toponym toponym) {
        HashMap<String,String> map = new HashMap<String,String>();
        if (toponym != null) {
            map.put("id", toponym.getId());
            map.put("nameASCII", toponym.getNameASCII());
            map.put("nameUTF", toponym.getNameUTF());
            map.put("latitude", new Double(toponym.getLatitude()).toString());
            map.put("longitude", new Double(toponym.getLongitude()).toString());
            map.put("type", toponym.getType().getCode());
            return map;
        }

        return null;
    }

    public HashMap<String,String> getMap(TypeToponym type) {
        HashMap<String,String> map = new HashMap<String,String>();
        if (type != null) {
            map.put("name", type.getName());
            map.put("category", type.getCategory());
            map.put("code", type.getCode());
            return map;
        }

        return null;
    }

    public List<HashMap<String,String>> getMap(List<Toponym> toponymList) {
        List<HashMap<String,String>> mapList = new LinkedList<HashMap<String,String>>();
        for (Toponym toponym: toponymList) {
            mapList.add(getMap(toponym));
        }

        return mapList;
    }

    public boolean addToponym(String nameASCII, String nameUTF, double latitude, double longitude, String typeCode) {

        Toponym toponym;
        TypeToponym type = typeToponymsTrie.get(typeCode);

        try {
            toponym = new Toponym(nameASCII, nameUTF, latitude, longitude, type);
        } catch (IllegalArgumentException e) {
            return false;
        }

        // check that there doesn't exist already a toponym at the specified point
        Interval<Double> intX = new Interval<Double>(latitude, latitude);
        Interval<Double> intY = new Interval<Double>(longitude, longitude);
        Interval2D<Double> rect = new Interval2D<Double>(intX, intY);
        if (toponymsQuadTree.query2D(rect).isEmpty() && type != null) {
            
            toponymsTrie.put(toponym, nameUTF);
            toponymsQuadTree.insert(latitude, longitude, toponym);
            return true;
        }
        return false; // point already exists
    }

    public List<HashMap<String,String>> getToponymsByName(String name) {
        return getMap(toponymsTrie.getList(name));
    }

    public HashMap<String,String> getToponymByNameAndId(String name, String id) {
        return getMap(toponymsTrie.get(name, id));
    }

    public Iterator<HashMap<String,String>> getToponymsPrefixIterator(String name) {
        return new ToponymsIterator(toponymsTrie.iteratorPrefix(name));
    }

    public boolean modifyToponym(String nameUTF, String id, String newNameASCII, String newNameUTF, double newLatitude, 
                                 double newLongitude, String newTypeCode) {
        // erase old toponym from the quadtree and the trie
        HashMap<String, String> oldToponym = getToponymByNameAndId(nameUTF, id);
        boolean result;

        if (oldToponym != null) {
            try {
                result = addToponym(newNameASCII, newNameUTF, newLatitude, newLongitude, newTypeCode);
            } catch (IllegalArgumentException e) {
                return false;
            }
            if (result) {  // toponym added successfully
                toponymsTrie.remove(nameUTF, id);
                toponymsQuadTree.remove(new Double(oldToponym.get("latitude")), new Double(oldToponym.get("longitude")));
                return true;
            }
        }
        return false;  // original toponym not found
    }
}
