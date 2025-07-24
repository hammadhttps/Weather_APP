# Beautiful Weather App ☀️

A modern, beautiful weather application built with JavaFX that provides real-time weather data, forecasts, and air quality information for any location worldwide.

![Weather App](https://img.shields.io/badge/JavaFX-19-blue)
![Maven](https://img.shields.io/badge/Maven-3.8+-green)
![Java](https://img.shields.io/badge/Java-11+-orange)
![OpenWeatherMap](https://img.shields.io/badge/API-OpenWeatherMap-yellow)

## ✨ Features

### 🌤️ Current Weather
- **Real-time weather data** for any city worldwide
- **Temperature** with "feels like" reading
- **Weather description** with appropriate icons
- **Min/Max temperatures** for the day
- **Detailed metrics**: humidity, wind speed, pressure, visibility
- **Sunrise and sunset times**

### 🌍 Air Quality
- **Air Quality Index (AQI)** with color-coded indicators
- **Real-time pollution data**
- **Health recommendations** based on air quality

### 📅 5-Day Forecast
- **Extended weather forecast** with 3-hour intervals
- **Daily summaries** with temperature trends
- **Weather icons** and descriptions
- **Interactive scrollable** forecast cards

### 🎨 Beautiful Design
- **Modern glass-morphism** design with gradients
- **Smooth animations** and transitions
- **Responsive layout** that adapts to window size
- **Intuitive user interface** with emoji weather icons
- **Professional styling** with custom CSS

## 🚀 Getting Started

### Prerequisites
- **Java 11** or higher
- **Maven 3.8** or higher
- **Internet connection** for API calls

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd beautiful-weather-app
   ```

2. **Build the application**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

   Or alternatively:
   ```bash
   mvn clean javafx:run
   ```

### Alternative Running Methods

**Using Maven exec plugin:**
```bash
mvn clean compile exec:java -Dexec.mainClass="com.weatherapp.WeatherApp"
```

**Creating a JAR:**
```bash
mvn clean package
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/classes com.weatherapp.WeatherApp
```

## 🏗️ Project Structure

```
beautiful-weather-app/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── weatherapp/
│       │           ├── WeatherApp.java        # Main JavaFX application
│       │           ├── WeatherService.java    # API service layer
│       │           ├── WeatherData.java       # Data models
│       │           └── module-info.java       # Module configuration
│       └── resources/
│           └── weather-app.css               # Beautiful styling
├── pom.xml                                   # Maven configuration
└── README.md                                # This file
```

## 🌐 API Integration

This application uses the **OpenWeatherMap API** to fetch:
- Current weather data
- 5-day weather forecast
- Air pollution data
- Geocoding for city name lookup

### API Endpoints Used:
- `api.openweathermap.org/data/2.5/weather` - Current weather
- `api.openweathermap.org/data/2.5/forecast` - 5-day forecast
- `api.openweathermap.org/data/2.5/air_pollution` - Air quality
- `api.openweathermap.org/geo/1.0/direct` - Geocoding

## 🎯 Usage

1. **Launch the application** - it starts with London weather by default
2. **Search for any city** by typing in the search field
3. **Press Enter** or click the Search button
4. **View comprehensive weather data** including:
   - Current temperature and conditions
   - Detailed weather metrics
   - 5-day forecast
   - Air quality information

## 🛠️ Technical Details

### Architecture
- **MVVM Pattern**: Clean separation of UI and business logic
- **Async Processing**: Non-blocking API calls using CompletableFuture
- **Modern JavaFX**: Utilizes latest JavaFX features and styling
- **Modular Design**: Proper Java module system integration

### Dependencies
- **JavaFX Controls & FXML**: Modern UI framework
- **JSON Processing**: For API response parsing
- **HTTP Client**: Built-in Java 11+ HTTP client
- **Material Design Icons**: Beautiful iconography

### Performance Features
- **Concurrent API calls**: Parallel fetching of weather, forecast, and air quality data
- **Responsive UI**: Smooth animations and loading indicators
- **Error handling**: Graceful error messages and automatic retry
- **Memory efficient**: Proper resource management

## 🎨 Customization

### Styling
The application uses a custom CSS file (`weather-app.css`) with:
- **Glass-morphism effects**
- **Gradient backgrounds**
- **Smooth transitions**
- **Color-coded air quality indicators**
- **Responsive design utilities**

### Adding New Features
The modular architecture makes it easy to add new features:
- Extend `WeatherData` for new data fields
- Add new API endpoints in `WeatherService`
- Create new UI components in `WeatherApp`

## 🔧 Troubleshooting

### Common Issues

**"Module not found" errors:**
- Ensure you're using Java 11 or higher
- Verify JavaFX is properly installed

**API connection issues:**
- Check your internet connection
- Verify the API key is valid (embedded in the code)

**Build errors:**
- Ensure Maven 3.8+ is installed
- Run `mvn clean` before building

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📧 Support

If you have any questions or issues, please open an issue on the repository.

---

**Enjoy using the Beautiful Weather App!** 🌟

*Built with ❤️ using JavaFX and OpenWeatherMap API*