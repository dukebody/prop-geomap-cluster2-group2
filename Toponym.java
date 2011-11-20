
/*
 *  Class Toponym
 * 
 *  @author Sergi Sisó Gòdia
 */

public class Toponym extends Point implements IGetId {

    private String nameASCII;
    private String nameUTF;
    private TypeToponym type;
    private String id;
    private static int next_id = 0;
    
    public Toponym(String nameASCII, String nameUTF, double latitude, double longitude, TypeToponym type) {
        super(latitude, longitude);
        this.nameASCII = nameASCII;
        this.nameUTF = nameUTF;
        this.type = type;
        this.id = ((Integer)next_id++).toString();
    }

    public Toponym(String nameASCII, String nameUTF, double latitude, double longitude) {
        super(latitude, longitude);
        this.nameASCII = nameASCII;
        this.nameUTF = nameUTF;
        this.type = null;
        this.id = ((Integer)next_id++).toString();
    }

    public String getNameASCII() {
        return nameASCII;
    }

    public String getNameUTF() {
        return nameUTF;
    }

    public TypeToponym getType() {
        return type;
    }

    public void setNameASCII(String n) {
        this.nameASCII = n;
    }

    public void setNameUTF(String nUTF) {
        this.nameUTF = nUTF;
    }

    public void setType(TypeToponym tt) {
        this.type = tt;
    }

    public String getId() {
        return id;
    }
    
}
