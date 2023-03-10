package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import hi.verkefni4.vinnsla.GameObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.Random;

public class Platform extends Rectangle implements GameObject {

    private static final int OUT_OF_BOUNDS = 40;

    private Random rand;

    public Platform(Player player) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("platform-view.fxml"));
        fxmlLoader.setRoot(this);   // rótin á viðmótstrénu sett hér
        fxmlLoader.setController(this); // controllerinn settur hér en ekki í .fxml skránni
        try {
            fxmlLoader.load();          // viðmótstréð lesið inn (þ.e. .fxml skráin)
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        rand = new Random();
        setColor();
    }

    public void update() {
        this.setY(this.getY() - Game.getPlatformSpeed());
        if(this.getY() < OUT_OF_BOUNDS){
            newPos();
        }
        double litur = ((getY() - OUT_OF_BOUNDS) / Game.GAME_HEIGHT);
        if(litur > 1)
            litur = 1;
        this.setStrokeWidth((this.getHeight() / 2) * litur);
    }

    private void newPos(){
        final int OFFSET = 100;
        this.setX(rand.nextInt(Game.GAME_WIDTH) - getWidth() / 2);
        this.setY(Game.GAME_HEIGHT + rand.nextInt(OFFSET));

        // Nýjan lit líka
        setColor();
    }

    private void setColor(){
        this.setStroke(new Color(rand.nextDouble(),rand.nextDouble(),rand.nextDouble(), 1.0));
    }



}
