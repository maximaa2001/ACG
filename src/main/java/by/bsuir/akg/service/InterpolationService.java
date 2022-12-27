package by.bsuir.akg.service;

import by.bsuir.akg.entity.Texture;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;

import java.util.ArrayList;
import java.util.List;

public class InterpolationService {

    public static List<Vector> interpolationNormalPositionLine(double px, Vertex vertex1, Vertex vertex2) {
        double x1 = vertex1.getPositionScreen().getX();
        double x2 = vertex2.getPositionScreen().getX();
        double w2 = (px - x1) / (x2 - x1);
        double w1 = 1 - w2;
        Vector normal = getInterpolationVector2W(vertex1.getNormal(), vertex2.getNormal(), w1, w2);
        Vector position = getInterpolationVector2W(vertex1.getPosition(), vertex2.getPosition(), w1, w2);
        List<Vector> res = new ArrayList<>();
        res.add(normal);
        res.add(position);
        return res;
    }

    public static double interpolationZ(double px, Vertex startVertex, Vertex endVertex) {
        double x1 = startVertex.getPositionScreen().getX();
        double x2 = endVertex.getPositionScreen().getX();

        double z1 = startVertex.getPositionScreen().getZ();
        double z2 = endVertex.getPositionScreen().getZ();

        double w2 = (px - x1) / (x2 - x1);
        double w1 = 1 - w2;
        return w1 * z1 + w2 * z2;
    }

    public static double interpolationZTriangle(double px, double py, List<Vertex> triangle, List<Double> ks) {

        double z1 = triangle.get(0).getPositionScreen().getZ();
        double z2 = triangle.get(1).getPositionScreen().getZ();
        double z3 = triangle.get(2).getPositionScreen().getZ();

        double w1 = ks.get(0);
        double w2 = ks.get(1);
        double w3 = ks.get(2);

        return w1 * z1 + w2 * z2 + w3 * z3;
    }

    public static List<Vector> interpolationNormalPosition(double px, double py, List<Vertex> triangle) {
        List<Double> listW = getInterpolationKs(triangle, px, py);

        double w1 = listW.get(0);
        double w2 = listW.get(1);
        double w3 = listW.get(2);

        Vector normal = getInterpolationVector(
                triangle.get(0).getNormal(),
                triangle.get(1).getNormal(),
                triangle.get(2).getNormal(),
                w1, w2, w3);
        Vector position = getInterpolationVector(
                triangle.get(0).getPosition(),
                triangle.get(1).getPosition(),
                triangle.get(2).getPosition(),
                w1, w2, w3);
        List<Vector> res = new ArrayList<>();
        res.add(normal);
        res.add(position);
        return res;
    }

    public static List<Double> getInterpolationKs(List<Vertex> triangle, double px, double py) {
        double x1 = triangle.get(0).getPositionScreen().getX();
        double x2 = triangle.get(1).getPositionScreen().getX();
        double x3 = triangle.get(2).getPositionScreen().getX();

        double y1 = triangle.get(0).getPositionScreen().getY();
        double y2 = triangle.get(1).getPositionScreen().getY();
        double y3 = triangle.get(2).getPositionScreen().getY();

        double div = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);
        double w1 = ((y2 - y3) * (px - x3) + (x3 - x2) * (py - y3)) / div;
        double w2 = ((y3 - y1) * (px - x3) + (x1 - x3) * (py - y3)) / div;
        double w3 = 1 - w1 - w2;

        return List.of(w1, w2, w3);
    }

    public static Texture interpolateTexture(List<Vertex> triangle, List<Double> ks) {
        double u1 = triangle.get(0).getTexture().getX();
        double u2 = triangle.get(1).getTexture().getX();
        double u3 = triangle.get(2).getTexture().getX();

        double v1 = triangle.get(0).getTexture().getY();
        double v2 = triangle.get(1).getTexture().getY();
        double v3 = triangle.get(2).getTexture().getY();

        Double w1 = triangle.get(0).getW();
        Double w2 = triangle.get(1).getW();
        Double w3 = triangle.get(2).getW();

        double k1 = ks.get(0);
        double k2 = ks.get(1);
        double k3 = ks.get(2);

        double u = (k1 * u1 / w1 + k2 * u2 / w2 + k3 * u3 / w3) / (k1 / w1 + k2 / w2 + k3 / w3);
        double v = (k1 * v1 / w1 + k2 * v2 / w2 + k3 * v3 / w3) / (k1 / w1 + k2 / w2 + k3 / w3);
        return new Texture(u, v);
    }

    public static Vector interpolateWorldVectorWithPerspective(List<Vertex> triangle, List<Double> ks) {
        double x1 = triangle.get(0).getPosition().getX();
        double x2 = triangle.get(1).getPosition().getX();
        double x3 = triangle.get(2).getPosition().getX();

        double y1 = triangle.get(0).getPosition().getY();
        double y2 = triangle.get(1).getPosition().getY();
        double y3 = triangle.get(2).getPosition().getY();

        double z1 = triangle.get(0).getPosition().getZ();
        double z2 = triangle.get(1).getPosition().getZ();
        double z3 = triangle.get(2).getPosition().getZ();

        Double w1 = triangle.get(0).getW();
        Double w2 = triangle.get(1).getW();
        Double w3 = triangle.get(2).getW();

        double k1 = ks.get(0);
        double k2 = ks.get(1);
        double k3 = ks.get(2);

        double x = (k1 * x1 / w1 + k2 * x2 / w2 + k3 * x3 / w3) / (k1 / w1 + k2 / w2 + k3 / w3);
        double y = (k1 * y1 / w1 + k2 * y2 / w2 + k3 * y3 / w3) / (k1 / w1 + k2 / w2 + k3 / w3);
        double z = (k1 * z1 / w1 + k2 * z2 / w2 + k3 * z3 / w3) / (k1 / w1 + k2 / w2 + k3 / w3);
        return new Vector(x, y, z);
    }

    private static Vector getInterpolationVector(Vector vector1, Vector vector2, Vector vector3, double w1, double w2, double w3) {
        double vector_x1 = vector1.getX();
        double vector_x2 = vector2.getX();
        double vector_x3 = vector3.getX();

        double vector_y1 = vector1.getY();
        double vector_y2 = vector2.getY();
        double vector_y3 = vector3.getY();

        double vector_z1 = vector1.getZ();
        double vector_z2 = vector2.getZ();
        double vector_z3 = vector3.getZ();

        double vector_px = w1 * vector_x1 + w2 * vector_x2 + w3 * vector_x3;
        double vector_py = w1 * vector_y1 + w2 * vector_y2 + w3 * vector_y3;
        double vector_pz = w1 * vector_z1 + w2 * vector_z2 + w3 * vector_z3;

        return (new Vector(vector_px, vector_py, vector_pz));
    }

    private static Vector getInterpolationVector2W(Vector vector1, Vector vector2, double w1, double w2) {
        double vector_x1 = vector1.getX();
        double vector_x2 = vector2.getX();

        double vector_y1 = vector1.getY();
        double vector_y2 = vector2.getY();

        double vector_z1 = vector1.getZ();
        double vector_z2 = vector2.getZ();

        double vector_px = w1 * vector_x1 + w2 * vector_x2;
        double vector_py = w1 * vector_y1 + w2 * vector_y2;
        double vector_pz = w1 * vector_z1 + w2 * vector_z2;

        return (new Vector(vector_px, vector_py, vector_pz));
    }
}
