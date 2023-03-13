package hi.verkefni4.vinnsla;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class Game {

    private IntegerProperty score = new SimpleIntegerProperty(0);

    public static final int GAME_HEIGHT = 600;
    public static final int GAME_WIDTH = 500;
    public final static int PLATFORM_SPEED = 60;
    private static double platformSpeedIncrease = 0;
    private final static double SPEED_STEP = 3;
    public final static int OUT_OF_BOUNDS = -20;
    public final static int PLAYER_SPEED = 300;
    public final static int FALL_SPEED = 200;
    public final static int MAX_PLATFORMS = 10;
    public final static int MIN_PLATFORMS = 1;
    public final static int SECONDS_PER_PLATFORM = 10;


    public Game(Label fxScore){
        fxScore.textProperty().bind(scoreProperty().asString());
    }

    public static double getPlatformSpeed() {
        return PLATFORM_SPEED + platformSpeedIncrease;
    }

    public static void increaseSpeed(double deltaTime){
        platformSpeedIncrease += deltaTime * SPEED_STEP;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void increaseScore(int tala){
        score.set(score.get() + tala);
    }


}
