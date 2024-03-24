package com.example.project;

import com.example.project.Location;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {
    private static final String API_KEY = "your_api_key_here";

    public WeatherData getCurrentWeather(Location location) throws IOException {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + API_KEY;
        return parseWeatherData(sendGetRequest(urlString));
    }

    public WeatherData getForecast(Location location) throws IOException {
        String urlString = "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + API_KEY;
        return parseForecastData(sendGetRequest(urlString));
    }

    public AirPollution getAirPollution(Location location) throws IOException {
        String urlString = "http://api.openweathermap.org/data/2.5/air_pollution?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + API_KEY;
        return parseAirPollutionData(sendGetRequest(urlString));
    }

    private HttpURLConnection sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();
        return connection;
    }

    private WeatherData parseWeatherData(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        JsonObject jsonObject =JsonParser.parseString(stringBuilder.toString()).getAsJsonObject();
        WeatherData weatherData = new WeatherData();
        weatherData.setLocationName(jsonObject.get("name").getAsString());
        weatherData.setTemperature(jsonObject.get("main").getAsJsonObject().get("temp").getAsDouble() - 273.15);
        weatherData.setFeelsLike(jsonObject.get("main").getAsJsonObject().get("feels_like").getAsDouble() - 273.15);
        weatherData.setMinTemperature(jsonObject.get("main").getAsJsonObject().get("temp_min").getAsDouble() - 273.15);
        weatherData.setMaxTemperature(jsonObject.get("main").getAsJsonObject().get("temp_max").getAsDouble() - 273.15);
        weatherData.setWeatherDescription(jsonObject.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString());
        weatherData.setSunrise(jsonObject.get("sys").getAsJsonObject().get("sunrise").getAsLong());
        weatherData.setSunset(jsonObject.get("sys").getAsJsonObject().get("sunset").getAsLong());
        return weatherData;
    }

    private WeatherData parseForecastData(HttpURLConnection connection) throws IOException {
        // Similar to parseWeatherData, but parse the forecast data instead
    }

    private AirPollution parseAirPollutionData(HttpURLConnection connection) throws IOException {
        // Similar to parseWeatherData, but parse the air pollution data instead
    }
}