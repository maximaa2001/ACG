package by.bsuir.akg.service;

import by.bsuir.akg.RenderController;
import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Vector;

import java.awt.*;
import java.util.List;

public class DrawService {
    private static DrawService instance;
    private static RenderController renderController;

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

    public void drawTriangle(List<Vector> triangle, float intens) {
        Vector p1 = triangle.get(0);
        Vector p2 = triangle.get(1);
        Vector p3 = triangle.get(2);
//        drawDda(p1.getX().intValue(), p1.getY().intValue(), p2.getX().intValue(), p2.getY().intValue());
//        drawDda(p2.getX().intValue(), p2.getY().intValue(), p3.getX().intValue(), p3.getY().intValue());
//        drawDda(p1.getX().intValue(), p1.getY().intValue(), p3.getX().intValue(), p3.getY().intValue());
        fillTriangle(p1, p2, p3, intens);
    }

    public void drawDda(int x1, int y1, int x2, int y2) {
        int L = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        double currentX = x1;
        double currentY = y1;
        double dx = (x2 - x1) / (double) L;
        double dy = (y2 - y1) / (double) L;
        for (int i = 0; i < L; i++) {
            if (currentX < 0 || currentX >= Const.WIDTH || currentY < 0 || currentY >= Const.HEIGHT) {
                break;
            }
            drawPixel((int) Math.floor(currentX), (int) Math.floor(currentY), 255, 0, 0);
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
        //int rgb = (red << 16 | green << 8 | blue);
        Color color = new Color((float) (red * 0.5 + 0.5), (float) (green * 0.5 + 0.5), (float) (blue * 0.5 + 0.5));
        renderController.getBufferedImage().setRGB(x, y, color.getRGB());
    }

    private void fillTriangle(Vector p1, Vector p2, Vector p3, float intens) {
        Vector topLeft = topLeft(p1, p2, p3);
        Vector bottomRight = bottomRight(p1, p2, p3);
        if (topLeft.getX() < 0 || topLeft.getY() < 0 || bottomRight.getX() < 0 || bottomRight.getY() < 0
                || topLeft.getX() > Const.WIDTH || topLeft.getY() > Const.HEIGHT || bottomRight.getX() > Const.WIDTH || bottomRight.getY() > Const.HEIGHT) {
            return;
        }
        for (int y = topLeft.getY().intValue(); y < bottomRight.getY().intValue(); y++) {
            for (int x = topLeft.getX().intValue(); x < bottomRight.getX().intValue(); x++) {
                if (isInTriangle(new Vector((double) x, (double) y, 0.0), p1, p2, p3)) {
                    if (x < 0 || x >= Const.WIDTH || y < 0 || y >= Const.HEIGHT) {
                        continue;
                    }
                    drawPixel(x, y, intens, intens, intens);
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
        return new Vector(minX, minY, 0.0);
    }

    private Vector bottomRight(Vector p1, Vector p2, Vector p3) {
        double maxX = max(p1.getX(), p2.getX(), p3.getX());
        double maxY = max(p1.getY(), p2.getY(), p3.getY());
        return new Vector(maxX, maxY, 0.0);
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
