package com.example.poo.utils;

import com.example.poo.spaceEntity.Entity;
import com.example.poo.spaceEntity.Player;
import com.example.poo.spaceEntity.SpaceEntity;
import com.example.poo.weapons_bullets.BulletEntity;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The class that handle what is displayed on screen and a vast majority of the games logic
 */
public class ViewManager
{
    private Group root;
    private final Stage mainStage;
    private WaveParser parser;
    private final double menuHeight = 800;
    private final double menuWidth = 800;
    private final Image menuBack = new Image("gameBackground.png", menuWidth, menuHeight, false, false);
    private final Image gameBack = new Image("gameBackground.png",Global.widgetHeight*1.80, Global.widgetWidth*2,false,false);
    private ImageView gameBackground,menuBackground;
    private final ArrayList<ImageView> ships = new ArrayList<>();
    private final Text score = new Text();
    private Text confirmation,no,yes,refusal;
    private Text ownedMoney;
    private final String shipsSaveFile = "src/main/java/com/example/poo/textData/saveShips.txt";
    private final String highScoreFile = "src/main/java/com/example/poo/textData/highScore";
    private final String highScorePlayerFile = "src/main/java/com/example/poo/textData/highScorrer";
    private final String moneyFIle = "src/main/java/com/example/poo/textData/money.txt";
    private final String waveDataFile = "src/main/java/com/example/poo/textData/WaveData.txt";
    private final String companyFont = "file:src/main/resources/fonts/Action-Comics.ttf";
    //private Font gameFont = Font.loadFont("file:src/main/resources/fonts/Pixellari.ttf",20);
    private String playerShip = "playerGreenShip.png";
    private String playerName;
    private final String mainFont = "file:src/main/resources/fonts/ThaleahFat.ttf";
    private final int highScore = readingIntValuesFromFiles(highScoreFile);
    private final Image icon = new Image("player.png");
    private final Image redShipImg = new Image("playerRedShip.png");
    private final Image blueShipImg = new Image("playerBlueShip.png");
    private final Image greenShipImg = new Image("playerGreenShip.png");
    private final Image coinImage = new Image( "coin_anim00.png");
    private final Image gunnerImg = new Image("GalacticGunner.png");
    private final Image pauseImg = new Image("pause.png");
    private final Image startImg = new Image("start.png");
    private final Image shipText = new Image("shipText.png");
    private final Image bullet = new Image("bulletRed.png");
    private Rectangle selectedShip;
    private boolean isRunning = true;
    public static boolean isPaused = false;

    /**
     * Constructor for the ViewManager class.
     * It sets up the game's graphical interface, loads the WaveData.txt file, and prepares the main menu scene.
     */
    public ViewManager()
    {
        this.root = new Group();
        this.mainStage = new Stage();
        this.mainStage.setTitle("G4L4KT1K 5H00T3R");
        try
        {
            this.mainStage.getIcons().add(icon);
        } catch (Exception e)
        {
            System.out.println("Error loading icon's image : " + e.getMessage());
            Global.log.write("Error loading icon's image : " + e.getMessage());
        }

        try {
            this.parser = new WaveParser(waveDataFile);
        } catch (Exception e)
        {
            System.out.println("Error loading WaveData.txt file : " + e.getMessage());
            Global.log.write("Error loading WaveData.txt file : " + e.getMessage());
        }
    }

    /**
     * Writes a score or text content to a file at the specified filePath.
     *
     * @param filePath   The path to the file where the content will be written.
     * @param score      The score or text content to be written to the file.
     * @param canAppend  A boolean indicating whether the content should be appended to an existing file (true) or overwrite it (false).
     */
    public  void writingFiles(String filePath, String score, boolean canAppend) {
        try (FileWriter fileWriter = new FileWriter((filePath),canAppend))
        {
            fileWriter.write(score);
            //if canAppend is true then we write in the next line so we need a separator
            if (canAppend) {fileWriter.write(System.lineSeparator());}
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    /**
     * Reads an integer value from a file located at the specified filePath.
     *
     * @param filePath The path to the file from which the integer value will be read.
     * @return The integer value read from the file, or 0 if the file is empty or -1 if an error occurs during reading.
     */
    public int readingIntValuesFromFiles(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line = bufferedReader.readLine();
             return (line != null) ? Integer.parseInt(line) : 0;
        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Reads a string value from a file located at the specified filePath.
     *
     * @param filePath The path to the file from which the string value will be read.
     * @return The string value read from the file, or "NO HIGH SCORER" if the file is empty, or
     *         "ERROR READING VALUE FROM FILE" if an error occurs during reading.
     */
    public String radingHighScoreOwnerFromFile(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line = bufferedReader.readLine();
            return (line != null) ? line : "NO HIGH SCORRER";
        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
            return "ERROR READING VALUE FROM FILE";
        }
    }

    /**
     * Sets the attributes for a JavaFX Text node, such as text content, position, color, font, and font size.
     *
     * @param txtNode    The Text node to which attributes will be applied.
     * @param textContent The content to be set for the Text node.
     * @param x           The x-coordinate position of the Text node.
     * @param y           The y-coordinate position of the Text node.
     * @param color       The color to fill the Text node.
     * @param fontPath    The file path of the font to be used for the Text node.
     * @param fontSize    The size of the font for the Text node.
     */
    private void setTextAttributs(Text txtNode,String textContent,double x,double y,Color color,String fontPath,double fontSize)
    {
        txtNode.setText(textContent);
        txtNode.setX(x);
        txtNode.setY(y);
        txtNode.setFill(color);
        txtNode.setFont(Font.loadFont(fontPath, fontSize));
    }

    /**
     * Sets the attributes for a JavaFX ImageView, such as the image source, and position.
     *
     * @param imgv The ImageView to which attributes will be applied.
     * @param img  The Image to set as the source for the ImageView.
     * @param x    The x-coordinate position of the ImageView.
     * @param y    The y-coordinate position of the ImageView.
     */
    private void setImageViewAttributs(ImageView imgv,Image img,double x,double y)
    {
        imgv.setImage(img);
        imgv.setX(x);
        imgv.setY(y);
    }

    /**
     * An overloading of setImageViewAttrivutes , adding to it width and height.
     *
     * @param imgv   The ImageView to which attributes will be applied.
     * @param img    The Image to set as the source for the ImageView.
     * @param x      The x-coordinate position of the ImageView.
     * @param y      The y-coordinate position of the ImageView.
     * @param width  The width to set for the ImageView.
     * @param height The height to set for the ImageView.
     */
    private void setImageViewAttributs(ImageView imgv,Image img,double x,double y,double width,double height)
    {
        setImageViewAttributs(imgv,img,x,y);
        imgv.setFitWidth(width);
        imgv.setFitHeight(height);
    }

    /**
     * Extracts the file name from a given full path by locating the last occurrence of '/'.
     *
     * @param fullPath The full path from which the file name will be extracted.
     * @return The extracted file name, or the original full path if no '/' is found.
     */
    private String extractFileName(String fullPath) {
        int lastSlashIndex = fullPath.lastIndexOf('/');
        return (lastSlashIndex != -1) ? fullPath.substring(lastSlashIndex + 1) : fullPath;
    }

    /**
     * Adds an element to a file if the element is not already present in the file.
     *
     * @param filePath The path to the file where the element will be added.
     * @param element  The element to be added to the file.
     */
    public void addElementToFile(String filePath, String element) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Check if the element is already in the file
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(element)) {
                    System.out.println("Element already exists in the file.");
                    return; // Exit the function if the element is already present
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        writingFiles(filePath,element,true);
    }

    /**
     * Updates the score displayed periodically using a JavaFX Timeline.
     * For demonstration purposes, the score is updated every millisecond.
     * In a real application, you might update the score based on user interactions or events.
     */
    private void updateScorePeriodically() {
        // In a real application, you might update the highScore based on user interactions or some events
        // For demonstration, let's update the highScore every second using a Timeline
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(1),
                        event -> {
                            score.setText("Score: " + Global.score);
                            score.setFont(Font.loadFont("file:src/main/resources/fonts/ThaleahFat.ttf",35));
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Updates the money value in a file.
     */
    private void updateMoney() {writingFiles(moneyFIle,Integer.toString(Global.money),false);}

    /**
     * This is the mainGameScene, it is where the game takes place, the loop of the game is here and is updated constantly using the AnimationTimer
     */
    public void mainGameScene()
    {
        // Setting the resolution of the screen
        Scene gameScene = new Scene(root,Global.widgetWidth,Global.widgetHeight);

        // Setting background image
        try {
            gameBackground = new ImageView(gameBack);
            setImageViewAttributs(gameBackground,gameBack,0,-Global.widgetHeight);
            root.getChildren().add(gameBackground);
        } catch (Exception e) {
            System.out.println("Error loading Background's image " + e.getMessage());
            Global.log.write("Error loading Background's image " + e.getMessage());
        }

        //score display on top of the health bar
        root.getChildren().add(score);
        score.setX(40);
        score.setY(320);
        score.setFill(Color.WHITE);


        updateScorePeriodically();

        //pause and start buttons
        try {
            ImageView pause = new ImageView();
            setImageViewAttributs(pause,pauseImg,Global.widgetWidth-200,Global.widgetHeight-(Global.widgetHeight - 240));

            root.getChildren().add(pause);

            pause.setOnMouseClicked(mouseEvent -> isPaused = true);
        } catch (Exception e)
        {
            System.out.println("Error loading pause button's image " + e.getMessage());
            Global.log.write("Error loading pause button's image " + e.getMessage());
        }


        try {
            ImageView start = new ImageView();
            setImageViewAttributs(start,startImg,Global.widgetWidth-200,Global.widgetHeight-(Global.widgetHeight - 300));

            root.getChildren().add(start);

            start.setOnMouseClicked(mouseEvent -> isPaused = false);
        } catch (Exception e)
        {
            System.out.println("Error loading start button's image " + e.getMessage());
            Global.log.write("Error loading start button's image " + e.getMessage());
        }

        this.mainStage.setResizable(true);
        this.mainStage.setMaximized(true);
        this.mainStage.setScene(gameScene);
        this.mainStage.show();

        // Managing Player's creation
        try {
            Global.entities.add(new Player(playerShip,gameScene, 1));
        } catch (Exception e) {
            System.out.println("Error loading Player's image: " + e.getMessage());
            Global.log.write("Error loading Player's image: " + e.getMessage());
        }


        // Creation of discrete timer so the entities can evolve
        AnimationTimer gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                if (isRunning)
                {

                    if (!isPaused)
                    {
                        //Testing collisions for each entity
                        for (Entity ent : Global.entities) {
                            ent.evolve(root);
                            ent.display(root);
                        }
                        /*
                         * Here we use an Iterator, so we can update and modify the entities Array while the loop is still being used, it allows us to remove and deleting
                         * objects instantly without needing to mark them and delete them at the end of the iteration.
                         */
                        Iterator<Entity> it = Global.entities.iterator();
                        while(it.hasNext())
                        {
                            Entity current = it.next();
                            if (current.hasDeadStatus())
                            {
                                if (current instanceof SpaceEntity) {
                                    // if the SpaceEntity has a weapon we delete all the bullets on the screen, so they don't freeze in the screen.
                                    if (((SpaceEntity) current).getWeapon() != null) {
                                        for (BulletEntity b :((SpaceEntity) current).getWeapon().getBullets()) {
                                            root.getChildren().remove(b.displayEntity());
                                            root.getChildren().remove(b.displayHitBox());
                                        }
                                    }
                                }
                                root.getChildren().remove(current.displayEntity());
                                root.getChildren().remove(current.displayHitBox());

                                if(current instanceof Player) {
                                    updateMoney();
                                    root.getChildren().remove(((Player) current).getScoreText());
                                    deathMenu();
                                }
                                it.remove();
                            }
                        }
                        try {
                            gameBackground.setY(gameBackground.getY() + 0.6); // speed of the background movement
                            if (gameBackground.getY() >= 0) {
                                gameBackground.setY(-Global.widgetHeight * 1.777);
                            } // Here we make the background looping, making the game look endless. 1.777 is used so the loop is perfect.
                        }
                        catch (Exception e)
                        {
                            System.out.println("Error moving background's image: " + e.getMessage());
                            Global.log.write("Error moving background's image: " + e.getMessage());
                        }


                        //If there is timedWave to come, check if they can be added now
                        Global.timedEntities.removeIf(TimedWave::needToEnter);

                        //If the only entity left is the player and there is no more collectibles to come
                        //The parser get the next Wave in the Global.waitingEntities ArrayList
                        if((Global.entities.size() == 1 && Global.entities.stream().anyMatch(e -> e instanceof Player)) && Global.waitingEntities.isEmpty() && Global.timedEntities.isEmpty() && !parser.getEOFStatus())
                            try {
                                parser.processLineByLine();
                            } catch (Exception e)
                            {
                                System.out.println("Error while loading WaveData.txt file : " + e.getMessage());
                                Global.log.write("Error while loading WaveData.txt file : " + e.getMessage());
                                // e.printStackTrace(); call this if you want to know the specific file that is not working
                            }

                        if((Global.entities.size() == 1 && Global.entities.stream().anyMatch(e -> e instanceof Player)) && Global.waitingEntities.isEmpty() && Global.timedEntities.isEmpty() && parser.getEOFStatus())
                            winMenu();

                        // Here we add all the entities that are going to be in the entities array
                        Global.entities.addAll(Global.waitingEntities);
                        Global.waitingEntities.clear(); // Clearing the waitingEntities, so it doesn't fill permanently the entities array
                    }
                }
            }
        };

        // the start() method call consistently the handle method, so the game logic is constantly updated
        gameLoop.start();
    }

    // MAIN MENU PART

    /**
     * Handles the transactions that occur in the ship shop (buying ships and choosing the one to play with )
     *
     * @param ship The ImageView representing the ship in the graphical user interface.
     * @param shipCost The Text object displaying the cost or ownership status of the ship.
     * @param shipCoin The ImageView representing the coin associated with the ship.
     */
    private void transaction(ImageView ship,Text shipCost,ImageView shipCoin)
    {

        // checking if we already own a ship
        String shipUrl = extractFileName(ship.getImage().getUrl());

        boolean alreadyOwned = false;

        Iterator<ImageView> iterator = ships.iterator();

        while (iterator.hasNext()) {
            ImageView sh = iterator.next();

            if (extractFileName(sh.getImage().getUrl()).equals(shipUrl)) { // check if the image url is already a ship's url in ships (meaning it's owned)

                shipCost.setText("OWNED");
                shipCost.setX(shipCost.getX()-17);

                iterator.remove();
                root.getChildren().remove(shipCoin);
                alreadyOwned = true;
            }
        }


        if (alreadyOwned){ships.add(ship);}

        ship.setOnMouseClicked(MouseEvent -> {
            root.getChildren().removeAll(confirmation,yes,no,refusal,selectedShip);

            if (ships.contains(ship))
            {

                playerShip = extractFileName(ship.getImage().getUrl()); // to set the ship's skin we want to play with

                // to show which ship is selected
                selectedShip = new Rectangle(ship.getX()-5,ship.getY(),85,85);
                selectedShip.setFill(Color.TRANSPARENT);
                selectedShip.setStroke(Color.WHITE);
                root.getChildren().add(selectedShip);

            }
            else
            {
                // buying offer

                confirmation = new Text();
                setTextAttributs(confirmation,"ARE YOU SURE YOU WANT TO BUY THIS ?",menuWidth/2-260,270,Color.WHITE,mainFont,35);

                yes = new Text("YES");
                setTextAttributs(yes,"YES",menuWidth/2 - 60,300,Color.GREEN,mainFont,35);

                no = new Text("NO");
                setTextAttributs(no,"NO",menuWidth/2 +30,300,Color.RED,mainFont,35 );

                root.getChildren().addAll(confirmation,yes,no);

                yes.setOnMouseClicked(event ->
                {
                    if (Global.money < Integer.parseInt(shipCost.getText()))
                    {
                        root.getChildren().removeAll(yes,no,confirmation);
                        refusal = new Text();
                        setTextAttributs(refusal,"YOU DON'T HAVE ENOUGH MONEY TO BUY THIS SHIP !",menuWidth/2 - 340,270,Color.WHITE,mainFont,35);
                        root.getChildren().add(refusal);

                        KeyFrame refusalFrame = new KeyFrame(Duration.seconds(1),actionEvent -> root.getChildren().remove(refusal)); // delay of 1 sec to delete the msg

                        Timeline tl = new Timeline(refusalFrame);
                        tl.setCycleCount(1);
                        tl.play();
                    }
                    else if (Global.money >= Integer.parseInt(shipCost.getText()))
                    {
                        root.getChildren().removeAll(yes,no,confirmation,shipCoin);

                        Global.money -= Integer.parseInt(shipCost.getText());

                        shipCost.setX(shipCost.getX()-17);
                        shipCost.setText("OWNED");

                        ships.add(ship);

                        ownedMoney.setText(""+Global.money);//the money on the top left corner of the screen

                        updateMoney();
                        addElementToFile(shipsSaveFile,extractFileName(ship.getImage().getUrl()));
                    }
                });
                no.setOnMouseClicked(event -> root.getChildren().removeAll(yes,no,confirmation));
            }

        });
    }

    /**
     * Used to manage the high score node
     * Updates the high score , and the high score owner if the high score is beaten
     */
    private void highScore()
    {

        if (Global.score > highScore)
        {
            writingFiles(highScoreFile, Integer.toString(Global.score),false);
            writingFiles(highScorePlayerFile,playerName,false);
            Text newHighScore = new Text();
            setTextAttributs(newHighScore,"SIUUU NEW HIGH SCORE : "+Global.score,Global.widgetWidth/2 - 235,Global.widgetHeight/2+ 80,Color.RED,mainFont,50);
            root.getChildren().add(newHighScore);
        }
    }


    /**
     * Represents the scene for managing ship selection and purchases within the game.
     *
     * @throws Exception If an exception occurs during the execution, it is printed, helping identify any issues.
     */
    private void shipStoreScene()
    {
        try
        {
            Scene shipStore = new Scene(root,menuWidth,menuHeight);
            menuBackground = new ImageView(menuBack);
            this.root.getChildren().add(menuBackground);

            displayOwnedMoney();

            ImageView selectShip = new ImageView();
            setImageViewAttributs(selectShip,shipText,menuWidth/2 - 100,50);


            ImageView blueShip = new ImageView();
            setImageViewAttributs(blueShip,blueShipImg,350,401);

            ImageView redShip = new ImageView();
            setImageViewAttributs(redShip,redShipImg,550,401);

            ImageView greenShip = new ImageView();
            setImageViewAttributs(greenShip,greenShipImg,150,401);

            // coins beneath the ships
            ImageView blueShipCoin = new ImageView();
            setImageViewAttributs(blueShipCoin,coinImage,397,485.5,30,30);

            ImageView redShipCoin = new ImageView();
            setImageViewAttributs(redShipCoin,coinImage,597,485.5,30,30);

            //text or price beneath the ships
            Text blueShipCost = new Text();
            setTextAttributs(blueShipCost,"30",362,509,Color.WHITE,mainFont,35);

            Text redShipCost = new Text();
            setTextAttributs(redShipCost,"60",562,509,Color.WHITE,mainFont,35);

            Text greenShipCost = new Text();
            setTextAttributs(greenShipCost,"OWNED",147,509,Color.WHITE,mainFont,35);

            // to start the game from the shop
            GameButton goToStart = new GameButton("PLAY");
            goToStart.setLayoutX(menuWidth/2-goToStart.getWidth()-100);
            goToStart.setLayoutY(600);
            goToStart.setOnAction(actionEvent -> {
                root = new Group();
                mainGameScene();
            });

            root.getChildren().addAll(blueShip,redShip,greenShip,selectShip,blueShipCost,redShipCost,greenShipCost,blueShipCoin,redShipCoin,goToStart);

            greenShip.setOnMouseClicked(MouseEvent ->
            {
                root.getChildren().remove(selectedShip);
                selectedShip = new Rectangle(greenShip.getX()-5,greenShip.getY(),85,85);
                selectedShip.setFill(Color.TRANSPARENT);
                selectedShip.setStroke(Color.WHITE);
                root.getChildren().add(selectedShip);
                playerShip = "playerGreenShip.png";
            });



            try (BufferedReader reader = new BufferedReader(new FileReader(shipsSaveFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ships.add(new ImageView(line)); // we get the images urls of the ships that we bought and store them in ships so that we can verify if we own them.
                }
            } catch (FileNotFoundException e) {
                System.err.println("Le fichier n'existe pas : " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            }

            transaction(blueShip,blueShipCost,blueShipCoin);
            transaction(redShip,redShipCost,redShipCoin);


            mainStage.setScene(shipStore);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Represents the game over menu displayed when the player win the game.
     * Displays the money gained and the number of killed enemies
     * @throws Exception If an exception occurs during the execution, it is printed, helping identify any issues.
     */
    private void winMenu()
    {
        isRunning = false;
        highScore();

        Text youWin = new Text("YOU LOSE");
        setTextAttributs(youWin,"YOU LOSE",Global.widgetWidth/2 - 210,Global.widgetHeight/2-50,Color.WHITE,mainFont,100);

        root.getChildren().add(youWin);

        try{
            // amounts
            Text enemies = new Text();
            setTextAttributs(enemies,"- Enemies killed : " + Global.getPlayer().getEnemiesKilled(),youWin.getX()+50,youWin.getY()+80,Color.WHITE,mainFont,50);

            Text coins = new Text();
            setTextAttributs(coins,"- Coins collected : " + Global.getPlayer().getCoinsCollected(),youWin.getX() +50,youWin.getY()+50,Color.WHITE,mainFont,50);


            //images
            ImageView totalCoinImage = new ImageView();
            setImageViewAttributs(totalCoinImage,coinImage,coins.getX() - 50,coins.getY() - 25,30,30);

            ImageView totalEnemyImage = new ImageView(gunnerImg);
            setImageViewAttributs(totalEnemyImage,gunnerImg,enemies.getX() - 50,enemies.getY() - 25,30,30);

            root.getChildren().addAll(coins,enemies);
            root.getChildren().addAll(totalCoinImage,totalEnemyImage);

        } catch (Exception e)
        {
            System.out.println("Error while loading winMenu method : " + e.getMessage());
            Global.log.write("Error while loading winMenu method : " + e.getMessage());
        }
    }

    /**
     * Represents the game over menu displayed when the player loses the game.
     * Displays the money gained and the number of killed enemies
     * @throws Exception If an exception occurs during the execution, it is printed, helping identify any issues.
     */
    private void deathMenu()
    {
        isRunning = false;
        highScore();

        // since the YOU LOSE text hasn't an image, we don't put it in the try catch bloc
        Text youLose = new Text("YOU LOSE");
        setTextAttributs(youLose,"YOU LOSE",Global.widgetWidth/2 - 210,Global.widgetHeight/2-50,Color.WHITE,mainFont,100);

        root.getChildren().add(youLose);

        try{
            // amounts
            Text enemies = new Text();
            setTextAttributs(enemies,"- Enemies killed : " + Global.getPlayer().getEnemiesKilled(),youLose.getX()+50,youLose.getY()+80,Color.WHITE,mainFont,50);

            Text coins = new Text();
            setTextAttributs(coins,"- Coins collected : " + Global.getPlayer().getCoinsCollected(),youLose.getX() +50,youLose.getY()+50,Color.WHITE,mainFont,50);


            //images
            ImageView totalCoinImage = new ImageView();
            setImageViewAttributs(totalCoinImage,coinImage,coins.getX() - 50,coins.getY() - 25,30,30);

            ImageView totalEnemyImage = new ImageView(gunnerImg);
            setImageViewAttributs(totalEnemyImage,gunnerImg,enemies.getX() - 50,enemies.getY() - 25,30,30);

            root.getChildren().addAll(coins,enemies);
            root.getChildren().addAll(totalCoinImage,totalEnemyImage);

        } catch (Exception e)
        {
            System.out.println("Error while loading deathMenu method : " + e.getMessage());
            Global.log.write("Error while loading deathMenu method : " + e.getMessage());
        }
    }

    /**
     * Represents the main menu of the game, allowing players to navigate to different sections.
     */
    private void mainMenu()
    {
        ArrayList<GameButton> buttons = new ArrayList<GameButton>();
        GameButton startButton = new GameButton("START");
        GameButton exitButton = new GameButton("EXIT");
        GameButton creditsButton = new GameButton("SHOP");

        int buttonsSpace = 50; // the offset between each button and the one before it on the Y axis

        buttons.add(0,startButton);
        buttons.add(1,creditsButton);
        buttons.add(2,exitButton);

        for (GameButton button : buttons)
        {
            button.setLayoutX(menuWidth/2-button.getWidth()-100);
            button.setLayoutY(480+buttonsSpace);
            buttonsSpace+=50;
            this.root.getChildren().add(button);
        }

        for (GameButton button : buttons)
        {
            button.setOnAction(new EventHandler<>() { // To manage the events when a menu button is clicked
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (button == buttons.get(0)) {
                        root = new Group();
                        mainGameScene();
                    }
                    if (button == buttons.get(1)) {
                        root = new Group();
                        shipStoreScene();
                    }
                    if (button == buttons.get(2)) {
                        Platform.exit();
                    }
                }
            });
        }
    }

    /**
     * Displays the amount of money owned by the player along with a corresponding coin image.
     */
    public void displayOwnedMoney()
    {
        try
        {
            ownedMoney = new Text();
            setTextAttributs(ownedMoney,""+Global.money,50,50,Color.WHITE,mainFont,40);

            ImageView ownedCoins = new ImageView();
            setImageViewAttributs(ownedCoins,coinImage,20,27,30,30);

            root.getChildren().addAll(ownedMoney,ownedCoins);
        }
        catch (Exception e)
        {
            System.out.println("Error loading coin's image: " + e.getMessage());
            Global.log.write("Error loading coin's image: " + e.getMessage());
        }

    }

    /**
     * Creates and sets up a loading scene with visual elements, such as the company name and a loading image.
     */
    private void loadingScene()
    {
        Scene loadingScene = new Scene(root,menuWidth,menuHeight,Color.BLACK);

        Text company = new Text();
        setTextAttributs(company,"SKULULU GAMES",83,200,Color.WHITE,companyFont,35);

        Text copyright = new Text();
        setTextAttributs(copyright,"ALL IMAGES USED IN THIS PROJECT ARE ROYALTY-FREE",menuWidth/2 - 220,780,Color.WHITE,mainFont,20);

        root.getChildren().addAll(company,copyright);

        this.mainStage.setScene(loadingScene);
        this.mainStage.setResizable(false);
        this.mainStage.show();
    }

    /**
     * Initializes and displays the start game scene, including loading visuals and user interaction elements.
     */
    public void startGameScene()
    {
        loadingScene();

        AtomicBoolean isNameSaved = new AtomicBoolean(false);

        // Load the image asynchronously

        KeyFrame menuFrame = new KeyFrame(Duration.millis(1000),actionEvent -> {

            try {
                menuBackground = new ImageView(menuBack);
                this.root.getChildren().add(menuBackground);
            } catch (Exception e) {
                System.out.println("Error loading Menu Background's image: " + e.getMessage());
                Global.log.write("Error loading Menu Background's image: " + e.getMessage());
            }

            // game logo
            Text logoPart1 = new Text();
            setTextAttributs(logoPart1,"GALACTIC",260,50,Color.WHITE,mainFont,75);

            Text logoPart2 = new Text();
            setTextAttributs(logoPart2,"SHOOTER",266,90,Color.WHITE,mainFont,75);

            ImageView playerLogo = new ImageView();
            setImageViewAttributs(playerLogo,icon,250,136);
            playerLogo.setRotate(90);

            ImageView bulletLogo = new ImageView();
            setImageViewAttributs(bulletLogo,bullet,375,136,39,41);
            bulletLogo.setRotate(90);

            ImageView gunnerLogo = new ImageView();
            setImageViewAttributs(gunnerLogo,gunnerImg,500,136,39,41);
            gunnerLogo.setRotate(270);

            this.root.getChildren().addAll(logoPart1,logoPart2,playerLogo,bulletLogo,gunnerLogo);

            Global.money = readingIntValuesFromFiles(moneyFIle);

            Text highScorrer = new Text();
            setTextAttributs(highScorrer,"HIGH SCORE OWNER ",menuWidth/2 - 190,menuHeight/2 - 140,Color.WHITE,mainFont,50);

            Text highScorrerPlayerName = new Text();
            setTextAttributs(highScorrerPlayerName, radingHighScoreOwnerFromFile(highScorePlayerFile),menuWidth/2-90,highScorrer.getY()+40,Color.WHITE,mainFont,30);

            Text highScoreValue = new Text();
            setTextAttributs(highScoreValue,""+readingIntValuesFromFiles(highScoreFile),menuWidth/2-30,highScorrer.getY()+80,Color.WHITE,mainFont,30);

            Text namePlaceHolder = new Text();
            setTextAttributs(namePlaceHolder,"ENTER YOUR NAME",menuWidth/2 - 143,menuWidth/2 - 20,Color.WHITE,mainFont,40);

            TextField enterPlayerName = new TextField();

            enterPlayerName.setLayoutX(menuWidth/2 - 100);
            enterPlayerName.setLayoutY(menuHeight/2);
            enterPlayerName.setFont(Font.loadFont("file:src/main/resources/fonts/ThaleahFat.ttf",25));
            enterPlayerName.setPrefWidth(200);


            enterPlayerName.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    isNameSaved.set(true);
                    playerName = enterPlayerName.getText();
                    enterPlayerName.clear();
                }
            });
            if (!isNameSaved.get()){playerName = "GUEST";}
            root.getChildren().addAll(highScorrer,enterPlayerName,namePlaceHolder,highScorrerPlayerName,highScoreValue);
            displayOwnedMoney();
            mainMenu();


        });
        Timeline tl = new Timeline(menuFrame);
        tl.setCycleCount(1);
        tl.play();
    }
}
