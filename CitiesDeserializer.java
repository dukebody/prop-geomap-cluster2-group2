import java.lang.*;
import java.util.*;


class CitiesDeserializer implements IDeserializer {
    
    private Iterator<HashMap<String,String>> itr;
    private CitiesController cc;

    private static Set<String> cityToponymCodes;
    
    public CitiesDeserializer(Iterator<HashMap<String,String>> itr, CitiesController cc) {
        this.itr = itr;
        this.cc = cc;

        cityToponymCodes = new HashSet<String>();
        cityToponymCodes.add("PPL");
        cityToponymCodes.add("PPLA");
        cityToponymCodes.add("PPLA2");
        cityToponymCodes.add("PPLA3");
        cityToponymCodes.add("PPLA4");
        cityToponymCodes.add("PPLC");
        cityToponymCodes.add("PPLF");
        cityToponymCodes.add("PPLG");
        cityToponymCodes.add("PPLL");
        cityToponymCodes.add("PPLQ");
        cityToponymCodes.add("PPLR");
        cityToponymCodes.add("PPLS");
        cityToponymCodes.add("PPLW");
        cityToponymCodes.add("PPLX");
        cityToponymCodes.add("STLMT");
    }

    public void generate() {

        for (String code: cityToponymCodes) {
            cc.createToponymType(code, code, code); // XXX: THIS IS WRONG - READ FROM FILE!!!
        }

        while (itr.hasNext()) {
            HashMap<String,String> currentMap = itr.next();

            // create only cities
            if (cityToponymCodes.contains(currentMap.get("CodiToponim"))) {
                boolean result = cc.addCity(currentMap.get("Nom_ASCII"), currentMap.get("Nom_UTF"), 
                    new Double(currentMap.get("Latitud")), new Double(currentMap.get("Longitud")),
                    currentMap.get("CodiToponim"), new Integer(currentMap.get("Poblacio")));
            }
        }
    }
}
