import java.util.ArrayList;

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

class CityRoad {
	VerticeCity adjPoint;
	boolean isTouchRA;
	double distance;
	public CityRoad(VerticeCity newOne, double distance) {
		this.adjPoint = newOne;
		this.distance = distance;
	}
}
