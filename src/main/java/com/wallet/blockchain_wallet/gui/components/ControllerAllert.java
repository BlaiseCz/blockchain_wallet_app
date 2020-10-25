package com.wallet.blockchain_wallet.gui.components;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Controller;

@Controller
public class ControllerAllert {

    static void showAlert(Alert.AlertType alertType, String title, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
