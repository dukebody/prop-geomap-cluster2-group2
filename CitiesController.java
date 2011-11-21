import java.util.*;


class CitiesController {
    
    private Trie<City> citiesTrie;
    private Trie<TypeToponym> typeToponymsTrie;
    private QuadTree<Double,City> citiesQuadTree;

    private class CitiesIterator implements Iterator<HashMap<String,String>> {

        private Iterator<City> trieIterator;

        public CitiesIterator(Iterator trieIterator) {
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

    private class CityTypesIterator implements Iterator<HashMap<String,String>> {

        private Iterator<TypeToponym> trieIterator;

        public CityTypesIterator(Iterator trieIterator) {
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

    public CitiesController() {
        citiesTrie = new Trie<City>();
        typeToponymsTrie = new Trie<TypeToponym>();
        citiesQuadTree = new QuadTree<Double,City>();
    }

    public boolean createToponymType(String name, String category, String code) {
        if (getToponymType(code) == null) {
            TypeToponym typeToponym = new TypeToponym(name, category, code);
            typeToponymsTrie.put(typeToponym, code);
            return true;
        }
        return false;  // already existing city type with that code
    }

    public HashMap<String,String> getToponymType(String code) {
        return getMap(typeToponymsTrie.get(code));
    }

    public Iterator<HashMap<String,String>> getToponymTypesIterator() {
        return new CityTypesIterator(typeToponymsTrie.iterator());
    }

    public HashMap<String,String> getMap(City city) {
        HashMap<String,String> map = new HashMap<String,String>();
        if (city != null) {
            map.put("id", city.getId());
            map.put("nameASCII", city.getNameASCII());
            map.put("nameUTF", city.getNameUTF());
            map.put("latitude", new Double(city.getLatitude()).toString());
            map.put("longitude", new Double(city.getLongitude()).toString());
            map.put("type", city.getType().getCode());
            map.put("population", new Integer(city.getPopulation()).toString());
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

    public List<HashMap<String,String>> getMap(List<City> cityList) {
        List<HashMap<String,String>> mapList = new LinkedList<HashMap<String,String>>();
        for (City city: cityList) {
            mapList.add(getMap(city));
        }

        return mapList;
    }

    public boolean addCity(String nameASCII, String nameUTF, double latitude, double longitude, String typeCode, int population) {

        City city;
        TypeToponym type = typeToponymsTrie.get(typeCode);

        try {
            city = new City(nameASCII, nameUTF, latitude, longitude, type, population);
        } catch (IllegalArgumentException e) {
            return false;
        }

        // check that there doesn't exist already a city at the specified point
        if (citiesQuadTree.query(latitude, longitude) == null && type != null) {
            
            citiesTrie.put(city, nameUTF);
            citiesQuadTree.insert(latitude, longitude, city);
            return true;
        }
        return false; // point already exists
    }

    public List<HashMap<String,String>> getCitiesByName(String name) {
        return getMap(citiesTrie.getList(name));
    }

    public HashMap<String,String> getCityByNameAndId(String name, String id) {
        return getMap(citiesTrie.get(name, id));
    }

    public Iterator<HashMap<String,String>> getCitiesPrefixIterator(String name) {
        return new CitiesIterator(citiesTrie.iteratorPrefix(name));
    }

    public boolean modifyCity(String nameUTF, String id, String newNameASCII, String newNameUTF, double newLatitude, 
                                 double newLongitude, String newTypeCode, int newPopulation) {
        // erase old city from the quadtree and the trie
        HashMap<String, String> oldCity = getCityByNameAndId(nameUTF, id);
        boolean result;

        if (oldCity != null) {
            try {
                result = addCity(newNameASCII, newNameUTF, newLatitude, newLongitude, newTypeCode, newPopulation);
            } catch (IllegalArgumentException e) {
                return false;
            }
            if (result) {  // city created successfully
                citiesTrie.remove(nameUTF, id);
                citiesQuadTree.remove(new Double(oldCity.get("latitude")), new Double(oldCity.get("longitude")));
                return true;
            }
        }
        return false;  // original city not found
    }

    public boolean deleteCity(String name, String id) {
        HashMap<String,String> city = getCityByNameAndId(name, id);
        if (city != null) {
            citiesTrie.remove(name, id);
            citiesQuadTree.remove(new Double(city.get("latitude")), new Double(city.get("longitude")));
            return true;
        }
        return false;
    }
}