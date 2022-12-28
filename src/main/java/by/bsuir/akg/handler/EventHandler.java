package by.bsuir.akg.handler;

import by.bsuir.akg.Game;
import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.List;

public class EventHandler {

    private static Game game = Game.getGame(null);

    public static void init(Stage stage, Camera camera, List<? extends Model> models) {
        stage.getScene().setOnKeyPressed(new javafx.event.EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case D -> {
                        camera.rotateY(20);
                       // models.get(1).translate(0,2,0);
                        game.render();
                    }
                    case A -> {
                        camera.rotateY(-20);
                      //  models.get(1).translate(0,-2,0);
                        game.render();
                    }
                    case W -> {
                        camera.rotateX(20);
                     //   models.get(1).translate(2,0,0);
                        game.render();
                    }
                    case S -> {
                        camera.rotateX(-20);
                      //  models.get(1).translate(-2,0,0);
                        game.render();
                    }
                    case Q -> {
                       // camera.rotateZ(20);
                        models.get(1).translate(0,0,4.35);
                        game.render();
                    }
                    case E -> {
                       // camera.rotateZ(-20);
                        models.get(1).translate(0,0,-4.35);
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
