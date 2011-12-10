import java.util.*;


class ToponymTypesDeserializer implements IDeserializer {

    Iterator<HashMap<String,String>> itr;
    CitiesController cc;

    public ToponymTypesDeserializer(Iterator<HashMap<String,String>> itr, CitiesController cc) {
        this.itr = itr;
        this.cc = cc;
    }

    public void generate() {
        while (itr.hasNext()) {
            HashMap<String,String> type = itr.next();
            cc.createToponymType(type.get("name"), type.get("category"), type.get("type_code"));
        }
    }
}
