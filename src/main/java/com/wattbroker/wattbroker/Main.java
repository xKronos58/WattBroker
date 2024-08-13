package com.wattbroker.wattbroker;

import com.util.algorithmFileReader;
import com.wattbroker.wattbroker.UserHandling.USER_ID;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class Main extends Application {

    public static User user;

    public static USER_ID userId;

    public Main(User user) { Main.user = user; }

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1728, 1142);
        stage.setTitle("Watt broker");
        stage.setScene(scene);
        stage.show();
        primaryStage = stage;
    }

    public static void main(String[] args) {
        userId=new USER_ID(args[0]);
        try {
            new Main(new User()).start(new Stage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}