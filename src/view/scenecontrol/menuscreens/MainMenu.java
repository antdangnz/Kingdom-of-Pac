package view.scenecontrol.menuscreens;

import java.io.IOException;

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

public class MainMenu {

    @FXML private StackPane rootPane;
    @FXML private Button creditsButton;
    @FXML private Button storyButton;
    @FXML private Button exitButton;
    @FXML private Button arcadeButton;
    @FXML private Button highscoreButton;

    private SoundEffects soundFX;



    public MainMenu() {
        this.soundFX = new SoundEffects();
    }

    @FXML
    public void initialize() {
        rootPane.setOpacity(0);
        FadeScene.fadeIntoScene(rootPane);
    }

    @FXML
    private void buttonControls(ActionEvent event) throws IOException {
        String source = "../../resources/fxmlfiles/menuscreens/";
        Parent nextView = null;


        if (event.getSource().equals(storyButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "StoryModeMenu.fxml"));
        } else if (event.getSource().equals(arcadeButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "ArcadeMenu.fxml"));
        } else if (event.getSource().equals(highscoreButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "Highscores.fxml"));
        } else if (event.getSource().equals(creditsButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "Credits.fxml"));
        } else if (event.getSource().equals(exitButton)) {
            FadeScene.exitFadeOut(rootPane);
        }

        if (nextView != null) {
            this.soundFX.playButtonFX();
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(nextView));
        }
    }
}
