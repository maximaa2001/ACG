package by.bsuir.akg.service;

import by.bsuir.akg.RenderController;
import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Vector;
import by.bsuir.akg.entity.Vertex;

import java.awt.*;
import java.util.List;

public class DrawService {
    private static DrawService instance;
    private static RenderController renderController;

    private Double[][] zBuffer = new Double[Const.HEIGHT][Const.WIDTH];

    private DrawService() {
        for (int i = 0; i < Const.HEIGHT; i++) {
            for (int j = 0; j < Const.WIDTH; j++) {
                zBuffer[i][j] = 100000.0;
            }

        }
    }

    public static DrawService getInstance(RenderController renderController1) {
        if (instance == null) {
            if (renderController1 != null) {
                renderController = renderController1;
            }
            instance = new DrawService();
        }
        return instance;
    }

    public void drawTriangle(List<Vertex> triangle) {
        Vertex p1 = triangle.get(0);
        Vertex p2 = triangle.get(1);
        Vertex p3 = triangle.get(2);
        if (p1.position_screen.getX() < 0 || p2.position_screen.getX() < 0 || p3.position_screen.getX() < 0) return;
        if (p1.position_screen.getY() < 0 || p2.position_screen.getY() < 0 || p3.position_screen.getY() < 0) return;
        drawDda(p1, p2);
        drawDda(p2, p3);
        drawDda(p1, p3);
        fillTriangle(triangle);
    }

    public void drawDda(Vertex vertex1, Vertex vertex2) {
        int x1 = vertex1.position_screen.getX().intValue();
        int y1 = vertex1.position_screen.getY().intValue();
        int x2 = vertex2.position_screen.getX().intValue();
        int y2 = vertex2.position_screen.getY().intValue();
        int L = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        double currentX = x1;
        double currentY = y1;
        double dx = (x2 - x1) / (double) L;
        double dy = (y2 - y1) / (double) L;
        for (int i = 0; i < L; i++) {
            int x = (int) Math.round(currentX);
            int y = (int) Math.round(currentY);
            if (x < 0 || x >= Const.WIDTH || y < 0 || y >= Const.HEIGHT) {
                continue;
            }
            List<Vector> vectors = InterpolationService.interpolationNormalPositionLine(currentX, currentY, vertex1, vertex2);
            double z = InterpolationService.interpolationZ(x, y, vertex1, vertex2);
            if(zBuffer[y][x] > z) {
                Vector color = PhongShader.getPhongColor(vectors.get(1), vectors.get(0));
//            Vector color = InterpolationService.interpolationLine(currentX, currentY, vertex1, vertex2);
                drawPixel(x, y, color.getX().floatValue(), color.getY().floatValue(), color.getZ().floatValue());
                zBuffer[y][x] = z;
            }
//            drawPixel((int) Math.round(currentX), (int) Math.round(currentY), intens, intens, intens);
            currentX += dx;
            currentY += dy;
        }
    }

    public void clear() {
        renderController.getBufferedImage().getGraphics().clearRect(0, 0, Const.WIDTH, Const.HEIGHT);
        for (int i = 0; i < Const.HEIGHT; i++) {
            for (int j = 0; j < Const.WIDTH; j++) {
                zBuffer[i][j] = 100000.0;
            }
        }
        }

    public void repaint() {
        renderController.getPanel().repaint();
    }

    private void drawPixel(int x, int y, float red, float green, float blue) {
        Color color = new Color(red, green, blue);
        renderController.getBufferedImage().setRGB(x, y, color.getRGB());
    }

    public void fillTriangle(List<Vertex> triangle) {
        Vector topLeft = topLeft(
                triangle.get(0).position_screen,
                triangle.get(1).position_screen,
                triangle.get(2).position_screen);
        Vector bottomRight = bottomRight(
                triangle.get(0).position_screen,
                triangle.get(1).position_screen,
                triangle.get(2).position_screen);
        if (topLeft.getX() < 0 || topLeft.getY() < 0 || bottomRight.getX() < 0 || bottomRight.getY() < 0
                || topLeft.getX() > Const.WIDTH || topLeft.getY() > Const.HEIGHT
                || bottomRight.getX() > Const.WIDTH || bottomRight.getY() > Const.HEIGHT) {
            return;
        }
        for (int y = topLeft.getY().intValue(); y < bottomRight.getY().intValue(); y++) {
            for (int x = topLeft.getX().intValue(); x < bottomRight.getX().intValue(); x++) {
                if (isInTriangle(new Vector((double) x, (double) y, 0.0),
                        triangle.get(0).position_screen,
                        triangle.get(1).position_screen,
                        triangle.get(2).position_screen)) {
                    if (x < 0 || x >= Const.WIDTH || y < 0 || y >= Const.HEIGHT) {
                        continue;
                    }
                    List<Vector> vectors = InterpolationService.interpolationNormalPosition(x, y, triangle);
                    double z = InterpolationService.interpolationZTriangle(x, y, triangle);
                    if(zBuffer[y][x] > z) {
                        Vector color = PhongShader.getPhongColor(vectors.get(1), vectors.get(0));
//            Vector color = InterpolationService.interpolationLine(currentX, currentY, vertex1, vertex2);
                        drawPixel(x, y, color.getX().floatValue(), color.getY().floatValue(), color.getZ().floatValue());
                        zBuffer[y][x] = z;
                    }

                }
            }
        }
    }

    private boolean isInTriangle(Vector p, Vector p1, Vector p2, Vector p3) {
        int aSide = ((p1.getY().intValue() - p2.getY().intValue()) * p.getX().intValue() + (p2.getX().intValue() - p1.getX().intValue()) * p.getY().intValue() + (p1.getX().intValue() * p2.getY().intValue() - p2.getX().intValue() * p1.getY().intValue()));
        int bSide = ((p2.getY().intValue() - p3.getY().intValue()) * p.getX().intValue() + (p3.getX().intValue() - p2.getX().intValue()) * p.getY().intValue() + (p2.getX().intValue() * p3.getY().intValue() - p3.getX().intValue() * p2.getY().intValue()));
        int cSide = ((p3.getY().intValue() - p1.getY().intValue()) * p.getX().intValue() + (p1.getX().intValue() - p3.getX().intValue()) * p.getY().intValue() + (p3.getX().intValue() * p1.getY().intValue() - p1.getX().intValue() * p3.getY().intValue()));
        return (aSide >= 0 && bSide >= 0 && cSide >= 0) || (aSide < 0 && bSide < 0 && cSide < 0);
    }

    private Vector topLeft(Vector p1, Vector p2, Vector p3) {
        double minX = min(p1.getX(), p2.getX(), p3.getX());
        double minY = min(p1.getY(), p2.getY(), p3.getY());
        double miZ = min(p1.getZ(), p2.getZ(), p3.getZ());
        return new Vector(minX, minY, miZ);
    }

    private Vector bottomRight(Vector p1, Vector p2, Vector p3) {
        double maxX = max(p1.getX(), p2.getX(), p3.getX());
        double maxY = max(p1.getY(), p2.getY(), p3.getY());
        double maxZ = max(p1.getZ(), p2.getZ(), p3.getZ());
        return new Vector(maxX, maxY, maxZ);
    }

    private double min(Double... numbers) {
        Double min = Double.MAX_VALUE;
        for (Double number : numbers) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }

    private double max(Double... numbers) {
        Double max = -Double.MAX_VALUE;
        for (Double number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

}
