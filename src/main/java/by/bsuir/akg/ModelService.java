package by.bsuir.akg;

import by.bsuir.akg.entity.Camera;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.service.DrawService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelService {
    private final Model model;
    private final Camera camera;

    public ModelService(Model model, Camera camera) {
        this.model = model;
        this.camera = camera;
    }

    public void render() {
        camera.updateScreenMatrix();
        List<List<Vector>> newTriangles = new ArrayList<>();
        List<List<Vector>> triangles = model.getFaces();
        for (List<Vector> list : triangles) {
            Vector normal = findNormal(list);
            double angle = angle(camera.getPosition(), normal);
            if(angle < 90) {
                List<Vector> collect = list.stream().map(camera::transformToScreenVector).collect(Collectors.toList());
                newTriangles.add(collect);
            }
        }
        DrawService drawService = DrawService.getInstance(null);
        drawService.clear();
        newTriangles.forEach(drawService::drawTriangle);
        drawService.repaint();
    }

    public static Vector findNormal(List<Vector> triangle) {
        Vector p1 = triangle.get(0);
        Vector p2 = triangle.get(1);
        Vector p3 = triangle.get(2);

        double a = p1.getX() - p2.getX();
        double b = p1.getY() - p2.getY();
        double c = p1.getZ() - p2.getZ();

        Vector v1 = new Vector(a, b, c);

        double d = p2.getX() - p3.getX();
        double e = p2.getY() - p3.getY();
        double f = p2.getZ() - p3.getZ();

        Vector v2 = new Vector(d, e, f);

        double wrki = Math.sqrt(sqr(v1.getY() * v2.getZ() - v1.getZ() * v2.getY()) + sqr(v1.getZ() * v2.getX() - v1.getX() * v2.getZ()) + sqr(v1.getX() * v2.getY() - v1.getY() * v2.getX()));
        double g = (v1.getY() * v2.getZ() - v1.getZ() * v2.getY()) / wrki;
        double h = (v1.getZ() * v2.getX() - v1.getX() * v2.getZ()) / wrki;
        double i = (v1.getX() * v2.getY() - v1.getY() * v2.getX()) / wrki;

        return new Vector(g, h, i);

    }

    private static double sqr(double x) {
        return x * x;
    }

    public static double angle(Vector vector1, Vector vector2) {
        double ab = vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
        double a = Math.sqrt(sqr(vector1.getX()) + sqr(vector1.getY()) + sqr(vector1.getZ()));
        double b = Math.sqrt(sqr(vector2.getX()) + sqr(vector2.getY()) + sqr(vector2.getZ()));
        return Math.toDegrees(Math.acos(ab/(a * b)));
    }
}
