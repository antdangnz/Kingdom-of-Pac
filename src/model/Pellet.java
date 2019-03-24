package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

class Pellet extends Parent {
    public BooleanProperty shouldStopAnimation;
    public int type;

    private boolean visible;

    private IntegerProperty radius;

    private int animationRadius;
    private int delta;
    private Timeline timeline;

    private Circle circle;

    public Pellet(int x, int y, int type) {

        this.shouldStopAnimation = new SimpleBooleanProperty(false);
        this.type = type;

        if (type == MazeData.POWER_UP) {
            this.radius = new SimpleIntegerProperty(5);
        } else {
            this.radius = new SimpleIntegerProperty(2);
        }

        this.delta = -1;
        this.animationRadius = 3;

        this.visible = true;

        this.circle = new Circle((x * 32 + 16), (y * 24 + 12), this.radius.intValue(), Color.GOLD);
        this.circle.radiusProperty().bind(this.radius);
    }

    public Circle getCircle() {
        return circle;
    }

    public void toggleVisibility() {
        visible = !visible;
        circle.setVisible(visible);
    }

    public Boolean isItVisible() {
        return circle.isVisible();
    }

    private void createTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.25), event -> animatePowerUp());

        timeline.getKeyFrames().add(kf);
    }

    public void playTimeline() {
        if (timeline == null) {
            createTimeline();
        }

        timeline.play();
    }

    private final void animatePowerUp() {

        if (!isVisible() || shouldStopAnimation.get()) {
            return;
        }

        animationRadius += delta;
        int x1 = Math.abs(animationRadius) + 3;

        if (x1 > 5) delta = -delta;

        this.radius.set(x1);
    }

}


