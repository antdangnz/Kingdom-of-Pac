package view.scenecontrol.menuscreens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.SoundEffects;
import view.effects.FadeScene;

import java.io.IOException;

public class Credits {

    @FXML private StackPane creditsPane;
    @FXML private Button backButton;

    private SoundEffects soundFX;

    public Credits() {
        this.soundFX = new SoundEffects();
    }

    @FXML
    public void initialize() {
        creditsPane.setOpacity(0);
        FadeScene.fadeIntoScene(creditsPane);
    }

    @FXML
    private void buttonControlHandler(ActionEvent event) throws IOException {
        String source = "../../resources/fxmlfiles/menuscreens/";
        Parent nextView = null;

        if (event.getSource().equals(backButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "MainMenu.fxml"));

            if (nextView != null) {
                soundFX.playButtonFX();
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(new Scene(nextView));
            }
        }
    }
}
