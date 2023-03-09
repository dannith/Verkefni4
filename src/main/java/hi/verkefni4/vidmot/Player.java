package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import hi.verkefni4.vinnsla.GameObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Currency;

public class Player extends Circle implements GameObject {

    private static final int ACCEL = 6;
    public static  final int FALL_SPEED = 4;

    private boolean movingLeft = false;

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    private boolean movingRight = false;
    private boolean colliding = false;

    private DoubleProperty xLocation = new SimpleDoubleProperty(250);
    private DoubleProperty yLocation = new SimpleDoubleProperty(0);


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

    public void update(){
        if(movingLeft != movingRight){
            if(movingLeft){
                xLocation.set(xLocation.get() - ACCEL);
                if(getCenterX() < getRadius())
                    xLocation.set(getRadius());
            } else{
                xLocation.set(xLocation.get() + ACCEL);
                if(getCenterX() > Game.GAME_WIDTH - getRadius())
                    xLocation.set(Game.GAME_WIDTH - getRadius());
            }
        }
        if(colliding){
            yLocation.set(yLocation.get() - Game.getPlatformSpeed());
        } else {
            yLocation.set(yLocation.get() + FALL_SPEED);
        }
    }

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }
}
