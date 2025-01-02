import java.util.*;

/**
 * The {@code Graph} class represents a graph structure with points and roads.
 * It provides methods to add points and roads, find the fastest route, construct a Barely Connected Map (BCM),
 * and retrieve all roads in the graph.
 */
public class Graph {
    private final Map<Point, List<Road>> neighborsList;
    private final Map<Point, List<Road>> originalNeighborsList;

    /**
     * Constructs an empty {@code Graph}.
     */
    public Graph() {
        neighborsList = new HashMap<>();
        originalNeighborsList = new HashMap<>();
    }

    /**
     * Adds a point to the graph.
     *
     * @param point the point to add
     */
    public void addPoint(Point point) {
        neighborsList.putIfAbsent(point, new ArrayList<>());
        originalNeighborsList.putIfAbsent(point, new ArrayList<>());
    }

    /**
     * Adds a road to the graph, connecting two points.
     *
     * @param road the road to add
     */
    public void addRoad(Road road) {
        neighborsList.get(road.getPoint1()).add(road);
        neighborsList.get(road.getPoint2()).add(new Road(road.getPoint2(), road.getPoint1(), road.getDistance(), road.getId()));
        // Always add road in both directions but maintain original for output reference
        originalNeighborsList.get(road.getPoint1()).add(road);
    }

    /**
     * Finds the fastest route between two points, optionally restricted to a subset of allowed roads.
     *
     * @param start        the starting point
     * @param end          the ending point
     * @param allowedRoads the list of allowed roads (can be null or empty for no restrictions)
     * @return the list of roads representing the fastest route
     */
    public List<Road> fastestRoute(Point start, Point end, List<Road> allowedRoads) {
        Map<Point, List<Road>> restrictedNeighborsList;

        // Build the restricted graph if allowed roads are provided
        if (allowedRoads != null && !allowedRoads.isEmpty()) {
            restrictedNeighborsList = buildRestrictedGraph(allowedRoads);// Use barely connected map's neighbor list
        } else {
            restrictedNeighborsList = new HashMap<>(neighborsList); // Use a copy of the main neighbor list
        }

        Map<Point, Integer> distances = new HashMap<>();
        Map<Point, Point> predecessors = new HashMap<>();
        PriorityQueue<PointDistance> priorityQueue = new PriorityQueue<>();
        Set<Point> visited = new HashSet<>();

        // Initialize distances to infinity, except for the start point
        for (Point p : restrictedNeighborsList.keySet()) {
            distances.put(p, Integer.MAX_VALUE);

        }
        distances.put(start, 0);
        priorityQueue.add(new PointDistance(start, 0, -1));  // Initialize with an invalid ID

        // Process the priority queue
        while (!priorityQueue.isEmpty()) {
            PointDistance pd = priorityQueue.poll();
            Point current = pd.point;
            // Stop if we reached the end point
            if (current.equals(end)) break;

            // Skip already visited points
            if (!visited.add(current)) continue;

            int currentDistance = distances.get(current);
            for (Road road : restrictedNeighborsList.getOrDefault(current, Collections.emptyList())) {
                Point neighbor = road.getPoint2();
                int newDistance = currentDistance + road.getDistance();
                // Update the distance if a shorter path is found
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current);
                    priorityQueue.add(new PointDistance(neighbor, newDistance, road.getId()));
                }
            }
        }

        return constructNewPath(predecessors, start, end);
    }

    /**
     * Builds a restricted graph using only the provided roads.
     *
     * @param roads the list of allowed roads
     * @return the restricted graph as a neighbor list
     */
    private Map<Point, List<Road>> buildRestrictedGraph(List<Road> roads) {
        Map<Point, List<Road>> restrictedGraph = new HashMap<>();
        for (Road road : roads) {
            restrictedGraph.putIfAbsent(road.getPoint1(), new ArrayList<>());
            restrictedGraph.putIfAbsent(road.getPoint2(), new ArrayList<>());
            restrictedGraph.get(road.getPoint1()).add(road);
            restrictedGraph.get(road.getPoint2()).add(new Road(road.getPoint2(), road.getPoint1(), road.getDistance(), road.getId()));  // Add reverse for undirected graph
        }
        return restrictedGraph;
    }

    /**
     * Reconstructs the path from the predecessors map.
     *
     * @param predecessors the map of predecessors
     * @param start        the starting point
     * @param end          the ending point
     * @return the list of roads representing the path
     */
    private List<Road> constructNewPath(Map<Point, Point> predecessors, Point start, Point end) {
        LinkedList<Road> path = new LinkedList<>();
        Point current = end;
        while (current != null && !current.equals(start)) {
            Point previous = predecessors.get(current);
            if (previous == null) break;
            path.addFirst(findRoadBetweenTwoPoints(previous, current));
            current = previous;
        }
        return path;
    }

    /**
     * Finds the road between two points.
     *
     * @param from the starting point
     * @param to   the ending point
     * @return the road connecting the two points
     */
    private Road findRoadBetweenTwoPoints(Point from, Point to) {
        for (Road road : neighborsList.get(from)) {
            if (road.getPoint2().equals(to)) {
                return findOriginalRoad(road.getId());
            }
        }
        return null;
    }

    /**
     * Constructs the Barely Connected Map (BCM) of the graph.
     *
     * @return the list of roads in the BCM
     */
    public List<Road> barelyConnectedMap() {
        List<Road> result = new ArrayList<>();
        if (neighborsList.isEmpty()) return result;

        // Start from the point with the smallest name
        Point start = Collections.min(neighborsList.keySet(), Comparator.comparing(Point::getName));
        PriorityQueue<Road> pq = new PriorityQueue<>(Comparator.comparing(Road::getDistance).thenComparing(Road::getId));
        Set<Point> includedPoints = new HashSet<>();
        includedPoints.add(start);
        enqueueConnectedRoads(start, pq, includedPoints);

        while (!pq.isEmpty() && includedPoints.size() < neighborsList.size()) {
            Road road = pq.poll();
            Point nextPoint = includedPoints.contains(road.getPoint1()) ? road.getPoint2() : road.getPoint1();
            if (includedPoints.contains(nextPoint)) continue;

            Road originalRoad = findOriginalRoad(road.getId());
            if (originalRoad != null) {
                result.add(originalRoad);
                includedPoints.add(nextPoint);
                enqueueConnectedRoads(nextPoint, pq, includedPoints);
            }
        }

        return result;
    }

    /**
     * Finds the original road by its ID to stick to the original road order.
     *
     * @param roadId the ID of the road
     * @return the original road, or null if not found
     */
    private Road findOriginalRoad(int roadId) {
        for (List<Road> roads : originalNeighborsList.values()) {
            for (Road road : roads) {
                if (road.getId() == roadId) {
                    return road;
                }
            }
        }
        return null;
    }

    /**
     * Enqueues all roads connected to a given point that are not yet included.
     *
     * @param point         the point to process
     * @param pq            the priority queue for roads
     * @param includedPoints the set of already included points
     */
    private void enqueueConnectedRoads(Point point, PriorityQueue<Road> pq, Set<Point> includedPoints) {
        for (Road road : neighborsList.get(point)) {
            if (!includedPoints.contains(road.getPoint2())) {
                pq.add(road);
            }
        }
    }

    /**
     * Returns a list of all roads in the graph.
     *
     * @return the list of all roads
     */
    public List<Road> getAllRoads() {
        Set<Road> allRoads = new HashSet<>(); // Use set to avoid duplicates
        for (List<Road> roads : neighborsList.values()) {
            allRoads.addAll(roads);
        }
        return new ArrayList<>(allRoads);
    }

    /**
     * The {@code PointDistance} class is a helper class for managing points and their distances in the priority queue.
     */
    private static class PointDistance implements Comparable<PointDistance> {
        Point point;
        int distance;
        int roadId;  // Add road ID for tiebreak

        /**
         * Constructs a {@code PointDistance} with the specified point, distance, and road ID.
         *
         * @param point    the point
         * @param distance the distance
         * @param roadId   the road ID
         */
        public PointDistance(Point point, int distance, int roadId) {
            this.point = point;
            this.distance = distance;
            this.roadId = roadId;
        }

        /**
         * Compares this {@code PointDistance} with another for order.
         * Comparison is based on distance first, then road ID.
         *
         * @param other the {@code PointDistance} to be compared
         * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
         */
        @Override
        public int compareTo(PointDistance other) {
            if (this.distance != other.distance) {
                return Integer.compare(this.distance, other.distance);
            } else {
                return Integer.compare(this.roadId, other.roadId);  // Tiebreak on road ID
            }
        }
    }

}
