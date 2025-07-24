package com.weatherapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for fetching weather data from OpenWeatherMap API
 */
public class WeatherService {
    private static final String API_KEY = "a95ac20b44385c921f020bdcf01d1094";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5";
    private static final String GEO_URL = "https://api.openweathermap.org/geo/1.0";
    
    private final HttpClient httpClient;

    public WeatherService() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Get coordinates for a city name
     */
    public CompletableFuture<Location> getLocationByName(String cityName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String encodedCity = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
                String url = String.format("%s/direct?q=%s&limit=1&appid=%s", 
                    GEO_URL, encodedCity, API_KEY);
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JSONArray locations = new JSONArray(response.body());
                    if (locations.length() > 0) {
                        JSONObject location = locations.getJSONObject(0);
                        return new Location(
                            location.getString("name"),
                            location.optString("country", ""),
                            location.getDouble("lat"),
                            location.getDouble("lon")
                        );
                    }
                }
                throw new RuntimeException("Location not found: " + cityName);
            } catch (Exception e) {
                throw new RuntimeException("Error fetching location: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Get current weather data by coordinates
     */
    public CompletableFuture<WeatherData> getCurrentWeather(double lat, double lon) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = String.format("%s/weather?lat=%f&lon=%f&appid=%s&units=metric", 
                    BASE_URL, lat, lon, API_KEY);
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return parseCurrentWeather(response.body());
                }
                throw new RuntimeException("Failed to fetch weather data");
            } catch (Exception e) {
                throw new RuntimeException("Error fetching weather: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Get current weather data by city name
     */
    public CompletableFuture<WeatherData> getCurrentWeather(String cityName) {
        return getLocationByName(cityName)
            .thenCompose(location -> getCurrentWeather(location.getLat(), location.getLon())
                .thenApply(weather -> {
                    weather.setLocation(location.getName());
                    weather.setCountry(location.getCountry());
                    weather.setLatitude(location.getLat());
                    weather.setLongitude(location.getLon());
                    return weather;
                }));
    }

    /**
     * Get 5-day forecast
     */
    public CompletableFuture<List<WeatherData.ForecastData>> getForecast(double lat, double lon) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = String.format("%s/forecast?lat=%f&lon=%f&appid=%s&units=metric", 
                    BASE_URL, lat, lon, API_KEY);
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return parseForecast(response.body());
                }
                throw new RuntimeException("Failed to fetch forecast data");
            } catch (Exception e) {
                throw new RuntimeException("Error fetching forecast: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Get air pollution data
     */
    public CompletableFuture<Integer> getAirQuality(double lat, double lon) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = String.format("%s/air_pollution?lat=%f&lon=%f&appid=%s", 
                    BASE_URL, lat, lon, API_KEY);
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JSONObject json = new JSONObject(response.body());
                    JSONArray list = json.getJSONArray("list");
                    if (list.length() > 0) {
                        JSONObject main = list.getJSONObject(0).getJSONObject("main");
                        return main.getInt("aqi");
                    }
                }
                return 0; // Unknown
            } catch (Exception e) {
                return 0; // Unknown on error
            }
        });
    }

    /**
     * Get complete weather data including forecast and air quality
     */
    public CompletableFuture<WeatherData> getCompleteWeatherData(String cityName) {
        return getLocationByName(cityName)
            .thenCompose(location -> {
                CompletableFuture<WeatherData> weatherFuture = getCurrentWeather(
                    location.getLat(), location.getLon());
                CompletableFuture<List<WeatherData.ForecastData>> forecastFuture = getForecast(
                    location.getLat(), location.getLon());
                CompletableFuture<Integer> airQualityFuture = getAirQuality(
                    location.getLat(), location.getLon());

                return CompletableFuture.allOf(weatherFuture, forecastFuture, airQualityFuture)
                    .thenApply(v -> {
                        WeatherData weather = weatherFuture.join();
                        weather.setLocation(location.getName());
                        weather.setCountry(location.getCountry());
                        weather.setLatitude(location.getLat());
                        weather.setLongitude(location.getLon());
                        weather.setForecast(forecastFuture.join());
                        weather.setAirQualityIndex(airQualityFuture.join());
                        return weather;
                    });
            });
    }

    private WeatherData parseCurrentWeather(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        WeatherData weather = new WeatherData();

        // Basic info
        weather.setLocation(json.getString("name"));
        JSONObject sys = json.getJSONObject("sys");
        weather.setCountry(sys.optString("country", ""));

        // Coordinates
        JSONObject coord = json.getJSONObject("coord");
        weather.setLatitude(coord.getDouble("lat"));
        weather.setLongitude(coord.getDouble("lon"));

        // Temperature data
        JSONObject main = json.getJSONObject("main");
        weather.setTemperature(main.getDouble("temp"));
        weather.setFeelsLike(main.getDouble("feels_like"));
        weather.setMinTemp(main.getDouble("temp_min"));
        weather.setMaxTemp(main.getDouble("temp_max"));
        weather.setHumidity(main.getInt("humidity"));
        weather.setPressure(main.getDouble("pressure"));

        // Weather description
        JSONArray weatherArray = json.getJSONArray("weather");
        if (weatherArray.length() > 0) {
            JSONObject weatherObj = weatherArray.getJSONObject(0);
            weather.setDescription(weatherObj.getString("description"));
            weather.setIcon(weatherObj.getString("icon"));
        }

        // Wind
        if (json.has("wind")) {
            JSONObject wind = json.getJSONObject("wind");
            weather.setWindSpeed(wind.optDouble("speed", 0));
            weather.setWindDirection(wind.optInt("deg", 0));
        }

        // Visibility
        weather.setVisibility(json.optInt("visibility", 0));

        // Sunrise/Sunset
        if (sys.has("sunrise") && sys.has("sunset")) {
            weather.setSunrise(LocalDateTime.ofInstant(
                Instant.ofEpochSecond(sys.getLong("sunrise")), ZoneId.systemDefault()));
            weather.setSunset(LocalDateTime.ofInstant(
                Instant.ofEpochSecond(sys.getLong("sunset")), ZoneId.systemDefault()));
        }

        return weather;
    }

    private List<WeatherData.ForecastData> parseForecast(String jsonResponse) {
        List<WeatherData.ForecastData> forecast = new ArrayList<>();
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray list = json.getJSONArray("list");

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            WeatherData.ForecastData forecastData = new WeatherData.ForecastData();

            // Date/Time
            long dt = item.getLong("dt");
            forecastData.setDateTime(LocalDateTime.ofInstant(
                Instant.ofEpochSecond(dt), ZoneId.systemDefault()));

            // Temperature
            JSONObject main = item.getJSONObject("main");
            forecastData.setTemperature(main.getDouble("temp"));
            forecastData.setHumidity(main.getInt("humidity"));

            // Weather
            JSONArray weather = item.getJSONArray("weather");
            if (weather.length() > 0) {
                JSONObject weatherObj = weather.getJSONObject(0);
                forecastData.setDescription(weatherObj.getString("description"));
                forecastData.setIcon(weatherObj.getString("icon"));
            }

            // Wind
            if (item.has("wind")) {
                JSONObject wind = item.getJSONObject("wind");
                forecastData.setWindSpeed(wind.optDouble("speed", 0));
            }

            forecast.add(forecastData);
        }

        return forecast;
    }

    /**
     * Location helper class
     */
    public static class Location {
        private final String name;
        private final String country;
        private final double lat;
        private final double lon;

        public Location(String name, String country, double lat, double lon) {
            this.name = name;
            this.country = country;
            this.lat = lat;
            this.lon = lon;
        }

        public String getName() { return name; }
        public String getCountry() { return country; }
        public double getLat() { return lat; }
        public double getLon() { return lon; }
    }
}