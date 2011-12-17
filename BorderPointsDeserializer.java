import java.lang.*;
import java.util.*;

/**
Component to transform the output of the BorderPointsFileParser into the
proper countries and zones.
*/
public class BorderPointsDeserializer implements IDeserializer {

    private Iterator<HashMap<String,String>> itr;
    private CountryController cc;
    private ZonesController zc;

    /**
    Prepare a border points deserializer for the borderpoints HashMap representation
    coming from the specified iterator.

    @param itr Iterator of a BorderPointsFileParser.

    @param cc CountryController to handle the country creation.

    @param zc ZonesController to handle the zones creation.
    */
    public BorderPointsDeserializer(Iterator<HashMap<String,String>> itr, 
        CountryController cc, ZonesController zc) {
        this.itr = itr;
        this.cc = cc;
        this.zc = zc;

    }

    /**
    Create all the countries with their zones and borderpoints coming from the iterator.
    */
    public void generate() {
        Country curr_Country = null;
        String curr_CountryName = "INVALID_COUNTRYNAME";
        Zone curr_Zone = null;
        String curr_ZoneId = "INVALID_ZONEID";

        ArrayList<BorderPoint> zp = new ArrayList<BorderPoint>();
        HashMap<String,String> currentMap;

        while (itr.hasNext()) {
            // get next map
            currentMap = itr.next();

            // if the zone id is different from the current one, create the old zone for the old country
            // and start storing new points for the new zone
            if (!currentMap.get("id_zone").equals(curr_ZoneId)) {
                if (!zp.isEmpty()) {
                    zc.createZone(curr_Country, zp);
                }
                zp.clear();

                curr_ZoneId = currentMap.get("id_zone");
            }

            // if the country is different from the current one create it
            // note that it won't be created if it already exists
            if (!curr_CountryName.equals(currentMap.get("name_country"))) {
                cc.addCountry(currentMap.get("name_country"), currentMap.get("id_country"));
                curr_CountryName = currentMap.get("name_country");
                curr_Country = cc.getRawCountry(curr_CountryName);
            }

            // add current point to the list
            Double latitude = new Double(currentMap.get("latitude"));
            Double longitude = new Double(currentMap.get("longitude"));
            zp.add(new BorderPoint(latitude,longitude));

        }

        // create lastZone
        zc.createZone(curr_Country, zp);
    }
}