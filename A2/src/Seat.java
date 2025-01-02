/**
 * Represents a seat within a voyage. This class encapsulates the details of a seat such as its number,
 * whether it is sold, if it is a premium seat, and its price. It provides functionality to manage the seat's
 * sale status and access its properties.
 */
public class Seat {
    private int seatNumber;
    private boolean isSold;
    private boolean isPremium;
    private double price;

    /**
     * Constructs a new Seat with a specified number, premium status, and price. The seat is initially not sold.
     *
     * @param seatNumber the unique number identifying this seat within a voyage
     * @param isPremium  indicates if the seat offers premium features (e.g., more space, better location)
     * @param price      the cost of purchasing this seat
     */
    public Seat(int seatNumber, boolean isPremium, double price) {
        this.seatNumber = seatNumber;
        this.isSold = false; // Seats are initially not sold
        this.isPremium = isPremium;
        this.price = price;
    }

    /**
     * Returns the seat number of this seat.
     *
     * @return the seat's unique number
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Returns whether this seat is currently sold.
     *
     * @return {@code true} if the seat is sold, {@code false} otherwise
     */
    public boolean isSold() {
        return isSold;
    }

    /**
     * Sets the sold status of this seat. This method is typically used to mark a seat as sold
     * or unsold during ticket sales and refunds.
     *
     * @param sold {@code true} to mark the seat as sold, {@code false} to mark it as unsold
     */
    public void setSold(boolean sold) {
        isSold = sold;
    }

    /**
     * Returns the price of this seat.
     *
     * @return the cost of this seat
     */
    public double getPrice() {
        return price;
    }
}
