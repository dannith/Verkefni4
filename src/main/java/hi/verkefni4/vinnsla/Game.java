package hi.verkefni4.vinnsla;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;


public class Game {


    /**
     * Leikjastöður
     *  START: Núllstillir leikinn í updateloop.
     *  ONGOING: Leikurinn heldur áfram og pallar koma upp, uppfæra hraða osfrv.
     *  END: Bolti færir sig að upphafstöðu og byrjar nýjan leik (núllstillir stig).
     */
    public enum State {
        START,
        ONGOING,
        END
    }

    private static IntegerProperty score = new SimpleIntegerProperty(0);
    private static IntegerProperty finalScore = new SimpleIntegerProperty(0);
    public static State state = State.START;

    /**
     * Allar þessar breytur eru einskonar config, hægt er að breyta grunnstillingum leiks hér.
     */
    public static final int GAME_HEIGHT = 600; // Leikborðs hæð
    public static final int GAME_WIDTH = 500; // Leikborðs breidd
    public final static int PLATFORM_SPEED = 60; // Grunnhraði palla
    private static double platformSpeedIncrease = 0; // Núverandi hraðaaukning palla (Hraði palla er PLATFORM_SPEED + platformSpeedIncrease
    private final static double SPEED_STEP = 3; // Hröðun palla.
    public final static int PLATFORM_OUT_OF_BOUNDS = 40; // Lokastaða pallar.
    public final static double PLAYER_MAX_SPEED = 300; // Hámarkshraði spilara
    public final static double SPEED_INCREASE = 1200; // Hröðun spilara
    public final static double FALL_SPEED = 9.8; // Þyngdarafl kúlu
    public final static int MAX_PLATFORMS = 10; // Byrjunarfjöldi palla
    public final static int MIN_PLATFORMS = 2; // Lokafjöldi palla
    public final static double SECONDS_PER_PLATFORM = 8; // Hve langur tími í sec á að líða til að fækka um 1 pall.

    /**
     * Stillir stóru stigin við miðju skjás
     */
    public static void setFinalScore() {
        Game.finalScore.set(score.get());
    }

    /**
     * Núllstilla stigin í lok leiks
     */
    public static void resetScore() {
        score.set(0);
    }

    /**
     * Binda saman label við stig.
     * @param fxScore
     */
    public static void bindScore(Label fxScore){
        fxScore.textProperty().bind(score.asString());
    }

    /**
     * Binda saman label við lokastig (sem birtast fyrir miðju skjás)
     * @param lastScore
     */
    public static void bindLastScore(Label lastScore){
        lastScore.textProperty().bind(finalScore.asString());
    }

    /**
     * Fæ núverandi hraða palla.
     * @return
     */
    public static double getPlatformSpeed() {
        return PLATFORM_SPEED + platformSpeedIncrease;
    }

    /**
     * Núllstilla aukahraða palla.
     */
    public static void resetSpeed(){
        platformSpeedIncrease = 0;
    }

    /**
     * Hækkar hraðann á pöllum miðað við tíma og multiplyer sem er SPEED_STEP
     * Þeas hann hækkar alltaf með tímanum.
     * @param deltaTime
     */
    public static void increaseSpeed(double deltaTime){
        platformSpeedIncrease += deltaTime * SPEED_STEP;
    }

    /**
     * Hækkar stigin.
     * @param tala
     */
    public static void increaseScore(int tala){
        score.set(score.get() + tala);
    }


}
