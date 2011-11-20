import java.lang.Math ;
/**
 * Represents a geografical position in latitude and longitude
 * @author e1000i
 *
 */
public class Point  {
	
	public static double EARTH_RADIUS = 6378.1370; //ecuatorial radius of the earth in km
	public static final double maxLon = 180.0, maxLat = 90.0, minLon = -180.0, minLat = -90.0;
	private double latitude;
	private double longitude;
	
	public Point(double latitude, double longitude) throws IllegalArgumentException{
		if ((minLat>latitude)||(latitude>maxLat))
			throw new IllegalArgumentException("Input latitude is not valid. Use a latitude between 90.0º and -90.0º");
		if ((minLon>longitude)||(longitude>maxLon))
			throw new IllegalArgumentException("Input longitude is not valid. Use a longitude between 180.0º and -180.0º");
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	private double degreeToRadians(double degrees){
		return degrees*(Math.PI/180);
	}
	/**
	 * Calculates the distance from the point to a other specified point
	 * using the "havarsin earth distance" formula. 
	 * @param point
	 * @return distance The distance between this point to a other specified point. 
	 */
	public double getLinearDistanceTo(Point point){
		return getLinearDistanceTo(point.getLatitude(), point.getLongitude());
	}
	/**
	 * Calculates the distance from the point to a specified latitude and longitude 
	 * using the "havarsin earth distance"formula.
	 * @param latitude Use a latitude between 90.0º and -90.0º
	 * @param longitude Use a longitude between 180.0º and -180.0º
	 * @return distance The distance between this point to a the specified latitude and longitude.
	 */
	public double getLinearDistanceTo(double latitude, double longitude) {
		double lat1 = degreeToRadians(this.latitude);
		double lat2 = degreeToRadians(latitude);
		double lon1 = degreeToRadians(longitude);
		double lon2 = degreeToRadians(this.longitude);
		double d; //distance between points in km
		d=2*Point.EARTH_RADIUS*Math.asin(Math.sqrt(Math.pow(Math.sin((lat2-lat1)/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin((lon2-lon1)/2),2))); //haversin formula
		//d=Point.EARTH_RADIUS*Math.acos(Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)+Math.sin(lat1)*Math.sin(lat2));	//great circle distances formula
		return d;
	}
	
	@Override
	public String toString() {
		return "(" + latitude + "," + longitude + ")";
	}

}
