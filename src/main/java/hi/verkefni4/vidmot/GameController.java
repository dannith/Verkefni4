package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {
    private double deltaTime;
    private long lastTime;
    private double elapsedTime = 0;
    private int elapsedSeconds = 0;
    private int nrOfPlatforms;
    @FXML
    Label fxScore;

    public PlayArea getFxPlayArea() {
        return fxPlayArea;
    }

    @FXML
    PlayArea fxPlayArea;

    /**
     * Grunnstilla allt og búa til palla.
     */
    @FXML
    public void initialize() {
        deltaTime = 0;
        lastTime = System.nanoTime();
        Game.state = Game.State.START;
        Game.bindScore(fxScore);
        initGameLoop();
        fxPlayArea.createPlatforms(Game.MAX_PLATFORMS);
    }

    /**
     * Stilla hvað skuli gerast á 20ms fresti.
     */
    private void initGameLoop() {
        KeyFrame k = new KeyFrame(Duration.millis(20),
                e-> {
                    gameLoop();
                });
        Timeline t = new Timeline(k);
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }

    /**
     * Loopan sem á að vera keyrð á 20ms fresti.
     * Bætti inn deltaTime sem reiknar út tíma frá seinasta kalli á þetta (ætti að vera 20ms en er ekki alltaf)
     * fyrir betri hreyfingar og updates.
     */
    private void gameLoop() {
        long time = System.nanoTime();
        deltaTime = (time - lastTime) / 1000000000.0; // Seconds
        lastTime = time;
        elapsedTime += deltaTime;
        switch(Game.state) {
            case START:
                //Stillingar fyrir nýjan leik
                Game.resetSpeed();
                fxPlayArea.resetGameObjects(Game.MAX_PLATFORMS);
                nrOfPlatforms = Game.MAX_PLATFORMS;
                elapsedSeconds = 0;
                elapsedTime = 0;
                Game.state = Game.State.ONGOING;
                break;
            case ONGOING:
                //Leikur er í gangi
                fxPlayArea.update(deltaTime);
                Game.increaseSpeed(deltaTime);
                if (elapsedTime > 1) {
                    Game.increaseScore(1); // Hækka stigin um 1 fyrir hverja sekúndu liðna
                    elapsedSeconds++;
                    elapsedTime--;
                    if (elapsedSeconds % Game.SECONDS_PER_PLATFORM == 0
                            && nrOfPlatforms > Game.MIN_PLATFORMS) {
                        nrOfPlatforms--;
                        fxPlayArea.disablePlatform();
                    }
                }
                break;
            case END:
                //Leikur hefur klárast og endurstillir sig.
                fxPlayArea.handleGameOver(deltaTime);
                break;
        }
    }

}