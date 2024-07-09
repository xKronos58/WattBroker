package com.wattbroker.wattbroker;

import com.util.util;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class loginController {
    public Button signInButton;
    public PasswordField password;
    public PasswordField Password;
    public TextField username;
    public AnchorPane signInWithGoogleButton;
    public AnchorPane signInWithAppleButton;
    public AnchorPane createAccountButton;
    public AnchorPane root;
    public TextField Username;
    public TextField CompanyCode;
    public TextField EmployeeCode;
    public Button CreateAccountButton;
    public TextField FirstName;
    public TextField LastName;

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

    public void CreateAccount(MouseEvent mouseEvent) {
        try {
            Login.primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("CreateAccount.fxml")).load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void createAccount(MouseEvent mouseEvent) {
        String firstName = FirstName.getText(), lastName = LastName.getText(),
                password = Password.getText(), username = Username.getText(),
                companyCode = CompanyCode.getText(), employeeCode = EmployeeCode.getText();

        if(firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || username.isEmpty() || companyCode.isEmpty() || employeeCode.isEmpty()) {
            util.errorMessage("Please ensure you have filled out all of the fields", "Missing information");
            return;
        }

        if(employeeCode.length() != 6 || companyCode.length() != 6){
            util.errorMessage("Please ensure that you have provided valid company codes", "Missing information");
            return;
        }

        if(password.length() < 8)
            util.errorMessage("Your password must be greater than 8 digits", "Invalid Password");

    }
}