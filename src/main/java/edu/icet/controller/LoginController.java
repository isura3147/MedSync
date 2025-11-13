package edu.icet.controller;

import edu.icet.model.User;
import edu.icet.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password are required.");
            return;
        }

        Optional<User> userOpt = userService.authenticateUser(username, password);

        if (userOpt.isPresent()) {
            errorLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            errorLabel.setText("Login Successful! Welcome, " + userOpt.get().getUsername());

            // sceneNavigator.showDashboard();

        } else {
            // Login Failed
            errorLabel.setTextFill(javafx.scene.paint.Color.RED);
            errorLabel.setText("Invalid username or password.");
        }
    }
}