package by.bsuir.akg.entity;

import by.bsuir.akg.constant.Const;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

public class Camera {
    private Vector position = new Vector(3.0, 3.0, 1.0);
    private Vector target = new Vector(0.0, 0.0, 0.0);
    private Vector up = new Vector(0.0, 1.0, 0.0);
    private Double fow = Math.PI / 3f;

    private Double near = 0.1;
    private Double far = 100.0;

    private RealMatrix viewMatrix = createViewMatrix();
    private RealMatrix projectionMatrix = createProjectionMatrix();
    private RealMatrix windowMatrix = createWindowMatrix();

    private RealMatrix resultMatrix = viewMatrix.multiply(projectionMatrix).multiply(windowMatrix);

    public void updateScreenMatrix() {
        viewMatrix = createViewMatrix();
        projectionMatrix = createProjectionMatrix();
        windowMatrix = createWindowMatrix();
        resultMatrix = viewMatrix.multiply(projectionMatrix).multiply(windowMatrix);
    }

    public Vector transformToScreenVector(Vector worldVector) {
        RealMatrix fullMatrix = projectionMatrix.multiply(viewMatrix);
       // Array2DRowRealMatrix array2DRowRealMatrix = new Array2DRowRealMatrix(new double[][]{{worldVector.getX()}, {worldVector.getY()}, {worldVector.getZ()}, {worldVector.getW()}});
        Vector newbie = test(worldVector, fullMatrix);
        RealMatrix divide = divide(newbie, newbie.getW());
        return test(new Vector(divide.getData()[0][0], divide.getData()[1][0], divide.getData()[2][0]),windowMatrix);
    }
    private Vector test(Vector vector, RealMatrix matrix) {
        double[] res = new double[]{0,0,0,0};
        double[] asList = new double[]{vector.getX(),vector.getY(),vector.getZ(),vector.getW()};
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
//        double[] YAxis = UP;
        Vector YAxis = vectMult(ZAxis, XAxis);
        return new Array2DRowRealMatrix(new double[][]{
                {XAxis.getX(), XAxis.getY(), XAxis.getZ(), -(new ArrayRealVector(toDouble(XAxis)).dotProduct(new ArrayRealVector(toDouble(position))))},
                {YAxis.getX(), YAxis.getY(), YAxis.getZ(), -(new ArrayRealVector(toDouble(YAxis)).dotProduct(new ArrayRealVector(toDouble(position))))},
                {ZAxis.getX(), ZAxis.getY(), ZAxis.getZ(), -(new ArrayRealVector(toDouble(ZAxis)).dotProduct(new ArrayRealVector(toDouble(position))))},
                {0, 0, 0, 1}});
    }

    private RealMatrix createProjectionMatrix() {
        double aspect = (double)Const.HEIGHT / Const.WIDTH;
        return new Array2DRowRealMatrix(new double[][]{
                {1/(aspect * Math.tan(fow / 2)), 0, 0, 0},
                {0, 1/Math.tan(fow/2), 0, 0},
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

    private RealMatrix divide(RealMatrix realMatrix, double w) {
        double[][] data = realMatrix.getData();
        return new Array2DRowRealMatrix(new double[][]{{data[0][0] / w}, {data[1][0] / w}, {data[2][0] / w}, {data[3][0] / w}});
    }

    private RealMatrix divide(Vector vector, double w) {
        return new Array2DRowRealMatrix(new double[][]{{vector.getX() / w}, {vector.getY() / w}, {vector.getZ() / w}, {vector.getW() / w}});
    }

}
