import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Initializes and starts the game application. This method sets up the primary stage and displays the start menu.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        game.createStartMenu(primaryStage);
    }


    /**
     * The main entry point for all JavaFX applications. This method launches the JavaFX application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
