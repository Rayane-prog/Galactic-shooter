package com.example.poo.weapons_bullets;

import java.util.ArrayList;

import com.example.poo.spaceEntity.SpaceEntity;
import com.example.poo.utils.Clock;
import javafx.scene.Group;
import java.util.Iterator;
import java.util.Random;

/**
 * The Weapon class is an abstract class representing a generic weapon in the game.
 * A weapon object will be used by any SpaceEntity to shoot certain type of Bullets objects
 */
public abstract class Weapon
{
    private SpaceEntity user;
    private final ArrayList<BulletEntity> bullets; //Stock all the bullets created by this weapon object
    private Clock shootingClock;
    private int bulletDamage;

    /**
     * Constructor for the Weapon class
     * @param user The SpaceEntity that use the Weapon Object.
     * @param clockTimestep The time step between two possible shot
     */
    public Weapon(SpaceEntity user, double clockTimestep)
    {
        this.user = user;
        if(user != null)
            user.addWeapon(this);
        bullets = new ArrayList<BulletEntity>();
        this.shootingClock = new Clock(clockTimestep);
        this.bulletDamage = 1;
    }

    /**
     * Second constructor for the Weapon class
     * Used for random time step between each possible shot
     * @param user The SpaceEntity that use the Weapon Object
     */
    public Weapon(SpaceEntity user) {
        this.user = user;
        if(user != null)
            user.addWeapon(this);
        bullets = new ArrayList<BulletEntity>();
        Random random = new Random();
        this.shootingClock = new Clock(random.nextInt(10000) + 3000);
        this.bulletDamage = 1;
    }

    /**
     * Gets the SpaceEntity Object linked to this Weapon
     * @return The SpaceEntity Object.
     */
    public SpaceEntity getUser() {return this.user;}

    /**
     * Gets the clock Object linked to this Weapon that have the time of it's creation
     * @return The clock Object
     */
    public Clock getClock() { return this.shootingClock;}

    /**
     * Link a SpaceEntity to this Weapon
     * @param user The SpaceEntity that will use this weapon
     */
    public void addUser(SpaceEntity user) { this.user = user;}

    /**
     * Unlink the weapon with the user
     */
    public void loseUser()
    {
        this.user = null;
    }

    /**
     * Gets the ArrayList Object of the bullets
     * @return The ArrayList<Bullet> object
     */
    public ArrayList<BulletEntity> getBullets() { return this.bullets;}

    /**
     * Gets the damage that the bullet will inflict
     * @return The int containing the value
     */
    public int getBulletDamage() {return this.bulletDamage;}

    /**
     * Sets the damage that the bullet will inflict
     * @param newDamage A int being the new value
     */
    public void setBulletDamage(int newDamage) {this.bulletDamage = newDamage;}

    /**
     * Method to link a Bullet object to the Weapon object.
     * Every bullet that the object emits will be added here
     * @param bullet The object just created
     */
    public void addBullet(BulletEntity bullet)
    {
        bullets.add(bullet);
    }

    /**
     * Sets a new time between two calls to the shoot method
     * @param time An int being the new time
     */
    public void setNewTimeStep(int time)
    {
        this.shootingClock = new Clock(time);
    }

    /**
     * Method called each frame from the SpaceEntity.
     * Uses to call the evolve method of the bullet and delete it when it's need to be.
     * @param root The Group Object used to display objects
     */
    public void evolve(Group root)
    {
        Iterator<BulletEntity> it = this.bullets.iterator();

        while(it.hasNext())
        {
            BulletEntity current = it.next();
            current.evolve(root);
            if(current.getBulletStatus()) {
                current.loseWeapon();
                root.getChildren().remove(current.displayHitBox());
                root.getChildren().remove(current.displayEntity());
                it.remove();
            }
        }
    }

    /**
     * Checks if the Weapon has active bullets in the game
     * @return A boolean that is true if there is active bullets liked to this Weapon object
     */
    public boolean hasActiveBullets() {
        return !this.bullets.isEmpty();
    }

    /**
     * Method called each frame in order to update the position of each bullet that had been shot by the weapon.
     * @param root The Group Object used to display objects
     */
    public void display(Group root)
    {
        Iterator<BulletEntity> it = this.bullets.iterator();

        while(it.hasNext())
        {
            BulletEntity current = it.next();
            current.display(root);
        }
    }

    /**
     * Abstract method that will be call to fire from the Weapon Object.
     * This method will vary for each type of Weapon entities
     */
    public abstract void shoot();
}
