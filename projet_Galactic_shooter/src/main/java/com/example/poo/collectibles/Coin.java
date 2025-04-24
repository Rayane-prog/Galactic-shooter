package com.example.poo.collectibles;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Player;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class to represents a coin that only grants a big score bonus and gain money for the shop
 */
public class Coin extends CollectibleEntity {
    /**
     * Constructor for the Coin class.
     *
     * @param imagePath Path to the image file representing the entity.
     * @param emitter The SpaceEntity that dropped the coin
     */
    public Coin(String imagePath, Enemy emitter) {
        super(imagePath,100);
        this.setEntityPos(emitter.getXPosition(), emitter.getYPosition());
        this.setEntitySize(32,32);
        this.setImgPaths((new ArrayList<>(Arrays.asList(
                imagePath,          // Here you put the first sprite of the animation
                "coin_anim01.png",
                "coin_anim02.png",
                "coin_anim03.png",
                "coin_anim04.png",
                "coin_anim05.png",
                "coin_anim06.png",
                "coin_anim07.png"
        ))));
        this.setSpriteAnimation();
    }

    /**
     * Overrides the superclass method to describe the effect of the collectible
     * Augment the score and add money
     * @param player The player
     */
    @Override
    public void onCollect(Player player) {
        player.updateScore(this.getScore());
        this.setIsTouched(true);
        player.updateCoinsCollected();
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Coin Collectibles";
    }
}
