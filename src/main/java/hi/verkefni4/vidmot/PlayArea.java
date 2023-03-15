package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PlayArea extends Pane {

    int distanceBetweenPlatforms;
    @FXML
    Player player;
    @FXML
    Circle ghostLeft;
    @FXML
    Circle ghostRight;
    @FXML
    ArrayList<Platform> platforms = new ArrayList<Platform>();

    @FXML
    Label lastScore;

    DoubleProperty opacity = new SimpleDoubleProperty(0);


    public PlayArea() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("play-view.fxml"));
        fxmlLoader.setRoot(this);   // rótin á viðmótstrénu sett hér
        fxmlLoader.setController(this); // controllerinn settur hér en ekki í .fxml skránni
        try {
            fxmlLoader.load();          // viðmótstréð lesið inn (þ.e. .fxml skráin)
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Upphafstilli pallastaðsetningu.
     * @param nrOfPlatforms fjöldi palla
     */
    public void resetGameObjects(int nrOfPlatforms) {
        distanceBetweenPlatforms = Game.GAME_HEIGHT / nrOfPlatforms;
        int ySpawnLevel = 300;
        Random rand = new Random();
        for(int i = 0; i < platforms.size(); i++)
        {
            platforms.get(i).activate();
            platforms.get(i).setX(rand.nextInt(Game.GAME_WIDTH - (int) platforms.get(i).getWidth()));
            platforms.get(i).setY(ySpawnLevel + distanceBetweenPlatforms * i);
        }
        player.setyLocation(player.getRadius());
        player.setxLocation(Game.GAME_WIDTH / 2);
    }


    /**
     * Stilli bindingar og takka
     */
    public void init() {
        Game.bindLastScore(lastScore);
        lastScore.opacityProperty().bind(opacity);
        lastScore.textFillProperty().bind(player.fillProperty());
        player.setGhost(ghostLeft, ghostRight);
        player.getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
                    switch (event.getCode()) {
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
                    switch (event.getCode()) {
                        case LEFT:
                            player.setMovingLeft(false);
                            break;
                        case RIGHT:
                            player.setMovingRight(false);
                            break;
                    }
                });
    }

    /**
     *  Uppfæri palla og spilara, stóri textinn fyrir miðju hverfur.
      * @param deltaTime sekúndur frá seinasta kalli á þessa aðferð
     */
    public void update(double deltaTime) {
        for (Platform platform : platforms) {
            platform.update(deltaTime);
            if (!player.isColliding())
                platform.checkCollision(deltaTime);
        }
        player.update(deltaTime);
        highscoreFadeOut(deltaTime);
    }

    /**
     * Stóru stig fyrir miðju hverfa með tíma.
     * hér er FADE_STEP 2 sem þýðir hann hverfur alveg á 2 sek.
     * @param deltaTime sekúndur frá seinasta kalli á þessa aðferð
     */
    private void highscoreFadeOut(double deltaTime){
        if(opacity.get() > 0) {
            final double FADE_STEP = 2;
            opacity.set(opacity.get() - FADE_STEP * deltaTime);
        }
    }

    /**
     * Stóru stig fyrir miðju birtast með tíma.
     * hér er FADE_STEP 1.2 sem þýðir hann birtist alveg á 1.2sek
     * @param deltaTime sekúndur frá seinasta kalli á þessa aðferð
     */
    private void highscoreFadeIn(double deltaTime){
        if(opacity.get() < 1) {
            final double FADE_STEP = 1.2;
            opacity.set(opacity.get() + FADE_STEP * deltaTime);
        }
    }

    /**
     * Stilli alla palla svarthvíta, stilli texta fyrir stigin sem leikmaður náði svo hann er fyrir miðju
     * Einnig láta hann birtast með tímanum.
     * Allir palla hverfa líka með tímanum.
     * Spilari uppfærist - en er nú í gameover mode og þá er ekki hægt að stýra honum fyrr en því er lokið.
      * @param deltaTime sekúndur frá seinasta kalli á þessa aðferð
     */
    public void handleGameOver(double deltaTime){
        lastScore.setTranslateX(Game.GAME_WIDTH / 2 - lastScore.getWidth() / 2);
        lastScore.setTranslateY(Game.GAME_HEIGHT / 2 - lastScore.getHeight() / 2);
        highscoreFadeIn(deltaTime);
        for(Platform platform : platforms){
            Color color = ((Color) platform.getFill()).grayscale();
            platform.setFill(color);
            platform.fadeOut(deltaTime);
        }
        player.update(deltaTime);
    }

    /**
     * Finn næsta platform sem er virkt og stilli það óvirkt
     */
    public void disablePlatform() {
        for(Platform platform : platforms)
            if(platform.deactivate()) return;
    }

    /**
     * Bý til palla og set inná play-view
     * @param nrOfPlatforms hversu marga palla.
     */
    public void createPlatforms(int nrOfPlatforms) {
        for (int i = 0; i < nrOfPlatforms; i++) {
            Platform platform = new Platform(player);
            platforms.add(platform);
            getChildren().add(platform);
            player.toFront();
        }
    }
}