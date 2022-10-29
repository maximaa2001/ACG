module by.bsuir.akg {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires java.desktop;
    requires javafx.swing;


    opens by.bsuir.akg to javafx.fxml;
    exports by.bsuir.akg;
}