package view.scenecontrol.menuscreens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.SoundEffects;
import view.effects.FadeScene;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Highscores {

    @FXML private StackPane highscorePane;
    @FXML private Button backButton;
    @FXML private Label highscore1;
    @FXML private Label highscore2;
    @FXML private Label highscore3;
    @FXML private Label highscore4;
    @FXML private Label highscore5;


    private SoundEffects soundFX;

    private List<String> saveData;

    public Highscores() throws IOException {
        this.soundFX = new SoundEffects();

        File saveToLoad = new File("view/resources/highscores.txt");
        saveData = Files.readAllLines(Paths.get(saveToLoad.getAbsolutePath()), StandardCharsets.UTF_8);

    }

    private void readyToGo(List<String> saveData) {
        highscore1.setText(saveData.get(0));
        highscore2.setText(saveData.get(1));
        highscore3.setText(saveData.get(2));
        highscore4.setText(saveData.get(3));
        highscore5.setText(saveData.get(4));
    }

    @FXML
    public void initialize() {
        highscorePane.setOpacity(0);
        FadeScene.fadeIntoScene(highscorePane);

        readyToGo(saveData);
    }

    @FXML
    public void buttonControlHandler(ActionEvent event) throws IOException {
        String menuSource = "../../resources/fxmlfiles/menuscreens/";

        Parent nextView = null;

        if (event.getSource().equals(backButton)) {
            nextView = FXMLLoader.load(getClass().getResource(menuSource + "MainMenu.fxml"));
        }

        if (nextView != null) {
            soundFX.playButtonFX();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(nextView));
        }
    }

}
