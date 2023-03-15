package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import hi.verkefni4.vinnsla.GameObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class Player extends Circle implements GameObject {


    private double color = 1;
    private boolean movingLeft = false;

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    private boolean movingRight = false;

    public boolean isColliding() {
        return colliding;
    }

    private boolean colliding = false;

    private double currentXSpeed;

    private double currentYSpeed;

    public double getCurrentYSpeed() {
        return currentYSpeed;
    }


    public void setxLocation(double xLocation) {
        this.xLocation.set(xLocation);
    }

    public void setyLocation(double yLocation) {
        this.yLocation.set(yLocation);
    }

    private DoubleProperty xLocation = new SimpleDoubleProperty(250);
    private DoubleProperty yLocation = new SimpleDoubleProperty(0);

    private Platform platformRef = null;

    public Player () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("player-view.fxml"));
        fxmlLoader.setRoot(this);   // rótin á viðmótstrénu sett hér
        fxmlLoader.setController(this); // controllerinn settur hér en ekki í .fxml skránni
        try {
            fxmlLoader.load();          // viðmótstréð lesið inn (þ.e. .fxml skráin)
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.centerXProperty().bind(xLocation);
        this.centerYProperty().bind(yLocation);
        currentYSpeed = 0;
    }

    public void setGhost(Circle left, Circle right){
        left.fillProperty().bind(fillProperty());
        left.centerYProperty().bind(centerYProperty());
        left.centerXProperty().bind(centerXProperty().subtract(Game.GAME_WIDTH));
        right.fillProperty().bind(fillProperty());
        right.centerYProperty().bind(centerYProperty());
        right.centerXProperty().bind(centerXProperty().add(Game.GAME_WIDTH));
    }

    public void update(double deltaTime){
        switch(Game.state){
            case ONGOING:
                setColor();
                if(movingLeft != movingRight){
                    if(movingLeft){
                        currentXSpeed -= (Game.SPEED_INCREASE * deltaTime);
                        if(currentXSpeed < -Game.PLAYER_MAX_SPEED) currentXSpeed = -Game.PLAYER_MAX_SPEED;
                    } else{
                        currentXSpeed += (Game.SPEED_INCREASE * deltaTime);
                        if(currentXSpeed > Game.PLAYER_MAX_SPEED) currentXSpeed = Game.PLAYER_MAX_SPEED;
                    }
                } else{
                    currentXSpeed *= 0.9;
                }
                double newXLoc = xLocation.get() + currentXSpeed * deltaTime;
                xLocation.set(newXLoc < 0 ? newXLoc + Game.GAME_WIDTH : newXLoc % Game.GAME_WIDTH);

                /*  Hér var ég með collision á veggina sitthvoru megin - tók út og setti inn ghost svo þú getur farið hringinn
                    Og hinn helmingurinn af spilaranum sést koma í gegn, þannig þú sérð alltaf spilarann.
                if(getCenterX() < getRadius()) {
                    xLocation.set(getRadius());
                    currentXSpeed = 0;
                }
                if(getCenterX() > Game.GAME_WIDTH - getRadius()) {
                    xLocation.set(Game.GAME_WIDTH - getRadius());
                    currentXSpeed = 0;
                }
                 */

                if(colliding){
                    yLocation.set(platformRef.getY() - getRadius());
                    checkOutofPlatform();
                } else {
                    yLocation.set(yLocation.get() + currentYSpeed);
                    currentYSpeed += Game.FALL_SPEED * deltaTime;
                }
                if(getCenterY() >= Game.GAME_HEIGHT - getRadius()){
                    startEnd();
                    Game.state = Game.State.END;
                }
                break;
            case END:
                xLocation.set(getCenterX() - stepX * Game.PLAYER_MAX_SPEED * deltaTime);
                yLocation.set(getCenterY() - stepY * Game.PLAYER_MAX_SPEED * deltaTime);
                final double MARGIN = 5;
                setColor();
                if(Math.abs(xLocation.get() - Game.GAME_WIDTH / 2) < MARGIN && Math.abs(yLocation.get() - getRadius()) < MARGIN)
                    Game.state = Game.State.START;
                break;
        }

    }

    double stepX;
    double stepY;

    /**
     * Finn vigur fyrir player til að fara eftir að upphafsstað áður en leikur byrjar aftur
     * Uppfæri stóru stigin sem birtast á miðju skjás
     * Núllstilli núverandi hraða á x og y ás svo hann byrji ekki næsta leik á fullri ferð.
     */
    public void startEnd(){
        disconnect();
        double length = Math.sqrt(Math.pow(getCenterX() - (Game.GAME_WIDTH / 2),2) + Math.pow(getCenterY() - getRadius(),2));
        stepX = (getCenterX() - (Game.GAME_WIDTH / 2)) / length;
        stepY = (getCenterY() - getRadius()) / length;
        currentYSpeed = 0;
        currentXSpeed = 0;
        Game.setFinalScore();
        Game.resetScore();
    }

    /**
     * Uppfæri núverandi lit spilara miðað við hvar hann er á leikborðinu:
     * Því ofar sem hann er því grænni, því neðar því rauðari verður hann.
     */
    private void setColor(){
        color = getCenterY() / Game.GAME_HEIGHT;
        color = color < 0 ? 0 : color;
        color = color > 1.0 ? 1 : color;
        setFill(new Color(color, 1 - color, 0, 1));
    }

    /**
     * Athuga hvort spilari er kominn út fyrir
     */
    private void checkOutofPlatform() {
        if(getCenterX() < platformRef.getX() || getCenterX() > platformRef.getX() + platformRef.getWidth())
            disconnect();
    }

    /**
     * Stilli platform sem spilari er á
     * @param platform
     */
    public void setPlatform(Platform platform) {
        //Game.increaseScore(5);
        platformRef = platform;
        colliding = true;
    }

    /**
     * Athuga hvort spilari sé staddur við platform, ef svo er þá aftengjast.
     * Það er aðeins kallað á þetta fall þegar platform er komið efst upp og hverfur.
     * @param platform
     */
    public void checkPlatform(Platform platform) {
        if(colliding)
            if(platform.equals(platformRef))
                disconnect();
    }

    /**
     * Afterngir spilara við platform, og núllstillir fallhraðann.
     */
    private void disconnect(){
        colliding = false;
        platformRef = null;
        currentYSpeed = 0;
    }
}
