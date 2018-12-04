import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

// this class will read the cityMapResource.txt
// and then create a graph
// the object of this class will support
// the direction from a vertice to another vertice
// and also avoid going into the restriction area.
public class SPCity {
    ArrayList<River> allRivers;
    HashMap<String, VerticeCity> allPointsOfRoad;
    ArrayList<RA> restrictiveAreas;
    ArrayList<SimplePoint> pointsInRA;
    ArrayList<SimpleEdge> edgeTouchRA;
    HashMap<String, VerticeCity> allCopy;

    public SPCity() throws FileNotFoundException {
        allPointsOfRoad = new HashMap<>();
        allRivers = new ArrayList<>();
        restrictiveAreas = new ArrayList<RA>();
        pointsInRA = new ArrayList<>();
        edgeTouchRA = new ArrayList<>();
        allCopy = new HashMap<>();
        LoadMap("cityMapResource.txt");
    }

    public void LoadMap(String dataFileName) throws FileNotFoundException {
        // in this method
        // the river, from my persepctive, it will be considered
        // as a vertice, and if two points are seperated by a river
        // the path can go cross the river directly

        // When loading the map from the file
        // I need to take care of the whether the road is
        // one way or two way.

        File input = new File(dataFileName);
        Scanner reader = new Scanner(input);
        // load the Restricted Area
        reader.nextLine();
        while (true) {
            String curLine = reader.nextLine();
            if (curLine.isEmpty()) break;
            Scanner readCur = new Scanner(curLine);
            RA newOne = new RA(Double.parseDouble(readCur.next()),
                    Double.parseDouble(readCur.next()),
                    Double.parseDouble(readCur.next()));
            restrictiveAreas.add(newOne);
        }

        // load the vertice
        reader.nextLine();
        while (true) {
            String curLine = reader.nextLine();
            if (curLine.isEmpty()) break;
            Scanner readCur = new Scanner(curLine);
            String name = readCur.next();
            double x = Double.parseDouble(readCur.next());
            double y = Double.parseDouble(readCur.next());

            VerticeCity newOne = new VerticeCity(x, y, name);
            allCopy.put(name, newOne);
            if (isPointInRa(x, y)) pointsInRA.add(new SimplePoint(x, y, name));
            else {
                allPointsOfRoad.put(name, newOne);
            }
        }

        // load the road
        reader.nextLine();
        int total = 0;
        int valid = 0;
        while (true) {
            String curLine = reader.nextLine();
            if (curLine.isEmpty()) break;
            Scanner readCur = new Scanner(curLine);
            String start = readCur.next();
            String end = readCur.next();
            Double distance = Double.parseDouble(readCur.next());
            VerticeCity a = allCopy.get(start);
            VerticeCity b = allCopy.get(end);
            if (a == null || b == null) {
                System.out.println(123123123);
                continue;
            }
            if (isEdgeInRa(a.x, a.y, b.x, b.y)) {
                edgeTouchRA.add(new SimpleEdge(a.x, a.y, b.x, b.y, distance));
                System.out.printf("(%f, %f) -> (%f, %f)\n", a.x, a.y, b.x, b.y);
            }
            else {
                valid++;
                a.neighbors.add(new CityRoad(b, distance, true));
                //System.out.printf("add new road %s(%f, %f)->%s(%f, %f), " +
                //        "distance: %f\n", start, a.x, a.y, end, b.x, b.y, distance);
            }
            total++;
        }
        System.out.println(total + " " + valid);

        // load the river information
        reader.nextLine();
        reader.nextLine();
        reader.nextLine();
        while (true) {
            ArrayList<VerticeCity> oneSide = new ArrayList<>();
            ArrayList<VerticeCity> anotherSide = new ArrayList<>();
            String curLine1 = reader.nextLine();
            if (curLine1.isEmpty()) break;
            Scanner cur = new Scanner(curLine1);
            System.out.println("***" + curLine1 + "***");
            String name = cur.next();
            double x = cur.nextInt(), y = cur.nextInt(),
                    width = cur.nextInt(), length = cur.nextInt();
            allRivers.add(new River(name, x, y, width, length));

            String curLine2 = reader.nextLine();
            cur.close();
            cur = new Scanner(curLine2);
            while(cur.hasNext())
                oneSide.add(allCopy.get(cur.next()));
            cur.close();
            String curLine3 = reader.nextLine();
            cur = new Scanner(curLine3);
            while(cur.hasNext())
                anotherSide.add(allCopy.get(cur.next()));
            cur.close();
            for(VerticeCity each : oneSide) {
                for(VerticeCity eachAnotherSide : anotherSide) {
                    if(!isEdgeInRa(each.x, each.y, eachAnotherSide.x, eachAnotherSide.y)) {
                        double distance = Math.hypot(each.x - eachAnotherSide.x,
                                each.y - eachAnotherSide.y);
                        each.neighbors.add(new CityRoad(eachAnotherSide,
                                distance, false));
                        eachAnotherSide.neighbors.add(new CityRoad(each, distance, false));
                    }
                }
            }
        }


        reader.close();
    }

    public boolean isPointInRa(double x, double y) {
        // detect whether a point is in the restrictive areas
        for (RA each : restrictiveAreas) {
            if (Math.hypot(each.x - x, each.y - y) < each.radius)
                return true;
        }
        return false;
    }

    public boolean isEdgeInRa(double x1, double y1, double x2, double y2) {
        // detect whether a edge is in the restrictive areas
        // if either point of a edge is in the RA
        // the edge related with this point will be removed before this method
        // is called

        for (RA each : restrictiveAreas) {
            // here is the process to detect whether a edge
            // is intersected with a RA

            // first, check whether the edge's staright distance
            // to the RA is smaller than the  radius
            // if not, the edge is not intersected with RA
            // if the distance is smaller than the RA's radius
            // do further check to ensure the edge is intersected with RA
            double edgeK = (y2 - y1) / (x2 - x1);
            double distanceK = -1 / edgeK;
            double edgeB = y1 - x1 * edgeK;
            double distanceB = each.y - each.x * distanceK;
            double intersectedX = (distanceB - edgeB) / (edgeK - distanceK);
            double intersectedY = intersectedX * edgeK + edgeB;
            double distance = Math.hypot(each.x - intersectedX, each.y - intersectedY);
            // the accuray is 0.0001, prevent the high accuray of double computation
            // affect the judgement for the intersection
            double leftX = Math.min(x1, x2);
            double rightX = x1 + x2 - leftX;
            if (distance < each.radius
                    || (isPointInRa(x1, y1)||isPointInRa(x2, y2)) ||
                    (leftX < each.x - each.radius && rightX > each.x + each.radius))
                return true;
        }
        return false;
    }

    public Stack<VerticeCity> findShortestPath(String start, String end) {
        // this actually require the dijkstra algorithm
        // System.out.println("size of map is: " + allPointsOfRoad.size());
        Stack<VerticeCity> result = new Stack<>();
        VerticeCity from = allPointsOfRoad.get(start);
        VerticeCity to = allPointsOfRoad.get(end);
        if (from == null || to == null) {
            // System.out.printf("the path from %s to %s doesn't exist\n", start, end);
            return result;
        }
        // run the djikstra algorithm to get the result;

        for (Map.Entry<String, VerticeCity> each : allPointsOfRoad.entrySet()) {
            each.getValue().distance = Double.MAX_VALUE;
            each.getValue().parent = null;
        }

        from.distance = 0;
        PriorityQueue<VerticeCity> vertices = new PriorityQueue<>(allPointsOfRoad.size(), new Comparator<VerticeCity>() {
            @Override
            public int compare(VerticeCity o1, VerticeCity o2) {
                if (o1.distance < o2.distance) return -1;
                else if (o1.distance > o2.distance) return 1;
                else return 0;
            }
        });
        for (Map.Entry<String, VerticeCity> each : allPointsOfRoad.entrySet()) {
            vertices.add(each.getValue());
        }

        while (!vertices.isEmpty()) {
            VerticeCity curOne = vertices.poll();
            if (curOne == null) break;
            for (CityRoad each : curOne.neighbors) {
                relax(curOne, each.adjPoint, each.distance);
                boolean a = vertices.remove(each.adjPoint);
                if (a) vertices.add(each.adjPoint);
            }
        }

        while (to != null) {
            result.push(to);
            to = to.parent;
        }

        return result;
    }

    private void relax(VerticeCity first, VerticeCity second, double distanceBetween) {
        if (second.distance > first.distance + distanceBetween) {
            second.distance = first.distance + distanceBetween;
            second.parent = first;
        }
    }

    // print out the road from a vertice to another vertice
    // with minimum distance
    public void getPath(String start, String end) {
        Stack<VerticeCity> tmp = findShortestPath(start, end);
        while (!tmp.isEmpty())
            System.out.println(tmp.pop().verticeName);
    }

    // get all edges in the graph
    public ArrayList<SimpleEdge> getAllEdge() {
        ArrayList<SimpleEdge> result = new ArrayList<>();
        for(int i = 0; i < edgeTouchRA.size(); i++) result.add(edgeTouchRA.get(i));
        for (Map.Entry<String, VerticeCity> each : allCopy.entrySet()) {
            for (CityRoad eachEdge : each.getValue().neighbors) {
                if(eachEdge.isReal) {
                    VerticeCity from = each.getValue();
                    VerticeCity to = eachEdge.adjPoint;
                    result.add(new SimpleEdge(from.x, from.y, to.x, to.y, eachEdge.distance));
                }
            }
        }
        System.out.println(result.size());
        return result;
    }

    // get all points in the graph
    public ArrayList<SimplePoint> getAllPoint() {
        ArrayList<SimplePoint> result = new ArrayList<>();
        for(int i = 0; i < pointsInRA.size(); i++) result.add(pointsInRA.get(i));
        for (Map.Entry<String, VerticeCity> each : allCopy.entrySet()) {
            result.add(new SimplePoint(each.getValue().x, each.getValue().y, each.getValue().verticeName));
        }

        return result;
    }

    public ArrayList<RA> getRA() {
        return restrictiveAreas;
    }

    public ArrayList<River> getRivers() {
        return allRivers;
    }

	public static void main(String[] args) throws FileNotFoundException {
        /*
        SPCity newOne = new SPCity();
        long startTime = System.currentTimeMillis();
        int times = Integer.parseInt(args[0]);
        System.out.println("Runnign times: " + times);
        for(int i = 0; i < times; i++)
            newOne.findShortestPath("vertice1", "vertice36");
        long endTime = System.currentTimeMillis();
        System.out.println("Processing time: " + (endTime - startTime) + "millseconds");
        */
        SPCity newOne = new SPCity();
        spCity(newOne);
    }

    // this is the method creating the GUI
    // to show the best route from a vertice to another vertice
    public static void spCity(SPCity cityMap) {
        ArrayList<SimpleEdge> street = cityMap.getAllEdge();
        ArrayList<RA> restrictedAreas = cityMap.getRA();
        ArrayList<SimplePoint> allPoint = cityMap.getAllPoint();

        JFrame CurrentMap = new JFrame();
        CurrentMap.setTitle("464 Project, city");
        CurrentMap.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        CurrentMap.setSize(1000, 1000);

        JPanel graphics = new JPanel() {
            @Override
            public void paint(Graphics g) {
                //draw all the restricted area
                Stack<VerticeCity> shortestPath = cityMap.findShortestPath("vertice1", "vertice30");
                System.out.println("size of stack: " + shortestPath.size());
                Graphics2D g2d = (Graphics2D)g.create();

                for(River each : cityMap.getRivers()) {
                    g2d.setColor(Color.blue);
                    g2d.fillRect((int)each.x, (int)each.y, (int)each.width, (int)each.length);
                }

                for (RA circle : restrictedAreas) {
                    g2d.setColor(Color.cyan);
                    g2d.fillArc((int) (circle.x-circle.radius), (int) (circle.y-circle.radius),
                            (int) circle.radius * 2, (int) circle.radius * 2, 0, 360);
                }

                for(SimplePoint each : allPoint) {
                    g2d.setColor(Color.black);
                    g2d.fillOval((int)each.x-5, (int)each.y-5, 10, 10);
                    g2d.drawString("v" + each.name.substring(6), (int)each.x, (int)each.y);
                }

                //draw all the streets
                for (SimpleEdge edges : street) {
                    g2d.drawLine((int) edges.x1, (int) edges.y1, (int) edges.x2, (int) edges.y2);
                }

                if(shortestPath.isEmpty())  {
                    System.out.println("fucking awesome");
                    //return;
                }
                if(shortestPath.isEmpty()) return;
                Point pt1 = new Point((int) shortestPath.peek().x, (int) shortestPath.peek().y);
                shortestPath.pop();
                //draw the shortest path
                while (!shortestPath.isEmpty()) {
                    Point pt2 = new Point((int) shortestPath.peek().x, (int) shortestPath.peek().y);

                    g2d.setStroke(new BasicStroke(4.f));
                    g2d.setColor(Color.RED);
                    g2d.drawLine( (int)pt1.x, (int)pt1.y, (int)pt2.x, (int)pt2.y);
                    System.out.println(pt1 + " " + pt2 + " ");
                    pt1 = pt2;
                    shortestPath.pop();
                }
            }
        };
        CurrentMap.getContentPane().add(graphics);
        CurrentMap.setVisible(true);
    }
}

// these two classese belows are used to express
// vertice and edge position information for GUI
class SimpleEdge{
    double x1,y1,x2,y2,distance;
    public SimpleEdge(double x1, double y1, double x2, double y2, double distance) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.distance = distance;
    }
}

class SimplePoint {
    String name;
    double x, y;
    public SimplePoint (double x, double y, String name) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}

class River {
    String name;
    double x, y, width, length;
    public River(String name, double x, double y, double width, double length) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
    }
}
