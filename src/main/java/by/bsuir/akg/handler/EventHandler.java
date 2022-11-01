package by.bsuir.akg.handler;

import by.bsuir.akg.Game;
import by.bsuir.akg.entity.Camera;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class EventHandler {

    private static Game game = Game.getGame(null);

    public static void init(Stage stage, Camera camera) {
        stage.getScene().setOnKeyPressed(new javafx.event.EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case D -> {
                        camera.rotateY(5);
                        Game game = Game.getGame(null);
                        game.render();
                    }
                    case A -> {
                        camera.rotateY(-5);
                        game.render();
                    }
                    case W -> {
                        camera.rotateX(5);
                        game.render();
                    }
                    case S -> {
                        camera.rotateX(-5);
                        game.render();
                    }
                    case Q -> {
                        camera.rotateZ(5);
                        game.render();
                    }
                    case E -> {
                        camera.rotateZ(-5);
                        game.render();
                    }
                    case EQUALS -> {
                        camera.transform(-1);
                        game.render();
                    }
                    case MINUS -> {
                        camera.transform(1);
                        game.render();
                    }
                }
            }
        });
    }
}
