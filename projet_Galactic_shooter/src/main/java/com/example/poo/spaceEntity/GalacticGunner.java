package com.example.poo.spaceEntity;

import com.example.poo.utils.Global;

import java.util.Random;

/**
 * The GalacticGunner Enemy is a continuously moving entity that randomly shoot toward the player
 * It is the most common enemy, deals little damage and has few hp too.
 */
public class GalacticGunner extends Enemy {
    private boolean goRight;

    /**
     * Constructor for the Enemy class.
     * @param movementSpeed the speed of the CosmicCharger
     */
    public GalacticGunner( double movementSpeed) {
        super("GalacticGunner.png", movementSpeed,10,10,true); // GalacticGunner has 15 hp
        //Depending on where it spawns, it will go to the left or the right ( the closest side will be chosen ).
        this.goRight = (this.getXPosition() < Global.widgetWidth / 2);
    }

    /**
     * Sets the attributes goRight to the passed value
     * @param b The new value of go Right
     */
    public void setDirection(boolean b)
    {
        this.goRight = b ;
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
                // will return a number between [0-5000] + 5000, meaning [5000-10000]ms.
                this.getWeapon().setNewTimeStep(random.nextInt(15001) + 5000);
            }

        }

    }

    /**
     * The GalacticGunner move left side to right side.
     */
    @Override
    public void move()
    {
        isNotExceedingBoundaryRight = this.getXPosition()+(this.getMoveMultiplier()*10) <= Global.widgetWidth - this.getEntityWidth()/3;
        isNotExceedingBoundaryLeft =  this.getXPosition()-(this.getMoveMultiplier()*10) >= 0 - this.getEntityWidth()/1.5;

        if (this.isNotExceedingBoundaryLeft && goRight) { this.moveEntityLeft();}
        if (this.isNotExceedingBoundaryRight && !goRight) { this.moveEntityRight();}

        if (!this.isNotExceedingBoundaryLeft) { goRight = false;}
        if (!this.isNotExceedingBoundaryRight) { goRight = true;}
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Galactic Gunner";
    }
}
