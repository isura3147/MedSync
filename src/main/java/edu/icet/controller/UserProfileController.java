package edu.icet.controller;

import edu.icet.model.User;
import edu.icet.service.SessionService;
import edu.icet.service.UserService;
import edu.icet.util.FileStorageService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class UserProfileController {

    @FXML
    private ImageView profileImageView;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button uploadButton;
    @FXML
    private Label statusLabel;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = sessionService.getLoggedInUser();
        usernameLabel.setText("Username: " + currentUser.getUsername());

        String imagePath = currentUser.getProfilePicUrl();
        if (imagePath != null && !imagePath.isEmpty()) {
            // Load image from local file path
            // We must add "file:" to the path for JavaFX to load it
            profileImageView.setImage(new Image("file:" + imagePath));
        } else {
            // (Handle default image)
        }
    }

    @FXML
    private void onUploadClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                statusLabel.setText("Uploading...");
                // Create a unique key name
                String keyName = "profile-pic-" + currentUser.getId() + "-" + selectedFile.getName();

                // Upload to local disk
                String savedPath = fileStorageService.saveFile(selectedFile, keyName);

                // Update the user object
                currentUser.setProfilePicUrl(savedPath);

                // Save the new path to the database
                userService.updateUser(currentUser);

                // Update the UI
                profileImageView.setImage(new Image("file:" + savedPath));
                statusLabel.setText("Upload successful!");

            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setTextFill(javafx.scene.paint.Color.RED);
                statusLabel.setText("Upload failed: " + e.getMessage());
            }
        }
    }
}