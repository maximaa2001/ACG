package by.bsuir.akg.entity;

import by.bsuir.akg.util.TextureInitializer;

import java.awt.image.BufferedImage;
import java.util.List;

public abstract class Model {
    private final List<List<Vertex>> triangles;
    private final BufferedImage colorTexture = TextureInitializer.getInstance().getColorTexture();
    private final BufferedImage normalTexture = TextureInitializer.getInstance().getNormalTexture();
    private final BufferedImage mirrorTexture = TextureInitializer.getInstance().getMirrorTexture();

    public Model(List<List<Vertex>> triangles) {
        this.triangles = triangles;
    }

    public List<List<Vertex>> getFaces() {
        return triangles;
    }
}
