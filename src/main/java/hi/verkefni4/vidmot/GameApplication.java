package hi.verkefni4.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 620);
        stage.setTitle("BBBBBBounceddddddddownnn!");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        GameController controller = fxmlLoader.getController();
        controller.getFxPlayArea().init();
    }

    public static void main(String[] args) {
        launch();
    }
}