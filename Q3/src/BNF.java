import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This class provides functionality to parse and expand BNF (Backus-Naur Form) grammar
 * rules stored in a text file and outputs the expanded result for the start symbol 'S'.
 */
public class BNF {
    private static HashMap<String, Collection<String>> rules = new HashMap<>();

    /**
     * The main method reads the input file, processes BNF rules, expands them starting
     * from the 'S' symbol, and writes the result to the output file.
     *
     * @param args Command line arguments where args[0] is the input file path and
     *             args[1] is the output file path.
     */
    public static void main(String[] args) {
        try {
            String[] lines = FileInput.readFile(args[0], true, true);
            parseInput(lines);
            Collection<String> productions = rules.get("S");  // Get the productions for 'S'

            StringBuilder result = new StringBuilder();  // Initialize StringBuilder to accumulate the results
            for (String production : productions) {
                String expanded = expand(production);  // Expand each production
                if (result.length() > 0) result.append("|");
                result.append(expanded);
            }

            // Format final result to have outermost parentheses
            String finalResult = "(" + result + ")";
            FileOutput.writeToFile(args[1], finalResult, false, false);  // Write the final result to file
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses BNF grammar rules from an array of strings and stores them in a HashMap.
     * Each line represents a BNF production rule where the key is the non-terminal
     * and the value is a list of productions.
     *
     * @param lines An array of strings, each containing a BNF rule.
     */
    private static void parseInput(String[] lines) {
        for (String line : lines) {
            String[] parts = line.split("->");
            String nonTerminal = parts[0].trim();
            String[] productions = parts[1].split("\\|");
            rules.put(nonTerminal, Arrays.asList(productions));
        }
    }

    /**
     * Recursively expands a given production according to the BNF rules stored in the HashMap.
     * It handles both terminal and non-terminal symbols.
     *
     * @param production A string representing a production that may contain non-terminals.
     * @return A string representing the expanded production.
     */
    private static String expand(String production) {
        // Check if the production is purely terminal symbols
        if (!production.matches(".*[A-Z].*")) {
            return production;
        }
        StringBuilder result = new StringBuilder();

        // Split the production into parts, handling terminals and non-terminals
        String[] parts = production.split("(?<=\\G.)");

        for (String part : parts) {
            if (rules.containsKey(part)) {

                // If it's a non-terminal, expand each of its productions
                result.append("(");
                Collection<String> subProductions = rules.get(part);
                String expandedProductions = subProductions.stream()
                        .map(BNF::expand)
                        .collect(Collectors.joining("|"));

                result.append(expandedProductions);
                result.append(")");
            } else {
                // Directly append terminal symbols
                result.append(part);
            }
        }
        return result.toString();
    }
}
