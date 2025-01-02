import java.util.Comparator;
import java.util.List;

/**
 * The {@code Analyzer} class is responsible for loading, analyzing, and outputting geographic or network data.
 * It uses a {@code Graph} to represent the data and provides methods to load data from a file, perform various analyses, and write the results to an output file.
 */
public class Analyzer {
    private final Graph graph;

    /**
     * Constructs an {@code Analyzer} and initializes the graph.
     */
    public Analyzer() {
        this.graph = new Graph(); // Initialization
    }

    /**
     * Loads data from the input file, performs analysis, and writes the results to the output file.
     *
     * @param inputFile  the path to the input file
     * @param outputFile the path to the output file
     */
    public void loadAndAnalyze(String inputFile, String outputFile) {
        String[] lines = FileInput.readFile(inputFile, true, true);

        // Check if reading the file was successful
        if (lines == null) {
            System.err.println("Error reading the file.");
            return;
        }

        String[] points = lines[0].split("\t");
        Point start = new Point(points[0]);
        Point end = new Point(points[1]);

        for (int i = 1; i < lines.length; i++) {
            String[] data = lines[i].split("\t");
            Point point1 = new Point(data[0]);
            Point point2 = new Point(data[1]);
            int distance = Integer.parseInt(data[2]);
            int id = Integer.parseInt(data[3]);

            graph.addPoint(point1);
            graph.addPoint(point2);
            graph.addRoad(new Road(point1, point2, distance, id));
        }

        analyzeAndOutput(start, end, outputFile);
    }

    /**
     * Analyzes the graph to find the fastest routes and the Barely Connected Map (BCM),
     * then writes the results to the output file.
     *
     * @param start      the starting point
     * @param end        the ending point
     * @param outputFile the path to the output file
     */
    public void analyzeAndOutput(Point start, Point end, String outputFile) {
        // Find the fastest route in the original graph. To get the fastest route, we pass null for the allowed roads.
        List<Road> fastestRoute = graph.fastestRoute(start, end, null);
        // Construct the Barely Connected Map (BCM)
        List<Road> barelyConnectedMap = graph.barelyConnectedMap();
        // Find the fastest route in the BCM
        List<Road> fastestRouteInBCM = graph.fastestRoute(start, end, barelyConnectedMap);

        int totalDistanceOriginal = calculateTotalDistance(graph.getAllRoads())/2; // Divide by 2 to account for bidirectional roads
        int totalDistanceBCM = calculateTotalDistance(barelyConnectedMap);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Fastest Route from %s to %s (%d KM):\n", start, end, calculateTotalDistance(fastestRoute)));
        fastestRoute.forEach(road -> sb.append(road).append("\n"));

        sb.append("Roads of Barely Connected Map is:\n");
        barelyConnectedMap.sort(Comparator.comparing(Road::getDistance).thenComparing(Road::getId));
        barelyConnectedMap.forEach(road -> sb.append(road).append("\n"));

        sb.append(String.format("Fastest Route from %s to %s on Barely Connected Map (%d KM):\n", start, end, calculateTotalDistance(fastestRouteInBCM)));
        fastestRouteInBCM.forEach(road -> sb.append(road).append("\n"));


        double materialRatio = (double) totalDistanceBCM / totalDistanceOriginal;
        double routeRatio = (double) calculateTotalDistance(fastestRouteInBCM) / calculateTotalDistance(fastestRoute);
        sb.append("Analysis:\n");
        sb.append(String.format("Ratio of Construction Material Usage Between Barely Connected and Original Map: %.2f\n", materialRatio));
        sb.append(String.format("Ratio of Fastest Route Between Barely Connected and Original Map: %.2f", routeRatio));

        FileOutput.writeToFile(outputFile, sb.toString(), false, false);
    }

    /**
     * Calculates the total distance of the roads in the provided list.
     *
     * @param roads the list of roads
     * @return the total distance of the roads
     */
    public int calculateTotalDistance(List<Road> roads) {
        int totalDistance = 0;
        for (Road road : roads) {
            totalDistance += road.getDistance();
        }
        return totalDistance;
    }
}
