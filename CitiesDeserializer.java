import java.lang.*;
import java.util.*;


class CitiesDeserializer implements IDeserializer {
    
    private Iterator<HashMap<String,String>> itr;
    private CitiesController cc;

    private static Set<String> cityToponymCodes;
    
    public CitiesDeserializer(Iterator<HashMap<String,String>> itr, CitiesController cc) {
        this.itr = itr;
        this.cc = cc;
    }

    public void generate() {
        while (itr.hasNext()) {
            HashMap<String,String> currentMap = itr.next();

            // create only cities with population > 0
            if (cc.getToponymType(currentMap.get("CodiToponim")) != null && !currentMap.get("Poblacio").equals("0")) {
                boolean result = cc.addCity(currentMap.get("Nom_ASCII").replaceAll("_", " "), 
                    currentMap.get("Nom_UTF").replaceAll("_", " "), 
                    new Double(currentMap.get("Latitud")), 
                    new Double(currentMap.get("Longitud")),
                    currentMap.get("CodiToponim"), 
                    new Integer(currentMap.get("Poblacio")));
            }
        }
    }
}
