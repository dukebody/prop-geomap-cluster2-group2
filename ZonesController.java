import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Guillermo
 */
public class ZonesController {

    private QuadTree<Double,BorderPoint> borderPointsQuadTree;

    public ZonesController(){
        borderPointsQuadTree = new QuadTree<Double,BorderPoint>();
    }

    //ArrayList<Zone> getZonesFromCountry(String countryCode);

    public boolean createZone(Country country, ArrayList<BorderPoint> zonePoints){
        ArrayList<Zone> countryZones = country.getZones();

        try{
            Zone zone = new Zone(country);

            for (int i=0;i<zonePoints.size();i++) {
                zone.addBorderPoint(zonePoints.get(i), i);
                borderPointsQuadTree.insert(zonePoints.get(i).getLatitude(), zonePoints.get(i).getLongitude(),zonePoints.get(i));
            }

            countryZones.add(zone);
            country.setZones(countryZones);
        } catch (Exception e) {
            System.out.print("Exception at creating a new zone for country "+country.getName()+": " + e.getMessage());
            return false;
        }

        return true;
    }

    //Zone getZone(Integer id);

    public void addBorderPointZone(Zone zone, BorderPoint newBorderPoint){  //OR BOOLEAN?
        borderPointsQuadTree.insert(newBorderPoint.getLatitude(), newBorderPoint.getLongitude(), newBorderPoint);
        zone.addBorderPoint(newBorderPoint, zone.getBorderpoints().size());
    }

    public void modifyBorderPointZone(Zone zone, BorderPoint newBorderPoint, BorderPoint oldBorderPoint, int index){ //INDEX FROM 0 TO SIZE-1, //OR BOOLEAN?
        if (oldBorderPoint.getZones().size()==1) borderPointsQuadTree.remove(oldBorderPoint.getLatitude(), oldBorderPoint.getLongitude());
        borderPointsQuadTree.insert(newBorderPoint.getLatitude(), newBorderPoint.getLongitude(), newBorderPoint);
        zone.getBorderpoints().set(index, newBorderPoint);
    }

    public void deleteBorderPointZone(Zone zone, BorderPoint oldBorderPoint){    //OR BOOLEAN?
        if (oldBorderPoint.getZones().size()==1) borderPointsQuadTree.remove(oldBorderPoint.getLatitude(), oldBorderPoint.getLongitude());
        zone.removeBorderPoint(oldBorderPoint);
    }

    public boolean deleteZonesFromCountry(Country country){
        boolean isOk = true;
        Zone zone = null;

        try {
            for (int i=0;i<country.getZones().size();i++) {
                zone = country.getZones().get(i);
                isOk = deleteZone(zone);
                if (!isOk) break;
            }
        } catch (Exception e) {
            System.out.println("Exception at 'deleteZonesFromCountry' "+country.getName()+": "+e.getMessage());
            return false;
        }

        return isOk;
    }

    public boolean deleteZone(Zone zone){
        BorderPoint borderPoint = null;
        
        try {
            for (int j=0;j<zone.getBorderpoints().size();j++) {
                borderPoint = zone.getBorderpoint(j);
                if (borderPoint.getZones().size()>1) {
                    borderPoint.removeZone(zone);
                } else if (borderPoint.getZones().size()==1) {
                    borderPointsQuadTree.remove(borderPoint.getLatitude(), borderPoint.getLongitude());
                }
            }
        } catch (Exception e) {
            System.out.println("Exception at 'deleteZone' "+zone.getId()+": "+e.getMessage());
            return false;
        }

        return true;
    }

    //Boolean validateCorrectGeographicZone(ArrayList<Point> points);
}
