package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.LargeGraph;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class MarketPaneController {
    public ScrollPane scrollPane;
    public LargeGraph market_graph;
    public LargeGraph usage_graph;
    public LargeGraph fossil_graph;
    public LargeGraph renewable_graph;
    public LargeGraph main_graph;

    @FXML
    public void initialize() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        market_graph.RebindGraph();
        usage_graph.RebindGraph();
        fossil_graph.RebindGraph();
        renewable_graph.RebindGraph();
        main_graph.RebindGraph();

    }
}
