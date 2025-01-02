import java.util.Locale;
/**
 * The {@code MapAnalyzer} class serves as the entry point to the program which analyzes geographic or network data from an input file and produces an analysis in an output file.
 * This class handles the command-line arguments and initiates the analysis process by creating an instance of {@code Analyzer}.
 */
public class MapAnalyzer {

    /**
     * Main method which serves as the entry point for the program. It expects two command-line arguments:
     * the first for the input file path and the second for the output file path.
     *
     * @param args the command line arguments where
     *             args[0] should be the path to the input file and
     *             args[1] should be the path to the output file.
     *             If the correct number of arguments is not provided, the usage information is printed.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        // Check if the correct number of arguments are passed
        if (args.length < 2) {
            System.out.println("Usage: java MapAnalyzer <inputFile> <outputFile>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        Analyzer analyzer = new Analyzer();
        analyzer.loadAndAnalyze(inputFile, outputFile);
    }
}
