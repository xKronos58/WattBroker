package com.wattbroker.wattbroker.Controllers.SettingsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

public class TradingSettingsController {

    public ScrollPane rootScrollPane;

    @FXML
    private void initialize() {
        rootScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void editUser(MouseEvent mouseEvent) {

    }
}
