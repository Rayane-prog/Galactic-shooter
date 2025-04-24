package com.example.poo.weapons_bullets;

import com.example.poo.spaceEntity.SpaceEntity;
import com.example.poo.utils.Global;
import javafx.scene.Group;

/**
 * The AimBullet class is a subClass of the superClass BulletEntity.
 * It represents a Bullet goes in a straight line to the last coordinates of the player
 */
public class AimBullet extends HomingBullet{
    private boolean isNotFound = true ;

    /**
     * Constructor for the AimBullet class
     * @param ImagePath Path to the image file representing the HomingBullet entity.
     * @param damage The quantity of damage received by the entity that it hits
     * @param movementSpeed Quantity of movement at each timestep.
     * @param emiter The entity that created the bullet.
     * @param shootAngle The angle that there is between the emiter and the direction the bullet face
     * @param target The SpaceEntity that this bullet will follow
     */
    public AimBullet(String ImagePath, int damage, double movementSpeed, Weapon emiter, int shootAngle, SpaceEntity target) {
        super(ImagePath, damage, movementSpeed, emiter, shootAngle, target);
        this.setMaxAngle(180);
    }

    /**
     * Method called each frame from the Weapon object.
     * @param root The Group Object used to display objects.
     */
    @Override
    public void evolve(Group root)
    {
        //Reset the rotation to the originalAngle so the Bullet don't spin in circle constantly
        if (isNotFound) {
            faceTarget(this.getTarget().getXPosition(), this.getTarget().getYPosition());
            this.setRotationImageView(this.getRotation());
            isNotFound = false;
        }

        this.move();
        if (this.getXPosition() + this.getFitEntityWidth() > 2* Global.widgetWidth
                || this.getXPosition() + this.getFitEntityWidth() < 0
                || this.getYPosition() + this.getFitEntityHeight() > Global.widgetHeight
                || this.getYPosition() + this.getFitEntityHeight() < 0)
            this.setToDelete();
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Aim bullet from " + this.getEmiter();
    }
}
