import java.util.*;

/**
This controller manages city-related operations.
*/
public class CitiesController {

    private Trie<City> citiesTrie;
    private Trie<TypeToponym> typeToponymsTrie;
    private QuadTree<City> citiesQuadTree;
    private QuadTree<BorderPoint> borderPointsQuadTree;

    private LineController lc;

    /**
    Iterator that transforms incoming cities into their HashMap representation.
    */
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

    /**
    Iterator that transforms incoming toponym types into their HashMap representation.
    */
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

    /**
    Spawn a new CitiesController attached to the specified data storage.

    @param ds DataStorage object to be attached to.
    */
    public CitiesController(DataStorage ds) {
        lc = new LineController(ds);
        citiesTrie = ds.getCitiesTrie();
        typeToponymsTrie = ds.getTypeToponymsTrie();
        citiesQuadTree = ds.getCitiesQuadTree();
        borderPointsQuadTree = ds.getBorderPointsQuadTree();
    }


    /**
    Create a new TypeToponym with the specified parameters.

    @param name Name (description) of the TypeToponym.

    @param category Category of the TypeToponym.

    @param code Code of the TypeToponym.
    */
    public boolean createToponymType(String name, String category, String code) {
        if (getToponymType(code) == null) {
            TypeToponym typeToponym = new TypeToponym(name, category, code);
            typeToponymsTrie.put(typeToponym, code);
            return true;
        }
        return false;  // already existing city type with that code
    }

    /**
    Get the HashMap representation of the specified toponym type.

    @param code Code of the toponym type to be retrieved.
    */
    public HashMap<String,String> getToponymType(String code) {
        return getMap(typeToponymsTrie.get(code));
    }

    /**
    Get the HashMap representation of the specified toponym type.

    @param type Instance of the TypeToponym to be translated into HashMap.
    */
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

    /**
    Get an iterator over the HashMap representation of all the toponym types
    available in the system.
    */
    public Iterator<HashMap<String,String>> getToponymTypesIterator() {
        return new CityTypesIterator(typeToponymsTrie.iterator());
    }

    /**
    Get the HashMap representation of the specified city.

    @param city Instance of the city to be translated into HashMap.
    */
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


    /**
    Get the HashMap representation of the specified list of cities.

    @param cityList List of the city instances to be translated into HashMaps.
    */
    public List<HashMap<String,String>> getMap(List<City> cityList) {
        List<HashMap<String,String>> mapList = new LinkedList<HashMap<String,String>>();
        for (City city: cityList) {
            mapList.add(getMap(city));
        }

        return mapList;
    }

    /**
    Determining the zone where this city belongs.

    This is done getting the closer border points to the city and then exploring the zones
    they are part of, checking if they're inside or not.

    @param city City whose zone has to be determined.

    @return Zone instance where the city belongs.
    */
    private Zone calculateZone(City city) {
        // create a stack for open (to explore) zones and another for closed (explored) ones
        Stack<Zone> openZones = new Stack<Zone>();
        Stack<Zone> closedZones = new Stack<Zone>();

        // get closest borderpoints to the city
        if (borderPointsQuadTree.isEmpty())
                return null;

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
        return null;
    }

    /**
    Create and save a city with the specified parameters.

    @param nameASCII ASCII name of the city.

    @param nameUTF UTF8 name of the city.

    @param latitude Latitude coordinate of the city. Must be in the range
    -90 -- 90 or the city creation will fail.

    @param longitude Longitude coordinate of the city. Must be in the
    range -180 -- 80 or the city creation will fail.

    @param typeCode Code of the toponym type for this city.

    @param population Population of the city as an integer.

    @return True if the city was created successfully, false otherwise.
    */
    public boolean addCity(String nameASCII, String nameUTF, double latitude, 
        double longitude, String typeCode, int population) {

        City city;
        TypeToponym type = typeToponymsTrie.get(typeCode);
        Zone zone;

        try {
            city = new City(nameASCII, nameUTF, latitude, longitude, type, population);

            // fallback if the quadtree is empty (so tests work)
            if (borderPointsQuadTree.isEmpty()) {
                zone = null;
            } else {
                zone = calculateZone(city);
                if (zone == null) {  // zone not found, so drop this city
                    return false;
                }
                city.setZone(zone);
            }

            
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

    /**
    Get the list of HashMap representation of the cities with the specified
    name. Note that there can be more than one city with the same name.

    @param name UTF8 name of the cities to be retrieved.

    */
    public List<HashMap<String,String>> getCitiesByName(String name) {
        return getMap(citiesTrie.getList(name));
    }

    /**
    Get the HashMap representation of the city with the specified
    name and id. There can only be one city with a specified (name,id) pair.
    Actually, the id identifies the city uniquely, but the name needs to be
    specified for indexing reasons.

    @param name UTF8 name of the city to be retrieved.

    @param id Id of the city to be retrieved.
    */
    public HashMap<String,String> getCityByNameAndId(String name, String id) {
        return getMap(citiesTrie.get(name, id));
    }


    /**
    Get the HashMap representation of the cities starting with the specified prefix.

    @param name Prefix matching the start of the UTF8 name of the cities to be
    retrieved.
    */
    public Iterator<HashMap<String,String>> getCitiesPrefixIterator(String name) {
        return new CitiesIterator(citiesTrie.iteratorPrefix(name));
    }


    /**
    Modify the city matching the given UTF8-name and id, replacing the old values
    with the new ones.
    */
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

    /**
    Delete the specified city.

    @param name UTF8 name of the city.

    @param id Id of the city.
    */
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
