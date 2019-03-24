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

public class StoryModeMenu {

    @FXML private StackPane storyModePane;
    @FXML private Button newGameButton;
    @FXML private Button continueButton;
    @FXML private Button backButton;

    private SoundEffects soundFX;

    private int level;

    public StoryModeMenu() {
        this.soundFX = new SoundEffects();

//        File saveToLoad = new File("src/view/resources/saves.txt");
//        saveData = Files.readAllLines(Paths.get(saveToLoad.getAbsolutePath()), StandardCharsets.UTF_8);

        //initially planned to have the continue button become available if there is a level saved in the story mode
        //but due to the story mode not being completed, this should never be true
        level = 0;//Character.getNumericValue(saveData.get(0).charAt(0));
    }

    //@Override
    public void initialize() {
        storyModePane.setOpacity(0);
        FadeScene.fadeIntoScene(storyModePane);
        continueStory();
    }

    private void continueStory() {
        if (level > 0) {
            continueButton.setDisable(false);
        }
    }

    @FXML
    private void buttonControlHandler(ActionEvent event) throws IOException {
        String source = "../../resources/fxmlfiles/menuscreens/";
        Parent nextView = null;

        if (event.getSource().equals(newGameButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "CharacterSelection.fxml"));
        } else if (event.getSource().equals(continueButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "CharacterSelection.fxml"));
        } else if (event.getSource().equals(backButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "MainMenu.fxml"));
        }

        if (nextView != null) {
            soundFX.playButtonFX();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(nextView));
        }
    }

}
