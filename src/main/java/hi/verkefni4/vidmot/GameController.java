package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameController {

    Game game;

    @FXML
    Label fxScore;

    public PlayArea getFxPlayArea() {
        return fxPlayArea;
    }

    @FXML
    PlayArea fxPlayArea;

    @FXML
    public void initialize() {
        game = new Game(fxScore);
        initGameLoop();
        fxPlayArea.initGameObjects(4, game);
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
        fxPlayArea.update();
        game.increaseScore(1);
    }

}