import java.util.ArrayList;

// this class actually means the vertice
// of the graph, the vertice also have a lot of edge point out to
// other adjacent vertices
// vertice got x, got y, and distance to the source, and the
// supposed best parent to reach the source with minimum distance.
public class VerticeCity {
	public static int number = 0;
	public double x;
	public double y;
	public ArrayList<CityRoad> neighbors;
	public VerticeCity parent;
	public double distance;
	public String verticeName; // for each verticeCity, this value should be unique.

	public VerticeCity(double x, double y, String verticeName) {
		this.x = x;
		this.y = y;
		this.verticeName = verticeName;
		neighbors = new ArrayList<CityRoad>();
	}

	public VerticeCity(double x, double y) {
		this.x = x;
		this.y = y;
		this.verticeName = "default-point: " + number;
		number++;
	}

	public int hashCode() {
		return verticeName.hashCode();
	}


	public int compare(VerticeCity other) {
		return (int)(Math.round(this.distance - other.distance));
	}

	public boolean equals(Object other) {	
		VerticeCity second = (VerticeCity)other;
		return verticeName.hashCode() == second.verticeName.hashCode();
	}
}

// this means the edge in a graph
class CityRoad {
	VerticeCity adjPoint;
	double distance;
	public boolean isReal;
	public CityRoad(VerticeCity newOne, double distance, boolean real) {
		isReal = real;
		this.adjPoint = newOne;
		this.distance = distance;
	}
}
