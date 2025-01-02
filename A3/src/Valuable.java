import javafx.scene.image.Image;

/**
 * The type Valuable.
 */
public class Valuable {
    private final String name;
    private final int worth;
    private final int weight;
    private final Image image;

    /**
     * Instantiates a new Valuable.
     *
     * @param name   the name
     * @param worth  the worth
     * @param weight the weight
     * @param image  the image
     */
    public Valuable(String name, int worth, int weight, Image image) {
        this.name = name;
        this.worth = worth;
        this.weight = weight;
        this.image = image;
    }


// Getters for each property
    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets worth.
     *
     * @return the worth
     */
    public int getWorth() {
        return worth;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }
}
