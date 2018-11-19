
public class Arc {
	int x;
	int y;
	int radius;
	int startAngel;
	int arcAngel;
	
	/**
	 * 
	 * @param x x coordinate of the center point
	 * @param y y coordinate of the center point
	 * @param radius radius of the arc
	 * @param startAngel the start angel
	 * @param arcAngel the arc angel
	 */
	public Arc(double x, double y, double radius, double startAngel, double arcAngel) {
		this.x = (int)x;
		this.y = (int)y;
		this.radius = (int)radius;
		this.startAngel = (int)startAngel;
		this.arcAngel = (int)arcAngel;
	}
}
