package com.wattbroker.wattbroker;

import com.util.util;
import com.util.util.Vector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Graph extends Pane {
    @FXML
    private Pane graph_pane;

    public Graph() {
        FXMLLoader load = new FXMLLoader(getClass().getResource("graph.fxml"));
        load.setRoot(this);
        load.setController(this);

        graphType.Market.setput(null);
        try {
            load.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static Node market() {
//        return graphType.Market.setput(null);
//    }
//    public static Node data() {
//        return graphType.Data.setput(null);
//    }
//    public static Node settings() {
//        return graphType.Settings.setput(null);
//    }
//    public static Node algorithms() {
//        return graphType.Algorithms.setput(null);
//    }


    enum graphType {
        Market {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> setput(List<URI> apiLocation) {
                com.wattbroker.wattbroker.Data data = new Data();
                List<Data.tV> _data = data.getMarketData(new Date(System.currentTimeMillis()));
                Data.tV initial = _data.get(0), end = _data.get(_data.size()-1);
                // Check time and devise scale :
                String t1 = initial.dateTime(), t2 = end.dateTime();
                graphTimeAxisType type = null;
                type = findTimeAxis(t1, t2, type);
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
                System.out.println(_final-current + "ns\n"+max_val);

                List<Vector<Minimum<Double>, Maximum<Double>>> maxMin = new ArrayList<>();
                maxMin.add(new Vector<>(new Minimum<>(min_val), new Maximum<>(max_val)));
                maxMin.add(new Vector<>(new Minimum<>(0.0), new Maximum<>(_data.size() - 1.0)));
                return maxMin;
            }
        }, Data {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> setput(List<URI> apiLocation) {
                return null;

            }
        }, Settings {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> setput(List<URI> apiLocation) {
                return null;

            }
        }, Algorithms {
            @Override
            public List<Vector<Minimum<Double>, Maximum<Double>>> setput(List<URI> apiLocation) {
                return null;

            }
        };
        public abstract List<Vector<Minimum<Double>, Maximum<Double>>> setput(List<URI> apiLocation);
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

    public static graphTimeAxisType findTimeAxis(String t1, String t2, graphTimeAxisType type) {
        int indexOfBreak_t1 = util.until(0, t1, ' '), indexOfBreak_t2 = util.until(0, t2, ' ');
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
    private List<Vector<Double, Double>> plotPoints(
            List<Vector<Minimum<Double>, Maximum<Double>>> maxMin, List<Data.tV> data) {
        if(maxMin.size() != 2)
            throw new IllegalArgumentException("maxMin must have 2 elements");
        Vector<Double, Double> graphSize = new Vector<>(graph_pane.getWidth(), graph_pane.getHeight());
        Vector<Double, Double> graphOrigin = new Vector<>(maxMin.get(0).getX().min, maxMin.get(1).getX().min);
        Vector<Double, Double> Ratio_xy = Ratio(graphSize);
        Vector<Double, Double> Ratio_vn_vx = Ratio(false, maxMin.get(0));
        Vector<Double, Double> Ratio_vn_vy = Ratio(true, maxMin.get(1));

        for(Data.tV d : data) {
            double x = d.value(), y = d.dateTimeAsDouble();
            double x_ = x * Ratio_vn_vx.getX(), y_ = y * Ratio_vn_vy.getX();
            x_ = x_ * Ratio_xy.getX();
            y_ = y_ * Ratio_xy.getY();
        }

        // x = a_length, y = b_length, ratio = x/y, y/x
        // value =y, time =x


        return null;
    }

    private Vector<Double, Double> Ratio(Vector<Double, Double> wh) {
        double x = wh.getX(), y = wh.getY();
        return new Vector<>(x / y, y / x);
    }

    /**
     * @param ignore IGNORE THIS! it does not matter what you pass in as it is to differentiate the pass-through type.
     * */
    private Vector<Double, Double> Ratio(Boolean ignore, Vector<Minimum<Double>, Maximum<Double>> ah) {
        double x = ah.getX().min, y = ah.getY().max;
        return new Vector<>(x / y, y / x);
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
