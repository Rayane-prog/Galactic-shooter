package com.example.poo.utils;

import com.example.poo.spaceEntity.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The WaveParser class will be used to create a new Wave at each call with next one contained in the file "WaveData.txt"
 * To see the format of the WaveData file, see the ReadMe
 */
public class WaveParser {
    private Scanner scan;
    private boolean eofReached; //True if the end of the file is reached

    /**
     * Constructor for the Wave Parser
     * @param filepath The filepath to the file containing all the wave data
     */
    public WaveParser(String filepath)
    {
        this.eofReached = false;
        try {
            this.scan = new Scanner(new File(filepath));
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
            Global.log.write("ERROR : " + e.getMessage());
        }
    }

    /**
     * Gets the status of the current file reading
     * @return A boolean that is true if the end of the file has been reached
     */
    public boolean getEOFStatus() {return this.eofReached;}

    /**
     * Method to read all data for the next Wave line by line
     */
    public void processLineByLine()
    {
        Global.log.write("Enter wave file");
        String currentLine;
        while(this.scan.hasNextLine()) {
            currentLine = this.scan.nextLine();
            if(currentLine.equals("START")) {
                Global.log.write("New wave loading");
                continue;
            }

            if(currentLine.equals("END")) {
                Global.log.write("New wave loaded");
                break;
            }

            if(currentLine.equals("ENDF")) {
                Global.log.write("Last wave loaded");
                this.eofReached = true;
                this.scan.close();
                break;
            }

            Global.log.write("Current line is : " + currentLine);
            splitLine(currentLine);
            Global.log.write("END OF LINE");
        }
    }

    /**
     * Method to split the current line in all its element
     * @param currentLine The line currently being treated by the processLineByLine method
     */
    private void splitLine(String currentLine)
    {
        Scanner scanner = new Scanner(currentLine);
        ArrayList<String> lineElement = new ArrayList<String>();

        scanner.useDelimiter("/");
        while(scanner.hasNext()) {
            lineElement.add(scanner.next().trim());
        }

        createContent(lineElement);
    }

    /**
     * Method to create all the entities specified by the WaveData file
     * Add them all to the waitingEntities ArrayList, so they can be added when this wave is finished being prepared
     * @param element The ArrayList containing each element from a single line created in the splitLine method
     */
    private void createContent(ArrayList<String> element)
    {
        boolean error = false;
        double x, y;
        Enemy waveElement = null;
        TimedWave futureWave = null;
        Enemy bossRight = null;
        Enemy bossLeft = null;

        switch (element.get(0)) {
            case "GalacticGunner":
                waveElement = new GalacticGunner( 0.5);
                if(element.get(1).equals("MIDDLE")) {
                    x = Global.widgetWidth/2;
                } else { x = Double.valueOf(element.get(1));}

                if(element.get(2).equals("MIDDLE")) {
                    y = Global.widgetHeight/2;
                } else { y = Double.valueOf(element.get(2));}
                waveElement.setEntityPos(x, y);
                ((GalacticGunner) waveElement).setDirection((waveElement.getXPosition() < Global.widgetWidth / 2));
                break;

            case "CosmicCharger":
                if(element.get(1).equals("MIDDLE")) {
                    x = Global.widgetWidth/2;
                } else { x = Double.valueOf(element.get(1));}

                if(element.get(2).equals("MIDDLE")) {
                    y = Global.widgetHeight/2;
                } else { y = Double.valueOf(element.get(2));}
                waveElement = new CosmicCharger( 1.2,x, y);
                break;

            case "LongShooter":
                waveElement = new LongShooter();
                if(element.get(1).equals("MIDDLE")) {
                    x = Global.widgetWidth/2;
                } else { x = Double.valueOf(element.get(1));}

                if(element.get(2).equals("MIDDLE")) {
                    y = Global.widgetHeight/2;
                } else { y = Double.valueOf(element.get(2));}
                waveElement.setEntityPos(x, y);
                break;

            case "Asteroid":
                // Movement speed and size of the Asteroid are random
                Random r = new Random();
                double randomMovementSpeed = r.nextDouble(0.35)+0.05;
                int randomSize = r.nextInt(150)+20;

                waveElement = new Asteroid(randomMovementSpeed);
                if(element.get(1).equals("MIDDLE")) {
                    x = Global.widgetWidth/2;
                } else { x = Double.valueOf(element.get(1));}

                if(element.get(2).equals("MIDDLE")) {
                    y = Global.widgetHeight/2;
                } else { y = Double.valueOf(element.get(2));}
                waveElement.setEntityPos(x, y);
                waveElement.setEntitySize(randomSize,randomSize);
                ((Asteroid) waveElement).setDirection((waveElement.getXPosition() <= Global.widgetWidth / 2));
                break;

            case "Boss":
                if(element.get(1).equals("MIDDLE")) {
                    x = Global.widgetWidth/2;
                } else { x = Double.valueOf(element.get(1));}

                if(element.get(2).equals("MIDDLE")) {
                    y = Global.widgetHeight/2;
                } else { y = Double.valueOf(element.get(2));}

                waveElement = new Boss("Boss.png", x, y);
                bossRight = new BossPart("BossRight.png", (Boss)waveElement, true);
                bossLeft = new BossPart("BossLeft.png", (Boss)waveElement, false);
                ((Boss) waveElement).addPart((BossPart) bossRight, true);
                ((Boss) waveElement).addPart((BossPart) bossLeft, false);
                break;

            default:
                error = true;
                Global.log.write("First element of line is not a usable element");
                break;
        }

        //Don't enter if the line is not valid
        if(!error)
            //If the second last parameter is T, it means that it's a timed enemy
            if(element.size() > 3) {
                if(element.get(element.size() - 2).equals("T")) {

                    //Checks if a wave with the same waiting time exists
                    for (TimedWave e : Global.timedEntities) {
                        if(e.getTime() == Double.valueOf(element.get(element.size() - 1))) {
                            futureWave = e;
                            e.addEnemy(waveElement);
                            if(waveElement instanceof Boss) {
                                e.addEnemy(bossRight);
                                e.addEnemy(bossLeft);
                            }
                            Global.log.write(e + " added to existing timed wave");
                        }
                    }
                    //If not create the wave and add the enemy to it
                    if (futureWave == null) {
                        Global.log.write("New timed Wave created in parser");
                        futureWave = new TimedWave(Double.valueOf(element.get(element.size() - 1)));
                        futureWave.addEnemy(waveElement);
                        if(waveElement instanceof Boss) {
                            futureWave.addEnemy(bossRight);
                            futureWave.addEnemy(bossLeft);
                        }
                        Global.timedEntities.add(futureWave);
                        Global.log.write(waveElement + " added to new timed wave");
                    }
                }
            } else {
                //Add the enemy if it is not in a timed wave
                Global.waitingEntities.add(waveElement);
                if(waveElement instanceof Boss) {
                    Global.waitingEntities.add(bossLeft);
                    Global.waitingEntities.add(bossRight);
                }
            }
    }
}