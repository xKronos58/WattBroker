package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.Objects;

public class UserProfileController {
    public AnchorPane account_back;
    public Text account_button;
    public AnchorPane statistics_back;
    public Text statistics_button;
    public AnchorPane data_back;
    public Text data_button;
    public Pane contentPane;

    Node Account;
    Node Statistics;
    Node Data;
    String lastPane = "";

    @FXML
    void initialize() {

        try {
            Account = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Account.fxml")));
            Statistics = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Statistics.fxml")));
            Data = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Data_account.fxml")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setAccount();

        account_button.setOnMouseClicked(e -> setAccount());
        statistics_button.setOnMouseClicked(e -> setStatistics());
        data_button.setOnMouseClicked(e -> setData());
    }

    private void setAccount() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(Account);
        account_back.setStyle("-fx-background-color: #172854; -fx-background-radius: 25");
        if(!lastPane.equals("account")) setLastPane();
        lastPane = "account";
    }

    private void setLastPane() {
        switch (lastPane) {
            case "account":
                account_back.setStyle("-fx-background-color: transparent");
                break;
            case "statistics":
                statistics_back.setStyle("-fx-background-color: transparent");
                break;
            case "data":
                data_back.setStyle("-fx-background-color: transparent");
                break;
        }
    }

    private void setStatistics() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(Statistics);
        statistics_back.setStyle("-fx-background-color: #172854; -fx-background-radius: 25");
        if(!lastPane.equals("statistics")) setLastPane();
        lastPane = "statistics";
    }
    private void setData() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(Data);
        data_back.setStyle("-fx-background-color: #172854; -fx-background-radius: 25");
        if(!lastPane.equals("data")) setLastPane();
        lastPane = "data";
    }
}
