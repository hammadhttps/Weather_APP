package com.weatherapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherAPIService {
    private static final String API_KEY = "a95ac20b44385c921f020bdcf01d1094";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public WeatherData getCurrentWeather(double lat, double lon) throws IOException, InterruptedException {
        String url = String.format("%s/weather?lat=%.6f&lon=%.6f&appid=%s&units=metric",
                BASE_URL, lat, lon, API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseCurrentWeather(response.body());
    }

    public List<WeatherData.HourlyForecast> getForecast(double lat, double lon)
            throws IOException, InterruptedException {
        String url = String.format("%s/forecast?lat=%.6f&lon=%.6f&appid=%s&units=metric",
                BASE_URL, lat, lon, API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseForecast(response.body());
    }

    public WeatherData.AirQuality getAirQuality(double lat, double lon) throws IOException, InterruptedException {
        String url = String.format("%s/air_pollution?lat=%.6f&lon=%.6f&appid=%s",
                BASE_URL, lat, lon, API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseAirQuality(response.body());
    }

    public double[] getCoordinates(String cityName) throws IOException, InterruptedException {
        String encodedCityName = java.net.URLEncoder.encode(cityName, "UTF-8");
        String url = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                encodedCityName, API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseCoordinates(response.body());
    }

    private WeatherData parseCurrentWeather(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        String cityName = jsonObject.get("name").getAsString();
        JsonObject main = jsonObject.getAsJsonObject("main");
        JsonObject weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();
        JsonObject wind = jsonObject.getAsJsonObject("wind");
        JsonObject sys = jsonObject.getAsJsonObject("sys");

        WeatherData weatherData = new WeatherData(
                cityName,
                main.get("temp").getAsDouble(),
                main.get("feels_like").getAsDouble(),
                main.get("temp_min").getAsDouble(),
                main.get("temp_max").getAsDouble(),
                main.get("humidity").getAsInt(),
                main.get("pressure").getAsInt(),
                wind.has("speed") ? wind.get("speed").getAsDouble() : 0.0,
                wind.has("deg") ? wind.get("deg").getAsString() : "N/A",
                weather.get("description").getAsString(),
                weather.get("icon").getAsString());

        // Parse sunrise/sunset
        String sunrise = formatTime(sys.get("sunrise").getAsLong());
        String sunset = formatTime(sys.get("sunset").getAsLong());
        weatherData.setSunriseSunset(new WeatherData.SunriseSunset(sunrise, sunset));

        return weatherData;
    }

    private List<WeatherData.HourlyForecast> parseForecast(String json) {
        List<WeatherData.HourlyForecast> forecasts = new ArrayList<>();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray list = jsonObject.getAsJsonArray("list");

        for (int i = 0; i < Math.min(list.size(), 24); i++) {
            JsonObject item = list.get(i).getAsJsonObject();
            JsonObject main = item.getAsJsonObject("main");
            JsonObject weather = item.getAsJsonArray("weather").get(0).getAsJsonObject();

            LocalDateTime time = LocalDateTime.parse(
                    item.get("dt_txt").getAsString(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            WeatherData.HourlyForecast forecast = new WeatherData.HourlyForecast(
                    time,
                    main.get("temp").getAsDouble(),
                    weather.get("description").getAsString(),
                    weather.get("icon").getAsString(),
                    item.has("pop") ? (int) (item.get("pop").getAsDouble() * 100) : 0);

            forecasts.add(forecast);
        }

        return forecasts;
    }

    private WeatherData.AirQuality parseAirQuality(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject list = jsonObject.getAsJsonArray("list").get(0).getAsJsonObject();
        JsonObject components = list.getAsJsonObject("components");
        JsonObject main = list.getAsJsonObject("main");

        return new WeatherData.AirQuality(
                main.get("aqi").getAsInt(),
                components.get("co").getAsDouble(),
                components.get("no2").getAsDouble(),
                components.get("o3").getAsDouble(),
                components.get("pm10").getAsDouble(),
                components.get("pm2_5").getAsDouble(),
                components.get("so2").getAsDouble());
    }

    private double[] parseCoordinates(String json) {
        try {
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
            if (jsonArray.size() > 0) {
                JsonObject location = jsonArray.get(0).getAsJsonObject();
                // Coordinates are directly in the root object, not nested under "coord"
                if (location.has("lat") && location.has("lon")) {
                    return new double[] {
                            location.get("lat").getAsDouble(),
                            location.get("lon").getAsDouble()
                    };
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing coordinates: " + e.getMessage());
        }
        return new double[] { 0.0, 0.0 };
    }

    private String formatTime(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, java.time.ZoneOffset.UTC);
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}