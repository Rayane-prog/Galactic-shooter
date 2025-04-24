package com.example.poo.collectibles;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Player;
import com.example.poo.utils.Global;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class representing a movement bonus collectible entity in the game.
 * It extends the TemporaryBonus class and provides a temporary boost to the player's movement.
 */
public class MovementBonus extends TemporaryBonus {

    private boolean timerEnded;

    /**
     * Constructor for the MovementBonus class.
     * @param imagePath Path to the image file representing the movement bonus entity.
     * @param emitter   The Enemy that will drop the movement bonus.
     */
    public MovementBonus(String imagePath, Enemy emitter)
    {
        super(imagePath, 100, 5);
        this.setEntityPos(emitter.getXPosition(), emitter.getYPosition());
        this.setEntitySize(40, 40);
        //PlaceHolder
        this.setImgPaths((new ArrayList<>(Arrays.asList(
                imagePath,
                "MovementBonus.png"
        ))));
        this.setSpriteAnimation();
        this.timerEnded = false;
    }

    /**
     * Sets the timerEnded status of the movement bonus.
     * @param value The boolean value to set for the timerEnded status.
     */
    public void setTimerEndedStatus(boolean value) {this.timerEnded = value;}

    /**
     * Overrides the onCollect method to handle the collection of the damage bonus by a player.
     * @param player The Player object who collected the damage bonus.
     */
    @Override
    public void onCollect(Player player)
    {
        this.setUser(player);
        if(!player.isInBonuses(this))
        {
            Global.log.write(this + " collected");
            this.getUser().addBonus(this);
            this.getUser().setMoveUpPossibility(true);
            this.getUser().setMoveDownPossibility(true);
            this.getClock().startClock();
        } else {
            Global.log.write(this + " timer reinitialized");
            MovementBonus b = (MovementBonus)player.getAlreadyActiveBonus(this);
            b.getUser().setMoveUpPossibility(true);
            b.getUser().setMoveDownPossibility(true);
            b.setTimerEndedStatus(false);
            b.getClock().startClock();
        }
        this.setIsTouched(true);
    }

    /**
     * Overrides the superclass method
     * Checks if the clock as ended and if so, reset the move possibility based on were the user is
     * @return A boolean that is true if the bonus is supposed to be active again
     */
    @Override
    public boolean isActive()
    {
        if(!(this.getClock().isComplete() || this.timerEnded)) {
            return true;
        } else {
            this.timerEnded = true;
            if(this.getUser().getYPosition() >= Global.widgetHeight - 100 - this.getEntityHeight())
                this.getUser().setMoveDownPossibility(false);
            if(this.getUser().getYPosition() <= Global.widgetHeight - 100 - this.getEntityHeight())
                this.getUser().setMoveUpPossibility(false);
            return (this.getUser().getYMovePossibility());
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
        return "Movement bonus";
    }
}
