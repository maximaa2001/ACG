package by.bsuir.akg;

import by.bsuir.akg.constant.Const;
import by.bsuir.akg.service.DrawService;
import by.bsuir.akg.util.ImagePanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RenderController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    public AnchorPane pane;
    private BufferedImage bufferedImage;
    private JPanel panel;

    @FXML
    void initialize() throws IOException {
        initPane();
        DrawService.getInstance(this);
    }

    private void initPane() {
        final SwingNode swingNode = new SwingNode();
        bufferedImage = new BufferedImage(Const.WIDTH, Const.HEIGHT, BufferedImage.TYPE_INT_RGB);
        panel = new ImagePanel(bufferedImage);
        panel.setPreferredSize(new Dimension(Const.WIDTH, Const.HEIGHT));
        swingNode.setContent(panel);
        pane.getChildren().add(swingNode);
    }


    public void setStage(Stage stage) {
        initGame(stage);
    }

    private void initGame(Stage stage) {
        Game game = Game.getGame(stage);
        try {
            game.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public JPanel getPanel() {
        return panel;
    }
}
