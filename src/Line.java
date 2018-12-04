import java.math.BigDecimal;

public class Line {
	int startX;
	int startY;
	int endX;
	int endY;
	boolean vert = false;
	/**
	 *
	 * @param x1 x coordinate of the first point
	 * @param y1 y corrdinate of the first point
	 * @param x2 x coordinate of the second point
	 * @param y2 y coordinate of the second point
	 */
	public Line(double x1, double y1, double x2, double y2) {
		this.startX = (int)x1;
		this.startY = (int)y1;
		this.endX = (int)x2;
		this.endY = (int)y2;
		if(x1 == x2)
			vert = true;
	}

	public double getB() {
		if(vert)
			return 0.0;
		return -1.0;
	}

	// kx+b de b
	public double getC() {
		if(!vert) {
			double c = startY - (getA() * startX);
			BigDecimal b = new BigDecimal(c);
			double c1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return c1;
		}
		return (double)-startX;
	}

	// k
	public double getA() {
		if(!vert) {
			double a = (startY - endY) / ((double)startX - (double)endX);
			BigDecimal b = new BigDecimal(a);
			double a1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			return a1;
		}
		return 1.0;

	}

}
