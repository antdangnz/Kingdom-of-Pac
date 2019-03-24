package view.scenecontrol.menuscreens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.SoundEffects;
import view.effects.FadeScene;

import java.io.IOException;


public class WelcomeScreen {
    @FXML private Button startButton;
    @FXML private StackPane rootPane;

    private SoundEffects soundFX;

    public WelcomeScreen() {
        this.soundFX = new SoundEffects();
    }

    @FXML
    public void initialize() {
        startButton.setEffect(new DropShadow());
        rootPane.setOpacity(0);
        FadeScene.fadeIntoScene(rootPane);
    }

    @FXML
    private void buttonControlHandler(ActionEvent event) throws IOException{
        Parent nextView = null;
        if (event.getSource().equals(startButton)) {
            nextView = FXMLLoader.load(getClass().getResource("../../resources/fxmlfiles/menuscreens/MainMenu.fxml"));
        }

        if (nextView != null) {
            this.soundFX.playButtonFX();
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(nextView));
        }
    }


}
