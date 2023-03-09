package hi.verkefni4.vidmot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PlayArea extends Pane {

    int distanceBetweenPlatforms;
    @FXML
    Player player;

    @FXML
    ArrayList<Platform> platforms = new ArrayList<Platform>();

    public PlayArea () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("play-view.fxml"));
        fxmlLoader.setRoot(this);   // rótin á viðmótstrénu sett hér
        fxmlLoader.setController(this); // controllerinn settur hér en ekki í .fxml skránni
        try {
            fxmlLoader.load();          // viðmótstréð lesið inn (þ.e. .fxml skráin)
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initPlatforms(int nrOfPlatforms){
        distanceBetweenPlatforms = 600 / nrOfPlatforms;
        Random rand = new Random();
        for(int i = 0; i < nrOfPlatforms; i++){
            Platform platform = new Platform();
            platform.setX(rand.nextInt(400));
            platform.setY(100 + distanceBetweenPlatforms * i);
            platforms.add(platform);
            platform.toBack();
            getChildren().add(platform);
        }
    }


    public void initInput() {
        player.getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
                    switch(event.getCode()) {
                        case LEFT:
                            player.setMovingLeft(true);
                            break;
                        case RIGHT:
                            player.setMovingRight(true);
                            break;
                    }
                });
        player.getScene().addEventFilter(KeyEvent.KEY_RELEASED,
                event -> {
                    switch(event.getCode()) {
                        case LEFT:
                            player.setMovingLeft(false);
                            break;
                        case RIGHT:
                            player.setMovingRight(false);
                            break;
                    }
                });
    }

    public void updateBall(){
        player.update();
    }

    public void updatePlatforms() {
        boolean colliding = false;
        for (Platform platform : platforms) {
            platform.update();
            if(!colliding)
                colliding = platform.getBoundsInParent().intersects(player.getBoundsInParent());
        }
        player.setColliding(colliding);
    }
}