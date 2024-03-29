import java.util.*;
import java.lang.Math;

public class Line {
	private BorderPoint[] points = new BorderPoint[2];
	private Double length;
	private String type;
        private ArrayList<Country> countries = new ArrayList<Country>();  //Neither more than two, nor less than one
        //private ArrayList<Integer> orderIntoCountryZone = new ArrayList<Integer>();

	public Line(BorderPoint a, BorderPoint b){
		points[0] = a;
		points[1] = b;
		length = a.getLinearDistanceTo(b);
		setType(getCountriesFromPoint(a), getCountriesFromPoint(b));
	}

	private void setType(List<Country> a, List<Country> b){ //REMEMBER TO VALIDATE THE MORE THAN ONE FRONTIER CASE...
		int count = 0;
		Iterator<Country> itr = a.iterator();
		while(itr.hasNext()){
			Country c1 = itr.next();
			if(b.contains(c1)) {
                countries.add(c1);
				count++;
			}
		}
		if(count == 1) type = "coastal";
		else if(count >= 2) type = "border";
		else type = "undefined";
	}

	private List<Country> getCountriesFromPoint(BorderPoint point){
		List<Country> countries = new ArrayList<Country>();
		Iterator<Zone> itr = point.getZones().iterator();
		while(itr.hasNext()){
			Zone zone = itr.next();
			Country country = zone.getCountry();
			if(!countries.contains(country))
                countries.add(country);
		}
		return countries;
	}

	public String getType(){
		return type;
	}

	public Double getLength(){
		return length;
	}

	public Point[] getPoints(){
		return points;
	}

        public ArrayList<Country> getCountries(){
		return countries;
	}

        public void setPoints(BorderPoint a, BorderPoint b){
            points[0]=a;
            points[1]=b;
            setLength();
        }

        private void setLength(){
            length=points[0].getLinearDistanceTo(points[1]);
        }

    public Double getDistanceToPoint(Point p) {
        // author: Israel
        // get the equation of the line ax+by+c=0
        Double a = -(points[1].getLongitude()-points[0].getLongitude())/(points[1].getLongitude()-points[0].getLatitude());
        Double b = 1.0;
        Double c = -a*points[0].getLatitude() - points[0].getLongitude();

        return Math.abs(a*points[0].getLatitude()+b*points[1].getLongitude()+c)/Math.sqrt(Math.pow(a,2)+Math.pow(b,2));
    }
}
