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
import java.util.Objects;

/***/
public class Graph extends Pane {
    @FXML @SuppressWarnings("unused") private Pane graphPane;
    @FXML @SuppressWarnings("unused") private Text hourButton;
    @FXML @SuppressWarnings("unused") private Text dayButton;
    @FXML @SuppressWarnings("unused") private Text weekButton;
    @FXML @SuppressWarnings("unused") private AnchorPane root;

    public static final LinearGradient Demand = LinearGradient.valueOf("linear-gradient(to right, EB00FF, EB00FF, D492F3, EB00FF, EB00FF)");
    public static final LinearGradient Price = LinearGradient.valueOf("linear-gradient(to right, 81CFFC, 525BC3, 525BC3, 525BC3, 81CFFC)");
    public static final LinearGradient Supply = LinearGradient.valueOf("linear-gradient(to right, 00FF85, 00FF85, 72DC76, 00FF85, 00FF85)");
    public static final LinearGradient Renewable = LinearGradient.valueOf("linear-gradient(to right, EBFF00, F1F392, EBFF00)");
    public static final LinearGradient Fossil = LinearGradient.valueOf("linear-gradient(to right, FF0000, F39292, FF0000)");
    public static final LinearGradient Profit = LinearGradient.valueOf("linear-gradient(to right, 00FF85, 00FF85, 72DC76, 00FF85, 00FF85)");
    public static final LinearGradient Efficiency = LinearGradient.valueOf("linear-gradient(to right, 81CFFC, 525BC3, 525BC3, 525BC3, 81CFFC)");

    public static final LinearGradient Price_Fill = LinearGradient.valueOf("linear-gradient(to bottom, 5057FF, 774FAF00)");
    public static final LinearGradient Demand_Fill = LinearGradient.valueOf("linear-gradient(to bottom, 5057FF, 774FAF00)");
    public static final LinearGradient Supply_Fill = LinearGradient.valueOf("linear-gradient(to bottom, 00FF8550, 00FFA300)");
    public static final LinearGradient Renewable_Fill = LinearGradient.valueOf("linear-gradient(to bottom, EBFF0050, EBFF0000)");
    public static final LinearGradient Fossil_Fill = LinearGradient.valueOf("linear-gradient(to bottom, FF000050, FF000000)");
    public static final LinearGradient Profit_Fill = LinearGradient.valueOf("linear-gradient(to bottom, 00FF8550, 00FFA300)");
    public static final LinearGradient Efficiency_Fill = LinearGradient.valueOf("linear-gradient(to bottom, 81CFFC50, 81CFFC00)");

    public static final String yesterdayData = "AEMO@2024-08-19.csv";
    public static final String todayData = "AEMO@2024-08-20.csv";
    public static final String tomorrowData = "AEMO@2024-08-21.csv";

    long start;

    /**
     * Graph class renders a styled graph based of sets of data rendered in three different sizes (Small, Regular, Large), this class is for the Regular graph size
     * Loads the "graph.fxml" file under the resource directory
     * @see GraphSize size of the graph
     * @see LargeGraph large version of graph
     * @see SmallGraph small version of graph
     * @throws Exception *(within code) e if "graph.fxml" is not found under the resource directory */
    public Graph() {
        // Get fxml file
        FXMLLoader load = new FXMLLoader(getClass().getResource("graph.fxml"));

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
        StackPane graph = new StackPane(wavyPath(Objects.requireNonNull(plotPoints_AEMO(
                graphType.AEMO.set_put(null, 'd', "SPOT_PRICE"), new Data()
                        .getAEMOdata("AEMO@2024-08-20.csv"), GraphSize.REGULAR, "SPOT_PRICE")), GraphSize.REGULAR, Price, true, Price_Fill),
                wavyPath(Objects.requireNonNull(plotPoints_AEMO(
                        graphType.AEMO.set_put(null, 'd', "DEMAND"), new Data().getAEMOdata("AEMO@2024-08-20.csv"), GraphSize.REGULAR, "DEMAND")), GraphSize.REGULAR, Demand, false, null),
                wavyPath(Objects.requireNonNull(plotPoints_AEMO(
                        graphType.AEMO.set_put(null, 'd', "GENERATION"), new Data().getAEMOdata("AEMO@2024-08-20.csv"), GraphSize.REGULAR, "GENERATION")), GraphSize.REGULAR, Supply, false, null)

        );

        // Add id to the graph for later usage
        graph.setId("Graph");

        // Add the graph to the root pane
        graphPane.getChildren().add(graph);

        // Add the currently selected button line
        Line l = new Line(131, 24, 131+ dayButton.prefWidth(0), 24);
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
            l.setTranslateX(dayButton.getLayoutX());
            hourButton.setFill(Color.rgb(132, 132, 132, 1));
            weekButton.setFill(Color.rgb(132, 132, 132, 1));
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
            l.setTranslateX(weekButton.getLayoutX());
            hourButton.setFill(Color.rgb(132, 132, 132, 1));
            dayButton.setFill(Color.rgb(132, 132, 132, 1));
            // Set last graph
            lastGraph[0] = 'w';
        });
    }

    public void updateGraph(char[] lastGraph) {
        for(Node n : graphPane.getChildren()) {
            if(n.getId().equals("Graph")) {
                graphPane.getChildren().remove(n);
                break;
            }
        }

        StackPane graph = new StackPane(wavyPath(Objects.requireNonNull(plotPoints_AEMO(
                graphType.AEMO.set_put(null, lastGraph[0], "SPOT_PRICE"), new Data()
                        .getAEMOdata(
                                switch (lastGraph[0]) {
                                    case 'h' -> "AEMO@2024-08-19.csv";
                                    case 'd' -> "AEMO@2024-08-20.csv"; /* DOWNLOAD OTHER FILES */
                                    case 'w' -> "AEMO@2024-08-21.csv";
                                    default -> throw new IllegalStateException("Unexpected value: " + lastGraph[0]);
                                }), GraphSize.REGULAR, "SPOT_PRICE")), Graph.GraphSize.REGULAR, Price, true, Price_Fill),
                wavyPath(Objects.requireNonNull(plotPoints_AEMO(
                        graphType.AEMO.set_put(null, lastGraph[0], "DEMAND"), new Data()
                                .getAEMOdata(
                                        switch (lastGraph[0]) {
                                            case 'h' -> "AEMO@2024-08-19.csv";
                                            case 'd' -> "AEMO@2024-08-20.csv"; /* DOWNLOAD OTHER FILES */
                                            case 'w' -> "AEMO@2024-08-21.csv";
                                            default -> throw new IllegalStateException("Unexpected value: " + lastGraph[0]);
                                        }), GraphSize.REGULAR, "DEMAND")), Graph.GraphSize.REGULAR, Demand, false, null),
                wavyPath(Objects.requireNonNull(plotPoints_AEMO(
                        graphType.AEMO.set_put(null, lastGraph[0], "GENERATION"), new Data()
                                .getAEMOdata(
                                        switch (lastGraph[0]) {
                                            case 'h' -> "AEMO@2024-08-19.csv";
                                            case 'd' -> "AEMO@2024-08-20.csv"; /* DOWNLOAD OTHER FILES */
                                            case 'w' -> "AEMO@2024-08-21.csv";
                                            default -> throw new IllegalStateException("Unexpected value: " + lastGraph[0]);
                                        }), GraphSize.REGULAR, "GENERATION")), Graph.GraphSize.REGULAR, Supply, false, null));

        graph.setId("Graph");

        graphPane.getChildren().add(graph);
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public static StackPane wavyPath(List<Vector<Double, Double>> vectors, GraphSize size, LinearGradient gradient, boolean hasFill, LinearGradient fillGradient) {
        Path path = new Path(new MoveTo(0, vectors.get(0).getX())),
                Overlay = new Path(new MoveTo(0, size.getY()), new LineTo(0, vectors.get(0).getX()));

        // Iterate through the list of vectors to create the curve
        for (int i = 0; i + 2 < vectors.size(); i += 3) {
            var v1 = vectors.get(i);
            var v2 = vectors.get(i + 1);
            var v3 = vectors.get(i + 2);

            // Create the cubic curve with the three control points
            CubicCurveTo curve = new CubicCurveTo(v1.getY(), v1.getX(), v2.getY(), v2.getX(), v3.getY(), v3.getX());
//            System.out.println(curve.getX() + ", " + curve.getY()); //(Logging for large data)
            path.getElements().add(curve);
            Overlay.getElements().add(curve);

        }

        // If there are leftover points that cannot form a complete cubic curve, handle them
        int remainingPoints = vectors.size() % 3;
        if (remainingPoints == 2) {
            var v1 = vectors.get(vectors.size() - 2);
            var v2 = vectors.get(vectors.size() - 1);
            // Create a quadratic curve to approximate the remaining points
            CubicCurveTo x = new CubicCurveTo(v1.getY(), v1.getX(), v2.getY(), v2.getX(), v2.getY(), v2.getX());
            path.getElements().add(x);
            if(hasFill) Overlay.getElements().add(x);
        } else if (remainingPoints == 1) {
            var v1 = vectors.get(vectors.size() - 1);
            // Create a line to the last point
            CubicCurveTo x1 = new CubicCurveTo(v1.getY(), v1.getX(), v1.getY(), v1.getX(), v1.getY(), v1.getX());
            path.getElements().add(x1);
            if(hasFill) Overlay.getElements().add(x1);
        }


        if(hasFill) Overlay.getElements().add(new LineTo(vectors.get(vectors.size() -1).getY(), size.getY()));

        if(hasFill) Overlay.setStroke(Color.rgb(0, 0, 0, 0.0));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        dropShadow.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);

        path.setStroke(gradient);

        if(hasFill) Overlay.setFill(fillGradient);
        path.setStrokeWidth(3);

        Node ValueNeedle;

        ValueNeedle = new Circle(10);

        // Drag Function ** add for other graphs
        Overlay.setOnMouseMoved(e -> {
//            System.out.println(e.getX() + ", " + e.getY());
            ValueNeedle.setTranslateX(e.getX()-360); // TODO Add scalar based off graph size to snap to correct x;
            // Find Y value
            int x = (int) ((e.getX()/vectors.size()));

            double y = vectors.get(Math.min(x, vectors.size() - 1)).getY();

            ValueNeedle.setTranslateY(y);
        });


        StackPane temp;
        // Check if has a fill if it does then add the overlay otherwise should only add the path to avoid duplicate overlays.
        if(hasFill) temp = new StackPane(Overlay, path, ValueNeedle);
        else temp = new StackPane(path);
        temp.setAlignment(Pos.TOP_CENTER);

        return temp;
    }

    private static final LinearGradient gradient = new LinearGradient(1, -0.5, 1, 1, true, null,
            new Stop(-2, Color.rgb(74, 165, 210, 1)),
            new Stop(1, Color.rgb(119, 79, 175, 0.1)));

    /**
     * Defines the type of graph
     * and differentiates the required scalars and other graph modifiers for each graph type and dataset
     * @see graphTimeAxisType Graph scalar */
    enum graphType {
        Market {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt, String type) {
                com.wattbroker.wattbroker.Data data = new Data();
                // Get market data from these files * Requires AEMO API access
                List<Data.tV> _data = data.getMarketData(new Date(System.currentTimeMillis()),
                        switch (gt) { //TODO once AEMO API access is gotten change formatting to those datasets
                            case 'h' -> "market@2024-06-01_00:00:00-23:59:00.csv";
                            case 'd' -> "market@2024-06-02_00:00:00-23:59:00.csv";
                            case 'w' -> "market@2024-06-03_00:00:00-23:59:00.csv";
                            default -> throw new IllegalStateException("Unexpected value: " + gt);
                        });

//                Data.tV initial = _data.get(0), end = _data.get(_data.size()-1);
                // Check time and devise scale :
//                String t1 = initial.dateTime(), t2 = end.dateTime();

                // TO be implemented
//                graphTimeAxisType type ;
//                type = findTimeAxis(t1, t2);

                // Get time current time for performance metrics
                long current = System.nanoTime();

                // Find y (Value) axis high and low.
                double max_val = 0, min_val = 0;
                for (Data.tV data_i : _data) {
                    if (data_i.value() > max_val)
                        max_val = data_i.value();
                    else if (data_i.value() < min_val)
                        min_val = data_i.value();
                }

                // Return time taken
                long _final = System.nanoTime();
                long time = _final-current;
                System.out.println(time + "ns => " + time/100000000 + "ms");

                // Return the max and min values of the data
                List<Vector<Minimum<Double>, Maximum<Double>>> maxMin = new ArrayList<>();
                maxMin.add(new Vector<>(new Minimum<>(min_val), new Maximum<>(max_val)));
                maxMin.add(new Vector<>(new Minimum<>(0.0), new Maximum<>(_data.size() - 1.0)));
                return maxMin;
            }
        }, Data {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt, String type) {
                return null;
            }
        }, Settings {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt, String type) {
                return null;

            }
        }, Algorithms {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt, String type) {
                return null;

            }
        }, AEMO {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt, String type) {
                com.wattbroker.wattbroker.Data data = new Data();

                // Graph Type Overhaul
                com.wattbroker.wattbroker.AEMO.type graph_type_dataset = com.wattbroker.wattbroker.AEMO.ConvertToType(type);

                List<AEMO> _data = data.getAEMOdata(switch (gt) {
                    case 'h' -> "AEMO@2024-08-19.csv"; // ADD _HT
                    case 'd' -> "AEMO@2024-08-20.csv"; // Add _DT to allow for differently formatted data
                    case 'w' -> "AEMO@2024-08-21.csv"; // ADD _WT
                    default -> throw new IllegalStateException("Unexpected value: " + gt);
                });

                double max_val = 0, min_val = 0;

                for(AEMO aemo_data : _data) {
                    if(aemo_data.get_special(graph_type_dataset) > max_val) {
                        max_val = aemo_data.get_special(graph_type_dataset);
                    } else if(aemo_data.get_special(graph_type_dataset) < min_val) {
                        min_val = aemo_data.get_special(graph_type_dataset);
                    }
                }

                List<Vector<Minimum<Double>, Maximum<Double>>> maxMin = new ArrayList<>();
                maxMin.add(new Vector<>(new Minimum<>(min_val), new Maximum<>(max_val)));
                maxMin.add(new Vector<>(new Minimum<>(0.0), new Maximum<>(_data.size() - 1.0)));
                return maxMin;
            }
        };

        /**
         * Sets the graph type.
         *
         * @see Vector Vector class
         * @see Minimum Minimum type
         * @see Maximum Maximum tyoe
         */
        public abstract List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt, String type);
    }

    /**
     * Stores the different time break indexes used for devising graph scales for different time ranges of data
     * */
    public enum graphTimeAxisType {
        SECOND(10), // 10ms of 100ms => 10
        MINUTE(6),  // 10s of 60s => 6
        HOUR(10),   // 10m of 60m => 6
        DAY(8),     // 3h of 24h => 8
        WEEK(7);    // 1d of 7d => 7

        private final int breaks;

        /**
         * Constructor for each time set
         * @param i amount of time breaks */
        graphTimeAxisType(int i) {
            this.breaks = i;
        }

        /**
         * gets the break index
         * @return int index of breaks */
        public int getBreaks() {
            return breaks;
        }
    }

    /**
     * Finds the time axis of the graph
     * @param t1 the initial time
     * @param t2 the final time
     * @return the time axis of the graph
     * */
    public static graphTimeAxisType findTimeAxis(String t1, String t2) {
        // Get the index of breaks in the time
        int indexOfBreak_t1 = util.until(0, t1, ' '),
                indexOfBreak_t2 = util.until(0, t2, ' ');
        graphTimeAxisType type;
        // Check the time axis
        if(t1.charAt(indexOfBreak_t1 - 1) != t2.charAt(indexOfBreak_t2 - 1)) {
            type = graphTimeAxisType.WEEK;
        } else if (!t1.substring(indexOfBreak_t1, indexOfBreak_t1 + 2)
                .equals (t2.substring(indexOfBreak_t2, indexOfBreak_t2 + 2))) {
            type = graphTimeAxisType.HOUR;
        } else if (!t1.substring(indexOfBreak_t1 + 3, indexOfBreak_t1 + 5)
                .equals(t2.substring(indexOfBreak_t2 + 3, indexOfBreak_t2 + 5))) {
            type = graphTimeAxisType.MINUTE;
        } else {
            type = graphTimeAxisType.SECOND;
        }

        return type;
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
    public static List<Vector<Double, Double>> plotPoints(
            List<Vector<Minimum<Double>, Maximum<Double>>> maxMin, List<Data.tV> data,
            GraphSize size
    ) {
        // Check if maxMin is of size 2
        if(maxMin.size() != 2)
            throw new IllegalArgumentException("maxMin must have 2 elements");

        // Define the list of points to be plotted
        List<Vector<Double, Double>> points = new ArrayList<>();

        // Define the scaling factor for the graph
        double d1 = size.getY()/maxMin.get(1).getY().max,
                d2 = size.getX()/maxMin.get(0).getY().max;

        // Iterate through the data and convert it to a vector
        for(int i = 0; i < data.size(); i++) {
            double x = data.get(i).value(), y = i;
            x *= d2; // Multiply the x by the x scalar value
            y *= d1; // Multiply the x by the x scalar value
            x = size.getY() - x;
//            System.out.println(x + ", " + y); // Logging for large data sets
            points.add(new Vector<>(x, y)); // Add the points
        }

        return points;
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public static List<Vector<Double, Double>> plotPoints_AEMO(
            List<Vector<Minimum<Double>, Maximum<Double>>> maxMin, List<AEMO> data, GraphSize graphSize, String type
    ) {
        AEMO.type GraphDataType = AEMO.ConvertToType(type);

        if(maxMin.size() != 2) {
            throw new IllegalArgumentException("maxMin must have 2 elements");
        }

        double mod = 0;

        if(maxMin.get(0).getX().getMin() < 0) {
            for (AEMO d : data)
                d.incrimentVal(GraphDataType, maxMin.get(0).getX().getMin() * -1);
            mod += maxMin.get(0).getX().getMin() * -1;
        }

        List<Vector<Double, Double>> points = new ArrayList<>();

        double d1 = graphSize.getY()/(maxMin.get(1).getY().max),
                d2 = graphSize.getX()/(maxMin.get(0).getY().max + mod);

        // Iterate through the data and convert it to a vector
        for(int i = 0; i < data.size(); i++) {
            double x = data.get(i).value(GraphDataType), y = i;
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

    /**
     * Enum class for the size of the graph
     * @see Vector Vector class
     * */
    public enum GraphSize {
        SMALL(new Vector<>(390.0, 483.0)),
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