package com.wattbroker.wattbroker.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class MarketPaneController {
    public ScrollPane scrollPane;

    @FXML
    public void initialize() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
