import java.util.ArrayList;
import java.util.List;

/**
 * The abstract {@code Voyage} class represents a general voyage entity for a transportation system.
 * This class defines the structure and capabilities that all specific types of voyages must implement,
 * including initializing seats, managing ticket sales, refunds, and calculating revenues.
 * Different types of voyages (like standard bus, premium bus, and minibus) will extend this class.
 */
public abstract class Voyage {
    protected int id;
    protected String from;
    protected String to;
    protected int rows; // Note: This might be used differently based on the type of bus
    protected List<Seat> seats = new ArrayList<>();
    protected double totalCut = 0.0; // To track the total cut from the refund to add back to revenue

    /**
     * Constructs a new Voyage with specified details.
     *
     * @param id    the unique identifier for the voyage
     * @param from  the starting point of the voyage
     * @param to    the destination point of the voyage
     * @param rows  the number of rows of seats in the voyage vehicle
     */
    public Voyage(int id, String from, String to, int rows) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.rows = rows;
          }

    /**
     * Initializes seats within the voyage. Specific seat configuration is defined in subclasses.
     *
     * @param price the base price for seats
     */
    protected abstract void initializeSeats(double price);

    /**
     * Returns the type of bus as a string. Must be implemented by subclasses to specify the bus type.
     *
     * @return the type of the bus
     */
    public abstract String getBusType();

    /**
     * Attempts to sell tickets for a list of specified seat numbers.
     * Checks seat validity and availability before processing the sale.
     *
     * @param seatNumbers the list of seat numbers to sell
     * @return a {@code ticketSaleResult} indicating success or failure, including total price if successful or error message if failed.
     */
    public ticketSaleResult sellTickets(List<Integer> seatNumbers) {
        double totalPrice = 0.0;

        // Check if all specified seats are valid and available
        for (int seatNumber : seatNumbers) {
            if (seatNumber <= 0) {
                return new ticketSaleResult("ERROR: "+ seatNumber +
                        " is not a positive integer, seat number must be a positive integer!");
            }
            Seat seat = seats.stream()
                    .filter(s -> s.getSeatNumber() == seatNumber)
                    .findFirst()
                    .orElse(null);
            if (seat == null) {
                return new ticketSaleResult("ERROR: There is no such a seat!");
            } else if (seat.isSold()) {
                return new ticketSaleResult("ERROR: One or more seats already sold!");
            }
            totalPrice += seat.getPrice();
        }

        // If all seats are available, sell them
        seatNumbers.forEach(seatNumber -> seats.stream()
                .filter(seat -> seat.getSeatNumber() == seatNumber)
                .findFirst()
                .ifPresent(seat -> seat.setSold(true)));

        return new ticketSaleResult(totalPrice);
    }

    /**
     * Processes ticket refunds for specified seat numbers. Validates that each seat is sold before refunding.
     *
     * @param seatNumbers the list of seat numbers to refund
     * @return the total refund amount, or a negative value indicating specific errors
     */
    public double refundTickets(List<Integer> seatNumbers) {

        boolean negativeSeatNumber = seatNumbers.stream().anyMatch(seatNumber -> seatNumber <= 0);
        if (negativeSeatNumber) {
            return -2.0; // Indicates a negative seat number
        }

        boolean invalidSeatNumber = seatNumbers.stream()
                .anyMatch(seatNumber -> seats.stream()
                        .noneMatch(seat -> seat.getSeatNumber() == seatNumber));
        if (invalidSeatNumber) {
            return -3.0; // Indicates that one or more seats don't exist
        }

        boolean allSeatsSold = seatNumbers.stream()
                .allMatch(seatNumber -> seats.stream()
                        .anyMatch(seat -> seat.getSeatNumber() == seatNumber && seat.isSold()));

        if (!allSeatsSold) {
            return -1.0; // Indicates that one or more seats aren't sold
        }

        double totalRefund = 0.0;

        // Iterate over each specified seat number
        for (int seatNumber : seatNumbers) {
            Seat seat = seats.stream()
                    .filter(s -> s.getSeatNumber() == seatNumber && s.isSold())
                    .findFirst()
                    .orElse(null);


            double refundAmount = seat.getPrice() * (1 - getRefundCut() / 100.0);
            totalRefund += refundAmount;

            double originalPrice = seat.getPrice();

            double cutAmount = originalPrice - refundAmount; // The cut from the original price
            totalCut += cutAmount;

            // Mark the seat as not sold
            seat.setSold(false);
        }

        // Return the total refund amount
        return totalRefund;
    }

    /**
     * Generates a string representing detailed information about the voyage, including seat layout.
     *
     * @return detailed string of voyage information
     */
    public String printDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Voyage ").append(this.id).append("\n")
                .append(this.from).append("-").append(this.to).append("\n");

        String busType = getBusType();
        int seatsPerRow = busType.equals("Premium") ? 3 : 4; // Premium has 2+1 configuration, others have 2+2
        if(busType.equals("Minibus")) seatsPerRow = 2; // Minibus has 2 seats per row without divider

        StringBuilder seatingPlan = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
                // Calculate seat number based on i, j, and seatsPerRow
                int seatNumber = i * seatsPerRow + j + 1;
                Seat seat = seats.stream().filter(s -> s.getSeatNumber() == seatNumber).findFirst().orElse(null);

                if (seat != null) {
                    seatingPlan.append(seat.isSold() ? "X" : "*");
                    if (busType.equals("Premium") && j == 0) seatingPlan.append(" | ");// Add separator after the first seat for Premium

                    else if (busType.equals("Standard") && j == 1 && seatsPerRow > 2) seatingPlan.append(" | "); // Add separator after the second seat for Standard
                    else if (busType.equals("Premium") && j != seatsPerRow-1) seatingPlan.append(" ");// Add separator after the first seat for Premium
                    else if(j!=seatsPerRow-1) seatingPlan.append(" ");
                }
            }
            seatingPlan.append("\n");
        }
        details.append(seatingPlan);
        return details.toString();

    }

    /**
     * Computes and returns the total revenue for the voyage, including from sales and refunds.
     *
     * @return formatted string representing the total revenue
     */
    public String printRevenue() {
        return "Revenue: " + String.format("%.2f", calculateTotalRevenue()) + "\n";

    }

    /**
     * Cancels the voyage, marking all seats as unsold and adjusting revenue as necessary.
     */
    public void cancelVoyage() {
        // Mark all seats as unsold. Adjust revenue if necessary.
        for (Seat seat : seats) {
            seat.setSold(false);
        }
    }

    /**
     * Gets the default cut for refunds. Can be overridden by subclasses to provide specific cuts.
     *
     * @return the percentage cut for refunds
     */
    protected double getRefundCut() {
        return 0.0; // Default refund cut is 0%
    }

    /**
     * Calculates the total revenue accumulated from ticket sales and adjusted by any refund cuts.
     * This method sums up the prices of all sold seats and adds any accumulated cuts from refunds,
     * providing a complete overview of the financial outcome of the voyage.
     *
     * @return the total revenue of the voyage as a double value, formatted to two decimal places
     */
    public double calculateTotalRevenue() {
        double totalRevenue = 0.0;

        // Sum the prices of all sold seats
        totalRevenue += seats.stream()
                .filter(Seat::isSold)   // Filter only the sold seats
                .mapToDouble(Seat::getPrice)  // Get the price of each sold seat
                .sum();  // Sum up all the prices

        // Include the total cut from refunds to adjust the total revenue
        totalRevenue += totalCut;

        return totalRevenue;
    }

    /**
     * Gets the starting location of the voyage.
     *
     * @return the starting point of the voyage
     */
    public String getFrom() {
        return from;
    }

    /**
     * Gets the destination location of the voyage.
     *
     * @return the endpoint of the voyage
     */
    public String getTo() {
        return to;
    }

    /**
     * Nested class to encapsulate the result of a ticket sale operation, containing total price and error message.
     */
    class ticketSaleResult {
        double totalPrice = 0.0;
        String errorMessage = null;

        /**
         * Constructs a result for a successful sale.
         * @param totalPrice the total price of sold tickets
         */
        ticketSaleResult(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        /**
         * Constructs a result for a failed sale.
         * @param errorMessage the error message detailing why the sale failed
         */
        ticketSaleResult(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        /**
         * Determines if the sale was successful.
         * @return true if the sale was successful, false otherwise
         */
        boolean isSuccess() {
            return errorMessage == null;
        }
    }

}
