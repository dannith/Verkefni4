package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import hi.verkefni4.vinnsla.GameObject;
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

    Game game;

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

    public void initGameObjects(int nrOfPlatforms, Game game){
        this.game = game;
        distanceBetweenPlatforms = Game.GAME_HEIGHT / nrOfPlatforms;
        int ySpawnLevel = 300;
        Random rand = new Random();
        for(int i = 0; i < nrOfPlatforms; i++){
            Platform platform = new Platform(player);
            platform.setX(rand.nextInt(Game.GAME_WIDTH - (int) platform.getWidth()));
            platform.setY(ySpawnLevel + distanceBetweenPlatforms * i);
            platforms.add(platform);
            getChildren().add(platform);
            player.toFront();
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

    public void update(){
        player.update();
        boolean colliding = false;
        for (Platform platform : platforms) {
            platform.update();
            if(!colliding) {
                colliding = platform.getBoundsInParent().intersects(player.getBoundsInParent());
                if(colliding)
                    colliding = player.getCenterY() + player.getRadius() - Player.FALL_SPEED * 2 < platform.getY();
            }
        }
        player.setColliding(colliding);
        if(game.scoreProperty().get() % 2000 == 0){
            Game.increaseSpeed();
        }
    }
}