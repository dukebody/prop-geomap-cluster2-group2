import java.lang.*;
import java.util.*;

/**
Component to transform the output of the ToponymsFileParser into the
proper cities.

Note that this deserializer should be used *after* importing the borderpoints
and the toponymtypes into the data storage. Otherwise the cities might not be
assigned to the right zones or not imported at all (due to toponym 
type restrictions).
*/
class CitiesDeserializer implements IDeserializer {
    
    private Iterator<HashMap<String,String>> itr;
    private CitiesController cc;

    private static Set<String> cityToponymCodes;

    /**
    Prepare a cities deserializer for the toponyms HashMap representation
    coming from the specified iterator.

    @param itr Iterator of a BorderPointsFileParser.

    @param cc CitiesController to handle the city creation.
    */
    public CitiesDeserializer(Iterator<HashMap<String,String>> itr, CitiesController cc) {
        this.itr = itr;
        this.cc = cc;
    }

    /**
    Create all the cities coming from the iterator.

    Cities of a type different from the currently loaded ones and cities
    with 0 population will be dropped.
    */
    public void generate() {
        // hashset for fast searching
        cityToponymCodes = new HashSet<String>();
        Iterator<HashMap<String,String>> ttItr = cc.getToponymTypesIterator();
        while (ttItr.hasNext()) {
            cityToponymCodes.add(ttItr.next().get("code"));
        }

        while (itr.hasNext()) {
            HashMap<String,String> currentMap = itr.next();

            // create only cities with population > 0
            if (cityToponymCodes.contains(currentMap.get("CodiToponim")) && !currentMap.get("Poblacio").equals("0")) {
                cc.addCity(currentMap.get("Nom_ASCII"), 
                    currentMap.get("Nom_UTF"), 
                    new Double(currentMap.get("Latitud")), 
                    new Double(currentMap.get("Longitud")),
                    currentMap.get("CodiToponim"), 
                    new Integer(currentMap.get("Poblacio")));
            }
        }
    }
}
