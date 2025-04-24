package com.example.poo.spaceEntity;

import com.example.poo.collectibles.TemporaryBonus;
import com.example.poo.utils.Global;
import com.example.poo.utils.ViewManager;
import com.example.poo.weapons_bullets.BasicWeapon;
import com.example.poo.weapons_bullets.Weapon;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * The Player class is a subclass of the abstract superclass SpaceEntity.
 * It represents the player's spaceship in the game and defines its behavior.
 * The player's spaceship can move freely and has specific death effects.
 */
public class Player extends SpaceEntity
{
    private Scene gameScene ; // The scene where the game is played
    private boolean isMovingUp ;
    private boolean isMovingDown ;
    private boolean isMovingLeft ;
    private boolean isMovingRight ;
    private boolean isNotExceedingBoundaryUp;
    private boolean isNotExceedingBoundaryDown;
    private boolean canMoveUp;
    private boolean canMoveDown;
    private boolean isShooting ;
    private int coinsCollected ;
    private int enemiesKilled ;
    private Text scoreText;
    private ArrayList<TemporaryBonus> bonusList;
    private ArrayList<Weapon> pastWeapon; //Store all the weapon used that still have active bullets
    private double initialBarHeight,initialHelathPoints; // Store the initial values of the health and bar height for the calculation of the value of resize.
    private Rectangle healthBar,healthBarCanva;  // the health bar is a composition of two rectangles , one that displays health
                                                 // and the other that is a canva of the real bar to create an effect of emptiness when losing health
    private String skinColor ;

    /**
     * Constructor for the Player class.
     * @param ship          Path to the image file representing the player's spaceship.
     * @param gameScene     The scene where the game is played.
     * @param movementSpeed The current speed of the player.
     */
    public Player(String ship, Scene gameScene,double movementSpeed)
    {
        super(ship,30); // Call superclass constructor with the given ship image and health points
        this.gameScene = gameScene;
        this.setEntityPos( Global.widgetWidth / 2 - this.getEntityWidth() / 2, Global.widgetHeight - 100 - this.getEntityHeight());
        this.setEntitySize(64,64);
        this.addWeapon(new BasicWeapon(this, 300)); //Add a Basic Weapon the user, this for test purpose only
        this.setMovementSpeed(movementSpeed);
        this.enemiesKilled = 0;
        this.coinsCollected = 0 ;
        this.scoreText = new Text();

        scoreText.setFont(Font.loadFont("file:src/main/resources/fonts/ThaleahFat.ttf",35));
        scoreText.setFill(Color.WHITE);

        this.healthBar = new Rectangle(50,360,20,400);
        this.healthBar.setFill(Color.RED);

        this.healthBarCanva = new Rectangle(50,360,20,400);
        this.healthBarCanva.setFill(Color.TRANSPARENT);
        this.healthBarCanva.setStroke(Color.GRAY);

        this.initialBarHeight = this.healthBar.getHeight();
        this.initialHelathPoints = this.getHealthPoints();

        this.bonusList = new ArrayList<>();
        this.pastWeapon = new ArrayList<>();

        // get ship color to apply special bullet color
        switch (ship) {
            case "playerBlackShip.png" -> skinColor = "BLACK";
            case "playerRedShip.png" -> skinColor = "RED";
            case "playerBlueShip.png" -> skinColor = "BLUE";
            default -> skinColor = "GREEN";
        }

    }

    /**
     * Listen to all the key the player can input to play and set the corresponding boolean in response.
     */
    private void listener()
    {
        gameScene.setOnKeyPressed(event ->
        {
            KeyCode code = event.getCode();
            switch (code)
            {
                case LEFT:
                    this.isMovingRight = false;
                    this.isMovingLeft = true;
                    break;

                case RIGHT:
                    this.isMovingLeft = false;
                    this.isMovingRight = true;
                    break;

                case UP:
                    if(this.canMoveUp) {
                        this.isMovingDown = false;
                        this.isMovingUp = true;
                    }
                    break;

                case DOWN:
                    if(this.canMoveDown) {
                        this.isMovingUp = false;
                        this.isMovingDown = true;
                    }
                    break;

                case SPACE:
                    this.isShooting = true ;
                    break;

                default:
                    break;
            }

            if (event.getCode() == KeyCode.P) {
                ViewManager.isPaused = false;
            }
            if (event.getCode() == KeyCode.M) {
                ViewManager.isPaused = true;
            }

        });
        gameScene.setOnKeyReleased(event ->
        {
            KeyCode code = event.getCode();
            switch (code)
            {
                case LEFT:
                    this.isMovingLeft = false;
                    break;

                case RIGHT:
                    this.isMovingRight = false ;
                    break;

                case UP:
                    this.isMovingUp = false;
                    break;

                case DOWN:
                    this.isMovingDown = false;
                    break;

                case SPACE:
                    this.isShooting = false ;
                    break;

                default:
                    break;
            }
        });
    }

    /**
     * Gets the text object representing the player's score.
     * @return The score text.
     */
    public Text getScoreText(){return  this.scoreText;}

    /**
     * Updates the player's score and displays a fading score increment text.
     * @param value The value to increment the score by.
     */
    public void updateScore(int value)
    {
        Global.score+=value;
        this.scoreText.setText("+"+value);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), scoreText);
        // Set the initial and final opacity values
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        // Set the cycle count (how many times the animation should repeat)
        fadeTransition.setCycleCount(1);

        // Play the animation
        fadeTransition.play();
    }

    /**
     * Gets the number of enemy killed
     * @return A int with the value
     */
    public int getEnemiesKilled() { return this.enemiesKilled;}

    /**
     * Gets the number of coins collected
     * @return A int with the value
     */
    public int getCoinsCollected() { return this.coinsCollected;}

    /**
     * Gets the list of TemporaryBonus
     * @return The ArrayList
     */
    public ArrayList<TemporaryBonus> getBonuses() { return this.bonusList ;}

    /**
     * Gets the skinColor of the player
     * @return The string with the value
     */
    public String getSkinColor() { return  this.skinColor ;}

    //True if can move in at least one direction false if 0 possibility to move on Y axis

    /**
     * Return a boolean that is true if the player can move in at least one Y axis
     * @return The boolean
     */
    public boolean getYMovePossibility() {return (this.canMoveUp || this.canMoveDown);}

    /**
     * Sets the possibility to move up to the set value
     * @param value The value to be set
     */
    public void setMoveUpPossibility(boolean value) {this.canMoveUp = value;}

    /**
     * Sets the possibility to move down to the set value
     * @param value The value to be set
     */
    public void setMoveDownPossibility(boolean value) {this.canMoveDown = value;}

    /**
     * Sets the skinColor attributes to the passed value
     * @param color The color to be set
     */
    public void setSkinColor(String color)
    {
        if(color.equals("GREEN") || color.equals("RED") || color.equals("BLACK") || color.equals("BLUE"))
            this.skinColor = color ;
        // else we don't change the color
    }

    /**
     * Increments the number of entity killed
     */
    public void updateKills() {
        this.enemiesKilled ++ ;
    }

    /**
     * Increments the number of coins collected
     */
    public void updateCoinsCollected() {this.coinsCollected ++ ;}

    /**
     * Overrides the move method from the superclass to handle player's spaceship movement.
     * The player's spaceship can move freely based on keyboard input (LEFT, RIGHT keys).
     */
    @Override
    public void move()
    {
        isNotExceedingBoundaryRight = this.getXPosition()+(this.getMoveMultiplier()*10) <= Global.widgetWidth - this.getEntityWidth()/3;
        isNotExceedingBoundaryLeft =  this.getXPosition()-(this.getMoveMultiplier()*10) >= 0 - this.getEntityWidth()/1.5;

        //Ensure that the player is restrained to moving back to its base position when it loses the movementBonus
        if(this.canMoveUp)
            this.isNotExceedingBoundaryUp = this.getYPosition()-(this.getMoveMultiplier()*10) >= 0 - this.getEntityHeight()/1.5;
        else
            this.isNotExceedingBoundaryUp = this.getYPosition()-(this.getMoveMultiplier()*10) > Global.widgetHeight - 100 - this.getEntityHeight();

        if(this.canMoveDown)
            this.isNotExceedingBoundaryDown = this.getYPosition()+(this.getMoveMultiplier()*10) <= Global.widgetHeight - this.getEntityHeight();
        else
            this.isNotExceedingBoundaryDown = this.getYPosition()+(this.getMoveMultiplier()*10) < Global.widgetHeight - 100 - this.getEntityHeight();

        this.listener();
        if (this.isMovingLeft && this.isNotExceedingBoundaryLeft) { this.moveEntityLeft();}
        if (this.isMovingRight && this.isNotExceedingBoundaryRight) { this.moveEntityRight();}
        if (this.isMovingUp && this.isNotExceedingBoundaryUp) {this.moveEntityUp();}
        if (this.isMovingDown && this.isNotExceedingBoundaryDown) {this.moveEntityDown();}
    }

    /**
     * Adds the current weapon to the past weapons list.
     */
    public void addPastWeapon()
    {
        this.pastWeapon.add(this.getWeapon());
    }

    /**
     * Overrides the shoot method from the superclass to handle the player's shoot possibility.
     * The player's spaceship use it's weapon based on the key pressed.
     */
    @Override
    public void shoot()
    {
        this.listener();

        if (this.isShooting && this.getWeapon().getClock().isComplete())
        {
            this.getWeapon().getClock().startClock();
            this.getWeapon().shoot();
        }
    }

    /**
     * The effect on death
     * @param group The JavaFX Group representing the root of the scene.
     */
    @Override
    public void deathEffect(Group group)
    {
        Global.log.write(this + " is dead");
    }

    /**
     * Add a new bonus the list of bonus
     * @param newBonus The new Temporary bonus
     */
    public void addBonus(TemporaryBonus newBonus)
    {
        this.bonusList.add(newBonus);
    }

    /**
     * Checks if the player hold a bonus of the same type of the passed one
     * @param bonus The bonus to check existence
     * @return A boolean that is true if the bonus is held by the player
     */
    public boolean isInBonuses(TemporaryBonus bonus)
    {
        for(TemporaryBonus b : this.bonusList)
        {
            if( bonus.equals(b)) return true ;
        }
        return false ;
    }

    /**
     * Return the active version of the passed bonus held by the player
     * @param bonus The bonus to check
     * @return The real bonus held by the player
     */
    public TemporaryBonus getAlreadyActiveBonus(TemporaryBonus bonus)
    {
        for(TemporaryBonus b : this.bonusList)
        {
            if(bonus.getClass() == b.getClass())
                return b;
        }
        Global.log.write("Call to getAlreadyActiveBonus when existence is not tested");
        return null;
    }

    /**
     * Decrease the displayed healthBar
     * @param hp The hp of the player
     */
    public void decreasingHealthBar(int hp)
    {
        double decreasingBarValue = this.initialBarHeight /((double) this.initialHelathPoints /hp) ;
        this.healthBar.setHeight(this.healthBar.getHeight()-decreasingBarValue);
    }

    /**
     * Increase the displayed healthBar
     * @param hp The hp of the player
     */
    public void increasingHealthBar(int hp)
    {
        double increasingBarValue = this.initialBarHeight / ((double) this.initialHelathPoints /hp)  ;
        this.healthBar.setHeight(this.healthBar.getHeight()+increasingBarValue);
    }

    /**
     * Overrides the evolve method from the superclass. Contains every action of the Player at each frame
     * @param root Group Object used the display the object and the bullet if they exist
     */
    @Override
    public void evolve(Group root)
    {
        this.scoreText.setY(Global.getPlayer().getYPosition());
        this.scoreText.setX(Global.getPlayer().getXPosition()+50);
        this.move();
        this.onCollisionEnter(root);
        if(!this.getTouchedStatus()) {
            if (this.getWeapon() != null) {
                this.shoot();
                this.getWeapon().evolve(root);
                for(Weapon e : this.pastWeapon)
                    e.evolve(root);
            }
        }
        this.bonusList.removeIf(e -> !e.isActive());
        this.pastWeapon.removeIf(e -> !e.hasActiveBullets());

    }

    /**
     * Displays various elements of the player, including the health bar and score text.
     * @param root The root group to display elements.
     */
    @Override
    public void display(Group root)
    {
        this.getWeapon().display(root);
        root.getChildren().remove(this.displayEntity());
        root.getChildren().add(this.displayEntity());
        root.getChildren().remove(this.displayHitBox());
        root.getChildren().add(this.displayHitBox());
        root.getChildren().remove(this.healthBar);
        root.getChildren().add(this.healthBar);
        root.getChildren().remove(this.healthBarCanva);
        root.getChildren().add(this.healthBarCanva);
        root.getChildren().remove(scoreText);
        root.getChildren().add(scoreText);
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return "Player";
    }
}
