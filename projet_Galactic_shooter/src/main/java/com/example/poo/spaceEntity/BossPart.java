package com.example.poo.spaceEntity;

import com.example.poo.utils.Global;
import com.example.poo.utils.WaveParser;
import javafx.scene.Group;

import java.util.Random;

/**
 * The bossPart represents the boss arm
 */
public class BossPart extends Enemy {
    private Boss user;
    private boolean isRightPart;
    private WaveParser waveCreator;

    /**
     * Constructor for the BossPart class
     * @param imagePath Path to the image file representing the Bosspart entity.
     * @param boss The boss Entity linked to this part
     * @param isRight A boolean that set it to be the right part
     */
    public BossPart(String imagePath, Boss boss, boolean isRight)
    {
        super(imagePath, 0.1, 50, 5, true);
        this.isRightPart = isRight;
        this.user = boss;
        this.setEntitySize(146, 58);
        this.setEntityPos(this.user.getXPosition(), this.user.getYPosition());
        try {
            this.waveCreator = new WaveParser("src/main/java/com/example/poo/textData/BossPartWave.txt");
        } catch (Exception e) {
            System.out.println("Error loading BossPartWave.txt file" + e.getMessage());
            Global.log.write("Error loading BossPartWave.txt file" + e.getMessage());
        }

    }

    /**
     * Method that describe what to do on a part death
     */
    public void actionOnDeath()
    {
        Global.log.write(this + " is dead");
        if(!this.waveCreator.getEOFStatus()) {
            try {
                this.waveCreator.processLineByLine();
            } catch (Exception e) {
                System.out.println("Error while reading BossPartWave.txt file : " + e.getMessage());
                Global.log.write("Error while reading BossPartWave.txt file : " + e.getMessage());
            }
        }
    }

    /**
     * Override the setIsDead method from Entity to add action to the existing definition
     * @param isDead The dead status of the entity.
     */
    @Override
    public void setIsDead(boolean isDead)
    {
        super.setIsDead(isDead);
        if(isDead) {
            this.actionOnDeath();
            this.user.losePart(this.isRightPart);
            this.user = null;
        }
    }

    /**
     * Overrides the shoot method from the superclass to handle the shooting behavior.
     * Shoot an only BasicBullet in front of the entity that act as an AimWeapon bullet because of the BossPart behavior
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
     * Overrides the evolve method from the superclass. Contains every action of the BossPart at each frame
     * @param root Group Object used the display the object and the bullet if they exist
     */
    @Override
    public void evolve(Group root)
    {
        assert Global.getPlayer() != null;
        facePlayer(Global.getPlayer().getXPosition(),Global.getPlayer().getYPosition());
        super.evolve(root);
    }

    /**
     * Override the java method to string
     * This is done to make the log file easier to read
     * @return The String describing the object
     */
    @Override
    public String toString()
    {
        return this.isRightPart ? "Boss Right Part" : "Boss Left Part";
    }
}
