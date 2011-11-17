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
public Toponym(String nameASCII, String nameUTF,double latitude, double longitude, TypeToponym type) {
super(latitude, longitude);
this.nameASCII = nameASCII;
this.nameUTF = nameUTF;
this.type = type;
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
public String getId() {
return id;
}
}