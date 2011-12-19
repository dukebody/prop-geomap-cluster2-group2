/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Guillermo
 */
public class ZonesController {

    private QuadTree<BorderPoint> borderPointsQuadTree;
    private boolean validateZones = false;

    public ZonesController(DataStorage ds){
        borderPointsQuadTree = ds.getBorderPointsQuadTree();
    }

    //ArrayList<Zone> getZonesFromCountry(String countryCode);

    public boolean createZone(Country country, ArrayList<BorderPoint> zonePoints) throws NullPointerException {
        try {
            if (validateZones && !LineController.checkGeometricalConsistenceForZone(zonePoints)) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Invalid zone-points:"); e.printStackTrace();
        }
        
        Zone zone = new Zone(country);

        for (BorderPoint bp: zonePoints) {
            addBorderPointZone(zone, bp);
        }

        ArrayList<Zone> countryZones = country.getZones();
        countryZones.add(zone);


        return true;
    }

    public ArrayList<String> getCountryZoneIds(Country country){
        ArrayList<String> zoneIds = new ArrayList<String>();

        for (int i=0;i<country.getZones().size();i++) {
            zoneIds.add(country.getZones().get(i).getId());
        }

        return zoneIds;
    }

    //Zone getZone(Integer id);

    public void addBorderPointZone(Zone zone, BorderPoint newBorderPoint){  //OR BOOLEAN?
         // if the point is already in the QuadTree, simply add the new zone to it
         Node<BorderPoint> node = borderPointsQuadTree.query(newBorderPoint.getLatitude(), newBorderPoint.getLongitude());
         BorderPoint existingBorderPoint;
         if (node != null) {
             existingBorderPoint = node.value;
             zone.addBorderPoint(existingBorderPoint, zone.getBorderpoints().size());
             if (!existingBorderPoint.getZones().contains(zone))
                existingBorderPoint.addZone(zone);
         }
 
        else {
            borderPointsQuadTree.insert(newBorderPoint.getLatitude(), newBorderPoint.getLongitude(), newBorderPoint);
            zone.addBorderPoint(newBorderPoint, zone.getBorderpoints().size());
            newBorderPoint.addZone(zone);
         }

    }

    public void modifyBorderPointZone(Zone zone, BorderPoint newBorderPoint, BorderPoint oldBorderPoint, int index){ //INDEX FROM 0 TO SIZE-1, //OR BOOLEAN?
        if (oldBorderPoint.getZones().size()==1) borderPointsQuadTree.remove(oldBorderPoint.getLatitude(), oldBorderPoint.getLongitude());
        borderPointsQuadTree.insert(newBorderPoint.getLatitude(), newBorderPoint.getLongitude(), newBorderPoint);
        zone.getBorderpoints().set(index, newBorderPoint);
        newBorderPoint.addZone(zone);
    }

    //Presentation layer should check that the zone is still geometrically ok and with at least 3 points
    public void deleteBorderPointZone(Zone zone, BorderPoint oldBorderPoint){    //OR BOOLEAN?
        if (oldBorderPoint.getZones().size()==1) borderPointsQuadTree.remove(oldBorderPoint.getLatitude(), oldBorderPoint.getLongitude());
        zone.removeBorderPoint(oldBorderPoint);
        oldBorderPoint.removeZone(zone);
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

    public void setValidateZones(boolean flag) {
        validateZones = flag;
    }
}
