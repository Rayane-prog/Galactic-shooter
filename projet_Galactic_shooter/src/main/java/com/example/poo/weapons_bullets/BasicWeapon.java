package com.example.poo.weapons_bullets;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.Player;
import com.example.poo.spaceEntity.SpaceEntity;
import com.example.poo.utils.Global;

/**
 * The BasicWeapon class is a subclass o the abstract superclass Weapon.
 * It represents a Weapon with no specific purpose.
 * It will create a bullet when called from a SpaceEntity object.
 */
public class BasicWeapon extends Weapon
{
    /**
     * Constructor for the BasicWeapon class
     * @param user The SpaceEntity that use the Weapon Object
     * @param clockTimestep The time between to call to the shoot method
     */
    public BasicWeapon(SpaceEntity user, int clockTimestep)
    {
        super(user, clockTimestep);
        this.setBulletDamage(5);
    }

    /**
     * Second Constructor for the BasicWeapon class
     * Used for random time step between each possible shot
     * @param user The SpaceEntity that use the Weapon Object
     */
    public BasicWeapon(SpaceEntity user)
    {
        super(user);
        this.setBulletDamage(5);
    }

    /**
     * Overrides the shoot method from the superclass to handle the shooting behavior.
     * Shoot an only BasicBullet in front of the entity
     */
    @Override
    public void shoot()
    {
        try {
            double mvSpeed = 1 ;
            if (this.getUser() instanceof Player) {mvSpeed = 2.5;}
            BasicBullet tempBullet = createBullet(mvSpeed, 0);
            tempBullet.setRotationImageView(this.getUser().getRotation() + tempBullet.getAngle());
            this.addBullet(tempBullet);
        } catch (Exception e) {
            System.out.println("Error loading BasicBullet's image " + e.getMessage());
            Global.log.write("Error loading BasicBullet's image " + e.getMessage());
        }
    }

    /**
     * Second method shoot called to shoot a basic bullet in a given angle
     * @param shootAngle The angle the bullet is emit with
     */
    public void shoot(int shootAngle)
    {
        try {
            double mvSpeed = 1 ;
            if (this.getUser() instanceof Player) {mvSpeed = 2.5;}
            BasicBullet tempBullet = createBullet(mvSpeed, shootAngle);
            tempBullet.setRotationImageView(this.getUser().getRotation() + tempBullet.getAngle());
            this.addBullet(tempBullet);
        } catch (Exception e) {
            System.out.println("Error loading BasicBullet's image " + e.getMessage());
            Global.log.write("Error loading BasicBullet's image " + e.getMessage());
        }
    }

    /**
     * Method to create a Basic Bullet with the right parameters
     * @param movementSpeed The Speed of the basic Bullet object
     * @param shootAngle The angle the bullet will be emit with
     * @return The Basic Bullet created from the parameters
     */
    private BasicBullet createBullet(double movementSpeed, int shootAngle)
    {
        String playerBullet ;

        //This is used to match the bullet color to the skin choose by the player
        if (this.getUser() instanceof Player)
        {
            switch (((Player) this.getUser()).getSkinColor())
            {
                case "RED" -> playerBullet = "bulletRed.png";
                case "BLUE" -> playerBullet = "bulletBlue.png";
                case "BLACK" -> playerBullet = "bulletBlack.png";
                default -> playerBullet = "bulletGreen.png";
            }
        } else {
            playerBullet = "bulletRed.png" ;
        }

        BasicBullet tempBullet = new BasicBullet(playerBullet, this.getBulletDamage(), movementSpeed, this, shootAngle);
        tempBullet.setEntityPos(this.getUser().getXPosition(),this.getUser().getYPosition()-this.getUser().getFitEntityHeight());
        if(this.getUser() instanceof Enemy)
        {
            tempBullet.setEntityPos(this.getUser().getXPosition() - this.getUser().getEntityWidth() / 3.5, this.getUser().getYPosition() + this.getUser().getEntityHeight());
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
        return "Basic Weapon held by " + this.getUser();
    }
}
