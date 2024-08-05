package com.wattbroker.wattbroker;

import com.util.util;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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
        DB_query dbq = new DB_query();
        if(dbq.checkCredentials(username.getText(), passwordHash)) {
            WattBroker.primaryStage.close();
            Main main = new Main(new User());
            main.start(new Stage());
        } else {
            util.infoMessage("The username or password provided was incorrect.", "Invalid credentials");
        }
    }

    private String convertToHash(String text) {
        return Hash.hashText(new String[]{text});
    }

    public void gotoCreateAccount(MouseEvent mouseEvent) {
        try {
            WattBroker.primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("CreateAccount.fxml")).load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void createAccount(ActionEvent mouseEvent) {
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

        if(password.length() < 8) {
            util.errorMessage("Your password must be greater than 8 digits", "Invalid Password");
            return;
        }

        // For future french developers :
        // Did you know title in French is spelled as "Titre" and pronounced as "tit"? - Ned P

        // Create a new database query.
        DB_query dbq = new DB_query();

        // Adds the user to the database *
        // returns false if there was an error and shows an error message from the util
        if(dbq.addUser(firstName, lastName, username, "email" /*Todo: implement*/, convertToHash(password), companyCode, employeeCode)) {
            util.infoMessage("Account created successfully!", "Success");
            try {
                // If successful shows the login page
                WattBroker.primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("Login.fxml")).load()));
            } catch (IOException e) {
                // If the FXML file cannot be found
                throw new RuntimeException(e);
            }
        } else {
            // Else shows an error message
            util.errorMessage("There was a problem", "ERROR");
        }

    }

    public void closeCreateAccountScreen(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ESCAPE) {
            try {
                WattBroker.primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("Login.fxml")).load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void signInKey(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            signIn(new ActionEvent());
        }
    }

    public void ForgotPassword(MouseEvent mouseEvent) {
        try {
            WattBroker.primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("ForgotPassword.fxml")).load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}