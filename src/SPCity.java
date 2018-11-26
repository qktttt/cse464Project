import java.util.HashMap;
import java.util.ArrayList;
import java.util.Queue;

public class SPCity {

	HashMap<String, VerticeCity> allPointsOfRoad;
	ArrayList<RA> restrictiveAreas;

	public SPCity() {
		allPointsOfRoad<>;
		restrictiveAreas = new ArrayList<RA>();
		LoadMap("cityMap.txt");
	}

	public void LoadMap(String dataFileName) {
		// in this method
		// the river, from my persepctive, it will be considered
		// as a vertice, and if two points are seperated by a river
		// the path can go cross the river directly

		// When loading the map from the file
		// I need to take care of the whether the road is 
		// one way or two way.

	}

	public boolean isPointInRa(double x, double y) {
		// detect whether a point is in the restrictive areas
		for(RA each : restrictiveAreas) {
			if(Math.hypot(each.x - x, each.y - y) < each.radius)
				return true;
		}
		return false;
	}

	public boolean isEdgeInRa(double x1, double y1, double x2, double y2) {
		// detect whether a edge is in the restrictive areas
		// if either point of a edge is in the RA
		// the edge related with this point will be removed before this method 
		// is called 

		for(RA each : restrictiveAreas) {
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
			if(distance < each.radius 
				&& (intersectedX - x1) * (intersectedX - x2)-0 < 0.0001)
				return true;
		}
		return false;
	}

	public Stack<VerticeCity> findShortestPath(String start, Start end) {
		// this actually require the dijkstra algorithm 
		Stack<VerticeCity> result = new Stack<>();
		VerticeCity from = allPointsOfRoad(start);
		VerticeCity to = allPointsOfRoad(end);
		if(from == null || to == null) {
			System.out.printf("the path from %s to %s doesn't exist\n", start, end);
			return result;
		}
		
		// run the djikstra algorithm to get the result;
	}
}
