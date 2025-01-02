/**
 * Represents a Minibus type of voyage. This class extends the {@code Voyage} class and provides
 * implementation specific to minibuses, which generally have simpler seating arrangements and pricing
 * compared to other types of buses.
 *
 * <p>Minibuses typically have a more compact layout and this implementation assumes a non-premium,
 * uniform pricing model for all seats.
 */
public class Minibus extends Voyage {

    /**
     * Constructs a new {@code Minibus} voyage with specified details. This constructor initializes
     * the voyage with the provided id, starting location, destination, number of seat rows, and
     * uniform seat pricing. It immediately initializes seats upon creation.
     *
     * @param id    the unique identifier for the voyage
     * @param from  the starting point of the voyage
     * @param to    the destination point of the voyage
     * @param rows  the number of rows of seats in the minibus
     * @param price the price for each seat in the minibus
     */
    public Minibus(int id, String from, String to, int rows, double price) {
        super(id, from, to, rows);
        initializeSeats(price); // Calls the method to initialize seats with the given price
    }

    /**
     * Returns the type of the bus as "Minibus".
     *
     * @return a string representing the type of bus, which is "Minibus" for this class
     */
    @Override
    public String getBusType() {
        return "Minibus";
    }

    /**
     * Initializes the seats in the minibus. All seats are initialized as non-premium with the same price.
     * This method assumes a 2+2 seating layout for simplicity, totaling to 4 seats per row.
     *
     * @param price the price to be set for each seat
     */
    @Override
    protected void initializeSeats(double price) {
        for (int i = 1; i <= this.rows * 4; i++) { // Iterates to create seats based on the number of rows * 4
            boolean isPremium = false; // Minibus does not use premium seats in this context
            seats.add(new Seat(i, isPremium, price)); // Adds a new seat to the list
        }
    }
}
