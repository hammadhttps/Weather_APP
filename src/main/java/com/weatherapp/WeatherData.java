package com.weatherapp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Weather data model class to hold all weather information
 */
public class WeatherData {
    private String location;
    private String country;
    private double latitude;
    private double longitude;
    private double temperature;
    private double feelsLike;
    private double minTemp;
    private double maxTemp;
    private int humidity;
    private double pressure;
    private double windSpeed;
    private int windDirection;
    private String description;
    private String icon;
    private int visibility;
    private double uvIndex;
    private LocalDateTime sunrise;
    private LocalDateTime sunset;
    private LocalDateTime timestamp;
    private int airQualityIndex;
    private List<ForecastData> forecast;

    // Constructors
    public WeatherData() {
        this.timestamp = LocalDateTime.now();
    }

    public WeatherData(String location, String country) {
        this();
        this.location = location;
        this.country = country;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }

    public double getMinTemp() { return minTemp; }
    public void setMinTemp(double minTemp) { this.minTemp = minTemp; }

    public double getMaxTemp() { return maxTemp; }
    public void setMaxTemp(double maxTemp) { this.maxTemp = maxTemp; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public int getWindDirection() { return windDirection; }
    public void setWindDirection(int windDirection) { this.windDirection = windDirection; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public int getVisibility() { return visibility; }
    public void setVisibility(int visibility) { this.visibility = visibility; }

    public double getUvIndex() { return uvIndex; }
    public void setUvIndex(double uvIndex) { this.uvIndex = uvIndex; }

    public LocalDateTime getSunrise() { return sunrise; }
    public void setSunrise(LocalDateTime sunrise) { this.sunrise = sunrise; }

    public LocalDateTime getSunset() { return sunset; }
    public void setSunset(LocalDateTime sunset) { this.sunset = sunset; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getAirQualityIndex() { return airQualityIndex; }
    public void setAirQualityIndex(int airQualityIndex) { this.airQualityIndex = airQualityIndex; }

    public List<ForecastData> getForecast() { return forecast; }
    public void setForecast(List<ForecastData> forecast) { this.forecast = forecast; }

    // Helper methods
    public String getFullLocation() {
        return location + (country != null && !country.isEmpty() ? ", " + country : "");
    }

    public String getTemperatureString() {
        return String.format("%.0f°C", temperature);
    }

    public String getFeelsLikeString() {
        return String.format("Feels like %.0f°C", feelsLike);
    }

    public String getWindString() {
        return String.format("%.1f m/s", windSpeed);
    }

    public String getAirQualityString() {
        switch (airQualityIndex) {
            case 1: return "Good";
            case 2: return "Fair";
            case 3: return "Moderate";
            case 4: return "Poor";
            case 5: return "Very Poor";
            default: return "Unknown";
        }
    }

    /**
     * Inner class for forecast data
     */
    public static class ForecastData {
        private LocalDateTime dateTime;
        private double temperature;
        private String description;
        private String icon;
        private int humidity;
        private double windSpeed;

        public ForecastData() {}

        public ForecastData(LocalDateTime dateTime, double temperature, String description, String icon) {
            this.dateTime = dateTime;
            this.temperature = temperature;
            this.description = description;
            this.icon = icon;
        }

        // Getters and Setters
        public LocalDateTime getDateTime() { return dateTime; }
        public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }

        public int getHumidity() { return humidity; }
        public void setHumidity(int humidity) { this.humidity = humidity; }

        public double getWindSpeed() { return windSpeed; }
        public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

        public String getTemperatureString() {
            return String.format("%.0f°C", temperature);
        }
    }
}