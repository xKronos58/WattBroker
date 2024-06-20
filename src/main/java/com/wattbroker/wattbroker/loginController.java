package com.wattbroker.wattbroker;

import com.util.util;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class loginController {
    public Button signInButton;
    public PasswordField password;
    public TextField username;

    public void signIn(ActionEvent actionEvent) throws IOException {
        String passwordHash = convertToHash(password.getText());
        String usernameHash = convertToHash(username.getText());

        //TODO implement application server
//        if(Data.APPLICATION_SERVER_IP.query(usernameHash, passwordHash)) {
//            // Open the main window
//        } else {
//            // Show error message
//        }

        //HARD CODED LOGIN FOR TESTING
        if(username.getText().equals("admin") && password.getText().equals("admin")) {
            Login.primaryStage.close();
            Main main = new Main();
            main.start(new Stage());
        } else {
            util.infoMessage("Login failed", "Invalid credentials");
        }
    }

    private String convertToHash(String text) {

        return text;
    }
}
