#!/bin/bash

# Simple Weather App Builder (Console Version)
echo "🌤️ Building Weather App (Console Version)..."

# Create target directory
mkdir -p target/classes
mkdir -p target/lib

# Download JSON dependency
JSON_JAR="target/lib/json-20230227.jar"
if [ ! -f "$JSON_JAR" ]; then
    echo "📦 Downloading JSON library..."
    curl -L "https://repo1.maven.org/maven2/org/json/json/20230227/json-20230227.jar" -o "$JSON_JAR"
fi

echo "🔨 Compiling core classes..."

# Compile WeatherData and WeatherService only (without JavaFX dependencies)
javac -cp "$JSON_JAR" -d target/classes \
    src/main/java/com/weatherapp/WeatherData.java \
    src/main/java/com/weatherapp/WeatherService.java

if [ $? -eq 0 ]; then
    echo "✅ Core classes compiled successfully!"
    echo ""
    echo "📋 Available classes:"
    echo "  - WeatherData: Data model for weather information"
    echo "  - WeatherService: API service for fetching weather data"
    echo ""
    echo "🎯 To create a console test application, you can create a simple main class"
    echo "   that uses WeatherService to fetch and display weather data."
    echo ""
    echo "🚀 For the full GUI application, ensure JavaFX is installed and use:"
    echo "   ./run.sh"
else
    echo "❌ Compilation failed!"
    exit 1
fi