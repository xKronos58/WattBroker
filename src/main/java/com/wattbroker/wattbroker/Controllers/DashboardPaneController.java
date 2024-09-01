package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.API_INFO;
import com.wattbroker.wattbroker.Algorithms;
import com.wattbroker.wattbroker.ToggleMenu;
import com.wattbroker.wattbroker.toggleable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DashboardPaneController {
    public ScrollPane Content;
    public AnchorPane AlgorithmInfo;
    public VBox menu_list;
    public ScrollPane alg_scroll;


    public void initialize() {
        Content.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Content.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        alg_scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        alg_scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        menu_list.getChildren().add(new ToggleMenu(List.of(new Algorithms("Algorithm 1", 100, 0, 100, false),
                new Algorithms("Algorithm 2", 200, 0, 100, false),
                new Algorithms("Algorithm 3", 300, 0, 100, false)), Algorithms.dataType.EFFICIENCY));

        menu_list.getChildren().add(new ToggleMenu(List.of(new Algorithms("Algorithm 1", 100, 0, 100, false),
                new Algorithms("Algorithm 2", 200, 0, 100, false),
                new Algorithms("Algorithm 3", 300, 0, 100, false)), Algorithms.dataType.PROFIT));

        menu_list.getChildren().add(new ToggleMenu(List.of(new API_INFO("API 1", 100, false),
                new API_INFO("API 2", 200, false),
                new API_INFO("API 3", 300, false)), API_INFO.dataType.RESPONSE_TIME));
    }

    public void CloseALGPane(MouseEvent mouseEvent) {

    }
}
