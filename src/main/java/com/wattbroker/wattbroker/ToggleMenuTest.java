package com.wattbroker.wattbroker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a testing class for creating the toggle menu. refer to ToggleMenu for actual toggle menu
 * @see ToggleMenu*/
public class ToggleMenuTest extends Application {

    static List<Algorithm> algos = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        algos.addAll(List.of(new Algorithm("Algorithm 1", 100, 0, false),
                new Algorithm("Algorithm 2", 200, 0, false),
                new Algorithm("Algorithm 3", 300, 0, false)));

        VBox vbox = new VBox(10); // Spacing of 10 between items
        AnchorPane Button = new AnchorPane();
        Button.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-border-radius: 10; -fx-background-color: #171717; -fx-background-radius: 10;");
        Text text = new Text("Algorithms");
        SVGPath buttonIcon = new SVGPath();
        buttonIcon.setContent("M12.3389 19.125L24.6776 31.875L37.0163 19.125");
        buttonIcon.setStroke(javafx.scene.paint.Color.WHITE);
        buttonIcon.setStrokeWidth(3);
        Button.getChildren().addAll(text, buttonIcon);
        AnchorPane.setLeftAnchor(text, 10.0);
        AnchorPane.setTopAnchor(text, 10.0);
        AnchorPane.setRightAnchor(buttonIcon, 10.0);
        AnchorPane.setTopAnchor(buttonIcon, 10.0);

        final state[] currentState = {state.hidden};

        VBox rootBox = new VBox(10);
        for (Algorithm algo : algos) {
            AnchorPane pane = createToggleablePane(algo.name, algo.profit, algo.holding, algo.running);
            rootBox.getChildren().add(pane);
        }

        rootBox.setVisible(false);
        vbox.getChildren().addAll(Button, rootBox);

        buttonIcon.setOnMouseClicked(e -> {

            if(currentState[0] == state.hidden) {
                currentState[0] = state.shown;
                rootBox.setVisible(true);
                buttonIcon.setContent("M12.3389 31.875L24.6776 19.125L37.0163 31.875");
            } else {
                currentState[0] = state.hidden;
                rootBox.setVisible(false);
                buttonIcon.setContent("M12.3389 19.125L24.6776 31.875L37.0163 19.125");
            }

        });

        primaryStage.setScene(new Scene(vbox, 400, 600));
        primaryStage.show();
    }

    enum state {shown, hidden}

    private AnchorPane createToggleablePane(String name, double profit, double holding, boolean running) {
        AnchorPane pane = new AnchorPane();
        pane.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10; -fx-background-color: #171717;");
        Text text = new Text(name);
        pane.getChildren().add(text);
        return pane;
    }

    public static void main(String[] args) {
        launch(args);
    }

    class Algorithm {
        String name;
        double profit;
        double holding;
        boolean running;

        public Algorithm(String name, double profit, double holding, boolean running) {
            this.name = name;
            this.profit = profit;
            this.holding = holding;
            this.running = running;
        }
    }
}