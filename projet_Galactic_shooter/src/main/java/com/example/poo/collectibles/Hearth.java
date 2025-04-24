package com.example.poo.collectibles;

import com.example.poo.spaceEntity.Entity;
import com.example.poo.spaceEntity.Player;
import com.example.poo.utils.Global;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class representing a hearth collectible entity in the game.
 * It extends the CollectibleEntity class and provides health points to the player when collected.
 */
public class Hearth extends CollectibleEntity{

    private int healthPoints;

    /**
     * Constructor for the Hearth class.
     * @param imagePath Path to the image file representing the entity.
     * @param emitter   The SpaceEntity who will drop the coin
     */
    public Hearth(String imagePath, Entity emitter) {
        super(imagePath, 5);
        this.healthPoints = 5 ;
        this.setEntityPos(emitter.getXPosition(), emitter.getYPosition());
        this.setEntitySize(32,32);
        this.setImgPaths((new ArrayList<>(Arrays.asList(
                imagePath,          // Here you put the first sprite of the animation
                "hearth_anim00.png",
                "hearth_anim01.png"
        ))));
        this.setSpriteAnimation();
    }

    /**
     * Overrides the superclass method to describe the effect of the collectible
     * Augment the score and the health of the player
     * @param player The player
     */
    @Override
    public void onCollect(Player player) {
        player.updateScore(this.getScore());
        player.increaseHealth(this.healthPoints);
        Global.log.write(this + " collected. New Player hp : " + player.getHealthPoints());
        this.setIsTouched(true);
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Hearth Collectibles";
    }
}
