package hi.verkefni4.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameController {

    @FXML
    private VBox fxGame;

    @FXML
    Label fxStig;

    public PlayArea getFxPlayArea() {
        return fxPlayArea;
    }

    @FXML
    PlayArea fxPlayArea;

    @FXML
    Player player;

    @FXML
    public void initialize() {
        initGameLoop();
        fxPlayArea.initPlayer();
        fxPlayArea.initPlatforms(5);
    }

    private void initGameLoop() {
        KeyFrame k = new KeyFrame(Duration.millis(20),
                e-> {
                    gameLoop();
                });
        Timeline t = new Timeline(k);
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }

    private void gameLoop() {
        fxPlayArea.updateBall();
        fxPlayArea.updatePlatforms();
    }

}