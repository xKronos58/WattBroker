package com.wattbroker.wattbroker;

import com.util.util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public static Node market() {
        return graphType.Market.setput(null);
    }
    public static Node data() {
        return graphType.Data.setput(null);
    }
    public static Node settings() {
        return graphType.Settings.setput(null);
    }
    public static Node algorithms() {
        return graphType.Algorithms.setput(null);
    }


    enum graphType {
        Market {
            @Override
            public Pane setput(List<URI> apiLocation) {
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
                double max = 0;
                for (Data.tV datum : _data) {
                    if (datum.value() > max)
                        max = datum.value();
                }
                long _final = System.nanoTime();
                System.out.println(_final-current + "ns\n"+max);
                return null;
            }
        }, Data {
            @Override
            public Pane setput(List<URI> apiLocation) {
                return null;

            }
        }, Settings {
            @Override
            public Pane setput(List<URI> apiLocation) {
                return null;

            }
        }, Algorithms {
            @Override
            public Pane setput(List<URI> apiLocation) {
                return null;

            }
        };
        public abstract Pane setput(List<URI> apiLocation);
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
}
