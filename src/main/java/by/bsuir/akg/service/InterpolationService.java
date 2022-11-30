package by.bsuir.akg.service;

import by.bsuir.akg.entity.Vector;

public class InterpolationService {
    public static double interpolationZ(double px, double py, Vector vector1, Vector vector2) {
        double x1 = vector1.getX();
        double x2 = vector2.getX();

        double z1 = vector1.getZ();
        double z2 = vector2.getZ();
        double w2 = (px - x1) / (x2 - x1);
        double w1 = 1 - w2;
        return w1 * z1 + w2 * z2;
    }

    public static double interpolationZTriangle(double px, double py, Vector vector1, Vector vector2, Vector vector3) {
        double x1 = vector1.getX();
        double x2 = vector2.getX();
        double x3 = vector3.getX();

        double y1 = vector1.getY();
        double y2 = vector2.getY();
        double y3 = vector3.getY();

        double z1 = vector1.getZ();
        double z2 = vector2.getZ();
        double z3 = vector3.getZ();

        double div = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        double w1 = ((y2 - y3) * (px - x3) + (x3 - x2) * (py - y3)) / div;
        double w2 = ((y3 - y1) * (px - x3) + (x1 - x3) * (py - y3)) / div;
        double w3 = 1 - w1 - w2;

        return w1 * z1 + w2 * z2 + w3 * z3;
    }

}
