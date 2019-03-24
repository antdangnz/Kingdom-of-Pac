package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Enemy extends Movement {

    private static final double CHANGE_CHANCE = 0.70, FIND_HERO = 0.5;
    private final Hero hero;

    private ImageView enemyChar;

    private int x, y, homingCount, positionIndex;
    private boolean runAway, playerControlled;

    // the sprites of the enemy used for different animations
    private Image[][] enemySprites;

    public Enemy (Hero hero, ImageView enemyChar, int enemyIndex, boolean playerControlled) {
        this.hero = hero;
        this.enemyChar = enemyChar;
        this.enemySprites = Sprites.enemySprites;
        this.positionIndex = enemyIndex;
        this.images = enemySprites[MOVE_RIGHT];
        this.enemyChar.setImage(images[0]);
        this.runAway = false;
        this.playerControlled = playerControlled;

        this.timeline.setRate(ENEMY_SPEED);

        this.xDirection = 1;
        this.yDirection = 0;

        this.setStartingPoint(this.positionIndex);

        this.inMotion = false;
    }

    private void setStartingPoint(int index) {

        this.x = MazeData.getEnemyPositionX(index);
        this.y = MazeData.getEnemyPositionY(index);
    }

    public void resetEnemyPosition() {
        setStartingPoint(this.positionIndex);
        this.inMotion = false;
        this.enemyChar.setTranslateX(x * GRID_GAP_X);
        this.enemyChar.setTranslateY(y * GRID_GAP_Y);

        xDirection = 1;
        yDirection = 0;

        currentImage.set(0);
        moveCounter = 0;
    }

    private void directionHandler() {

        switch(keyboardBuffer) {
            case (MOVE_LEFT) :
                moveLeft();
                break;
            case (MOVE_RIGHT) :
                moveRight();
                break;
            case (MOVE_UP) :
                moveUp();
                break;
            case (MOVE_DOWN) :
                moveDown();
                break;
        }
    }
    private void moveLeft() {

        int nextDirection = this.x - 1;

        if (MazeData.canWrapLower(nextDirection, this.y)) {
            nextDirection = MazeData.MAX_BLOCKS_H - 1;
        }
        if (MazeData.canMoveHere(nextDirection, this.y)) {
            images = enemySprites[MOVE_LEFT];

            setDirection(-1, 0);

        }
    }

    private void moveUp() {

        int nextDirection = this.y - 1;

        if (MazeData.canWrapLower(this.x, nextDirection)) {
            nextDirection = MazeData.MAX_BLOCKS_V - 1;
        }
        if (MazeData.canMoveHere(this.x, nextDirection)) {
            images = enemySprites[MOVE_UP];
            setDirection(0,-1);
        }
    }

    private void moveRight() {

        int nextDirection = this.x + 1;

        if (MazeData.canWrapUpper(nextDirection, this.y)) {
            nextDirection = 0;
        }
        if (MazeData.canMoveHere(nextDirection, this.y)) {
            images = enemySprites[MOVE_RIGHT];
            setDirection(1, 0);
        }
    }

    private void moveDown() {

        int nextDirection = this.y + 1;

        if (MazeData.canWrapUpper(this.x, nextDirection)) {
            nextDirection = 0;
        }
        if (MazeData.canMoveHere(this.x, nextDirection)) {
            images = enemySprites[MOVE_DOWN];
            setDirection(0, 1);
        }
    }

    private void setDirection(int newXDirection, int newYDirection) {
        this.xDirection = newXDirection;
        this.yDirection = newYDirection;
        this.inMotion = true;
        this.keyboardBuffer = -1;
    }

    @Override
    public void moveOneStep() {
        if (playerControlled) {
            if (currentImage.get() == 0) {
                directionHandler();
            }
        }

        if (inMotion) {
            if (xDirection != 0) {
                moveHorizontal();

            } else if (yDirection != 0) {
                moveVertical();
            }

            if (currentImage.get() < ANIMATION_STEP - 1 && inMotion) {//state == MOBILE) {
                currentImage.set(currentImage.get() + 1);
            } else {
                currentImage.set(0);
            }
            enemyChar.setImage(images[currentImage.get()]);
        }

        checkCollision();
    }

    private void checkCollision() {
        if (this.x == hero.heroPositionX() && this.y == hero.heroPositionY()) {
            this.stop();
            if (runAway) {
                enemyDefeated();
            } else {
                hero.setHasBeenDefeated(true);
            }
        }
    }

    private void enemyDefeated() {
        hero.defeatedEnemy();

        new Timeline(
                new KeyFrame(Duration.millis(500), event -> {
                    this.enemyChar.setVisible(false);
                    this.resetEnemyPosition();
                    this.runAway = false;
                }),
                new KeyFrame(Duration.seconds(6), event ->
                    this.enemyChar.setVisible(true)
                ),
                new KeyFrame(Duration.seconds(9), event -> {
                    this.setInMotion(true);
                    this.start();
                })
        ).play();
    }

    public void runAway() {
        this.runAway = true;
        this.timeline.setRate(2);
        new Timeline(
                new KeyFrame(Duration.seconds(10), event -> {
                    this.runAway = false;
                    this.timeline.setRate(ENEMY_SPEED);
                })
        ).play();
    }

    private void moveHorizontal() {
        int nextXPosition = this.x + xDirection;

        this.images = (xDirection < 0) ? enemySprites[MOVE_LEFT] : enemySprites[MOVE_RIGHT];

        if (MazeData.canWrapLower(nextXPosition, this.y)) {
            nextXPosition = MazeData.MAX_BLOCKS_H - 1;
        }
        else if (MazeData.canWrapUpper(nextXPosition, this.y)) {
            nextXPosition = 0;
        }

        if (MazeData.canMoveHere(nextXPosition, this.y)) {

            moveCounter++;
            if (moveCounter < ANIMATION_STEP) {
                enemyChar.setTranslateX(enemyChar.getTranslateX() + (xDirection * MOVE_SPEED_X));
            } else {
                enemyChar.setTranslateX(enemyChar.getTranslateX() + (xDirection * MOVE_SPEED_X));
                moveCounter = 0;

                this.x = nextXPosition;
                enemyChar.setTranslateX(this.x * GRID_GAP_X);
            }

            //Do this only after enemy has reached the new position. Will check the position in front of this new one

        }

        if (!playerControlled) {
            if (moveCounter == 0) {

                if (!MazeData.canMoveHere(this.x + xDirection, this.y)) {
                    changeDirection(true);
                } else {
                    changeDirection(false);
                }
            }
        }
    }

    private void moveVertical() {

        int nextYPosition = y + yDirection;

        images = (yDirection < 0) ? enemySprites[MOVE_UP] : enemySprites[MOVE_DOWN];

        if (MazeData.canWrapLower(this.x, nextYPosition)) {
            nextYPosition = MazeData.MAX_BLOCKS_V - 1;
        }
        else if (MazeData.canWrapUpper(this.x, nextYPosition)) {
            nextYPosition = 0;
        }

        if (MazeData.canMoveHere(this.x, nextYPosition)) {

            moveCounter++;
            if (moveCounter < ANIMATION_STEP) {
                enemyChar.setTranslateY(enemyChar.getTranslateY() + (yDirection * MOVE_SPEED_Y));
            } else {

                enemyChar.setTranslateY(enemyChar.getTranslateY() + (yDirection * MOVE_SPEED_Y));
                moveCounter = 0;

                this.y = nextYPosition;
                enemyChar.setTranslateY(this.y * GRID_GAP_Y);
            }

        }
        if (!playerControlled) {
            if (moveCounter == 0) {

                if (!MazeData.canMoveHere(this.x, this.y + yDirection)) {
                    changeDirection(true);
                } else {
                    changeDirection(false);
                }
            }
        }

    }

    private void changeDirection(boolean needToChangeDirection) {

        //If enemy doesn't need to change direction, there's still a chance to change
        if (!needToChangeDirection && Math.random() > CHANGE_CHANCE) { return; }


        //Change X to Y direction
        if (xDirection != 0) {
            DirectionChoice goNorth = new DirectionChoice(this.x, this.y - 1, hero.heroPositionX(), hero.heroPositionY(), runAway);
            DirectionChoice goSouth = new DirectionChoice(this.x, this.y + 1, hero.heroPositionX(), hero.heroPositionY(), runAway);
            DirectionChoice continueStraight = new DirectionChoice
                    (this.x + xDirection, this.y, hero.heroPositionX(), hero.heroPositionY(), runAway);
            //Do nothing if you can't go up or down

            //Check if dead end
            if (!goNorth.isScoreGreater(0) && !goSouth.isScoreGreater(0) && !continueStraight.isScoreGreater(0)) {
                xDirection = -xDirection;
                yDirection = 0;
            } else if ((goNorth.isScoreGreater( 0) || goSouth.isScoreGreater(0))) {

                //Randomly decide when to chase the hero
                if (Math.random() <  FIND_HERO && homingCount == 0) {
                    homingCount = (int) (Math.random() * 10 + 5);
                }


                //Don't change direction if you can go straight
                if (continueStraight.isScoreGreater(0)
                        && continueStraight.isScoreGreater(goNorth.getScore())
                        && continueStraight.isScoreGreater(goSouth.getScore())
                        && homingCount > 0) {
                    homingCount--;
                } else {
                    xDirection = 0;
                    yDirection = newDirection(goSouth.getScore(), goNorth.getScore());
                }
            }
        }

        //Change Y to X direction
        else if (yDirection != 0) {
            DirectionChoice goEast = new DirectionChoice(this.x + 1, this.y, hero.heroPositionX(), hero.heroPositionY(), runAway);
            DirectionChoice goWest = new DirectionChoice(this.x - 1, this.y, hero.heroPositionX(), hero.heroPositionY(), runAway);
            DirectionChoice continueStraight = new DirectionChoice
                    (this.x, this.y + yDirection, hero.heroPositionX(), hero.heroPositionY(), runAway);

            if (!goEast.isScoreGreater(0) && !goWest.isScoreGreater(0) && !continueStraight.isScoreGreater(0)) {
                xDirection = 0;
                yDirection = -yDirection;
            } else if ((goEast.isScoreGreater(0) || goWest.isScoreGreater(0))) {

                if (Math.random() <  FIND_HERO && homingCount == 0) {
                    homingCount = (int) (Math.random() * 10 + 4);
                }

                if (continueStraight.isScoreGreater(0)
                        && continueStraight.isScoreGreater(goEast.getScore())
                        && continueStraight.isScoreGreater(goWest.getScore())
                        && homingCount > 0) {
                    homingCount--;
                } else {
                    xDirection = newDirection(goEast.getScore(), goWest.getScore());
                    yDirection = 0;
                }
            }
        }
    }

    private int newDirection(int firstScore, int secondScore) {
        if (firstScore < 0) {
            return -1;
        } else if (secondScore > 0) {

            if (homingCount > 0) {
                if (secondScore > firstScore) {
                    homingCount--;
                    return -1;
                }
            } else if (Math.random() < 0.5) {
                return -1;
            }
        }
        return 1;
    }

    public void slowDown() {
        this.timeline.setRate(ENEMY_SPEED / 4);

        new Timeline(
                new KeyFrame(Duration.seconds(10), event -> this.timeline.setRate(ENEMY_SPEED))
        ).play();
    }

}
