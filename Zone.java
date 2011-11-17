import java.util.ArrayList;
import java.util.List;

public class Zone implements IGetId {
	private Country country;
	private ArrayList<BorderPoint> borderpoints;
	private String id;
	private static int nextId = 0;

	public Zone(Country country) {
		this.country = country;
		this.id = (new Integer(nextId)).toString();
		borderpoints = new ArrayList<BorderPoint>();
		nextId++;
	}

	public String getId() {
		return id;
	}

	public Country getCountry() {
		return country;
	}

	public List<BorderPoint> getBorderpoints() {
		return borderpoints;
	}

	public BorderPoint getBorderpoint(int index) {
		if (index >= borderpoints.size() || index < 0)
			return null;
		return this.borderpoints.get(index);
	}

	public void addBorderPoint(BorderPoint bp, int index) {
		if (bp==null) return;
		if (index > borderpoints.size() || index < 0)
			throw new IllegalArgumentException("index:"+index+" size:"+this.borderpoints.size());
		this.borderpoints.add(index, bp);
	}

	public boolean removeBorderPoint(int index) {
		if (index >= this.borderpoints.size() || index < 0) {
			return false;
		}
		this.borderpoints.remove(index);
		return true;
	}

	public boolean removeBorderPoint(BorderPoint bp) {
		return this.borderpoints.remove(bp);
	}
}