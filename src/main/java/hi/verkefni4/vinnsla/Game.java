package hi.verkefni4.vinnsla;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class Game {

    private IntegerProperty score = new SimpleIntegerProperty(0);

    public static final int GAME_HEIGHT = 600;
    public static final int GAME_WIDTH = 500;

    public static int getPlatformSpeed() {
        return PLATFORM_SPEED;
    }

    public static void increaseSpeed(){
        PLATFORM_SPEED++;
    }

    private static int PLATFORM_SPEED = 2;

    public Game(Label fxScore){
        fxScore.textProperty().bind(scoreProperty().asString());
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void increaseScore(int tala){
        score.set(score.get() + tala);
    }


}
