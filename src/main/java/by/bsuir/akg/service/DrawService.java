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

    private Double[] zBuffer = new Double[Const.WIDTH * Const.HEIGHT];

    private DrawService() {

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

    public void drawTriangle(List<Vertex> triangle, float intens) {
        Vector p1 = triangle.get(0).position_screen;
        Vector p2 = triangle.get(1).position_screen;
        Vector p3 = triangle.get(2).position_screen;
        drawDda(p1.getX().intValue(), p1.getY().intValue(), p2.getX().intValue(), p2.getY().intValue(), intens, triangle);
        drawDda(p2.getX().intValue(), p2.getY().intValue(), p3.getX().intValue(), p3.getY().intValue(), intens, triangle);
        drawDda(p1.getX().intValue(), p1.getY().intValue(), p3.getX().intValue(), p3.getY().intValue(), intens, triangle);
        fillTriangle(triangle);
    }

    public void drawDda(int x1, int y1, int x2, int y2, float intens, List<Vertex> triangle) {
        int L = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        double currentX = x1;
        double currentY = y1;
        double dx = (x2 - x1) / (double) L;
        double dy = (y2 - y1) / (double) L;
        for (int i = 0; i < L; i++) {
            if (Math.round(currentX) < 0 || Math.round(currentX) >= Const.WIDTH || Math.round(currentY) < 0 || Math.round(currentY) >= Const.HEIGHT) {
                break;
            }
            int x = (int) Math.round(currentX);
            int y = (int) Math.round(currentY);
            Vector color = InterpolationService.interpolation(x, y, triangle);
            drawPixel(x, y, color.getX().floatValue(), color.getY().floatValue(), color.getZ().floatValue());
//            drawPixel((int) Math.round(currentX), (int) Math.round(currentY), intens, intens, intens);
            currentX += dx;
            currentY += dy;
        }
    }

    public void clear() {
        renderController.getBufferedImage().getGraphics().clearRect(0, 0, Const.WIDTH, Const.HEIGHT);
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
                    Vector color = InterpolationService.interpolation(x, y, triangle);
                    drawPixel(x, y, color.getX().floatValue(), color.getY().floatValue(), color.getZ().floatValue());
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
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return min;
    }

    private double max(Double... numbers) {
        Double max = -Double.MAX_VALUE;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max;
    }

}
