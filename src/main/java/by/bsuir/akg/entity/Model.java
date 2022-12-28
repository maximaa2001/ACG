package by.bsuir.akg.entity;

import by.bsuir.akg.util.TextureInitializer;

import java.awt.image.BufferedImage;
import java.util.List;

public abstract class Model {
    private final List<List<Vertex>> triangles;

    private final double[][] translateMatrix = new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    };
    private final BufferedImage colorTexture = TextureInitializer.getInstance().getColorTexture();
    private final BufferedImage normalTexture = TextureInitializer.getInstance().getNormalTexture();
    private final BufferedImage mirrorTexture = TextureInitializer.getInstance().getMirrorTexture();

    public Model(List<List<Vertex>> triangles) {
        this.triangles = triangles;
    }

    public void translate(double x, double y, double z) {
        translateMatrix[0][3] = translateMatrix[0][3] + x;
        translateMatrix[1][3] = translateMatrix[1][3] + y;
        translateMatrix[2][3] = translateMatrix[2][3] + z;
    }

    public double[][] getTranslateMatrix() {
        return translateMatrix;
    }

    public List<List<Vertex>> getFaces() {
        return triangles;
    }
}
