package model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.util.Duration;


public abstract class Movement extends Parent {

    // Timeline is needed for the animation of moving characters
    protected Timeline timeline;

    protected static final int ANIMATION_STEP = 8;

    protected static final int GRID_GAP_X = 32;
    protected static final int GRID_GAP_Y = 24;

    protected static final int MOVE_SPEED_X = (int) Math.ceil(GRID_GAP_X/ANIMATION_STEP);
    protected static final int MOVE_SPEED_Y = (int) Math.ceil(GRID_GAP_Y/ANIMATION_STEP);

    // controls the directions that the characters move
    static final int MOVE_LEFT = 0;
    static final int MOVE_UP = 1;
    static final int MOVE_RIGHT = 2;
    static final int MOVE_DOWN = 3;
    static final int DYING = 4;

    static final double NORMAL_SPEED = 3.8;
    static final double ENEMY_SPEED = 4.05;

    protected int keyboardBuffer;

    protected int moveCounter;

    protected boolean inMotion;

    // Used to control the sprites used for the characters
    protected IntegerProperty currentImage;
    protected Image[] images;

    // Used to help locate the position characters
    protected int xDirection;
    protected int yDirection;

    public Movement() {
        currentImage = new SimpleIntegerProperty(0);

        createAnimation();
    }

    private void createAnimation() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), event -> moveOneStep());

        this.timeline = new Timeline();
        this.timeline.getKeyFrames().add(keyFrame);
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.setRate(NORMAL_SPEED);

    }

    public void setKeyboardBuffer(int key) {
        keyboardBuffer = key;
    }

    public boolean isInMotion() {
        return inMotion;
    }

    public void setInMotion(boolean inMotion) {
        this.inMotion = inMotion;
    }

    public void stop() {
        this.timeline.stop();
    }

    public void pause() {
        this.timeline.pause();
    }

    public void start() {
        this.timeline.play();
    }

    public boolean isRunning() {
        return this.timeline.getStatus() == Animation.Status.RUNNING;
    }

    public boolean isPaused() {
        return this.timeline.getStatus() == Animation.Status.PAUSED;
    }

    public abstract void moveOneStep();

}
