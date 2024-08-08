package com.wattbroker.wattbroker;

import com.util.util;
import com.util.util.Vector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmallGraph extends Pane {
    @FXML @SuppressWarnings("unused")
    private Pane graphPane;
    @FXML @SuppressWarnings("unused")
    private Text hourButton;
    @FXML @SuppressWarnings("unused")
    private Text dayButton;
    @FXML @SuppressWarnings("unused")
    private Text weekButton;
    @FXML @SuppressWarnings("unused") private Text _1;
    @FXML @SuppressWarnings("unused") private Text _2;
    @FXML @SuppressWarnings("unused") private Text _3;
    @FXML @SuppressWarnings("unused") private Text _4;
    @FXML @SuppressWarnings("unused") private Text _5;
    @FXML @SuppressWarnings("unused") private Text _6;
    @FXML @SuppressWarnings("unused") private Text _7;
    @FXML @SuppressWarnings("unused")
    private AnchorPane root;


    long start;

    /**
     * <b>IMPORTANT! </b> This class is essentially a duplicate of the Graph class until it is modified to be resizable
     * <b>ONLY SET SIZES ARE AVAILABLE AT THE MOMENT!</b><newline/>
     * Graph class renders a styled graph based of sets of data rendered in three different sizes (Small, Regular, Large), this class is for the Small graph size
     * @see GraphSize size of the graph
     * @see LargeGraph large version of graph
     * @see SmallGraph small version of graph
     * Loads the "graph.fxml" file under the resource directory
     * @throws Exception *(within code) e if "graph.fxml" is not found under the resource directory */
    public SmallGraph() {
        // Get fxml file
        FXMLLoader load = new FXMLLoader(getClass().getResource("small_graph.fxml"));

        // Set root and controller for the FXML class
        load.setRoot(this);
        load.setController(this);

        // Load fxml to be used later
        try {
            load.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Create the graph and add it to the graphPane
        StackPane graph = new StackPane(Graph.wavyPath(Graph.plotPoints(
                Graph.graphType.Market.set_put(null, 'd'), new Data()
                        .getMarketData(new Date(System.currentTimeMillis()),
                                "market@2024-06-02_00:00:00-23:59:00.csv"), Graph.GraphSize.SMALL), Graph.GraphSize.SMALL));

        // Add id to the graph for later usage
        graph.setId("Graph");

        // Add the graph the to root pane
        graphPane.getChildren().add(graph);

        // Add the currently selected button line
        Line l = new Line(77, 24, 77+ dayButton.prefWidth(0), 24);
        l.setStroke(Color.rgb(120, 32, 150, 1));
        l.setStrokeWidth(3);
        root.getChildren().add(l);

        dayButton.setFill(Color.WHITE);
        final char[] lastGraph = {'d'}; // [h, d, w] => hour graph, day graph, week graph. Default is day graph

        // Set action for the hour button
        hourButton.setOnMouseClicked(e -> {
            // Ignores if current
            if(lastGraph[0] == 'h') return;
            // Update Graph
            updateGraph(new char[]{'h'});
            // Set Buttons
            hourButton.setFill(Color.WHITE);
            l.setStartX(hourButton.getX());
            l.setEndX(hourButton.prefWidth(0));
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

        // Set the action for the day button.
        dayButton.setOnMouseClicked(e -> {
            // Ignores if current
            if(lastGraph[0] == 'd') return;
            // Update Graph
            updateGraph(new char[]{'d'});
            // Set Buttons
            dayButton.setFill(Color.WHITE);
            l.setStartX(dayButton.getX());
            l.setEndX(dayButton.prefWidth(0));
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
            l.setStartX(weekButton.getX());
            l.setEndX(weekButton.prefWidth(0));
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

    private void updateGraph(char[] lastGraph) {
        for(Node n : graphPane.getChildren()) {
            if(n.getId().equals("Graph")) {
                graphPane.getChildren().remove(n);
                break;
            }
        }

        StackPane graph = new StackPane(Graph.wavyPath(Graph.plotPoints(
                Graph.graphType.Market.set_put(null, lastGraph[0]), new Data()
                        .getMarketData(new Date(System.currentTimeMillis()),
                                switch (lastGraph[0]) {
                                    case 'h' -> "market@2024-06-01_00:00:00-23:59:00.csv";
                                    case 'd' -> "market@2024-06-02_00:00:00-23:59:00.csv";
                                    case 'w' -> "market@2024-06-03_00:00:00-23:59:00.csv";
                                    default -> throw new IllegalStateException("Unexpected value: " + lastGraph[0]);
                                }), Graph.GraphSize.SMALL), Graph.GraphSize.SMALL));

        graph.setId("Graph");

        graphPane.getChildren().add(graph);
    }

    /**
     * Converts data into vector co-ordinates
     * @param maxMin takes a list (Size 2) of the max x (value) and y (time)
     * @param data takes a list of the data to be plotted
     * @see Vector Vector storage
     * @see Minimum Minimum
     * @see Maximum Maximum
     * @see Data.tV Data types
     * */
    @SuppressWarnings("ClassEscapesDefinedScope")
    public List<Vector<Double, Double>> plotPoints(
            List<Vector<Graph.Minimum<Double>, Graph.Maximum<Double>>> maxMin, List<Data.tV> data
    ) {
        // Check if maxMin is of size 2
        if(maxMin.size() != 2)
            throw new IllegalArgumentException("maxMin must have 2 elements");

        // Define the size of the graph. Based of the GraphSize.x enum to define X and Y
        Vector<Double, Double> graphSize = new Vector<>(515.0, 439.0);

        // Define the list of points to be plotted
        List<Vector<Double, Double>> points = new ArrayList<>();

        // Define the scaling factor for the graph
        double d1 = graphSize.getY()/maxMin.get(1).getY().max,
                d2 = graphSize.getX()/maxMin.get(0).getY().max;

        // Iterate through the data and convert it to a vector
        for(int i = 0; i < data.size(); i++) {
            double x = data.get(i).value(), y = i;
            x *= d2; // Multiply the x by the x scalar value
            y *= d1; // Multiply the x by the x scalar value
            x = graphSize.getY() - x;
//            System.out.println(x + ", " + y); // Logging for large data sets
            points.add(new Vector<>(x, y)); // Add the points
        }

        return points;
    }

    /**
     * Main class encapsulating methods used for maximum values and manipulation and storage of the value
     * @see Minimum Minimum version
     * @param <t> Numerical param for space efficiency between Byte ... Double*/
    public static class Maximum<t> {
        t max;
        /**
         * Class constructor setting the base maximum value
         * @param max type of class within value range*/
        public Maximum(t max) {
            this.max = max;
        }

        /**
         * Gets the stored maximum value
         * @return type of <b><i>t</i></b> value*/
        public t getMax() {
            return max;
        }

        /**
         * sets the stored maximum value
         * @param max type of <b><i>t</i></b> value to be set*/
        public void setMax(t max) {
            this.max = max;
        }
    }

    /**
     * Main class encapsulating methods used for minimum values and manipulation and storage of the value
     * @see Maximum Maximum version
     * @param <t> Numerical param for space efficiency between Byte ... Double*/
    public static class Minimum<t> {
        t min;

        /**
         * Class constructor setting the base minimum value
         * @param min type of class within value range*/
        public Minimum(t min) {
            this.min = min;
        }

        /**
         * Gets the stored minimum value
         * @return type of <b><i>t</i></b> value*/
        public t getMin() {
            return min;
        }

        /**
         * sets the stored minimum value
         * @param min type of <b><i>t</i></b> value to be set*/
        public void setMin(t min) {
            this.min = min;
        }
    }


    public enum GraphSize {
        SMALL(new Vector<>(10.0/*TBD*/,10.0/*TBD*/)),
        REGULAR(new Vector<>(585.0, 728.0)),
        LARGE(new Vector<>(639.0, 1169.0));

        /**
         * Local storage for the size of the graph*/
        private final Vector<Double, Double> size;

        /**
         * Stores the local vector value for sizes
         * @param size x and y size of the graph*/
        GraphSize(Vector<Double, Double> size) {
            this.size = size;
        }

        /**
         * Returns the y value of the vector
         * @see util.Vector Vector class
         * @return type Y (Double) y value*/
        public Double getY() {
            return size.getY();
        }

        /**
         * Returns the x value of the vector
         * @see util.Vector Vector class
         * @return type X (Double) x value*/
        public Double getX() {
            return size.getX();
        }
    }
}