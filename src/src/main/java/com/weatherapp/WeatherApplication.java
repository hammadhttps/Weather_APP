package com.weatherapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class WeatherApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/WeatherApp.fxml"));
        primaryStage.setTitle("Just Weather - Modern Weather App");
        Scene scene = new Scene(root);

        // Load CSS file
        try {
            String css = getClass().getResource("/weather-app.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Set application icon (if available)
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}