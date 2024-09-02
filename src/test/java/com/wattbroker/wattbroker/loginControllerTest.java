package com.wattbroker.wattbroker;

import com.util.util;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(ApplicationExtension.class)
public class loginControllerTest {

    private loginController controller;
    private DB_query dbQueryMock;

    @Start
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();

        controller = loader.getController();
        dbQueryMock = mock(DB_query.class);

        controller.username = (TextField) scene.lookup("#username");
        controller.password = (PasswordField) scene.lookup("#password");
        controller.signInButton = (Button) scene.lookup("#signInButton");
    }

    @BeforeEach
    public void setUp() {
        // Mocking the DB_query class
        dbQueryMock = mock(DB_query.class);
    }

    @Test
    public void testSignInSuccessful(FxRobot robot) throws IOException {
        try (MockedStatic<util> utilMockedStatic = Mockito.mockStatic(util.class)) {
            when(dbQueryMock.checkCredentials("testUser", "hashedPassword")).thenReturn(true);
            controller.username.setText("testUser");
            controller.password.setText("testPassword");

            robot.clickOn("#signInButton");

            verify(dbQueryMock).checkCredentials("testUser", "hashedPassword");
            utilMockedStatic.verify(() -> util.infoMessage(Mockito.anyString(), Mockito.anyString()), Mockito.never());
        }
    }

    @Test
    public void testSignInFailed(FxRobot robot) {
        try (MockedStatic<util> utilMockedStatic = Mockito.mockStatic(util.class)) {
            when(dbQueryMock.checkCredentials("testUser", "hashedPassword")).thenReturn(false);
            controller.username.setText("testUser");
            controller.password.setText("testPassword");

            robot.clickOn("#signInButton");

            verify(dbQueryMock).checkCredentials("testUser", "hashedPassword");
            utilMockedStatic.verify(() -> util.infoMessage("The username or password provided was incorrect.", "Invalid credentials"));
        }
    }

    @Test
    public void testGotoCreateAccount(FxRobot robot) throws IOException {
        robot.clickOn("#createAccountButton");

        Scene scene = controller.root.getScene();
        assertEquals("CreateAccount.fxml", scene.getRoot().getUserData());
    }

    @Test
    public void testCreateAccount(FxRobot robot) {
        try (MockedStatic<util> utilMockedStatic = Mockito.mockStatic(util.class)) {
            controller.FirstName.setText("John");
            controller.LastName.setText("Doe");
            controller.Password.setText("password123");
            controller.Username.setText("johndoe");
            controller.CompanyCode.setText("123456");
            controller.EmployeeCode.setText("654321");

            robot.clickOn("#CreateAccountButton");

            verify(dbQueryMock).addUser("John", "Doe", "johndoe", "email", "hashedPassword", "123456", "654321");
            utilMockedStatic.verify(() -> util.infoMessage("Account created successfully!", "Success"));
        }
    }
}