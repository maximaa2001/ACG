package by.bsuir.akg.service;

import by.bsuir.akg.RenderController;
import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.entity.Vector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;

import static by.bsuir.akg.service.MatrixService.*;

public class ConvertService {
    private static ConvertService instance;
    private static RenderController renderController;

    private ConvertService() {
    }

    public static ConvertService getInstance(RenderController renderController1) {
        if (instance == null) {
            if (renderController1 != null) {
                renderController = renderController1;
            }
            instance = new ConvertService();
        }
        return instance;
    }

    public void changeModelCoordinate(Model model, int rotateX, int rotateY, int rotateZ) {
        List<Vector> newVectors = new ArrayList<Vector>();
        for (Vector vector : model.getVerts()) {
            RealMatrix pointMatrix = new Array2DRowRealMatrix(new double[][]{{vector.getX()}, {vector.getY()}, {vector.getZ()}, {vector.getW()}});
            RealMatrix matrix = multiplyMatrix(createTransformMatrix(0, 0, 0), pointMatrix);
            RealMatrix matrix1 = multiplyMatrix(createRotateXMatrix(rotateX), matrix);
//            RealMatrix matrix2 = multiplyMatrix(createRotateYMatrix(rotateY), matrix1);
//            RealMatrix matrix3 = multiplyMatrix(createRotateZMatrix(rotateZ), matrix2);
            //    RealMatrix matrix2 = multiplyMatrix(createRotateYMatrix(180), matrix);
            //      RealMatrix matrix3 = multiplyMatrix(createRotateZMatrix(20), matrix);
//            RealMatrix matrix2 = multiplyMatrix(createRotateYMatrix(90), matrix1);
//            RealMatrix matrix3 = multiplyMatrix(createRotateZMatrix(0), matrix2);
            //  RealMatrix matrix4 = multiplyMatrix(createScaleMatrix(50, 50, 50), matrix);
//            RealMatrix matrix1 = multiplyMatrix(createRotateYMatrix(60), matrix);
            //  RealMatrix matrix1 = multiplyMatrix(createTransformMatrix(0,0,-50), matrix);
            // RealMatrix worldMatrix = multiplyMatrix(createTransformMatrix(0, 0, 0), pointMatrix);
            RealMatrix viewMatrix = multiplyMatrix(createViewMatrix(), matrix1);
            RealMatrix projectionMatrix = multiplyMatrix(createProjectionMatrix(), viewMatrix);
            RealMatrix divide = divide(projectionMatrix, projectionMatrix.getData()[3][0]);
            RealMatrix windowMatrix = multiplyMatrix(createWindowMatrix(), divide);
            double[][] data = windowMatrix.getData();
            newVectors.add(new Vector(data[0][0], data[1][0], data[2][0]));
        }
      //  model.setVerts(newVectors);
        render(model, newVectors);
    }

    private void render(Model model, List<Vector> newVectors) {
        renderController.getBufferedImage().getGraphics().clearRect(0, 0, Const.WIDTH, Const.HEIGHT);
        for (List<Integer> list : model.getFaces()) {
            for (int i = 0; i < list.size(); i++) {
                Vector startPoint = null;
                Vector endPoint = null;
                if ((i + 1) < list.size()) {
                    startPoint =newVectors.get(list.get(i) - 1);
                    endPoint =newVectors.get(list.get(i + 1) - 1);
                } else {
                    startPoint = newVectors.get(list.get(i) - 1);
                    endPoint = newVectors.get(list.get(0) - 1);
                }
                if (startPoint.getX() >= 0 && startPoint.getX() <= Const.WIDTH && startPoint.getY() >= 0 && startPoint.getY() <= Const.HEIGHT
                        && endPoint.getX() >= 0 && endPoint.getX() <= Const.WIDTH && endPoint.getY() >= 0 && endPoint.getY() <= Const.HEIGHT) {
                    drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), 255, 255, 255);
                    renderController.getPanel().repaint();
                }
            }
        }

    }

    public void render(List<List<Vector>> newVectors) {
        renderController.getBufferedImage().getGraphics().clearRect(0, 0, Const.WIDTH, Const.HEIGHT);
        for(List<Vector> list : newVectors) {
            for (int i = 0; i < list.size(); i++) {
                Vector startPoint = null;
                Vector endPoint = null;
                if ((i + 1) < list.size()) {
                    startPoint =list.get(i);
                    endPoint =list.get(i + 1);
                } else {
                    startPoint = list.get(i);
                    endPoint = list.get(0);
                }
                if (startPoint.getX() >= 0 && startPoint.getX() <= Const.WIDTH && startPoint.getY() >= 0 && startPoint.getY() <= Const.HEIGHT
                        && endPoint.getX() >= 0 && endPoint.getX() <= Const.WIDTH && endPoint.getY() >= 0 && endPoint.getY() <= Const.HEIGHT) {
                    drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY(), 255, 255, 255);
                    renderController.getPanel().repaint();
                }
            }

        }
    }

    private void drawPixel(int x, int y, int red, int green, int blue) {
        int rgb = (red << 16 | green << 8 | blue);
        renderController.getBufferedImage().setRGB(x, y, rgb);
    }

    public void drawLine(double x1, double y1, double x2, double y2, int red, int green, int blue) {
        x1 = (double) Math.floor(x1);
        y1 = (double) Math.floor(y1);
        x2 = (double) Math.floor(x2);
        y2 = (double) Math.floor(y2);

        double c1 = y2 - y1;
        double c2 = x2 - x1;

        double length = Math.max(
                Math.abs(c1),
                Math.abs(c2)
        );

        double xStep = c2 / length;
        double yStep = c1 / length;

        for (int i = 0; i <= length; i++) {
            drawPixel(
                    (int) Math.floor(x1 + xStep * i),
                    (int) Math.floor(y1 + yStep * i),
                    red, green, blue
            );
        }
    }


    private RealMatrix divide(RealMatrix realMatrix, double w) {
        double[][] data = realMatrix.getData();
        return new Array2DRowRealMatrix(new double[][]{{data[0][0] / w}, {data[1][0] / w}, {data[2][0] / w}, {data[3][0] / w}});
    }
}
