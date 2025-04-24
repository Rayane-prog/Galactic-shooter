package com.example.poo.spaceEntity;

import com.example.poo.utils.Global;

/**
 * The CosmicCharger Enemy is an aggressive but weak entity that randomly shoot toward the player
 * It is the most common enemy, deals little damage and has few hp too.
 */
public class CosmicCharger extends Enemy{
    private double xDirection;
    private double yDirection;

    /**
     * Constructor for the CosmicCharger class.
     * @param movementSpeed the speed of the CosmicCharger
     * @param xPosition the x coordinates of the CosmicCharger
     * @param yPosition the y coordinates of the CosmicCharger
     */
    public CosmicCharger(double movementSpeed,double xPosition, double yPosition) {
        super("CosmicCharger.png", movementSpeed,1,15,false);
        this.setEntityPos(xPosition,yPosition);
        this.setEntitySize(64,64);
        this.initPlayerPosition();
    }

    /**
     * Gets the player position
     */
    public void initPlayerPosition()
    {
        Player player = Global.getPlayer();
        if (player != null)
        {
            this.xDirection = player.getXPosition();
            this.yDirection = player.getYPosition();
        }
        facePlayer(xDirection, yDirection);
    }

    /**
     * The CosmicCharger charges toward the player
     */
    @Override
    public void move()
    {
        this.setEntityPos(this.getXPosition() + Math.sin(this.getRotation()*Math.PI/180) * this.getMoveMultiplier() * this.getMovementSpeed(),
                this.getYPosition() - Math.cos(this.getRotation()*Math.PI/180) * this.getMoveMultiplier() * this.getMovementSpeed());
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Cosmic Charger";
    }
}
