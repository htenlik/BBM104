import java.io.IOException;
import java.nio.file.NoSuchFileException;


/**
 * The {@code BookingSystem} class represents the entry point of the booking system application.
 * This class is responsible for handling the initial file input/output operations and starting
 * the command processing for managing voyages using the {@code VoyageManager}.
 *
 * <p>It reads the input commands from a file, processes each command, and if the final command
 * isn't a Z_REPORT, it triggers the generation of a Z report. The class also handles the reading
 * and rewriting of debug information from a secondary file provided as a command-line argument.
 */

public class BookingSystem {
    /**
     * The main method of the booking system. It reads commands from the input file specified
     * by the first command-line argument and outputs the results and logs to the output file
     * specified by the second command-line argument.
     *
     * @param args Command-line arguments where
     *             args[0] should be the path to the input file containing commands,
     *             args[1] should be the path to the output file for debug information and command outputs.
     * @throws ArrayIndexOutOfBoundsException If the required command-line arguments are not provided.
     * @throws NoSuchFileException If the specified file paths do not exist or cannot be accessed.
     * @throws IOException If an I/O error occurs reading from the input file or writing to the output file.
     * @throws Exception If any other unexpected error occurs during the execution of the program.
     */
    public static void main(String[] args) {

        try {
            String[] input = FileInput.readFile(args[0], true, true);

            String lastCommand = "";

            for (String line : input) {
                VoyageManager.input(line);
                lastCommand = line.split("\t")[0];

            }

            if (!lastCommand.equals("Z_REPORT")) {
                VoyageManager.printZReport();
            }


            String[] debug = FileInput.readFile(args[1], true, true);


            FileOutput.writeToFile(args[1], "", false, false);

            for (int i = 0; debug.length > i; i++) {
                FileOutput.writeToFile(args[1], debug[i], true, false);
                if (i != debug.length - 1) {
                    FileOutput.writeToFile(args[1], "", true, true);
                }

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            String error = "ERROR: This program works exactly with two command line arguments, " +
                    "the first one is the path to the input file whereas the second one is the " +
                    "path to the output file. Sample usage can be as follows:" +
                    " \"java8 BookingSystem input.txt output.txt\". " +
                    "Program is going to terminate!";

            System.out.println(error);
        } catch (NoSuchFileException e) {
            System.out.println("ERROR: This program cannot read from the " + e.getFile()+
                    ", either this program does not have read " +
                    "permission to read that file or file does not exist. " +
                    "Program is going to terminate! ");
        } catch (IOException e) {
            System.out.println("An I/O error occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}