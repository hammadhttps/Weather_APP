module com.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires com.google.gson;
    requires java.net.http;
    requires java.desktop;

    opens com.weatherapp to javafx.fxml;
    exports com.weatherapp;
}