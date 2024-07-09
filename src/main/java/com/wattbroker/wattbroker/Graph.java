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
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Graph extends Pane {
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
    public Graph() {
        FXMLLoader load = new FXMLLoader(getClass().getResource("graph.fxml"));
        load.setRoot(this);
        load.setController(this);

        try {
            load.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        StackPane graph = new StackPane(wavyPath(plotPoints(
                Graph.graphType.Market.set_put(null, 'd'), new Data()
                        .getMarketData(new Date(System.currentTimeMillis()),
                                "market@2024-06-02_00:00:00-23:59:00.csv"))));

        graph.setId("Graph");

        graphPane.getChildren().add(graph);

        Line l = new Line(77, 24, 77+ dayButton.prefWidth(0), 24);
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

        StackPane graph = new StackPane(wavyPath(plotPoints(
                Graph.graphType.Market.set_put(null, lastGraph[0]), new Data()
                        .getMarketData(new Date(System.currentTimeMillis()),
                                switch (lastGraph[0]) {
                                    case 'h' -> "market@2024-06-01_00:00:00-23:59:00.csv";
                                    case 'd' -> "market@2024-06-02_00:00:00-23:59:00.csv";
                                    case 'w' -> "market@2024-06-03_00:00:00-23:59:00.csv";
                                    default -> throw new IllegalStateException("Unexpected value: " + lastGraph[0]);
                                }))));

        graph.setId("Graph");

        graphPane.getChildren().add(graph);
    }

    private Path wavyPath(List<util.Vector<Double, Double>> vectors) {
        Path path = new Path(new MoveTo(0, vectors.get(0).getX()));
        // Iterate through the list of vectors to create the curve
        for (int i = 0; i + 2 < vectors.size(); i += 3) {
            var v1 = vectors.get(i);
            var v2 = vectors.get(i + 1);
            var v3 = vectors.get(i + 2);

            // Create the cubic curve with the three control points
            CubicCurveTo curve = new CubicCurveTo(v1.getY(), v1.getX(), v2.getY(), v2.getX(), v3.getY(), v3.getX());
//            System.out.println(curve.getX() + ", " + curve.getY()); //(Logging for large data)
            path.getElements().add(curve);
        }

        // If there are leftover points that cannot form a complete cubic curve, handle them
        int remainingPoints = vectors.size() % 3;
        if (remainingPoints == 2) {
            var v1 = vectors.get(vectors.size() - 2);
            var v2 = vectors.get(vectors.size() - 1);
            // Create a quadratic curve to approximate the remaining points
            path.getElements().add(new CubicCurveTo(v1.getY(), v1.getX(), v2.getY(), v2.getX(), v2.getY(), v2.getX()));
        } else if (remainingPoints == 1) {
            var v1 = vectors.get(vectors.size() - 1);
            // Create a line to the last point
            path.getElements().add(new CubicCurveTo(v1.getY(), v1.getX(), v1.getY(), v1.getX(), v1.getY(), v1.getX()));
        }

        // TODO set data to gay switch

        path.setStroke(LinearGradient.valueOf("linear-gradient(to right, 81CFFC, 525BC3, 525BC3, 525BC3, 81CFFC)"));
//        path.setStroke(LinearGradient.valueOf("linear-gradient(to right, red, yellow, green, blue, purple)"));
        path.setStrokeWidth(3);
        return path;
    }

    enum graphType {
        Market {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt) {
                com.wattbroker.wattbroker.Data data = new Data();
                List<Data.tV> _data = data.getMarketData(new Date(System.currentTimeMillis()),
                        switch (gt) {
                            case 'h' -> "market@2024-06-01_00:00:00-23:59:00.csv";
                            case 'd' -> "market@2024-06-02_00:00:00-23:59:00.csv";
                            case 'w' -> "market@2024-06-03_00:00:00-23:59:00.csv";
                            default -> throw new IllegalStateException("Unexpected value: " + gt);
                        });
                Data.tV initial = _data.get(0), end = _data.get(_data.size()-1);
                // Check time and devise scale :
                String t1 = initial.dateTime(), t2 = end.dateTime();
                graphTimeAxisType type ;
                type = findTimeAxis(t1, t2);
                type.set();

                long current = System.nanoTime();
                // Find y (Value) axis high and low.
                double max_val = 0, min_val = 0;
                for (Data.tV data_i : _data) {
                    if (data_i.value() > max_val)
                        max_val = data_i.value();
                    else if (data_i.value() < min_val)
                        min_val = data_i.value();
                }


                long _final = System.nanoTime();
                long time = _final-current;
                System.out.println(time + "ns => " + time/10000 + "ms");

                List<Vector<Minimum<Double>, Maximum<Double>>> maxMin = new ArrayList<>();
                maxMin.add(new Vector<>(new Minimum<>(min_val), new Maximum<>(max_val)));
                maxMin.add(new Vector<>(new Minimum<>(0.0), new Maximum<>(_data.size() - 1.0)));
                return maxMin;
            }
        }, Data {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt) {
                return null;
            }
        }, Settings {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt) {
                return null;

            }
        }, Algorithms {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt) {
                return null;

            }
        };
        public abstract List<Vector<Minimum<Double>, Maximum<Double>>> set_put(List<URI> apiLocation, char gt);
    }

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

    public static graphTimeAxisType findTimeAxis(String t1, String t2) {
        int indexOfBreak_t1 = util.until(0, t1, ' '), indexOfBreak_t2 = util.until(0, t2, ' ');
        graphTimeAxisType type;
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
     * */
    @SuppressWarnings("ClassEscapesDefinedScope")
    public List<Vector<Double, Double>> plotPoints(
            List<Vector<Minimum<Double>, Maximum<Double>>> maxMin, List<Data.tV> data) {
        if(maxMin.size() != 2)
            throw new IllegalArgumentException("maxMin must have 2 elements");
        Vector<Double, Double> graphSize = new Vector<>(585.0, 728.0);

        List<Vector<Double, Double>> points = new ArrayList<>();

        double d1 = graphSize.getY()/maxMin.get(1).getY().max,
                d2 = graphSize.getX()/maxMin.get(0).getY().max;

        for(int i = 0; i < data.size(); i++) {
            double x = data.get(i).value(), y = i;
            x *= d2;
            y *= d1;
            x = graphSize.getY() - x;
            points.add(new Vector<>(x, y));
        }

        return points;
    }

    public static class Maximum<t> {
        t max;
        public Maximum(t max) {
            this.max = max;
        }

        public t getMax() {
            return max;
        }

        public void setMax(t max) {
            this.max = max;
        }
    }

    public static class Minimum<t> {
        t min;
        public Minimum(t min) {
            this.min = min;
        }

        public t getMin() {
            return min;
        }

        public void setMin(t min) {
            this.min = min;
        }
    }

}