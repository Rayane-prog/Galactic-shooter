package com.example.poo.weapons_bullets;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Player;
import com.example.poo.spaceEntity.SpaceEntity;

/**
 * The RocketWeapon class is a subclass of the abstract superclass Weapon
 * It represents a Weapon shooting a HomingBullet
 * It will create a RocketBullet when called from a SpaceEntity object
 */
public class RocketWeapon extends Weapon
{
    /**
     * Constructor for the Rocket Weapon class
     * @param user The SpaceEntity that use this object
     * @param clockTimestep The time between to call to the shoot method
     */
    public RocketWeapon(SpaceEntity user, double clockTimestep) {
        super(user, clockTimestep);
        this.setBulletDamage(5);
    }

    /**
     * Second Constructor for the BasicWeapon class
     * Used for random time step between each possible shot
     * @param user The SpaceEntity that use the Weapon Object
     */
    public RocketWeapon(SpaceEntity user) {
        super(user);
        this.setBulletDamage(5);
    }

    /**
     * Overrides the shoot method from the superclass to handle the shooting behavior.
     * Shoot an only RocketBullet in front of the entity
     */
    @Override
    public void shoot()
    {
        try {
            double mvSpeed = 1;
            if(this.getUser() instanceof Player) {mvSpeed = 2.5;}
            RocketBullet tempBullet = createBullet(mvSpeed);
            tempBullet.setRotationImageView(this.getUser().getRotation() + tempBullet.getAngle());
            this.addBullet(tempBullet);
        } catch (Exception e) {
            System.out.println("Error loading RocketBullet's image " + e.getMessage());
        }
    }

    /**
     * Method to create a Rocket Bullet with the right parameters
     * @param movementSpeed The Speed of the basic Bullet object
     * @return The Basic Bullet created from the parameters
     */
    private RocketBullet createBullet(double movementSpeed)
    {
        RocketBullet tempBullet = new RocketBullet("rocketBullet.png", this.getBulletDamage(), movementSpeed, this, 0, 300);
        tempBullet.setEntityPos(this.getUser().getXPosition(), this.getUser().getYPosition()-this.getUser().getFitEntityHeight());

        if(this.getUser() instanceof Enemy)
        {
            tempBullet.setEntityPos(this.getUser().getXPosition()-this.getUser().getEntityWidth() / 3.5, this.getUser().getYPosition() + this.getUser().getEntityHeight());
        }
        return tempBullet;
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Rocket Weapon hold by " + this.getUser();
    }
}
