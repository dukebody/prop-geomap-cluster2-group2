/*
 *  Class BorderPoint
 * 
 *  @author Sergi Sisó Gòdia
 */

import java.util.*;


public class BorderPoint extends Point {
    private List<Zone> zones = new ArrayList<Zone>(2);

    public BorderPoint(double latitude, double longitude) {
        super(latitude,longitude);
    }

    public BorderPoint(double latitude, double longitude, Zone zone) {
        super(latitude,longitude);
        zones.add(zone);
    }
    
    public boolean addZone(Zone zone){
        if(zone==null)return false;
        return zones.add(zone);
    }
    
    public List<Zone> getZones(){
        return zones;
    }
    
    public Iterator getZonesIterator(){
        return zones.iterator();
    }
    
    public boolean removeZone(Zone zone){
        return zones.remove(zone);
    }
}
