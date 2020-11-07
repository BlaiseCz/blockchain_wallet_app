package com.wallet.blockchain_wallet.gui.components.controllers;

import javafx.scene.control.Alert;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;

import static lombok.AccessLevel.PRIVATE;

@Controller
@NoArgsConstructor(access = PRIVATE)
public class ControllerAlert {

    static void showAlert(Alert.AlertType alertType, String title, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
