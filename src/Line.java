
public class Line {
	int x1;
	int y1;
	int x2;
	int y2;

	/**
	 * 
	 * @param x1 x coordinate of the first point
	 * @param y1 y corrdinate of the first point
	 * @param x2 x coordinate of the second point
	 * @param y2 y coordinate of the second point
	 */
	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = (int)x1;
		this.y1 = (int)y1;
		this.x2 = (int)x2;
		this.y2 = (int)y2;
	}
}
