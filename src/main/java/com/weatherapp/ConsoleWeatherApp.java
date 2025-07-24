package com.weatherapp;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Console version of the Weather App for testing and demonstration
 */
public class ConsoleWeatherApp {
    private static final WeatherService weatherService = new WeatherService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printWelcome();
        
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    getCurrentWeather();
                    break;
                case "2":
                    getForecast();
                    break;
                case "3":
                    getCompleteWeatherData();
                    break;
                case "4":
                    System.out.println("\n👋 Thank you for using Beautiful Weather App!");
                    return;
                default:
                    System.out.println("\n❌ Invalid choice. Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private static void printWelcome() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║          🌤️ Beautiful Weather App          ║");
        System.out.println("║              Console Version              ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static void printMenu() {
        System.out.println("\n┌─ Main Menu ─────────────────────────────┐");
        System.out.println("│ 1. Current Weather                      │");
        System.out.println("│ 2. 5-Day Forecast                       │");
        System.out.println("│ 3. Complete Weather Data                │");
        System.out.println("│ 4. Exit                                  │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("Choose an option (1-4): ");
    }
    
    private static void getCurrentWeather() {
        System.out.print("\n🌍 Enter city name: ");
        String cityName = scanner.nextLine().trim();
        
        if (cityName.isEmpty()) {
            System.out.println("❌ City name cannot be empty!");
            return;
        }
        
        System.out.println("\n🔄 Fetching current weather for " + cityName + "...");
        
        try {
            WeatherData weather = weatherService.getCurrentWeather(cityName).get();
            displayCurrentWeather(weather);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("❌ Error fetching weather data: " + e.getMessage());
        }
    }
    
    private static void getForecast() {
        System.out.print("\n🌍 Enter city name: ");
        String cityName = scanner.nextLine().trim();
        
        if (cityName.isEmpty()) {
            System.out.println("❌ City name cannot be empty!");
            return;
        }
        
        System.out.println("\n🔄 Fetching forecast for " + cityName + "...");
        
        try {
            WeatherService.Location location = weatherService.getLocationByName(cityName).get();
            List<WeatherData.ForecastData> forecast = weatherService.getForecast(
                location.getLat(), location.getLon()).get();
            displayForecast(location.getName(), forecast);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("❌ Error fetching forecast data: " + e.getMessage());
        }
    }
    
    private static void getCompleteWeatherData() {
        System.out.print("\n🌍 Enter city name: ");
        String cityName = scanner.nextLine().trim();
        
        if (cityName.isEmpty()) {
            System.out.println("❌ City name cannot be empty!");
            return;
        }
        
        System.out.println("\n🔄 Fetching complete weather data for " + cityName + "...");
        
        try {
            WeatherData weather = weatherService.getCompleteWeatherData(cityName).get();
            displayCompleteWeatherData(weather);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("❌ Error fetching weather data: " + e.getMessage());
        }
    }
    
    private static void displayCurrentWeather(WeatherData weather) {
        System.out.println("\n┌─ Current Weather ───────────────────────┐");
        System.out.printf("│ 📍 Location: %-26s │\n", weather.getFullLocation());
        System.out.printf("│ 🌡️  Temperature: %-21s │\n", weather.getTemperatureString());
        System.out.printf("│ 🤔 Feels Like: %-23s │\n", weather.getFeelsLikeString());
        System.out.printf("│ 📝 Description: %-21s │\n", weather.getDescription());
        System.out.printf("│ 💧 Humidity: %-25s │\n", weather.getHumidity() + "%");
        System.out.printf("│ 💨 Wind: %-30s │\n", weather.getWindString());
        System.out.printf("│ 📊 Pressure: %-24s │\n", String.format("%.0f hPa", weather.getPressure()));
        
        if (weather.getSunrise() != null && weather.getSunset() != null) {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            System.out.printf("│ 🌅 Sunrise: %-25s │\n", weather.getSunrise().format(timeFormat));
            System.out.printf("│ 🌇 Sunset: %-26s │\n", weather.getSunset().format(timeFormat));
        }
        
        System.out.println("└─────────────────────────────────────────┘");
    }
    
    private static void displayForecast(String location, List<WeatherData.ForecastData> forecast) {
        System.out.println("\n┌─ 5-Day Forecast: " + location + " ────────────────┐");
        
        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("EEE dd/MM");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        
        // Take every 8th item for daily forecast (3-hour intervals)
        for (int i = 0; i < forecast.size() && i < 40; i += 8) {
            WeatherData.ForecastData item = forecast.get(i);
            System.out.printf("│ %s %s: %s, %s │\n", 
                item.getDateTime().format(dayFormat),
                item.getDateTime().format(timeFormat),
                item.getTemperatureString(),
                item.getDescription());
        }
        
        System.out.println("└─────────────────────────────────────────┘");
    }
    
    private static void displayCompleteWeatherData(WeatherData weather) {
        displayCurrentWeather(weather);
        
        // Air Quality
        if (weather.getAirQualityIndex() > 0) {
            System.out.println("\n┌─ Air Quality ───────────────────────────┐");
            System.out.printf("│ 🌍 Quality: %-26s │\n", weather.getAirQualityString());
            System.out.printf("│ 📈 Index: %-28s │\n", weather.getAirQualityIndex());
            System.out.println("└─────────────────────────────────────────┘");
        }
        
        // Forecast
        if (weather.getForecast() != null && !weather.getForecast().isEmpty()) {
            displayForecast(weather.getLocation(), weather.getForecast());
        }
    }
}