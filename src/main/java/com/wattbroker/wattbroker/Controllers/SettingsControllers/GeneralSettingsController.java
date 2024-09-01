package com.wattbroker.wattbroker.Controllers.SettingsControllers;

import com.wattbroker.wattbroker.DB_query;
import com.wattbroker.wattbroker.Main;
import com.wattbroker.wattbroker.WattBroker;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
import java.util.Objects;

/**
 * Controls the general settings page reading and mapping all functions necessary. Contains EDIT menus and data setting for settings states*/
public class GeneralSettingsController {
    // FXML based elements
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

    /**
     * Runs initialization code to set up screen.
     * * clear scrollbars, get user info, sets settings
     * @see settingsState Set settings & reading
     * */
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

    /**
     * Creates a new edit window to modify the username of the account.
     * @see EDIT Gui
     * @param ignoredMouseEvent * Ignored */
    public void editUser(MouseEvent ignoredMouseEvent) {
        new EDIT(EditType.USERNAME, this);
    }

    /**
     * Refreshes label content after a change. Called from the EDIT menu on closing regardless if information is modified
     * Creates a new DB query to get updated user information rather than from the gui to prevent discrepancies.
     * @see EDIT caller
     * @see DB_query Database querier */
    public void updateLabels() {
        username_field.setText(new DB_query().getUsername(Main.userId.getId()));
        email_field.setText(new DB_query().getEmail(Main.userId.getId()));
    }

    /**
     * Creates a new edit window to modify the email of the account.
     * @see EDIT Gui
     * @param ignoredMouseEvent * Ignored */
    public void editEmail(MouseEvent ignoredMouseEvent) {
        new EDIT(EditType.EMAIL, this);
    }

    public void change_email_notifications(MouseEvent ignoredMouseEvent) {

    }

    public void change_settings_notification(MouseEvent ignoredMouseEvent) {

    }

    public void change_push_notifications(MouseEvent ignoredMouseEvent) {

    }

    public void change_high_contrast(MouseEvent ignoredMouseEvent) {

    }

    /**
     * On logout button click,
     * destroys the current scene
     * and navigates back to the login screen,*Does not need to clean user data from cache
     * as once logged back in will be overwritten.
     * @param ignoredMouseEvent * Ignored
     * @throws IOException thrown if login.fxml is not found in the project resources directory
     * @bug <code>001A</code> Opens a second window
     * @bug-description Opens a second window as the <code>login()</code> method does not close
     * <code>Main.primaryStage</code> and creates a new instance of that.
     * This is under construction and has issues with static states and duplicate instances of objects. */
    public void Logout(MouseEvent ignoredMouseEvent) throws IOException {
        Main.primaryStage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Login.fxml")))));
    }

    /**
     * Class built for modular editing menus, e.g. edit username.
     * @implNote When adding a new type of edit menu create the type in the enum and then add a case in the set.setOnAction(e -> [...]) to allow for different db queries and functions based on edit type
     * @see DB_query Query structure
     * @see EditType
     *
     * */
    private static class EDIT extends Application {

        /**
         * GUI method to launch the edit menu
         * @see EDIT
         * @param primaryStage when calling parse in a new stage as all content will be overridden and will popup in a new window
         */
        @Override
        public void start(Stage primaryStage) {
            primaryStage.show();
            primaryStage.setTitle("EDIT");
        }

        /**
         * Constructor for the EDIT class, builds and starts the GUI.
         * @param type type of edit menu to be displayed based off the EditType class * view impNote for more
         * @param g passed in to allow the super class to refresh labels on closing of menu to allow the root GUI to be in sync with all changes * only used for information passing
         * @implNote When adding a new type of edit menu create the type in the enum and then add a case in the set.setOnAction(e -> [...]) to allow for different db queries and functions based on edit type
         * @see EditType EditType type of GUI-menus
         * @see DB_query DB query structure

         */
        public EDIT(EditType type, GeneralSettingsController g) {

            // root stage
            Stage stage = new Stage();

            // Define elements used in GUI
            AnchorPane root = new AnchorPane();
            Text info = new Text("Enter new " + type.name().toLowerCase());
            TextField content = new TextField();
            Button set = new Button("Update");
            Button cancel = new Button("Cancel");

            // close the stage if the close button is pressed.
            cancel.setOnAction(e -> stage.close());

            // Check the type of menu is shown and parse information from the
            // text box to the relevant data base query based of the current
            // user id which is stored from the initial login into the app.
            set.setOnAction(e -> {
                switch (type) {
                    // if the gui is username based it will update the username in the database.
                    case USERNAME:
                        DB_query dbq = new DB_query();
                        dbq.setUsername(Main.userId.getId(), content.getText());
                        break;
                    // if the gui is email based it will update the email in the database.
                    case EMAIL:
                        DB_query dbq1 = new DB_query();
                        dbq1.setEmail(Main.userId.getId(), content.getText());
                        break;
                }
                stage.close();
            });

            // Check if enter or escape is pressed for keyboard shortcuts for user.
            root.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    set.fire();
                } else if (e.getCode() == KeyCode.ESCAPE) {
                    cancel.fire();
                }
            });

            // Set element colours
            root.setStyle("-fx-background-color: #171717;");
            content.setStyle("-fx-background-color: #2B2B2B; -fx-text-fill: #FFF;");
            set.setStyle("-fx-background-color: #4C7EFF; -fx-text-fill: #FFF;");
            cancel.setStyle("-fx-background-color: #4C7EFF; -fx-text-fill: #FFF;");
            info.setFont(new Font("Inter Light", 16));
            info.setFill(javafx.scene.paint.Color.WHITE);

            // Set padding for elements
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

            // Create the scene and add all the elements
            root.getChildren().addAll(info, content, set, cancel);
            stage.setScene(new Scene(root));

            // Map the update method to the closing action
            stage.setOnHiding(e -> g.updateLabels());

            // Launch the stage
            start(stage);
        }
    }

}


