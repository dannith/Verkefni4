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

    private final double SPEED_INCREASE = 600;


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
                        currentXSpeed -= (SPEED_INCREASE * deltaTime);
                        if(currentXSpeed < -Game.PLAYER_SPEED) currentXSpeed = -Game.PLAYER_SPEED;
                    } else{
                        currentXSpeed += (SPEED_INCREASE * deltaTime);
                        if(currentXSpeed > Game.PLAYER_SPEED) currentXSpeed = Game.PLAYER_SPEED;
                    }
                } else{
                    currentXSpeed *= 0.9;
                }
                double newXLoc = xLocation.get() + currentXSpeed * deltaTime;
                xLocation.set(newXLoc < 0 ? newXLoc + Game.GAME_WIDTH : newXLoc % Game.GAME_WIDTH);

                /*  Hér var ég með collision á veggina sitthvoru megin - tók út og setti inn ghost svo þú getur farið hringinn
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
                    checkOutofPlatform(deltaTime);
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
                xLocation.set(getCenterX() - stepX * Game.PLAYER_SPEED * deltaTime);
                yLocation.set(getCenterY() - stepY * Game.PLAYER_SPEED * deltaTime);
                final double MARGIN = 5;
                setColor();
                if(Math.abs(xLocation.get() - Game.GAME_WIDTH / 2) < MARGIN && Math.abs(yLocation.get() - getRadius()) < MARGIN)
                    Game.state = Game.State.START;
                break;
        }

    }


    double length;
    double stepX;
    double stepY;
    public void startEnd(){
        disconnect();
        currentXSpeed = 0;
        length = Math.sqrt(Math.pow(getCenterX() - (Game.GAME_WIDTH / 2),2) + Math.pow(getCenterY() - getRadius(),2));
        stepX = (getCenterX() - (Game.GAME_WIDTH / 2)) / length;
        stepY = (getCenterY() - getRadius()) / length;
        currentYSpeed = 0;
        Game.setFinalScore();
        Game.resetScore();
    }

    private void setColor(){
        color = getCenterY() / Game.GAME_HEIGHT;
        color = color < 0 ? 0 : color;
        color = color > 1.0 ? 1 : color;
        setFill(new Color(color, 1 - color, 0, 1));
    }

    private void checkOutofPlatform(double deltaTime) {
        if(getCenterX() < platformRef.getX() || getCenterX() > platformRef.getX() + platformRef.getWidth())
            disconnect();
    }
    public void setPlatform(Platform platform) {
        //Game.increaseScore(5);
        platformRef = platform;
        colliding = true;
    }

    public void checkPlatform(Platform platform) {
        if(colliding)
            if(platform.equals(platformRef))
                disconnect();
    }

    private void disconnect(){
        colliding = false;
        platformRef = null;
        currentYSpeed = 0;
    }
}
