package com.wattbroker.wattbroker;

import com.wattbroker.wattbroker.UserHandling.USER_ID;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserProfile extends Application {
    public static Stage s;
    User user;
    USER_ID userId;

    public UserProfile(User user, USER_ID userId) throws Exception {
        this.user = user;
        this.userId = userId;
        start(new Stage());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountNavMenu.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle(new DB_query().getUsername(userId.getId()));
        primaryStage.show();
        s = primaryStage;
    }
}
