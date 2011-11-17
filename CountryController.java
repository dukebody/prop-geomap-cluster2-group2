import java.util.*;

class CountryController {

    private Trie<Country> countriesTrie;

    public CountryController() {
        countriesTrie = new Trie<Country>();
    }

    public boolean addCountry(String name) {
        Country country = new Country(name);
        if (countriesTrie.get(name) == null) {
            countriesTrie.put(country, name);
            return true;
        }

        return false; // a country with this name already exists
    }

    public Country getCountry(String name) {
        return countriesTrie.get(name);
    }

    public boolean modifyCountry(String oldName, String newName) {
        Country oldCountry = countriesTrie.get(oldName);
        if (oldCountry != null) {  // the origin country exists
            if (newName != null && newName.length() != 0 && countriesTrie.get(newName) == null) {
                // no null or zero length target name, and no country with the target name

                // copy country
                oldCountry = countriesTrie.get(oldName);
                Country newCountry = new Country(oldName);
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

    public Iterator<Country> getAllCountriesIterator() {
        return countriesTrie.iterator();
    }

    public Iterator<Country> getPrefixCountriesIterator(String prefix) {
        return countriesTrie.iteratorPrefix(prefix);
    }

    public ZonesController getZonesController(String countryName) {
        if (getCountry(countryName) != null)
            return new ZonesController(countryName);
        else
            return null;
    }

}

