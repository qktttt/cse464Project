import java.awt.Point;

public class Intersect {
	Point l1 = new Point();
	Point l2 = new Point();
	Point c = new Point();
	double r;

	public Intersect(double x1, double y1, double x2, double y2, double c1, double c2, double r, double startX, double startY) {
		double d1 = Math.pow(startX-x1, 2) + Math.pow(startY-y1, 2);
		double d2 = Math.pow(startX-x2, 2) + Math.pow(startY-y2, 2);
		if(d1 < d2) {
			l1.setLocation(x1, y1);
			l2.setLocation(x2, y2);
			c.setLocation(c1, c2);
			this.r = r;
		}
		else {
			l1.setLocation(x2, y2);
			l2.setLocation(x1, y1);
			c.setLocation(c1, c2);
			this.r = r;
		}
	}


}
