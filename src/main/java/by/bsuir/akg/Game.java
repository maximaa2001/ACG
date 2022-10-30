package by.bsuir.akg;

import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.util.Parser;

import java.io.IOException;

public class Game {
    private Parser parser = new Parser();
    private Camera camera = new Camera();
    public void create() throws IOException {
        Model model = parser.readObject();
        ModelService modelService = new ModelService(model, camera);
        modelService.render();
    }
}
