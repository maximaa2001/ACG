package by.bsuir.akg;

import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.handler.EventHandler;
import by.bsuir.akg.util.Parser;
import javafx.stage.Stage;

import java.io.IOException;

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
        EventHandler.init(stage, camera);
        Model model = parser.readObject();
        modelService = new ModelService(model, camera);
        modelService.render();
    }

    public void render() {
        modelService.render();
    }
}
