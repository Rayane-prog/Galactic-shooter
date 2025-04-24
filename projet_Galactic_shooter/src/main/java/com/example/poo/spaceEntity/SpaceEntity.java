package com.example.poo.spaceEntity;

import com.example.poo.collectibles.Coin;
import com.example.poo.collectibles.CollectibleEntity;
import com.example.poo.utils.Global;
import com.example.poo.utils.SpriteAnimation;
import com.example.poo.weapons_bullets.BulletEntity;
import com.example.poo.weapons_bullets.Weapon;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

/**
 * The SpaceEntity class is an abstract class representing a generic space entity in the game.
 * It contains properties and methods common to all space entities, such as position, health points, and movement.
 */
public abstract class SpaceEntity extends Entity {

    private int healthPoints;
    private int maxHealthPoints ;
    private Weapon weapon;
    protected boolean isNotExceedingBoundaryLeft ;
    protected boolean isNotExceedingBoundaryRight ;
    private SpriteAnimation spriteModifications;

    /**
     * Constructor for the SpaceEntity class.
     * @param imagePath Path to the image file representing the space entity.
     * @param healthPoints Initial health points of the space entity.
     */
    public SpaceEntity(String imagePath, int healthPoints)
    {

        super(imagePath);
        this.healthPoints = healthPoints;
        this.maxHealthPoints = healthPoints;
        this.setMoveMultiplier(3.0);
        this.setIsDead(false);
        this.weapon = null;
        this.spriteModifications = new SpriteAnimation(this);
    }

    /**
     * Gets the health points of the space entity.
     * @return The health points of the space entity.
     */
    public int getHealthPoints() { return this.healthPoints ;}

    /**
     * Gets the Weapon object linked to this entity
     * @return The Weapon Object
     */
    public Weapon getWeapon() {return this.weapon;}

    /**
     * Method to set the health points of the space entity.
     * If health points reach zero or below, the space entity is marked as dead.
     * @param hP Health points to be added or subtracted.
     */
    public void setHealthPoints(int hP)
    {
        this.healthPoints = hP ;
        if (this.getHealthPoints() <= 0)
        {
            this.setIsDead(true);
        }
    }

    /**
     * Increases the health points of the space entity by a specified amount.
     * @param hP The amount by which to increase the health points.
     */
    public void increaseHealth(int hP)
    {
        // We can't have more than the hp the entity had at its creation.
        if (this.getHealthPoints() + hP > this.maxHealthPoints)
        {
            this.healthPoints = this.maxHealthPoints ;
        } else {
            this.healthPoints += hP ;
            assert Global.getPlayer() != null;
            Global.getPlayer().increasingHealthBar(hP);
        }
    }

    /**
     * Decreases the health points of the space entity by a specified amount.
     * @param hP The amount by which to decrease the health points.
     */
    public void decreaseHealth(int hP)
    {
        this.healthPoints -= hP ;
        if (this instanceof Player)
        {
            assert Global.getPlayer() != null;
            Global.getPlayer().decreasingHealthBar(hP);
        }
        if (this.getHealthPoints() <= 0)
        {
            this.setIsDead(true);
        }
        else {
            this.spriteModifications.takeDamageAnimation();
        }
    }

    /**
     * Method to link a Weapon object to its SpaceEntity
     * @param gun The Weapon object to link
     */
    public void addWeapon(Weapon gun)
    {
        if(this.weapon != null)
            this.weapon.loseUser();
        gun.addUser(this);
        this.weapon = gun;
    }

    /**
     * Method to unlink the weapon from it's SpaceEntity.
     */
    public void loseWeapon()
    {
        this.weapon.loseUser();
        this.weapon = null;
    }

    /**
     * Method to detect collisions between the player and the enemies, it allows entities being able to enter in contact depending on their class / superclass
     * @param root :Group Object used in the evolve method called in this method.
     */
    public void onCollisionEnter(Group root)
    {
        /*
         * For each Node that are presents in the root , we check if the Node is a Rectangle, which is only used for HitBoxes.
         */
        for (Node ent : root.getChildren())
        {
            //Then, if we have a Rectangle, we check if it intersects with another HitBox that hasn't the same parentEntity and that isn't itself too
            if(ent instanceof Rectangle && this.getHitBox().getHitbox().getBoundsInParent().intersects(ent.getBoundsInParent()) && this.getHitBox().getHitbox() != ent
                    && ent.getProperties().containsKey("parentEntity"))
            {
                // We let the two entities passing through each other if they are the same entities, also we can't shoot to a collectible
                if (!(ent.getProperties().get("parentEntity").getClass().getSuperclass().equals(this.getClass().getSuperclass())) && !(ent.getProperties().get("parentEntity") instanceof CollectibleEntity))

                {
                    if(ent.getProperties().get("parentEntity") instanceof BulletEntity)
                    {
                        // Here we cast a BulletEntity, so we can apply BulletEntity's methods to the object, since only Bullets can be here.
                        BulletEntity b = ((BulletEntity) ent.getProperties().get("parentEntity"));

                        // With this, an enemy cannot shoot to another enemy, even if they are different enemies, we check if the class
                        //  of the emitter of the bullet is different to the receiver's class.
                        if (!(b.getEmiter().getUser().getClass().getSuperclass()).equals(this.getClass().getSuperclass()))
                        {
                            b.hitEffect(this,root); // Reduce hp by 5 if it's a BasicBullet for example
                            b.setToDelete(); // Delete the bullet, so it doesn't hit multiple times the touched entity.
                        }

                    } else {
                        this.decreaseHealth(this.getHealthPoints());  // we are getting one shot if we touch an Enemy.
                        ((SpaceEntity) ent.getProperties().get("parentEntity")).decreaseHealth(((SpaceEntity) ent.getProperties().get("parentEntity")).getHealthPoints());
                    }
                }
                // Only the player can interact with collectibles
                if((ent.getProperties().get("parentEntity") instanceof CollectibleEntity) && this instanceof Player)
                {

                    CollectibleEntity c = ((CollectibleEntity) ent.getProperties().get("parentEntity"));
                    if (c instanceof Coin){
                        assert Global.getPlayer() != null;
                        Global.money+=1;}
                    c.onCollect((Player) this);
                    c.setIsDead(true);
                }
            }
        }
    }


    /**
     * Abstract method that will be called when the SpaceEntity need to use it's linked Weapon Object
     */
    public abstract void shoot();

    /**
     * Abstract method to define the effect when the SpaceEntity dies.
     * @param root The JavaFX Group representing the root of the scene.
     */
    public abstract void deathEffect(Group root);

    /**
     * Displays the entity on the screen.
     * @param root The JavaFX Group representing the root of the scene.
     */
    @Override
    public void display(Group root)
    {
        this.getWeapon().display(root);
        root.getChildren().remove(this.displayEntity());
        root.getChildren().add(this.displayEntity());
        root.getChildren().remove(this.displayHitBox());
        root.getChildren().add(this.displayHitBox());
    }
}


