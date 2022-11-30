package by.bsuir.akg.service;

import by.bsuir.akg.RenderController;
import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Vector;

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

    public void drawTriangle(List<Vector> triangle, float intens) {
        Vector p1 = triangle.get(0);
        Vector p2 = triangle.get(1);
        Vector p3 = triangle.get(2);
        drawDda(p1.getX().intValue(), p1.getY().intValue(), p2.getX().intValue(), p2.getY().intValue(), intens, p1, p2);
        drawDda(p2.getX().intValue(), p2.getY().intValue(), p3.getX().intValue(), p3.getY().intValue(), intens, p2, p3);
        drawDda(p1.getX().intValue(), p1.getY().intValue(), p3.getX().intValue(), p3.getY().intValue(), intens, p1, p3);
        fillTriangle(p1, p2, p3, intens);
    }

    public void drawDda(int x1, int y1, int x2, int y2, float intens, Vector v1, Vector v2) {
        int L = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        double currentX = x1;
        double currentY = y1;
        double dx = (x2 - x1) / (double) L;
        double dy = (y2 - y1) / (double) L;
        for (int i = 0; i < L; i++) {
            if (Math.round(currentX) < 0 || Math.round(currentX) >= Const.WIDTH || Math.round(currentY) < 0 || Math.round(currentY) >= Const.HEIGHT) {
                break;
            }
            double z = InterpolationService.interpolationZ(currentX, currentY, v1, v2);
            if (zBuffer[(int) Math.round(currentY)][(int) Math.round(currentX)] > z) {
                drawPixel((int) Math.round(currentX), (int) Math.round(currentY), intens, intens, intens);
                zBuffer[(int) Math.round(currentY)][(int) Math.round(currentX)] = z;
            }
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
        //int rgb = (red << 16 | green << 8 | blue);
        // Color color = new Color((float) (red * 0.5 + 0.5), (float) (green * 0.5 + 0.5), (float) (blue * 0.5 + 0.5));
        Color color = new Color(red, green, blue);
        //renderController.getBufferedImage().setRGB(x, y, color.getRGB());
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
                    // if(zBuffer[y][x] == null ||  nz < zBuffer[y][x]) {
                    double z = InterpolationService.interpolationZTriangle(x, y, p1, p2, p3);
                    if(zBuffer[y][x] > z) {
                        drawPixel(x, y, intens, intens, intens);
                        zBuffer[y][x] = z;
                    }
                    //  zBuffer[y][x] = nz;
                    //  }
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
