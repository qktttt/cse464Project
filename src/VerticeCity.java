import java.util.ArrayList;

public class VerticeCity {
	public static int number = 0;
	public double x;
	public double y;
	public ArrayList<VerticeCity> neighbors;
	public VerticeCity parent;
	public double distanceToSource;
	public String verticeName; // for each verticeCity, this value should be unique.
	VerticeCity(double x, double y, String verticeName) {
		this.x = x;
		this.y = y;
		neighbors = new ArrayList<VerticeCity>();
	}

	VerticeCity(double x, double y) {
		VerticeCity(x, y, "default-point-" + number);
		number++;
	}

	public int hashCode() {
		return verticeName.hashCode();
	}

	public boolean equals(Object other) {	
		return verticeName.hashCode() == other.hashCode();
	}
}
