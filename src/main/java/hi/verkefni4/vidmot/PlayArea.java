package hi.verkefni4.vidmot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayArea extends Pane {

    int bilMilliPlatforms;
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
        bilMilliPlatforms = 600 / nrOfPlatforms;
        Random rand = new Random();
        for(int i = 0; i < nrOfPlatforms; i++){
            Platform platform = new Platform();
            platform.setX(rand.nextInt(400));
            platform.setY(100 + bilMilliPlatforms * i);
            platforms.add(platform);
            getChildren().add(platform);
        }
    }


    public void initInput() {
        player.getScene().addEventFilter(KeyEvent.ANY,
                event -> {
                    switch(event.getCode()) {
                        case LEFT:
                            player.updateSpeed(-4);
                            break;
                        case RIGHT:
                            player.updateSpeed(4);
                            break;
                    }
                });
    }

    public void initPlayer(){
        player.setCenterX(250);
    }

    public void updateBall(){
        player.update();
    }

    public void updatePlatforms(){
        for(Platform platform : platforms)
        {
            platform.update();
        }
    }
}