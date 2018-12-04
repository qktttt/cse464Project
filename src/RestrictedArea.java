import java.awt.Point;
import java.util.ArrayList;

public class RestrictedArea {
	
	// Declare variables
	// Point p is a 
	private Point p;
	private int r;
	
	public RestrictedArea() {
		p = new Point(0,0);
		r = 0;
	}
	
	public RestrictedArea(int x, int y, int r) {
		p = new Point(x,y);
		this.r = r;
	}
	
	
	public RestrictedArea(Point p, int r) {
		this.p = p;
		this.r = r;
	}
	
	
}
