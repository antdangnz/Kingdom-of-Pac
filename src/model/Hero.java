package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

public class Hero extends Movement {

    // A hero can only use a Power up if powerUpAvailable is true
    private boolean powerUpAvailable;
    private int powerPossessed;
    private static final int SWORD_PAC = 0, SWIFT_RUN = 1, ICE_SPELL = 2, EXTRA_LIFE = 3;

    // the sprites of the hero used for different animations
    private Image[][] heroSprites;

    private String name;
    private SimpleIntegerProperty score;
    private SimpleIntegerProperty lifeCount;
    private int pelletsConsumed;

    private ImageView playerChar;
    private boolean isLevelCompleted, hasBeenDefeated;
    //position of the character
    private int x;
    private int y;

    public Hero(String name, ImageView playerChar, Image[][] heroSprites) {//, int startingX, int startingY, Group gameZone){///, ImageView character, Scene scene){//} Image[][] heroSprites) {
        this.name = name;
        this.playerChar = playerChar;
        this.heroSprites = heroSprites;
        this.powerPossessed = -1;


        this.score = new SimpleIntegerProperty(0);
        this.pelletsConsumed = 0;
        this.powerUpAvailable = false;
        this.isLevelCompleted = false;
        this.hasBeenDefeated = false;

        this.images = heroSprites[MOVE_LEFT];
        playerChar.setImage(images[0]);

        this.inMotion = true;
        this.keyboardBuffer = -1;
        this.xDirection = -1;
        this.yDirection = 0;

        this.lifeCount = new SimpleIntegerProperty(2);

        setStartingPoint();
    }

    public void setPelletsConsumed(int pelletsConsumed) {
        this.pelletsConsumed = pelletsConsumed;
    }

    public String getPlayerName() {
        return name;
    }

    private void setStartingPoint() {
        this.x = MazeData.getPlayerStartingX();
        this.y = MazeData.getPlayerStartingY();
    }

    public SimpleIntegerProperty getScore() {
        return this.score;
    }

    public SimpleIntegerProperty getLifeCount() {
        return lifeCount;
    }

    public void setLifeCount(int newLifeCount) {
        this.lifeCount.set(newLifeCount);
    }

    public void decreaseLifeCount() {
        this.lifeCount.set(this.lifeCount.get() - 1);
    }

    private void increaseLifeCount() {
        this.lifeCount.set(this.lifeCount.get() + 1);
    }

    public boolean isLevelCompleted() {
        return isLevelCompleted;
    }

    public int heroPositionX() {
        return this.x;
    }

    public int heroPositionY() {
        return this.y;
    }

    private void resetPelletsConsumed() {
        this.pelletsConsumed = 0;
    }

    private void pelletHandler() {
        Pellet pellet = (Pellet) MazeData.getPellet(x, y);

        if (pellet != null && pellet.isItVisible()) {
            int pelletValue = 10;
            pellet.toggleVisibility();
            pelletsConsumed++;

            if (pellet.type == MazeData.POWER_UP) {
                pelletValue = 50;
                    powerUpAvailable = true;
                    giveHeroPower();
            }
            score.set(score.get() + pelletValue);

            if (pelletsConsumed >= MazeData.getTotalPellets()) {
                // starting a new level happens here, once the player has consumed all the pellets on the map
                stop();
                pelletsConsumed = 0;
                powerUpAvailable = false;
                isLevelCompleted = true;

            }
        }
    }

    private void giveHeroPower() {

        double randomizeValue = Math.random();
        int nextPower;

        if (randomizeValue > 0.0 && randomizeValue < 0.15) {
            nextPower = EXTRA_LIFE;
        } else  {
            nextPower = (int) (Math.random() * 3);
        }

        if (nextPower == EXTRA_LIFE) {
            if (this.lifeCount.get() < 3) {
                this.increaseLifeCount();
            } else {
                this.powerUpAvailable = false;
                this.powerPossessed = -1;
            }
        } else {
            this.powerPossessed = nextPower;
        }
    }

    @Override
    public void moveOneStep() {
        if (currentImage.get() == 0) {
            directionHandler();
        }

        if (inMotion && !hasBeenDefeated) {

            if (xDirection != 0) {
                moveHorizontal();
            } else if (yDirection != 0) {
                moveVertical();
            }

            if (currentImage.get() < ANIMATION_STEP - 1 && inMotion) {//state == MOBILE) {
                currentImage.set(currentImage.get() + 1);
            } else {
                currentImage.set(0);
                pelletHandler();
            }
            playerChar.setImage(images[currentImage.get()]);

        }

    }

    private void moveHorizontal() {
        int nextXPosition = x + xDirection;

        if (MazeData.canWrapLower(nextXPosition, y)) {
            nextXPosition = MazeData.MAX_BLOCKS_H - 1;
        }
        else if (MazeData.canWrapUpper(nextXPosition, y)) {
            nextXPosition = 0;
        }

        if (!MazeData.canMoveHere(nextXPosition, y)) {
            this.inMotion = false;

        } else {

            moveCounter++;
            if (moveCounter < ANIMATION_STEP) {
                playerChar.setTranslateX(playerChar.getTranslateX() + (xDirection * MOVE_SPEED_X));
            } else {
                moveCounter = 0;
                playerChar.setTranslateX(playerChar.getTranslateX() + (xDirection * MOVE_SPEED_X));
                this.x = nextXPosition;
                playerChar.setTranslateX(this.x * GRID_GAP_X);
//            }
            }
        }
    }

    private void moveVertical() {
        int nextYPosition = y + yDirection;

        if (MazeData.canWrapLower(x, nextYPosition)) {
            nextYPosition = MazeData.MAX_BLOCKS_V - 1;
        }
        else if (MazeData.canWrapUpper(x, nextYPosition)) {
            nextYPosition = 0;
        }

        if (!MazeData.canMoveHere(x, nextYPosition)) {
            inMotion = false;
//            return;
        } else {

            moveCounter++;
            if (moveCounter < ANIMATION_STEP) {
                playerChar.setTranslateY(playerChar.getTranslateY() + (yDirection * MOVE_SPEED_Y));
            } else {
                moveCounter = 0;
                playerChar.setTranslateY(playerChar.getTranslateY() + (yDirection * MOVE_SPEED_Y));
                this.y = nextYPosition;
                playerChar.setTranslateY(this.y * GRID_GAP_Y);
            }
        }
    }

    private void directionHandler() {

        if (keyboardBuffer == MOVE_LEFT) {
            moveLeft();
        } else if (keyboardBuffer == MOVE_RIGHT) {
            moveRight();
        } else if (keyboardBuffer == MOVE_UP) {
            moveUp();
        } else if (keyboardBuffer == MOVE_DOWN) {
            moveDown();
        }
    }

    public void dyingHero() {
        stop();
        images = heroSprites[DYING];
        inMotion = false;

        AtomicInteger i = new AtomicInteger();

        Timeline timeline1 = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.millis(100), event -> dyingFrames(i.getAndIncrement()));

        timeline1.getKeyFrames().add(kf1);
        timeline1.setCycleCount(6);
        timeline1.setRate(0.6);
        timeline1.setOnFinished(event -> resetHero());

        timeline1.playFromStart();

    }

    private void dyingFrames(int i) {
        playerChar.setImage(images[i]);
    }

    public void resetHero() {

        new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            setStartingPoint();


            this.playerChar.setTranslateX(x * GRID_GAP_X);
            this.playerChar.setTranslateY(y * GRID_GAP_Y);

            this.images = heroSprites[MOVE_LEFT];
            playerChar.setImage(images[0]);
            inMotion = true;

            xDirection = -1;
            yDirection = 0;

            this.powerUpAvailable = false;
            this.powerPossessed = -1;

            keyboardBuffer = -1; // no directional key has been pressed

            currentImage.set(0); // reset the animation for the moving sprite
            moveCounter = 0;

        })).play();

//        setVisible(true);
        if (lifeCount.get() > 0 && !isLevelCompleted) {
            playerChar.setVisible(true);
            start();
        } else if (lifeCount.get() <= 0) {
            playerChar.setVisible(false);
        }

        if (isLevelCompleted) {
            resetPelletsConsumed();
            isLevelCompleted = false;
        }
    }

    private void moveLeft() {

        int nextDirection = this.x - 1;

        if (MazeData.canWrapLower(nextDirection, this.y)) {
            nextDirection = MazeData.MAX_BLOCKS_H - 1;
        }
        if (MazeData.canMoveHere(nextDirection, this.y)) {
            images = heroSprites[MOVE_LEFT];

            setDirection(-1, 0);

        }
    }

    private void moveUp() {

        int nextDirection = this.y - 1;

        if (MazeData.canWrapLower(this.x, nextDirection)) {
            nextDirection = MazeData.MAX_BLOCKS_V - 1;
        }
        if (MazeData.canMoveHere(this.x, nextDirection)) {
            images = heroSprites[MOVE_UP];
            setDirection(0,-1);
        }
    }

    private void moveRight() {

        int nextDirection = this.x + 1;

        if (MazeData.canWrapUpper(nextDirection, this.y)) {
            nextDirection = 0;
        }
        if (MazeData.canMoveHere(nextDirection, this.y)) {
            images = heroSprites[MOVE_RIGHT];
            setDirection(1, 0);
        }
    }

    private void moveDown() {

        int nextDirection = this.y + 1;

        if (MazeData.canWrapUpper(this.x, nextDirection)) {
            nextDirection = 0;
        }
        if (MazeData.canMoveHere(this.x, nextDirection)) {
            images = heroSprites[MOVE_DOWN];
            setDirection(0, 1);
        }
    }

    private void setDirection(int newXDirection, int newYDirection) {
        this.xDirection = newXDirection;
        this.yDirection = newYDirection;
        this.inMotion = true;
        this.keyboardBuffer = -1;
    }

    public boolean hasBeenDefeated() {
        return hasBeenDefeated;
    }

    public void setHasBeenDefeated(boolean hasBeenDefeated) {
        this.hasBeenDefeated = hasBeenDefeated;
        if (this.hasBeenDefeated) {
            stop();
        }
    }

    public boolean hasPowerUp() {
        return powerUpAvailable;
    }

    public int getPowerUpType() {
        return powerPossessed;
    }

    public void defeatedEnemy() {
        this.score.set(this.score.get() + 500);
    }

    public int usePowerUp() {

        if (powerUpAvailable) {
            if (powerPossessed == SWORD_PAC) {
                powerPossessed = -1;
                powerUpAvailable = false;
                return 0;
            } else if (powerPossessed == SWIFT_RUN) {
                this.timeline.setRate(NORMAL_SPEED * 2);
                new Timeline(
                        new KeyFrame(Duration.seconds(10), event -> this.timeline.setRate(NORMAL_SPEED))
                ).play();
                powerPossessed = -1;
                powerUpAvailable = false;
                return 1;

            } else if (powerPossessed == ICE_SPELL) {
                powerPossessed = -1;
                powerUpAvailable = false;
                return 2;
            }
        }
        return -1;
    }

}
