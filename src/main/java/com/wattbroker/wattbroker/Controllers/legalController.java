package com.wattbroker.wattbroker.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class legalController {

    public ScrollPane rootScroll;

    @FXML
    void initialize() {
        rootScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
