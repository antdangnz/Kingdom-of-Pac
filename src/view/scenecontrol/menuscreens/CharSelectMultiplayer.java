package view.scenecontrol.menuscreens;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class CharSelectMultiplayer {

    @FXML private Button players2;
    @FXML private Button players3;
    @FXML private StackPane charSelectPane;
    @FXML private Button heroBSelect;
    @FXML private Button heroGSelect;
    @FXML private Button mageSelect;
    @FXML private Button hikerSelect;
    @FXML private Button sprinterSelect;
    @FXML private Button spySelect;
    @FXML private Button letsGoButton;
    @FXML private Button backButton;
    @FXML private Label readyPlayers;

    private SoundEffects soundFX;

    private int charactersUnlocked;

    private SimpleIntegerProperty enemyPlayers;

    public CharSelectMultiplayer() throws IOException {
        this.soundFX = new SoundEffects();

        File saveToLoad = new File("view/resources/saves.txt");
        List<String> saveData = Files.readAllLines(Paths.get(saveToLoad.getAbsolutePath()), StandardCharsets.UTF_8);
        charactersUnlocked = Character.getNumericValue(saveData.get(0).charAt(0));

        enemyPlayers = new SimpleIntegerProperty(1);
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
        out.write(enemyPlayers.asString().get());
        out.newLine();
        out.write("");
        out.close();

    }

    private void readyToGo(String playerOne) {//, int enemyPlayers) {
        readyPlayers.textProperty().bind(enemyPlayers.asString(playerOne + " and %d Knight(s)"));
        readyPlayers.setVisible(true);
        letsGoButton.setDisable(false);
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

        if (event.getSource().equals(players2)) {
            enemyPlayers.set(1);
        } else if (event.getSource().equals(players3)) {
            enemyPlayers.set(2);
        }

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

}


