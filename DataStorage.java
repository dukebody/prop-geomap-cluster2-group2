/*
author: Israel
*/

/**
This class encapsulates the whole data storage of the living application. Everytime a new
DataStorage instance is created we get a new fresh empty data workspace.

An instance of this class is expected to be passed to the constructor of every controller
for them to get hold of any data storage resource needed.
*/
class DataStorage {
    private QuadTree<BorderPoint> borderPointsQuadTree;
    private QuadTree<City> citiesQuadTree;
    private Trie<Country> countriesTrie;
    private Trie<TypeToponym> typeToponymsTrie;
    private Trie<City> citiesTrie;

    public DataStorage() {
        borderPointsQuadTree = new QuadTree<BorderPoint>();
        citiesQuadTree = new QuadTree<City>();
        countriesTrie = new Trie<Country>();
        typeToponymsTrie = new Trie<TypeToponym>();
        citiesTrie = new Trie<City>();
    }

    public QuadTree<BorderPoint> getBorderPointsQuadTree() {
        return borderPointsQuadTree;
    }

    public QuadTree<City> getCitiesQuadTree() {
        return citiesQuadTree;
    }

    public Trie<Country> getCountriesTrie() {
        return countriesTrie;
    }

    public Trie<TypeToponym> getTypeToponymsTrie() {
        return typeToponymsTrie;
    }

    public Trie<City> getCitiesTrie() {
        return citiesTrie;
    }
}