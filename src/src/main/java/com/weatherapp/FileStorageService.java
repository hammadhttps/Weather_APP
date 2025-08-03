package com.weatherapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {
    private static final String DATA_DIR = "weather_data";
    private static final String CACHE_FILE = "weather_cache.json";
    private static final String LOCATIONS_FILE = "saved_locations.json";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setLenient()
            .registerTypeAdapter(LocalDateTime.class, new com.google.gson.JsonSerializer<LocalDateTime>() {
                @Override
                public com.google.gson.JsonElement serialize(LocalDateTime src, java.lang.reflect.Type typeOfSrc,
                        com.google.gson.JsonSerializationContext context) {
                    return new com.google.gson.JsonPrimitive(src.toString());
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new com.google.gson.JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
                        com.google.gson.JsonDeserializationContext context) {
                    return LocalDateTime.parse(json.getAsString());
                }
            })
            .create();

    public FileStorageService() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void saveWeatherData(WeatherData weatherData) {
        try {
            String cityName = weatherData.getCityName().replaceAll("[^a-zA-Z0-9]", "_");
            String fileName = DATA_DIR + File.separator + cityName + "_weather.json";

            FileWriter writer = new FileWriter(fileName);
            gson.toJson(weatherData, writer);
            writer.close();

            // Also save to cache
            saveToCache(weatherData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WeatherData loadWeatherData(String cityName) {
        try {
            String fileName = DATA_DIR + File.separator + cityName.replaceAll("[^a-zA-Z0-9]", "_") + "_weather.json";
            File file = new File(fileName);

            if (file.exists()) {
                FileReader reader = new FileReader(file);
                WeatherData weatherData = gson.fromJson(reader, WeatherData.class);
                reader.close();
                return weatherData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveToCache(WeatherData weatherData) {
        try {
            List<WeatherData> cache = loadCache();
            cache.add(weatherData);

            // Keep only last 10 entries
            if (cache.size() > 10) {
                cache = cache.subList(cache.size() - 10, cache.size());
            }

            FileWriter writer = new FileWriter(DATA_DIR + File.separator + CACHE_FILE);
            gson.toJson(cache, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WeatherData> loadCache() {
        try {
            File file = new File(DATA_DIR + File.separator + CACHE_FILE);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                Type listType = new TypeToken<ArrayList<WeatherData>>() {
                }.getType();
                List<WeatherData> cache = gson.fromJson(reader, listType);
                reader.close();
                return cache != null ? cache : new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void saveLocation(String cityName, double lat, double lon) {
        try {
            List<Location> locations = loadLocations();

            // Check if location already exists
            boolean exists = locations.stream()
                    .anyMatch(loc -> loc.getCityName().equalsIgnoreCase(cityName));

            if (!exists) {
                locations.add(new Location(cityName, lat, lon));

                FileWriter writer = new FileWriter(DATA_DIR + File.separator + LOCATIONS_FILE);
                gson.toJson(locations, writer);
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Location> loadLocations() {
        try {
            File file = new File(DATA_DIR + File.separator + LOCATIONS_FILE);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                Type listType = new TypeToken<ArrayList<Location>>() {
                }.getType();
                List<Location> locations = gson.fromJson(reader, listType);
                reader.close();
                return locations != null ? locations : new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void deleteLocation(String cityName) {
        try {
            List<Location> locations = loadLocations();
            locations.removeIf(loc -> loc.getCityName().equalsIgnoreCase(cityName));

            FileWriter writer = new FileWriter(DATA_DIR + File.separator + LOCATIONS_FILE);
            gson.toJson(locations, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Location {
        private String cityName;
        private double latitude;
        private double longitude;

        public Location(String cityName, double latitude, double longitude) {
            this.cityName = cityName;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        // Getters and Setters
        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}