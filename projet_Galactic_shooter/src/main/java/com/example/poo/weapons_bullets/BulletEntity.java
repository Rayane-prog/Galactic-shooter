package com.example.poo.weapons_bullets;

import com.example.poo.spaceEntity.Entity;
import com.example.poo.utils.Global;
import com.example.poo.spaceEntity.SpaceEntity;
import javafx.scene.Group;

/**
 * The Bullet class is an abstract class representing a generic weapon in the game.
 */
public abstract class BulletEntity extends Entity
{
    private boolean toDelete; //Boolean to let the weapon know he needs to delete this object
    private int damage;
    private Weapon emiter; // The weapon that created this bullet object
    private int shootAngle; // Define the angle applied to the sprite when fired, the bullet will go straight following it

    /**
     * Constructor for the Bullet class
     * @param imagePath Path to the image file representing the Bullet entity.
     * @param damage The quantity of damage received by the entity that it hits.
     * @param movementSpeed Quantity of movement at each timestep.
     * @param emiter The entity that created the bullet
     * @param shootAngle The angle that the bullet will be emit with
     */
    public BulletEntity(String imagePath, int damage, double movementSpeed, Weapon emiter, int shootAngle)
    {
        super(imagePath);
        this.emiter = emiter;
        this.setEntityPos(emiter.getUser().getXPosition(),emiter.getUser().getYPosition());
        this.damage = damage;
        this.setMovementSpeed(movementSpeed);
        this.setEntitySize(64,64);
        this.setMoveMultiplier(3.0);
        this.toDelete = false;
        this.shootAngle = shootAngle;
    }

    /**
     * Constructor for the Bullet class
     * @param imagePath Path to the image file representing the space entity
     * @param emiter The entity that created the bullet
     */
    public BulletEntity(String imagePath, Weapon emiter)
    {
        this(imagePath, 1, 1, emiter, 0);
    }

    /**
     * Gets the damage applied on hit
     * @return A float representing the quantity of damage
     */
    public int getDamage() { return this.damage;}

    /**
     * Gets the Status of the bullet
     * @return A boolean that is true if the bullet needs to be deleted
     */
    public boolean getBulletStatus() {return this.toDelete;}

    /**
     * Gets the Weapon that created this object
     * @return The Weapon Object
     */
    public Weapon getEmiter() {return this.emiter;}

    /**
     * Gets the angle that this bullet was created with
     * @return A int with the angle
     */
    public int getAngle() {return this.shootAngle;}

    /**
     * Remove the link that this bullet have with its emitter weapon
     */
    public void loseWeapon() {this.emiter = null;}

    /**
     * Sets the Status of the bullets to "have to be deleted"
     */
    public void setToDelete() {this.toDelete = true;}

    /**
     * Method called each frame from the Weapon object.
     * @param root The Group Object used to display objects.
     */
    public void evolve(Group root)
    {
        this.move();
        //Restrict the bullet to the screen
        //If it leaves the screen it is set to be deleted
        if (this.getXPosition() + this.getFitEntityWidth() > 2* Global.widgetWidth
                || this.getXPosition() + this.getFitEntityWidth() < 0
                || this.getYPosition() + this.getFitEntityHeight() > Global.widgetHeight
                || this.getYPosition() + this.getFitEntityHeight() < 0)
            this.toDelete = true;
    }

    /**
     * Overrides The display method
     * Called each frame to display the object
     * @param root The JavaFX Group representing the root of the scene.
     */
    public void display(Group root)
    {
        root.getChildren().remove(this.displayEntity());
        root.getChildren().add(this.displayEntity());
        root.getChildren().remove(this.displayHitBox());
        root.getChildren().add(this.displayHitBox());
    }

    /**
     * Move the bullet in the direction it is facing using Math java class with the speed given by its attribute
     * Will need to be overridden when a more specific bullet object will need it
     */
    public void move()
    {
        this.setEntityPos(this.getXPosition() + Math.sin(this.getRotation()*Math.PI/180) * this.getMoveMultiplier() * this.getMovementSpeed(),
                this.getYPosition() - Math.cos(this.getRotation()*Math.PI/180) * this.getMoveMultiplier() * this.getMovementSpeed());
    }

    /**
     * Method representing the effect on hit of the Bullet entity
     * Defines a basic effect to display on collision and what to do to the collide entity
     */
    public void hitEffect(SpaceEntity e,Group root)
    {
        e.decreaseHealth(this.getDamage());
        if (e.hasDeadStatus()) {e.deathEffect(root);}
    }
}