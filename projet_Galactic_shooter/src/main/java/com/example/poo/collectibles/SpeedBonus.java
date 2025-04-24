package com.example.poo.collectibles;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Player;
import com.example.poo.utils.Global;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class representing a speed bonus collectible entity in the game.
 * It extends the TemporaryBonus class and provides a temporary speed boost to the player.
 */
public class SpeedBonus extends TemporaryBonus {

    private double speedMultiplier;

    /**
     * Constructor for the SpeedBonus class.
     * @param imagePath Path to the image file representing the speed bonus entity.
     * @param emitter   The Enemy that will drop the speed bonus.
     */
    public SpeedBonus(String imagePath, Enemy emitter)
    {
        // default values : gives 10 score, a x2 speed boost that is during for 15 seconds
        super(imagePath, 25, 15);
        this.speedMultiplier = 2;
        this.setEntityPos(emitter.getXPosition(), emitter.getYPosition());
        this.setEntitySize(40,40);
        this.setImgPaths((new ArrayList<>(Arrays.asList(
                imagePath,
                "SpeedBonus.png"
        ))));
        this.setSpriteAnimation();
    }

    /**
     * Gets the speed multiplier of the speed bonus.
     * @return The speed multiplier.
     */
    public double getModifier() {return this.speedMultiplier;}

    /**
     * Overrides the onCollect method to handle the collection of the speed bonus by a player.
     * @param player The Player object who collected the speed bonus.
     */
    @Override
    public void onCollect(Player player)
    {
        this.setUser(player);
        if (!this.getUser().isInBonuses(this))
        {
            Global.log.write(this + " collected");
            this.getUser().addBonus(this);
            this.getUser().setMovementSpeed(this.getUser().getMovementSpeed() * this.getModifier());
            this.getClock().startClock();
        } else {
            Global.log.write(this + " timer reinitialized");
            this.getUser().getAlreadyActiveBonus(this).getClock().startClock();
        }
        // We let this here to reinitialize the timer or initialize it.
        this.setIsTouched(true);
    }

    /**
     * Overrides the superclass method
     * Checks if the clock as ended and if so, reset the user's speed to what they were before
     * @return A boolean that is true if the bonus is supposed to be active again
     */
    @Override
    public boolean isActive()
    {
        if(!this.getClock().isComplete()) {
            return true;
        } else {
            this.getUser().setMovementSpeed(this.getUser().getMovementSpeed() / this.speedMultiplier);
            return false;
        }
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Speed Bonus";
    }
}
