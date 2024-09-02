package com.wattbroker.wattbroker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main clas for wattbroker launches into the login screen
 * <b>Ignore commented code, problem with mac-os import</b>*/
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

        // Ignore this code as there is a problem with the import causing a runtime error

//        URL iconURL = Main.class.getResource("images/wattbroker_icon.svg");
//        assert iconURL != null;
////        Image icon = new Image(String.valueOf(iconURL));
//
//        if (System.getenv("OS").equals("mac")) {
//            //Set icon
//            try {
//                // Different type of image for mac OS
//            } catch (Exception e) {
//                // Mac exception accounted for ...
//            }
//        } else if (System.getenv("OS").equals("windows")) {
////            primaryStage.getIcons().add(icon);
//        } else if (System.getenv("OS").equals("linux")) {
//
//        } else {} // BRO what the fudge...

//        SearchBar.panes p = SearchBar.panes.fromString("setting");
//        System.out.println(p);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public WattBroker() {
        // Prevent multiple instances of WattBroker
//        if (instance != null) {
//            throw new RuntimeException("WattBroker is already running");
//        }
        // Set the static instance reference
//        main(null);
        instance = this;
    }
}
