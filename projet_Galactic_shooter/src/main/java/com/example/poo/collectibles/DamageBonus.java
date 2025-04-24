package com.example.poo.collectibles;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Player;
import com.example.poo.utils.Global;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class representing a damage bonus collectible entity in the game.
 * It extends the TemporaryBonus class and introduces properties specific to damage modification.
 */
public class DamageBonus extends TemporaryBonus {

    private int damageAdded;

    /**
     * Constructor for the DamageBonus class.
     * @param imagePath Path to the image file representing the damage bonus entity.
     * @param emitter    The Enemy object dropping the damage bonus.
     */
    public DamageBonus(String imagePath, Enemy emitter) {
        super(imagePath, 25, 15);
        this.damageAdded = 5;
        this.setEntityPos(emitter.getXPosition(), emitter.getYPosition());
        this.setEntitySize(50,50);
        this.setImgPaths((new ArrayList<>(Arrays.asList(
                imagePath,
                "DamageBonus.png"
        ))));
        this.setSpriteAnimation();
    }

    /**
     * Gets the damage modifier value provided by the damage bonus.
     * @return The damage modifier value.
     */
    public int getModifier() {return this.damageAdded;}

    /**
     * Overrides the onCollect method to handle the collection of the damage bonus by a player.
     * @param player The Player object who collected the damage bonus.
     */
    @Override
    public void onCollect(Player player)
    {
        this.setUser(player);
        if (!player.isInBonuses(this)) {
            Global.log.write(this + " collected.");
            this.getUser().addBonus(this);
            this.getUser().getWeapon().setBulletDamage(this.getUser().getWeapon().getBulletDamage() + this.damageAdded);
            this.getClock().startClock();
        } else {
            Global.log.write(this + " collected timer reinitialized");
            player.getAlreadyActiveBonus(this).getClock().startClock();
        }
        this.setIsTouched(true);
    }

    /**
     * Overrides the superclass method
     * Checks if the clock as ended and if so, reset the user's damage to what they were before
     * @return a boolean that is true if the bonus is supposed to be active again
     */
    @Override
    public boolean isActive()
    {
        if(!this.getClock().isComplete()) {
            return true;
        } else {
            this.getUser().getWeapon().setBulletDamage(this.getUser().getWeapon().getBulletDamage() - this.damageAdded);
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
        return "Damage Bonus";
    }

}
