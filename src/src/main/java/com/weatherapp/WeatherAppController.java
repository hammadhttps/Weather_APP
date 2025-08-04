package com.weatherapp;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.concurrent.Task;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.prefs.Preferences;

public class WeatherAppController implements Initializable {

    // FXML Components
    @FXML
    private TextField searchField;
    @FXML
    private Button darkModeToggle;
    @FXML
    private Label darkModeIcon;
    @FXML
    private ToggleButton fahrenheitBtn;
    @FXML
    private ToggleButton celsiusBtn;
    @FXML
    private Label cityLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label minTempLabel;
    @FXML
    private Label maxTempLabel;
    @FXML
    private Label weatherIcon;
    @FXML
    private Label weatherDescription;
    @FXML
    private HBox hourlyForecastContainer;
    @FXML
    private GridPane detailsGrid;
    @FXML
    private GridPane airQualityGrid;

    // Loading and Error States
    @FXML
    private StackPane loadingOverlay;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label loadingText;
    @FXML
    private VBox errorContainer;
    @FXML
    private Label errorTitle;
    @FXML
    private Label errorMessage;
    @FXML
    private Button retryButton;
    @FXML
    private HBox successContainer;
    @FXML
    private Label successText;

    // Containers
    @FXML
    private VBox currentWeatherContainer;
    @FXML
    private VBox forecastContainer;
    @FXML
    private VBox detailsContainer;
    @FXML
    private VBox airQualityContainer;

    // Services and State
    private WeatherAPIService apiService;
    private FileStorageService storageService;
    private boolean isCelsius = true;
    private boolean isDarkMode = false;
    private WeatherData currentWeatherData;
    private Timeline searchDelayTimer;
    private Preferences preferences;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apiService = new WeatherAPIService();
        storageService = new FileStorageService();
        preferences = Preferences.userNodeForPackage(WeatherAppController.class);

        setupEventHandlers();
        loadUserPreferences();
        setupAnimations();
        loadDefaultWeather();
    }

    private void setupEventHandlers() {
        // Enhanced search with debouncing
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (searchDelayTimer != null) {
                searchDelayTimer.stop();
            }

            if (newValue != null && newValue.trim().length() > 2) {
                searchDelayTimer = new Timeline(new KeyFrame(Duration.millis(500), e -> searchWeather()));
                searchDelayTimer.play();
            }
        });

        searchField.setOnAction(e -> searchWeather());

        // Dark mode toggle
        darkModeToggle.setOnAction(e -> toggleDarkMode());

        // Temperature unit toggles
        fahrenheitBtn.setOnAction(e -> {
            if (fahrenheitBtn.isSelected()) {
                celsiusBtn.setSelected(false);
                isCelsius = false;
                updateTemperatureDisplay();
                saveUserPreferences();
                animateTemperatureChange();
            }
        });

        celsiusBtn.setOnAction(e -> {
            if (celsiusBtn.isSelected()) {
                fahrenheitBtn.setSelected(false);
                isCelsius = true;
                updateTemperatureDisplay();
                saveUserPreferences();
                animateTemperatureChange();
            }
        });

        // Retry button
        retryButton.setOnAction(e -> {
            hideError();
            searchWeather();
        });

        // Keyboard shortcuts - set up after scene is available
        Platform.runLater(() -> {
            if (searchField.getScene() != null) {
                searchField.getScene().setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case F5:
                            refreshWeatherData();
                            break;
                        case ESCAPE:
                            searchField.clear();
                            break;
                    }
                });
            }
        });
    }

    private void setupAnimations() {
        // Add entrance animations to main containers
        addEntranceAnimation(currentWeatherContainer, 0);
        addEntranceAnimation(forecastContainer, 200);
        addEntranceAnimation(detailsContainer, 400);
    }

    private void addEntranceAnimation(Node node, double delay) {
        node.setOpacity(0);
        node.setTranslateY(20);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setDelay(Duration.millis(delay));

        TranslateTransition slideUp = new TranslateTransition(Duration.millis(600), node);
        slideUp.setFromY(20);
        slideUp.setToY(0);
        slideUp.setDelay(Duration.millis(delay));

        ParallelTransition entrance = new ParallelTransition(fadeIn, slideUp);
        entrance.play();
    }

    private void loadUserPreferences() {
        isDarkMode = preferences.getBoolean("darkMode", false);
        isCelsius = preferences.getBoolean("celsius", true);

        updateDarkModeUI();
        celsiusBtn.setSelected(isCelsius);
        fahrenheitBtn.setSelected(!isCelsius);
    }

    private void saveUserPreferences() {
        preferences.putBoolean("darkMode", isDarkMode);
        preferences.putBoolean("celsius", isCelsius);
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        updateDarkModeUI();
        saveUserPreferences();
        animateDarkModeTransition();
    }

    private void updateDarkModeUI() {
        if (searchField.getScene() != null) {
            VBox root = (VBox) searchField.getScene().getRoot();
            if (isDarkMode) {
                root.getStyleClass().add("dark-theme");
                darkModeIcon.setText("☀️");
            } else {
                root.getStyleClass().remove("dark-theme");
                darkModeIcon.setText("🌙");
            }
        }
    }

    private void animateDarkModeTransition() {
        if (searchField.getScene() != null) {
            VBox root = (VBox) searchField.getScene().getRoot();
            FadeTransition fade = new FadeTransition(Duration.millis(300), root);
            fade.setFromValue(1.0);
            fade.setToValue(0.8);
            fade.setAutoReverse(true);
            fade.setCycleCount(2);
            fade.play();
        }
    }

    private void animateTemperatureChange() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), temperatureLabel);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void searchWeather() {
        String cityName = searchField.getText().trim();
        if (cityName.isEmpty()) {
            return;
        }

        showLoading("Searching for " + cityName + "...");

        Task<WeatherData> weatherTask = new Task<WeatherData>() {
            @Override
            protected WeatherData call() throws Exception {
                // Get coordinates for the city
                double[] coords = apiService.getCoordinates(cityName);
                if (coords[0] == 0.0 && coords[1] == 0.0) {
                    throw new Exception("City not found. Please check the spelling and try again.");
                }

                updateMessage("Getting current weather...");

                // Get current weather
                WeatherData weatherData = apiService.getCurrentWeather(coords[0], coords[1]);

                updateMessage("Getting forecast...");

                // Get forecast
                List<WeatherData.HourlyForecast> forecast = apiService.getForecast(coords[0], coords[1]);
                weatherData.setHourlyForecast(forecast);

                updateMessage("Getting air quality data...");

                // Get air quality
                try {
                    WeatherData.AirQuality airQuality = apiService.getAirQuality(coords[0], coords[1]);
                    weatherData.setAirQuality(airQuality);
                } catch (Exception e) {
                    // Air quality data is optional
                    System.out.println("Could not fetch air quality data: " + e.getMessage());
                }

                // Save to storage
                storageService.saveWeatherData(weatherData);
                storageService.saveLocation(cityName, coords[0], coords[1]);

                return weatherData;
            }
        };

        weatherTask.messageProperty().addListener((obs, oldMessage, newMessage) -> {
            Platform.runLater(() -> loadingText.setText(newMessage));
        });

        weatherTask.setOnSucceeded(e -> {
            currentWeatherData = weatherTask.getValue();
            hideLoading();
            updateWeatherDisplay();
            showSuccess("Weather data updated successfully!");
        });

        weatherTask.setOnFailed(e -> {
            hideLoading();
            Throwable exception = weatherTask.getException();
            showError("Weather Data Error", exception.getMessage());
        });

        new Thread(weatherTask).start();
    }

    private void refreshWeatherData() {
        if (currentWeatherData != null) {
            searchField.setText(currentWeatherData.getCityName());
            searchWeather();
        }
    }

    private void loadDefaultWeather() {
        // Try to load cached weather first
        List<WeatherData> cache = storageService.loadCache();
        if (!cache.isEmpty()) {
            currentWeatherData = cache.get(cache.size() - 1);
            updateWeatherDisplay();

            // Refresh data in background
            Platform.runLater(() -> {
                searchField.setText(currentWeatherData.getCityName());
                searchWeather();
            });
        } else {
            // Default to Madrid
            Platform.runLater(() -> {
                searchField.setText("Madrid");
                searchWeather();
            });
        }
    }

    private void updateWeatherDisplay() {
        if (currentWeatherData == null)
            return;

        // Animate content update
        animateContentUpdate(() -> {
            updateBasicWeatherInfo();
            updateTemperatureDisplay();
            updateWeatherIcon();
            updateHourlyForecast();
            updateWeatherDetails();
            updateAirQuality();
        });
    }

    private void animateContentUpdate(Runnable updateContent) {
        // Fade out current content
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), currentWeatherContainer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.3);

        fadeOut.setOnFinished(e -> {
            updateContent.run();

            // Fade in new content
            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), currentWeatherContainer);
            fadeIn.setFromValue(0.3);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    private void updateBasicWeatherInfo() {
        cityLabel.setText(currentWeatherData.getCityName());
        weatherDescription.setText(capitalizeWords(currentWeatherData.getDescription()));
    }

    private void updateTemperatureDisplay() {
        if (currentWeatherData == null)
            return;

        double temp = currentWeatherData.getTemperature();
        double minTemp = currentWeatherData.getMinTemp();
        double maxTemp = currentWeatherData.getMaxTemp();

        if (!isCelsius) {
            temp = celsiusToFahrenheit(temp);
            minTemp = celsiusToFahrenheit(minTemp);
            maxTemp = celsiusToFahrenheit(maxTemp);
        }

        String unit = isCelsius ? "°C" : "°F";
        temperatureLabel.setText(String.format("%.0f°", temp));
        minTempLabel.setText(String.format("L: %.0f°", minTemp));
        maxTempLabel.setText(String.format("H: %.0f°", maxTemp));
    }

    private void updateWeatherIcon() {
        if (currentWeatherData != null) {
            String iconText = getWeatherEmoji(currentWeatherData.getIcon());

            // Animate icon change
            RotateTransition rotate = new RotateTransition(Duration.millis(300), weatherIcon);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);

            ScaleTransition scale = new ScaleTransition(Duration.millis(300), weatherIcon);
            scale.setFromX(1.0);
            scale.setFromY(1.0);
            scale.setToX(1.2);
            scale.setToY(1.2);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);

            ParallelTransition iconAnimation = new ParallelTransition(rotate, scale);
            iconAnimation.setOnFinished(e -> weatherIcon.setText(iconText));
            iconAnimation.play();
        }
    }

    private void updateHourlyForecast() {
        hourlyForecastContainer.getChildren().clear();

        if (currentWeatherData != null && currentWeatherData.getHourlyForecast() != null) {
            List<WeatherData.HourlyForecast> forecast = currentWeatherData.getHourlyForecast();

            for (int i = 0; i < Math.min(forecast.size(), 12); i++) {
                WeatherData.HourlyForecast hourly = forecast.get(i);
                VBox hourlyItem = createHourlyItem(hourly);

                // Add staggered entrance animation
                hourlyItem.setOpacity(0);
                hourlyItem.setTranslateY(20);
                hourlyForecastContainer.getChildren().add(hourlyItem);

                Timeline animation = new Timeline(
                        new KeyFrame(Duration.millis(i * 50),
                                new KeyValue(hourlyItem.opacityProperty(), 0),
                                new KeyValue(hourlyItem.translateYProperty(), 20)),
                        new KeyFrame(Duration.millis(300 + i * 50),
                                new KeyValue(hourlyItem.opacityProperty(), 1),
                                new KeyValue(hourlyItem.translateYProperty(), 0)));
                animation.play();
            }
        }
    }

    private VBox createHourlyItem(WeatherData.HourlyForecast hourly) {
        VBox item = new VBox(8);
        item.getStyleClass().add("hourly-item");
        item.setAlignment(Pos.CENTER);

        // Time
        String timeText = hourly.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        Label timeLabel = new Label(timeText);
        timeLabel.getStyleClass().add("hourly-time");

        // Weather icon
        Label iconLabel = new Label(getWeatherEmoji(hourly.getIcon()));
        iconLabel.getStyleClass().add("hourly-icon");

        // Temperature
        double temp = hourly.getTemperature();
        if (!isCelsius) {
            temp = celsiusToFahrenheit(temp);
        }
        Label tempLabel = new Label(String.format("%.0f°", temp));
        tempLabel.getStyleClass().add("hourly-temp");

        // Rain chance
        if (hourly.getChanceOfRain() > 0) {
            Label rainLabel = new Label(hourly.getChanceOfRain() + "%");
            rainLabel.getStyleClass().add("hourly-rain");
            item.getChildren().addAll(timeLabel, iconLabel, tempLabel, rainLabel);
        } else {
            item.getChildren().addAll(timeLabel, iconLabel, tempLabel);
        }

        return item;
    }

    private void updateWeatherDetails() {
        detailsGrid.getChildren().clear();

        if (currentWeatherData == null)
            return;

        int row = 0;
        int col = 0;
        int maxCols = 4;

        // Feels Like
        double feelsLike = currentWeatherData.getFeelsLike();
        if (!isCelsius) {
            feelsLike = celsiusToFahrenheit(feelsLike);
        }
        addDetailItem("🌡️", "Feels like", String.format("%.0f°", feelsLike), col++, row);

        // Humidity
        addDetailItem("💧", "Humidity", currentWeatherData.getHumidity() + "%", col++, row);

        // Wind
        addDetailItem("💨", "Wind", String.format("%.1f km/h", currentWeatherData.getWindSpeed()), col++, row);

        // Pressure
        addDetailItem("📊", "Pressure", currentWeatherData.getPressure() + " hPa", col++, row);

        // New row
        row++;
        col = 0;

        // Sunrise
        if (currentWeatherData.getSunriseSunset() != null) {
            addDetailItem("🌅", "Sunrise", currentWeatherData.getSunriseSunset().getSunrise(), col++, row);
        }

        // Sunset
        if (currentWeatherData.getSunriseSunset() != null) {
            addDetailItem("🌇", "Sunset", currentWeatherData.getSunriseSunset().getSunset(), col++, row);
        }

        // UV Index (placeholder)
        addDetailItem("☀️", "UV Index", "5 of 10", col++, row);

        // Visibility (placeholder)
        addDetailItem("👁️", "Visibility", "10 km", col++, row);

        // Animate details appearance
        animateDetailsGrid();
    }

    private void addDetailItem(String icon, String label, String value, int col, int row) {
        VBox item = new VBox(8);
        item.getStyleClass().add("detail-item");
        item.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("detail-icon");

        Label labelText = new Label(label.toUpperCase());
        labelText.getStyleClass().add("detail-label");

        Label valueText = new Label(value);
        valueText.getStyleClass().add("detail-value");

        item.getChildren().addAll(iconLabel, labelText, valueText);
        detailsGrid.add(item, col, row);
    }

    private void animateDetailsGrid() {
        for (Node node : detailsGrid.getChildren()) {
            node.setOpacity(0);
            node.setScaleX(0.8);
            node.setScaleY(0.8);

            int index = detailsGrid.getChildren().indexOf(node);
            Timeline animation = new Timeline(
                    new KeyFrame(Duration.millis(index * 100),
                            new KeyValue(node.opacityProperty(), 0),
                            new KeyValue(node.scaleXProperty(), 0.8),
                            new KeyValue(node.scaleYProperty(), 0.8)),
                    new KeyFrame(Duration.millis(400 + index * 100),
                            new KeyValue(node.opacityProperty(), 1),
                            new KeyValue(node.scaleXProperty(), 1),
                            new KeyValue(node.scaleYProperty(), 1)));
            animation.play();
        }
    }

    private void updateAirQuality() {
        if (currentWeatherData == null || currentWeatherData.getAirQuality() == null) {
            airQualityContainer.setVisible(false);
            airQualityContainer.setManaged(false);
            return;
        }

        airQualityContainer.setVisible(true);
        airQualityContainer.setManaged(true);
        airQualityGrid.getChildren().clear();

        WeatherData.AirQuality aq = currentWeatherData.getAirQuality();

        // Air Quality Index
        String aqiText = getAQIDescription(aq.getAqi());
        addAirQualityItem("🌬️", "Air Quality", aqiText, 0, 0);

        // PM2.5
        addAirQualityItem("🔬", "PM2.5", String.format("%.1f μg/m³", aq.getPm25()), 1, 0);

        // PM10
        addAirQualityItem("🔬", "PM10", String.format("%.1f μg/m³", aq.getPm10()), 2, 0);

        // Ozone
        addAirQualityItem("🌫️", "Ozone", String.format("%.1f μg/m³", aq.getO3()), 3, 0);
    }

    private void addAirQualityItem(String icon, String label, String value, int col, int row) {
        VBox item = new VBox(8);
        item.getStyleClass().add("detail-item");
        item.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("detail-icon");

        Label labelText = new Label(label.toUpperCase());
        labelText.getStyleClass().add("detail-label");

        Label valueText = new Label(value);
        valueText.getStyleClass().add("detail-value");

        item.getChildren().addAll(iconLabel, labelText, valueText);
        airQualityGrid.add(item, col, row);
    }

    private void showLoading(String message) {
        loadingText.setText(message);
        loadingOverlay.setVisible(true);
        loadingOverlay.setManaged(true);

        // Animate loading appearance
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), loadingOverlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        hideError();
        hideSuccess();
    }

    private void hideLoading() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), loadingOverlay);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            loadingOverlay.setVisible(false);
            loadingOverlay.setManaged(false);
        });
        fadeOut.play();
    }

    private void showError(String title, String message) {
        errorTitle.setText(title);
        errorMessage.setText(message);
        errorContainer.setVisible(true);
        errorContainer.setManaged(true);

        // Animate error appearance
        errorContainer.setOpacity(0);
        errorContainer.setTranslateY(20);

        Timeline showError = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(errorContainer.opacityProperty(), 0),
                        new KeyValue(errorContainer.translateYProperty(), 20)),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(errorContainer.opacityProperty(), 1),
                        new KeyValue(errorContainer.translateYProperty(), 0)));
        showError.play();

        hideSuccess();
    }

    private void hideError() {
        if (errorContainer.isVisible()) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), errorContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                errorContainer.setVisible(false);
                errorContainer.setManaged(false);
            });
            fadeOut.play();
        }
    }

    private void showSuccess(String message) {
        successText.setText(message);
        successContainer.setVisible(true);
        successContainer.setManaged(true);

        // Auto-hide after 3 seconds
        Timeline autoHide = new Timeline(new KeyFrame(Duration.seconds(3), e -> hideSuccess()));
        autoHide.play();

        // Add success glow effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(76, 175, 80, 0.6));
        glow.setRadius(15);
        successContainer.setEffect(glow);
    }

    private void hideSuccess() {
        if (successContainer.isVisible()) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), successContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                successContainer.setVisible(false);
                successContainer.setManaged(false);
                successContainer.setEffect(null);
            });
            fadeOut.play();
        }
    }

    // Utility Methods
    private double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

    private String capitalizeWords(String text) {
        if (text == null || text.isEmpty())
            return text;

        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    private String getWeatherEmoji(String iconCode) {
        switch (iconCode) {
            case "01d":
                return "☀️"; // clear sky day
            case "01n":
                return "🌙"; // clear sky night
            case "02d":
                return "⛅"; // few clouds day
            case "02n":
                return "☁️"; // few clouds night
            case "03d":
            case "03n":
                return "☁️"; // scattered clouds
            case "04d":
            case "04n":
                return "☁️"; // broken clouds
            case "09d":
            case "09n":
                return "🌧️"; // shower rain
            case "10d":
                return "🌦️"; // rain day
            case "10n":
                return "🌧️"; // rain night
            case "11d":
            case "11n":
                return "⛈️"; // thunderstorm
            case "13d":
            case "13n":
                return "❄️"; // snow
            case "50d":
            case "50n":
                return "🌫️"; // mist
            default:
                return "🌤️";
        }
    }

    private String getAQIDescription(int aqi) {
        switch (aqi) {
            case 1:
                return "Good";
            case 2:
                return "Fair";
            case 3:
                return "Moderate";
            case 4:
                return "Poor";
            case 5:
                return "Very Poor";
            default:
                return "Unknown";
        }
    }
}