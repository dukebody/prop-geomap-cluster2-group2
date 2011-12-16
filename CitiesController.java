import java.util.*;


class CitiesController {

    private Trie<City> citiesTrie;
    private Trie<TypeToponym> typeToponymsTrie;
    private QuadTree<City> citiesQuadTree;
    private QuadTree<BorderPoint> borderPointsQuadTree;

    private LineController lc;

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

    public CitiesController(DataStorage ds) {
        lc = new LineController(ds);
        citiesTrie = ds.getCitiesTrie();
        typeToponymsTrie = ds.getTypeToponymsTrie();
        citiesQuadTree = ds.getCitiesQuadTree();
        borderPointsQuadTree = ds.getBorderPointsQuadTree();
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

    private Zone calculateZone(City city) {
        // create a stack for open (to explore) zones and another for closed (explored) ones
        Stack<Zone> openZones = new Stack<Zone>();
        Stack<Zone> closedZones = new Stack<Zone>();

        // get closest borderpoints to the city
        ArrayList<Node<BorderPoint>> nodes =  borderPointsQuadTree.getCloserNodes(city.getLatitude(), city.getLongitude(), 0.5, 0.5);
                    
        // for every bpoint
        for (Node node: nodes) {
            // get the associated zones
            BorderPoint bp = (BorderPoint) node.value;
            List<Zone> zones = bp.getZones();
            for (Zone zone: zones) {
                // stack the new ones
                if (openZones.search(zone) == -1 && closedZones.search(zone) == -1) {
                    openZones.push(zone);
                }
            }
            // while there are zones in the open-zones stack, explore them
            while (!openZones.empty()) {
                Zone zone = openZones.pop();
                if (lc.checkIfPointIsInsideZone(city, zone)) {
                    return zone;
                }
                // if the city wasn't in that zone, mark it as explored
                closedZones.push(zone);
            }
        }

        //System.out.println("zone not found for city: " + city.getNameUTF());
        //System.out.println("Checked countries: ");
        // for (Zone z: closedZones) {
        //     System.out.println(z.getCountry().getName());
        // }
        return null;
    }


    public boolean addCity(String nameASCII, String nameUTF, double latitude, double longitude, String typeCode, int population) {

        City city;
        TypeToponym type = typeToponymsTrie.get(typeCode);

        try {
            city = new City(nameASCII, nameUTF, latitude, longitude, type, population);
            Zone zone = calculateZone(city);
            if (zone == null) {  // zone not found, so drop this city
                return false;
            }
            city.setZone(zone);
        } catch (IllegalArgumentException e) {
            return false;
        }

        // check that there doesn't exist already a city at the specified point
        if (type != null &&citiesQuadTree.query(latitude, longitude) == null) {
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
