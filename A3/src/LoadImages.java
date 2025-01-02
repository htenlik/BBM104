import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Constructs a LoadImages object responsible for loading and storing all image resources needed for the game.
 */
public class LoadImages {

    private final List<Image> drillImages = new ArrayList<>();
    private final List<Image> lavaImages = new ArrayList<>();
    private final List<Image> obstacleImages = new ArrayList<>();
    private final List<Image> soilImages = new ArrayList<>();
    private final List<Image> topImages = new ArrayList<>();
    private final List<Image> valuableImages = new ArrayList<>();
    private final List<Valuable> valuables = new ArrayList<>();


    /**
     * Loads all image assets required for the game, including those for drills, obstacles, terrain layers, and valuables.
     * This is a primary method called to initiate all other loading methods.
     */
    public void loadImages() {
        loadDrillImages();
        loadLavaImages();
        loadObstacleImages();
        loadSoilImages();
        loadTopImages();
        loadValuableImages();
        initializeValuables();
    }

    /**
     * Loads image assets for the drill animation, preparing them for use in the game to visually represent the drill's
     * actions and states.
     */
    private void loadDrillImages() {
        for (int i = 1; i <= 61; i++) {
            String imagePath = "/assets/drill/drill_" + (i < 10 ? "0" : "") + i + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            drillImages.add(image);
        }
    }
    /**
     * Loads image assets representing various types of lava encountered in the game, enhancing the visual experience and
     * challenges within the game terrain.
     */
    private void loadLavaImages() {
        for (int i = 1; i <= 3; i++) {
            String imagePath = "/assets/underground/" +"lava_0"+ i + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            lavaImages.add(image); // Add to lavaImages list

        }
    }
    /**
     * Loads images for obstacles that the drill may encounter, adding gameplay complexity and requiring strategic
     * navigation skills from the player.
     */
    private void loadObstacleImages() {
        for (int i = 1; i <= 3; i++) {
            String imagePath = "/assets/underground/" +"obstacle_0"+ i + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            obstacleImages.add(image); // Add to obstacleImages list

        }
    }
    /**
     * Loads soil images to visually represent different layers of the underground environment, contributing to the
     * game's depth and realism.
     */
    private void loadSoilImages() {
        for (int i = 1; i <= 5; i++) {
            String imagePath = "/assets/underground/soil_0"+ i + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            soilImages.add(image);
        }
    }
    /**
     * Loads images for the top layer of the terrain, setting the visual foundation of the game's underground world.
     */
    private void loadTopImages() {
        for (int i = 1; i <= 2; i++) {
            String imagePath = "/assets/underground/top_0" + i + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            topImages.add(image);
        }
    }
    /**
     * Loads images for various valuable objects that the player can collect, each associated with specific rewards,
     * enhancing the game's reward system and player motivation.
     */
    private void loadValuableImages() {
        // List of names of the valuables without the file extension
        String[] valuableNames = {
                "amazonite", "bronzium", "diamond",
                "einsteinium", "emerald", "goldium",
                "ironium", "platinum", "ruby",
                "silverium"
        };

        // Loop through the names and load each image
        for (String name : valuableNames) {
            String imagePath = "assets/underground/valuable_" + name + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            valuableImages.add(image);
        }
    }
    /**
     * Initializes the in-game valuables with their respective images and values, setting up the economic aspect of the
     * game where players mine to collect these valuables for in-game currency.
     */
    private void initializeValuables() {
        valuables.add(new Valuable("Amazonite", 500000, 120, valuableImages.get(0)));
        valuables.add(new Valuable("Bronzium", 60, 10, valuableImages.get(1)));
        valuables.add(new Valuable("Diamond", 100000, 100, valuableImages.get(2)));
        valuables.add(new Valuable("Einsteinium", 2000, 40, valuableImages.get(3)));
        valuables.add(new Valuable("Emerald", 5000, 60, valuableImages.get(4)));
        valuables.add(new Valuable("Goldium", 250, 20, valuableImages.get(5)));
        valuables.add(new Valuable("Ironium", 30, 10, valuableImages.get(6)));
        valuables.add(new Valuable("Platinum", 750, 30, valuableImages.get(7)));
        valuables.add(new Valuable("Ruby", 20000, 80, valuableImages.get(8)));
        valuables.add(new Valuable("Silverium", 100, 10, valuableImages.get(9)));
    }


    /**
     * Returns the list of drill images.
     *
     * @return A list of images used for the drill animations.
     */
    public List<Image> getDrillImages() {
        return drillImages;
    }

    /**
     * Returns the list of lava images.
     *
     * @return A list of images representing different lava textures.
     */
    public List<Image> getLavaImages() {
        return lavaImages;
    }

    /**
     * Returns the list of obstacle images.
     *
     * @return A list of images representing different obstacles in the game.
     */
    public List<Image> getObstacleImages() {
        return obstacleImages;
    }

    /**
     * Returns the list of soil images.
     *
     * @return A list of images depicting various types of soil.
     */
    public List<Image> getSoilImages() {
        return soilImages;
    }

    /**
     * Returns the list of top layer images.
     *
     * @return A list of images for the top layer of the terrain.
     */
    public List<Image> getTopImages() {
        return topImages;
    }

    /**
     * Returns the list of valuables initialized with images and values.
     *
     * @return A list of Valuable objects representing different collectible items in the game.
     */
    public List<Valuable> getValuables() {
        return valuables;
    }
}
