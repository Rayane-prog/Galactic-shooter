package com.example.poo.collectibles;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Player;
import com.example.poo.utils.Global;
import com.example.poo.weapons_bullets.*;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class representing a weapon bonus collectible entity in the game.
 * It extends the TemporaryBonus class and provides a temporary bonus weapon to the player.
 */
public class WeaponBonus extends TemporaryBonus {

    Weapon bonusWeapon;

    Group root;
    /**
     * Constructor for the WeaponBonus class
     * @param imagePath The sprite of the bonus
     * @param given     The weapon given if the bonus is acquired
     * @param emitter   The enemy entity that drop this on death
     */
    public WeaponBonus (String imagePath, Weapon given, Enemy emitter, Group root) {
        super(imagePath, 50, 5);
        this.root = root;
        this.bonusWeapon = given;
        this.setEntityPos(emitter.getXPosition(), emitter.getYPosition());
        this.setEntitySize(50, 50);

        //Change the list of image based on the type of weapon
        //ImagePath will be hardcoded for each type, just like in the basic weapon
        if(given instanceof  BasicWeapon) {
            this.setImgPaths((new ArrayList<>(Arrays.asList(
                    "basicWeapon.png",
                    "basicWeapon2.png"
            ))));
        } else if(given instanceof HomingWeapon) {
            this.setImgPaths((new ArrayList<>(Arrays.asList(
                    imagePath,
                    "HomingBonus.png"
            ))));
        } else if (given instanceof RocketWeapon) {
            this.setImgPaths((new ArrayList<>(Arrays.asList(
                    imagePath,
                    "RocketBonus.png"
            ))));
        }

        this.setSpriteAnimation();
    }

    /**
     * Gets the bonus weapon associated with this WeaponBonus.
     * @return The bonus weapon.
     */
    public Weapon getBonusWeapon() {return this.bonusWeapon;}

    /**
     * Overrides the onCollect method to handle the collection of the weapon bonus by a player.
     * Based on what is in the bonus, switch the weapon of tht player
     * @param player The Player object who collected the damage bonus.
     */
    @Override
    public void onCollect(Player player)
    {
        this.setUser(player);
        if(!this.getUser().isInBonuses(this)) {
            Weapon beforeCollectWeapon = this.getUser().getWeapon();
            this.getUser().addPastWeapon();
            player.addWeapon(this.getBonusWeapon());
            beforeCollectWeapon.addUser(player);
            this.getUser().addBonus(this);
            this.getClock().startClock();
            Global.log.write("Player acquired new weapon : " + this);
        } else {
            Global.log.write(this + " timer reinitialized");
            this.getUser().getAlreadyActiveBonus(this).getClock().startClock();
        }
        this.setIsTouched(true);
    }

    /**
     * Overrides the superclass method
     * Checks if the clock as ended and if so, reset the weapon of the user and store the bonus weapon to let it evolve
     * @return A boolean that is true if the bonus is supposed to be active again
     */
    @Override
    public boolean isActive()
    {
        if(!this.getClock().isComplete()) {
            return true;
        } else {
            Global.log.write(this + " is over.");
            Weapon deactivateWeapon = this.getBonusWeapon();
            this.getUser().addPastWeapon();
            this.getUser().addWeapon(new BasicWeapon(this.getUser(), 300));
            deactivateWeapon.addUser(Global.getPlayer());
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
        String result = "No type WeaponBonus";

        if(this.bonusWeapon instanceof BasicWeapon) {
            result = "BasicWeapon Bonus";
        }
        if(this.bonusWeapon instanceof HomingWeapon) {
            result = "HomingWeapon Bonus";
        }
        if(this.bonusWeapon instanceof RocketWeapon) {
            result = "RocketWeapon Bonus";
        }

        return result;
    }
}
