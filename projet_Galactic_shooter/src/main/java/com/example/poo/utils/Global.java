package com.example.poo.utils;
import com.example.poo.spaceEntity.Entity;
import com.example.poo.spaceEntity.Player;
import javafx.stage.Screen;

import java.util.ArrayList;


/**
 * The Global class contains global data and constants used throughout the application.
 * It groups all data that needs to be accessed globally.
 */
public class Global {

    /**
     * The width of the game window.
     */
    final public static double widgetWidth = (int) Screen.getPrimary().getBounds().getWidth();

    /**
     * The height of the game window.
     */
    final public static double widgetHeight = (int) Screen.getPrimary().getBounds().getHeight();

    public static ArrayList<Entity> entities = new ArrayList<>();
    public static ArrayList<Entity> waitingEntities = new ArrayList<>();
    public static ArrayList<TimedWave> timedEntities = new ArrayList<>();
    public static int score = 0;

    public static int money = 0;

    public static WriteInFile log = new WriteInFile("Log.txt", false);

    /**
     * Return the entity player if it is in the ArrayList entities or null if it isn't
     * @return The SpaceEntity of the player or null
     */
    public static Player getPlayer()
    {
        for (Entity e : entities) {
            if (e instanceof Player)
                return (Player) e;
        }
        return null;
    }
}
