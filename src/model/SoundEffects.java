package model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffects {
    private MediaPlayer buttonSound;
    private MediaPlayer gameMusic;
    private MediaPlayer dyingSound;
    private MediaPlayer powerUpSound;


    public SoundEffects() {
//        File buttonSFX = new File("src/view/resources/sounds/bertrof-game-sound-correct.wav");

        //Media musicFile = new Media("file:///" + System.getProperty("user.dir") + "/src/view/" + file);
        //Media musicFile = new Media("file:///" + buttonSFX.getAbsolutePath());
//        this.buttonSound = new MediaPlayer(new Media("file:///" + buttonSFX.getAbsolutePath()));
        this.buttonSound = new MediaPlayer(new Media(getClass()
                .getResource("../view/resources/sounds/bertrof-game-sound-correct.wav").toExternalForm()));

        this.gameMusic = new MediaPlayer(new Media(getClass()
                .getResource("../view/resources/sounds/sirkoto51-retro-game-overworld-loop.wav").toExternalForm()));
        this.gameMusic.setCycleCount(MediaPlayer.INDEFINITE);

        this.dyingSound = new MediaPlayer(new Media(getClass()
                .getResource("../view/resources/sounds/jacksonacademyashmore-death.wav").toExternalForm()));

        this.powerUpSound = new MediaPlayer(new Media(getClass()
                .getResource("../view/resources/sounds/josepharaoh99-game-powerup.wav").toExternalForm()));

    }

    public void playButtonFX() {
        this.buttonSound.setVolume(0.8);
        buttonSound.play();
    }

    public void playGameMusic() {
        this.gameMusic.setVolume(1.0);
        gameMusic.play();
    }

    public void stopGameMusic() {
        this.gameMusic.stop();
    }

    public void pauseGameMusic() {
        this.gameMusic.pause();
    }

    public void dyingSoundPlay() {
        this.dyingSound.setVolume(2.0);
        dyingSound.play();
    }

    public void powerUpPlay(boolean play) {
        this.powerUpSound.setVolume(2.0);
        if (play) {
            powerUpSound.play();
        } else {
            powerUpSound.stop();
        }
    }
}
