package com.example.poo.spaceEntity;

import com.example.poo.utils.HitBox;
import com.example.poo.utils.SpriteAnimation;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * An abstract class representing a game entity.
 */
public abstract class Entity
{
    private double xPosition;
    private double yPosition;
    private boolean isTouched; // checks whether an Entity enters in collision with another one.
    private boolean isDead;
    private double moveMultiplier ;
    private double movementSpeed;
    private String imageName; // file name of the entity image
    private Image entity; // Image object representing the entity
    private ImageView newEntity;  // ImageView object for displaying the entity
    private HitBox gameEntityHitBox; // An artificial HitBox to detect collisions with the getBoundsInParent() method of the rectangles.
    private ArrayList<String> imgPaths;
    private SpriteAnimation spriteAnimation;

    /**
     * Constructor for the Entity class.
     * @param imagePath Path to the image file representing the entity.
     */
    public Entity(String imagePath)
    {
        this.entity = new Image(imagePath); // load entity image from file
        this.newEntity = new ImageView(entity); // create ImageView with the entity image
        this.newEntity.setPreserveRatio(false);
        this.newEntity.setSmooth(true);
        this.gameEntityHitBox = new HitBox(this,entity.getWidth(),entity.getHeight(),this.xPosition,this.yPosition);
    }

    // GETTERS //

    /**
     * Gets the X-coordinate position of the entity.
     * @return The X-coordinate position of the entity.
     */
    public double getXPosition() { return this.xPosition ;}

    /**
     * Gets the Y-coordinate position of the entity.
     * @return The Y-coordinate position of the entity.
     */
    public double getYPosition() { return this.yPosition ;}

    /**
     * Gets the HitBox of the entity.
     * @return The Rectangle shaped HitBox of the entity.
     */
    public HitBox getHitBox() {return this.gameEntityHitBox;}

    /**
     * Gets the movement multiplier for the entity.
     * @return The movement multiplier for the entity.
     */
    public double getMoveMultiplier() {
        return moveMultiplier;
    }

    /**
     * Gets the height of the entity image.
     * @return The height of the entity image.
     */
    public double getEntityHeight() {return entity.getHeight();}

    /**
     * Gets the fit width of the entity image.
     * @return The fit width of the entity image.
     */
    public double getFitEntityWidth() {return newEntity.getFitWidth();}

    /**
     * Gets the fit height of the entity image.
     * @return The fit height of the entity image.
     */
    public double getFitEntityHeight() {return newEntity.getFitHeight();}

    /**
     * Gets the width of the entity image.
     * @return The width of the entity image.
     */
    public double getEntityWidth() {return entity.getWidth();}

    /**
     * Gets the rotation already applied to the imageView
     * @return A double with the rotation value
     */
    public double getRotation() {return this.newEntity.getRotate();}

    /**
     * Gets the touched status of the entity.
     * @return The touched status of the entity.
     */
    public boolean getTouchedStatus() {return isTouched;}

    /**
     * Gets the movement speed of the entity.
     * @return The movement speed of the entity.
     */
    public double getMovementSpeed() {
        return movementSpeed;
    }

    /**
     * Gets the status of the entity.
     * @return The status of the entity.
     */
    public boolean hasDeadStatus() {return this.isDead;}

    /**
     * Gets the SpriteAnimation of the entity.
     * @return The ArrayList representing all the sprites that make the animated sprite.
     */
    public SpriteAnimation getSpriteAnimation() { return this.spriteAnimation;}

    // SETTERS //

    /**
     * Method to set the position of the entity.
     * @param x X-coordinate position.
     * @param y Y-coordinate position.
     */
    public void setEntityPos(double x, double y)
    {
        this.xPosition = x;
        this.yPosition = y;
        this.newEntity.setX(this.xPosition);
        this.newEntity.setY(this.yPosition);
        if(this instanceof Boss)
        {
            this.gameEntityHitBox.updatePosition(x+98,y); // 98 is the size of an arm of the boss
        } else {
            this.gameEntityHitBox.updatePosition(x,y); // So that the HitBox follows the entity.
        }

    }

    /**
     * Method to resize the entity.
     * @param height Height value.
     * @param width Width value.
     */
    public void setEntitySize(double height, double width)
    {
        this.newEntity.setFitHeight(height);
        this.newEntity.setFitWidth(width);
        this.gameEntityHitBox.updateSize(width,height);
    }

    /**
     * Sets the touched status of the entity.
     * @param isTouched The touched status of the entity.
     */
    public void setIsTouched(boolean isTouched)
    {
        this.isTouched = isTouched;
    }

    /**
     * Sets the dead status of the entity.
     * @param isDead The dead status of the entity.
     */
    public void setIsDead(boolean isDead) { this.isDead = isDead;}

    /**
     * Sets the movement multiplier for the entity.
     * @param moveMultiplier The movement multiplier for the entity.
     */
    public void setMoveMultiplier(double moveMultiplier)
    {
        this.moveMultiplier = moveMultiplier;
    }

    /**
     * Sets the movement speed of the entity.
     * @param movementSpeed The movement speed of the entity.
     */
    public void setMovementSpeed(double movementSpeed)
    {
        this.movementSpeed = movementSpeed;
    }

    /**
     * Sets the rotation angle for the imageView of the entity.
     * @param angle The rotation angle.
     */
    public void setRotationImageView(double angle)
    {
        newEntity.setRotate(angle);
    }

    /**
     * Sets a new ImageView for the entity.
     * @param imageView The new ImageView object.
     */
    public void setNewImageView(ImageView imageView) { this.newEntity = imageView;}

    /**
     * Sets the image paths for the SpriteAnimation.
     * @param imgPaths The ArrayList representing the image paths.
     */
    public void setImgPaths(ArrayList<String> imgPaths) {
        this.imgPaths = imgPaths;
    }

    /**
     * Sets the SpriteAnimation for the entity.
     */
    public void setSpriteAnimation()
    {
        this.spriteAnimation = new SpriteAnimation(this,imgPaths,imgPaths.size());
    }

    /**
     * Gets the ImageView object representing the entity for display.
     * @return The ImageView object representing the entity.
     */
    public ImageView displayEntity(){ return this.newEntity;}

    /**
     * Gets the Rectangle object representing the HitBox.
     * @return The HitBox object representing the collision of the entity.
     */
    public Rectangle displayHitBox(){return this.getHitBox().getHitbox(); }

    /**
     * Moves the entity upwards on the screen by adjusting its Y-coordinate position.
     */
    public void moveEntityUp()
    {
        this.setEntityPos(this.getXPosition(),this.getYPosition()-(this.moveMultiplier*this.movementSpeed));
    }

    /**
     * Moves the entity downwards on the screen by adjusting its Y-coordinate position.
     */
    public void moveEntityDown()
    {
        this.setEntityPos(this.getXPosition(),this.getYPosition()+(this.moveMultiplier*this.movementSpeed));
    }

    /**
     * Moves the entity leftwards on the screen by adjusting its X-coordinate position.
     */
    public void moveEntityLeft()
    {
        this.setEntityPos(this.getXPosition()-(this.moveMultiplier*this.movementSpeed),this.getYPosition());
    }

    /**
     * Moves the entity rightwards on the screen by adjusting its X-coordinate position.
     */
    public void moveEntityRight()
    {
        this.setEntityPos(this.getXPosition()+(this.moveMultiplier*this.movementSpeed),this.getYPosition());
    }

    /**
     * Abstract method representing the movement of the entity.
     */
    public abstract void move() ;

    /**
     * Displays the entity on the screen.
     * @param root The JavaFX Group representing the root of the scene.
     */
    public abstract void display(Group root);

    /**
     * Abstract method representing all the action a SpaceEntity object wil have to do each frame.
     * @param root Group Object used the display the object if necessary
     */
    public abstract void evolve(Group root);
}
