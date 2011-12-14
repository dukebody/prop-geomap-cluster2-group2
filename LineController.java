import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Georgi
 */
public class LineController {

    public LineController(){}

    //ArrayList<Line> getLinesFromZone(Zone zone);  //ALREADY IMPLEMENTED IN ZONE AS A GETTER

    //Void determineAndSetLineType(Line line);  //ALREADY IMPLEMENTED IN LINE AS A SETTER

    //Double calculateLineDistance(Point point1, Point point2); //ALREADY IMPLEMENTED IN POINT FOR 2 RANDOM POINTS, AND CALLED FROM LINE

    //Boolean createZoneLines(ArrayList<Point> zonePoints);

    //Boolean createLine(Point point1, Point point2);

    //Boolean updateZoneLines(ArrayList<Point> zonePoints);

    //public boolean deleteLinesFromCountry(Country country);   //THE LINES ARE NOT STORED ANYWHERE SO NOT NECESARY BY NOW...

    //public boolean deleteLine(Line line); //DOESN'T SEEM NECESARY, LINE OBJECTS ONLY FOR LOCAL PURPOSES BY NOW...

    public ArrayList<Country> getNeighborCountries(Country country){
        ArrayList<Country> neighborsCountries = new ArrayList<Country>();
        ArrayList<Line> countryBorderLines = new ArrayList<Line>();
        ArrayList<Country> lineCountries = new ArrayList<Country>();
        List<BorderPoint> countryPoints = new ArrayList<BorderPoint>();

        for (Zone zone: country.getZones()) {
            Line line = null;
            countryPoints = zone.getBorderpoints();
            
            for (int j=0;j<countryPoints.size();j++) {
                if (j<countryPoints.size()-1) {
                    line = new Line(zone.getBorderpoint(j),zone.getBorderpoint(j+1));
                }
                else {
                    line = new Line(zone.getBorderpoint(j),zone.getBorderpoint(0));
                }

                if (line.getType().equals("border")) countryBorderLines.add(line);
            }
        }

        for (Line l: countryBorderLines) {
            lineCountries = l.getCountries();
            for (Country neighborCountry: l.getCountries()) {
                
                if (!neighborCountry.equals(country) && !neighborsCountries.contains(neighborCountry)) {
                    neighborsCountries.add(neighborCountry);
                    break;
                }
            }
        }

        return neighborsCountries;
    }

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
}
