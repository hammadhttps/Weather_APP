# Weather App Installation Guide

## Prerequisites

1. **Java 17 or higher** - Already installed ✓
2. **Maven 3.6 or higher** - Need to install
3. **JavaFX SDK** - Need to download

## Installation Steps

### 1. Install Maven

#### Windows:
1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH: `C:\Program Files\Apache\maven\bin`

#### Using Chocolatey (if available):
```bash
choco install maven
```

### 2. Download JavaFX SDK

1. Go to: https://openjfx.io/
2. Download JavaFX SDK for Windows
3. Extract to a folder (e.g., `C:\javafx-sdk-17`)

### 3. Set Environment Variables

Add these to your system environment variables:
```
JAVAFX_HOME=C:\javafx-sdk-17
PATH=%PATH%;%JAVAFX_HOME%\bin
```

### 4. Build and Run

1. Navigate to the `src` directory
2. Run: `mvn clean compile`
3. Run: `mvn javafx:run`

## Alternative: Using IDE

1. **IntelliJ IDEA**:
   - Import as Maven project
   - Add JavaFX SDK to project libraries
   - Run `WeatherApplication` class

2. **Eclipse**:
   - Import as Maven project
   - Install e(fx)clipse plugin
   - Run `WeatherApplication` class

3. **VS Code**:
   - Install Java Extension Pack
   - Install Maven for Java extension
   - Open project and run

## Project Structure

```
Weather_APP/
├── src/
│   ├── com/weatherapp/
│   │   ├── WeatherApplication.java      # Main app
│   │   ├── WeatherAppController.java   # UI controller
│   │   ├── WeatherData.java           # Data models
│   │   ├── WeatherAPIService.java     # API service
│   │   └── FileStorageService.java    # File storage
│   ├── fxml/
│   │   └── WeatherApp.fxml           # UI layout
│   ├── weather-app.css               # Styles
│   ├── pom.xml                      # Maven config
│   └── module-info.java             # Module config
├── README.md                        # Project documentation
├── build.bat                        # Build script
└── run.bat                         # Run script
```

## Features Implemented

✅ **Modern UI Design**: Clean, responsive interface similar to popular weather apps
✅ **File-based Storage**: JSON-based local storage instead of database
✅ **OpenWeatherMap Integration**: Real-time weather data
✅ **Hourly Forecast**: 24-hour weather predictions
✅ **Weather Details**: Comprehensive metrics (sunrise/sunset, pressure, wind, etc.)
✅ **Temperature Units**: Celsius/Fahrenheit toggle
✅ **City Search**: Search any city worldwide
✅ **Error Handling**: User-friendly error messages
✅ **Loading States**: Visual feedback during API calls

## API Configuration

The app uses OpenWeatherMap API with a pre-configured API key. To use your own:

1. Sign up at: https://openweathermap.org/api
2. Get your API key
3. Replace the key in `WeatherAPIService.java` line 12

## Troubleshooting

### Maven not found:
- Install Maven and add to PATH
- Or use IDE with built-in Maven support

### JavaFX modules not found:
- Download JavaFX SDK
- Set JAVAFX_HOME environment variable
- Or use IDE with JavaFX support

### Compilation errors:
- Ensure Java 17+ is installed
- Check all dependencies in pom.xml
- Verify module-info.java configuration

## Next Steps

Once the environment is set up:

1. Run `mvn clean compile` to build
2. Run `mvn javafx:run` to start the app
3. Search for a city (e.g., "Madrid", "London", "New York")
4. Explore the weather details and hourly forecast

The app will automatically cache weather data locally for offline access. 