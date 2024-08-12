package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.util.Objects;

public class SettingsNavController {

    public AnchorPane general_button;
    public AnchorPane trading_button;
    public AnchorPane integration_button;
    public AnchorPane legal_button;
    public AnchorPane support_button;
    public Pane background_pane;
    public SVGPath general_icon;
    public SVGPath trading_icon;
    public SVGPath integration_icon;
    public SVGPath legal_icon;
    public SVGPath support_icon;
    Node GeneralSettings;
    Node TradingSettings;
    Node IntegrationSettings;
    Node LegalSettings;
    Node Support;

    @FXML
    public void initialize() {
        try {
            GeneralSettings = FXMLLoader.load((Objects.requireNonNull(Main.class.getResource("GeneralSettings.fxml"))));
            TradingSettings = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("TradingSettings.fxml")));
            IntegrationSettings = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("IntegrationSettings.fxml")));
            LegalSettings = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("LegalSettings.fxml")));
            Support = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Support.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SetGeneralSettings(null);
    }

    String lastPane = "";
    String currentPane = "";

    public void SetGeneralSettings(MouseEvent mouseEvent) {
        if(currentPane.equals("general")) return;
        background_pane.getChildren().clear();
        setSettings("general");

        general_button.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "general";

        setLastButton(lastPane);
        general_icon.setStyle("-fx-stroke: #4C7EFF; -fx-stroke-width: 2");
    }

    private void setLastButton(String lastPane) {
        switch (lastPane) {
            case "general" -> {
                general_button.setStyle("-fx-background-color: #1C1C1C; -fx-background-radius: 25px");
                general_icon.setStyle("-fx-stroke: #FFF; -fx-stroke-width: 2");
            }
            case "trading" -> {
                trading_button.setStyle("-fx-background-color: #1C1C1C; -fx-background-radius: 25px");
                trading_icon.setStyle("-fx-stroke: #FFF; -fx-stroke-width: 2");
            }
            case "integration" -> {
                integration_button.setStyle("-fx-background-color: #1C1C1C; -fx-background-radius: 25px");
                integration_icon.setStyle("-fx-stroke: #FFF; -fx-stroke-width: 2");
            }
            case "legal" -> {
                legal_button.setStyle("-fx-background-color: #1C1C1C; -fx-background-radius: 25px");
                legal_icon.setStyle("-fx-stroke: #FFF; -fx-stroke-width: 2");
            }
            case "support" -> {
                support_button.setStyle("-fx-background-color: #1C1C1C; -fx-background-radius: 25px");
                support_icon.setStyle("-fx-stroke: #FFF; -fx-stroke-width: 2");
            }
        }
    }

    public void SetTradingSettings(MouseEvent mouseEvent) {
        if(currentPane.equals("trading")) return;
        background_pane.getChildren().clear();
        setSettings("trading");

        trading_button.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "trading";

        setLastButton(lastPane);
        trading_icon.setStyle("-fx-stroke: #4C7EFF; -fx-stroke-width: 2");
    }

    public void SetIntegrationSettings(MouseEvent mouseEvent) {
        if(currentPane.equals("integration")) return;
        background_pane.getChildren().clear();
        setSettings("integration");

        integration_button.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "integration";

        setLastButton(lastPane);
        integration_icon.setStyle("-fx-stroke: #4C7EFF; -fx-stroke-width: 2");
    }

    public void SetLegalSettings(MouseEvent mouseEvent) {
        if(currentPane.equals("legal")) return;
        background_pane.getChildren().clear();
        setSettings("legal");

        legal_button.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "legal";

        setLastButton(lastPane);
        legal_icon.setStyle("-fx-stroke: #4C7EFF; -fx-stroke-width: 2");
    }

    public void SetSupport(MouseEvent mouseEvent) {
        if(currentPane.equals("support")) return;
        background_pane.getChildren().clear();
        setSettings("support");

        support_button.setStyle("-fx-background-color: #172854; -fx-background-radius: 25px");
        lastPane = currentPane;
        currentPane = "support";

        setLastButton(lastPane);
        support_icon.setStyle("-fx-stroke: #4C7EFF; -fx-stroke-width: 2");
    }

    private void setSettings(String name) {
        background_pane.getChildren().clear();
        background_pane.getChildren().add(switch (name) {
            case "general" -> GeneralSettings;
            case "trading" -> TradingSettings;
            case "integration" -> IntegrationSettings;
            case "legal" -> LegalSettings;
            case "support" -> Support;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        } );
    }
}
