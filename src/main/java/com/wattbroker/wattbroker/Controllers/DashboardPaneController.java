package com.wattbroker.wattbroker.Controllers;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

public class DashboardPaneController {
    public ScrollPane Content;

    public void initialize() {
        Content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void CloseALGPane(MouseEvent mouseEvent) {

    }
}
