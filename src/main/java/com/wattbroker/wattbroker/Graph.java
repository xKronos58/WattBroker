package com.wattbroker.wattbroker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.net.URI;
import java.util.List;

public class Graph extends Pane {
    @FXML
    private Pane graph_pane;

    public Graph() {
        FXMLLoader load = new FXMLLoader(getClass().getResource("graph.fxml"));
        load.setRoot(this);
        load.setController(this);

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
}
