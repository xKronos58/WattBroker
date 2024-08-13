package com.wattbroker.wattbroker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class WattBroker extends Application {

    public static WattBroker instance;

    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene s = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml"))), 1728, 1117);
        WattBroker.primaryStage = primaryStage;
        primaryStage.setScene(s);
        primaryStage.setTitle("Watt Broker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public WattBroker() {
        // Prevent multiple instances of WattBroker
        if (instance != null) {
            throw new RuntimeException("WattBroker is already running");
        }
        // Set the static instance reference
        instance = this;
    }
}
