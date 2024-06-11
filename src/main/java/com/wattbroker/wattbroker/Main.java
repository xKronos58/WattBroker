package com.wattbroker.wattbroker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1728, 1117);
        stage.setTitle("Watt broker");
        stage.setScene(scene);
        stage.show();

        Data d = new Data();
        d.getMarketGraph(new Date(System.currentTimeMillis()));

    }

    public static void main(String[] args) {
        launch();
    }
}