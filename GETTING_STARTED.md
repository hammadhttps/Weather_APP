# 🚀 Quick Start Guide

## Beautiful Weather App - Console Version (Working)

The console version is ready to use immediately!

### 1. Build & Run Console Version
```bash
# Build the core classes
./build-simple.sh

# Run the demo (shows London weather)
./demo.sh

# Run interactively
java -cp "target/lib/json-20230227.jar:target/classes" com.weatherapp.ConsoleWeatherApp
```

### 2. Features Available
✅ **Current Weather** - Real-time weather data for any city
✅ **5-Day Forecast** - Extended weather predictions  
✅ **Air Quality** - AQI with color-coded quality levels
✅ **Complete Data** - All weather metrics in one view

### 3. Example Usage
```
🌍 Enter city name: Tokyo
🔄 Fetching complete weather data for Tokyo...

┌─ Current Weather ───────────────────────┐
│ 📍 Location: Tokyo, JP                  │
│ 🌡️  Temperature: 24°C                  │
│ 🤔 Feels Like: Feels like 28°C         │
│ 📝 Description: clear sky              │
└─────────────────────────────────────────┘
```

## Beautiful Weather App - GUI Version (Requires JavaFX)

### Setup for GUI Version
```bash
# Install JavaFX (Ubuntu/Debian)
sudo apt install openjfx

# Run the beautiful GUI app
./run.sh

# Or with Maven (if available)
mvn clean javafx:run
```

### GUI Features
🎨 **Modern Design** - Glass-morphism effects with gradients
🌈 **Animations** - Smooth transitions and loading states
📱 **Responsive** - Adapts to window size
🖱️ **Interactive** - Hover effects and clickable elements

## 📁 Project Structure
```
beautiful-weather-app/
├── src/main/java/com/weatherapp/
│   ├── WeatherApp.java          # JavaFX GUI application
│   ├── ConsoleWeatherApp.java   # Console application
│   ├── WeatherService.java      # API service layer
│   ├── WeatherData.java         # Data models
│   └── module-info.java         # Module configuration
├── src/main/resources/
│   └── weather-app.css          # Beautiful styling
├── build-simple.sh              # Build console version
├── run.sh                       # Run GUI version
├── demo.sh                      # Demo console app
├── pom.xml                      # Maven configuration
└── README.md                    # Full documentation
```

## 🌐 API Information
- **Service**: OpenWeatherMap API
- **Endpoints**: Weather, Forecast, Air Quality, Geocoding
- **Rate Limits**: Generous free tier
- **Data**: Real-time global weather data

## 🔧 Troubleshooting
- **Console app not working**: Check internet connection
- **GUI app issues**: Install JavaFX or use Maven
- **Compilation errors**: Ensure Java 11+ is installed

---
**🌟 Start with the console version - it works out of the box!**