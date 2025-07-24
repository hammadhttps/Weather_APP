package com.weatherapp;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Beautiful Weather Application built with JavaFX
 */
public class WeatherApp extends Application {
    private static final double WINDOW_WIDTH = 800;
    private static final double WINDOW_HEIGHT = 900;
    
    private WeatherService weatherService;
    private TextField searchField;
    private Button searchButton;
    private VBox mainContainer;
    private VBox weatherContainer;
    private ProgressIndicator loadingIndicator;
    private Label loadingLabel;
    
    @Override
    public void start(Stage primaryStage) {
        weatherService = new WeatherService();
        
        primaryStage.setTitle("Beautiful Weather App");
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(700);
        
        // Create main layout
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        
        // Create main container with styling
        mainContainer = new VBox(20);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(30));
        
        // Create search section
        VBox searchContainer = createSearchSection();
        
        // Create weather display container
        weatherContainer = new VBox(20);
        weatherContainer.setAlignment(Pos.CENTER);
        
        // Create loading indicator
        VBox loadingContainer = createLoadingIndicator();
        
        // Add components to main container
        mainContainer.getChildren().addAll(searchContainer, weatherContainer);
        
        // Wrap in ScrollPane for responsiveness
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scroll-pane");
        
        root.setCenter(scrollPane);
        
        // Load CSS
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/weather-app.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Load default weather (London)
        loadWeatherData("London");
    }
    
    private VBox createSearchSection() {
        VBox searchContainer = new VBox(15);
        searchContainer.getStyleClass().add("search-container");
        searchContainer.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Beautiful Weather");
        titleLabel.getStyleClass().addAll("location-label", "xl-text");
        
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        
        searchField = new TextField();
        searchField.getStyleClass().add("search-field");
        searchField.setPromptText("Enter city name...");
        searchField.setPrefWidth(300);
        searchField.setOnAction(e -> searchWeather());
        
        searchButton = new Button("Search");
        searchButton.getStyleClass().add("search-button");
        searchButton.setOnAction(e -> searchWeather());
        
        searchBox.getChildren().addAll(searchField, searchButton);
        searchContainer.getChildren().addAll(titleLabel, searchBox);
        
        return searchContainer;
    }
    
    private VBox createLoadingIndicator() {
        VBox loadingContainer = new VBox(15);
        loadingContainer.getStyleClass().add("loading-indicator");
        loadingContainer.setAlignment(Pos.CENTER);
        
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.getStyleClass().add("progress-indicator");
        loadingIndicator.setMaxSize(50, 50);
        
        loadingLabel = new Label("Loading weather data...");
        loadingLabel.getStyleClass().add("loading-text");
        
        loadingContainer.getChildren().addAll(loadingIndicator, loadingLabel);
        return loadingContainer;
    }
    
    private void searchWeather() {
        String cityName = searchField.getText().trim();
        if (!cityName.isEmpty()) {
            loadWeatherData(cityName);
            searchField.clear();
        }
    }
    
    private void loadWeatherData(String cityName) {
        // Show loading indicator
        showLoading(true);
        
        Task<WeatherData> task = new Task<WeatherData>() {
            @Override
            protected WeatherData call() throws Exception {
                return weatherService.getCompleteWeatherData(cityName).get();
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    WeatherData weather = getValue();
                    displayWeatherData(weather);
                    showLoading(false);
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    showError("Failed to load weather data for: " + cityName, 
                             getException().getMessage());
                    showLoading(false);
                });
            }
        };
        
        new Thread(task).start();
    }
    
    private void showLoading(boolean show) {
        if (show) {
            weatherContainer.getChildren().clear();
            weatherContainer.getChildren().add(createLoadingIndicator());
        }
    }
    
    private void displayWeatherData(WeatherData weather) {
        weatherContainer.getChildren().clear();
        
        // Current weather card
        VBox currentWeatherCard = createCurrentWeatherCard(weather);
        
        // Weather details grid
        GridPane detailsGrid = createWeatherDetailsGrid(weather);
        
        // Forecast section
        VBox forecastSection = createForecastSection(weather.getForecast());
        
        // Add all sections with fade-in animation
        weatherContainer.getChildren().addAll(currentWeatherCard, detailsGrid, forecastSection);
        
        // Animate the weather container
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), weatherContainer);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    private VBox createCurrentWeatherCard(WeatherData weather) {
        VBox card = new VBox(15);
        card.getStyleClass().add("weather-card");
        card.setAlignment(Pos.CENTER);
        
        // Location
        Label locationLabel = new Label(weather.getFullLocation());
        locationLabel.getStyleClass().add("location-label");
        
        // Current time
        Label timeLabel = new Label(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy • h:mm a")));
        timeLabel.getStyleClass().add("feels-like-label");
        
        // Temperature
        Label tempLabel = new Label(weather.getTemperatureString());
        tempLabel.getStyleClass().add("temperature-label");
        
        // Description
        Label descLabel = new Label(weather.getDescription().toUpperCase());
        descLabel.getStyleClass().add("description-label");
        
        // Feels like
        Label feelsLikeLabel = new Label(weather.getFeelsLikeString());
        feelsLikeLabel.getStyleClass().add("feels-like-label");
        
        // Min/Max temperatures
        HBox minMaxBox = new HBox(20);
        minMaxBox.setAlignment(Pos.CENTER);
        
        Label minTempLabel = new Label(String.format("L: %.0f°", weather.getMinTemp()));
        Label maxTempLabel = new Label(String.format("H: %.0f°", weather.getMaxTemp()));
        minTempLabel.getStyleClass().add("feels-like-label");
        maxTempLabel.getStyleClass().add("feels-like-label");
        
        minMaxBox.getChildren().addAll(minTempLabel, maxTempLabel);
        
        card.getChildren().addAll(locationLabel, timeLabel, tempLabel, descLabel, 
                                 feelsLikeLabel, minMaxBox);
        
        return card;
    }
    
    private GridPane createWeatherDetailsGrid(WeatherData weather) {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("details-grid");
        grid.setAlignment(Pos.CENTER);
        
        // Create detail items
        VBox humidityItem = createDetailItem("💧", weather.getHumidity() + "%", "Humidity");
        VBox windItem = createDetailItem("💨", weather.getWindString(), "Wind");
        VBox pressureItem = createDetailItem("📊", String.format("%.0f hPa", weather.getPressure()), "Pressure");
        VBox visibilityItem = createDetailItem("👁", String.format("%.1f km", weather.getVisibility() / 1000.0), "Visibility");
        
        // Air quality with color coding
        VBox airQualityItem = createDetailItem("🌍", weather.getAirQualityString(), "Air Quality");
        String airQualityClass = "air-quality-" + weather.getAirQualityString().toLowerCase().replace(" ", "-");
        airQualityItem.getStyleClass().add(airQualityClass);
        
        // Sunrise/Sunset
        VBox sunriseItem = null, sunsetItem = null;
        if (weather.getSunrise() != null && weather.getSunset() != null) {
            sunriseItem = createDetailItem("🌅", 
                weather.getSunrise().format(DateTimeFormatter.ofPattern("h:mm a")), "Sunrise");
            sunsetItem = createDetailItem("🌇", 
                weather.getSunset().format(DateTimeFormatter.ofPattern("h:mm a")), "Sunset");
        }
        
        // Add to grid
        grid.add(humidityItem, 0, 0);
        grid.add(windItem, 1, 0);
        grid.add(pressureItem, 2, 0);
        grid.add(visibilityItem, 0, 1);
        grid.add(airQualityItem, 1, 1);
        
        if (sunriseItem != null && sunsetItem != null) {
            grid.add(sunriseItem, 2, 1);
            // Add sunset in a new row if needed
            if (grid.getColumnCount() > 2) {
                grid.add(sunsetItem, 0, 2);
            }
        }
        
        return grid;
    }
    
    private VBox createDetailItem(String icon, String value, String label) {
        VBox item = new VBox(8);
        item.getStyleClass().add("detail-item");
        item.setAlignment(Pos.CENTER);
        
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("detail-icon");
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("detail-value");
        
        Label labelText = new Label(label);
        labelText.getStyleClass().add("detail-label");
        
        item.getChildren().addAll(iconLabel, valueLabel, labelText);
        return item;
    }
    
    private VBox createForecastSection(List<WeatherData.ForecastData> forecast) {
        VBox section = new VBox(15);
        section.getStyleClass().add("forecast-container");
        
        Label titleLabel = new Label("5-Day Forecast");
        titleLabel.getStyleClass().add("forecast-title");
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("forecast-scroll");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        
        HBox forecastBox = new HBox(15);
        forecastBox.setAlignment(Pos.CENTER_LEFT);
        forecastBox.setPadding(new Insets(10));
        
        if (forecast != null) {
            // Take every 8th item to get daily forecasts (3-hour intervals)
            for (int i = 0; i < forecast.size(); i += 8) {
                if (i < forecast.size()) {
                    WeatherData.ForecastData item = forecast.get(i);
                    VBox forecastItem = createForecastItem(item);
                    forecastBox.getChildren().add(forecastItem);
                }
            }
        }
        
        scrollPane.setContent(forecastBox);
        section.getChildren().addAll(titleLabel, scrollPane);
        
        return section;
    }
    
    private VBox createForecastItem(WeatherData.ForecastData forecast) {
        VBox item = new VBox(8);
        item.getStyleClass().addAll("forecast-item", "hoverable");
        item.setAlignment(Pos.CENTER);
        
        // Day/Time
        String timeText = forecast.getDateTime().format(DateTimeFormatter.ofPattern("EEE\nMM/dd"));
        Label timeLabel = new Label(timeText);
        timeLabel.getStyleClass().add("forecast-time");
        timeLabel.setStyle("-fx-text-alignment: center");
        
        // Weather icon (using emoji as placeholder)
        String weatherIcon = getWeatherEmoji(forecast.getIcon());
        Label iconLabel = new Label(weatherIcon);
        iconLabel.getStyleClass().add("detail-icon");
        
        // Temperature
        Label tempLabel = new Label(forecast.getTemperatureString());
        tempLabel.getStyleClass().add("forecast-temp");
        
        // Description
        Label descLabel = new Label(forecast.getDescription());
        descLabel.getStyleClass().add("forecast-desc");
        descLabel.setMaxWidth(100);
        
        item.getChildren().addAll(timeLabel, iconLabel, tempLabel, descLabel);
        return item;
    }
    
    private String getWeatherEmoji(String icon) {
        if (icon == null) return "☀️";
        
        switch (icon.substring(0, 2)) {
            case "01": return "☀️"; // clear sky
            case "02": return "⛅"; // few clouds
            case "03": return "☁️"; // scattered clouds
            case "04": return "☁️"; // broken clouds
            case "09": return "🌧️"; // shower rain
            case "10": return "🌦️"; // rain
            case "11": return "⛈️"; // thunderstorm
            case "13": return "❄️"; // snow
            case "50": return "🌫️"; // mist
            default: return "🌤️";
        }
    }
    
    private void showError(String title, String message) {
        weatherContainer.getChildren().clear();
        
        VBox errorContainer = new VBox(15);
        errorContainer.getStyleClass().add("error-container");
        errorContainer.setAlignment(Pos.CENTER);
        errorContainer.setMaxWidth(500);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("error-title");
        
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("error-message");
        messageLabel.setWrapText(true);
        
        Button retryButton = new Button("Try Again");
        retryButton.getStyleClass().add("search-button");
        retryButton.setOnAction(e -> loadWeatherData("London"));
        
        errorContainer.getChildren().addAll(titleLabel, messageLabel, retryButton);
        weatherContainer.getChildren().add(errorContainer);
        
        // Auto-hide error after 5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> loadWeatherData("London"));
        pause.play();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}