package by.bsuir.akg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/by/bsuir/akg/render.fxml"));
        Parent root = loader.load();
        RenderController controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}