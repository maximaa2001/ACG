package by.bsuir.akg.util;

import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

public class MathHelper {

    private MathHelper() {
    }

    public static Vector normilizeVector(Vector vector) {
        double sqrt = Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
        return new Vector(vector.getX() / sqrt, vector.getY() / sqrt, vector.getZ() / sqrt);
    }

    public static Vector findNormalForTriangle(List<Vertex> triangle) {
        Vector p1 = triangle.get(0).getPosition();
        Vector p2 = triangle.get(1).getPosition();
        Vector p3 = triangle.get(2).getPosition();

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

    public static Vector minus3(Vector vector1, Vector vector2) {
        return new Vector(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
    }

    public static float scalarMultiple(Vector vector1, Vector vector2) {
        return (float) (vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ());
    }

    public static Vector multMatrixVector(Vector vector, RealMatrix matrix) {
        double[] res = new double[]{0, 0, 0, 0};
        double[] asList = new double[]{vector.getX(), vector.getY(), vector.getZ(), vector.getW()};
        for (int row = 0; row <= 3; row++) {
            for (int column = 0; column <= 3; column++) {
                res[row] += matrix.getData()[row][column] * asList[column];
            }
        }
        return new Vector(res[0], res[1], res[2], res[3]);
    }
}
