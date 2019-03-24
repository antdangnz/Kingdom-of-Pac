package view.scenecontrol.ingameui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import view.effects.FadeScene;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameScreen extends Parent {

    @FXML private AnchorPane anchorPane;
    @FXML private Group gameZone;
    @FXML private Group startMessage;
    @FXML private Group levelCompleteDialogue;
    @FXML private Group gameOverDialog;
    @FXML private Group escapeDialog;
    @FXML private Group pauseDialog;

    @FXML private Button yesButton;
    @FXML private Button noButton;
    @FXML private Button restartButton;
    @FXML private Button endToHSButton;

    @FXML private ImageView lifeImage1;
    @FXML private ImageView lifeImage2;
    @FXML private ImageView lifeImage3;

    @FXML private Label levelLabel;
    @FXML private Label scoreLabel;
    @FXML private Label minsDisplay;
    @FXML private Label secondsDisplay;
    @FXML private Label gameOverLabel;
    @FXML private Label levelCompleteLabel;
    @FXML private Label gamePausedLabel;
    @FXML private Label startTimerLabel;

    @FXML private Label swordPacLabel;
    @FXML private Label iceSpellLabel;
    @FXML private Label swiftRunLabel;


    private Hero heroPlayer;
    private List<Enemy> enemyList;

    private Maze maze;

    private SimpleIntegerProperty level;

    private boolean gamePaused = false;
    private boolean escapePressed = false;
    private boolean pPressed = false;
    private boolean gameStarted = false;
    private boolean gameOver = false;

    private Timeline timerTimeline;
    private Timeline flashingGameOver;
    private Timeline flashingPause;
    private Timeline enemySpawning;

    private int timerCounter, playableEnemies;
    private SimpleIntegerProperty timerMins;
    private SimpleIntegerProperty timerSecs;

    private AtomicInteger enemyIndexForSpawn;

    private List<String> saveData;

    private SoundEffects soundFX;

    public GameScreen() {
        this.soundFX = new SoundEffects();

        this.playableEnemies = 0;

        //Initialize the timer display values
        timerCounter = 120;
        timerMins = new SimpleIntegerProperty(2);
        timerSecs = new SimpleIntegerProperty(0);

        level = new SimpleIntegerProperty(1);

        enemyIndexForSpawn = new AtomicInteger(1);

        saveData = new ArrayList<>();
        enemyList = new ArrayList<>();
    }

    @FXML
    public void initialize() throws IOException, URISyntaxException {
        //Fade animations for entering the game screen
        anchorPane.setOpacity(0);
        FadeScene.fadeIntoGame(anchorPane);

        maze = new Maze(this.gameZone);
        //Give the maze the gameZone node

        //Setting which character to use from character select
        File saveToLoad = new File("view/resources/character.txt");
        saveData = Files.readAllLines(Paths.get(saveToLoad.getAbsolutePath()), StandardCharsets.UTF_8);
//        saveData = Files.readAllLines(Paths.get(getClass().getResource(saveToLoad.toString()).toURI()), StandardCharsets.UTF_8);
        String character = saveData.get(0);
        this.playableEnemies = Character.getNumericValue(saveData.get(1).charAt(0));

        String heroName = "";
        if (this.playableEnemies == 0) {
            heroName = saveData.get(2);
        }
//        String heroName = saveData.get(2);


        Image[][] heroSprites = setSprites(character);

        if (heroPlayer == null) {
            heroPlayer = new Hero(heroName,
                    createCharacterNode(MazeData.getPlayerStartingX(), MazeData.getPlayerStartingY()),
                    heroSprites);
        }


        for (int i = 0; i < 4 - this.playableEnemies; i++) {
            Enemy enemy = new Enemy(
                    heroPlayer,
                    createEnemyNode(MazeData.getEnemyPositionX(i),
                            MazeData.getEnemyPositionY(i)),
                    i,
                    false);

            enemyList.add(enemy);
        }


        if (this.playableEnemies > 0) {
            Enemy enemy, enemy2;

            if (this.playableEnemies == 1) {
                enemy = new Enemy(
                        heroPlayer,
                        createEnemyNode(MazeData.getEnemyPositionX(3),
                                MazeData.getEnemyPositionY(3)),
                        3,
                        true);
                enemyList.add(enemy);
            } else if (this.playableEnemies == 2) {
                enemy = new Enemy(
                        heroPlayer,
                        createEnemyNode(MazeData.getEnemyPositionX(2),
                                MazeData.getEnemyPositionY(2)),
                        2,
                        true);

                enemyList.add(enemy);

                enemy2 = new Enemy(
                        heroPlayer,
                        createEnemyNode(MazeData.getEnemyPositionX(3),
                                MazeData.getEnemyPositionY(3)),
                        3,
                        true);
                enemyList.add(enemy2);
            }

        }

        livesDisplay();
        levelDisplay();
        scoreDisplay();
        setLevelCompleteDialogue();
        setTimerDisplay();
        createTimeLines();
    }

    private void createTimeLines() {
        KeyFrame timerKeyF = new KeyFrame(Duration.millis(1000), event -> {
            timerCounter--;
            if (timerCounter <= 120 && timerCounter >= 0) {
                timerSecs.set(timerSecs.get() - 1);
                if (timerSecs.get() < 0) {
                    timerMins.set(timerMins.get() - 1);
                    timerSecs.set(59);
                }
            }

            if (timerCounter < 0) {
                try {
                    playerDied();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        KeyFrame checkingCompleteKeyF = new KeyFrame(Duration.millis(12), event -> {
            if (heroPlayer.isLevelCompleted()) {

                startNewLevel();
            } else if (heroPlayer.hasBeenDefeated()){
                try {
                    playerDied();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (heroPlayer.hasPowerUp()) {
                    int power = heroPlayer.getPowerUpType();
                    if (power == 0) {
                        swordPacLabel.setVisible(true);
                        swiftRunLabel.setVisible(false);
                        iceSpellLabel.setVisible(false);
                    } else if (power == 1) {
                        swordPacLabel.setVisible(false);
                        swiftRunLabel.setVisible(true);
                        iceSpellLabel.setVisible(false);
                    } else if (power == 2) {
                        swordPacLabel.setVisible(false);
                        swiftRunLabel.setVisible(false);
                        iceSpellLabel.setVisible(true);
                    }

            }
        });

        this.timerTimeline = new Timeline(timerKeyF, checkingCompleteKeyF);
        this.timerTimeline.setCycleCount(Timeline.INDEFINITE);
        this.timerTimeline.setRate(1.0);


        this.flashingGameOver = new Timeline(
                new KeyFrame(Duration.millis(400), event -> gameOverLabel.setVisible(false)),
                new KeyFrame(Duration.millis(800), event -> gameOverLabel.setVisible(true))
        );
        this.flashingGameOver.setCycleCount(Timeline.INDEFINITE);


        this.flashingPause = new Timeline(
                new KeyFrame(Duration.millis(400), event -> gamePausedLabel.setVisible(false)),
                new KeyFrame(Duration.millis(800), event -> gamePausedLabel.setVisible(true))
        );
        this.flashingPause.setCycleCount(Timeline.INDEFINITE);

        this.enemySpawning = new Timeline(
                new KeyFrame(Duration.seconds(12), event -> {

                    enemyList.get(enemyIndexForSpawn.get()).start();
                    enemyList.get(enemyIndexForSpawn.getAndIncrement()).setInMotion(true);
                    if (enemyIndexForSpawn.get() == 4) {
                        enemyIndexForSpawn.set(1);
                    }
                })
        );
        this.enemySpawning.setCycleCount(3);
    }

    private void clearPowerLabels() {
        swordPacLabel.setVisible(false);
        swiftRunLabel.setVisible(false);
        iceSpellLabel.setVisible(false);
    }

    private ImageView createCharacterNode(int startPositionX, int startPositionY) {
        ImageView characterNode = new ImageView(new Image(getClass().getResourceAsStream("../../resources/images/heroB/down1.png")));
        characterNode.setX(-16); //Offset values
        characterNode.setY(-42);
        characterNode.setTranslateX(startPositionX * 32);
        characterNode.setTranslateY(startPositionY * 24);
        gameZone.getChildren().add(characterNode);

        ImageView playerCharacterNode = characterNode;
        return playerCharacterNode;
    }

    private ImageView createEnemyNode(int startPositionX, int startPositionY) {
        ImageView characterNode = new ImageView(new Image(getClass().getResourceAsStream("../../resources/images/knight/down1.png")));
        characterNode.setX(-16);
        characterNode.setY(-42);
        characterNode.setTranslateX(startPositionX * 32);
        characterNode.setTranslateY(startPositionY * 24);
        gameZone.getChildren().add(characterNode);

        ImageView enemyNode = characterNode;
        return enemyNode;
    }

    private Image[][] setSprites(String readData) {
        switch (readData) {
            case "heroBSprites":
                return Sprites.heroBSprites;
            case "heroGSprites":
                return Sprites.heroGSprites;
            case "mageSprites":
                return Sprites.mageSprites;
            case "hikerSprites":
                return Sprites.hikerSprites;
            case "sprinterSprites":
                return Sprites.sprinterSprites;
            case "spySprites":
                return Sprites.spySprites;
            default:
                return Sprites.heroBSprites;
        }
    }

    private void livesDisplay() {
        lifeImage1.visibleProperty().bind(heroPlayer.getLifeCount().greaterThan(0));
        lifeImage2.visibleProperty().bind(heroPlayer.getLifeCount().greaterThan(1));
        lifeImage3.visibleProperty().bind(heroPlayer.getLifeCount().greaterThan(2));

    }

    private void setTimerDisplay() {
        minsDisplay.textProperty().bind(timerMins.asString("%02d:"));
        secondsDisplay.textProperty().bind(timerSecs.asString("%02d"));
    }

    private void setLevelCompleteDialogue() {levelCompleteLabel.textProperty().bind(level.asString("LEVEL %d COMPLETE"));}

    private void levelDisplay() {
        levelLabel.textProperty().bind(level.asString("LEVEL: %d"));
    }

    private void scoreDisplay() {
        scoreLabel.textProperty().bind(heroPlayer.getScore().asString("SCORE: %d"));
    }

    private void resetClock() {
        timerCounter = 120;
        timerMins.set(2);
        timerSecs.set(0);
    }

    private void startNewLevel() {

        timerTimeline.stop();
        pauseEnemies();
        enemySpawning.stop();

        gameStarted = false;
        gamePaused = true;

        new Timeline(new KeyFrame(Duration.seconds(1),event -> {
            levelCompleteDialogue.setVisible(true);
        }), new KeyFrame(Duration.seconds(6), event -> {
            heroPlayer.resetHero();
            restartEnemies();
        }), new KeyFrame(Duration.seconds(7),event -> {
            levelCompleteDialogue.setVisible(false);
            resetClock();

            maze.resetPellets();
            level.set(level.get() + 1);

            gamePaused = false;

            startMessage.setVisible(true);

        })).play();
        soundFX.playGameMusic();

    }


    private void playerDied() throws IOException {

        soundFX.stopGameMusic();
        soundFX.dyingSoundPlay();
        if (heroPlayer.getLifeCount().get() > 0) {

            heroPlayer.decreaseLifeCount();

            clearPowerLabels();
            pauseEnemies();
            enemySpawning.stop();
            heroPlayer.dyingHero();

            this.timerTimeline.stop();

            if (heroPlayer.getLifeCount().get() <= 0 || timerCounter < 0) {
                gameOver();
            } else {

                new Timeline(new KeyFrame(Duration.millis(2000), event -> {
                    restartEnemies();
                    heroPlayer.setHasBeenDefeated(false);
                    soundFX.playGameMusic();
                    this.timerTimeline.play();
                })).play();


            }
        }

    }


    private void gameOver() throws IOException {
        soundFX.stopGameMusic();

        if (heroPlayer.getLifeCount().get() > 0) {
            heroPlayer.setLifeCount(0);
        }

        gameStarted = false;
        gameOver = true;
        heroPlayer.stop();

        new Timeline(new KeyFrame(Duration.millis(2000), event -> gameOverDialog.setVisible(true))).play();

        if (!heroPlayer.getPlayerName().equals("")) {
            unlockCharacters();
        }

        this.flashingGameOver.play();
    }

    private void unlockCharacters() throws IOException {

        File saveToLoad = new File("view/resources/saves.txt");
        saveData = Files.readAllLines(Paths.get(saveToLoad.getAbsolutePath()), StandardCharsets.UTF_8);
        int charactersUnlocked = Character.getNumericValue(saveData.get(0).charAt(0));

        if (level.get() > charactersUnlocked) {
            writeToCharacter();
        }
    }

    private void writeToCharacter() throws IOException {
        FileWriter fstream = new FileWriter("view/resources/saves.txt");
        BufferedWriter out = new BufferedWriter(fstream);


        out.write(level.asString().get());
        out.close();
    }

    @FXML
    private void keyPressHandler(KeyEvent keyPressed) throws IOException {

        //Game won't start until after 3 seconds. Commented out to save time during testing
        if (!gameStarted && !gameOver && !gamePaused) {
            startGame();
            return;
        }

        if (gameStarted) {
            //Code for Pausing the game. Detecting when P is pressed.
            if (keyPressed.getCode() == KeyCode.P) {
                togglePauseDialog();
                return;
            }
            //Detect when you want to exit the game.
            if (keyPressed.getCode() == KeyCode.ESCAPE) {
                toggleEscapeDialog();
                return;
            }
            //Detect power up usage.
            if (keyPressed.getCode() == KeyCode.SHIFT || keyPressed.getCode() == KeyCode.SPACE) {

                int powerUsed = heroPlayer.usePowerUp();
                clearPowerLabels();
                if (powerUsed == -1) {
                    return;
                }

                soundFX.powerUpPlay(true);

                if (powerUsed == 0) {
                    for (Enemy enemy : enemyList) {
                        enemy.runAway();
                    }
                } else if (powerUsed == 1) {
                    clearPowerLabels();

                } else if (powerUsed == 2) {
                    for (Enemy enemy : enemyList) {
                        enemy.slowDown();
                    }
                }

                return;
            }

            //DEBUG ONLY
            if (keyPressed.getCode() == KeyCode.PAGE_UP) {
                heroPlayer.setPelletsConsumed(MazeData.getTotalPellets());
                return;
            }
            if (keyPressed.getCode() == KeyCode.PAGE_DOWN) {
                timerCounter = 0;
                timerMins.set(0);
                timerSecs.set(0);
                return;
            }
            //DEBUG ONLY

            //Player 1 Controls.
            if (!gamePaused && !heroPlayer.hasBeenDefeated()) {
                if (keyPressed.getCode() == KeyCode.UP) {
                    heroPlayer.setKeyboardBuffer(1);
                } else if (keyPressed.getCode() == KeyCode.DOWN) {
                    heroPlayer.setKeyboardBuffer(3);
                } else if (keyPressed.getCode() == KeyCode.LEFT) {
                    heroPlayer.setKeyboardBuffer(0);
                } else if (keyPressed.getCode() == KeyCode.RIGHT) {
                    heroPlayer.setKeyboardBuffer(2);
                }

                //Player 2 Controls.
                if (playableEnemies > 0) {
                    if (keyPressed.getCode() == KeyCode.W) {
                        enemyList.get(3).setKeyboardBuffer(1);
                    } else if (keyPressed.getCode() == KeyCode.S) {
                        enemyList.get(3).setKeyboardBuffer(3);
                    } else if (keyPressed.getCode() == KeyCode.A) {
                        enemyList.get(3).setKeyboardBuffer(0);
                    } else if (keyPressed.getCode() == KeyCode.D) {
                        enemyList.get(3).setKeyboardBuffer(2);
                    }

                    //Player 3 Controls.
                    if (playableEnemies == 2) {
                        if (keyPressed.getCode() == KeyCode.U) {
                            enemyList.get(2).setKeyboardBuffer(1);
                        } else if (keyPressed.getCode() == KeyCode.J) {
                            enemyList.get(2).setKeyboardBuffer(3);
                        } else if (keyPressed.getCode() == KeyCode.H) {
                            enemyList.get(2).setKeyboardBuffer(0);
                        } else if (keyPressed.getCode() == KeyCode.K) {
                            enemyList.get(2).setKeyboardBuffer(2);
                        }
                    }
                }
            }
        }
    }

    private void toggleEscapeDialog() {

        if (!pPressed && !gameOver) {
            gamePaused = !gamePaused;
            pauseGame();
        }

        escapePressed = !escapePressed;
        escapeDialog.setVisible(gamePaused && escapePressed);

    }

    private void togglePauseDialog() {


        if (!escapePressed && !gameOver) {
            gamePaused = !gamePaused;
            pPressed = !pPressed;
            pauseDialog.setVisible(gamePaused);
            pauseGame();

        }
        if (gamePaused) {
            this.flashingPause.play();
        } else {
            this.flashingPause.stop();
        }
    }



    private void startGame() {
        startMessage.setVisible(false);
        startTimerLabel.setVisible(true);
        gamePaused = true;

        AtomicInteger countDown = new AtomicInteger(2);

        Timeline startingTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), ev -> startTimerLabel.setText(Integer.toString(countDown.getAndDecrement())))
        );

        startingTimeline.setCycleCount(3);

        startingTimeline.setOnFinished(event -> {
            startTimerLabel.setText("3");
            startTimerLabel.setVisible(false);
            gameStarted = true;
            gamePaused = false;
            heroPlayer.start();
            soundFX.playGameMusic();

            restartEnemies();

            this.timerTimeline.play();
        });

        startingTimeline.play();
    }

    private void pauseGame() {
        if (gamePaused) {
            heroPlayer.pause();
            enemySpawning.pause();
            pauseEnemies();
            soundFX.pauseGameMusic();

            this.timerTimeline.pause();
        } else {
            heroPlayer.start();
            enemySpawning.play();
            unpauseEnemies();
            soundFX.playGameMusic();

            this.timerTimeline.play();
        }
    }

    private void pauseEnemies() {
        for (Enemy enemy : enemyList) {
            enemy.stop();
        }
    }

    private void unpauseEnemies() {
        for (Enemy enemy : enemyList){
            if (enemy.isInMotion()) {
                enemy.start();
            }

        }
    }

    private void restartEnemies() {

        for (Enemy enemy : enemyList) {
            enemy.resetEnemyPosition();
        }
        if (gameStarted) {
            enemyList.get(0).start();
            enemyList.get(0).setInMotion(true);
            if (playableEnemies > 0) {
                enemyList.get(3).start();
                if (playableEnemies == 2) {
                    enemyList.get(2).start();
                }
            }
            enemySpawning.play();
        }

    }


    @FXML
    private void buttonHandler(ActionEvent event) throws IOException {
        String menuSource = "../../resources/fxmlfiles/menuscreens/";
        String inGameSource = "../../resources/fxmlfiles/ingame/";

        Parent nextView = null;

        if (event.getSource().equals(yesButton)) {
            nextView = FXMLLoader.load(getClass().getResource(menuSource + "MainMenu.fxml"));
        } else if (event.getSource().equals(noButton)) {
            toggleEscapeDialog();
        } else if (event.getSource().equals(restartButton)) {
            nextView = FXMLLoader.load(getClass().getResource(inGameSource + "GameScreen.fxml"));
            this.flashingGameOver.stop();
        } else if (event.getSource().equals(endToHSButton)) {
            nextView = FXMLLoader.load(getClass().getResource(menuSource + "Highscores.fxml"));
            this.flashingGameOver.stop();
        }

        if (nextView != null) {
            soundFX.playButtonFX();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(nextView));
        }
    }

}