import java.util.*;


class CitiesController {
    
    private Trie<City> citiesTrie;
    private Trie<TypeCity> typeCitiesTrie;
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

        private Iterator<TypeCity> trieIterator;

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
        typeCitiesTrie = new Trie<TypeCity>();
        citiesQuadTree = new QuadTree<Double,City>();
    }

    public boolean createCityType(String name, String category, String code) {
        if (getCityType(code) == null) {
            TypeCity typeCity = new TypeCity(name, category, code);
            typeCitiesTrie.put(typeCity, code);
            return true;
        }
        return false;  // already existing city type with that code
    }

    public HashMap<String,String> getCityType(String code) {
        return getMap(typeCitiesTrie.get(code));
    }

    public Iterator<HashMap<String,String>> getCityTypesIterator() {
        return new CityTypesIterator(typeCitiesTrie.iterator());
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
            return map;
        }

        return null;
    }

    public HashMap<String,String> getMap(TypeCity type) {
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

    public boolean addCity(String nameASCII, String nameUTF, double latitude, double longitude, String typeCode) {

        City city;
        TypeCity type = typeCitiesTrie.get(typeCode);

        try {
            city = new City(nameASCII, nameUTF, latitude, longitude, type);
        } catch (IllegalArgumentException e) {
            return false;
        }

        // check that there doesn't exist already a city at the specified point
        Interval<Double> intX = new Interval<Double>(latitude, latitude);
        Interval<Double> intY = new Interval<Double>(longitude, longitude);
        Interval2D<Double> rect = new Interval2D<Double>(intX, intY);
        if (citiesQuadTree.query2D(rect).isEmpty() && type != null) {
            
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
                                 double newLongitude, String newTypeCode) {
        // erase old city from the quadtree and the trie
        HashMap<String, String> oldCity = getCityByNameAndId(nameUTF, id);
        boolean result;

        if (oldCity != null) {
            try {
                result = addCity(newNameASCII, newNameUTF, newLatitude, newLongitude, newTypeCode);
            } catch (IllegalArgumentException e) {
                return false;
            }
            if (result) {  // city added successfully
                citiesTrie.remove(nameUTF, id);
                citiesQuadTree.remove(new Double(oldCity.get("latitude")), new Double(oldCity.get("longitude")));
                return true;
            }
        }
        return false;  // original city not found
    }
}
