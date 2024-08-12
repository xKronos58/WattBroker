package com.wattbroker.wattbroker.Controllers.SettingsControllers;

import com.wattbroker.wattbroker.DB_query;
import com.wattbroker.wattbroker.Main;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GeneralSettingsController {
    public ScrollPane rootScroll;
    public TextField Search_Bar;
    public Text username_field;
    public Text email_field;
    public Text edit_user;
    public Text email_notification_state;
    public Text email_notification_button;
    public Text sms_notification_state;
    public Text sms_notification_button;
    public Text push_notification_state;
    public Text push_notification_button;
    public Text notification_preferences_button;
    public SVGPath language_expand_button;
    public Text language_text;
    public Text time_zone_state;
    public SVGPath time_zone_expand_button;
    public Text region_text;
    public SVGPath region_expand_button;
    public Text theme_state;
    public SVGPath theme_expand_button;
    public Text high_contrast_state;
    public Text high_contrast_button;

    @FXML
    public void initialize() {
        rootScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        DB_query dbq = new DB_query();
        username_field.setText(dbq.getUsername(Main.userId.getId()));
        email_field.setText(dbq.getEmail(Main.userId.getId()));

        settingsState settings = new settingsState();

        email_notification_state.setText(settings.email_notifications ? "ON" : "OFF");
        email_notification_button.setText(settings.email_notifications ? "TURN OFF" : "TURN ON");
        sms_notification_state.setText(settings.sms_notifications ? "ON" : "OFF");
        sms_notification_button.setText(settings.sms_notifications ? "TURN OFF" : "TURN ON");
        push_notification_state.setText(settings.push_notifications ? "ON" : "OFF");
        push_notification_button.setText(settings.push_notifications ? "TURN OFF" : "TURN ON");
        language_text.setText(settings.language.toString());
        time_zone_state.setText(settings.time_zone);
        region_text.setText(settings.regional_format);
        theme_state.setText(settings.theme.toString());
        high_contrast_state.setText(settings.high_contrast ? "ON" : "OFF");
        high_contrast_button.setText(settings.high_contrast ? "TURN OFF" : "TURN ON");
    }

    public void editUser(MouseEvent mouseEvent) {
        new EDIT(EditType.USERNAME, this);
    }

    public void updateLabels() {
        username_field.setText(new DB_query().getUsername(Main.userId.getId()));
        email_field.setText(new DB_query().getEmail(Main.userId.getId()));
    }

    public void editEmail(MouseEvent mouseEvent) {
        new EDIT(EditType.EMAIL, this);
    }

    public void change_email_notifications(MouseEvent mouseEvent) {

    }

    public void change_settings_notification(MouseEvent mouseEvent) {

    }

    public void change_push_notifications(MouseEvent mouseEvent) {

    }

    public void change_high_contrast(MouseEvent mouseEvent) {

    }

    private static class EDIT extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.show();
            primaryStage.setTitle("EDIT");
        }

        public EDIT(EditType type, GeneralSettingsController g) {

            Stage stage = new Stage();

            AnchorPane root = new AnchorPane();
            Text info = new Text("Enter new " + type.name().toLowerCase());
            TextField content = new TextField();
            Button set = new Button("Update");
            Button cancel = new Button("Cancel");

            cancel.setOnAction(e -> stage.close());

            set.setOnAction(e -> {
                switch (type) {
                    case USERNAME:
                        DB_query dbq = new DB_query();
                        dbq.setUsername(Main.userId.getId(), content.getText());
                        break;
                    case EMAIL:
                        DB_query dbq1 = new DB_query();
                        dbq1.setEmail(Main.userId.getId(), content.getText());
                        break;
                }
                stage.close();
            });

            root.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    set.fire();
                } else if (e.getCode() == KeyCode.ESCAPE) {
                    cancel.fire();
                }
            });

            root.setStyle("-fx-background-color: #171717;");
            content.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #FFF;");
            set.setStyle("-fx-background-color: #4C7EFF; -fx-text-fill: #FFF;");
            cancel.setStyle("-fx-background-color: #4C7EFF; -fx-text-fill: #FFF;");
            info.setFont(new Font("Inter Light", 16));
            info.setFill(javafx.scene.paint.Color.WHITE);

            AnchorPane.setTopAnchor(info, 5.0);
            AnchorPane.setLeftAnchor(info, 10.0);
            AnchorPane.setTopAnchor(content, 40.0);
            AnchorPane.setLeftAnchor(content, 10.0);
            AnchorPane.setRightAnchor(content, 10.0);
            AnchorPane.setTopAnchor(set, 75.0);
            AnchorPane.setBottomAnchor(set, 10.0);
            AnchorPane.setLeftAnchor(set, 10.0);
            AnchorPane.setTopAnchor(cancel, 75.0);
            AnchorPane.setRightAnchor(cancel, 10.0);

            root.getChildren().addAll(info, content, set, cancel);
            stage.setScene(new Scene(root));

            stage.setOnHiding(e -> g.updateLabels());

            try {
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


