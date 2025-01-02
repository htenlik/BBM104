/**
 * The {@code Point} class represents a point in a graph.
 * It encapsulates a single field, {@code name}, which identifies the point.
 */
public class Point {
    private final String name;

    /**
     * Constructs a {@code Point} with the specified name.
     *
     * @param name the name of the point
     */
    public Point(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this point.
     *
     * @return the name of the point
     */
    public String getName() {
        return name;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two {@code Point} objects are considered equal if they have the same name.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return name.equals(point.name);
    }

    /**
     * Returns a hash code value for the point.
     * This method is supported for the benefit of hash tables such as those provided by {@link java.util.HashMap}.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }


    /**
     * Returns a string representation of the point.
     *
     * @return the name of the point
     */
    @Override
    public String toString() {
        return name;
    }
}



