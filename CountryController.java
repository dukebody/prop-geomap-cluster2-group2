import java.util.*;

class CountryController {

    private Trie<Country> countriesTrie;
    private QuadTree<Double,BorderPoint> borderPointsQuadTree;

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
}

