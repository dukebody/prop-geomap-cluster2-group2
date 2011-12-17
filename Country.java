import java.util.*;

/**
   Basic data structure class for countries. Contains itss name and code.
*/

public class Country implements IGetId {

    private String name;
    private String code;
    private ArrayList<Zone> zones;

    /**
    Creates a country with the specified name.
    @param name A string representing the name of the country.
    */
    public Country(String name){
        this.name = name;
        this.zones = new ArrayList<Zone>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return getName();
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }

    public void setZones(ArrayList<Zone> zones) {
        this.zones = zones;
    }
}