package com.example.poo;
import com.example.poo.utils.Global;
import com.example.poo.utils.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The SpaceInvaderGame class represents the main class of the Space Invader game.
 * It extends the JavaFX Application class.
 */
public class SpaceInvaderGame extends Application {


    /**
     * The start method is called when the JavaFX application is launched.
     * It initializes and sets up the game elements, such as the player, enemy, and scene.
     * @param stage The primary stage for the application.
     */
    @Override
    public void start(Stage stage)
    {
        ViewManager manager;
        // Scene initialization
        manager = new ViewManager();
        manager.startGameScene();
    }


    /**
     * The main method is the entry point of the application.
     * It launches the JavaFX application.
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args)
    {
        launch();
        Global.log.closeFile();

    }
}
