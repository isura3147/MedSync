package edu.icet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Starter extends Application {

    public static AnnotationConfigApplicationContext springContext;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));

        fxmlLoader.setControllerFactory(springContext::getBean);

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("MedSync - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}