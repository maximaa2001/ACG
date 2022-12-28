package by.bsuir.akg;

import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;
import by.bsuir.akg.service.DrawService;

import java.util.ArrayList;
import java.util.List;

import static by.bsuir.akg.util.MathHelper.*;

public class ModelService {
    private final List<? extends Model> models;
    private final Camera camera;

    public ModelService(List<? extends Model> models, Camera camera) {
        this.models = models;
        this.camera = camera;
    }

    public void render() {
        camera.updateScreenMatrix();
        List<List<Vertex>> screenTriangles = new ArrayList<>();
        for(Model model : models) {
            List<List<Vertex>> triangles = model.getFaces();
            for (List<Vertex> triangle : triangles) {
                Vector normal = normilizeVector(findNormalForTriangle(triangle));
                Vector position = normilizeVector(minus3(camera.getPosition(), triangle.get(0).getPosition()));
                float intensity = scalarMultiple(position, normal);
                if (intensity > 0) {
                    List<Vertex> screenTriangle = new ArrayList<>();
                    for (Vertex vertex : triangle) {
                        vertex.setW(camera.getWFromTransformationToScreenVector(vertex.getPosition()));
                        vertex.setPositionScreen(camera.transformToScreenVector(vertex.getPosition()));
                        screenTriangle.add(vertex);
                    }
                    screenTriangles.add(screenTriangle);
                }
            }
            drawObjects(screenTriangles);
        }
    }

    private void drawObjects(List<List<Vertex>> screenTriangles) {
        DrawService drawService = DrawService.getInstance(null);
        drawService.clear();
        for (List<Vertex> newTriangle : screenTriangles) {
            drawService.drawTriangle(newTriangle);
        }
        drawService.repaint();
    }
}
