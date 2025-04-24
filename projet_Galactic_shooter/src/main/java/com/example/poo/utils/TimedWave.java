package com.example.poo.utils;

import com.example.poo.spaceEntity.CosmicCharger;
import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Player;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a wave of ennemi that will arrive to the scene after an set elapsed time or when no ennemi are alive
 */
public class TimedWave {
    private ArrayList<Enemy> awaitingWave;
    private Clock waitingTime;
    private double time;

    /**
     * Constructor of the TimedWave
     * @param time The time before the wave enter the scene
     */
    public TimedWave(double time)
    {
        this.awaitingWave = new ArrayList<>();
        this.time = time;
        this.waitingTime = new Clock(time*1000); // Initialize the clock with the specified time in milliseconds
    }

    /**
     * Gets the clock representing the time the wave will wait
     * @return A clock object setup for the time of the wave
     */
    public Clock getClock() {return this.waitingTime;}

    /**
     * Gets the time the wave will need to wait
     * @return A double representing the time in seconds
     */
    public double getTime() {return this.time;}

    /**
     * Adds an enemy to the wave
     * @param newEnemy The enemy to add
     */
    public void addEnemy(Enemy newEnemy)
    {
        this.awaitingWave.add(newEnemy);
    }

    /**
     * Checks if the timer is reached or if no enemy are alive
     * If so, adds all the wave to the waitingWave arraylist
     * @return A boolean that is true if the wave is added
     */
    public boolean needToEnter()
    {
        Iterator<Enemy> it = this.awaitingWave.iterator();
        Enemy current;

        if(this.getClock().isComplete() || (Global.waitingEntities.isEmpty() && Global.entities.size() == 1 && Global.entities.stream().anyMatch(e -> e instanceof Player))) {
            while(it.hasNext())
            {
                current = it.next();
                if(current instanceof CosmicCharger) {
                    ((CosmicCharger) current).initPlayerPosition();
                }
            }

            Global.waitingEntities.addAll(this.awaitingWave);
            Global.log.write("All ennemy of timed wave of " + String.valueOf(this.getTime()) + " added");
            return true;
        }
        return false;
    }
}