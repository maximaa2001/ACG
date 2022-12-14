package by.bsuir.akg;

import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;
import by.bsuir.akg.service.DrawService;

import java.util.ArrayList;
import java.util.List;

public class ModelService {
    private final Model model;
    private final Camera camera;

    public ModelService(Model model, Camera camera) {
        this.model = model;
        this.camera = camera;
    }

    public void render() {
        camera.updateScreenMatrix();
        List<Float> intens = new ArrayList<>();
        List<List<Vertex>> newTriangles = new ArrayList<>();
        List<List<Vertex>> triangles = model.getFaces();
        for (List<Vertex> triangle : triangles) {
            Vector normal = normalize(findNormal(triangle));
            Vector position = normalize(camera.getPosition().minus3(triangle.get(0).position));
            float intensity = scalarMult(position, normal);
            if (intensity > 0) {
      //          float intensity = scalarMult(new Vector(position.getX(), position.getY(), position.getZ()), normal);
                List<Vertex> collect = new ArrayList<>();
                for (Vertex vertex : triangle) {
                    vertex.w = camera.getWFromTransformationToScreenVector(vertex.position);
                    vertex.position_screen = camera.transformToScreenVector(vertex.position);
                    collect.add(vertex);
                }
                newTriangles.add(collect);
                intens.add(intensity);
            }
        }
        DrawService drawService = DrawService.getInstance(null);
        drawService.clear();
        for (List<Vertex> newTriangle : newTriangles) {
            drawService.drawTriangle(newTriangle);
        }
        drawService.repaint();
    }

    public static Vector findNormal(List<Vertex> triangle) {
        Vector p1 = triangle.get(0).position;
        Vector p2 = triangle.get(1).position;
        Vector p3 = triangle.get(2).position;

        double x1 = p1.getX() - p2.getX();
        double y1 = p1.getY() - p2.getY();
        double z1 = p1.getZ() - p2.getZ();

        Vector v1 = new Vector(x1, y1, z1);

        double x2 = p2.getX() - p3.getX();
        double y2 = p2.getY() - p3.getY();
        double z2 = p2.getZ() - p3.getZ();

        Vector v2 = new Vector(x2, y2, z2);

        double x3 = (v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        double y3 = (v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        double z3 = (v1.getX() * v2.getY() - v1.getY() * v2.getX());

        return new Vector(x3, y3, z3);
    }

    private static double sqr(double x) {
        return x * x;
    }

    public static double angle(Vector vector1, Vector vector2) {
        double ab = vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
        double a = Math.sqrt(sqr(vector1.getX()) + sqr(vector1.getY()) + sqr(vector1.getZ()));
        double b = Math.sqrt(sqr(vector2.getX()) + sqr(vector2.getY()) + sqr(vector2.getZ()));
        return Math.toDegrees(Math.acos(ab / (a * b)));
    }

    private static float scalarMult(Vector vector1, Vector vector2) {
        return (float) (vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ());
    }

    private Vector normalize(Vector vector) {
        double sqrt = Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
        return new Vector(vector.getX() / sqrt, vector.getY() / sqrt, vector.getZ() / sqrt);
    }
}
