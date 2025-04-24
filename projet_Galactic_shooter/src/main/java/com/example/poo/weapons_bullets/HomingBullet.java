package com.example.poo.weapons_bullets;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.utils.Global;
import com.example.poo.spaceEntity.SpaceEntity;
import javafx.scene.Group;

/**
 * The HomingBullet class is a subClass of the superClass BulletEntity.
 * It represents a Bullet that chase it assigned target to maximal set angle
 */
public class HomingBullet extends BulletEntity {

    private double maxAngle; // The maximum angle the bullet will take while following the player
    private double originAngle;
    private SpaceEntity target;

    /**
     * Constructor for the HomingBullet class
     * @param ImagePath Path to the image file representing the HomingBullet entity.
     * @param damage The quantity of damage received by the entity that it hits
     * @param movementSpeed Quantity of movement at each timestep.
     * @param emiter The entity that created the bullet.
     * @param shootAngle The angle that there is between the emiter and the direction the bullet face
     * @param target The SpaceEntity that this bullet will follow
     */
    public HomingBullet(String ImagePath, int damage, double movementSpeed, Weapon emiter, int shootAngle, SpaceEntity target)
    {
        super(ImagePath, damage, movementSpeed, emiter, shootAngle);
        this.target = target;
        this.maxAngle = 45; //This sets the maximum angle reachable
        this.originAngle = this.getRotation();
    }

    /**
     * Gets the target that is followed
     * @return The SpaceEntity that is followed
      */
    public SpaceEntity getTarget() {return this.target;}

    /**
     * Sets the maximum angle that the bullet will take in order to follow the target
     * @param angle A double with the angle
     */
    public void setMaxAngle(double angle) {this.maxAngle = angle;}

    /**
     * Private method that rotate the object to face its target, the angle is override by the maxAngle attribute if it exceed it
     * Use a triangle with A being this object, B the target and C the point with X = this object X coordinate and Y = the target Y coordinate
     * So C is a point right bellow this object and on the same height as the target
     * @param xDirection The X position of the target
     * @param yDirection The Y position of the target
     */
    protected void faceTarget(double xDirection, double yDirection)
    {
        double vecACx, vecACy, vecABx,vecABy, distAC, distAB, angle;
        vecACx = 0;   //C.xPosition - this.xPosition;
        vecACy = yDirection - this.getYPosition();
        vecABx = xDirection - this.getXPosition();
        vecABy = yDirection - this.getYPosition();
        distAC = Math.sqrt(Math.pow(0, 2) + Math.pow(this.getYPosition() - yDirection, 2));
        distAB = Math.sqrt(Math.pow(this.getXPosition() - xDirection, 2) + Math.pow(this.getYPosition() - yDirection, 2));
        angle = Math.toDegrees(Math.acos((vecABx*vecACx + vecABy*vecACy) / (distAB * distAC)));

        //Change the value in this line to make the bullet lose the Homing
        if(angle > this.maxAngle)
            angle = this.maxAngle;

        //If the target is on the right we add the angle to the current rotation
        //If it's on the left we subtract the angle
        if (this.getEmiter().getUser() instanceof Enemy) {
            if (this.getXPosition() < xDirection)
                this.setRotationImageView(this.getRotation() - angle);
            else
                this.setRotationImageView(this.getRotation() + angle);
        } else {
            if (this.getXPosition() < xDirection)
                this.setRotationImageView(this.getRotation() + angle);
            else
                this.setRotationImageView(this.getRotation() - angle);
        }

    }

    /**
     * Method called each frame from the Weapon object.
     * It calculates the new rotation of the object before making it move
     * @param root The Group Object used to display objects.
     */
    @Override
    public void evolve(Group root)
    {
        //Reset the rotation to the originalAngle so the Bullet don't spin in circle constantly
        this.setRotationImageView(this.originAngle);
        if(this.getTarget() != null)
            faceTarget(this.target.getXPosition(), this.target.getYPosition());

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
        return "Homing Bullet from " + this.getEmiter() + " aiming to " + this.getTarget();
    }
}