package com.example.poo.weapons_bullets;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Entity;
import com.example.poo.utils.Global;
import com.example.poo.spaceEntity.SpaceEntity;
import javafx.scene.Group;

/**
 * The RocketBullet class is a subClass of the superClass BulletEntity.
 * It represents a Bullet that goes straight from its spawn point and gives damage to all enemy in an area
 */
public class RocketBullet extends BulletEntity {

    private double range; //The area of effect the rocket has on impact

    /**
     * Constructor for the RocketBullet class
     * @param ImagePath Path to the image file representing the HomingBullet entity.
     * @param damage The quantity of damage received by the entity that it hits
     * @param movementSpeed Quantity of movement at each timestep.
     * @param emiter The entity that created the bullet.
     * @param shootAngle The angle that there is between the emiter and the direction the bullet face
     * @param range The range in which the rocket inflict damage
     */
    public RocketBullet(String ImagePath, int damage, double movementSpeed, Weapon emiter, int shootAngle, double range)
    {
        super(ImagePath, damage, movementSpeed, emiter, shootAngle);
        this.range = range;
    }

    /**
     * Checks if the enemy passed in parameters is in the range of the explosion
     * @param ent The enemy to check
     * @return A boolean that is true if the enemy is in the range
     */
    private boolean isInRange(Enemy ent)
    {
        double dist = Math.sqrt(Math.pow(this.getXPosition() - ent.getXPosition(), 2) + Math.pow(this.getYPosition() - ent.getYPosition(), 2));
        return (dist <= range);
    }

    /**
     * Overrides the hitEffect method from the superclass.(to be implemented)
     * Defines the effect to display on collision and what to do to the collide entity
     */
    @Override
    public void hitEffect(SpaceEntity e, Group root)
    {
        e.decreaseHealth(this.getDamage());
        if(e.hasDeadStatus()) { e.deathEffect(root); }

        for (Entity ent : Global.entities) {
            if (!(ent instanceof Enemy) || (SpaceEntity) ent == e)
                continue;

            if (this.isInRange((Enemy) ent)) {
                ((Enemy) ent).decreaseHealth(this.getDamage());
                if(ent.hasDeadStatus()) {
                    ((Enemy) ent).deathEffect(root);
                }
            }
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
        return "Rocket Bullet from " + this.getEmiter();
    }
}
