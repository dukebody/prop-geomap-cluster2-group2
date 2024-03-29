import java.util.*;
import java.lang.Math;

/**
This controller manages country-related data and serves as
main connection with the presentation layer, since most use-cases
start with specifying a country.
*/
public class CountryController {

    private Trie<Country> countriesTrie;
    private QuadTree<BorderPoint> borderPointsQuadTree;
    private QuadTree<City> citiesQuadTree;
    private LineController lc;
    private CitiesController cc;

    private class CountriesIterator implements Iterator<HashMap<String,String>> {

        private Iterator<Country> trieIterator;

        public CountriesIterator(Iterator trieIterator) {
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
    Spawn a new CountryController attached to the specified data storage.

    @param ds DataStorage object to be attached to.
    */
    public CountryController(DataStorage ds) {
        countriesTrie = ds.getCountriesTrie();
        borderPointsQuadTree = ds.getBorderPointsQuadTree();
        citiesQuadTree = ds.getCitiesQuadTree();
        lc = new LineController(ds);
        cc = new CitiesController(ds);
    }


    /**
    Get HashMap representation for the specified country.
    @param country Country to be converted to HashMap.
    */
    private HashMap<String,String> getMap(Country country) {
        HashMap<String,String> map = new HashMap<String,String>();
        if (country != null) {
            map.put("name", country.getName());
            map.put("code", country.getCode());
            return map;
        }

        return null;
    }

    /**
    Get HashMap representation for the specified list of countries.

    @param countryList List of countries to be converted to HashMap.
    */
    public List<HashMap<String,String>> getMap(List<Country> countryList) {
        List<HashMap<String,String>> mapList = new LinkedList<HashMap<String,String>>();
        for (Country country: countryList) {
            mapList.add(getMap(country));
        }

        return mapList;
    }

    /**
    Create a country with the specified name and code.

    @param name The name of the country.

    @param code The country code of the country.
    */
    public boolean addCountry(String name, String code) {
        Country country = new Country(name);
        country.setCode(code);
        if (countriesTrie.get(name) == null && name != null) {
            countriesTrie.put(country, name);
            return true;
        }

        return false; // a country with this name already exists
    }

    /**
    Get the HashMap representation of the country with the given name.

    @param name Name of the country to retrieve.
    */
    public HashMap<String,String> getCountry(String name) {
        return getMap(countriesTrie.get(name));
    }

    /**
    Get the country object of the country with the given name.

    @param name Name of the country to retrieve.
    */
    public Country getRawCountry(String name) {
        return countriesTrie.get(name);
    }

    /**
    Modify the specified country.

    @param oldName Name of the country to modify.

    @param newName New name of the country.

    @param newCode New code of the country.
    */
    public boolean modifyCountry(String oldName, String newName, String newCode) {
        Country oldCountry = countriesTrie.get(oldName);
        if (oldCountry != null) {  // the origin country exists
            if (newName != null && newName.length() != 0 && countriesTrie.get(newName) == null) {
                // no null or zero length target name, and no country with the target name

                // copy country
                oldCountry = countriesTrie.get(oldName);
                Country newCountry = new Country(newName);
                newCountry.setCode(newCode);
                newCountry.setZones(oldCountry.getZones());

                // remove old country
                countriesTrie.remove(oldName);
                countriesTrie.put(newCountry, newName);
                return true;
            }
            return false;  // a country with the target name already exists
        }

        return false;  // there's no origin country with the specified oldName

        
    }

    /**
    Remove the country with the specified name.

    @param name Name of the country to delete.
    */
    public boolean removeCountry(String name) {
        return countriesTrie.remove(name, name);
    }

    /**
    Get a iterator over the HashMap representation of all countries in the system.
    */
    public CountriesIterator getAllCountriesIterator() {
        return new CountriesIterator(countriesTrie.iterator());
    }

    /**
    Get a iterator over the instances of all countries in the system.
    */
    public Iterator<Country> getAllRawCountriesIterator() {
        return countriesTrie.iterator();
    }

    /**
    Get a iterator over the HashMap representation of the countries in the
    system starting with the given prefix.

    @param prefix Prefix of the countries to retrieve. Ex: "S" will return "Spain",
    "Sweden", etc.
    */
    public CountriesIterator getPrefixCountriesIterator(String prefix) {
        return new CountriesIterator(countriesTrie.iteratorPrefix(prefix));
    }


    /**
    Get the HashMap representation of the countries limiting with the one
    with the specified name.

    @param name Name of the country to get the neighbours from.
    */
    public List<HashMap<String,String>> getNeighborCountries(String name) {
        Country country = countriesTrie.get(name);
        return getMap(lc.getNeighborCountries(country));
    }

    /**
    Get the shared border length, in km, between the specified countries.

    @param nameA Name of the first country.

    @param nameB Name of the second country.
    */
    public Double getSharedBorderLengthWith(String nameA, String nameB) {
        Country countryA = countriesTrie.get(nameA);
        Country countryB = countriesTrie.get(nameB);
        return lc.getTwoCountriesBordersLength(countryA, countryB);
    }

    /**
    Get the total shared (not coastal) border length of the specified country.

    @param name Name of the country.
    */
    public Double getTotalSharedBorderLength(String name) {
        Country country = countriesTrie.get(name);
        return lc.getWholeCountryBordersLength(country);
    }

    /**
    Get the total coastal (not shared) border length of the specified country.

    @param name Name of the country.
    */
    public Double getTotalCoastlineLength(String name) {
        Country country = countriesTrie.get(name);
        return lc.getCountryCoastalLength(country);
    }

    /**
    Get the HashMap representation of the main cities of the specified country, filtering by type.

    Only the cities of the specified types will be retrieved.

    @param countryName Name of the country.

    @param typeCodes List of toponym codes to do the filtering.
    */
    public List<HashMap<String,String>> getMainCitiesByType(String countryName, List<String> typeCodes) {
        Country country = countriesTrie.get(countryName);
        List<HashMap<String,String>> mainCities = new ArrayList<HashMap<String,String>>();

        // for every zone
        for (Zone zone: country.getZones()) {  // XXX: place this in the zone controller
            // get the biggest rectangle containing the zone
            ArrayList<Double> extremeValues = lc.getZoneExtremeValues(zone);
            Double maxLat = extremeValues.get(2);
            Double minLat = extremeValues.get(3);
            Double maxLong = extremeValues.get(0);
            Double minLong = extremeValues.get(1);
            
            Interval<Double> intX = new Interval(minLat,maxLat);
            Interval<Double> intY = new Interval(minLong,maxLong);
            Interval2D<Double> rect = new Interval2D(intX, intY);

            // get the cities belonging to this rectangle
            ArrayList<Node<City>> nodes = citiesQuadTree.query2D(rect);
            // discard all cities not belonging to this zone
            // filter cities by type
            for (Node<City> node: nodes) {
                City city = node.value;
                if (typeCodes.contains(city.getType().getCode()) && city.getZone().equals(zone)) {
                    mainCities.add(cc.getMap(city));
                }
            }
        }
        return mainCities;
    }

    /**
    Get the HashMap representation of the top N main cities of the specified country.

    The cities will be ordered by population and only the top ones returned.

    @param countryName Name of the country.

    @param topN Number of cities to return.

    @return ArrayList of the cities, empty list if the country was not found.
    */
    public List<HashMap<String,String>> getMainCitiesByPopulation(String countryName, int topN) {
        Country country = countriesTrie.get(countryName);

        if (country == null) {
            return new ArrayList();
        }

        List<HashMap<String,String>> mainCities = new ArrayList<HashMap<String,String>>();
        MinHeap<City> mh = new MinHeap<City>(); // build minheap with topPercentage items <-- This is IR-based

        // for every zone
        for (Zone zone: country.getZones()) {  // XXX: place this in the zone controller
            // get the biggest rectangle containing the zone
            ArrayList<Double> extremeValues = lc.getZoneExtremeValues(zone);
            Double maxLat = extremeValues.get(2);
            Double minLat = extremeValues.get(3);
            Double maxLong = extremeValues.get(0);
            Double minLong = extremeValues.get(1);
            
            Interval<Double> intX = new Interval(minLat,maxLat);
            Interval<Double> intY = new Interval(minLong,maxLong);
            Interval2D<Double> rect = new Interval2D(intX, intY);

            // get the cities belonging to this rectangle
            ArrayList<Node<City>> nodes = citiesQuadTree.query2D(rect);
            for (Node<City> node: nodes) {
                City city = node.value;
                // discard all cities not belonging to this zone
                if (city.getZone().equals(zone)) {
                    // fill minheap with topN elements
                    if (mh.size() < topN)
                        mh.add(city);
                    // if population >= root => discard root, sink new city
                    else if (new Integer(city.getPopulation()) > mh.peek().getPopulation()) {
                        mh.remove();
                        mh.add(city);
                        
                    }
                    // if population <= root => discard
                }
            }
        }

        // sort the cities in the minheap
        ArrayList<City> cities = new ArrayList<City>();
        while (mh.size() > 0)
            cities.add(mh.remove());
        Collections.sort(cities, Collections.reverseOrder());

        for (City c: cities)
            mainCities.add(cc.getMap(c));

        return mainCities;
    }

    /**
    Get the main coastal and border cities of the specified country.

    @param countryName Name of the country.

    @param dist Distance to the coast or border to be considered coastal or border city.
    */
    public List<HashMap<String,String>> getMainCoastalBorderCities(String countryName, Double dist) {
        Country country = countriesTrie.get(countryName);
        List<HashMap<String,String>> mainCities = new ArrayList<HashMap<String,String>>();
        MinHeap<City> mh = new MinHeap<City>(); // build minheap with topPercentage items <-- This is IR-based

        // for every zone
        for (Zone zone: country.getZones()) {  // XXX: place this in the zone controller
            // get the biggest rectangle containing the zone
            ArrayList<Double> extremeValues = lc.getZoneExtremeValues(zone);
            Double maxLat = extremeValues.get(2);
            Double minLat = extremeValues.get(3);
            Double maxLong = extremeValues.get(0);
            Double minLong = extremeValues.get(1);
            
            Interval<Double> intX = new Interval(minLat,maxLat);
            Interval<Double> intY = new Interval(minLong,maxLong);
            Interval2D<Double> rect = new Interval2D(intX, intY);

            // get the cities belonging to this rectangle
            ArrayList<Node<City>> nodes = citiesQuadTree.query2D(rect);
            for (Node<City> node: nodes) {
                City city = node.value;
                // discard all cities not belonging to this zone
                if (city.getZone().equals(zone)) {
                    // get the 2 points closer to it
                    ArrayList<BorderPoint> closests = new ArrayList<BorderPoint>(2);
                    ArrayList<Node<BorderPoint>> closerNodes = borderPointsQuadTree.getCloserNodes(city.getLatitude(), city.getLongitude(), 0.5, 0.5);
                    for (Node<BorderPoint> closerNode: closerNodes) {
                        BorderPoint bp = closerNode.value;
                        if (closests.size() < 2) {
                            closests.add(bp);
                        } else if (bp.getLinearDistanceTo(city) < closests.get(0).getLinearDistanceTo(city)) {
                            if (closests.get(0).getLinearDistanceTo(city) < closests.get(1).getLinearDistanceTo(city)) {
                                closests.set(1, bp);
                            } else {
                                closests.set(0, bp);
                            }
                        }
                    }
                    // if distance < dist, add
                    Line l = new Line(closests.get(0), closests.get(1));
                    if (l.getDistanceToPoint(city) < dist) {
                        mainCities.add(cc.getMap(city));
                    }
                }
            }
        }

        return mainCities;
        // at the end, sort the top elements in the minheap

    }

    /**
    Get the main coastal and border cities of the specified country. (second implementation)

    @param countryName Name of the country.

    @param dist Distance to the coast or border to be considered coastal or border city.
    */
    public List<HashMap<String,String>> getAllCoastalBorderCities(String countryName, Double dist) {
        Country country = countriesTrie.get(countryName);
        List<HashMap<String,String>> allCities = new ArrayList<HashMap<String,String>>();
        for (Zone zone: country.getZones()) {  // XXX: place this in the zone controller
            // get the biggest rectangle containing the zone
            ArrayList<Double> extremeValues = lc.getZoneExtremeValues(zone);
            Double maxLat = extremeValues.get(2);
            Double minLat = extremeValues.get(3);
            Double maxLong = extremeValues.get(0);
            Double minLong = extremeValues.get(1);
            
            Interval<Double> intX = new Interval(minLat,maxLat);
            Interval<Double> intY = new Interval(minLong,maxLong);
            Interval2D<Double> rect = new Interval2D(intX, intY);

            List<Node<City>> nodeCities = new LinkedList<Node<City>>();
            // get the cities belonging to this rectangle
            ArrayList<Node<City>> nodes = citiesQuadTree.query2D(rect);
            Iterator<Node<City>> nodesItr = nodes.iterator();
            while (nodesItr.hasNext()) {
                Node<City> nodeCity = nodesItr.next();
                City city = nodeCity.value;
                if (city.getZone().getId().equals(zone.getId())) {
                    nodeCities.add(nodeCity);
                }
            }
            ArrayList<City> mainCities = lc.getAllBorderCitiesFromZone(zone, nodeCities, dist);
            for (City c: mainCities) {
                allCities.add(cc.getMap(c));
            }
        }

        return allCities;
    }

    /**
    Iterator over ALL instances of BorderPoints present in the system.
    */
    private class RawBorderPointsIterator implements Iterator<BorderPoint> {

        private Iterator<Country> countryItr;
        private Iterator<Zone> zoneItr;
        private Iterator<BorderPoint> borderPointItr;
        private Country currCountry;
        private Zone currZone;
        
        private BorderPoint currBorderPoint;

        public RawBorderPointsIterator() {
            countryItr = getAllRawCountriesIterator();
            currCountry = countryItr.next();
            zoneItr = currCountry.getZones().iterator();
            currZone = zoneItr.next();
            borderPointItr = currZone.getBorderpoints().iterator();
        }

        public boolean hasNext() {
            if (borderPointItr.hasNext()) {
                return true;
            }

            else if (zoneItr.hasNext()) {
                currZone = zoneItr.next();
                borderPointItr = currZone.getBorderpoints().iterator();
                return hasNext();
            }

            else if (countryItr.hasNext()) {
                currCountry = countryItr.next();
                zoneItr = currCountry.getZones().iterator();
                return hasNext();
            }

            return false;
        }

        public BorderPoint next() {
            return borderPointItr.next();
        }

        public void remove() {
            // not implmemented
        }
    }

    /**
    Get an iterator over all the border points instance in the database.
    */
    public Iterator<BorderPoint> getAllRawBorderPointsIterator() {
        return new RawBorderPointsIterator();
    }

    /**
    Get a nested list of (x,y) pairs representing the points of each zone of the 
    specified country.

    The first level represents the different zones, and inside each zone there are the
    (x,y) pair values of the points. Note that the order is inverted wrt the usual
    (latitude, longitude).

    @param countryName Name of the country.

    @return List of border points or an empty List if the country wasn't found.
    */
    public ArrayList<ArrayList<Double[]>> getCountryBorderPointsForDrawing(String countryName) {
        Country country = getRawCountry(countryName);
        if (country == null)  // country not found
            return new ArrayList();;
        ArrayList<ArrayList<Double[]>> allPoints = new ArrayList<ArrayList<Double[]>>();

        for (Zone zone: country.getZones()) {
            ArrayList<Double[]> zonePoints = new ArrayList<Double[]>();
            for (BorderPoint bp: zone.getBorderpoints()) {
                Double[] point = new Double[2];
                point[0] = bp.getLongitude(); point[1] = bp.getLatitude();  // x, y (longitude, latitude)
                zonePoints.add(point);
            }
            allPoints.add(zonePoints);
        }
        return allPoints;
    }

    /**
    Get the most extreme values for the specified country. This will effectively
    return the corners of the smallest rectangle containing the country.

    @param countryName Name of the country.

    @return ArrayList in the order (maxLong, minLong, maxLat, minLat) or an empty list
    if the country was not found.
    */
    public ArrayList<Double> getCountryExtremeValues(String countryName) {
        Country country = getRawCountry(countryName);
        if (country == null)  // country not found
            return new ArrayList();

        ArrayList<Double> extremeValues = new ArrayList<Double>(4);

        Double maxLong = -180.0;
        Double minLong = 180.0;
        Double maxLat = -90.0;
        Double minLat = 90.0;

        ArrayList<Double> zoneExtremeValues;
        // for every zone
        for (Zone zone: country.getZones()) {
            // update the country extreme values
            zoneExtremeValues = lc.getZoneExtremeValues(zone);
            maxLong = Math.max(maxLong, zoneExtremeValues.get(0));
            minLong = Math.min(minLong, zoneExtremeValues.get(1));
            maxLat = Math.max(maxLat, zoneExtremeValues.get(2));
            minLat = Math.min(minLat, zoneExtremeValues.get(3));
        }

        extremeValues.add(maxLong);
        extremeValues.add(minLong);
        extremeValues.add(maxLat);
        extremeValues.add(minLat);

        return extremeValues;
    }

    /**
    Get the HashMap representation of the countries in the same area of the specified one.
    
    I.e. the ones that would appear in a map if one draws a bounding box covering all the
    zones of the country.

    @param countryName Name of the country.

    @return HashMap list of countries in the same area or empty list if the specified country wasn't found.
    */
    public List<HashMap<String,String>> getCountriesInTheSameArea(String countryName) {
        ArrayList<Double> extremeValues = getCountryExtremeValues(countryName);

        ArrayList<HashMap<String,String>> countries = new ArrayList<HashMap<String,String>>();
        if (extremeValues.isEmpty()) // country not found
            return countries;

        Double maxLong = extremeValues.get(0);
        Double minLong= extremeValues.get(1);
        Double maxLat = extremeValues.get(2);
        Double minLat = extremeValues.get(3);

        Interval<Double> intX = new Interval(minLat,maxLat);
        Interval<Double> intY = new Interval(minLong,maxLong);
        Interval2D<Double> rect = new Interval2D(intX, intY);

        // get the nodes belonging to this rectangle
        ArrayList<Node<BorderPoint>> nodes = borderPointsQuadTree.query2D(rect);
        for (Node<BorderPoint> node: nodes) {
            BorderPoint bp = node.value;
            for (Zone zone: bp.getZones()) {
                HashMap<String,String> countryMap = getMap(zone.getCountry());
                if (!countries.contains(countryMap)) {
                    countries.add(countryMap);
                }
            }
        }

        return countries;
    }
}

