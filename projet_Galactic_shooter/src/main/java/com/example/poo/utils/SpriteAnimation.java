package com.example.poo.utils;


import com.example.poo.spaceEntity.Entity;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * A class that handle all the multiple sprite animation of the game
 */
public class SpriteAnimation {
    private ArrayList<String> imgPaths;
    private int totalImages;
    private int currentImageIndex;
    private long lastTime = 0;
    private long animationInterval = 100_000_000; // 100 milliseconds
    private Entity entity;
    private double sizeHeight;
    private double sizeWidth;

    /**
     * Constructor for the Sprite Animation class
     * @param entity The entity that has an animation
     * @param imgPaths The list of sprites
     * @param totalImages The number of images needed for the animation
     */
    public SpriteAnimation(Entity entity,ArrayList<String> imgPaths, int totalImages) {
        this.imgPaths = imgPaths;
        this.totalImages = totalImages;
        this.currentImageIndex = 0;
        this.entity = entity;
        this.sizeHeight = entity.getFitEntityHeight();
        this.sizeWidth = entity.getFitEntityWidth();
    }

    /**
     * this constructor is used for entities that has no animated sprite but that can still modify their image
     * @param entity The entity that will modify its sprite
     */
    public SpriteAnimation(Entity entity) {
        this.imgPaths = new ArrayList<>();
        this.totalImages = 1;
        this.currentImageIndex = 0;
        this.entity = entity;
    }

    /**
     * Play the animation
     * @param root The JavaFX Group representing the root of the scene.
     */
    public void SpriteAnimationPlay( Group root) {
        int frameCounter = 0;
        // Here we delete the old image and it HitBox since we won't be able to access to it when we will use the display method, so we manually delete them here
        root.getChildren().remove(this.entity.displayHitBox());
        root.getChildren().remove(this.entity.displayEntity());

        // Check if it's time to change the image
        if (System.nanoTime() - lastTime >= animationInterval) {
            // Update the image, the currentImageIndex % totalImages make the sprite loop endlessly
            this.entity.setNewImageView(new ImageView(imgPaths.get(currentImageIndex % totalImages)));
            this.entity.setEntitySize(this.sizeHeight,this.sizeWidth);


            // Reset the frame counter
            // Update the last time
            lastTime = System.nanoTime();

            currentImageIndex++;
        }
        frameCounter++;
    }

    /**
     * The animation that is played when the entity takes a damage
     */
    public void takeDamageAnimation()
    {
        // Sprite effect : the sprite become white for 0.2 seconds, representing the damage being taken.
        ColorAdjust newColor = new ColorAdjust();
        newColor.setBrightness(1.0);
        entity.displayEntity().setEffect(newColor);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(event -> entity.displayEntity().setEffect(null)); // Removing the white color by applying a null effect on the ImageView
        pause.play();
    }

}
