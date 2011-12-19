/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// package Controllers;

// import Beans.BorderPoint;
// import Beans.City;
// import Beans.Country;
// import Beans.DataStorage;
// import Beans.Interval;
// import Beans.Interval2D;
// import Beans.Zone;
// import Beans.Line;
// import Beans.Node;
// import Beans.Point;
// import Beans.QuadTree;
import java.util.*;
import java.math.*;

/**
 *
 * @author xD
 */
public class LineController {

    public LineController(DataStorage ds){}

    //ArrayList<Line> getLinesFromZone(Zone zone);  //ALREADY IMPLEMENTED IN ZONE AS A GETTER

    //Void determineAndSetLineType(Line line);  //ALREADY IMPLEMENTED IN LINE AS A SETTER

    //Double calculateLineDistance(Point point1, Point point2); //ALREADY IMPLEMENTED IN POINT FOR 2 RANDOM POINTS, AND CALLED FROM LINE

    //Boolean createZoneLines(ArrayList<Point> zonePoints);

    //Boolean createLine(Point point1, Point point2);

    //Boolean updateZoneLines(ArrayList<Point> zonePoints);

    //public boolean deleteLinesFromCountry(Country country);   //THE LINES ARE NOT STORED ANYWHERE SO NOT NECESARY BY NOW...

    //public boolean deleteLine(Line line); //DOESN'T SEEM NECESARY, LINE OBJECTS ONLY FOR LOCAL PURPOSES BY NOW...

    /*
    METHOD FROM COUNTRYCONTROLLER THAT WILL CALL THE ONE IMPLEMENTED ABOVE:
    public List<HashMap<String,String>> getNeighbourCountries(String name);
    // return a list of countries the country with the given name shares borders with
    // null if the given country does not exist
     */
    public ArrayList<Country> getNeighborCountries(Country country){
        ArrayList<Country> neighborsCountries = new ArrayList<Country>();
        ArrayList<String> neighborsIds = new ArrayList<String>();
        ArrayList<Line> countryBorderLines = new ArrayList<Line>();
        ArrayList<Country> lineCountries = new ArrayList<Country>();
        List<BorderPoint> countryPoints = new ArrayList<BorderPoint>();
        Zone zone = null;
        Line line = null;

        for (int i=0;i<country.getZones().size();i++){
            zone = country.getZones().get(i);
            countryPoints = zone.getBorderpoints();
            for (int j=0;j<countryPoints.size();j++) {
                if (j<(countryPoints.size()-1)) line = new Line(countryPoints.get(j),countryPoints.get(j+1));
                else line = new Line(countryPoints.get(j),countryPoints.get(0));

                if (line.getType().equals("border")) countryBorderLines.add(line);
            }
        }

        for (int i=0;i<countryBorderLines.size();i++) {
            lineCountries = countryBorderLines.get(i).getCountries();
            for (int j=0;j<lineCountries.size();j++) {
                if (!lineCountries.get(j).getId().equals(country.getId()) &&
                    !neighborsIds.contains(lineCountries.get(j).getId())) {
                    neighborsCountries.add(lineCountries.get(j));
                    neighborsIds.add(lineCountries.get(j).getId());
                    break;
                }
            }
        }

        return neighborsCountries;
    }

    /*
    METHOD FROM COUNTRYCONTROLLER THAT WILL CALL THE ONE IMPLEMENTED ABOVE:
    public Double getTotalSharedBorderLength(String name);
    // return the total shared border length
    // return 0 if the given country does not exist
     */
    public double getWholeCountryBordersLength(Country country){
        double borderLength = 0;

        for (int i=0;i<country.getZones().size();i++) {
            borderLength+=getWholeZoneBordersLength(country.getZones().get(i));
        }

        return borderLength;
    }

    private double getWholeZoneBordersLength(Zone zone){
        Line line = null;
        double borderLength = 0;

        for (int i=0;i<zone.getBorderpoints().size();i++) {
            if (i<zone.getBorderpoints().size()-1) line = new Line(zone.getBorderpoint(i),zone.getBorderpoint(i+1));
            else line = new Line(zone.getBorderpoint(i),zone.getBorderpoint(0));

            if (line.getType().equals("border")) borderLength+=line.getLength();
        }

        return borderLength;
    }

    /*
    METHOD FROM COUNTRYCONTROLLER THAT WILL CALL THE ONE IMPLEMENTED ABOVE:
    public Double getSharedBorderLengthWith(String nameA, String nameB);
    // return the shared border length of the specified two countries
    // if there's not shared border or these countries do not exist, return 0
     */
    public double getTwoCountriesBordersLength(Country mainCountry, Country neighborCountry){
        ArrayList<Line> mainCountryBorderLines = new ArrayList<Line>();
        ArrayList<Country> lineCountries = new ArrayList<Country>();
        double borderLength = 0;
        Zone zone = null;
        Line line = null;

        for (int i=0;i<mainCountry.getZones().size();i++) {
            zone = mainCountry.getZones().get(i);
            for (int j=0;j<zone.getBorderpoints().size();j++) {
                if (j<zone.getBorderpoints().size()-1) line = new Line(zone.getBorderpoint(j),zone.getBorderpoint(j+1));
                else line = new Line(zone.getBorderpoint(j),zone.getBorderpoint(0));

                if (line.getType().equals("border")) mainCountryBorderLines.add(line);
            }
        }

        for (int i=0;i<mainCountryBorderLines.size();i++) {
            lineCountries = mainCountryBorderLines.get(i).getCountries();
            for (int j=0;j<lineCountries.size();j++) {
                if (lineCountries.get(j).equals(neighborCountry)) {
                    borderLength += mainCountryBorderLines.get(i).getLength();
                    break;
                }
            }
        }

        return borderLength;
    }

    /*private double getTwoZonesBordersLength(Zone zoneCountry1, ArrayList<Zone> zonesCountry2){
        Line line = null;
        double borderLength = 0;

        for (int i=0;i<zoneCountry1.getBorderpoints().size();i++) {
            if (i<zoneCountry1.getBorderpoints().size()-1) line = new Line(zoneCountry1.getBorderpoint(i),zoneCountry1.getBorderpoint(i+1));
            else line = new Line(zoneCountry1.getBorderpoint(i),zoneCountry1.getBorderpoint(0));

            if (line.getType().equals("border")) borderLength+=line.getLength();
        }


        //obtain zone 1's border lines
        //loop zonesCountry2
        //obtain zones Country2's border lines
        //for each of this zones, compare and sum common lines distaces with zone 1
        //return the result
        return borderLength;
    }*/

    /*
    METHOD FROM COUNTRYCONTROLLER THAT WILL CALL THE ONE IMPLEMENTED ABOVE:
    public Double getTotalCoastlineLength(String name);
    // return the total coastline length
    // return 0 if the country does not exist
     */
    public double getCountryCoastalLength(Country country){
        double coastalLength = 0;

        for (int i=0;i<country.getZones().size();i++) {
            coastalLength+=getZoneCoastalLength(country.getZones().get(i));
        }

        return coastalLength;
    }

    public double getZoneCoastalLength(Zone zone){
        Line line = null;
        double coastalLength = 0;

        for (int i=0;i<zone.getBorderpoints().size();i++) {
            if (i<zone.getBorderpoints().size()-1) line = new Line(zone.getBorderpoint(i),zone.getBorderpoint(i+1));
            else line = new Line(zone.getBorderpoint(i),zone.getBorderpoint(0));

            if (line.getType().equals("coastal")) coastalLength+=line.getLength();
        }

        return coastalLength;
    }

    public String ZoneFinderForPoint(ArrayList<Country> countries, Point point){
        //Check the valid countries
        if ((countries == null) || (countries.isEmpty())) return "";

        Country country = null;
        Zone zone = null;

        for (int i=0;i<countries.size();i++) {
            country = countries.get(i);
            for (int j=0;j<country.getZones().size();j++) {
                zone = country.getZones().get(j);
                if (checkIfPointIsInsideZone(point, zone)) return zone.getId();
            }
        }

        return "";
    }

    public static Zone seekZoneWherePointBelongsTo(Point point, QuadTree<BorderPoint> allCountriesPoints){
        ArrayList<String> checkedZones = new ArrayList<String>();
        BorderPoint checkedPoint = null;
        Zone zone = null;

        ArrayList<Node<BorderPoint>> nodes =  allCountriesPoints.getCloserNodes(point.getLatitude(), point.getLongitude(), 0.5, 0.5);

        for (int i=0;i<nodes.size();i++) {
            checkedPoint = nodes.get(i).value;

            for (int j=0;j<checkedPoint.getZones().size();j++) {
                zone = checkedPoint.getZones().get(j);
                if (!checkedZones.contains(zone.getId())) {
                    if (checkIfPointIsInsideZone(point, zone)) {
                        return zone;
                    }
                    checkedZones.add(zone.getId());
                }
            }
        }

        return null;
    }

    public static boolean checkGeometricalConsistenceForZone(ArrayList<BorderPoint> zonePoints){
        //Check the valid points
        if (zonePoints == null) return false;

        //Killing the last point (there first point appears repeted at the end in the BorderPoints File)
        zonePoints.remove(zonePoints.size() - 1);

        //Check points can form an area
        if (zonePoints.size() < 3) return false;

        //ArrayList<BorderPoint> repeatedPointCheck = new ArrayList<BorderPoint>();
        ArrayList<BorderPoint> altPointsA = new ArrayList<BorderPoint>();
        ArrayList<BorderPoint> altPointsB = new ArrayList<BorderPoint>();
        ArrayList<Double> altPendents = new ArrayList<Double>();
        ArrayList<Double> altCuttingPoints = new ArrayList<Double>();
        ArrayList<Double> crossPoint = new ArrayList<Double>(2);
        BorderPoint localPointA = null;
        BorderPoint localPointB = null;
        Double localPendent = new Double(0);
        Double localCuttingPoint = new Double(0);
        Double majorXRange1 = new Double(0);
        Double minorXRange1 = new Double(0);
        Double majorYRange1 = new Double(0);
        Double minorYRange1 = new Double(0);
        Double majorXRange2 = new Double(0);
        Double minorXRange2 = new Double(0);
        Double majorYRange2 = new Double(0);
        Double minorYRange2 = new Double(0);
        Double crossX = new Double(0);
        Double crossY = new Double(0);
        int altCont = 0;
        int aux = 0;

        for (int i=0;i<zonePoints.size();i++) {
            //Check there are no repeated points
            for (int j=0;j<i;j++) {
                if (i == 0) aux = zonePoints.size() - 1;
                else aux = j;
                try {
                    if ((zonePoints.get(i).getLatitude() == zonePoints.get(aux).getLatitude()) &&
                        (zonePoints.get(i).getLongitude() == zonePoints.get(aux).getLongitude())) {
                        System.out.println("problemas aqui0 en "+i+"!");
                        return false;
                    } //else repeatedPointCheck.add(zonePoints.get(i));
                } catch (Exception e) { System.out.println("peta aqui0 en "+i+": "+e.getMessage()); }
            }

            //Store old points info as the ones for being compared with the following ones
            if (i > 0) {
                altPointsA.add(new BorderPoint(localPointA.getLatitude(), localPointA.getLongitude()));
                altPointsB.add(new BorderPoint(localPointB.getLatitude(), localPointB.getLongitude()));
                altPendents.add(new Double(localPendent));
                altCuttingPoints.add(new Double(localCuttingPoint));
                altCont++;
            }

            //Calculate the properties of the current point
            try{
                localPointA = zonePoints.get(i);
                if (i < (zonePoints.size() - 1)) localPointB = zonePoints.get(i+1);
                else localPointB = zonePoints.get(0);

                //Chech that the current pendent is not the exact same as the previous one
                try {
                    if ((altCont > 0) && checkIfOppositePendents(localPointA, localPointB, altPointsA.get(altCont - 1), altPointsB.get(altCont - 1))) {
                        System.out.println("problemas aqui1 en "+i+"! (consecutive lines with OPPOSITE pendents)");
                        return false;
                    }
                } catch (Exception e) { System.out.println("peta aqui1 en "+i+": "+e.getMessage()); }

                localPendent = calculatePendent(localPointA, localPointB);
                localCuttingPoint = calculateCuttingPoint(localPointA, localPointB);
            } catch (Exception e) { System.out.println("peta aqui2 en "+i+": "+e.getMessage()); }

            if (i > 1) {
                //Make loop to previous lines and check that current line doesn't cut or get cut at any point
                for (int j=0;j<(altCont-1);j++) {
                    //First make sure both lines to be compared (consecutives) don't share a same point
                    if (!(((i+1) == zonePoints.size()) && (j == 0))) {
                        //Calculate crossing point's coordinates of two infinite lines
                        crossPoint = calculateCrossPoint(localPendent, localCuttingPoint, localPointA, altPendents.get(j), altCuttingPoints.get(j), altPointsA.get(j));

                        if ((crossPoint != null) && (crossPoint.size() == 2)) {
                            crossX = crossPoint.get(0);                             //Longitude
                            crossY = crossPoint.get(1);                             //Latitude
                            //Check if the current line would not include the crossing point inside itself
                            try {
                                if (localPointA.getLatitude() > localPointB.getLatitude()) {
                                    majorYRange1 = localPointA.getLatitude();
                                    minorYRange1 = localPointB.getLatitude();
                                } else {
                                    majorYRange1 = localPointB.getLatitude();
                                    minorYRange1 = localPointA.getLatitude();
                                }
                                if (localPointA.getLongitude() > localPointB.getLongitude()) {
                                    majorXRange1 = localPointA.getLongitude();
                                    minorXRange1 = localPointB.getLongitude();
                                } else {
                                    majorXRange1 = localPointB.getLongitude();
                                    minorXRange1 = localPointA.getLongitude();
                                }
                                //Check if the current previous line would not include the crossing point inside itself
                                if (altPointsA.get(j).getLatitude() > altPointsB.get(j).getLatitude()) {
                                    majorYRange2 = altPointsA.get(j).getLatitude();
                                    minorYRange2 = altPointsB.get(j).getLatitude();
                                } else {
                                    majorYRange2 = altPointsB.get(j).getLatitude();
                                    minorYRange2 = altPointsA.get(j).getLatitude();
                                }
                                if (altPointsA.get(j).getLongitude() > altPointsB.get(j).getLongitude()) {
                                    majorXRange2 = altPointsA.get(j).getLongitude();
                                    minorXRange2 = altPointsB.get(j).getLongitude();
                                } else {
                                    majorXRange2 = altPointsB.get(j).getLongitude();
                                    minorXRange2 = altPointsA.get(j).getLongitude();
                                }

                                if ((((crossX > minorXRange1) && (crossX < majorXRange1)) &&
                                     ((crossY > minorYRange1) && (crossY < majorYRange1))) &&
                                    (((crossX > minorXRange2) && (crossX < majorXRange2)) &&
                                     ((crossY > minorYRange2) && (crossY < majorYRange2)))) {
                                    System.out.println("problemas aqui2.1 en "+i+", "+j+"!");
                                    return false;
                                }
                            } catch (Exception e) { System.out.println("peta aqui3 en "+i+": "+e.getMessage()); }
                        }
                    }
                }
            }
        }

        return true;
    }

    public static boolean checkIfPointIsInsideZone(Point point, Zone zone){
        //Check the points exist and points can form an area
        if ((zone.getBorderpoints() == null) || (zone.getBorderpoints().size() < 3)) return false;

        //Check that zone is even close to the point
        ArrayList<Double> extremes = getZoneExtremeValues(zone);

        double lon = point.getLongitude();
        double lat = point.getLatitude();

        if ((extremes.get(0) < lon) || (extremes.get(1) > lon) ||
            (extremes.get(2) < lat) || (extremes.get(3) > lat)) {
            //System.out.println("\tOUT OF THE POINT'S RANGE");
            return false;
        }

        ArrayList<Double> crossPoints = new ArrayList<Double>();
        List<BorderPoint> zonePoints = zone.getBorderpoints();
        BorderPoint zPoint = null;
        double cutPoint = 0;
        double pendent = 0;
        double cross = 0;
        double x0 = 0;
        double x1 = 0;
        double x2 = 0;
        double x3 = 0;
        double y1 = 0;
        double y2 = 0;
        int crossings = 0;

        //System.out.println("lon: " + lon + ", lat: " + lat);

        for (int i=0;i<zonePoints.size();i++) {
            zPoint = zonePoints.get(i);
            x1 = zPoint.getLongitude();
            y1 = zPoint.getLatitude();
            if ((x1 == lon) && (y1 == lat)) {
                //System.out.println("    EQUAL POINTS");
                return true;
            }
            if (i == 0) {
                x0 = zonePoints.get(zonePoints.size()-1).getLongitude();
            } else {
                x0 = zonePoints.get(i-1).getLongitude();
            }
            if ((i+1) == zonePoints.size()) {
                x2 = zonePoints.get(0).getLongitude();
                y2 = zonePoints.get(0).getLatitude();
            } else {
                x2 = zonePoints.get(i+1).getLongitude();
                y2 = zonePoints.get(i+1).getLatitude();
            }
            if (i < (zonePoints.size()-2)) {
                x3 = zonePoints.get(i+2).getLongitude();
            } else if (i == (zonePoints.size()-2)) {
                x3 = zonePoints.get(0).getLongitude();
            } else {
                x3 = zonePoints.get(1).getLongitude();
            }

            //System.out.println("x1: " + x1 + ", y1: " + y1 + " - x2: " + x2 + ", y2: " + y2);

            if ((lon == x1) && (lon == x2) && (((lat >= y1) && (lat <= y2)) || (lat >= y2) && (lat <= y1))) {
                //System.out.println("    IN SAME VERTICAL AXIS");
                return true;
            } else if (((lon > x1) && (lon < x2)) || ((lon > x2) && (lon < x1))) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((lon * pendent) + cutPoint);
                if (cross >= lat) {
                    crossPoints.add(cross);
                    //System.out.println("MIDDLE CROSSING IN THE MIDDLE OF 1 LINE: " + cross);
                    crossings++;
                }
            } else if ((lon == x1) && (lon < x2) && (x0 < lon)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((lon * pendent) + cutPoint);
                if (cross > lat) {
                    //System.out.println("possible crossing 1 at: " + cross + "...");
                    if (crossPoints.isEmpty() || !crossPoints.contains(cross)) {
                        crossPoints.add(cross);
                        //System.out.println("MIDDLE CROSSING 1 IN THE POINT BETWEEN TWO LINES: " + cross);
                        crossings++;
                    }
                }
            } else if ((lon == x2) && (lon > x1) && (x3 > lon)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((lon * pendent) + cutPoint);
                if (cross > lat) {
                    //System.out.println("possible crossing 2 at: " + cross + "...");
                    if (crossPoints.isEmpty() || !crossPoints.contains(cross)) {
                        crossPoints.add(cross);
                        //System.out.println("MIDDLE CROSSING 2 IN THE POINT BETWEEN TWO LINES: " + cross);
                        crossings++;
                }
                }
            } else if ((lon == x2) && (lon < x1) && (x3 < lon)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((lon * pendent) + cutPoint);
                if (cross > lat) {
                    //System.out.println("possible crossing 3 at: " + cross + "...");
                    if (crossPoints.isEmpty() || !crossPoints.contains(cross)) {
                        crossPoints.add(cross);
                        //System.out.println("MIDDLE CROSSING 3 IN THE POINT BETWEEN TWO LINES: " + cross);
                        crossings++;
                }
                }
            } else if ((lon == x1) && (lon > x2) && (x0 > lon)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((lon * pendent) + cutPoint);
                if (cross > lat) {
                    //System.out.println("possible crossing 4 at: " + cross + "...");
                    if (crossPoints.isEmpty() || !crossPoints.contains(cross)) {
                        crossPoints.add(cross);
                        //System.out.println("MIDDLE CROSSING 4 IN THE POINT BETWEEN TWO LINES: " + cross);
                        crossings++;
                    }
                }
            }
        }

        //System.out.println("#crossings: " + crossings);

        if ((crossings % 2) == 1) return true;

        return false;
    }

    //assumes wrongly long as Y and lat as X, but caculates correctly
    public static boolean checkIfPointBelongsToZone(Point point, Zone zone){
        List<BorderPoint> zonePoints = zone.getBorderpoints();

        //Check the valid points
        if (zonePoints == null) return false;

        //Check points can form an area
        if (zonePoints.size() < 3) return false;

        //Check that zone is even close to the point
        ArrayList<Double> extremes = getZoneExtremeValues(zone);
        double pointLong = point.getLongitude();
        double pointLat = point.getLatitude();

        if ((extremes.get(0) < pointLong) || (extremes.get(1) > pointLong) ||
            (extremes.get(2) < pointLat) || (extremes.get(3) > pointLat)) return false;

        //The Lat coordinate will be used by default to start with the checkings:
        ArrayList<Double> crossPoints = new ArrayList<Double>();
        ArrayList<Double> touchPoints = new ArrayList<Double>();
        double cutPoint = 0;
        double pendent = 0;
        double cross = 0;
        double x0 = 0;
        double x1 = 0;
        double x2 = 0;
        double x3 = 0;
        double y1 = 0;
        double y2 = 0;

        for (int i=0;i<zonePoints.size();i++) {
            y1 = zonePoints.get(i).getLatitude();
            x1 = zonePoints.get(i).getLongitude();
            if ((y1 == pointLat) && (x1 == pointLong)) return true;
            if (i == 0) {
                x0 = zonePoints.get(zonePoints.size()-1).getLongitude();
            } else {
                x0 = zonePoints.get(i-1).getLongitude();
            }
            if ((i+1) == zonePoints.size()) {
                x2 = zonePoints.get(0).getLongitude();
                y2 = zonePoints.get(0).getLatitude();
            } else {
                x2 = zonePoints.get(i+1).getLongitude();
                y2 = zonePoints.get(i+1).getLatitude();
            }
            if (i < zonePoints.size()-2) {
                x3 = zonePoints.get(i+2).getLongitude();
            } else if (i == zonePoints.size()-2) {
                x3 = zonePoints.get(0).getLongitude();
            } else {
                x3 = zonePoints.get(1).getLongitude();
            }

            if ((pointLong == x1) && (pointLong == x2) && (((pointLat >= y1) && (pointLat <= y2)) || ((pointLat >= y2) && (pointLat <= y1)))) {
                return true;
            } else if(((pointLong > x1) && (pointLong < x2)) || ((pointLong > x2) && (pointLong < x1))) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                crossPoints.add(new Double((pointLong * pendent) + cutPoint));
            } else if ((pointLong == x1) && (pointLong < x2) && (x0 < pointLong)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((pointLong * pendent) + cutPoint);
                if (crossPoints.isEmpty() || !crossPoints.contains(cross)) crossPoints.add(cross);
            } else if ((pointLong == x2) && (pointLong > x1) && (x3 > pointLong)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((pointLong * pendent) + cutPoint);
                if (crossPoints.isEmpty() || !crossPoints.contains(cross)) crossPoints.add(cross);
            } else if ((pointLong == x2) && (pointLong < x1) && (x3 < pointLong)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((pointLong * pendent) + cutPoint);
                if (crossPoints.isEmpty() || !crossPoints.contains(cross)) crossPoints.add(cross);
            } else if ((pointLong == x1) && (pointLong > x2) && (x0 > pointLong)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((pointLong * pendent) + cutPoint);
                if (crossPoints.isEmpty() || !crossPoints.contains(cross)) crossPoints.add(cross);
            } else if ((pointLong == x1) || (pointLong == x2)) {
                pendent = calculatePendent(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cutPoint = calculateCuttingPoint(new BorderPoint(y1, x1), new BorderPoint(y2, x2));
                cross = new Double((pointLong * pendent) + cutPoint);
                if (!crossPoints.contains(cross) && !touchPoints.contains(cross)) touchPoints.add(cross);
            }
        }

        //System.out.print("-touch size: " + touchPoints.size());

        for (int i=0;i<(touchPoints.size());i++) {
            if (pointLat == touchPoints.get(i)) {
                //System.out.println("");
                return true;
            }
        }

        //System.out.print(",cross size: " + crossPoints.size());
        if ((crossPoints.isEmpty()) || ((crossPoints.size() % 2) != 0)) {
            //System.out.println("            (* crossings fail!)");
            return false;
        }

        //System.out.println("");

        crossPoints = reorderValues(crossPoints);

        for (int i=0;i<(crossPoints.size()-1);i++) {
            if ((i % 2) == 0) {
                if ((pointLat >= crossPoints.get(i)) && (pointLat <= crossPoints.get(i+1))) return true;
                else if((pointLat <= crossPoints.get(i)) && (pointLat >= crossPoints.get(i+1))) return true;
            }
        }

        return false;
    }

    private static ArrayList<Double> calculateCrossPoint(double pendent1, double cutPoint1, Point p1, double pendent2, double cutPoint2, Point p2){
        if (pendent1 == pendent2) return null;

        ArrayList<Double> crossPoint = new ArrayList<Double>(2);
        
        if ((pendent1 == Double.POSITIVE_INFINITY) || (pendent1 == Double.NEGATIVE_INFINITY)) {
            crossPoint.add(p1.getLongitude());                                  //Longitude
            crossPoint.add((pendent2 * p1.getLongitude()) + cutPoint2);         //Latitude
        } else if ((pendent2 == Double.POSITIVE_INFINITY) || (pendent2 == Double.NEGATIVE_INFINITY)) {
            crossPoint.add(p2.getLongitude());                                  //Longitude
            crossPoint.add((pendent1 * p2.getLongitude()) + cutPoint1);         //Latitude
        } else {
            crossPoint.add((cutPoint2 - cutPoint1) / (pendent1 - pendent2));                                //Longitude
            crossPoint.add(((pendent2 * cutPoint1) - (pendent1 * cutPoint2)) / (pendent2 - pendent1));      //Latitude
        }

        return crossPoint;
    }

    private static double calculatePendent(Point pointA, Point pointB){
        if (pointB.getLatitude() == pointA.getLatitude()) return 0;
        else if ((pointB.getLongitude() == pointA.getLongitude()) &&
                 ((pointB.getLatitude() - pointA.getLatitude()) > 0)) return Double.POSITIVE_INFINITY;
        else if ((pointB.getLongitude() == pointA.getLongitude()) &&
                 ((pointB.getLatitude() - pointA.getLatitude()) < 0)) return Double.NEGATIVE_INFINITY;
        else return new Double((pointB.getLatitude() - pointA.getLatitude()) /
                               (pointB.getLongitude() - pointA.getLongitude()));
    }

    private static double calculateCuttingPoint(Point pointA, Point pointB){
        if ((pointB.getLatitude() * pointA.getLongitude()) ==
            (pointA.getLatitude() * pointB.getLongitude())) return 0;
        else if ((pointA.getLongitude() == pointB.getLongitude()) &&
                 (((pointB.getLatitude() * pointA.getLongitude()) -
                   (pointA.getLatitude() * pointB.getLongitude()))) > 0) return Double.POSITIVE_INFINITY;
        else if ((pointA.getLongitude() == pointB.getLongitude()) &&
                 (((pointB.getLatitude() * pointA.getLongitude()) -
                   (pointA.getLatitude() * pointB.getLongitude()))) < 0) return Double.NEGATIVE_INFINITY;
        else return new Double(((pointB.getLatitude() * pointA.getLongitude()) -
                                (pointA.getLatitude() * pointB.getLongitude())) /
                               (pointA.getLongitude() - pointB.getLongitude()));
    }

    private static boolean checkIfOppositePendents(Point point1A, Point point1B, Point point2A, Point point2B){
        if (calculatePendent(point1A, point1B) == calculatePendent(point2A, point2B)) {
            int s1 = 1;
            int s2 = 1;

            if (((point1B.getLatitude() - point1A.getLatitude()) < 0) &&
                ((point1B.getLongitude() - point1A.getLongitude()) >= 0)) s1 = -1;
            else if (((point1B.getLongitude() - point1A.getLongitude()) < 0) &&
                     ((point1B.getLatitude() - point1A.getLatitude()) >= 0)) s1 = -1;
            if (((point2B.getLatitude() - point2A.getLatitude()) < 0) &&
                ((point2B.getLongitude() - point2A.getLongitude()) >= 0)) s2 = -1;
            else if (((point2B.getLongitude() - point2A.getLongitude()) < 0) &&
                     ((point2B.getLatitude() - point2A.getLatitude()) >= 0)) s2 = -1;

            if ((s1 + s2) == 0) return true;
            else return false;
        } else return false;
    }

    //Method to reorder values descendently
    private static ArrayList<Double> reorderValues(ArrayList<Double> values){
        double aux;

        for (int i=0;i<(values.size()-1);i++) {
            for (int j=0;j<(values.size()-1);j++) {
                if (values.get(i) < values.get(i+1)) {
                    aux = new Double(values.get(i));
                    values.set(i, values.get(i+1));
                    values.set(i+1, aux);
                }
            }
        }

        return values;
    }

    public static ArrayList<Double> getZoneExtremeValues(Zone zone){
        ArrayList<Double> extremeValues = new ArrayList<Double>(4);
        List<BorderPoint> zonePoints = zone.getBorderpoints();
        BorderPoint point = null;
        double maxLong = -180;
        double minLong = 180;
        double maxLat = -90;
        double minLat = 90;

        for (int i=0;i<zonePoints.size();i++) {
            point = zonePoints.get(i);
            if (point.getLongitude() > maxLong) maxLong = new Double(point.getLongitude());
            if (point.getLongitude() < minLong) minLong = new Double(point.getLongitude());
            if (point.getLatitude() > maxLat) maxLat = new Double(point.getLatitude());
            if (point.getLatitude() < minLat) minLat = new Double(point.getLatitude());
        }

        //System.out.println("QUADRANT: Mx: " + maxLong + ", mx: " + minLong + ", My: " + maxLat + ", my: " + minLat);

        extremeValues.add(maxLong);
        extremeValues.add(minLong);
        extremeValues.add(maxLat);
        extremeValues.add(minLat);

        return extremeValues;
    }

    public ArrayList<City> getAllBorderCitiesFromZone(Zone zone, List<Node<City>> areaCities, Double refDistance){
        ArrayList<City> borderCities = new ArrayList<City>();
        BorderPoint pointA = null;
        BorderPoint pointB = null;
        City city = null;
        
        for (int j=0;j<zone.getBorderpoints().size();j++) {
            pointA = zone.getBorderpoints().get(j);
            if ((j + 1) == zone.getBorderpoints().size()) pointB = zone.getBorderpoints().get(0);
            else pointB = zone.getBorderpoints().get(j+1);

            Iterator<Node<City>> cityItr = areaCities.iterator();

            while (cityItr.hasNext()) {
                city = cityItr.next().value;
                if ((!borderCities.contains(city)) && (city.getZone().getId().equals(zone.getId()))) {
                    if (getMinDistanceFromPointToLine(city, new Line(pointA, pointB)) <= refDistance) {
                        borderCities.add(city);
                    }
                }
            }
        }

        return borderCities;
    }

    private static double getMinDistanceFromPointToLine(Point point, Line line){
        double d1 = point.getLinearDistanceTo(line.getPoints()[0]);
        double d2 = point.getLinearDistanceTo(line.getPoints()[1]);
        double se = line.getLength();

        if ((d2 > d1) && (((Math.pow(d1, d1) + Math.pow(d2, d2)) < Math.pow(se, se)) ||
                          ((Math.pow(d1, d1) + Math.pow(d2, d2)) == Math.pow(se, se)))) {
            return d1;
        } else if ((d1 > d2) && (((Math.pow(d1, d1) + Math.pow(d2, d2)) < Math.pow(se, se)) ||
                                 ((Math.pow(d1, d1) + Math.pow(d2, d2)) == Math.pow(se, se)))) {
            return d2;
        } else {
            return getDistanceBetweenPointAndLine(point, line);
        }
    }

    private static double getDistanceBetweenPointAndLine(Point point, Line line){
        if (line.getPoints()[0].getLongitude() == line.getPoints()[1].getLongitude()) {
            //Parallel line to Y axis (Latitude)
            return Math.abs(point.getLongitude() - line.getPoints()[0].getLongitude());
        } else {
            double pendent = calculatePendent(line.getPoints()[0], line.getPoints()[1]);
            double cutPoint = calculateCuttingPoint(line.getPoints()[0], line.getPoints()[1]);
            if (pendent == 0) {
                //Parallel line to X axis (Longitude)
                return Math.abs(point.getLatitude() - cutPoint);
            } else {
                //Any other case
                return Math.abs((pendent + point.getLongitude()) - point.getLatitude() + cutPoint) /
                       Math.sqrt(Math.pow(pendent, pendent) + 1);
            }
        }
    }
}
