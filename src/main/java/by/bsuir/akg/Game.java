package by.bsuir.akg;

import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.handler.EventHandler;
import by.bsuir.akg.util.Parser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Game {
    private static Game game;
    private Parser parser = new Parser();
    private Camera camera = new Camera();

    private ModelService modelService;

    private Stage stage;

    private Game(Stage stage) {
        this.stage = stage;
    }

    public static Game getGame(Stage stage) {
        if(game == null) {
            game = new Game(stage);
        }
        return game;
    }

    public void create() throws IOException {
        List<? extends Model> models = parser.readObject();
        EventHandler.init(stage, camera, models);
        modelService = new ModelService(models, camera);
        render();
    }

    public void render() {
        modelService.render();
    }
}
