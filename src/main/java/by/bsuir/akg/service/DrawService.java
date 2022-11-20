package by.bsuir.akg.service;

import by.bsuir.akg.RenderController;
import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Vector;

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

    public void drawTriangle(List<Vector> triangle) {
        Vector p1 = triangle.get(0);
        Vector p2 = triangle.get(1);
        Vector p3 = triangle.get(2);
        drawDda(p1.getX().intValue(), p1.getY().intValue(), p2.getX().intValue(), p2.getY().intValue());
        drawDda(p2.getX().intValue(), p2.getY().intValue(), p3.getX().intValue(), p3.getY().intValue());
        drawDda(p1.getX().intValue(), p1.getY().intValue(), p3.getX().intValue(), p3.getY().intValue());
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
            drawPixel((int) Math.floor(currentX), (int) Math.floor(currentY), 255, 255, 255);
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

    private void drawPixel(int x, int y, int red, int green, int blue) {
        int rgb = (red << 16 | green << 8 | blue);
        renderController.getBufferedImage().setRGB(x, y, rgb);
    }
}
