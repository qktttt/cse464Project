import java.awt.Point;
import java.util.ArrayList;

public class SPCountry {

    /**
     * Declare variables
     */
    Map map;
    Line line;
    Intersect temp;
    ArrayList<RA> intersection = new ArrayList<>();
    ArrayList<Intersect> inter = new ArrayList<>();
    ArrayList<Arc> arcs = new ArrayList<>();
    ArrayList<Line> lines = new ArrayList<>();

    /**
     * Constructor
     * @param RArea
     * @param line
     */
    public SPCountry(ArrayList<RA> RArea, Line line) {
        this.line = line;
        map = new Map(RArea, line);
    }


    /**
     * A method that find and add all the restricted area (intersections)
     * to the intersection list
     */
    public void findAllIntersection() {
        // for each RArea in list
        for(RA r : map.RArea ) {
            // if intersects, then add
            boolean t = checkIntersect(r);
            if(t) {
                intersection.add(r);
                System.out.println("Line intersects with this resrticted area: (" + r.x + ", " + r.y + ") r: " +r.radius);
            }
        }
    }

    /**
     * A method to check whether restricted area
     * intersect with the line
     * @param r : restricted area (a circle)
     * @return : return true if intersect. Otherwise false
     */
    private boolean checkIntersect(RA r) {

        // based on the formula of the center of the circle to
        // the line.
        // abs(ax+by+c)/sqrt(a^2+b^2)
        // standard formular of line
        // ax+by+c=0
        double d = Math.abs(line.getA() * r.x + line.getB() * r.y + line.getC()) /
                Math.sqrt(Math.pow(line.getA(), 2) + Math.pow(line.getB(), 2));

        if (r.radius > d) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Sort the intersection sets.
     */
    public void sortIntertsect() {
        // move on Y axis
        sort();
    }


    // All sort parts are using bubble sort
    // Sorted: O(n)
    // Worst or Avg: O(n^2)

    /**
     * Sort based on which intersection point is closest to the start point
     */
    private void sort() {
        boolean checkSort = true;
        for(int i = 0; i < inter.size()-1; i++) {
            for(int j = 0; j < inter.size()-i-1; j++) {
                // a^2 + b^2 = c^2
                if(Math.pow(line.startX-inter.get(j).l1.x, 2) + Math.pow(line.startY-inter.get(j).l1.y, 2)
                        > Math.pow(line.startX-inter.get(j+1).l1.x, 2) + Math.pow(line.startY-inter.get(j+1).l1.y, 2))
                {
                    temp = inter.get(j);
                    inter.set(j, inter.get(j+1));
                    inter.set(j+1, temp);
                    checkSort = false;
                }
            }
            // check whether the arrayList is sorted before
            // If yes, then break.
            if(checkSort)
                break;
        }
    }


    /**
     * Calculate the intersection points
     */
    public void calInter() {
        // standard line function
        if (line.getB()!=0) {
            for(int i = 0; i < intersection.size(); i++) {
                // circle (x,y) r
                double cx = intersection.get(i).x;
                double cy = intersection.get(i).y;
                double r = intersection.get(i).radius;
                // y=kx+b, k and b
                double b = line.getC();
                double k = line.getA();
                // calculation
                double c = cx * cx + Math.pow((b - cy), 2) - r * r;
                double a = 1 + k * k;
                double b1 = (2 * cx - 2 * k * (b-cy));
                double tmp = Math.sqrt(b1 * b1 - 4 * a * c);
                // intersection points
                double x1 = (b1 + tmp) / (2 * a);
                double y1 = k * x1 + b;
                double x2 = (b1 - tmp) / (2 * a);
                double y2 = k * x2 + b;
                // add to the intersect object
                Intersect intersect = new Intersect(x1, y1, x2, y2, cx, cy, r, line.startX, line.startY);
                System.out.println("Intersection point: x1:" + x1 + " y1: "+ y1 + " x2: " + x2 + " y2: " + y2 +
                        "with cricle center(" + cx + ", " + cy + ") with r = " + r);
                System.out.println();
                inter.add(intersect);
            }
        }
        // vertical line
        else {
            for(int i = 0; i < intersection.size(); i++) {
                double cx = intersection.get(i).x;
                double cy = intersection.get(i).y;
                double r = intersection.get(i).radius;
                double y1 = Math.pow((Math.pow(r,2)-Math.pow((-line.getC()-cx),2)),0.5)+cy;
                System.out.println(":" + line.getC());
                double y2=  -Math.pow((Math.pow(r,2)-Math.pow((-line.getC()-cx),2)),0.5)+cy;
                Intersect intersect = new Intersect(line.startX,y1,line.startX,y2,cx,cy,r, line.startX, line.startY);
                System.out.println("Intersection point: x1:" + line.startX + " y1: "+ y1 + " x2: " + line.startX + " y2: " + y2 +
                        "with cricle center(" + cx + ", " + cy + ") with r = " + r);
                System.out.println();
                inter.add(intersect);
            }
        }

        //}
    }


    public ArrayList<Intersect> getIntersectionPointList(){
        return inter;
    }

    public ArrayList<RA> getIntersections(){
        return intersection;
    }

    /**
     * toString for the unsorted intersection area (circle)
     */
    public void tostring() {
        System.out.println("*** Unsorted intersections ***");
        for(RA c : intersection) {
            System.out.println("(" + c.x + ", " + c.y + ")");
        }
    }

    public void toStringArea() {
        for(RA r: map.RArea) {
            System.out.println("Restricted Area: (" + r.x + ", " + r.y + ") r = " + r.radius);
        }
    }


    /**
     * toString for the sorted intersection area and intersection points
     */
    public void tostringPoint() {
        System.out.println("*** Sorted intersections");
        for(Intersect ca : inter) {
            System.out.println();
            System.out.println("1st intersection point: (" + ca.l1.x + ", " + ca.l1.y + ")");
            System.out.println("2nd intersection point: (" + ca.l2.x + ", " + ca.l2.y + ")");
            System.out.println("Cirlce: (" + ca.c.x + ", " + ca.c.y + ")" + " radius: " + ca.r);
        }
    }


    /**
     * Draw GUI
     * @param intersections
     */
    public void GUIHelper(ArrayList<Intersect> intersections) {
        Point start = new Point(line.startX, line.startY);
        Point end = null;
        for(int i = 0;i<intersection.size();i++) {
            Intersect it1 = intersections.get(i);
            double startAngel = 0;
            //move the circle to the origin
            Point pt1 = new Point((int)(it1.l1.x-it1.c.x), (int)(it1.l1.y - it1.c.y));
            Point pt2 = new Point((int)(it1.l2.x-it1.c.x), (int)(it1.l2.y - it1.c.y));
            Point pt3 = new Point((int)it1.r, 0);
            //compute the dot product and angel;
            double DotProduct1 = pt1.x * pt3.x + pt1.y * pt3.y;
            double cosAngel1 = DotProduct1/(it1.r * it1.r);
            double angel1 = Math.acos(cosAngel1);

            double DotProduct2 = pt2.x * pt3.x + pt2.y * pt3.y;
            double cosAngel2 = DotProduct2/(it1.r * it1.r);
            double angel2 = Math.acos(cosAngel2);

            double DotProduct = pt1.x * pt2.x + pt1.y * pt2.y;
            double cosAngel = DotProduct/(it1.r * it1.r);
            double angel = Math.acos(cosAngel);
            double arcLength = angel;

            if(angel1 + angel2 == angel) {
                //pt1 is below pt2
                if(pt1.y > pt2.y) {
                    startAngel = 360/Math.PI - angel1;
                    end = pt1;
                }
                //pt1 is above pt2
                else if(pt2.y > pt1.y) {
                    startAngel = 360/Math.PI - angel2;
                    end = pt2;
                }
            }
            else {
                if(pt1.x > pt2.x) {
                    startAngel = angel1;
                    end = pt1;
                }
                else if(pt2.x > pt1.x) {
                    startAngel = angel2;
                    end = pt2;
                }
            }
            arcs.add(new Arc(it1.c.x - it1.r/2, it1.c.y - it1.r/2, it1.r, startAngel, arcLength));
            lines.add(new Line(start.x, start.y, end.x, end.y));
            if(end == pt2)
                start = pt1;
            else if(end == pt1)
                start = pt2;
        }
        lines.add(new Line(start.x, start.y, line.endX, line.endY));
    }


}
