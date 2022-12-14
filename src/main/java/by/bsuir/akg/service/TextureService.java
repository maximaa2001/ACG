package by.bsuir.akg.service;

import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Vector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextureService {
    private final int height;
    private final int width;
    private final BufferedImage bufferedImageColor = ImageIO.read(new File(Const.PATH_TO_COLOR));
    private final BufferedImage bufferedImageNormal = ImageIO.read(new File(Const.PATH_TO_NORMAL));
    private final BufferedImage bufferedImageMirror = ImageIO.read(new File(Const.PATH_TO_MIRROR));

    public TextureService() throws IOException {
        height = bufferedImageColor.getHeight();
        width = bufferedImageColor.getWidth();
    }

    public List<Vector> getTextureVectors(double xt, double yt) {
        int x = (int) Math.round(width * xt);
        int y = (int) Math.round(height * yt);
        if (x >= width )
            x = width - 1;
        if (y >= height)
            y = height - 1;
        if(y < 1)
            y = 1;
        if(x < 1)
            x = 1;

        Vector color = getTextureColor(x, y, bufferedImageColor);
        Vector mirror = getTextureMirror(x, y, bufferedImageMirror);
        Vector normal = getTextureNormal(x, y, bufferedImageNormal);
        List<Vector> vectors = new ArrayList<>();
        vectors.add(color);//diffuse ambient
        vectors.add(normal);//
        vectors.add(mirror);// specular albedo, power???
        return vectors;
    }

    public Vector getTextureMirror(int x, int y, BufferedImage bufferedImage) {
        int rgb = bufferedImage.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new Vector((double) red / 255, (double) green / 255, (double) blue / 255);
    }

    public Vector getTextureColor(int x, int y, BufferedImage bufferedImage) {
        int rgb = bufferedImage.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new Vector((double) red / 255, (double) green / 255, (double) blue / 255);
    }

    public Vector getTextureNormal(int x, int y, BufferedImage bufferedImage) {
        int rgb = bufferedImage.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new Vector((double) red / 255 * 2 - 1, (double) green / 255 * 2 - 1, (double) blue / 255 * 2 - 1);
    }


}