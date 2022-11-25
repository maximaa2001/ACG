package by.bsuir.akg.service;

import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;

import java.util.List;

public class InterpolationService {

    public static Vector interpolation(double px, double py, List<Vertex> triangle) {
        double x1 = triangle.get(0).position_screen.getX();
        double x2 = triangle.get(1).position_screen.getX();
        double x3 = triangle.get(2).position_screen.getX();

        double y1 = triangle.get(0).position_screen.getY();
        double y2 = triangle.get(1).position_screen.getY();
        double y3 = triangle.get(2).position_screen.getY();

        double div = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        double w1 = ((y2 - y3) * (px - x3) + (x3 - x2) * (py - y3)) / div;
        double w2 = ((y3 - y1) * (px - x3) + (x1 - x3) * (py - y3)) / div;
        double w3 = 1 - w1 - w2;

        Vector normal = getInterpolationVector(
                triangle.get(0).normal,
                triangle.get(1).normal,
                triangle.get(2).normal,
                w1, w2, w3);
        Vector position = getInterpolationVector(
                triangle.get(0).position,
                triangle.get(1).position,
                triangle.get(2).position,
                w1, w2, w3);
        return PhongShader.getPhongColor(position, normal);
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
}
