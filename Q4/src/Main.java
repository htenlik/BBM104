public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Main <inputFile> <outputFile>");
            return;
        }

        // Instantiate inventory
        Inventory<Item> inventory = new Inventory<>();

        // StringBuilder to collect output
        StringBuilder sb = new StringBuilder();

        // Reading input file
        try {
            String[] input = FileInput.readFile(args[0], true, true);

            for(String line : input) {
                Command.handleCommand(line, inventory, sb);
            }

            // Writing output to file
            FileOutput.writeToFile(args[1], sb.toString(), false, false);
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
