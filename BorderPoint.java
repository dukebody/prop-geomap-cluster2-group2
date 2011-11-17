/*
 *  Class BorderPoint
 *  @author Sergi Sisó Gòdia
 */

public class BorderPoint extends Point {

private Zone zone;

public BorderPoint(double latitude, double longitude, Zone zone) {
super(latitude,longitude);
this.zone = zone;
}

public Zone getZone() {
return zone;
}

}