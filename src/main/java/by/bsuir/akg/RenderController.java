package by.bsuir.akg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import by.bsuir.akg.constant.Const;
import by.bsuir.akg.entity.Model;
import by.bsuir.akg.service.ConvertService;
import by.bsuir.akg.util.ImagePanel;
import by.bsuir.akg.util.Parser;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;

public class RenderController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    public AnchorPane pane;
    private BufferedImage bufferedImage;
    private JPanel panel;
    private Stage stage;

    public static Model model;

    @FXML
    void initialize() throws IOException {
        renderPane();
        ConvertService instance = ConvertService.getInstance(this);
        Parser parser = new Parser();
        model = parser.readObject();
        instance.changeModelCoordinate(model, 0,0,0);

        // drawLine(10, 20, 50, 20, 255,255,255);
    }

    public void renderPane() {
        int width = Const.WIDTH;
        int height = Const.HEIGHT;
        final SwingNode swingNode = new SwingNode();
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        panel = new ImagePanel(bufferedImage);
        panel.setPreferredSize(new Dimension(width, height));
        swingNode.setContent(panel);
        pane.getChildren().add(swingNode);
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case Q -> {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("form.fxml"));
                        try {
                            Parent root = loader.load();
                            Scene scene = new Scene(root);
                            Stage stage1 = new Stage();
                            stage1.setScene(scene);
                            stage1.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public JPanel getPanel() {
        return panel;
    }
}
