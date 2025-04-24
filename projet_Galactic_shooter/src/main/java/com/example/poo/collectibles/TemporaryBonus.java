package com.example.poo.collectibles;

import com.example.poo.spaceEntity.Player;
import com.example.poo.utils.Clock;

/**
 * An abstract class representing a temporary bonus collectible entity in the game.
 * It extends the CollectibleEntity class and introduces properties related to time-limited effects.
 */
public abstract class TemporaryBonus extends CollectibleEntity {

    private double time;
    private Clock remainingTime;
    private Player user;

    /**
     * Constructor for the TemporaryBonus class.
     *
     * @param imagePath Path to the image file representing the temporary bonus entity.
     * @param value     Score given to the Player who collects the TemporaryBonus.
     * @param time      Time duration of the bonus effect in seconds.
     */
    public TemporaryBonus(String imagePath, int value, double time)
    {
        super(imagePath, value);
        this.time = time;
        remainingTime = new Clock((int)this.time*1000);
        this.user = null;
    }

    /**
     * Sets the player who collected the temporary bonus.
     * @param player The Player object who collected the bonus.
     */
    public void setUser(Player player) {this.user = player;}

    /**
     * Gets the player who collected the temporary bonus.
     * @return The Player object who collected the bonus.
     */
    public Player getUser() {return this.user;}

    /**
     * Removes the association with the player who collected the temporary bonus.
     */
    public void losePlayer() {this.user = null;}

    /**
     * Checks if the temporary bonus is currently active.
     * @return True if the bonus is active, false otherwise.
     */
    public boolean isActive() {return remainingTime.isComplete();}

    /**
     * Gets the clock object representing the remaining time for the bonus effect.
     * @return The remaining time of the clock object.
     */
    public Clock getClock() {return this.remainingTime;}

    /**
     * Overrides the equals method to compare TemporaryBonus objects based on their class names.
     * @param o The object to compare.
     * @return True if the class names are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o)
    {
        return this.getClass().getSimpleName().equals(o.getClass().getSimpleName());
    }
}