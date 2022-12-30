package by.bsuir.akg.entity;

import java.awt.*;
import java.util.List;

public abstract class Model {
    private final List<List<Vertex>> triangles;
    private Color customColor;

    private final double[][] translateMatrix = new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    };

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

    public void setCustomColor(Color customColor) {
        this.customColor = customColor;
    }

    public Color getCustomColor() {
        return customColor;
    }
}
