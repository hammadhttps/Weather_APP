package com.weatherapp;

import java.time.LocalDateTime;
import java.util.List;

public class WeatherData {
    private String cityName;
    private double temperature;
    private double feelsLike;
    private double minTemp;
    private double maxTemp;
    private int humidity;
    private int pressure;
    private double windSpeed;
    private String windDirection;
    private String description;
    private String icon;
    private LocalDateTime timestamp;
    private List<HourlyForecast> hourlyForecast;
    private AirQuality airQuality;
    private SunriseSunset sunriseSunset;
    private int uvIndex;
    private int visibility;
    private int chanceOfRain;

    public WeatherData() {
    }

    public WeatherData(String cityName, double temperature, double feelsLike, double minTemp,
            double maxTemp, int humidity, int pressure, double windSpeed,
            String windDirection, String description, String icon) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.description = description;
        this.icon = icon;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<HourlyForecast> getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(List<HourlyForecast> hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    public SunriseSunset getSunriseSunset() {
        return sunriseSunset;
    }

    public void setSunriseSunset(SunriseSunset sunriseSunset) {
        this.sunriseSunset = sunriseSunset;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getChanceOfRain() {
        return chanceOfRain;
    }

    public void setChanceOfRain(int chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
    }

    public static class HourlyForecast {
        private LocalDateTime time;
        private double temperature;
        private String description;
        private String icon;
        private int chanceOfRain;

        public HourlyForecast(LocalDateTime time, double temperature, String description, String icon,
                int chanceOfRain) {
            this.time = time;
            this.temperature = temperature;
            this.description = description;
            this.icon = icon;
            this.chanceOfRain = chanceOfRain;
        }

        // Getters and Setters
        public LocalDateTime getTime() {
            return time;
        }

        public void setTime(LocalDateTime time) {
            this.time = time;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getChanceOfRain() {
            return chanceOfRain;
        }

        public void setChanceOfRain(int chanceOfRain) {
            this.chanceOfRain = chanceOfRain;
        }
    }

    public static class AirQuality {
        private int aqi;
        private double co;
        private double no2;
        private double o3;
        private double pm10;
        private double pm25;
        private double so2;

        public AirQuality(int aqi, double co, double no2, double o3, double pm10, double pm25, double so2) {
            this.aqi = aqi;
            this.co = co;
            this.no2 = no2;
            this.o3 = o3;
            this.pm10 = pm10;
            this.pm25 = pm25;
            this.so2 = so2;
        }

        // Getters and Setters
        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public double getCo() {
            return co;
        }

        public void setCo(double co) {
            this.co = co;
        }

        public double getNo2() {
            return no2;
        }

        public void setNo2(double no2) {
            this.no2 = no2;
        }

        public double getO3() {
            return o3;
        }

        public void setO3(double o3) {
            this.o3 = o3;
        }

        public double getPm10() {
            return pm10;
        }

        public void setPm10(double pm10) {
            this.pm10 = pm10;
        }

        public double getPm25() {
            return pm25;
        }

        public void setPm25(double pm25) {
            this.pm25 = pm25;
        }

        public double getSo2() {
            return so2;
        }

        public void setSo2(double so2) {
            this.so2 = so2;
        }
    }

    public static class SunriseSunset {
        private String sunrise;
        private String sunset;

        public SunriseSunset(String sunrise, String sunset) {
            this.sunrise = sunrise;
            this.sunset = sunset;
        }

        // Getters and Setters
        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }
    }
}