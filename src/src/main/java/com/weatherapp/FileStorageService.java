package com.weatherapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileStorageService {
    private static final String DATA_DIR = "weather_data";
    private static final String CACHE_FILE = "weather_cache.json";
    private static final String LOCATIONS_FILE = "saved_locations.json";
    private static final String PREFERENCES_FILE = "user_preferences.json";
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
    
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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
        lock.writeLock().lock();
        try (FileWriter writer = new FileWriter(DATA_DIR + File.separator + 
                weatherData.getCityName().replaceAll("[^a-zA-Z0-9]", "_") + "_weather.json")) {
            String cityName = weatherData.getCityName().replaceAll("[^a-zA-Z0-9]", "_");
            gson.toJson(weatherData, writer);

            // Also save to cache
            saveToCache(weatherData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public WeatherData loadWeatherData(String cityName) {
        lock.readLock().lock();
        try (FileReader reader = new FileReader(DATA_DIR + File.separator + 
                cityName.replaceAll("[^a-zA-Z0-9]", "_") + "_weather.json")) {
            String fileName = DATA_DIR + File.separator + cityName.replaceAll("[^a-zA-Z0-9]", "_") + "_weather.json";
            File file = new File(fileName);

            if (file.exists()) {
                WeatherData weatherData = gson.fromJson(reader, WeatherData.class);
                return weatherData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    private void saveToCache(WeatherData weatherData) {
        try (FileWriter writer = new FileWriter(DATA_DIR + File.separator + CACHE_FILE)) {
            List<WeatherData> cache = loadCache();
            
            // Remove existing entry for the same city
            cache.removeIf(data -> data.getCityName().equalsIgnoreCase(weatherData.getCityName()));
            cache.add(weatherData);

            // Keep only last 10 entries
            if (cache.size() > 10) {
                cache = cache.subList(cache.size() - 10, cache.size());
            }

            gson.toJson(cache, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WeatherData> loadCache() {
        lock.readLock().lock();
        try (FileReader reader = new FileReader(DATA_DIR + File.separator + CACHE_FILE)) {
            File file = new File(DATA_DIR + File.separator + CACHE_FILE);
            if (file.exists()) {
                Type listType = new TypeToken<ArrayList<WeatherData>>() {
                }.getType();
                List<WeatherData> cache = gson.fromJson(reader, listType);
                return cache != null ? cache : new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        return new ArrayList<>();
    }

    public void saveLocation(String cityName, double lat, double lon) {
        lock.writeLock().lock();
        try (FileWriter writer = new FileWriter(DATA_DIR + File.separator + LOCATIONS_FILE)) {
            List<Location> locations = loadLocations();

            // Check if location already exists
            boolean exists = locations.stream()
                    .anyMatch(loc -> loc.getCityName().equalsIgnoreCase(cityName));

            if (!exists) {
                locations.add(new Location(cityName, lat, lon));
                
                // Keep only last 20 locations
                if (locations.size() > 20) {
                    locations = locations.subList(locations.size() - 20, locations.size());
                }

                gson.toJson(locations, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Location> loadLocations() {
        lock.readLock().lock();
        try (FileReader reader = new FileReader(DATA_DIR + File.separator + LOCATIONS_FILE)) {
            File file = new File(DATA_DIR + File.separator + LOCATIONS_FILE);
            if (file.exists()) {
                Type listType = new TypeToken<ArrayList<Location>>() {
                }.getType();
                List<Location> locations = gson.fromJson(reader, listType);
                return locations != null ? locations : new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        return new ArrayList<>();
    }

    public void deleteLocation(String cityName) {
        lock.writeLock().lock();
        try (FileWriter writer = new FileWriter(DATA_DIR + File.separator + LOCATIONS_FILE)) {
            List<Location> locations = loadLocations();
            locations.removeIf(loc -> loc.getCityName().equalsIgnoreCase(cityName));

            gson.toJson(locations, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void saveUserPreferences(UserPreferences preferences) {
        lock.writeLock().lock();
        try (FileWriter writer = new FileWriter(DATA_DIR + File.separator + PREFERENCES_FILE)) {
            gson.toJson(preferences, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public UserPreferences loadUserPreferences() {
        lock.readLock().lock();
        try (FileReader reader = new FileReader(DATA_DIR + File.separator + PREFERENCES_FILE)) {
            File file = new File(DATA_DIR + File.separator + PREFERENCES_FILE);
            if (file.exists()) {
                return gson.fromJson(reader, UserPreferences.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        return new UserPreferences(); // Return default preferences
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
    
    public static class UserPreferences {
        private boolean darkMode = false;
        private boolean celsius = true;
        private String defaultCity = "Madrid";
        private boolean autoRefresh = true;
        private int refreshInterval = 300; // seconds
        
        // Getters and setters
        public boolean isDarkMode() { return darkMode; }
        public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
        
        public boolean isCelsius() { return celsius; }
        public void setCelsius(boolean celsius) { this.celsius = celsius; }
        
        public String getDefaultCity() { return defaultCity; }
        public void setDefaultCity(String defaultCity) { this.defaultCity = defaultCity; }
        
        public boolean isAutoRefresh() { return autoRefresh; }
        public void setAutoRefresh(boolean autoRefresh) { this.autoRefresh = autoRefresh; }
        
        public int getRefreshInterval() { return refreshInterval; }
        public void setRefreshInterval(int refreshInterval) { this.refreshInterval = refreshInterval; }
    }
}