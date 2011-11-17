import java.lang.Math ;

public class Point {
	
	public static double EARTH_RADIUS = 6378.1370; //ecuatorial radius of the earth in km
	private double latitude;
	private double longitude;
	
	public Point(double latitude, double longitude) {
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
	
	public double getLinearDistanceTo(Point point){
		double lat1 = degreeToRadians(this.latitude);
		double lat2 = degreeToRadians(point.getLatitude());
		double lon1 = degreeToRadians(point.getLongitude());
		double lon2 = degreeToRadians(this.longitude);
		double d; //distance between points in km
		d=2*Point.EARTH_RADIUS*Math.asin(Math.sqrt(Math.pow(Math.sin((lat2-lat1)/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin((lon2-lon1)/2),2)));
		//d=Point.EARTH_RADIUS*Math.acos(Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)+Math.sin(lat1)*Math.sin(lat2));
		return d;
	}

}
