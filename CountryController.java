import java.util.*;

class CountryController {

    private Trie<Country> countriesTrie;
    private QuadTree<Double,BorderPoint> borderPointsQuadTree;
    private LineController lc;

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

    public CountryController() {
        countriesTrie = new Trie<Country>();
        borderPointsQuadTree = new QuadTree<Double,BorderPoint>();
        lc = new LineController();
    }

    private HashMap<String,String> getMap(Country country) {
        HashMap<String,String> map = new HashMap<String,String>();
        if (country != null) {
            map.put("name", country.getName());
            map.put("code", country.getCode());
            return map;
        }

        return null;
    }

    public List<HashMap<String,String>> getMap(List<Country> countryList) {
        List<HashMap<String,String>> mapList = new LinkedList<HashMap<String,String>>();
        for (Country country: countryList) {
            mapList.add(getMap(country));
        }

        return mapList;
    }


    public boolean addCountry(String name, String code) {
        Country country = new Country(name);
        country.setCode("code");
        if (countriesTrie.get(name) == null && name != null) {
            countriesTrie.put(country, name);
            return true;
        }

        return false; // a country with this name already exists
    }

    public HashMap<String,String> getCountry(String name) {
        return getMap(countriesTrie.get(name));
    }

    public Country getRawCountry(String name) {
        return countriesTrie.get(name);
    }

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

    public boolean removeCountry(String name) {
        return countriesTrie.remove(name, name);
    }

    public CountriesIterator getAllCountriesIterator() {
        return new CountriesIterator(countriesTrie.iterator());
    }

    public CountriesIterator getPrefixCountriesIterator(String prefix) {
        return new CountriesIterator(countriesTrie.iteratorPrefix(prefix));
    }

    public List<HashMap<String,String>> getNeighborCountries(String name) {
        Country country = countriesTrie.get(name);
        return getMap(lc.getNeighborCountries(country));
    }

    public Double getSharedBorderLengthWith(String nameA, String nameB) {
        Country countryA = countriesTrie.get(nameA);
        Country countryB = countriesTrie.get(nameB);
        return lc.getTwoCountriesBordersLength(countryA, countryB);
    }

    public Double getTotalSharedBorderLength(String name) {
        Country country = countriesTrie.get(name);
        return lc.getWholeCountryBordersLength(country);
    }

    public Double getTotalCoastlineLength(String name) {
        Country country = countriesTrie.get(name);
        return lc.getCountryCoastalLength(country);
    }

    public List<HashMap<String,String>> getMainCitiesByType(String countryName, List<String> types) {
        Country country = countriesTrie.get(countryName);
        // for every zone
        for (Zone z: country.getZones()) {
            
        }
            // get the biggest rectangle containing the zone
            // get the cities belonging to this rectangle
            // discard all cities not belonging to this zone
            // filter cities by type
            List<HashMap<String,String>> mainCities = new ArrayList<HashMap<String,String>>();

            return mainCities;
    }

    public List<HashMap<String,String>> getMainCitiesByPopulation(String countryName, Double topPercentage) {
        // for every zone
            // get the biggest rectangle containing the zone
            // get the cities belonging to this rectangle
            // discard all cities not belonging to this zone
            // build minheap with topPercentage items <-- This is IR-for
            // based every city in the list
                // if population < root => discard
                // if population >= root => discard root, sink new city
                // at the end, sort the top elements in the minheap
            List<HashMap<String,String>> mainCities = new ArrayList<HashMap<String,String>>();

            return mainCities;
    }
}

