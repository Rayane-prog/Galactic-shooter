package com.example.poo.weapons_bullets;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Entity;
import com.example.poo.utils.Global;
import com.example.poo.spaceEntity.SpaceEntity;

/**
 * The HomingWeapon class is a subclass of the abstract superclass Weapon
 * It represents a Weapon shooting a HomingBullet
 * It will create a HomingBullet when called from a SpaceEntity object
 */
public class HomingWeapon extends Weapon
{
    /**
     * Constructor for the HomingWeapon class
     * @param user The SpaceEntity that use the Weapon Object
     * @param clockTimestep The time between to call to the shoot method
     */
    public HomingWeapon(SpaceEntity user, int clockTimestep)
    {
        super(user, clockTimestep);
        this.setBulletDamage(5);
    }

    /**
     * Second Constructor for the HomingWeapon class
     * Used for random time step between each possible shot
     * @param user The SpaceEntity that use the Weapon Object
     */
    public HomingWeapon(SpaceEntity user)
    {
        super(user);
        this.setBulletDamage(5);
    }

    /**
     * A method to get the object each individual bullet will follow.
     * Based on the entity that shoots it will either be the Player or the nearest enemy from the player
     * @return The SpaceEntity that the next bullet will try to reach
     */
    protected SpaceEntity getTarget()
    {
        if (this.getUser() instanceof Enemy)
            return Global.getPlayer();

        double minDist = -1;
        double currentDist;
        Entity nearestEnemy = null;

        //Get the distance between the player and each Enemy entities
        //Only saves the entity that is the closer
        for(Entity e : Global.entities) {
            if(!(e instanceof Enemy))
                continue;

            currentDist = Math.sqrt(Math.pow(this.getUser().getXPosition() - e.getXPosition(), 2) + Math.pow(this.getUser().getYPosition() - e.getYPosition(), 2));

            if (minDist == -1) {
                minDist = currentDist;
                nearestEnemy = e;
            } else if (currentDist < minDist) {
                minDist = currentDist;
                nearestEnemy = e;
            }
        }
        return (SpaceEntity) nearestEnemy;
    }

    /**
     * Overrides the shoot method from the superclass to handle the shooting behavior
     * Shoot an only HomingBullet in front of the entity
     */
    @Override
    public void shoot()
    {
        SpaceEntity target = this.getTarget();

        try {
            HomingBullet tempBullet = new HomingBullet("bulletRed.png", this.getBulletDamage(), 1, this, 0, target);
            tempBullet.setEntityPos(this.getUser().getXPosition(),this.getUser().getYPosition()-this.getUser().getFitEntityHeight());
            if(this.getUser() instanceof Enemy)
                tempBullet.setEntityPos(this.getUser().getXPosition()+this.getUser().getEntityWidth()/3.5,this.getUser().getYPosition()+this.getUser().getEntityHeight());
            tempBullet.setRotationImageView(this.getUser().getRotation() + tempBullet.getAngle()); //Set the rotation of the object to face the same direction as its emiter and add the specific offset of the object
            this.addBullet(tempBullet);
        } catch (Exception e) {
            System.out.println("Error loading BasicBullet's image " + e.getMessage());
            Global.log.write("Error loading BasicBullet's image " + e.getMessage());
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
        return "Homing Weapon hold by " + this.getUser();
    }
}
