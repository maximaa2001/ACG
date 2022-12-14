package by.bsuir.akg.entity;

import by.bsuir.akg.constant.Const;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;

public class Camera {
    private Vector position = new Vector(20.0, 10.0, 10.0);
    private Vector target = new Vector(-5.0, 3.0, 0.0);
    private Vector up = new Vector(0.0, 1.0, 0.0);
    private Double fow = Math.PI / 4f;

    private Double near = 0.1;
    private Double far = 100.0;

    private RealMatrix viewMatrix = createViewMatrix();
    private RealMatrix projectionMatrix = createProjectionMatrix();
    private RealMatrix windowMatrix = createWindowMatrix();

    public void updateScreenMatrix() {
        viewMatrix = createViewMatrix();
        projectionMatrix = createProjectionMatrix();
        windowMatrix = createWindowMatrix();
    }

    public Double getWFromTransformationToScreenVector(Vector worldVector){
        RealMatrix fullMatrix = projectionMatrix.multiply(viewMatrix);
        Vector newbie = multMatrixVector(worldVector, fullMatrix);
        return newbie.getW();
    }
    public Vector transformToScreenVector(Vector worldVector) {
        RealMatrix fullMatrix = projectionMatrix.multiply(viewMatrix);
        Vector newbie = multMatrixVector(worldVector, fullMatrix);
        RealMatrix divide = divide(newbie, newbie.getW());
        return multMatrixVector(new Vector(divide.getData()[0][0], divide.getData()[1][0], divide.getData()[2][0]), windowMatrix);
//        Vector vector = multMatrixVector(worldVector, viewMatrix);
//        Vector vector1 = multMatrixVector(vector, projectionMatrix);
//        RealMatrix divide = divide(vector, vector1.getW());
//        Vector vector2 = new Vector(divide.getData()[0][0], divide.getData()[1][0], divide.getData()[2][0]);
//        return multMatrixVector(vector2, windowMatrix);
    }

    private Vector multMatrixVector(Vector vector, RealMatrix matrix) {
        double[] res = new double[]{0, 0, 0, 0};
        double[] asList = new double[]{vector.getX(), vector.getY(), vector.getZ(), vector.getW()};
        for (int row = 0; row <= 3; row++) {
            for (int column = 0; column <= 3; column++) {
                res[row] += matrix.getData()[row][column] * asList[column];
            }
        }
        return new Vector(res[0], res[1], res[2], res[3]);
    }

    private RealMatrix createViewMatrix() {
        Vector ZAxis = normalize(minus(position, target));
        Vector XAxis = normalize(vectMult(up, ZAxis));
        Vector YAxis = vectMult(ZAxis, XAxis);
        return new Array2DRowRealMatrix(new double[][]{
                {XAxis.getX(), XAxis.getY(), XAxis.getZ(), -(new ArrayRealVector(toDouble(XAxis)).dotProduct(new ArrayRealVector(toDouble(position))))},
                {YAxis.getX(), YAxis.getY(), YAxis.getZ(), -(new ArrayRealVector(toDouble(YAxis)).dotProduct(new ArrayRealVector(toDouble(position))))},
                {ZAxis.getX(), ZAxis.getY(), ZAxis.getZ(), -(new ArrayRealVector(toDouble(ZAxis)).dotProduct(new ArrayRealVector(toDouble(position))))},
                {0, 0, 0, 1}});
    }

    private RealMatrix createProjectionMatrix() {
        double aspect = (double) Const.WIDTH / Const.HEIGHT;
        return new Array2DRowRealMatrix(new double[][]{
                {1 / (aspect * Math.tan(fow / 2)), 0, 0, 0},
                {0, 1 / Math.tan(fow / 2), 0, 0},
                {0, 0, far / (near - far), near * far / (near - far)},
                {0, 0, -1, 0}});
    }

    private RealMatrix createWindowMatrix() {
        double xmin = 0;
        double ymin = 0;
        return new Array2DRowRealMatrix(new double[][]{
                {Const.WIDTH / 2.0, 0, 0, xmin + Const.WIDTH / 2.0},
                {0, -Const.HEIGHT / 2.0, 0, ymin + Const.HEIGHT / 2.0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}});
    }

    private Vector normalize(Vector vector) {
        double sqrt = Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
        return new Vector(vector.getX() / sqrt, vector.getY() / sqrt, vector.getZ() / sqrt);
    }

    private Vector vectMult(Vector vector1, Vector vector2) {
        double tmpX = vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY();
        double tmpY = -(vector1.getX() * vector2.getZ() - vector1.getZ() * vector2.getX());
        double tmpZ = vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX();
        return new Vector(tmpX, tmpY, tmpZ);
    }

    private Vector minus(Vector vector1, Vector vector2) {
        ArrayRealVector a = new ArrayRealVector(new double[]{vector1.getX(), vector1.getY(), vector1.getZ()});
        ArrayRealVector b = new ArrayRealVector(new double[]{vector2.getX(), vector2.getY(), vector2.getZ()});
        double[] dataRef = a.subtract(b).getDataRef();
        return new Vector(dataRef[0], dataRef[1], dataRef[2]);
    }

    private double[] toDouble(Vector vector) {
        return new double[]{vector.getX(), vector.getY(), vector.getZ()};
    }

    private RealMatrix divide(Vector vector, double w) {
        return new Array2DRowRealMatrix(new double[][]{{vector.getX() / w}, {vector.getY() / w}, {vector.getZ() / w}, {vector.getW() / w}});
    }

    public void rotateY(double angle) {
        Degree degree = new Degree(angle);
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {Math.cos(degree.toRadian()), 0, Math.sin(degree.toRadian()), 0},
                {0, 1, 0, 0},
                {-Math.sin(degree.toRadian()), 0, Math.cos(degree.toRadian()), 0},
                {0, 0, 0, 1}});
        RealMatrix multiply = matrix.multiply(new Array2DRowRealMatrix(new double[][]{{position.getX()}, {position.getY()}, {position.getZ()}, {position.getW()}}));
        position = new Vector(multiply.getData()[0][0], multiply.getData()[1][0], multiply.getData()[2][0]);
    }

    public void rotateX(double angle) {
        Degree degree = new Degree(angle);
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, 0},
                {0, Math.cos(degree.toRadian()), -Math.sin(degree.toRadian()), 0},
                {0, Math.sin(degree.toRadian()), Math.cos(degree.toRadian()), 0},
                {0, 0, 0, 1}});
        RealMatrix multiply = matrix.multiply(new Array2DRowRealMatrix(new double[][]{{position.getX()}, {position.getY()}, {position.getZ()}, {position.getW()}}));
        position = new Vector(multiply.getData()[0][0], multiply.getData()[1][0], multiply.getData()[2][0]);
    }

    public void rotateZ(double angle) {
        Degree degree = new Degree(angle);
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {Math.cos(degree.toRadian()), -Math.sin(degree.toRadian()), 0, 0},
                {Math.sin(degree.toRadian()), Math.cos(degree.toRadian()), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}});
        RealMatrix multiply = matrix.multiply(new Array2DRowRealMatrix(new double[][]{{position.getX()}, {position.getY()}, {position.getZ()}, {position.getW()}}));
        position = new Vector(multiply.getData()[0][0], multiply.getData()[1][0], multiply.getData()[2][0]);
    }

    public void transform(double diff) {
        double radius = Math.sqrt(Math.pow(position.getX(), 2) + Math.pow(position.getY(), 2) + Math.pow(position.getZ(), 2));
        int signA = sign(position.getX()) * sign(position.getY());
        double oldA = (position.getZ() == 0f) ? 0f : signA * Math.atan(Math.sqrt(Math.pow(position.getX(), 2) + Math.pow(position.getY(), 2)) / position.getZ());
        double oldB = (position.getX() == 0f) ? 0f : Math.atan(position.getY() / position.getX());
        position = new Vector(
                (radius + diff) * Math.sin(oldA) * Math.cos(oldB),
                (radius + diff) * Math.sin(oldA) * Math.sin(oldB),
                (radius + diff) * Math.cos(oldA)
        );
    }

    private int sign(double coord) {
        if (coord == 0) {
            return 0;
        } else if (coord > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public Vector getPosition() {
        return position;
    }

}
