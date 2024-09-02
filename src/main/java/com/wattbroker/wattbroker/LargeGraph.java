package com.wattbroker.wattbroker;

import com.util.util;
import com.util.util.Vector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

/**
 * Class used for the full width graph types.
 * @author finleycrowther
 * @implNote currently does not support scaling add modification to Graph.GraphSize.
 * @see com.wattbroker.wattbroker.Graph.GraphSize Graph Scaling
 * @see Graph Root Class*/
public class LargeGraph extends Pane {

    // FXML Variables form LargeGraph.fxml
    @FXML @SuppressWarnings("unused") private Pane graphPane;
    @FXML @SuppressWarnings("unused") private Text hourButton;
    @FXML @SuppressWarnings("unused") private Text dayButton;
    @FXML @SuppressWarnings("unused") private Text weekButton;
    @FXML @SuppressWarnings("unused") private Text _1;
    @FXML @SuppressWarnings("unused") private Text _2;
    @FXML @SuppressWarnings("unused") private Text _3;
    @FXML @SuppressWarnings("unused") private Text _4;
    @FXML @SuppressWarnings("unused") private Text _5;
    @FXML @SuppressWarnings("unused") private Text _6;
    @FXML @SuppressWarnings("unused") private Text _7;
    @FXML @SuppressWarnings("unused") private AnchorPane root;

    /**
     * Graph type, stores the data types displayed on the graph from FXML ("market", "usage", "fossil", "renewable")*/
    @FXML private String graphType;

    /**
     * Returns the type of the graph ("market", "usage", "fossil", "renewable")
     * @return String type of graph*/
    public String getGraphType() {
        return graphType;
    }

    /**
     * Communicating with the fxml to set the graph type
     * @param graphType Parsed type from the FXML ("market", "usage", "fossil", "renewable")
     * */
    public void setGraphType(String graphType) {
        this.graphType = graphType;
        // Handle graph type change if needed
    }

    /**
     * Constructor for the LargeGraph class, builds the large graph fxml and reads data
     * @see Graph root class
     * @see com.wattbroker.wattbroker.Graph.GraphSize*/
    public LargeGraph() {
        FXMLLoader load = new FXMLLoader(getClass().getResource("large_graph.fxml"));
        load.setRoot(this);
        load.setController(this);

        this.setId("large_graph");

        try {
            load.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        StackPane graph = buildGraph(Graph.todayData);

        graph.setId("Graph");

        graphPane.getChildren().add(graph);

        Line l = new Line(117, 24, 117+ dayButton.prefWidth(0), 24);
        l.setStroke(Color.rgb(120, 32, 150, 1));
        l.setStrokeWidth(3);
        root.getChildren().add(l);

        dayButton.setFill(Color.WHITE);
        final char[] lastGraph = {'d'}; // [h, d, w] => hour graph, day graph, week graph. Default is day graph

        // GET text position for resizable graphs

        hourButton.setOnMouseClicked(e -> {
            // Ignores if current
            if(lastGraph[0] == 'h') return;
            // Update Graph
            updateGraph(new char[]{'h'});
            // Set Buttons
            hourButton.setFill(Color.WHITE);
            l.setStartX(hourButton.getX()+40);
            l.setEndX(hourButton.prefWidth(0)+40);
            l.setTranslateX(0);
            dayButton.setFill(Color.rgb(132, 132, 132, 1));
            weekButton.setFill(Color.rgb(132, 132, 132, 1));
            // Set Time
            _1.setText("00:00");
            _2.setText("10.00");
            _3.setText("20.00");
            _4.setText("30.00");
            _5.setText("40.00");
            _6.setText("50.00");
            _7.setText("60.00");
            // Set last graph
            lastGraph[0] = 'h';
        });
        dayButton.setOnMouseClicked(e -> {
            // Ignores if current
            if(lastGraph[0] == 'd') return;
            // Update Graph
            updateGraph(new char[]{'d'});
            // Set Buttons
            dayButton.setFill(Color.WHITE);
            l.setStartX(dayButton.getX()+40);
            l.setEndX(dayButton.prefWidth(0)+40);
            l.setTranslateX(77);
            hourButton.setFill(Color.rgb(132, 132, 132, 1));
            weekButton.setFill(Color.rgb(132, 132, 132, 1));
            // Set Time
            _1.setText("00:00");
            _2.setText("04.00");
            _3.setText("08.00");
            _4.setText("12.00");
            _5.setText("16.00");
            _6.setText("20.00");
            _7.setText("24.00");
            // Set last graph
            lastGraph[0] = 'd';
        });
        weekButton.setOnMouseClicked(e -> {
            // Ignores if current
            if (lastGraph[0] == 'w') return;
            // Update Graph
            updateGraph(new char[]{'w'});
            // Set Buttons
            weekButton.setFill(Color.WHITE);
            l.setStartX(weekButton.getX()+40);
            l.setEndX(weekButton.prefWidth(0)+40);
            l.setTranslateX(135);
            hourButton.setFill(Color.rgb(132, 132, 132, 1));
            dayButton.setFill(Color.rgb(132, 132, 132, 1));
            // Set Time
            _1.setText("MON    ");
            _2.setText("TUE    ");
            _3.setText("WED    ");
            _4.setText("THU    ");
            _5.setText("FIR    ");
            _6.setText("SAT    ");
            _7.setText("SUN    ");
            // Set last graph
            lastGraph[0] = 'w';
        });
    }

    public void RebindGraph() {
        // Rebind the graph
        // graphType = "market", "usage", "fossil", "renewable"
        updateGraph(new char[]{'d'});
    }

    /**
     * Builds the graph based on the data type
     * @param data Data to be displayed on the graph
     * @return StackPane containing the graph*/
    private StackPane buildGraph(String data) {
        // If not provided sets the graph to the default to avoid null pointer exception with the switch statement

        if(getGraphType() == null)
            setGraphType("default");

        return switch (graphType.toLowerCase()) {
            case "market" -> new StackPane(Graph.wavyPath(Graph.plotPoints_AEMO(
                        Graph.graphType.AEMO.set_put(null, 'd', "SPOT_PRICE"),
                        new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "SPOT_PRICE"), Graph.GraphSize.LARGE, Graph.Price, true, Graph.Price_Fill));
            case "renewable" -> new StackPane(Graph.wavyPath(Graph.plotPoints_AEMO(
                        Graph.graphType.AEMO.set_put(null, 'd', "GENERATION"),
                        new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "GENERATION"), Graph.GraphSize.LARGE, Graph.Renewable, true, Graph.Renewable_Fill));
            case "fossil" -> new StackPane(Graph.wavyPath(Graph.plotPoints_AEMO(
                        Graph.graphType.AEMO.set_put(null, 'd', "GENERATION"),
                        new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "GENERATION"), Graph.GraphSize.LARGE, Graph.Fossil, true, Graph.Fossil_Fill));
            case "usage" -> new StackPane(Graph.wavyPath(Graph.plotPoints_AEMO(
                        Graph.graphType.AEMO.set_put(null, 'd', "DEMAND"),
                        new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "DEMAND"), Graph.GraphSize.LARGE, Graph.Supply, true, Graph.Supply_Fill));
            case "efficiency" -> new StackPane(Graph.wavyPath(Graph.plotPoints_AEMO(
                        Graph.graphType.AEMO.set_put(null, 'd', "DEMAND"),
                        new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "DEMAND"), Graph.GraphSize.LARGE, Graph.Efficiency, true, Graph.Efficiency_Fill));
            case "profit" -> new StackPane(Graph.wavyPath(Graph.plotPoints_AEMO(
                        Graph.graphType.AEMO.set_put(null, 'd', "SPOT_PRICE"),
                        new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "SPOT_PRICE"), Graph.GraphSize.LARGE, Graph.Profit, true, Graph.Profit_Fill));
            default -> new StackPane(Graph.wavyPath(Graph.plotPoints_AEMO(
                        Graph.graphType.AEMO.set_put(null, 'd', "SPOT_PRICE"),
                        new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "SPOT_PRICE"), Graph.GraphSize.LARGE, Graph.Price, true, Graph.Price_Fill),
                    Graph.wavyPath(Graph.plotPoints_AEMO(
                            Graph.graphType.AEMO.set_put(null, 'd', "DEMAND"),
                            new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "DEMAND"), Graph.GraphSize.LARGE, Graph.Demand, false, null),
                    Graph.wavyPath(Graph.plotPoints_AEMO(
                            Graph.graphType.AEMO.set_put(null, 'd', "GENERATION"),
                            new Data().getAEMOdata(data), Graph.GraphSize.LARGE, "GENERATION"), Graph.GraphSize.LARGE, Graph.Supply, false, null));
        };
    }

    /**
     * Updates the graph based on the last graph type
     * @param lastGraph Last graph type*/
    void updateGraph(char[] lastGraph) {
        for(Node n : graphPane.getChildren()) {
            if(n.getId().equals("Graph")) {
                graphPane.getChildren().remove(n);
                break;
            }
        }

        StackPane graph = buildGraph(switch (lastGraph[0]) {
            case 'h' -> Graph.yesterdayData;
            case 'w' -> Graph.tomorrowData;
            default -> Graph.todayData; // 'd' and others which are not valid values
        });

        graph.setId("Graph");

        graphPane.getChildren().add(graph);
    }

    void setType() {

    }

    /**
     * Enum for the time axis type
     * @see com.wattbroker.wattbroker.Graph.GraphSize Graph Scaling*/
    public enum graphTimeAxisType {
        SECOND {
            @Override
            public void set() {
                breaks = 10; // Per 10 ms of 100 ms
            }
        }, MINUTE {
            @Override
            public void set() {
                breaks = 6; // Per 10 s of 60 s
            }
        }, HOUR {
            @Override
            public void set() {
                breaks = 6; // Per 10 m of 60 m
            }
        }, DAY {
            @Override
            public void set() {
                breaks = 8; // Per 3 h of 24 h
            }
        }, WEEK {
            @Override
            public void set() {
                breaks = 7; // Per 1 d of 7 d
            }
        };
        public int breaks;
        public abstract void set();
    }
}