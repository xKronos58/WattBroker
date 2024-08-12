package com.wattbroker.wattbroker;

import com.wattbroker.wattbroker.UserHandling.USER_ID;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;

public class UI_Controller {
    public HBox DashboardButton;
    public Pane ContentPane;
    public HBox MarketButton;
    public HBox DataButton;
    public HBox SettingsButton;
    public HBox Algorithms;

    public ImageView Dashboard_icon;
    public ImageView Market_icon;
    public ImageView Data_icon;
    public ImageView Settings_icon;
    public ImageView Algorithms_icon;
    public ScrollPane ScrollPaneAlg;

    public Node Market_Pane;
    public Node Dashboard_Pane;
    public Node Settings_Pane;
    public Node Algorithms_Pane;
    public Node Data_Pane;
    public ImageView userIcon;

    /* Selected colour:   #3E1D2A
    *  Deselected colour: #1C1C1C
    * */
    String lastPane = "";
    String currentPane = "";

    @FXML
    public void initialize() {
        try {
            if(Main.user.hasUploadedProfilePicture())
                userIcon.setImage(Main.user.webUrlOrFile()
                        ? new Image(String.valueOf(Objects.requireNonNull(
                                Main.class.getResource(Main.user.fileLocation()))))
                        : new Image(Main.user.fileLocation()));
            Market_Pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Market_Pane.fxml")));
            Dashboard_Pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard_Pane.fxml")));
            Settings_Pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settings_nav.fxml")));
            Algorithms_Pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Algorithm_Pane.fxml")));
            Data_Pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Data_Pane.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ShowDashboard(null);
    }

    public void ShowMarket(MouseEvent mouseEvent) {
        if(currentPane.equals("Market")) return;
        setContent("Market_Pane.fxml");
        MarketButton.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "Market";
        setLastButton(lastPane);
        Market_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Market_icon_selected.png")).toString()));
    }

    public void ShowData(MouseEvent mouseEvent) {
        if(currentPane.equals("Data")) return;
        setContent("Data_Pane.fxml");
        DataButton.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "Data";
        setLastButton(lastPane);
        Data_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Data_icon_selected.png")).toString()));
    }

    public void ShowSettings(MouseEvent mouseEvent) {
        if(currentPane.equals("Settings")) return;
        setContent("Settings_Pane.fxml");
        SettingsButton.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "Settings";
        setLastButton(lastPane);
        Settings_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Settings_icon_selected.png")).toString()));
    }

    public void ShowAlgorithms(MouseEvent mouseEvent) {
        if(currentPane.equals("Algorithms")) return;
        setContent("Algorithm_Pane.fxml");
        Algorithms.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "Algorithms";
        setLastButton(lastPane);
        Algorithms_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Algorithms_icon_selected.png")).toString()));
    }

    public void ShowDashboard(MouseEvent mouseEvent) {
        if(currentPane.equals("Dashboard")) return;
        setContent("Dashboard_Pane.fxml");
        DashboardButton.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "Dashboard";
        setLastButton(lastPane);
        Dashboard_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Dashboard_icon_selected.png")).toString()));
    }

    public void setContent(String fxml_Name) {
        ContentPane.getChildren().clear();
        ContentPane.getChildren().addAll(switch (fxml_Name) {
            case "Market_Pane.fxml" -> Market_Pane;
            case "Dashboard_Pane.fxml" -> Dashboard_Pane;
            case "Settings_Pane.fxml" -> Settings_Pane;
            case "Algorithm_Pane.fxml" -> Algorithms_Pane;
            case "Data_Pane.fxml" -> Data_Pane;
            default -> throw new IllegalStateException("Unexpected value: " + fxml_Name);
        });
    }

    public void setLastButton(String buttonName) {
        switch (buttonName) {
            case "Dashboard":
                DashboardButton.setStyle("-fx-background-color: #1C1C1C");
                Dashboard_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Dashboard_icon.png")).toString()));
                break;
            case "Market":
                MarketButton.setStyle("-fx-background-color: #1C1C1C");
                Market_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Market_icon.png")).toString()));
                break;
            case "Data":
                DataButton.setStyle("-fx-background-color: #1C1C1C");
                Data_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Data_icon.png")).toString()));
                break;
            case "Settings":
                SettingsButton.setStyle("-fx-background-color: #1C1C1C");
                Settings_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Settings_icon.png")).toString()));
                break;
            case "Algorithms":
                Algorithms.setStyle("-fx-background-color: #1C1C1C");
                Algorithms_icon.setImage(new Image(Objects.requireNonNull(Main.class.getResource("images/Algorithms_icon.png")).toString()));
                break;
        }
    }

    public void CloseALGPane(MouseEvent mouseEvent) {

    }
}
