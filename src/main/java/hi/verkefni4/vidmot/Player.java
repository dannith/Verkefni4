package hi.verkefni4.vidmot;

import javafx.fxml.FXMLLoader;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class Player extends Circle {

    private static final int MAX_SPEED = 4;
    private static final int SLOW_DOWN = 1;

    private double currentSpeed = 0;

    public Player () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("player-view.fxml"));
        fxmlLoader.setRoot(this);   // rótin á viðmótstrénu sett hér
        fxmlLoader.setController(this); // controllerinn settur hér en ekki í .fxml skránni
        try {
            fxmlLoader.load();          // viðmótstréð lesið inn (þ.e. .fxml skráin)
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void update(){
        this.setCenterX(this.getCenterX() + currentSpeed);
    }

    public void updateSpeed(double speed){
        currentSpeed += speed;
        if(currentSpeed > MAX_SPEED)
            currentSpeed = MAX_SPEED;
        else if(currentSpeed < -MAX_SPEED)
            currentSpeed = -MAX_SPEED;
    }

}
