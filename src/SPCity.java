import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map;

public class SPCity {

    HashMap<String, VerticeCity> allPointsOfRoad;
    ArrayList<RA> restrictiveAreas;
    ArrayList<SimplePoint> pointsInRA;
    ArrayList<SimpleEdge> edgeTouchRA;

    public SPCity() throws FileNotFoundException {
        allPointsOfRoad = new HashMap<>();
        restrictiveAreas = new ArrayList<RA>();
        LoadMap("cityMapResource.txt");
        pointsInRA = new ArrayList<>();
        edgeTouchRA = new ArrayList<>();
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
            System.out.printf("add new RA (%f, %f) r = %f\n", newOne.x, newOne.y, newOne.radius);
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
            if (isPointInRa(x, y)) pointsInRA.add(new SimplePoint(x, y));
            else {
                allPointsOfRoad.put(name, new VerticeCity(x, y, name));
                System.out.printf("add new point %s (%f, %f)\n", name, x, y);
            }
        }

        // load the road
        reader.nextLine();
        while (true) {
            String curLine = reader.nextLine();
            if (curLine.isEmpty()) break;
            Scanner readCur = new Scanner(curLine);
            String start = readCur.next();
            String end = readCur.next();
            Double distance = Double.parseDouble(readCur.next());
            VerticeCity a = allPointsOfRoad.get(start);
            VerticeCity b = allPointsOfRoad.get(end);
            if (a == null || b == null) continue;
            if (isEdgeInRa(a.x, a.y, b.x, b.y)) edgeTouchRA.add(new SimpleEdge(a.x, a.y, b.x, b.y, distance));
            else a.neighbors.add(new CityRoad(b, distance));
            System.out.printf("add new road %s(%f, %f)->%s(%f, %f), distance: %f\n", start, a.x, a.y, end, b.x, b.y, distance);
        }

        // load the river information
        reader.nextLine();
        reader.nextLine();
        reader.nextLine();
        while (true) {
            String curLine1 = reader.nextLine();
            if (curLine1.isEmpty()) break;
            String cueLine2 = reader.nextLine();
            String curLine3 = reader.nextLine();
            ArrayList<VerticeCity> oneSide = new ArrayList<>();
            ArrayList<VerticeCity> anotherSide = new ArrayList<>();
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
            if (distance < each.radius
                    && (intersectedX - x1) * (intersectedX - x2) - 0 < 0.0001)
                return true;
        }
        return false;
    }

    public Stack<VerticeCity> findShortestPath(String start, String end) {
        // this actually require the dijkstra algorithm
        System.out.println("size of map is: " + allPointsOfRoad.size());
        Stack<VerticeCity> result = new Stack<>();
        VerticeCity from = allPointsOfRoad.get(start);
        VerticeCity to = allPointsOfRoad.get(end);
        if (from == null || to == null) {
            System.out.printf("the path from %s to %s doesn't exist\n", start, end);
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
            System.out.printf("%s : %f\n", curOne.verticeName, curOne.distance);
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

    public void getPath(String start, String end) {
        Stack<VerticeCity> tmp = findShortestPath(start, end);
        while (!tmp.isEmpty())
            System.out.println(tmp.pop().verticeName);
    }

    public ArrayList<SimpleEdge> getAllEdge() {
        ArrayList<SimpleEdge> result = (ArrayList<SimpleEdge>) getAllEdge().clone();
        for (Map.Entry<String, VerticeCity> each : allPointsOfRoad.entrySet()) {
            for (CityRoad eachEdge : each.getValue().neighbors) {
                VerticeCity from = each.getValue();
                VerticeCity to = eachEdge.adjPoint;
                result.add(new SimpleEdge(from.x, from.y, to.x, to.y, eachEdge.distance));
            }
        }
        return result;
    }

    public ArrayList<SimplePoint> getAllPoint() {
        ArrayList<SimplePoint> result = (ArrayList<SimplePoint>) pointsInRA.clone();
        for (Map.Entry<String, VerticeCity> each : allPointsOfRoad.entrySet()) {
            result.add(new SimplePoint(each.getValue().x, each.getValue().y));
        }
        return result;
    }

	public static void main(String[] args) throws FileNotFoundException {
        SPCity newOne = new SPCity();
        newOne.getPath("vertice1", "vertice7");
    }

}

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
    double x, y;
    public SimplePoint (double x, double y) {
        this.x = x;
        this.y = y;
    }
}
