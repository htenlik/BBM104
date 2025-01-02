/**
 * The {@code Road} class represents a road between two points in a graph.
 * It encapsulates the points it connects, the distance between them, and a unique identifier.
 */
public class Road implements Comparable<Road> {
    private final Point point1;
    private final Point point2;
    private final int distance;
    private final int id;

    /**
     * Constructs a {@code Road} with the specified points, distance, and identifier.
     *
     * @param point1   the first point
     * @param point2   the second point
     * @param distance the distance between the two points
     * @param id       the unique identifier for the road
     */
    public Road(Point point1, Point point2, int distance, int id) {
        this.point1 = point1;
        this.point2 = point2;
        this.distance = distance;
        this.id = id;
    }

    /**
     * Compares this road with the specified road for order.
     * Roads are compared first by distance, then by ID if distances are equal.
     *
     * @param other the road to be compared
     * @return a negative integer, zero, or a positive integer as this road
     *         is less than, equal to, or greater than the specified road
     */
    @Override
    public int compareTo(Road other) {
        // Compare by distance first
        int distCompare = Integer.compare(this.distance, other.distance);
        // If distances are equal, compare by ID
        return distCompare != 0 ? distCompare : Integer.compare(this.id, other.id);
    }

    /**
     * Returns a string representation of the road.
     * The format is: "point1\t point2\t distance\tid".
     *
     * @return a string representation of the road
     */
    @Override
    public String toString() {
        return point1 + "\t" + point2 + "\t" + distance + "\t" + id;
    }

    /**
     * Returns the first point of the road.
     *
     * @return the first point
     */
    public Point getPoint1() {
        return point1;
    }

    /**
     * Returns the second point of the road.
     *
     * @return the second point
     */
    public Point getPoint2() {
        return point2;
    }

    /**
     * Returns the distance of the road.
     *
     * @return the distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Returns the unique identifier of the road.
     *
     * @return the identifier
     */
    public int getId() {
        return id;
    }
}
