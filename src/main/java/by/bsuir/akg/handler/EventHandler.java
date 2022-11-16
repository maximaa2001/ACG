package by.bsuir.akg.handler;

import by.bsuir.akg.Game;
import by.bsuir.akg.entity.Camera;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class EventHandler {

    private static Game game = Game.getGame(null);

    public static void init(Stage stage, Camera camera) {
        stage.getScene().setOnKeyPressed(new javafx.event.EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case D -> {
                        camera.rotateY(20);
                        game.render();
                    }
                    case A -> {
                        camera.rotateY(-20);
                        game.render();
                    }
                    case W -> {
                        camera.rotateX(20);
                        game.render();
                    }
                    case S -> {
                        camera.rotateX(-20);
                        game.render();
                    }
                    case Q -> {
                        camera.rotateZ(20);
                        game.render();
                    }
                    case E -> {
                        camera.rotateZ(-20);
                        game.render();
                    }
                    case EQUALS -> {
                        camera.transform(-2);
                        game.render();
                    }
                    case MINUS -> {
                        camera.transform(2);
                        game.render();
                    }
                }
            }
        });
    }
}
