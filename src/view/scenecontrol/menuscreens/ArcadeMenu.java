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

public class ArcadeMenu {
    @FXML private StackPane arcadePane;
    @FXML private Button classicButton;
    @FXML private Button twoPlayerButton;
    @FXML private Button challengeButton;
    @FXML private Button backButton;

    private SoundEffects soundFX;

    public ArcadeMenu() {
        this.soundFX = new SoundEffects();
    }

    @FXML
    public void initialize() {
        arcadePane.setOpacity(0);
        FadeScene.fadeIntoScene(arcadePane);
    }

    @FXML
    private void buttonControlHandler(ActionEvent event) throws IOException{
        String menuSource = "../../resources/fxmlfiles/menuscreens/";

        Parent nextView = null;

        if (event.getSource().equals(classicButton)) {
            nextView = FXMLLoader.load(getClass().getResource(menuSource + "CharacterSelection.fxml"));
        } else if (event.getSource().equals(twoPlayerButton)) {
            System.out.println("Haven't implemented yet!");
        } else if (event.getSource().equals(challengeButton)) {
            nextView = FXMLLoader.load(getClass().getResource(menuSource + "CharSelectMultiplayer.fxml"));
        } else if (event.getSource().equals(backButton)) {
            nextView = FXMLLoader.load(getClass().getResource(menuSource + "MainMenu.fxml"));
        }


        if (nextView != null) {
            soundFX.playButtonFX();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(nextView));
        }
    }
}
