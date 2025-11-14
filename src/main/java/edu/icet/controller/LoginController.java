package edu.icet.controller;

import edu.icet.model.User;
import edu.icet.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @Autowired
    private UserService userService;

    @Autowired
    private AnnotationConfigApplicationContext springContext;

    public void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password are required.");
            return;
        }

        Optional<User> userOpt = userService.authenticateUser(username, password);

        if (userOpt.isPresent()) {
            // Login Successful
            showMainScreen();
        } else {
            // Login Failed
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);
            errorLabel.setText("Invalid username or password.");
        }
    }

    private void showMainScreen() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));

            //  Set the controller factory to use Spring
            fxmlLoader.setControllerFactory(springContext::getBean);

            Parent root = fxmlLoader.load();

            // Create a new scene and set it on the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("MedSync - Dashboard");
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to dashboard.");
        }
    }
}