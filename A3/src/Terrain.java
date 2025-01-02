import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Random;

public class Terrain {
    private final SpaceType[][] grid;
    private final Valuable[][] valuableGrid; // Additional grid to hold valuables


    private final List<Image> lavaImages;
    private final List<Image> obstacleImages;
    private final List<Image> soilImages;
    private final List<Image> topImages;
    private final List<Valuable> valuables;
    private final Pane terrainPane = new Pane(); // Use a Pane to hold the images


    /**
     * Constructs a Terrain object with specific image resources and valuables. Initializes the grid
     * for game elements and sets up the terrain layout.
     *
     * @param lavas List of images representing lava.
     * @param obstacles List of images representing obstacles.
     * @param soils List of images representing soil.
     * @param tops List of images representing the top surface of the terrain.
     * @param valuables List of valuable objects in the terrain.
     * @param width The width of the terrain.
     * @param height The height of the terrain.
     */
    public Terrain(List<Image> lavas, List<Image> obstacles, List<Image> soils, List<Image> tops, List<Valuable> valuables,int width, int height) {
        this.lavaImages = lavas;
        this.obstacleImages = obstacles;
        this.soilImages = soils;
        this.topImages = tops;
        this.valuables = valuables;
        double x = 0; // Set initial x position
        double y = 100; // Set initial y position
        this.terrainPane.setLayoutX(x); // Set the layout X position
        this.terrainPane.setLayoutY(y); // Set the layout Y position
        grid = new SpaceType[height/50][width/50];
        valuableGrid = new Valuable[height/50][width/50];

        initializeTerrain();
      }

    /**
     * Initializes the top layer, inner layer, and obstacles in the terrain, setting up the visual and interactive elements.
     */
    private void initializeTerrain() {
        setTops();
        setInside();
        setObstacles();
    }

    /**
     * Sets up the top-most visible layer of the terrain with images, ensuring these elements do not contain interactable objects.
     */
    private void setTops(){
        Random random = new Random();
        for (int i = 0; i < 800; i += (int) topImages.get(0).getWidth()) {
            int a = random.nextInt(2);
            ImageView newImage = new ImageView();
            newImage.setX(i);
            newImage.setY(0);
            newImage.setImage(topImages.get(a)); // Use obstacleImages.get(0) instead of obstacleImages.get(1)
            terrainPane.getChildren().add(newImage);
            grid[0][i/50] = SpaceType.EMPTY;
            grid[1][i/50] = SpaceType.EMPTY;
            // these for sky
            grid[2][i/50] = SpaceType.SOIL;
        }
    }

    /**
     * Populates the inner layers of the terrain with soil, valuables, and lava, creating a randomized game field.
     */
    private void setInside() {
        Random random = new Random();

        for (int i = 50; i < 750; i += (int) topImages.get(0).getWidth()) {
            for (int j = 50; j < 450; j += (int) topImages.get(0).getWidth()) {
                int chance = random.nextInt(100);

                ImageView newImage = new ImageView();
                newImage.setX(i);
                newImage.setY(j);

                if (chance < 87) {
                    int a = random.nextInt(5);
                    newImage.setImage(soilImages.get(a));
                    grid[j/50+2][i/50] = SpaceType.SOIL;

                }
                else if (chance < 93) {
                    int index = random.nextInt(valuables.size());
                    Valuable valuable = valuables.get(index);
                    newImage.setImage(valuable.getImage());
                    grid[j/50+2][i/50] = SpaceType.VALUABLE;
                    valuableGrid[j/50+2][i/50] = valuable; // Store the valuable reference
                } else {
                    int a = random.nextInt(3);
                    newImage.setImage(lavaImages.get(a));
                    grid[j/50+2][i/50] =  SpaceType.LAVA;

                }
                terrainPane.getChildren().add(newImage);

            }

        }
    }

    /**
     * Sets up the obstacles in the terrain, creating a challenging environment for the player to navigate.
     */
    private void setObstacles() {
        Random random = new Random();

        for (int i = 50; i < 450; i += (int) obstacleImages.get(0).getHeight()) {
            int a = random.nextInt(3);
            ImageView newImage = new ImageView();
            newImage.setX(0);
            newImage.setY(i);
            newImage.setImage(obstacleImages.get(a)); // Use obstacleImages.get(0) instead of obstacleImages.get(1)
            terrainPane.getChildren().add(newImage);

        }
        for (int x = 3; x < 12; x++) {
                grid[x][0] = SpaceType.BOULDER; // Set the grid cell to BOULDER
        }

        for (int i = 50; i < 450; i += (int) obstacleImages.get(0).getHeight()) {
            int a = random.nextInt(3);
            ImageView newImage = new ImageView();
            newImage.setX(750);
            newImage.setY(i);
            newImage.setImage(obstacleImages.get(a)); // Use obstacleImages.get(0) instead of obstacleImages.get(1)
            terrainPane.getChildren().add(newImage);
        }
        for (int x = 0; x < 16; x++) {
                grid[11][x] = SpaceType.BOULDER; // Set the grid cell to BOULDER

        }

        for (int j = 0; j < 800; j += (int) obstacleImages.get(0).getHeight()) {
            int a = random.nextInt(3);
            ImageView newImage = new ImageView();
            newImage.setX(j);
            newImage.setY(450);
            newImage.setImage(obstacleImages.get(a)); // Use obstacleImages.get(0) instead of obstacleImages.get(1)
            terrainPane.getChildren().add(newImage);
        }
        for (int x = 3; x < 12; x++) {
                grid[x][15] = SpaceType.BOULDER; // Set the grid cell to BOULDER

        }

    }

    /**
     * Returns the pane containing all the visual elements of the terrain, allowing it to be added to the game scene.
     *
     * @return Pane with the terrain images.
     */
    public Pane getTerrainPane() {
        return terrainPane;
    }

    /**
     * Retrieves the type of space located at a specific coordinate, important for game mechanics like movement and collision.
     *
     * @param x The x-coordinate of the space.
     * @param y The y-coordinate of the space.
     * @return The type of space at the given coordinates.
     */
    public SpaceType getSpaceTypeAt(int x, int y) {
        return grid[y/50][x/50];
    }

    /**
     * Removes an image from the terrain pane at specified grid coordinates, usually triggered by game actions like drilling.
     *
     * @param x The x-coordinate where the image will be removed.
     * @param y The y-coordinate where the image will be removed.
     */
    public void removeImageViewAt(int x, int y) {
        // Calculate the cell index based on the coordinates
        int gridX = x / 50;
        int gridY = y / 50;

        // Find the existing ImageView at these grid coordinates
        for (Node node : terrainPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView img = (ImageView) node;
                // Check if the ImageView is at the specified grid location
                if (img.getX() == x && img.getY() == y - 100) {
                    terrainPane.getChildren().remove(img);
                    break;
                }
            }
        }


        // Update the grid state to EMPTY
        grid[gridY][gridX] = SpaceType.EMPTY;
    }

    /**
     * Retrieves a Valuable object located at a specific coordinate, crucial for managing game inventory and scoring.
     *
     * @param x The x-coordinate of the valuable.
     * @param y The y-coordinate of the valuable.
     * @return The Valuable object at the given coordinates, if any.
     */
    public Valuable getValuableAt(int x, int y) {
        return valuableGrid[y/50][x/50]; // Return the valuable at the given location
    }

    /**
     * Clears a reference to a valuable at a given grid location, typically used when a valuable has been collected.
     *
     * @param x The x-coordinate of the cleared valuable.
     * @param y The y-coordinate of the cleared valuable.
     */
    public void clearValuableAt(int x, int y) {
        valuableGrid[y/50][x/50] = null; // Clear the valuable reference
    }

    /**
     * Returns the total height of the terrain, useful for boundary checks and positioning elements within the game world.
     *
     * @return The height of the terrain in pixels.
     */
    public int getHeight() {
        return 50*grid.length;
    }

    /**
     * Enum defining different types of spaces within the terrain, each representing different interactions and behaviors in the game.
     */
    public enum SpaceType {
        EMPTY,
        SOIL,
        VALUABLE,
        BOULDER,
        LAVA
    }

}
