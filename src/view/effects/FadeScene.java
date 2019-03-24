package view.effects;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.util.Duration;

public class FadeScene {
    public static void fadeIntoScene(Parent inputPane) {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.seconds(1.3));
        transition.setNode(inputPane);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    public static void fadeIntoGame(Parent gamePane) {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.seconds(3));
        transition.setNode(gamePane);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    public static void exitFadeOut(Parent inputPane) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(inputPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setOnFinished(event -> Platform.exit());
        fadeTransition.play();
    }
}
