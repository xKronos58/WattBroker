package com.wattbroker.wattbroker;

import com.util.util;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Date;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Graph g = new Graph();
        Button b = new Button("Test");
        b.setOnAction(e -> {
            g.start = System.currentTimeMillis();
            var temp = g.plotPoints(Graph.graphType.Market.set_put(null, 'd'), new Data().getMarketData(new Date(System.currentTimeMillis()), "market@2024-06-12_00:00:00-23:59:00.csv"), Graph.GraphSize.REGULAR);
            for(util.Vector<Double, Double> v : temp)
                System.out.println(v.toString());
            System.out.println("Time taken: " + (System.currentTimeMillis() - g.start) + "ms");
        });
        VBox vb = new VBox();
        vb.getChildren().addAll(g, b);
        Scene s = new Scene(vb, 1000, 720);
        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
