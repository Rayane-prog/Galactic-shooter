package com.example.poo.collectibles;

import com.example.poo.spaceEntity.Entity;
import com.example.poo.utils.Global;
import com.example.poo.spaceEntity.Player;
import javafx.scene.Group;

/**
 * An abstract class representing a collectible entity in the game.
 */
public abstract class CollectibleEntity extends Entity {

    private final int scoreValue ;
    /**
     * Constructor for the CollectibleEntity class.
     * @param imagePath Path to the image file representing the entity.
     * @param value     Score given to the Player who collect the CollectibleEntity
     */
    public CollectibleEntity(String imagePath, int value) {
        super(imagePath);
        this.setMovementSpeed(2);
        this.setMoveMultiplier(1);
        this.scoreValue = value;
    }

    /**
     * Moves the collectible entity down.
     */
    @Override
    public void move() {
        this.moveEntityDown();
    }

    /**
     * Override the superclass display method.
     * Used to display the collectible on screen
     * @param root The JavaFX Group representing the root of the scene.
     */
    @Override
    public void display(Group root) {
        root.getChildren().remove(this.displayEntity());
        root.getChildren().add(this.displayEntity());
        root.getChildren().remove(this.displayHitBox());
        root.getChildren().add(this.displayHitBox());
    }

    /**
     * Overrides the evolve method from the superclass. Contains every action of the enemy at each frame
     * @param root Group Object used the display the object
     */
    @Override
    public void evolve(Group root)
    {
        this.display(root);
        if (this.getSpriteAnimation() != null) {
            this.getSpriteAnimation().SpriteAnimationPlay(root);
        }

        this.move();
        // We delete the collectible once it exceeds the boundaries.
        if(this.getYPosition() > Global.widgetHeight)
        {
            this.setIsDead(true);
        }
    }

    /**
     * Abstract method that will be called when the player collect the entity
     * @param player The player
     */
    public abstract void onCollect(Player player);

    /**
     * Gets the score bonus that this collectible grants to the player
     * @return A int with the value
     */
    public int getScore() { return this.scoreValue;}

}
