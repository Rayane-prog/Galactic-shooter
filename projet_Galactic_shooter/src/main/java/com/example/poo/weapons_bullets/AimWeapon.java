package com.example.poo.weapons_bullets;

import com.example.poo.spaceEntity.Enemy;
import com.example.poo.spaceEntity.SpaceEntity;
import com.example.poo.utils.Global;

/**
 * The AimWeapon class is a subclass of the abstract superclass Weapon
 * It represents a Weapon shooting a AimBullet
 * It will create a AimBullet when called from a SpaceEntity object
 */
public class AimWeapon extends HomingWeapon{

    /**
     * Constructor for the AimWeapon class
     * @param user The SpaceEntity that use the Weapon Object
     * @param clockTimestep The time between to call to the shoot method
     */
    public AimWeapon(SpaceEntity user, int clockTimestep) {
        super(user, clockTimestep);
        this.setBulletDamage(5);
    }

    /**
     * Second Constructor for the BasicWeapon class
     * Used for random time step between each possible shot
     * @param user The SpaceEntity that use the Weapon Object
     */
    public AimWeapon(SpaceEntity user)
    {
        super(user);
        this.setBulletDamage(5);
    }

    /**
     * Overrides the shoot method from the superclass to handle the shooting behavior.
     * Shoot an only AimBullet in front of the entity
     */
    @Override
    public void shoot() {

        SpaceEntity target = this.getTarget();

        try {
            AimBullet tempBullet = new AimBullet("bulletRed.png", this.getBulletDamage(), 2, this, 0, target);
            tempBullet.setEntityPos(this.getUser().getXPosition(),this.getUser().getYPosition()-this.getUser().getFitEntityHeight());
            if(this.getUser() instanceof Enemy)
                tempBullet.setEntityPos(this.getUser().getXPosition() + this.getUser().getFitEntityWidth()/2.5,this.getUser().getYPosition()+this.getUser().getEntityHeight()/2);
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
        return "Aim Weapon hold by " + this.getUser();
    }
}
