package edu.icet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Starter extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Label label = new Label("Hello, MedSync!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Pharmacy Inventory System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
