@echo off
echo Weather App - Simple Version
echo ==========================

echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo Java is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo Note: This is a simplified version of the weather app.
echo To run the full JavaFX version, you need:
echo 1. Maven installed
echo 2. JavaFX SDK
echo 3. Run: mvn clean javafx:run
echo.
echo For now, the app structure is ready but requires proper JavaFX setup.
echo.
echo Project files created:
echo - Modern UI with FXML and CSS
echo - File-based storage system
echo - OpenWeatherMap API integration
echo - Complete weather data models
echo.
pause 