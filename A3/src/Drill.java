import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;


public class Drill {
    private double x, y;
    private boolean gameOver = false;

    private double fuel = 20; // New attribute for fuel
    private double haul = 0;
    private double money = 0;


    private final ImageView imageView;
    private final List<Image> drillImages;
    private Direction currentDirection = Direction.RIGHT;
    private final Terrain terrain; // Add a reference to Terrain


    /**
     * Constructs a Drill object with initial images and terrain context.
     * Initializes the drill's starting position and sets the initial image.
     *
     * @param drillImages The list of images for different drill animations.
     * @param terrain The terrain in which the drill operates.
     */
    public Drill(List<Image> drillImages, Terrain terrain, double x, double y) {
        this.drillImages = drillImages;
        this.imageView = new ImageView(drillImages.get(54)); // Default image
        this.x = x; // Set initial x position
        this.y = y; // Set initial y position
        this.terrain = terrain; // Initialize the terrain
        this.imageView.setX(x);
        this.imageView.setY(y);
    }

    /**
     * Applies gravity to the drill, causing it to move downward automatically unless obstructed.
     */
    public void applyGravity() {
        int newY = (int) y + 50;  // Attempt to move down by one grid unit
        if (newY >= terrain.getHeight() - 50 || terrain.getSpaceTypeAt((int) x, newY) != Terrain.SpaceType.EMPTY) {
            return;  // Stop if out of bounds or blocked
        }

        y = newY;// Apply the downward movement
        updatePosition();  // Update the position of the drill image
    }

    /**
     * Decreases the fuel over time when the drill is idle, simulating fuel consumption.
     */
    public void idleFuel(){
        fuel -= 0.05;
    }

    /**
     * Moves the drill in a specified direction, updates its position, and manages collisions with the terrain.
     *
     * @param direction The direction in which to move the drill.
     */
    public void move(Direction direction) {
        if (fuel <= 0){
            return; // Stop if out of fuel
        }

        int newX = (int)x, newY = (int)y;

        // Set the current direction of the drill
        currentDirection = direction;


        // Update position based on direction
        switch (direction) {
            case UP:
                newY -= 50; // Calculate the new position
                if (newY < 0 || terrain.getSpaceTypeAt(newX, newY) == Terrain.SpaceType.BOULDER) {
                    return; // Prevent moving out of the terrain bounds
                }

                // Prevent drilling upward
                if (terrain.getSpaceTypeAt(newX, newY) != Terrain.SpaceType.EMPTY) {
                    return;
                }

                handleMove(newX, newY);

                y -= 50; // Move up
                fuel -= 1; // Less fuel consumption when moving without drilling
                break;
            case DOWN:
                newY += 50;
                if(terrain.getSpaceTypeAt(newX, newY) == Terrain.SpaceType.BOULDER) {return;}

                handleMove(newX, newY);

                y += 50;
                fuel -= 1;

                break;

            case LEFT:
                newX -= 50; // Calculate the new position to the left
                if(newX<0 || terrain.getSpaceTypeAt(newX, newY) == Terrain.SpaceType.BOULDER) {return;}

                handleMove(newX, newY);

                x -= 50; // Update x-coordinate
                fuel -= 1; // Consume fuel, possibly less than drilling down

                break;

            case RIGHT:
                newX += 50; // Calculate the new position to the right
                if(newX>750||terrain.getSpaceTypeAt(newX, newY) == Terrain.SpaceType.BOULDER) {return;}

                handleMove(newX, newY);

                x += 50; // Update x-coordinate
                fuel -= 1; // Consume fuel, similarly to moving left

                break;
            default:
                break;

        }

        updatePosition();
    }

    /**
     * Updates the drill's graphical representation on the screen, including changing images based on the current direction.
     */
    private void updatePosition() {
        // Update the imageView's position
        imageView.setX(x);
        imageView.setY(y);

        // Change the image based on the direction (here we just select the first image for demonstration)
        Image newImage;
        Random random = new Random();
        switch (currentDirection) {
            case UP:
                int up = random.nextInt(5);
                newImage = drillImages.get(22+up); // Replace with the index of the UP image
                break;
            case DOWN:
                int down = random.nextInt(4);
                newImage = drillImages.get(40+down); // Replace with the index of the DOWN image
                break;
            case LEFT:
                int left = random.nextInt(8);
                newImage = drillImages.get(left); // Replace with the index of the LEFT image
                break;
            case RIGHT:
                int right = random.nextInt(6);
                newImage = drillImages.get(54+right); // Replace with the index of the RIGHT image
                break;
            default:
                return;
        }
        imageView.setImage(newImage);
    }

    /**
     * Handles the movement logic when the drill is instructed to move, including collision detection and special actions
     * based on the type of terrain encountered.
     *
     * @param newX The new x-coordinate after the move.
     * @param newY The new y-coordinate after the move.
     */
    private void handleMove(int newX, int newY){
        Terrain.SpaceType spaceType = terrain.getSpaceTypeAt(newX, newY);

        if (spaceType == Terrain.SpaceType.LAVA) {
            handleGameOverRed();

            return;
        }
        if (spaceType == Terrain.SpaceType.SOIL) {
            terrain.removeImageViewAt(newX, newY);
        }
        if (spaceType == Terrain.SpaceType.VALUABLE) {
            handleCollection(newX, newY);
            terrain.removeImageViewAt(newX, newY);
        }
    }

    /**
     * Handles game over conditions when the drill encounters a lava space. This method triggers a visual change
     * to indicate the game has ended due to the drill contacting a lava obstacle. A red screen is displayed with a
     * "GAME OVER" message to inform the player of the end of the game under these specific conditions.
     * <p>
     * This method is executed on the JavaFX Application thread to ensure thread safety when updating the UI components.
     * It also sets the gameOver flag to true, effectively stopping further game actions.
     */
    public void handleGameOverRed() {
        // Run later on the JavaFX application thread
        Platform.runLater(() -> {
            // Create a new layout for the Game Over screen
            VBox gameOverScreen = new VBox(20);
            gameOverScreen.setAlignment(Pos.CENTER);
            gameOverScreen.setStyle("-fx-background-color: red; -fx-font-size: 40px;");

            // Create labels for the game over text and money collected
            Label gameOverLabel = new Label("GAME OVER");
            gameOverLabel.setTextFill(Color.WHITE);

            // Add labels to the layout
            gameOverScreen.getChildren().addAll(gameOverLabel);

            // Set the scene
            Scene gameOverScene = new Scene(gameOverScreen, 800, 600);
            Stage primaryStage = (Stage) imageView.getScene().getWindow(); // Get the primary stage from any node
            primaryStage.setScene(gameOverScene);
            primaryStage.show();

            setGameOver(true);
        });
    }




    /**
     * Manages the collection of valuables when the drill encounters them, adjusting the haul and money accordingly.
     *
     * @param newX The x-coordinate of the valuable.
     * @param newY The y-coordinate of the valuable.
     */
    private void handleCollection(int newX, int newY) {
        Valuable collected = terrain.getValuableAt(newX, newY);
        if (collected != null) {
            haul += collected.getWeight();
            money += collected.getWorth(); 

            // Remove the valuable from the terrain
            terrain.removeImageViewAt(newX, newY);
            terrain.clearValuableAt(newX, newY);
        }
    }

    /**
     * Returns the current fuel level of the drill.
     *
     * @return The current fuel amount.
     */
    public double getFuel() {
        return fuel;
    }

    /**
     * Sets the current fuel level for the drill. This is used primarily to update the drill's fuel during the game,
     * especially after fuel consumption or refueling actions.
     *
     * @param fuel The new amount of fuel to be set for the drill.
     */
    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    /**
     * Checks whether the game over condition has been met, typically after the drill encounters a fatal obstacle or runs
     * out of fuel.
     *
     * @return true if the game is over, otherwise false.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game over state of the drill. This method is used to control the end of the game when the drill meets
     * game over conditions.
     *
     * @param gameOver A boolean value representing whether the game should be set to over or not.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Retrieves the total weight of all valuables collected by the drill. This metric is important for game statistics
     * and can affect the gameplay, such as slowing down the drill if it carries too much haul.
     *
     * @return The total weight of the valuables collected by the drill.
     */
    public double getHaul() {
        return haul;
    }

    /**
     * Retrieves the total monetary value of all valuables collected by the drill. This value is accumulated as the drill
     * collects different types of valuables and is used to calculate the player's score or purchase upgrades.
     *
     * @return The total amount of money accumulated from collected valuables.
     */
    public double getMoney() {
        return money;
    }

    /**
     * Returns the ImageView associated with the drill. This method is used to retrieve the visual representation of the
     * drill, so it can be manipulated or queried in the GUI, such as changing its position or updating its image.
     *
     * @return The ImageView object that displays the drill.
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Enumerates the possible directions that the drill can move in the game. This includes moving up, down, left, or right.
     * This enumeration is used to specify movement directions in various methods throughout the Drill class.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

}
