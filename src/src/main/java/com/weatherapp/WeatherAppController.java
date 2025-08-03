package com.weatherapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class WeatherAppController implements Initializable {

    @FXML
    private TextField searchField;
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
    private Label weatherIcon;
    @FXML
    private HBox hourlyForecastContainer;
    @FXML
    private GridPane detailsGrid;

    private WeatherAPIService apiService;
    private FileStorageService storageService;
    private boolean isCelsius = true;
    private WeatherData currentWeatherData;
    private Timeline loadingAnimation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apiService = new WeatherAPIService();
        storageService = new FileStorageService();

        setupEventHandlers();
        setupAnimations();
        loadDefaultWeather();
    }

    private void setupEventHandlers() {
        // Enhanced search functionality with real-time feedback
        searchField.setOnAction(e -> searchWeather());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 2) {
                // Auto-search after 3 characters
                searchWeather();
            }
        });

        // Enhanced temperature unit toggle with animations
        fahrenheitBtn.setOnAction(e -> {
            animateTemperatureToggle(false);
            isCelsius = false;
            fahrenheitBtn.setSelected(true);
            celsiusBtn.setSelected(false);
            updateTemperatureDisplay();
        });

        celsiusBtn.setOnAction(e -> {
            animateTemperatureToggle(true);
            isCelsius = true;
            celsiusBtn.setSelected(true);
            fahrenheitBtn.setSelected(false);
            updateTemperatureDisplay();
        });
    }

    private void setupAnimations() {
        // Create loading animation
        loadingAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(cityLabel.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(500), new KeyValue(cityLabel.opacityProperty(), 0.5)),
                new KeyFrame(Duration.millis(1000), new KeyValue(cityLabel.opacityProperty(), 1.0)));
        loadingAnimation.setCycleCount(Timeline.INDEFINITE);
    }

    private void animateTemperatureToggle(boolean toCelsius) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200),
                toCelsius ? celsiusBtn : fahrenheitBtn);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }

    private void searchWeather() {
        String cityName = searchField.getText().trim();
        if (!cityName.isEmpty()) {
            showLoading();
            CompletableFuture.runAsync(() -> {
                try {
                    // Get coordinates for the city
                    double[] coords = apiService.getCoordinates(cityName);
                    if (coords[0] != 0.0 && coords[1] != 0.0) {
                        // Get current weather
                        WeatherData weatherData = apiService.getCurrentWeather(coords[0], coords[1]);

                        // Get forecast
                        List<WeatherData.HourlyForecast> forecast = apiService.getForecast(coords[0], coords[1]);
                        weatherData.setHourlyForecast(forecast);

                        // Get air quality
                        WeatherData.AirQuality airQuality = apiService.getAirQuality(coords[0], coords[1]);
                        weatherData.setAirQuality(airQuality);

                        // Save to storage
                        storageService.saveWeatherData(weatherData);
                        storageService.saveLocation(cityName, coords[0], coords[1]);

                        // Update UI on JavaFX thread with animations
                        Platform.runLater(() -> {
                            currentWeatherData = weatherData;
                            animateWeatherUpdate();
                        });
                    } else {
                        Platform.runLater(() -> showError("City not found. Please try a different city name."));
                    }
                } catch (Exception ex) {
                    Platform.runLater(
                            () -> showError("Error fetching weather data. Please check your internet connection."));
                    ex.printStackTrace();
                }
            });
        }
    }

    private void animateWeatherUpdate() {
        // Fade out current content
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300),
                currentWeatherData != null ? cityLabel.getParent() : null);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.3);

        fadeOut.setOnFinished(e -> {
            updateWeatherDisplay();

            // Fade in new content
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500),
                    currentWeatherData != null ? cityLabel.getParent() : null);
            fadeIn.setFromValue(0.3);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    private void loadDefaultWeather() {
        // Load cached weather or default to Madrid
        List<WeatherData> cache = storageService.loadCache();
        if (!cache.isEmpty()) {
            currentWeatherData = cache.get(cache.size() - 1);
            updateWeatherDisplay();
        } else {
            // Default to Madrid
            searchField.setText("Madrid");
            searchWeather();
        }
    }

    private void updateWeatherDisplay() {
        if (currentWeatherData != null) {
            cityLabel.setText(currentWeatherData.getCityName());
            updateTemperatureDisplay();
            updateWeatherIcon();
            updateHourlyForecast();
            updateWeatherDetails();
            addSuccessEffect();
        }
    }

    private void updateTemperatureDisplay() {
        if (currentWeatherData != null) {
            double temp = currentWeatherData.getTemperature();
            double minTemp = currentWeatherData.getMinTemp();

            if (!isCelsius) {
                temp = (temp * 9 / 5) + 32;
                minTemp = (minTemp * 9 / 5) + 32;
            }

            // Animate temperature change
            animateValueChange(temperatureLabel, String.format("%.0f°", temp));
            animateValueChange(minTempLabel, String.format("%.0f°", minTemp));
        }
    }

    private void animateValueChange(Label label, String newValue) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), label);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        scaleUp.setOnFinished(e -> {
            label.setText(newValue);
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), label);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });

        scaleUp.play();
    }

    private void updateWeatherIcon() {
        if (currentWeatherData != null) {
            String icon = currentWeatherData.getIcon();
            String weatherText = getWeatherText(icon);

            // Animate weather icon change
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), weatherIcon);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                weatherIcon.setText(weatherText);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), weatherIcon);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();
        }
    }

    private void updateHourlyForecast() {
        hourlyForecastContainer.getChildren().clear();

        if (currentWeatherData != null && currentWeatherData.getHourlyForecast() != null) {
            List<WeatherData.HourlyForecast> forecast = currentWeatherData.getHourlyForecast();

            for (int i = 0; i < Math.min(forecast.size(), 8); i++) {
                WeatherData.HourlyForecast hourly = forecast.get(i);
                VBox hourlyItem = createHourlyItem(hourly);

                // Add staggered animation
                hourlyItem.setOpacity(0);
                hourlyForecastContainer.getChildren().add(hourlyItem);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), hourlyItem);
                fadeIn.setDelay(Duration.millis(i * 100));
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
        }
    }

    private VBox createHourlyItem(WeatherData.HourlyForecast hourly) {
        VBox item = new VBox(5);
        item.getStyleClass().add("hourly-item");
        item.setAlignment(Pos.CENTER);

        Label timeLabel = new Label(hourly.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.getStyleClass().add("hourly-time");

        Label iconLabel = new Label(getWeatherText(hourly.getIcon()));
        iconLabel.getStyleClass().add("hourly-icon");

        double temp = hourly.getTemperature();
        if (!isCelsius) {
            temp = (temp * 9 / 5) + 32;
        }
        Label tempLabel = new Label(String.format("%.0f°", temp));
        tempLabel.getStyleClass().add("hourly-temp");

        item.getChildren().addAll(timeLabel, iconLabel, tempLabel);
        return item;
    }

    private void updateWeatherDetails() {
        detailsGrid.getChildren().clear();

        if (currentWeatherData != null) {
            int row = 0;
            int col = 0;

            // Sunrise
            if (currentWeatherData.getSunriseSunset() != null) {
                detailsGrid.add(
                        createDetailItem("☀️", "Sunrise", currentWeatherData.getSunriseSunset().getSunrise()),
                        col++, row);
            }

            // Sunset
            if (currentWeatherData.getSunriseSunset() != null) {
                detailsGrid.add(createDetailItem("🌅", "Sunset", currentWeatherData.getSunriseSunset().getSunset()),
                        col++, row);
            }

            // Chance of Rain
            detailsGrid.add(createDetailItem("🌧️", "Chance of rain", currentWeatherData.getChanceOfRain() + "%"),
                    col++, row);

            // Pressure
            detailsGrid.add(createDetailItem("📊", "Pressure", currentWeatherData.getPressure() + " mb"), col++,
                    row);

            row++;
            col = 0;

            // Wind
            detailsGrid.add(
                    createDetailItem("💨", "Wind", String.format("%.1f km/h", currentWeatherData.getWindSpeed())),
                    col++, row);

            // UV Index
            detailsGrid.add(createDetailItem("☀️", "UV Index", currentWeatherData.getUvIndex() + " of 10"), col++, row);

            // Feels Like
            double feelsLike = currentWeatherData.getFeelsLike();
            if (!isCelsius) {
                feelsLike = (feelsLike * 9 / 5) + 32;
            }
            detailsGrid.add(createDetailItem("🌡️", "Feels like", String.format("%.0f°", feelsLike)), col++, row);

            // Visibility
            detailsGrid.add(createDetailItem("👁️", "Visibility", currentWeatherData.getVisibility() + " km"),
                    col++, row);

            // Animate details appearance
            animateDetailsAppearance();
        }
    }

    private void animateDetailsAppearance() {
        for (int i = 0; i < detailsGrid.getChildren().size(); i++) {
            VBox detailItem = (VBox) detailsGrid.getChildren().get(i);
            detailItem.setOpacity(0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), detailItem);
            fadeIn.setDelay(Duration.millis(i * 50));
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }

    private VBox createDetailItem(String icon, String label, String value) {
        VBox item = new VBox(5);
        item.getStyleClass().add("detail-item");
        item.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("detail-icon");

        Label labelText = new Label(label);
        labelText.getStyleClass().add("detail-label");

        Label valueText = new Label(value);
        valueText.getStyleClass().add("detail-value");

        item.getChildren().addAll(iconLabel, labelText, valueText);
        return item;
    }

    private String getWeatherText(String iconCode) {
        // Enhanced weather text mapping with emojis
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

    private void showLoading() {
        // Enhanced loading state
        cityLabel.setText("Loading weather data...");
        cityLabel.getStyleClass().add("loading-text");
        loadingAnimation.play();

        // Add loading effect to search field
        searchField.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
    }

    private void showError(String message) {
        // Stop loading animation
        loadingAnimation.stop();
        cityLabel.getStyleClass().remove("loading-text");

        // Show error with animation
        cityLabel.setText("Error");
        cityLabel.setStyle("-fx-text-fill: #ffcccc; -fx-font-weight: bold;");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Weather App Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        // Reset search field style
        searchField.setStyle("");
    }

    private void addSuccessEffect() {
        // Stop loading animation
        loadingAnimation.stop();
        cityLabel.getStyleClass().remove("loading-text");

        // Add success effect
        DropShadow successGlow = new DropShadow();
        successGlow.setColor(Color.rgb(76, 175, 80, 0.6));
        successGlow.setRadius(10);
        successGlow.setSpread(0.3);

        cityLabel.setEffect(successGlow);

        // Remove effect after 2 seconds
        Timeline removeEffect = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            cityLabel.setEffect(null);
        }));
        removeEffect.play();

        // Reset search field style
        searchField.setStyle("");
    }
}