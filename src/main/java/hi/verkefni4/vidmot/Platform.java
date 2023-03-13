package hi.verkefni4.vidmot;

import hi.verkefni4.vinnsla.Game;
import hi.verkefni4.vinnsla.GameObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.Random;

public class Platform extends Rectangle implements GameObject {

    private Random rand;

    private boolean isActive;
    private boolean updating;

    private double red;
    private double green;
    private double blue;
    private boolean redGlow = true;
    private boolean greenGlow = true;
    private boolean blueGlow = true;

    private double colorStep = 0.5;
    private static final double COLOR_MIN = 0.05;
    private static final double COLOR_MAX = 0.95;

    private Player playerRef;

    public Platform(Player player) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("platform-view.fxml"));
        fxmlLoader.setRoot(this);   // rótin á viðmótstrénu sett hér
        fxmlLoader.setController(this); // controllerinn settur hér en ekki í .fxml skránni
        try {
            fxmlLoader.load();          // viðmótstréð lesið inn (þ.e. .fxml skráin)
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        playerRef = player;
        rand = new Random();
        isActive = true;
        updating = true;
        setColor();
    }

    public void update(double deltaTime) {
        if(updating) {
            this.setY(this.getY() - Game.getPlatformSpeed() * deltaTime);
            if (this.getY() < Game.OUT_OF_BOUNDS) {
                setColor();
                newPos();
            }
            double litur = ((getY() - Game.OUT_OF_BOUNDS) / Game.GAME_HEIGHT);
            if (litur > 1)
                litur = 1;
            this.setStrokeWidth((this.getHeight() / 2) * litur);
            setFill(updateColor(deltaTime));
        }
    }

    private void newPos(){
        playerRef.checkPlatform(this);
        if(isActive) {
            final int OFFSET = 100;
            this.setX(rand.nextInt(Game.GAME_WIDTH) - (getWidth() / 2));
            this.setY(Game.GAME_HEIGHT + rand.nextInt(OFFSET));
            // Nýjan lit
            setColor();
        } else {
            this.setY(Game.GAME_HEIGHT * 2);
            updating = false;
        }

    }

    public boolean checkCollision(Player playerRef, double deltaTime){
        boolean colliding = false;
        if(playerRef.getCenterX() > getX() && playerRef.getCenterX() < getX() + getWidth())
            if(Math.abs(playerRef.getCenterY() + playerRef.getRadius() - getY()) < Game.FALL_SPEED * deltaTime + Game.getPlatformSpeed() * deltaTime){
                playerRef.setPlatform(this);
                colliding = true;
            }
        return colliding;
    }

    private void setColor(){
        colorStep = (rand.nextInt(25) + 25) * 0.01;
        red = rand.nextDouble();
        green = rand.nextDouble();
        blue = rand.nextDouble();
        setFill(new Color(red * 0.01, green * 0.01, blue * 0.01, 1.0));
    }

    private Color updateColor(double deltaTime){
        if(redGlow){
            red += (colorStep * deltaTime);
            if(red > COLOR_MAX){
                red = COLOR_MAX;
                redGlow = false;
            }
        } else{
            red -= (colorStep * deltaTime);
            if(red < COLOR_MIN){
                red = COLOR_MIN;
                redGlow = true;
            }
        }
        if(greenGlow){
            green += (colorStep * deltaTime);
            if(green > COLOR_MAX){
                green = COLOR_MAX;
                greenGlow = false;
            }
        } else{
            green -= (colorStep * deltaTime);
            if(green < COLOR_MIN){
                green = COLOR_MIN;
                greenGlow = true;
            }
        }
        if(blueGlow){
            blue += (colorStep * deltaTime);
            if(blue > COLOR_MAX){
                blue = COLOR_MAX;
                blueGlow = false;
            }
        } else{
            blue -= (colorStep * deltaTime);
            if(blue < COLOR_MIN){
                blue = COLOR_MIN;
                blueGlow = true;
            }
        }
        return new Color(red, green, blue, 1.0);
    }

    public boolean deactivate() {
        if(isActive) {
            isActive = false;
            return true;
        }
        return false;
    }
}
