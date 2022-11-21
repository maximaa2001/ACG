package by.bsuir.akg.service;

import by.bsuir.akg.RenderController;
import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Vector;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public void drawTriangle(List<Vector> triangle, float intens) {
        Vector p1 = triangle.get(0);
        Vector p2 = triangle.get(1);
        Vector p3 = triangle.get(2);
        drawDda(p1.getX().intValue(), p1.getY().intValue(), p2.getX().intValue(), p2.getY().intValue(), intens);
        drawDda(p2.getX().intValue(), p2.getY().intValue(), p3.getX().intValue(), p3.getY().intValue(), intens);
        drawDda(p1.getX().intValue(), p1.getY().intValue(), p3.getX().intValue(), p3.getY().intValue(), intens);
         fillTriangle(p1, p2, p3, intens);
        // fill(p1, p2, p3, intens);
        //  rasterTriangle(p1, p2, p3, intens);
    }

    public void drawDda(int x1, int y1, int x2, int y2, float intens) {
        int L = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        double currentX = x1;
        double currentY = y1;
        double dx = (x2 - x1) / (double) L;
        double dy = (y2 - y1) / (double) L;
        for (int i = 0; i < L; i++) {
            if (Math.round(currentX) < 0 || Math.round(currentX) >= Const.WIDTH || Math.round(currentY) < 0 || Math.round(currentY) >= Const.HEIGHT) {
                break;
            }
            drawPixel((int) Math.round(currentX), (int) Math.round(currentY), intens, intens, intens);
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
        // Color color = new Color((float) (red * 0.5 + 0.5), (float) (green * 0.5 + 0.5), (float) (blue * 0.5 + 0.5));
        Color color = new Color(red, green, blue);
        //renderController.getBufferedImage().setRGB(x, y, color.getRGB());
        renderController.getBufferedImage().setRGB(x, y, color.getRGB());
    }

    private void fill(Vector dot1, Vector dot2, Vector dot3, float intens) {
        if (dot1.getY() < 0 || dot2.getY() < 0 || dot3.getY() < 0 || dot1.getX() < 0 || dot2.getX() < 0 || dot3.getX() < 0) {
            return;
        }
        if (dot1.getY() > Const.HEIGHT || dot2.getY() > Const.HEIGHT || dot3.getY() > Const.HEIGHT ||
                dot1.getX() > Const.WIDTH || dot2.getX() > Const.WIDTH || dot3.getX() > Const.WIDTH) {
            return;
        }
        double xl, xr, zl, zr;

        for (int Y = dot1.getY().intValue(); Y <= dot2.getY().intValue(); Y++) {
            xl = dot1.x + (dot2.x - dot1.x) * ((Y - dot1.y) / (dot2.y - dot1.y));
            xr = dot1.x + (dot3.x - dot1.x) * ((Y - dot1.y) / (dot3.y - dot1.y));
            zl = dot1.z + (dot2.z - dot1.z) * ((Y - dot1.y) / (dot2.y - dot1.y));
            zr = dot1.z + (dot3.z - dot1.z) * ((Y - dot1.y) / (dot3.y - dot1.y));
            fillLine(xl, xr, zl, zr, Y, intens);
        }

        for (int Y = dot2.getY().intValue(); Y <= dot2.getY().intValue(); Y++) {
            xl = dot2.x + (dot3.x - dot2.x) * ((Y - dot2.y) / (dot3.y - dot2.y));
            xr = dot1.x + (dot3.x - dot1.x) * ((Y - dot1.y) / (dot3.y - dot1.y));
            zl = dot2.z + (dot3.z - dot2.z) * ((Y - dot2.y) / (dot3.y - dot2.y));
            zr = dot1.z + (dot3.z - dot1.z) * ((Y - dot1.y) / (dot3.y - dot1.y));
            fillLine(xl, xr, zl, zr, Y, intens);
        }
    }


    public void rasterTriangle(Vector t0, Vector t1, Vector t2, float intens) {
        if (Objects.equals(t0.y, t1.y) && Objects.equals(t0.y, t2.y)) return; // i dont care about degenerate triangles
        if (t0.y>t1.y) {
            Double temp = t0.y;
            t0.y = t1.y;
            t1.y = temp;
        }
        if (t0.y>t2.y) {
            Double temp = t0.y;
            t0.y = t2.y;
            t2.y = temp;
        }
        if (t1.y>t2.y) {
            Double temp = t1.y;
            t1.y = t2.y;
            t2.y = temp;
        }
        double total_height = t2.y-t0.y;
        for (int i=0; i<total_height; i++) {
            boolean second_half = i>t1.y-t0.y || t1.y.equals(t0.y);
            double segment_height = second_half ? t2.y-t1.y : t1.y-t0.y;
            float alpha = (float)(i/total_height);
            float beta  = (float)((i-(second_half ? t1.y-t0.y : 0))/segment_height); // be careful: with above conditions no division by zero here
            Vector A = add(t0, scalarMult(minus(t2, t0), alpha));
            Vector B = second_half ? add(t1, scalarMult(minus(t2, t1), beta)): add(t0, scalarMult(minus(t1, t0), beta));
            if (A.x>B.x) {
                Vector temp = A;
                A = B;
                B = temp;
            }
            for (int j=A.x.intValue(); j<=B.x; j++) {
                double phi = B.x.equals(A.x) ? 1. : (float)(j-A.x)/(float)(B.x-A.x);
                Vector P = add(A,scalarMult(minus(B,A), phi));
                int idx = (int) (P.x+P.y*Const.WIDTH);
                if (zBuffer[idx]<P.z) {
                    zBuffer[idx] = P.z;
                    drawPixel(P.x.intValue(), P.y.intValue(), intens, intens, intens);
                }
            }
        }
    }

    private Vector add(Vector vector1, Vector vector2) {
        return new Vector(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY(), vector1.getZ() + vector2.getZ());
    }

    private Vector minus(Vector vector1, Vector vector2) {
        return new Vector(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
    }


    private void fillLine(Double _xl, Double _xr, Double zl, Double zr, int y, float intens) {
        var xl = _xl;
        var xr = _xr;
        if (xl > xr) {
            var t = xr;
            xr = xl;
            xl = t;
        }

        for (int X = (int) Math.round(xl); X <= (int) Math.round(xr); X++)
            if (X >= 0 && X < Const.WIDTH && y >= 0 && y < Const.HEIGHT) {
                var nz = zl + (zr - zl) * ((X - xl) / (xr - xl));
//                if (zBuffer[y][X] == null || zBuffer[y][X] > nz) {
//                    //var color = plane.getColor(X.toFloat(), Y.toFloat())
//                    drawPixel(X, y, intens, intens, intens);
//                    zBuffer[y][X] = nz;
//                }
            }

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
                    drawPixel(x, y, intens, intens, intens);
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

    private Vector scalarMult(Vector vector1, double alpha) {
        return new Vector(vector1.getX() * alpha, vector1.getY() * alpha, vector1.getZ() * alpha);
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
