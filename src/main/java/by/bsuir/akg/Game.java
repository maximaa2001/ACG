package by.bsuir.akg;

import by.bsuir.akg.constant.GameObjectDefine;
import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Gamer;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.domain.Checker;
import by.bsuir.akg.service.game.GameService;
import by.bsuir.akg.util.Parser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Game {
    private static Game game;
    private final Parser parser = new Parser();
    private final Camera camera = new Camera();

    private Map<GameObjectDefine, List<? extends Model>> models;

    private ModelService modelService;

    private GameService gameService;

    private final Stage stage;

    private Game(Stage stage) {
        this.stage = stage;
    }

    public static Game getGame(Stage stage) {
        if (game == null) {
            game = new Game(stage);
        }
        return game;
    }

    public void create() throws IOException {
        models = parser.readObject();
        gameService = new GameService(models, stage, camera);
        modelService = new ModelService(models.values().stream().flatMap(Collection::stream).collect(Collectors.toList()), camera);
        render();
    }

    public void remove(Model model) {
        modelService.remove(model);
    }


    public void render() {
        modelService.render();
    }
}
