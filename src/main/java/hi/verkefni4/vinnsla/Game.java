package hi.verkefni4.vinnsla;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class Game {


    public enum State {
        START,
        ONGOING,
        END
    }

    private static IntegerProperty score = new SimpleIntegerProperty(0);

    public static void setFinalScore() {
        Game.finalScore.set(score.get());
    }
    public static void resetScore() {
        score.set(0);
    }

    private static IntegerProperty finalScore = new SimpleIntegerProperty(0);

    public static State state = State.ONGOING;
    public static final int GAME_HEIGHT = 600;
    public static final int GAME_WIDTH = 500;
    public final static int PLATFORM_SPEED = 60;
    private static double platformSpeedIncrease = 0;
    private final static double SPEED_STEP = 3;
    public final static int OUT_OF_BOUNDS = 40;
    public final static int PLAYER_SPEED = 300;
    public final static int FALL_SPEED = 3;
    public final static int MAX_PLATFORMS = 10;
    public final static int MIN_PLATFORMS = 2;
    public final static int SECONDS_PER_PLATFORM = 8;


    public static void bindScore(Label fxScore){
        fxScore.textProperty().bind(score.asString());
    }
    public static void bindLastScore(Label lastScore){
        lastScore.textProperty().bind(finalScore.asString());
    }

    public static double getPlatformSpeed() {
        return PLATFORM_SPEED + platformSpeedIncrease;
    }
    public static void resetSpeed(){
        platformSpeedIncrease = 0;
    }
    public static void increaseSpeed(double deltaTime){
        platformSpeedIncrease += deltaTime * SPEED_STEP;
    }
    public static void increaseScore(int tala){
        score.set(score.get() + tala);
    }


}
