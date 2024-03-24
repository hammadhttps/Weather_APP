package com.example.project;

import com.example.project.Location;
import com.example.project.WeatherAPI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WeatherApp {
    private static final String DB_URL = "jdbc:sqlite:weather.db";
    private static final int MAX_CACHE_AGE = 10 * 60 * 1000; // 10 minutes

    public static void main(String[] args) throws SQLException, IOException {
        WeatherAPI weatherAPI = new WeatherAPI();

        // Add locations
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Lahore", 31.5497, 74.3436));
        locations.add(new Location("New York", 40.7128, -74.0060));

        // Terminal UI
        System.out.println("Terminal UI:");
        for (Location location : locations) {
            WeatherData weatherData = getWeatherData(weatherAPI, location);
            System.out.println(weatherData);
        }

        // Desktop UI
        // TODO: Implement Desktop UI

        // Cache management
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS weather_data (location_name TEXT, temperature REAL, feels_like REAL, min_temperature REAL, max_temperature REAL, weather_description TEXT, sunrise INTEGER, sunset INTEGER, timestamp INTEGER)");

            for (Location location : locations) {
                WeatherData weatherData = getWeatherData(weatherAPI, location);
                storeWeatherData(connection, weatherData);
            }

            statement.execute("DELETE FROM weather_data WHERE timestamp < " + (Instant.now().toEpochMilli() - MAX_CACHE_AGE));

            ResultSet resultSet = statement.executeQuery("SELECT * FROM weather_data");
            while (resultSet.next()) {
                WeatherData weatherData = new WeatherData();
                weatherData.setLocationName(resultSet.getString("location_name"));
                weatherData.setTemperature(resultSet.getDouble("temperature"));
                weatherData.setFeelsLike(resultSet.getDouble("feels_like"));
                weatherData.setMinTemperature(resultSet.getDouble("min_temperature"));
                weatherData.setMaxTemperature(resultSet.getDouble("max_temperature"));
                weatherData.setWeatherDescription(resultSet.getString("weather_description"));
                weatherData.setSunrise(resultSet.getLong("sunrise"));
                weatherData.setSunset(resultSet.getLong("sunset"));
                weatherData.setTimestamp(resultSet.getLong("timestamp"));
                System.out.println(weatherData);
            }
        }
    }

    private static WeatherData getWeatherData(WeatherAPI weatherAPI, Location location) throws IOException {
        WeatherData weatherData = getCachedWeatherData(location);
        if (weatherData == null) {
            weatherData = weatherAPI.getCurrentWeather(location);
            storeWeatherData(weatherData);
        }
        return weatherData;
    }

    private static WeatherData getCachedWeatherData(Location location) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM weather_data WHERE location_name = ? ORDER BY timestamp DESC LIMIT 1");
            preparedStatement.setString(1, location.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                WeatherData weatherData = new WeatherData();
                weatherData.setLocationName(resultSet.getString("location_name"));
                weatherData.setTemperature(resultSet.getDouble("temperature"));
                weatherData.setFeelsLike(resultSet.getDouble("feels_like"));
                weatherData.setMinTemperature(resultSet.getDouble("min_temperature"));
                weatherData.setMaxTemperature(resultSet.getDouble("max_temperature"));
                weatherData.setWeatherDescription(resultSet.getString("weather_description"));
                weatherData.setSunrise(resultSet.getLong("sunrise"));
                weatherData.setSunset(resultSet.getLong("sunset"));
                weatherData.setTimestamp(resultSet.getLong("timestamp"));
                return weatherData;
            }
        }
        return null;
    }

    private static void storeWeatherData(WeatherData weatherData) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO weather_data (location_name, temperature, feels_like, min_temperature, max_temperature, weather_description, sunrise, sunset, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, weatherData.getLocationName());
            preparedStatement.setDouble(2, weatherData.getTemperature());
            preparedStatement.setDouble(3, weatherData.getFeelsLike());
            preparedStatement.setDouble(4, weatherData.getMinTemperature());
            preparedStatement.setDouble(5, weatherData.getMaxTemperature());
            preparedStatement.setString(6, weatherData.getWeatherDescription());
            preparedStatement.setLong(7, weatherData.getSunrise());
            preparedStatement.setLong(8, weatherData.getSunset());
            preparedStatement.setLong(9, weatherData.getTimestamp());
            preparedStatement.executeUpdate();
        }
    }

    private static void storeWeatherData(Connection connection, WeatherData weatherData) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO weather_data (location_name, temperature, feels_like, min_temperature, max_temperature, weather_description, sunrise, sunset, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, weatherData.getLocationName());
        preparedStatement.setDouble(2, weatherData.getTemperature());
        preparedStatement.setDouble(3, weatherData.getFeelsLike());
        preparedStatement.setDouble(4, weatherData.getMinTemperature());
        preparedStatement.setDouble(5, weatherData.getMaxTemperature());
        preparedStatement.setString(6, weatherData.getWeatherDescription());
        preparedStatement.setLong(7, weatherData.getSunrise());
        preparedStatement.setLong(8, weatherData.getSunset());
        preparedStatement.setLong(9, weatherData.getTimestamp());
        preparedStatement.executeUpdate();
    }
}