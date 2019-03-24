package view.scenecontrol.menuscreens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import model.SoundEffects;
import view.effects.FadeScene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CharacterSelection {
    @FXML private StackPane charSelectPane;
    @FXML private Button heroBSelect;
    @FXML private Button heroGSelect;
    @FXML private Button mageSelect;
    @FXML private Button hikerSelect;
    @FXML private Button sprinterSelect;
    @FXML private Button spySelect;
    @FXML private Button letsGoButton;
    @FXML private Button backButton;
    @FXML private Label characterName;
    @FXML private TextField nameField;

    private SoundEffects soundFX;

    private int charactersUnlocked;
    private boolean nameEntered;

    public CharacterSelection() throws IOException {
        this.soundFX = new SoundEffects();

        File saveToLoad = new File("view/resources/saves.txt");
        List<String> saveData = Files.readAllLines(Paths.get(saveToLoad.getAbsolutePath()), StandardCharsets.UTF_8);
        charactersUnlocked = Character.getNumericValue(saveData.get(0).charAt(0));

    }

    private void unlockCharacters() {
        if (charactersUnlocked > 2) {
            mageSelect.setDisable(false);
        }
        if (charactersUnlocked > 4) {
            hikerSelect.setDisable(false);
        }
        if (charactersUnlocked > 6) {
            sprinterSelect.setDisable(false);
        }
        if (charactersUnlocked > 8) {
            spySelect.setDisable(false);
        }
    }

    private void writeToCharacter(String character) throws IOException {
        FileWriter fstream = new FileWriter("view/resources/character.txt");
        BufferedWriter out = new BufferedWriter(fstream);

        out.write(character);
        out.newLine();
        out.write("0");
        out.newLine();
        out.write(nameField.getText());
        out.close();
    }

    private void readyToGo(String name) {
        characterName.setText(name);
        characterName.setVisible(true);
        if (nameEntered) {
            // right now this function only works if the name is entered before a button is pushed
            letsGoButton.setDisable(false);
        }
    }

    @FXML
    public void initialize() {
        charSelectPane.setOpacity(0);
        FadeScene.fadeIntoScene(charSelectPane);
        unlockCharacters();
    }

    @FXML
    private void buttonControlHandler(ActionEvent event) throws IOException {
        String source = "../../resources/fxmlfiles/menuscreens/";
        String inGameSource = "../../resources/fxmlfiles/ingame/";
        String character;

        Parent nextView = null;

        // set the hero for the gameScreen from here somehow
        if (event.getSource().equals(heroBSelect)) {
            readyToGo("Hero (B)");
            character = "heroBSprites";
            writeToCharacter(character);
        } else if (event.getSource().equals(heroGSelect)) {
            readyToGo("Hero (G)");
            character = "heroGSprites";
            writeToCharacter(character);
        } else if (event.getSource().equals(mageSelect)) {
            readyToGo("Mage");
            character = "mageSprites";
            writeToCharacter(character);
        } else if (event.getSource().equals(hikerSelect)) {
            readyToGo("Hiker");
            character = "hikerSprites";
            writeToCharacter(character);
        } else if (event.getSource().equals(sprinterSelect)) {
            readyToGo("Sprinter");
            character = "sprinterSprites";
            writeToCharacter(character);
        } else if (event.getSource().equals(spySelect)) {
            readyToGo("Spy");
            character = "spySprites";
            writeToCharacter(character);
        }

        // currently it only goes back to the story mode if back is selected
        if (event.getSource().equals(letsGoButton)) {
            nextView = FXMLLoader.load(getClass().getResource(inGameSource + "GameScreen.fxml"));
        } else if (event.getSource().equals(backButton)) {
            nextView = FXMLLoader.load(getClass().getResource(source + "ArcadeMenu.fxml"));
        }

        if (nextView != null) {
            soundFX.playButtonFX();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(nextView));
        }

    }

    @FXML
    private void checkName(KeyEvent keyEvent) {
        if ((!nameField.getText().equals(null))) {
            nameEntered = true;
        }

    }

}
