package by.bsuir.akg.util;

import by.bsuir.akg.constant.Const;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class TextureInitializer {
    private BufferedImage colorTexture;
    private BufferedImage normalTexture;
    private BufferedImage mirrorTexture;
    private static TextureInitializer instance;

    private TextureInitializer() {
        init();
    }

    public static TextureInitializer getInstance() {
        if (instance == null) {
            instance = new TextureInitializer();
        }
        return instance;
    }

    private void init() {
        try {
            colorTexture = ImageIO.read(Objects.requireNonNull(getClass().getResource(Const.PATH_TO_COLOR)));
            normalTexture = ImageIO.read(Objects.requireNonNull(getClass().getResource(Const.PATH_TO_NORMAL)));
            mirrorTexture = ImageIO.read(Objects.requireNonNull(getClass().getResource(Const.PATH_TO_MIRROR)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getColorTexture() {
        return colorTexture;
    }

    public BufferedImage getNormalTexture() {
        return normalTexture;
    }

    public BufferedImage getMirrorTexture() {
        return mirrorTexture;
    }
}
