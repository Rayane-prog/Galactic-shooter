package com.example.poo.spaceEntity;

import com.example.poo.utils.Global;
import com.example.poo.weapons_bullets.AimWeapon;
import com.example.poo.weapons_bullets.HomingWeapon;
import com.example.poo.weapons_bullets.Weapon;
import javafx.scene.Group;

/**
 * The Boss class represents the boss Body
 */
public class Boss extends Enemy {

    private BossPart right, left;
    private double angle = 0 ;
    private Weapon secondWeapon;

    /**
     * Constructor for the Boss class
     * @param imagePath The path to the Boss body sprite
     * @param xPosition The X coordinates of the Boss
     * @param yPosition The Y coordinates of the Boss
     */
    public Boss(String imagePath, double xPosition, double yPosition)
    {
        super(imagePath, 0.1, 300, 5, false);
        this.right = null;
        this.left = null;
        this.secondWeapon = new AimWeapon(this, 1000);
        this.setEntityPos(xPosition, yPosition);
        this.setEntitySize(258, 450);
        this.getHitBox().updateSize(258,258); // size of the boss without the arms
    }

    /**
     * Add a part to the boss at the right attribute based on it's side
     * @param part The already created BossPart object
     * @param isRight A boolean that is true if the part is the right part, false for the left
     */
    public void addPart(BossPart part, boolean isRight)
    {
        if(isRight) {
            this.right = part;
            this.right.setEntityPos(this.getXPosition() + this.getFitEntityWidth()/1.2 + this.right.getFitEntityWidth(), this.getYPosition() + this.right.getFitEntityHeight()/3 );
        } else {
            this.left = part;
            this.left.setEntityPos(this.getXPosition() - this.left.getFitEntityWidth()/2.6, this.getYPosition() + this.right.getFitEntityHeight()/3 );
        }
    }

    /**
     * Remove the part set by the boolean
     * @param isRight
     */
    public void losePart(boolean isRight)
    {
        if(isRight)
            this.right = null;
        else
            this.left = null;

//        this.getWeapon().setNewTimeStep((int)(this.getWeapon().getClock().getTimeToPass()/1.2));
        Global.log.write(this + " is now in state " + this.getLifeState());
    }

    /**
     * Gets the state of the boss based on the number of part killed
     * @return A int with the number of part killed
     */
    public int getLifeState()
    {
        int state = 0;

        if(this.right == null) state++;
        if(this.left == null) state++;
        return state;
    }

    /**
     * Overrides the shoot method from the superclass to handle the shooting behavior.
     */
    @Override
    public void shoot()
    {
        if (this.getWeapon().getClock().isComplete() && this.getLifeState() >= 1)
        {
            if(this.getLifeState() == 1) {
                this.getWeapon().getClock().startClock();
                this.getWeapon().shoot();
            }

            if(this.getLifeState() == 2) {
                this.secondWeapon.getClock().startClock();
                this.secondWeapon.shoot();
            }
        }
    }

    /**
     * Overrides the superclass method to make the bossBody invincible when all part are not dead
     * @param hp The amount by which to decrease the health points.
     */
    @Override
    public void decreaseHealth(int hp)
    {
        if(this.right == null && this.left == null) {
            super.decreaseHealth(hp);
        } else {
            Global.log.write(this + " hit when invulnerable");
        }
    }

    /**
     * Overrides the superclass method to make the boss follow a designed pattern
     */
    @Override
    public void move() {

        // The larger this value is, the smaller the circle will be
        double angularSpeed = 0.2;
        double radius = 10;
        double angleInRadians = Math.toRadians(angle);
        angle += angularSpeed;

        double newX = this.getXPosition() + 2*radius * Math.cos(angleInRadians) * this.getMovementSpeed();
        double newY = this.getYPosition() + 0.5*radius * Math.sin(angleInRadians) * this.getMovementSpeed();
        this.setEntityPos(newX, newY);

        if (this.right != null)
        {
            newX = this.right.getXPosition() + 2*radius * Math.cos(angleInRadians) * this.right.getMovementSpeed();
            newY = this.right.getYPosition() + 0.5*radius * Math.sin(angleInRadians) * this.right.getMovementSpeed();
            this.right.setEntityPos(newX,newY);
        }

        if (this.left != null)
        {
            newX = this.left.getXPosition() + 2 * radius * Math.cos(angleInRadians) * this.left.getMovementSpeed();
            newY = this.left.getYPosition() + 0.5 * radius * Math.sin(angleInRadians) * this.left.getMovementSpeed();
            this.left.setEntityPos(newX, newY);
        }


    }

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
                this.secondWeapon.evolve(root);
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
        return "Boss Body";
    }
}