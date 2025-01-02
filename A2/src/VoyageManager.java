import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@code VoyageManager} class handles command processing and management of various voyages
 * within the booking system. It maintains a registry of all active voyages, processes commands related to
 * voyage initialization, ticket sales, refunds, and administrative reports, and logs interactions to a specified output file.
 */
public class VoyageManager {
    private static Map<Integer, Voyage> voyages = new HashMap<>();

    public static final String OUTPUT_FILE_PATH = "output.txt";

    /**
     * Processes various commands for voyage management:
     * - INIT_VOYAGE: Initializes a new voyage with specified details, checking for duplicate or invalid entries.
     * - SELL_TICKET: Processes ticket sales for specific seats on a specific voyage, handling errors like sold-out or invalid seats.
     * - REFUND_TICKET: Handles ticket refunds, ensuring the seats were previously sold and that the voyage allows refunds.
     * - PRINT_VOYAGE: Prints the details and revenue of a specified voyage to the output file.
     * - CANCEL_VOYAGE: Cancels a specified voyage, marking all seats as unsold and removing the voyage from the system.
     * - Z_REPORT: Generates a report of all voyages, detailing each one's status and revenue.
     *
     * Each command is logged to {@link #OUTPUT_FILE_PATH} along with any outputs or errors.
     *
     * @param input The command string containing the command type and its parameters separated by tabs.
     */
    public static void input(String input) {
        int voyageId;
        Voyage voyage;
        List<Integer> seatNumbers;

        String inputString = "COMMAND: " +
                input;

        FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, inputString, true, true);

        String[] temp = input.split("\t");
        String commandType = temp[0];

        label:
        switch (commandType) {

            case "INIT_VOYAGE":
                StringBuilder init_voyage = new StringBuilder(); // Prepare the output content

                String type = temp[1];
                int ID = Integer.parseInt(temp[2]);

                if (voyages.containsKey(ID)) {
                    init_voyage.append(String.format("ERROR: There is already a voyage with ID of %d!\n", ID));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                    break; // Exit the case block
                }

                if (ID<0){
                    init_voyage.append(String.format("ERROR: %d is not a positive integer, " +
                            "ID of a voyage must be a positive integer!\n",ID));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                    break;
                }

                String from = temp[3];
                String to = temp[4];
                int rows = Integer.parseInt(temp[5]);
                if (rows<0){
                    init_voyage.append(String.format("ERROR: %d is not a positive integer, " +
                            "number of seat rows of a voyage must be a positive integer!\n",rows));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                    break;
                }

                double price = Double.parseDouble(temp[6]);
                if (price<0){
                    init_voyage.append(String.format("ERROR: %.0f is not a positive number, " +
                            "price must be a positive number!\n",price));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                    break;
                }



                switch (type) {
                    case "Standard": {
                        int refundCuts = Integer.parseInt(temp[7]);
                        if (refundCuts<0){
                            init_voyage.append(String.format("ERROR: %d is not an integer that is in range of [0, 100]," +
                                    " refund cut must be an integer that is in range of [0, 100]!\n",refundCuts));
                            FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                            break;
                        }
                        voyages.put(ID, new StandardBus(ID, from, to, rows, price, refundCuts));
                        init_voyage.append(String.format("Voyage %d was initialized as a standard (2+2) voyage from %s to %s with %.2f TL" +
                                        " priced %d regular seats. Note that refunds will be %d%% less than the" +
                                        " paid amount.%n",
                                ID, from, to, price, rows * 4, refundCuts));
                        FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                        break label;
                    }
                    case "Premium": {
                        int refundCuts = Integer.parseInt(temp[7]);
                        if (refundCuts<0){
                            init_voyage.append(String.format("ERROR: %d is not an integer that is in range of [0, 100]," +
                                    " refund cut must be an integer that is in range of [0, 100]!\n",refundCuts));
                            FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                            break label;
                        }

                        int premiumFee = Integer.parseInt(temp[8]);
                        if (premiumFee<0){
                            init_voyage.append(String.format("ERROR: %d is not a non-negative integer," +
                                    " premium fee must be a non-negative integer!\n",premiumFee));
                            FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                            break label;
                        }

                        voyages.put(ID, new PremiumBus(ID, from, to, rows, price, refundCuts, premiumFee));
                        init_voyage.append(String.format(
                                "Voyage %d was initialized as a premium (1+2) voyage from %s to %s with %.2f TL " +
                                        "priced %d regular seats and %.2f TL priced %d premium seats. Note that " +
                                        "refunds will be %d%% less than the paid amount.%n",
                                ID, from, to, price, rows * 2, price * (1 + premiumFee / 100.0), rows, refundCuts)); // Assuming 1+2 configuration

                        FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                        break label;
                    }
                    case "Minibus":  // Minibus
                        voyages.put(ID, new Minibus(ID, from, to, rows, price));
                        init_voyage.append(String.format(
                                "Voyage %d was initialized as a minibus (2) voyage from %s to %s with %.2f TL " +
                                        "priced %d regular seats. Note that minibus tickets are not refundable.%n",
                                ID, from, to, price, rows * 2)); // Assuming 2 seats per row

                        FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                        break label;

                    default:
                        init_voyage.append("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!\n");
                        FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, init_voyage.toString(), true, false);
                        break label;
                }


            case "SELL_TICKET":
                StringBuilder output = new StringBuilder();

                voyageId = Integer.parseInt(temp[1]);
                voyage = voyages.get(voyageId);

                if (temp.length<3) {
                    String notFoundMsg = "ERROR: Erroneous usage of \"SELL_TICKET\" command!";
                    // Use FileOutput to write the message to a file
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, notFoundMsg, true, true);
                    break;
                }

                if (!voyages.containsKey(voyageId)) {
                    output.append(String.format("ERROR: There is no voyage with ID of %d!\n", voyageId));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, output.toString(), true, false);
                    break; // Exit the case block
                }

                // Parse seat numbers from the command
                seatNumbers = Arrays.stream(temp[2].split("_"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                Voyage.ticketSaleResult result = voyage.sellTickets(seatNumbers);

                if (!result.isSuccess()) {
                    output.append(result.errorMessage);
                } else {
                    // Generate the summary output
                    String seatRange = seatNumbers.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining("-"));
                    output.append(String.format("Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.",
                            seatRange, voyageId, voyage.getFrom(), voyage.getTo(), result.totalPrice));
                }

                // Write the output to the file
                FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, output.toString(), true, true);
                break;

            case "REFUND_TICKET":
                voyageId = Integer.parseInt(temp[1]);
                voyage = voyages.get(voyageId);

                StringBuilder refund = new StringBuilder();

                if (temp.length<3) {
                    String notFoundMsg = "ERROR: Erroneous usage of \"REFUND_TICKET\" command!";
                    // Use FileOutput to write the message to a file
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, notFoundMsg, true, true);
                    break;
                }

                if (!voyages.containsKey(voyageId)) {
                    refund.append(String.format("ERROR: There is no voyage with ID of %d!\n", voyageId));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, refund.toString(), true, false);
                    break; // Exit the case block
                }

                if (voyage.getBusType().equals("Minibus")) {
                    String noRefundMsg = "ERROR: Minibus tickets are not refundable!";
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, noRefundMsg, true, true);
                    break;
                }

                // Parse seat numbers for refund
                seatNumbers = Arrays.stream(temp[2].split("_"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());


                double totalRefund = voyage.refundTickets(seatNumbers);
                if (totalRefund == -1.0) {
                    refund.append("ERROR: One or more seats are already empty!\n");
                } else if (totalRefund == -2.0) {
                    refund.append(String.format("ERROR: %d is not a positive integer, " +
                            "seat number must be a positive integer!\n", seatNumbers.get(0)));
                } else if (totalRefund == -3.0) {
                    refund.append("ERROR: There is no such a seat!\n");
                } else {
                    // If refund is successful, generate output
                    String seatRange = seatNumbers.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining("-"));
                    refund.append(String.format("Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.%n",
                            seatRange, voyageId, voyage.getFrom(), voyage.getTo(), totalRefund));

                }
                FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, refund.toString(), true, false);
                break;



            case "PRINT_VOYAGE":
                if (temp.length != 2) {
                    String notFoundMsg = "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!";
                    // Use FileOutput to write the message to a file
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, notFoundMsg, true, true);
                    break;
                }


                voyageId = Integer.parseInt(temp[1]);
                voyage = voyages.get(voyageId);

                StringBuilder print_voyage = new StringBuilder();

                if (voyageId<0){
                    print_voyage.append(String.format("ERROR: %d is not a positive integer, " +
                            "ID of a voyage must be a positive integer!\n",voyageId));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, print_voyage.toString(), true, false);
                    break;
                }

                if (!voyages.containsKey(voyageId)) {
                    print_voyage.append(String.format("ERROR: There is no voyage with ID of %d!\n", voyageId));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, print_voyage.toString(), true, false);
                    break; // Exit the case block
                }

                print_voyage.append(voyage.printDetails());
                print_voyage.append(voyage.printRevenue());
                FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, print_voyage.toString(), true, false);


                break;

            case "CANCEL_VOYAGE":
                if (temp.length != 2) {
                    String notFoundMsg = "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!";
                    // Use FileOutput to write the message to a file
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, notFoundMsg, true, true);
                    break;
                }


                StringBuilder cancelVoyage = new StringBuilder();

                int voyageIdToCancel = Integer.parseInt(temp[1]);
                Voyage voyageToCancel = voyages.get(voyageIdToCancel);

                if (voyageIdToCancel<0){
                    cancelVoyage.append(String.format("ERROR: %d is not a positive integer, " +
                            "ID of a voyage must be a positive integer!\n",voyageIdToCancel));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, cancelVoyage.toString(), true, false);
                    break;
                }

                if (!voyages.containsKey(voyageIdToCancel)) {
                    cancelVoyage.append(String.format("ERROR: There is no voyage with ID of %d!\n", voyageIdToCancel));
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, cancelVoyage.toString(), true, false);
                    break; // Exit the case block
                }

                cancelVoyage.append(String.format("Voyage %s was successfully cancelled!\nVoyage details can be found below:\n", voyageIdToCancel));


                // Print the last state of the voyage
                cancelVoyage.append(voyageToCancel.printDetails());

                // Handle voyage cancellation (this method will reset the seats to unsold)
                voyageToCancel.cancelVoyage();

                cancelVoyage.append(voyageToCancel.printRevenue());
                // Remove the voyage from the collection
                voyages.remove(voyageIdToCancel);
                FileOutput.writeToFile(OUTPUT_FILE_PATH, cancelVoyage.toString(), true, false);

                break;

            case "Z_REPORT":
                if(temp.length>1){
                    String notFoundMsg = "ERROR: Erroneous usage of \"Z_REPORT\" command!";
                    // Use FileOutput to write the message to a file
                    FileOutput.writeToFile(VoyageManager.OUTPUT_FILE_PATH, notFoundMsg, true, true);
                    break;

                }
                printZReport();
                break;


            default:
                String noCommandMsg = "ERROR: There is no command namely " + commandType+ "!\n";
                FileOutput.writeToFile(OUTPUT_FILE_PATH, noCommandMsg, true, false);

                break;
        }


    } // input

    /**
     * Prints a detailed Z report to the output file. The Z report lists the details and revenues of all voyages
     * sorted by their IDs. If no voyages are available, it logs an appropriate message.
     */
    public static void printZReport() {
        StringBuilder zReport = new StringBuilder();
        zReport.append("Z Report:\n");

        if (voyages.isEmpty()) {
            zReport.append("----------------\n");
            zReport.append("No Voyages Available!\n");
            zReport.append("----------------");
        } else {
            zReport.append("----------------");

            voyages.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        Voyage voyage = entry.getValue();
                        zReport.append("\n")
                                .append(voyage.printDetails()) // Use the string returned by printDetails()
                                .append(voyage.printRevenue())
                                .append("----------------"); // Use the string returned by printRevenue()
                    });
        }

        FileOutput.writeToFile(OUTPUT_FILE_PATH, zReport.toString(), true, true);

    } // printZReport
} // class
