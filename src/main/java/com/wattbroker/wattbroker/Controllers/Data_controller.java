package com.wattbroker.wattbroker.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class Data_controller {


    public ScrollPane rootScroll;

    @FXML
    private void initialize() {
        // Remove scroll bars
        rootScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}
