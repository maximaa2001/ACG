package by.bsuir.akg;

import java.net.URL;
import java.util.ResourceBundle;

import by.bsuir.akg.service.ConvertService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class FormController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn;

    @FXML
    private TextField rotateX;

    @FXML
    private TextField rotateY;

    @FXML
    private TextField rotateZ;

    private int rotateXInteger;
    private int rotateYInteger;
    private int rotateZInteger;

    @FXML
    void initialize() {
        btn.setOnAction(actionEvent -> {
            if (!rotateX.getText().isEmpty()) {
                System.out.println(Integer.parseInt(rotateX.getText()));
                rotateXInteger = Integer.parseInt(rotateX.getText());
            }
            if (!rotateY.getText().isEmpty()) {
                rotateYInteger = Integer.parseInt(rotateY.getText());
            }
            if (!rotateZ.getText().isEmpty()) {
                rotateZInteger = Integer.parseInt(rotateZ.getText());
            }
            ConvertService.getInstance(null).changeModelCoordinate(RenderController.model, rotateXInteger, rotateYInteger, rotateZInteger);
        });
    }

    public double getRotateXDouble() {
        return rotateXInteger;
    }

    public double getRotateYDouble() {
        return rotateYInteger;
    }

    public double getRotateZDouble() {
        return rotateZInteger;
    }
}
