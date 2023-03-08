package hi.verkefni4.vidmot;

import javafx.fxml.FXMLLoader;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.Random;

public class Platform extends Rectangle {

    private static int SPEED = 2;
    private static int OUT_OF_BOUNDS = -20;

    private Random rand;

    public Platform() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("platform-view.fxml"));
        fxmlLoader.setRoot(this);   // rótin á viðmótstrénu sett hér
        fxmlLoader.setController(this); // controllerinn settur hér en ekki í .fxml skránni
        try {
            fxmlLoader.load();          // viðmótstréð lesið inn (þ.e. .fxml skráin)
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        rand = new Random();
    }

    public void update() {
        this.setY(this.getY() - SPEED);
        if(this.getY() < OUT_OF_BOUNDS){
            newPos();
        }
    }

    private void newPos(){
        this.setX(rand.nextInt(480) - 50);
        this.setY(600 + rand.nextInt(80));
    }



}
