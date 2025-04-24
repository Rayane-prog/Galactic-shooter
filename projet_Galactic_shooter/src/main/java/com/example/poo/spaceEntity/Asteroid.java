package com.example.poo.spaceEntity;

import com.example.poo.collectibles.Coin;
import com.example.poo.utils.Global;
import javafx.scene.Group;

import java.util.Random;

/**
 * The class representing the Asteroid enemy that one shoot the player and goes slowly in a straight line
 */
public class Asteroid extends Enemy{

    private double initialRotation;
    private boolean isDirectingToRight;

    /**
     * Constructor for the Asteroid class.
     *
     * @param movementSpeed
     */
    public Asteroid(double movementSpeed) {
        super("Asteroid.png", movementSpeed,25,15,false); // GalacticGunner has 15 hp
        this.setEntitySize(100,100);
        this.initialRotation = this.getRotation();
        this.isDirectingToRight = true ;
    }

    /**
     * Direct the asteroid to go toward the player Y position
     * @param b A boolean representing were the asteroid is coming from
     */
    public void setDirection(boolean b)
    {
        this.isDirectingToRight = b ;
        Random random = new Random();

        // if the asteroid comes from the left, then it will go to the right, else it will go to the left.
        if(isDirectingToRight)
        {
            // the asteroid will go to an X pos between (500 and 2000)
            this.faceDirection(random.nextInt(1501) + 500, Global.getPlayer().getYPosition());
        } else
        {
            // the asteroid will go to an X pos between (-40, 960)
            this.faceDirection(random.nextInt(1061) - 100 , Global.getPlayer().getYPosition());
        }
    }

    /**
     * Overrides the deathEffect method from the superclass. Contains actions performed when the enemy is killed.
     * @param root Group Object used to display the object.
     */
    @Override
    public void deathEffect(Group root){
        Global.log.write(this.getClass() + " is dead");
        Global.score += this.getKillPoints();
        Global.getPlayer().updateScore(this.getKillPoints());
        Global.getPlayer().updateKills();
        //An asteroid will always drop a coin :
        try {
            Coin c = new Coin("coin_anim00.png", this);
            Global.waitingEntities.add(c);
        } catch (Exception e) {
            System.out.println("Error loading collectible's image: " + e.getMessage());
            Global.log.write("Error loading collectible's image: " + e.getMessage());
        }
    }

    /**
     * Rotate the object to make it face the player position
     * The third point used as coordinate as : X = Same as the object Y = Same as Player
     * @param xDirection X coordinate of the player
     * @param yDirection Y coordinate of the player
     */
    private void faceDirection(double xDirection, double yDirection)
    {
        this.setRotationImageView(this.initialRotation);
        double vecACx, vecACy, vecABx,vecABy, distAC, distAB, angle;
        vecACx = 0;                         //C.xPosition - this.xPosition;
        vecACy = yDirection - this.getYPosition();
        vecABx = xDirection - this.getXPosition();
        vecABy = yDirection - this.getYPosition();
        distAC = Math.sqrt(Math.pow(0, 2) + Math.pow(this.getYPosition() - yDirection, 2));
        distAB = Math.sqrt(Math.pow(this.getXPosition() - xDirection, 2) + Math.pow(this.getYPosition() - yDirection, 2));
        angle = Math.toDegrees(Math.acos((vecABx*vecACx + vecABy*vecACy) / (distAB * distAC)));

        if(this.getXPosition() < xDirection)
            this.setRotationImageView(this.getRotation() - angle);
        else
            this.setRotationImageView(this.getRotation() + angle);
    }

    /**
     * Overrides the move method from the superclass. Contains the movement behavior for the enemy.
     * The asteroid goes straight towards were it's heading
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
        return "Asteroid";
    }
}
