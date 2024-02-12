package tn.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Tuni'Art");
        primaryStage.setScene(new Scene(root, 800, 600)); // Set your preferred width and height
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
