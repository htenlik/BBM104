/**
 * Represents a StandardBus type of voyage. This class extends the {@code Voyage} class and provides
 * a specific implementation for standard bus services. Standard buses usually offer uniform seating
 * with no premium options, maintaining a single price across all seats.
 *
 * <p>This implementation also introduces a refund cut which determines the percentage of the ticket price
 * that is retained in case of a refund.
 */
public class StandardBus extends Voyage {
    private double refundCut; // Percentage of the price retained when a refund is issued

    /**
     * Constructs a new {@code StandardBus} voyage with specified details including refund cut.
     * This constructor initializes the voyage with the provided id, starting location, destination,
     * number of seat rows, and sets up uniform seat pricing across all seats.
     *
     * @param id        the unique identifier for the voyage
     * @param from      the starting point of the voyage
     * @param to        the destination point of the voyage
     * @param rows      the number of rows of seats in the standard bus
     * @param price     the uniform price for each seat
     * @param refundCut the percentage of the seat price that is not refunded
     */
    public StandardBus(int id, String from, String to, int rows, double price, double refundCut) {
        super(id, from, to, rows); // Initialize the base class properties
        this.refundCut = refundCut;
        initializeSeats(price); // Set up uniform seat pricing
    }

    /**
     * Returns the type of the bus as "Standard".
     *
     * @return a string representing the type of bus, which is "Standard" for this class
     */
    @Override
    public String getBusType() {
        return "Standard";
    }

    /**
     * Retrieves the refund cut percentage for this voyage. This percentage determines how much of the ticket
     * price is retained by the company in the event of a refund.
     *
     * @return the refund cut percentage
     */
    @Override
    protected double getRefundCut() {
        return refundCut;
    }

    /**
     * Initializes seats for the standard bus. This method sets up seats with a uniform pricing model,
     * assuming a 2+2 seating layout. All seats are non-premium.
     *
     * @param price the uniform price for each seat
     */
    @Override
    protected void initializeSeats(double price) {
        for (int i = 1; i <= this.rows * 4; i++) { // Iterates to create seats based on the number of rows * 4
            boolean isPremium = false; // All seats in a standard bus are non-premium
            seats.add(new Seat(i, isPremium, price)); // Adds new seats with uniform pricing
        }
    }
}
