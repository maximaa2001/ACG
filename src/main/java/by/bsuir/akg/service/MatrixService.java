package by.bsuir.akg.service;

import by.bsuir.akg.constant.Const;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;

@Deprecated
public class MatrixService {
    private static final double DEFAULT_W = 1;
    private static final double WIDTH = Const.WIDTH;
    private static final double HEIGHT = Const.HEIGHT;
    private static double Znear = 0.1;
    private static double Zfar = 100;
   // private static double fov = Math.PI / 3;
   private static double fov = 60;

    private static double[] CAMERA_POSITION = new double[]{3, 3, 3};
    private static double[] CAMERA_TARGET = new double[]{0, 0, 0};
    private static double[] UP = new double[]{0, 1, 0};

    public static RealMatrix createTransformMatrix(double transformX, double transformY, double transformZ) {
        return new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, transformX},
                {0, 1, 0, transformY},
                {0, 0, 1, transformZ},
                {0, 0, 0, 1}});
    }

    public static RealMatrix createScaleMatrix(double scaleX, double scaleY, double scaleZ) {
        return new Array2DRowRealMatrix(new double[][]{
                {scaleX, 0, 0, 0},
                {0, scaleY, 0, 0},
                {0, 0, scaleZ, 0},
                {0, 0, 0, 1}});
    }

    public static RealMatrix createRotateXMatrix(int angle) {
        return new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, 0},
                {0, Math.cos(angle), -Math.sin(angle), 0},
                {0, Math.sin(angle), Math.cos(angle), 0},
                {0, 0, 0, 1}});
    }

    public static RealMatrix createRotateYMatrix(int angle) {
        return new Array2DRowRealMatrix(new double[][]{
                {Math.cos(angle), 0, Math.sin(angle), 0},
                {0, 1, 0, 0},
                {-Math.sin(angle), 0, Math.cos(angle), 0},
                {0, 0, 0, 1}});
    }

    public static RealMatrix createRotateZMatrix(int angle) {
        return new Array2DRowRealMatrix(new double[][]{
                {Math.cos(angle), -Math.sin(angle), 0, 0},
                {Math.sin(angle), Math.cos(angle), 0, 0},
                {0, 0, 1, 0}, {0, 0, 0, 1}});
    }

    public static RealMatrix createViewMatrix() {
        double[] ZAxis = normalize(minus(CAMERA_POSITION, CAMERA_TARGET));
        double[] XAxis = normalize(vectMult(UP, ZAxis));
//        double[] YAxis = UP;
        double[] YAxis = vectMult(ZAxis, XAxis);
        return new Array2DRowRealMatrix(new double[][]{
                {XAxis[0], XAxis[1], XAxis[2], -(new ArrayRealVector(XAxis).dotProduct(new ArrayRealVector(CAMERA_POSITION)))},
                {YAxis[0], YAxis[1], YAxis[2], -(new ArrayRealVector(YAxis).dotProduct(new ArrayRealVector(CAMERA_POSITION)))},
                {ZAxis[0], ZAxis[1], ZAxis[2], -(new ArrayRealVector(ZAxis).dotProduct(new ArrayRealVector(CAMERA_POSITION)))},
                {0, 0, 0, 1}});
    }

    public static RealMatrix createProjectionMatrix() {
//        return new Array2DRowRealMatrix(new double[][]{
//                {2 * Znear / WIDTH, 0, 0, 0},
//                {0, 2 * Znear / HEIGHT, 0, 0},
//                {0, 0, Zfar / (Znear - Zfar), Znear * Zfar / (Znear - Zfar)},
//                {0, 0, -1, 0}});
        double aspect = HEIGHT / WIDTH;
        return new Array2DRowRealMatrix(new double[][]{
                {1/(aspect * Math.tan(fov / 2)), 0, 0, 0},
                {0, 1/Math.tan(fov/2), 0, 0},
                {0, 0, Zfar / (Znear - Zfar), Znear * Zfar / (Znear - Zfar)},
                {0, 0, -1, 0}});
    }

    public static RealMatrix createWindowMatrix() {
        double xmin = 0;
        double ymin = 0;
        return new Array2DRowRealMatrix(new double[][]{
                {WIDTH / 2, 0, 0, xmin + WIDTH / 2},
                {0, -HEIGHT / 2, 0, ymin + HEIGHT / 2},
                {0, 0, 1, 0},
                {0, 0, 0, 1}});
    }

    public static RealMatrix multiplyMatrix(RealMatrix matrix1, RealMatrix matrix2) {
        return matrix1.multiply(matrix2);
    }

    private static double[] vectMult(double[] vector1, double[] vector2) {
        double tmpX = vector1[1] * vector2[2] - vector1[2] * vector2[1];
        double tmpY = -(vector1[0] * vector2[2] - vector1[2] * vector2[0]);
        double tmpZ = vector1[0] * vector2[1] - vector1[1] * vector2[0];
        return new double[]{tmpX, tmpY, tmpZ};
    }

    private static double[] normalize(double[] vector) {
        double sqrt = Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2) + Math.pow(vector[2], 2));
        return new double[]{vector[0] / sqrt, vector[1] / sqrt, vector[2] / sqrt};
    }

    private static double[] minus(double[] vector1, double[] vector2) {
        ArrayRealVector a = new ArrayRealVector(vector1);
        ArrayRealVector b = new ArrayRealVector(vector2);
        return a.subtract(b).getDataRef();
    }

    private static double[][] coordinatesToMatrix(double x, double y, double z) {
        return new double[][]{{x}, {y}, {z}, {DEFAULT_W}};
    }

}
