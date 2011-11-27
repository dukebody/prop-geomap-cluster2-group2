import java.util.*;

public interface ICountryController {

    private Trie<Country> countriesTrie;  // name index of all countries
    private QuadTree<Double,BorderPoint> borderPointsQuadTree;

    public CountryController();

    public boolean addCountry(String name, String code);
    // Create a new country with this name and code

    private HashMap<String,String> getCountry(String name);
    // get a country by name

    public HashMap<String,String> getMap(Country country);
    // build a hashmap with name/code of the given country

    public boolean modifyCountry(String oldName, String newName, String newCode);
    // return True if the country was modified sucessfully, False otherwise

    public boolean removeCountry(String name);
    // return True if the country was deleted sucessfully, False otherwise

    public Iterator<HashMap<String,String>> getAllCountriesIterator();
    // return an iterator over all the countries

    public Iterator<HashMap<String,String>> getPrefixCountriesIterator(String prefix);
    // return an iterator over all the countries starting with prefix

    public List<HashMap<String,String>> getNeighbourCountries(String name);
    // return a list of countries the country with the given name shares borders with
    // null if the given country does not exist

    public Double getSharedBorderLengthWith(String nameA, String nameB);
    // return the shared border length of the specified two countries
    // if there's not shared border or these countries do not exist, return 0

    public Double getTotalSharedBorderLength(String name);
    // return the total shared border length
    // return 0 if the given country does not exist

    public Double getTotalCoastlineLength(String name);
    // return the total coastline length
    // return 0 if the country does not exist

    public List<HashMap<String,String>> getMainCitiesByType(String countryName, List<String> types);
    // return a list of main cities of the given country filtering by the specified toponym types
    // the resulting hashmap will contain the name, id, utfname, latitude, longitude, type
    // and population of each city, as long as a "bordercoastal" field that is 'b' if the city
    // is a border city and 'c' if it's coastal

    public List<HashMap<String,String>> getMainCitiesByPopulation(String countryName, Double topPercentage);
    // return the topPercent% main cities of the given country sorted by population
}