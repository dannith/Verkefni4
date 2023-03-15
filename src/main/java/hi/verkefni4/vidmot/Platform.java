package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import hi.verkefni4.vinnsla.GameObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.Random;

public class Platform extends Rectangle implements GameObject {

    private Random rand;

    private boolean isActive;
    private boolean updating;
    private double red;
    private double green;
    private double blue;
    private boolean redGlow = true;
    private boolean greenGlow = true;
    private boolean blueGlow = true;
    private double colorStep;
    private static final double COLOR_MIN = 0.05; // Vil ekki fá alveg svarta palla
    private static final double COLOR_MAX = 0.95; // Vil ekki fá alveg hvíta palla
    private Player playerRef;
    DoubleProperty opacity = new SimpleDoubleProperty(0);

    public Platform(Player player) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("platform-view.fxml"));
        fxmlLoader.setRoot(this);   // rótin á viðmótstrénu sett hér
        fxmlLoader.setController(this); // controllerinn settur hér en ekki í .fxml skránni
        try {
            fxmlLoader.load();          // viðmótstréð lesið inn (þ.e. .fxml skráin)
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        playerRef = player;
        rand = new Random();
        opacityProperty().bind(opacity);
        isActive = true;
        updating = true;
        setColor();
    }

    /**
     * Hverri uppfærslu af leik uppfæra pall, lit, gegnsæisleika, staðsetningu
     * @param deltaTime  sekúndur frá seinasta kalli á þetta fall
     */
    public void update(double deltaTime) {
        if(updating) {
            this.setY(this.getY() - Game.getPlatformSpeed() * deltaTime);
            // Ef pallurinn er kominn 100 pixlum frá lokasvæði palls, byrja hverfa
            if (this.getY() < 100 + Game.PLATFORM_OUT_OF_BOUNDS) {
                // stilli hlutfall gegnsæisleika miðað við fjarlægð frá endapunkti
                // t.d: 40 pixlum frá endasvæði → gegnsæisgildið verður 0.4
                opacity.set((getY() - Game.PLATFORM_OUT_OF_BOUNDS) / 100);
            } else if(opacity.get() < 1.0){
                // Þetta er aðeins satt í byrjun leiks.
                fadeIn(deltaTime);
            }
            if (this.getY() < Game.PLATFORM_OUT_OF_BOUNDS) {
                setColor();
                newPos(deltaTime);
                opacity.set(1);
            }
            setFill(updateColor(deltaTime));
        }
    }

    /**
     * Færir pall niður á nýjan byrjunarstað, ef hann er virkur, ef hann var merkur óvirkur þá færist hann útfyrir leiksvæðið og hættir að hreyfast.
     * @param deltaTime sekúndur frá seinasta kalli á þetta fall
     */
    private void newPos(double deltaTime){
        playerRef.checkPlatform(this);
        if(isActive) {
            final int OFFSET = 150;
            this.setX(rand.nextInt((int)(Game.GAME_WIDTH - getWidth())));
            this.setY((int) ((Game.getPlatformSpeed() + Game.PLATFORM_SPEED) * deltaTime + Game.GAME_HEIGHT) + rand.nextInt(OFFSET));
            setColor();
        } else {
            this.setY(Game.GAME_HEIGHT * 2);
            updating = false;
        }
    }

    /**
     * Athugar hvort neðsti punktur bolta snerti efsta part palls
     * @param deltaTime sekúndur frá seinasta kalli á þetta fall
     */
    public void checkCollision(double deltaTime){
        if(playerRef.getCenterX() > getX() && playerRef.getCenterX() < getX() + getWidth())
            if(Math.abs(playerRef.getCenterY() + playerRef.getRadius() - getY()) < playerRef.getCurrentYSpeed() + Game.getPlatformSpeed() * deltaTime){
                playerRef.setPlatform(this);
            }
    }

    /**
     * Býr til nýjan lit fyrir pall og hversu hratt hann eigi að ítra í gegnum litagildin.
     * semsagt allt frá 0.25 - 0.49 hröðun á ítrun þar sem 0 er enginn litur og 1 hæsta gildi á lit
     */
    private void setColor(){
        colorStep = (rand.nextInt(25) + 25) * 0.01;
        red = rand.nextDouble();
        green = rand.nextDouble();
        blue = rand.nextDouble();
        setFill(new Color(red * 0.01, green * 0.01, blue * 0.01, 1.0));
    }

    /**
     * Hækkar eða lækkar R,G og B gildið á núverandi lit
     * ATH: Ég gerði fyrst hjálparfall en þar sem það er ekki leið til að gera pass by reference á
     * grunntög (int double, boolean osfrv) sem ég veit af var það ekki góð lausn, myndi þurfa búa til sér
     * klasa fyrir litina svo ég útfærði þetta svona.
     * @param deltaTime sekúndur frá seinasta kalli á þetta fall
     * @return
     */
    private Color updateColor(double deltaTime){
        if(redGlow){
            red += (colorStep * deltaTime);
            if(red > COLOR_MAX){
                red = COLOR_MAX;
                redGlow = false;
            }
        } else{
            red -= (colorStep * deltaTime);
            if(red < COLOR_MIN){
                red = COLOR_MIN;
                redGlow = true;
            }
        }
        if(greenGlow){
            green += (colorStep * deltaTime);
            if(green > COLOR_MAX){
                green = COLOR_MAX;
                greenGlow = false;
            }
        } else{
            green -= (colorStep * deltaTime);
            if(green < COLOR_MIN){
                green = COLOR_MIN;
                greenGlow = true;
            }
        }
        if(blueGlow){
            blue += (colorStep * deltaTime);
            if(blue > COLOR_MAX){
                blue = COLOR_MAX;
                blueGlow = false;
            }
        } else{
            blue -= (colorStep * deltaTime);
            if(blue < COLOR_MIN){
                blue = COLOR_MIN;
                blueGlow = true;
            }
        }
        return new Color(red, green, blue, 1.0);
    }

    /**
     * Merkir pallinn sem óvirkan og skilar hvort hann hafi verið óvirkur,
     * hann heldur áfram að uppfærast þrátt fyrir að vera óvirkur, en eftir hann hverfur efst næst
     * hættir hann að uppfærast
     * @return skilar true ef hann var virkur en false ef hann var óvirkur
     */
    public boolean deactivate() {
        if(isActive) {
            isActive = false;
            return true;
        }
        return false;
    }

    /**
     * Merkir pallinn virkan og að hann eigi að uppfærast
     */
    public void activate(){
        isActive = true;
        updating = true;
    }

    /**
     * Pallur birtist yfir tíma
     * @param deltaTime sekúndur frá seinasta kalli á þetta fall
     */
    public void fadeIn(double deltaTime){
        if(opacity.get() < 1.0) {
            final double FADE_STEP = 0.5;
            opacity.set(opacity.get() + FADE_STEP * deltaTime);
        }
    }

    /**
     * Pallur hverfur yfir tíma
     * @param deltaTime sekúndur frá seinasta kalli á þetta fall
     */
    public void fadeOut(double deltaTime) {
        if(opacity.get() > 0) {
            final double FADE_STEP = 0.8;
            opacity.set(opacity.get() - FADE_STEP * deltaTime);
        }
    }
}
