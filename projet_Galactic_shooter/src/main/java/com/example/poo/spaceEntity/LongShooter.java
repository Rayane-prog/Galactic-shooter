package com.example.poo.spaceEntity;

import com.example.poo.weapons_bullets.AimWeapon;

import java.util.Random;

/**
 * The Long Shooter is an enemy that has more hp than the galactic shooter but don't move and use an AimWeapon
 */
public class LongShooter extends Enemy{
    /**
     * Constructor for the LongShooter class
     */
    public LongShooter()
    {
        super("LongShooter.png",0,15,20,true);
        this.setEntitySize(55,64);
        this.loseWeapon();
        this.addWeapon(new AimWeapon(this,2500));
    }

    /**
     * Overrides the shoot method from the superclass to define the way enemy will shoot.
     */
    @Override
    public void shoot()
    {
        if (this.getWeapon().getClock().isComplete())
        {
            this.getWeapon().getClock().startClock();
            this.getWeapon().shoot();

            if (this.isShootingRandomly)
            {
                Random random = new Random();
                // will return a number between [0-2500] + 2500, meaning [2500-5000]ms.
                this.getWeapon().setNewTimeStep(random.nextInt(2501) + 2500);
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
        return "Long Shooter";
    }
}
