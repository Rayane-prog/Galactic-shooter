package com.example.poo.weapons_bullets;

/**
 * The BasicBullet class is a subclass of the superClass BulletEntity.
 * It represents a Bullet with no specific behavior.
 * It goes in a straight line from its spawn point.
 */
public class BasicBullet extends BulletEntity {

    /**
     * Constructor for the BasicBullet class
     * @param ImagePath Path to the image file representing the BasicBullet entity.
     * @param damage The quantity of damage received by the entity that it hits
     * @param movementSpeed Quantity of movement at each timestep.
     * @param emiter The entity that created the bullet.
     * @param shootAngle The angle that there is between the emiter and the direction the bullet face
     */
    public BasicBullet(String ImagePath, int damage, double movementSpeed, Weapon emiter, int shootAngle)
    {
        super(ImagePath, damage, movementSpeed, emiter, shootAngle);
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Basic Bullet from " + this.getEmiter();
    }
}
