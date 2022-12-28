package by.bsuir.akg.entity.domain;

import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;
import by.bsuir.akg.util.TextureInitializer;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Board extends Model {

    public Board(List<List<Vertex>> triangles) throws IOException {
        super(triangles);
    }
}
