package com.example.poo.spaceEntity;

import com.example.poo.collectibles.*;
import com.example.poo.utils.Global;
import com.example.poo.weapons_bullets.BasicWeapon;
import com.example.poo.weapons_bullets.HomingWeapon;
import com.example.poo.weapons_bullets.RocketWeapon;
import javafx.scene.Group;

import java.util.Random;

/**
 * The Enemy class represents an enemy spaceship in the game.
 * It is a subclass of the abstract superclass SpaceEntity.
 * Enemies have specific behaviors and characteristics.
 */
public abstract class Enemy extends SpaceEntity {

    private final int killPoints;
    protected final boolean isShootingRandomly;

    private double initialRotation;

    /**
     * Constructor for the Enemy class.
     *
     * @param alien              Path to the image file representing the enemy spaceship.
     * @param movementSpeed      Speed of the enemy's movement.
     * @param healthpoint        Initial health points of the enemy.
     * @param killPoints         Points earned when the enemy is killed.
     * @param isShootingRandomly A flag indicating whether the enemy shoots randomly.
     */
    Enemy(String alien,double movementSpeed,int healthpoint,int killPoints,boolean isShootingRandomly)
    {
        super(alien,healthpoint); // Call superclass constructor with the given ship image and health points
        this.setMovementSpeed(movementSpeed);
        this.setRotationImageView(180); // Rotate the sprite to make face the player
        this.killPoints = killPoints;
        this.isShootingRandomly = isShootingRandomly;
        this.initialRotation = this.getRotation();

        if (isShootingRandomly) this.addWeapon(new BasicWeapon(this));                      //If it shoots randomly, we also apply a random value for the first shoot
        else                    this.addWeapon(new BasicWeapon(this, 600));   // Else we apply a static timestep
    }

    /**
     * Overrides the shoot method from the superclass. Provides the shooting behavior for the enemy.
     */
    @Override
    public void shoot() {

    }

    /**
     * Gets the kill points earned when the enemy is killed.
     * @return The kill points.
     */
    int getKillPoints() {return this.killPoints;}

    /**
     * Overrides the deathEffect method from the superclass. Contains actions performed when the enemy is killed.
     * @param root Group Object used to display the object.
     */
    @Override
    public void deathEffect(Group root){
        Global.log.write(this.getClass() + " is dead");
        Global.getPlayer().updateScore(this.getKillPoints());
        Global.getPlayer().updateKills();

        //Creating a collectible on death depending on a random value :
        try {
            Random randomValue = new Random();
            int val = (randomValue.nextInt(101));
            if ((val) <= 40 )
            {
                // do nothing
            } else if (val <= 70) {
                //Coin
                Coin c = new Coin("coin_anim00.png", this);
                Global.waitingEntities.add(c);

            } else if (val <= 95) {
                // Hearth
                Hearth h = new Hearth("hearth_anim00.png", this);
                Global.waitingEntities.add(h);
                // Bonuses
            } else {
                val = (randomValue.nextInt(101));
                if (val <= 20)
                {
                    SpeedBonus sB = new SpeedBonus("SpeedBonus.png", this);
                    Global.waitingEntities.add(sB);
                } else if (val <= 40) {
                    DamageBonus dB = new DamageBonus("DamageBonus.png", this);
                    Global.waitingEntities.add(dB);
                } else if (val <= 60) {
                    MovementBonus mB = new MovementBonus("MovementBonus.png",this);
                    Global.waitingEntities.add(mB);
                } else if (val <= 80) {
                    WeaponBonus rB = new WeaponBonus("RocketBonus.png",new RocketWeapon(null, 300), this, root);
                    Global.waitingEntities.add(rB);
                } else {
                    WeaponBonus rB = new WeaponBonus("HomingBonus.png",new HomingWeapon(null, 300), this, root);
                    Global.waitingEntities.add(rB);
                }
            }

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
    public void facePlayer(double xDirection, double yDirection)
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
     */
    @Override
    public void move() {}

    /**
     * Overrides the evolve method from the superclass. Contains every action of the enemy at each frame
     * @param root Group Object used the display the object
     */
    @Override
    public void evolve(Group root)
    {
        this.move();
        this.onCollisionEnter(root);
        if(!this.getTouchedStatus()) {
            if (this.getWeapon() != null) {
                this.shoot();
                this.getWeapon().evolve(root);
            }
        }
        if(this.getYPosition()+this.getEntityHeight()>=Global.widgetHeight && !(this instanceof Boss )) {this.setIsDead(true);}
    }

}