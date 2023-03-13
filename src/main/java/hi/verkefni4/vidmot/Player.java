package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import hi.verkefni4.vinnsla.GameObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Currency;

public class Player extends Circle implements GameObject {


    double color = 1;

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
    }

    public void update(double deltaTime){
        setColor();
        if(movingLeft != movingRight){
            if(movingLeft){
                xLocation.set(xLocation.get() - Game.PLAYER_SPEED * deltaTime);
                if(getCenterX() < getRadius())
                    xLocation.set(getRadius());
            } else{
                xLocation.set(xLocation.get() + Game.PLAYER_SPEED * deltaTime);
                if(getCenterX() > Game.GAME_WIDTH - getRadius())
                    xLocation.set(Game.GAME_WIDTH - getRadius());
            }
        }
        if(colliding){
            yLocation.set(platformRef.getY() - getRadius());
            checkOutofPlatform();
        } else {
            yLocation.set(yLocation.get() + Game.FALL_SPEED * deltaTime);
        }
    }

    private void setColor(){
        color = getCenterY() / Game.GAME_HEIGHT;
        color = color < 0 ? 0 : color;
        color = color > 1.0 ? 1 : color;
        setFill(new Color(color, 1 - color, 0, 1));
    }

    private void checkOutofPlatform() {
        if(
                getCenterX() < platformRef.getX() ||
                getCenterX() > platformRef.getX() + platformRef.getWidth()
        ){
            colliding = false;
            platformRef = null;
        }
    }
    public void setPlatform(Platform platform) {
        platformRef = platform;
        colliding = true;
    }

    public void checkPlatform(Platform platform) {
        if(colliding)
            if(platform.equals(platformRef))
            {
                colliding = false;
                platformRef = null;
            }
    }
}
