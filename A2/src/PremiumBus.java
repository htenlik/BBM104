/**
 * Represents a PremiumBus type of voyage. This class extends the {@code Voyage} class and provides
 * a specific implementation for premium bus services, which typically feature more luxurious and
 * spacious seating arrangements with different pricing strategies for standard and premium seats.
 *
 * <p>The premium bus service introduces the concept of a refund cut and a premium fee that modifies
 * the base price for premium seats.
 */
public class PremiumBus extends Voyage {
    private double refundCut; // Percentage of the price retained when a refund is issued
    private int premiumFee; // Additional percentage charged on top of the base price for premium seats

    /**
     * Constructs a new {@code PremiumBus} voyage with specified details including premium fee and refund cut.
     * This constructor initializes the voyage with id, start location, destination, seat rows, and sets up
     * seat pricing based on premium and standard configurations.
     *
     * @param id         the unique identifier for the voyage
     * @param from       the starting point of the voyage
     * @param to         the destination point of the voyage
     * @param rows       the number of rows of seats in the premium bus
     * @param price      the base price for standard seats
     * @param refundCut  the percentage of the seat price that is not refunded
     * @param premiumFee the additional percentage that increases the base price for premium seats
     */
    public PremiumBus(int id, String from, String to, int rows, double price, double refundCut, int premiumFee) {
        super(id, from, to, rows); // Initialize the base class properties
        this.refundCut = refundCut;
        this.premiumFee = premiumFee;
        initializeSeats(price); // Set up seats with differentiated pricing
    }

    /**
     * Returns the type of the bus as "Premium".
     *
     * @return a string representing the type of bus, which is "Premium" for this class
     */
    @Override
    public String getBusType() {
        return "Premium";
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
     * Initializes seats for the premium bus. This method sets up both premium and standard seats,
     * calculating the price for premium seats based on the standard price and premium fee.
     *
     * @param standardPrice the base price for standard seats
     */
    @Override
    protected void initializeSeats(double standardPrice) {
        double premiumPrice = standardPrice + standardPrice * (premiumFee / 100.0); // Calculate the price for premium seats
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= 3; j++) {
                int seatNumber = (i - 1) * 3 + j;
                boolean isPremium = (j == 1); // First seat in each group is premium
                double price = isPremium ? premiumPrice : standardPrice;
                seats.add(new Seat(seatNumber, isPremium, price));
            }
        }
    }
}
