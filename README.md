# Just Weather - Modern Weather Application

A beautiful, modern weather application built with JavaFX that provides real-time weather information for any city in the world.

## Features

- **Modern UI**: Clean, responsive design with gradient backgrounds and smooth animations
- **Real-time Weather**: Current weather conditions with detailed information
- **Hourly Forecast**: 24-hour weather forecast with temperature and conditions
- **Weather Details**: Comprehensive weather metrics including:
  - Sunrise and sunset times
  - Chance of rain
  - Atmospheric pressure
  - Wind speed and direction
  - UV index
  - Feels like temperature
  - Visibility
- **Air Quality**: Air pollution data and quality index
- **File-based Storage**: Local caching of weather data for offline access
- **Temperature Units**: Toggle between Celsius and Fahrenheit
- **City Search**: Search for any city worldwide

## Screenshots

The application features a modern interface similar to popular weather apps with:
- Header with logo, search bar, and temperature unit toggle
- Current weather display with large temperature and weather icon
- Horizontal scrolling hourly forecast
- Grid layout of weather details with icons

## Technologies Used

- **JavaFX 17**: Modern UI framework
- **OpenWeatherMap API**: Weather data provider
- **Gson**: JSON parsing
- **Maven**: Build and dependency management
- **CSS**: Custom styling for modern appearance

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Internet connection for API calls

## Installation & Running

### Option 1: Using Maven (Recommended)

1. Navigate to the `src` directory:
   ```bash
   cd src
   ```

2. Build the project:
   ```bash
   mvn clean compile
   ```

3. Run the application:
   ```bash
   mvn javafx:run
   ```

### Option 2: Using the build script (Windows)

1. Double-click `build.bat` or run it from command prompt
2. The script will automatically build and run the application

### Option 3: Using IDE

1. Import the project as a Maven project in your IDE
2. Run the `WeatherApplication` class

## API Configuration

The application uses the OpenWeatherMap API. The API key is already configured in the code. If you need to use your own API key:

1. Sign up at [OpenWeatherMap](https://openweathermap.org/api)
2. Get your API key
3. Replace the API key in `WeatherAPIService.java` line 12

## Project Structure

```
src/
├── com/weatherapp/
│   ├── WeatherApplication.java      # Main application class
│   ├── WeatherAppController.java   # UI controller
│   ├── WeatherData.java           # Data models
│   ├── WeatherAPIService.java     # API service
│   └── FileStorageService.java    # File storage service
├── fxml/
│   └── WeatherApp.fxml           # UI layout
├── weather-app.css               # Stylesheet
├── pom.xml                      # Maven configuration
└── module-info.java             # Module configuration
```

## Data Storage

The application uses file-based storage instead of a database:
- Weather data is cached locally in JSON format
- Saved locations are stored for quick access
- Data is stored in the `weather_data/` directory

## Features Implemented

✅ **Current Weather Display**: Shows temperature, conditions, and city name
✅ **Hourly Forecast**: 24-hour weather predictions
✅ **Weather Details**: Comprehensive weather metrics
✅ **Air Quality Data**: Pollution information
✅ **File-based Storage**: Local data caching
✅ **Temperature Unit Toggle**: Celsius/Fahrenheit conversion
✅ **City Search**: Search any city worldwide
✅ **Modern UI**: Clean, responsive design
✅ **Error Handling**: User-friendly error messages
✅ **Loading States**: Visual feedback during API calls

## API Endpoints Used

- **Current Weather**: `https://api.openweathermap.org/data/2.5/weather`
- **5-day Forecast**: `https://api.openweathermap.org/data/2.5/forecast`
- **Air Pollution**: `https://api.openweathermap.org/data/2.5/air_pollution`
- **Geocoding**: `http://api.openweathermap.org/geo/1.0/direct`

## Contributing

Feel free to contribute to this project by:
- Reporting bugs
- Suggesting new features
- Improving the UI/UX
- Adding new weather data sources

## License

This project is open source and available under the MIT License.