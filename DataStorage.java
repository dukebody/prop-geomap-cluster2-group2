/*
author: Israel
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