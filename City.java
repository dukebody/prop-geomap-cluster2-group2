class City extends Toponym implements Comparable<City> {

    private int population;
    private Zone zone;

    public City(String nameASCII, String nameUTF, double latitude, double longitude, TypeToponym type, int population) {
        super(nameASCII, nameUTF, latitude, longitude, type);
        this.population = population;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int compareTo(City c2) {  // XXX: Test this!!
        if (this.getPopulation() < c2.getPopulation())
            return -1;
        if (this.getPopulation() > c2.getPopulation())
            return 1;
        return 0;
    }
}