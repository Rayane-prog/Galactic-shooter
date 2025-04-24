package com.example.poo.utils;

import com.example.poo.spaceEntity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The HitBox class represents a rectangular HitBox.
 * This HitBox can be used for collision detection by tracking its position and size.
 */
public class HitBox {
    private Rectangle hitbox;
    private Entity parentEntity;

    /**
     * Constructs a HitBox with the specified width, height, and initial position.
     * @param width      The width of the HitBox.
     * @param height     The height of the HitBox.
     * @param xPosition  The initial X position of the HitBox.
     * @param yPosition  The initial Y position of the HitBox.
     */
    public HitBox(Entity parentEntity,double width, double height, double xPosition, double yPosition) {

        this.hitbox = new Rectangle(width, height);
        this.hitbox.setFill(Color.BLUE);
        this.hitbox.setX(xPosition);
        this.hitbox.setY(yPosition);
        this.hitbox.setVisible(false);
        this.hitbox.setOpacity(0.5);

        this.parentEntity = parentEntity; // so we can access to the emitter of the HitBox with only the rectangle in the Group root
        this.hitbox.getProperties().put("parentEntity", parentEntity); // Adding a property to the rectangle
    }

    /**
     * Get the JavaFX Rectangle representing the HitBox.
     * @return The Rectangle representing the HitBox.
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Get the Parent using the HitBox.
     * @return The Entity Object representing the parentEntity.
     */
    public Entity getParentEntity() { return parentEntity;}

    /**
     * Update the position of the HitBox to the specified coordinates corresponding to its entity's position.
     * @param x The new X position of the HitBox.
     * @param y The new Y position of the HitBox.
     */
    public void updatePosition(double x, double y) {
        this.hitbox.setX(x);
        this.hitbox.setY(y);
    }

    /**
     * Update the size of the HitBox to the specified width and height corresponding to its entity's size.
     * @param w The new width of the HitBox.
     * @param h The new height of the HitBox.
     */
    public void updateSize(double w, double h) {
        this.hitbox.setWidth(w);
        this.hitbox.setHeight(h);
    }
}
