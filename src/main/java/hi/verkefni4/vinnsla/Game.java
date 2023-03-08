package hi.verkefni4.vinnsla;

import javafx.beans.property.IntegerProperty;

public class Game {

    private IntegerProperty score;

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void increaseScore(){
        score.set(score.get() + 1);
    }

}
