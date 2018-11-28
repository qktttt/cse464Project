import java.awt.Point;
import java.util.ArrayList;

public class Map {
	ArrayList<RA> RArea = new ArrayList<RA>();
	Vertice station = new Vertice(0, 0);
	/**
	 * Construct qnd initialize a Map length as 20
	 * @param x the x coordinate of the center point
	 * @param y the y coordinate of the center point
	 */
	public Map(double x, double y) {
		station.setLocation(x, y);
	}

	/**
	 * 
	 * @param x, the x coordinate of the restricted area
	 * @param y, the y coordinate of the restricted area
	 * @param radius, the radius of the restricted area
	 */
	public void addRA(int x, int y, int radius) {
		RA RestritedAread = new RA(x, y, radius);
		RArea.add(RestritedAread);
	}
	
}
