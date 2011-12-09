import java.lang.*;
import java.util.*;


class BorderPointsDeserializer {

    private Iterator<HashMap<String,String>> itr;
    private CountryController cc;
    private ZonesController zc;
    
    public BorderPointsDeserializer(Iterator<HashMap<String,String>> itr, CountryController cc, ZonesController zc) {
        this.itr = itr;
        this.cc = cc;
        this.zc = zc;
    }

    void generate() {
        Country curr_Country = null;
        String curr_CountryName = null;
        Zone curr_Zone = null;
        String curr_ZoneId = null;

        while (itr.hasNext()) {
            // get next map
            HashMap<String,String> currentMap = itr.next();
            

            // if the country is different from the current one and does not exist, create it
            if ((curr_CountryName == null || !curr_CountryName.equals(currentMap.get("name_country"))) && cc.getCountry(currentMap.get("name_country")) == null) {
                cc.addCountry(currentMap.get("name_country"), currentMap.get("id_country"));
            }


            curr_CountryName = currentMap.get("name_country");

            // if the zone id is different from the current one, create a new zone for the current country
            // and set the zone as current
            if (curr_ZoneId == null || !currentMap.get("id_zone").equals(curr_ZoneId)) {
                curr_Country = cc.getRawCountry(curr_CountryName);
                ArrayList<BorderPoint> zp = new ArrayList<BorderPoint>();

                zc.createZone(curr_Country, zp);
                curr_ZoneId = currentMap.get("id_zone");
            }

            curr_Zone = curr_Country.getZones().get(curr_Country.getZones().size()-1);

            // add point to current country and zone
            if (curr_Country != null && curr_Zone != null) {
                
                Double latitude = new Double(currentMap.get("latitude"));
                Double longitude = new Double(currentMap.get("longitude"));
                zc.addBorderPointZone(curr_Zone, new BorderPoint(latitude,longitude));
            }

        }
    }
}