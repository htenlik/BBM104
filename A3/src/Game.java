import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class Game {
    private final Label fuelLabel = new Label();
    private final Label haulLabel = new Label();
    private final Label moneyLabel = new Label();
    private final Label fuelLabel2 = new Label();
    private final Label haulLabel2 = new Label();
    private final Label moneyLabel2 = new Label();

    private Drill drill, drill2;


    /**
     * Creates the start menu for the game. This menu provides options for single or multiplayer modes.
     *
     * @param primaryStage The primary stage on which the start menu scene will be set.
     */
    public void createStartMenu(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: #333;");

        Button onePlayerButton = new Button("Single Player");
        Button twoPlayerButton = new Button("Multiplayer");

        onePlayerButton.setOnAction(e -> startGame(primaryStage, 1));
        twoPlayerButton.setOnAction(e -> startGame(primaryStage, 2));

        menuLayout.getChildren().addAll(onePlayerButton, twoPlayerButton);

        Scene startMenuScene = new Scene(menuLayout, 800, 600);
        primaryStage.setScene(startMenuScene);
        primaryStage.setTitle("Select Game Mode");
        primaryStage.show();
    }

    /**
     * Updates the labels for a single drill with its current fuel, haul, and money statistics.
     *
     * @param drill The drill whose metrics are being updated.
     * @param fuelLabel The label displaying the fuel level of the drill.
     * @param haulLabel The label displaying the haul weight of the drill.
     * @param moneyLabel The label displaying the accumulated money of the drill.
     */
    private void updateDrillLabels(Drill drill, Label fuelLabel, Label haulLabel, Label moneyLabel) {
        fuelLabel.setText(String.format("fuel:%.2f", drill.getFuel()));
        haulLabel.setText(String.format("haul:%.2f", drill.getHaul()));
        moneyLabel.setText(String.format("money:%.2f", drill.getMoney()));
    }

    /**
     * Updates the labels on the HUD to reflect the current values of fuel, haul, and money for all active drills.
     * This provides real-time feedback to the player about their game status.
     *
     * @param numberOfPlayers The number of players in the game, which determines how many drills' data to update.
     */
    private void updateLabels(int numberOfPlayers) {
        updateDrillLabels(drill, fuelLabel, haulLabel, moneyLabel);
        if( numberOfPlayers > 1 && drill2 != null ) {
            updateDrillLabels(drill2, fuelLabel2, haulLabel2, moneyLabel2);}
    }

    /**
     * Starts the game with the specified number of players. This method sets up the game environment including terrain,
     * drills, and the HUD (Heads-Up Display) for displaying game metrics such as fuel, haul, and money.
     *
     * @param primaryStage The primary stage on which the game scene is set.
     * @param numberOfPlayers The number of players (1 or 2) that will participate in the game.
     */
    private void startGame(Stage primaryStage, int numberOfPlayers) {
        // Set the initial size of the window
        int windowWidth = 800;
        int windowHeight = 600;

        // Load images
        LoadImages loadImages = new LoadImages();

        loadImages.loadImages();

        List<Image> drillImages = loadImages.getDrillImages();
        List<Image> lavaImages = loadImages.getLavaImages();
        List<Image> obstacleImages = loadImages.getObstacleImages();
        List<Image> soilImages = loadImages.getSoilImages();
        List<Image> topImages = loadImages.getTopImages();
        List<Valuable> valuables = loadImages.getValuables();


        // Create sky and background
        double skyHeight = 100; // The height of the sky portion
        Rectangle sky = new Rectangle(0, 0, windowWidth, skyHeight);
        sky.setFill(Color.PALETURQUOISE);

        Rectangle background = new Rectangle(0, skyHeight, windowWidth, windowHeight-skyHeight);
        background.setFill(Color.PERU);
        Terrain terrain = new Terrain(lavaImages, obstacleImages, soilImages, topImages,valuables,windowWidth,windowHeight);
        Pane terrainPane = terrain.getTerrainPane(); // Get the terrainPane from Terrain

        // Set up the HUD
        HBox hud1 = new HBox(5);
        HBox hud2 = new HBox(5);
        HBox hud3 = new HBox(5);

        VBox hud = new VBox(5);

        hud1.getChildren().addAll(fuelLabel, fuelLabel2);
        hud2.getChildren().addAll(haulLabel, haulLabel2);
        hud3.getChildren().addAll(moneyLabel, moneyLabel2);

        hud.getChildren().addAll(hud1, hud2, hud3);


        Pane root = new Pane();


        // Set up the drill's ImageView
        if (numberOfPlayers > 1 ) {
            drill = new Drill(drillImages, terrain, 0, 0);
            drill2 = new Drill(drillImages, terrain, 100, 0);

            ImageView drillView = drill.getImageView();
            drillView.setX(0); // Set initial X position
            drillView.setY(0); // Set initial Y position just below the sky

            ImageView drillView2 = drill2.getImageView();
            drillView2.setX(100); // Set initial X position
            drillView2.setY(0); // Set initial Y position just below the sky

            root.getChildren().addAll(sky, background, terrainPane, drillView, drillView2, hud);

            setupDrillAnimations(drill, drill2, numberOfPlayers);
            setupDrillAnimations(drill2, drill ,numberOfPlayers);

            if(drill.getFuel() <= 0 && drill2.getFuel() <= 0){
                handleGameOverGreen(numberOfPlayers);
                handleGameOverGreen(numberOfPlayers);
            }

        }
        else{
            drill = new Drill(drillImages, terrain, 0, 0);

            ImageView drillView = drill.getImageView();
            drillView.setX(0); // Set initial X position
            drillView.setY(0); // Set initial Y position just below the sky

            root.getChildren().addAll(sky, background, terrainPane, drillView, hud);

            setupDrillAnimations(drill, null,numberOfPlayers);

            if(drill.getFuel() <= 0) {
                handleGameOverGreen(numberOfPlayers);
            }
        }


        // Set up the scene
        Scene scene = new Scene(root, windowWidth, windowHeight);
        setupKeyboardControls(scene,numberOfPlayers); // Set up controls


        // Set the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("HU-Load");
        primaryStage.show();

    }



    /**
     * Sets up keyboard controls for the game, enabling player(s) to control the drill's movements using arrow keys.
     * For a two-player game, additional controls are set for the second player using W, A, S, D keys.
     *
     * @param scene The scene to which keyboard controls are added.
     * @param numberOfPlayers The number of players, which determines how many drills need to be controlled.
     */
    private void setupKeyboardControls(Scene scene, int numberOfPlayers) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case UP: drill.move(Drill.Direction.UP); break;
                case DOWN: drill.move(Drill.Direction.DOWN); break;
                case LEFT: drill.move(Drill.Direction.LEFT); break;
                case RIGHT: drill.move(Drill.Direction.RIGHT); break;
                case W:     if (numberOfPlayers > 1 && drill2 != null) drill2.move(Drill.Direction.UP); break;
                case S:     if (numberOfPlayers > 1 && drill2 != null) drill2.move(Drill.Direction.DOWN); break;
                case A:     if (numberOfPlayers > 1 && drill2 != null) drill2.move(Drill.Direction.LEFT); break;
                case D:     if (numberOfPlayers > 1 && drill2 != null) drill2.move(Drill.Direction.RIGHT); break;
            }
            updateLabels(numberOfPlayers); // Update labels for both drills, might need to adjust method
        });
    }


    /**
     * Sets up the animation timers and gravity effects for a drill. This method controls the fuel consumption and gravity
     * application, and checks for game over conditions.
     *
     * @param drill The primary drill for which the animations are set up.
     * @param otherDrill The second drill in the game, which is also checked for game over conditions in multiplayer mode.
     * @param numberOfPlayers The number of players, used to determine when to display the game over screen.
     */
    private void setupDrillAnimations(Drill drill, Drill otherDrill, int numberOfPlayers) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drill.idleFuel();
                updateLabels(numberOfPlayers); // Update UI labels continuously
                if (drill.getFuel() <= 0) {
                    this.stop(); // Stop the timer if fuel is zero
                    drill.setFuel(0); // Set fuel to zero
                    if (numberOfPlayers == 1 || (otherDrill != null && otherDrill.getFuel() <= 0)) {
                        this.stop(); // Stop the timer if fuel is zero for both drills in multiplayer
                        handleGameOverGreen(numberOfPlayers);
                    }
                }
            }
        };
        timer.start();  // Start the continuous update loop

        Timeline gravityTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> drill.applyGravity()));
        gravityTimeline.setCycleCount(Timeline.INDEFINITE);
        gravityTimeline.play();
    }


    /**
     * Displays the game over screen with the collected money information. This method adjusts its behavior based on the number
     * of players in the game, showing either one or two players' money statistics.
     *
     * @param numberOfPlayers The number of players, which affects the display of the game over screen.
     */
    private void handleGameOverGreen(int numberOfPlayers) {
        if(drill.isGameOver()) return; // Prevent multiple game over screens

        Platform.runLater(() -> {
            VBox gameOverScreen = new VBox(20);
            gameOverScreen.setAlignment(Pos.CENTER);
            gameOverScreen.setStyle("-fx-background-color: green; -fx-font-size: 20px;");

            Label gameOverLabel = new Label("GAME OVER");
            gameOverLabel.setTextFill(Color.WHITE);
            gameOverScreen.getChildren().add(gameOverLabel);

            if (numberOfPlayers == 1) {
                Label moneyLabel = new Label("Collected Money: " + String.format("%.2f", drill.getMoney()));
                moneyLabel.setTextFill(Color.WHITE);
                gameOverScreen.getChildren().add(moneyLabel);

                Scene gameOverScene = new Scene(gameOverScreen, 800, 600);
                Stage primaryStage = (Stage) fuelLabel.getScene().getWindow(); // Get the primary stage from any node, here using fuelLabel as reference
                primaryStage.setScene(gameOverScene);
                primaryStage.show();
            } else {
                Label moneyLabel1 = new Label("Player 1 Money: " + String.format("%.2f", drill.getMoney()));
                Label moneyLabel2 = new Label("Player 2 Money: " + String.format("%.2f", drill2.getMoney()));
                moneyLabel1.setTextFill(Color.WHITE);
                moneyLabel2.setTextFill(Color.WHITE);
                gameOverScreen.getChildren().addAll(moneyLabel1, moneyLabel2);

                Scene gameOverScene = new Scene(gameOverScreen, 800, 600);
                Stage primaryStage = (Stage) fuelLabel.getScene().getWindow(); // Get the primary stage from any node, here using fuelLabel as reference
                primaryStage.setScene(gameOverScene);
                primaryStage.show();
            }
        });
    }


}
